package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.*
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SettingsEntity
import com.example.ui.components.GlassCard
import kotlinx.coroutines.delay
import java.util.Calendar

data class CountdownState(
    val days: Long = 0,
    val hours: Long = 0,
    val minutes: Long = 0,
    val seconds: Long = 0,
    val isToday: Boolean = false
)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TimerScreen(
    settings: SettingsEntity,
    onNavigateToGreeting: (Int) -> Unit
) {
    val scrollState = rememberScrollState()

    var birthdayCountdown by remember { mutableStateOf(CountdownState()) }
    var anniversaryCountdown by remember { mutableStateOf(CountdownState()) }

    // Live update loop
    LaunchedEffect(settings) {
        while (true) {
            birthdayCountdown = calculateCountdown(settings.birthdayMonth, settings.birthdayDay)
            anniversaryCountdown = calculateCountdown(settings.anniversaryMonth, settings.anniversaryDay)
            delay(1000)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Hisoblagich",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF8D493A),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // 1. Birthday Countdown Card
        CountdownCard(
            title = "Dadajonning Tug'ilgan kunlari",
            subtitle = "22-Noyabr",
            countdown = birthdayCountdown,
            eventColor = Color(0xFF8D493A),
            onCelebrateClick = { onNavigateToGreeting(1) }
        )

        Spacer(modifier = Modifier.height(28.dp))

        // 2. Anniversary Countdown Card
        CountdownCard(
            title = "Dadajon va Onajonning Nikoh yilliklari",
            subtitle = "26-Iyun",
            countdown = anniversaryCountdown,
            eventColor = Color(0xFFB17A50),
            onCelebrateClick = { onNavigateToGreeting(2) }
        )

        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun CountdownCard(
    title: String,
    subtitle: String,
    countdown: CountdownState,
    eventColor: Color,
    onCelebrateClick: () -> Unit
) {
    val isNear = !countdown.isToday && countdown.days < 10

    val infiniteTransition = rememberInfiniteTransition(label = "pulseTransition")
    
    val pulseAlpha by if (isNear) {
        infiniteTransition.animateFloat(
            initialValue = 0.3f,
            targetValue = 0.9f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulseAlpha"
        )
    } else {
        remember { mutableStateOf(1f) }
    }

    val pulseScale by if (isNear) {
        infiniteTransition.animateFloat(
            initialValue = 0.97f,
            targetValue = 1.03f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulseScale"
        )
    } else {
        remember { mutableStateOf(1f) }
    }

    val cardBorderModifier = if (isNear) {
        Modifier.border(
            width = 2.dp,
            color = eventColor.copy(alpha = pulseAlpha),
            shape = RoundedCornerShape(24.dp)
        )
    } else {
        Modifier
    }

    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .then(cardBorderModifier)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF3E2723),
                textAlign = TextAlign.Center
            )
            Text(
                text = subtitle,
                fontSize = 14.sp,
                color = eventColor,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
            )

            if (isNear) {
                Box(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .scale(pulseScale)
                        .clip(RoundedCornerShape(8.dp))
                        .background(eventColor.copy(alpha = 0.12f))
                        .border(1.dp, eventColor.copy(alpha = pulseAlpha), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "⏳ Yaqin qoldi! (${countdown.days} kun qoldi)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = eventColor,
                        letterSpacing = 0.5.sp
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(6.dp))
            }

            if (countdown.isToday) {
                // Celebration button shown when today is the day!
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(vertical = 12.dp)
                ) {
                    Text(
                        text = "Bugun o'sha bayram kuni! 🎉",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = eventColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    ElevatedButton(
                        onClick = onCelebrateClick,
                        colors = ButtonDefaults.elevatedButtonColors(
                            containerColor = eventColor,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "Tabrikni ochish 🎁",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            } else {
                // Show countdown row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    CountdownUnit(value = countdown.days, label = "Kun", accentColor = eventColor, isUrgent = isNear)
                    CountdownUnit(value = countdown.hours, label = "Soat", accentColor = eventColor, isUrgent = false)
                    CountdownUnit(value = countdown.minutes, label = "Daqiqa", accentColor = eventColor, isUrgent = false)
                    CountdownUnit(value = countdown.seconds, label = "Soniya", accentColor = eventColor, isUrgent = false)
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Allow user to check the greetings animation anytime!
                ElevatedButton(
                    onClick = onCelebrateClick,
                    colors = ButtonDefaults.elevatedButtonColors(
                        containerColor = eventColor.copy(alpha = 0.12f),
                        contentColor = eventColor
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Tabrik kartasini sinash 🌸", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun CountdownUnit(value: Long, label: String, accentColor: Color, isUrgent: Boolean = false) {
    val infiniteTransition = rememberInfiniteTransition(label = "unitPulse")
    
    val pulseScale by if (isUrgent) {
        infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.08f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "unitScale"
        )
    } else {
        remember { mutableStateOf(1f) }
    }

    val pulseAlpha by if (isUrgent) {
        infiniteTransition.animateFloat(
            initialValue = 0.3f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "unitAlpha"
        )
    } else {
        remember { mutableStateOf(0.25f) }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(64.dp)
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .scale(pulseScale)
                .clip(RoundedCornerShape(14.dp))
                .background(
                    if (isUrgent) {
                        accentColor.copy(alpha = 0.12f)
                    } else {
                        Color.White.copy(alpha = 0.5f)
                    }
                )
                .border(
                    width = if (isUrgent) 2.dp else 1.dp,
                    color = if (isUrgent) accentColor.copy(alpha = pulseAlpha) else accentColor.copy(alpha = 0.25f),
                    shape = RoundedCornerShape(14.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = String.format("%02d", value),
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (isUrgent) accentColor else Color(0xFF3E2723)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isUrgent) FontWeight.Bold else FontWeight.Medium,
            color = if (isUrgent) accentColor else Color(0xFF5D4037).copy(alpha = 0.8f)
        )
    }
}

fun calculateCountdown(month: Int, day: Int): CountdownState {
    val now = Calendar.getInstance()
    val event = Calendar.getInstance().apply {
        set(Calendar.MONTH, month - 1)
        set(Calendar.DAY_OF_MONTH, day)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    val isSameDay = now.get(Calendar.MONTH) == event.get(Calendar.MONTH) &&
            now.get(Calendar.DAY_OF_MONTH) == event.get(Calendar.DAY_OF_MONTH)

    if (isSameDay) {
        return CountdownState(isToday = true)
    }

    if (event.before(now)) {
        event.add(Calendar.YEAR, 1)
    }

    val diffMs = event.timeInMillis - now.timeInMillis
    val days = diffMs / (1000 * 60 * 60 * 24)
    val hours = (diffMs / (1000 * 60 * 60)) % 24
    val minutes = (diffMs / (1000 * 60)) % 60
    val seconds = (diffMs / 1000) % 60

    return CountdownState(days, hours, minutes, seconds)
}
