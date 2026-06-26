package com.example.music

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.math.sin

object AmbientMusicPlayer {
    private const val FILE_NAME = "ambient_music_loop.wav"
    private var appContext: Context? = null
    private var mediaPlayer: MediaPlayer? = null
    private val scope = CoroutineScope(Dispatchers.IO)
    private var isGenerating = false

    @Volatile
    var isPlaying = false
        private set

    fun initialize(context: Context) {
        appContext = context.applicationContext
        scope.launch {
            try {
                val file = File(context.cacheDir, FILE_NAME)
                if (!file.exists() || file.length() < 100000) {
                    synchronized(this) {
                        if (isGenerating) return@launch
                        isGenerating = true
                    }
                    generateAmbientWav(file)
                    synchronized(this) {
                        isGenerating = false
                    }
                }
            } catch (e: Exception) {
                Log.e("AmbientMusicPlayer", "Failed to pre-generate ambient sound", e)
            }
        }
    }

    fun start() {
        if (isPlaying) return
        isPlaying = true

        val context = appContext
        if (context == null) {
            Log.e("AmbientMusicPlayer", "Cannot start. AmbientMusicPlayer not initialized with Context yet!")
            isPlaying = false
            return
        }

        scope.launch {
            try {
                val file = File(context.cacheDir, FILE_NAME)
                if (!file.exists() || file.length() < 100000) {
                    generateAmbientWav(file)
                }

                if (!isPlaying) return@launch // Stopped during generation

                withContext(Dispatchers.Main) {
                    try {
                        mediaPlayer?.release()
                        mediaPlayer = MediaPlayer().apply {
                            setDataSource(context, android.net.Uri.fromFile(file))
                            isLooping = true
                            prepare()
                            start()
                        }
                        Log.d("AmbientMusicPlayer", "Started MediaPlayer playing pre-generated ambient loop WAV")
                    } catch (e: Exception) {
                        Log.e("AmbientMusicPlayer", "Failed to start MediaPlayer playback", e)
                        isPlaying = false
                    }
                }
            } catch (e: Exception) {
                Log.e("AmbientMusicPlayer", "Failed to play music", e)
                isPlaying = false
            }
        }
    }

    fun stop() {
        isPlaying = false
        scope.launch(Dispatchers.Main) {
            try {
                mediaPlayer?.apply {
                    if (isPlaying) {
                        stop()
                    }
                    release()
                }
            } catch (e: Exception) {
                // ignore
            }
            mediaPlayer = null
            Log.d("AmbientMusicPlayer", "Stopped and released MediaPlayer")
        }
    }

    private fun generateAmbientWav(outputFile: File) {
        val tempFile = File(outputFile.absolutePath + ".tmp")
        try {
            val sampleRate = 22050
            val durationSeconds = 48.0 // 4 chords * 12 seconds each
            val numSamples = (sampleRate * durationSeconds).toInt()
            val buffer = ShortArray(numSamples)

            val progression = listOf(
                listOf(261.63, 329.63, 392.00, 493.88), // Cmaj7
                listOf(349.23, 440.00, 523.25, 659.25), // Fmaj7
                listOf(392.00, 493.88, 587.33, 659.25), // G6
                listOf(440.00, 523.25, 659.25, 783.99)  // Am7
            )

            // Synthesize each chord
            val chordDuration = 12.0
            val chordSamples = (sampleRate * chordDuration).toInt()

            for (chordIdx in progression.indices) {
                val frequencies = progression[chordIdx]
                val chordStartSample = chordIdx * chordSamples

                for (noteIdx in frequencies.indices) {
                    val freq = frequencies[noteIdx]
                    val noteStartDelay = noteIdx * 0.5 // stagger by 0.5s
                    val noteStartSample = chordStartSample + (noteStartDelay * sampleRate).toInt()
                    
                    val noteDuration = 9.0 // let notes ring for 9 seconds
                    val noteSamples = (noteDuration * sampleRate).toInt()

                    val angleIncrement = (2.0 * Math.PI * freq) / sampleRate

                    for (i in 0 until noteSamples) {
                        val targetSampleIdx = noteStartSample + i
                        if (targetSampleIdx >= numSamples) break

                        val t = i.toDouble() / sampleRate
                        
                        // Pure sine + soft electric piano bell harmonic
                        val sine = sin(i * angleIncrement)
                        val harmonic = 0.15 * sin(2 * i * angleIncrement)
                        val wave = sine + harmonic

                        // Attack envelope to prevent clicks (0.1s attack)
                        val attackSamples = (0.1 * sampleRate).toInt()
                        val attack = if (i < attackSamples) (i.toDouble() / attackSamples) else 1.0

                        // Decay envelope: slow natural fade
                        val decay = Math.exp(-0.7 * t)

                        val volume = 0.05f // Low soft background volume

                        val sampleVal = (wave * attack * decay * volume * 32767.0).toInt()
                        
                        // Add to existing samples in the buffer (chords overlap)
                        val existingVal = buffer[targetSampleIdx].toInt()
                        val newVal = (existingVal + sampleVal).coerceIn(-32768, 32767)
                        buffer[targetSampleIdx] = newVal.toShort()
                    }
                }
            }

            // Write WAV header and data to file
            tempFile.outputStream().use { out ->
                val totalDataLen = numSamples * 2
                val totalAudioLen = totalDataLen + 36

                // RIFF header
                out.write("RIFF".toByteArray())
                out.write(byteArrayOf(
                    (totalAudioLen and 0xff).toByte(),
                    ((totalAudioLen shr 8) and 0xff).toByte(),
                    ((totalAudioLen shr 16) and 0xff).toByte(),
                    ((totalAudioLen shr 24) and 0xff).toByte()
                ))
                out.write("WAVE".toByteArray())

                // Format chunk
                out.write("fmt ".toByteArray())
                out.write(byteArrayOf(16, 0, 0, 0)) // subchunk1size (16 for PCM)
                out.write(byteArrayOf(1, 0)) // audio format (1 for PCM)
                out.write(byteArrayOf(1, 0)) // channels (1 for Mono)
                
                // Sample rate
                out.write(byteArrayOf(
                    (sampleRate and 0xff).toByte(),
                    ((sampleRate shr 8) and 0xff).toByte(),
                    ((sampleRate shr 16) and 0xff).toByte(),
                    ((sampleRate shr 24) and 0xff).toByte()
                ))

                // Byte rate
                val byteRate = sampleRate * 1 * 16 / 8
                out.write(byteArrayOf(
                    (byteRate and 0xff).toByte(),
                    ((byteRate shr 8) and 0xff).toByte(),
                    ((byteRate shr 16) and 0xff).toByte(),
                    ((byteRate shr 24) and 0xff).toByte()
                ))

                out.write(byteArrayOf(2, 0)) // block align
                out.write(byteArrayOf(16, 0)) // bits per sample (16)

                // Data chunk
                out.write("data".toByteArray())
                out.write(byteArrayOf(
                    (totalDataLen and 0xff).toByte(),
                    ((totalDataLen shr 8) and 0xff).toByte(),
                    ((totalDataLen shr 16) and 0xff).toByte(),
                    ((totalDataLen shr 24) and 0xff).toByte()
                ))

                // Short buffer to bytes
                val byteBuffer = ByteArray(totalDataLen)
                for (i in buffer.indices) {
                    val s = buffer[i].toInt()
                    byteBuffer[i * 2] = (s and 0xff).toByte()
                    byteBuffer[i * 2 + 1] = ((s shr 8) and 0xff).toByte()
                }
                out.write(byteBuffer)
            }

            // Rename to final output file
            if (tempFile.exists()) {
                if (outputFile.exists()) {
                    outputFile.delete()
                }
                tempFile.renameTo(outputFile)
                Log.d("AmbientMusicPlayer", "Successfully generated and cached ambient WAV file")
            }
        } catch (e: Exception) {
            Log.e("AmbientMusicPlayer", "Error generating ambient WAV", e)
            try {
                if (tempFile.exists()) tempFile.delete()
            } catch (ex: Exception) { /* ignore */ }
        }
    }
}
