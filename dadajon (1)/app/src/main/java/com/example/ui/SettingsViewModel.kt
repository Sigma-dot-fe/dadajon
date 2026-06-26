package com.example.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.DadajonApp
import com.example.data.SettingsEntity
import com.example.data.SettingsRepository
import com.example.music.AmbientMusicPlayer
import com.example.notifications.NotificationScheduler
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repository: SettingsRepository,
    private val context: Context
) : ViewModel() {

    val settings: StateFlow<SettingsEntity> = repository.settingsFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SettingsEntity()
        )

    fun updateBirthday(day: Int, month: Int) {
        viewModelScope.launch {
            val current = settings.value
            val updated = current.copy(birthdayDay = day, birthdayMonth = month)
            repository.saveSettings(updated)
            NotificationScheduler.scheduleAlarms(context)
        }
    }

    fun updateAnniversary(day: Int, month: Int) {
        viewModelScope.launch {
            val current = settings.value
            val updated = current.copy(anniversaryDay = day, anniversaryMonth = month)
            repository.saveSettings(updated)
            NotificationScheduler.scheduleAlarms(context)
        }
    }

    fun toggleMusic(enabled: Boolean) {
        viewModelScope.launch {
            val current = settings.value
            val updated = current.copy(isMusicEnabled = enabled)
            repository.saveSettings(updated)
            
            if (enabled) {
                AmbientMusicPlayer.start()
            } else {
                AmbientMusicPlayer.stop()
            }
        }
    }

    fun toggleNotifications(enabled: Boolean) {
        viewModelScope.launch {
            val current = settings.value
            val updated = current.copy(isNotificationEnabled = enabled)
            repository.saveSettings(updated)
            
            if (enabled) {
                NotificationScheduler.scheduleAlarms(context)
            } else {
                NotificationScheduler.cancelAlarms(context)
            }
        }
    }
}

class SettingsViewModelFactory(
    private val repository: SettingsRepository,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(repository, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
