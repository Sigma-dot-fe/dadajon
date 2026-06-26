package com.example

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.data.SettingsEntity
import com.example.data.SettingsRepository
import com.example.music.AmbientMusicPlayer
import com.example.ui.SettingsViewModel
import com.example.ui.SettingsViewModelFactory
import com.example.ui.components.GlassCard
import com.example.ui.screens.GreetingScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.TimerScreen
import com.example.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var repository: SettingsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as DadajonApp
        repository = app.repository

        // Handle initial deep link from system notification
        val showGreetingType = intent.getIntExtra("show_greeting_type", 0)

        setContent {
            MyApplicationTheme {
                var currentTab by rememberSaveable { mutableStateOf("home") }
                var activeGreetingType by remember { mutableStateOf(if (showGreetingType != 0) showGreetingType else null) }

                val viewModel: SettingsViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                    factory = SettingsViewModelFactory(repository, LocalContext.current)
                )

                val settingsState by viewModel.settings.collectAsStateWithLifecycle()

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFFFDF8F6), // Warm cream
                                    Color(0xFFF1E0D6)  // Soft sand
                                )
                            )
                        )
                ) {
                    // Static decorative background elements
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Hearts
                        Text(
                            text = "❤️",
                            fontSize = 24.sp,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(top = 60.dp, end = 40.dp)
                        )
                        Text(
                            text = "❤️",
                            fontSize = 14.sp,
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(top = 180.dp, start = 20.dp)
                        )
                        Text(
                            text = "❤️",
                            fontSize = 20.sp,
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(bottom = 160.dp, end = 32.dp)
                        )
                        
                        // Gold dots
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(top = 100.dp, start = 80.dp)
                                .size(6.dp)
                                .background(Color(0xFFD4AF37).copy(alpha = 0.5f), androidx.compose.foundation.shape.CircleShape)
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(top = 280.dp, end = 100.dp)
                                .size(6.dp)
                                .background(Color(0xFFD4AF37).copy(alpha = 0.5f), androidx.compose.foundation.shape.CircleShape)
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(bottom = 200.dp, start = 40.dp)
                                .size(6.dp)
                                .background(Color(0xFFD4AF37).copy(alpha = 0.5f), androidx.compose.foundation.shape.CircleShape)
                        )
                    }
                    Scaffold(
                        containerColor = Color.Transparent,
                        bottomBar = {
                            if (activeGreetingType == null) {
                                CustomGlassNavigationBar(
                                    currentTab = currentTab,
                                    onTabSelected = { currentTab = it }
                                )
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    top = innerPadding.calculateTopPadding(),
                                    bottom = 0.dp // Custom floating navigation bar handles its own padding
                                )
                        ) {
                            when (currentTab) {
                                "home" -> HomeScreen(
                                    settings = settingsState,
                                    onToggleMusic = { viewModel.toggleMusic(it) },
                                    onNavigateToGreeting = { activeGreetingType = 1 }
                                )
                                "timer" -> TimerScreen(
                                    settings = settingsState,
                                    onNavigateToGreeting = { activeGreetingType = it }
                                )
                                "settings" -> SettingsScreen(
                                    settings = settingsState,
                                    onUpdateBirthday = { d, m -> viewModel.updateBirthday(d, m) },
                                    onUpdateAnniversary = { d, m -> viewModel.updateAnniversary(d, m) },
                                    onToggleMusic = { viewModel.toggleMusic(it) },
                                    onToggleNotifications = { viewModel.toggleNotifications(it) }
                                )
                            }
                        }
                    }

                    // Fullscreen Floating Greeting Overlay
                    AnimatedVisibility(
                        visible = activeGreetingType != null,
                        enter = slideInVertically(initialOffsetY = { it }),
                        exit = slideOutVertically(targetOffsetY = { it })
                    ) {
                        activeGreetingType?.let { type ->
                            GreetingScreen(
                                type = type,
                                onBack = { activeGreetingType = null }
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Resume background music if enabled
        lifecycleScope.launch {
            val settings = repository.getSettingsDirect()
            if (settings.isMusicEnabled) {
                AmbientMusicPlayer.start()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        // Pause synthesized music to save battery
        AmbientMusicPlayer.stop()
    }
}

@Composable
fun CustomGlassNavigationBar(
    currentTab: String,
    onTabSelected: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp),
            cornerRadius = 24.dp
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavBarItem(
                    selected = currentTab == "home",
                    icon = Icons.Default.Home,
                    label = "Bosh sahifa",
                    onClick = { onTabSelected("home") }
                )
                NavBarItem(
                    selected = currentTab == "timer",
                    icon = Icons.Default.CalendarMonth,
                    label = "Hisoblagich",
                    onClick = { onTabSelected("timer") }
                )
                NavBarItem(
                    selected = currentTab == "settings",
                    icon = Icons.Default.Settings,
                    label = "Sozlamalar",
                    onClick = { onTabSelected("settings") }
                )
            }
        }
    }
}

@Composable
fun NavBarItem(
    selected: Boolean,
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val color = if (selected) Color(0xFF8D493A) else Color(0xFF5D4037).copy(alpha = 0.5f)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .width(72.dp)
            .fillMaxHeight()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            color = color,
            fontWeight = if (selected) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Normal
        )
    }
}
