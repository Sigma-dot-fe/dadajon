package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.MusicOff
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.SettingsEntity
import com.example.ui.components.GlassCard
import java.util.Calendar

@Composable
fun HomeScreen(
    settings: SettingsEntity,
    onToggleMusic: (Boolean) -> Unit,
    onNavigateToGreeting: () -> Unit
) {
    val scrollState = rememberScrollState()

    // Soft pulsating animation for the family photo to make the UI feel alive
    val infiniteTransition = rememberInfiniteTransition(label = "pulsate")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "photoScale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App Title
        Text(
            text = "Dadajonga Sovg'a",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF8D493A),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Decorative Heartwarming Greeting Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp)
                .scale(scale)
                .clip(RoundedCornerShape(32.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF8D493A),
                            Color(0xFF5D4037)
                        )
                    )
                )
                .border(
                    width = 2.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.5f),
                            Color.Transparent
                        )
                    ),
                    shape = RoundedCornerShape(32.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "❤️",
                    fontSize = 64.sp,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Text(
                    text = "BIZNING SEVIMLI",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF1E0D6).copy(alpha = 0.8f),
                    letterSpacing = 4.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "DADAJONIMIZ",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFFFDF8F6),
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Text(
                    text = "Sizni cheksiz yaxshi ko'ramiz",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFF1E0D6).copy(alpha = 0.7f),
                    letterSpacing = 1.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Daily Quote Card
        val context = LocalContext.current
        val dayOfYear = remember { Calendar.getInstance().get(Calendar.DAY_OF_YEAR) }
        val initialQuoteIndex = dayOfYear % ParentQuotes.size
        var currentQuoteIndex by remember { mutableStateOf(initialQuoteIndex) }
        val currentQuoteText = ParentQuotes[currentQuoteIndex]

        GlassCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "✨",
                            fontSize = 18.sp,
                            modifier = Modifier.padding(end = 6.dp)
                        )
                        Text(
                            text = "Kun hikmati",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF8D493A)
                        )
                    }
                    
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF8D493A).copy(alpha = 0.1f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Bugungi",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF8D493A)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "“$currentQuoteText”",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF3E2723),
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Copy button
                    IconButton(
                        onClick = {
                            val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Parent Quote", currentQuoteText)
                            clipboardManager.setPrimaryClip(clip)
                            Toast.makeText(context, "Nusxalandi! 📋", Toast.LENGTH_SHORT).show()
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color(0xFF8D493A).copy(alpha = 0.08f),
                            contentColor = Color(0xFF8D493A)
                        ),
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("copy_quote_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Nusxa olish",
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Refresh / Shuffle button
                    IconButton(
                        onClick = {
                            var nextIndex = currentQuoteIndex
                            while (nextIndex == currentQuoteIndex && ParentQuotes.size > 1) {
                                nextIndex = (0 until ParentQuotes.size).random()
                            }
                            currentQuoteIndex = nextIndex
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color(0xFF8D493A).copy(alpha = 0.08f),
                            contentColor = Color(0xFF8D493A)
                        ),
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("refresh_quote_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Boshqa hikmat",
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    // Share button
                    IconButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, currentQuoteText)
                            }
                            context.startActivity(Intent.createChooser(intent, "Ulashish"))
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color(0xFF8D493A).copy(alpha = 0.08f),
                            contentColor = Color(0xFF8D493A)
                        ),
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("share_quote_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Ulashish",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Heartwarming Message Card
        GlassCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Aziz Dadajon,",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8D493A), // Elegant terracotta
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                Text(
                    text = "Siz mening hayotimdagi eng buyuk insonlardan birisiz.\n\nSizning mehringiz va qo'llab-quvvatlashingiz uchun rahmat.\n\nMen Sizni juda yaxshi ko'raman.",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF3E2723), // Deep brown
                    lineHeight = 28.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Music Control Section
        GlassCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Orqa fon musiqasi",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5D4037)
                    )
                    Text(
                        text = if (settings.isMusicEnabled) "Hozir ijro etilmoqda" else "O'chirilgan",
                        fontSize = 13.sp,
                        color = Color(0xFF5D4037).copy(alpha = 0.7f)
                    )
                }

                ElevatedButton(
                    onClick = { onToggleMusic(!settings.isMusicEnabled) },
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = if (settings.isMusicEnabled) Color(0xFF8D493A) else Color(0xFF8D493A).copy(alpha = 0.15f),
                        contentColor = if (settings.isMusicEnabled) Color.White else Color(0xFF8D493A)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Icon(
                        imageVector = if (settings.isMusicEnabled) Icons.Default.MusicNote else Icons.Default.MusicOff,
                        contentDescription = "Musiqa boshqaruvi"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (settings.isMusicEnabled) "O'chirish" else "Yoqish",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Interactive Greeting Button
        ElevatedButton(
            onClick = onNavigateToGreeting,
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = Color(0xFF8D493A),
                contentColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                text = "Tabrik Nomani ko'rish ❤️",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(100.dp)) // Extra space for bottom bar padding
    }
}

private val ParentQuotes = listOf(
    "Ota - hayot bog'ining eng mustahkam ustuni, ona esa shu bog'ning eng go'zal gulidir. Biz siz bilan baxtlimiz!",
    "Dunyoning barcha boyliklari ota-onaning bir lahzalik shirin tabassumi va mamnunligiga teng kela olmaydi.",
    "Otaning duosi qut-baraka eshigi, onaning duosi esa hayotimizning eng ishonchli qalqonidir.",
    "Dunyoga kelib ko'rgan eng go'zal baxtim va mukofotim - mening mehribon ota-onamdir. Borligingizga shukr!",
    "Ota duosini olgan inson hech qachon yo'lda qolmaydi, ona duosini olgan inson esa g'am nimaligini bilmaydi.",
    "Sizlarning bergan tarbiyangiz va yo'l-yo'riqlaringiz hayotdagi eng mustahkam poydevorimdir.",
    "Har kuni tongda sizlarning sog'-salomat, tabassum qilib turganingizni ko'rish men uchun eng katta bayramdir.",
    "Otaning qo'llari bizni har qanday bo'ronlardan himoya qilsa, onaning mehri qalbimizni yoritadi.",
    "Sizlar mening g'ururim, faxrim va hayotimdagi eng yorug' mayoqlarimsizlar. Sizlarni yaxshi ko'raman!",
    "Ota - oilaning soyaboni, qanchalik issiq bo'lmasin, uning soyasida har doim osoyishtalik hukm suradi.",
    "Onaning mehri cheksiz ummon bo'lsa, otaning sabri tog'dek ulug'vordir. Sizlarsiz hayotim kemasiz dengizdir.",
    "Dunyodagi eng tinch, xavfsiz va samimiy boshpana - bu ota-onaning bag'ridir.",
    "Mening muvaffaqiyatlarim, erishgan yutuqlarim ortida sizning uyqusiz tunlaringiz va cheksiz mehnatlaringiz yotibdi.",
    "Ota-ona qadri oltindan qimmat, ularning xizmatida bo'lish esa har bir farzand uchun oliy saodatdir.",
    "Sizlarning duolaringiz tufayli hayotim fayzli va yo'llarim doimo ochiq. Mehringiz uchun rahmat!",
    "Farzand uchun ota-onadan ko'ra yaqinroq, sirdoshroq va mehribonroq do'st topilmaydi.",
    "Ota - g'urur va jasorat timsoli, ona - shafqat va go'zallik manbai. Borligingiz qalbimizga quvonch bag'ishlaydi.",
    "Hayot tashvishlaridan charchagan damda, sizning bitta issiq so'zingiz va mehringiz barcha charchoqlarimni aritadi.",
    "Sizning ko'zingizdagi quvonch va baxt - hayotda intiladigan eng oliy maqsadimdir, azizlarim.",
    "Qalbi daryo, mehri quyosh ota-onam, sizlarga bo'lgan hurmatim va muhabbatim cheksizdir.",
    "Oila - muqaddas maskan, ota-ona esa uning farishtalaridir. Sizlar bor bu hayot go'zal va mazmunlidir.",
    "Bizga bergan har bir shirin so'zingiz, qilgan har bir mehribonligingiz qalbimiz xazinasidir.",
    "Yillar o'tsa hamki, sizning ko'zingizda o'sha samimiy mehr va bizga bo'lgan cheksiz ishonch so'nmaydi.",
    "Sizlardan o'rgangan halollik va mehnatsevarlik hayot yo'limni yoritib turuvchi eng yorqin chiroqdir.",
    "Hayotimizning har bir oni sizning borligingiz bilan shirin va fayzlidir. Umringiz uzoq bo'lsin!",
    "Sizning duolaringiz - mening eng katta kuchim, mehringiz esa mening eng buyuk yupanchimdir.",
    "Ota-onamizning tabassumi oilamizning haqiqiy baxti va xonadonimizning barakasidir.",
    "Qiyinchiliklar ortda qoladi, dunyo tashvishlari o'tadi, ammo sizning pok muhabbatingiz mangu qoladi.",
    "Dunyoning eng chiroyli so'zlari ham sizga bo'lgan minnatdorchiligimni ifodalashga kamlik qiladi.",
    "Sizlar mening hayotimdagi eng go'zal mo'jizasiz. Alloh sizlarni o'z panohida asrasin!",
    "Har bir kuningiz quvonchga, baxtga va sog'likka to'la bo'lsin. Siz mening qalbim tojidirsiz!"
)
