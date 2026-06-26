package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
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
import com.example.ui.components.FestiveParticleOverlay
import com.example.ui.components.GlassCard

@Composable
fun GreetingScreen(
    type: Int, // 1 = Birthday, 2 = Anniversary
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()

    val title: String
    val mainMessage: String
    val cardColor: Color

    if (type == 1) {
        title = "🎉 Tug'ilgan kun muborak!"
        mainMessage = """
            Aziz Dadajon!
            
            Tug'ilgan kuningiz bilan chin yurakdan tabriklayman.
            
            Sizga uzoq umr, mustahkam sog'lik, baxt va omad tilayman.
            
            Har doim baxtli bo'ling.
            
            Sizni juda yaxshi ko'raman.
            
            Hurmat bilan,
            O'g'lingiz Javohir ❤️
        """.trimIndent()
        cardColor = Color(0xFF8D493A) // Terracotta
    } else {
        title = "💍 Nikoh yilligi muborak!"
        mainMessage = """
            Aziz Dadajon va Onajon!
            
            Nikoh yilligingiz muborak bo'lsin.
            
            Sizlarga sog'lik, baxt, muhabbat va uzoq umr tilayman.
            
            Har doim baxtli yashang.
            
            Sizlar bilan faxrlanaman.
            
            O'g'lingiz Javohir ❤️
        """.trimIndent()
        cardColor = Color(0xFFB17A50) // Warm bronze/rose
    }

    // Soft heart pulsation animation
    val infiniteTransition = rememberInfiniteTransition(label = "pulsating_heart")
    val heartScale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOutBack),
            repeatMode = RepeatMode.Reverse
        ),
        label = "heartScale"
    )

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
        // Falling stars & hearts background animation overlay!
        FestiveParticleOverlay(enabled = true)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            // Header with Back Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color(0xFF8D493A).copy(alpha = 0.12f),
                        contentColor = Color(0xFF8D493A)
                    )
                ) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Orqaga")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Tabriknoma",
                    color = Color(0xFF8D493A),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Congratulations Letter
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Heart Pulsation Icon
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Pulsating Heart",
                    tint = cardColor,
                    modifier = Modifier
                        .size(90.dp)
                        .scale(heartScale)
                        .padding(bottom = 16.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 2.dp,
                            brush = Brush.verticalGradient(
                                colors = listOf(cardColor, Color.Transparent)
                            ),
                            shape = RoundedCornerShape(28.dp)
                        ),
                    cornerRadius = 28.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = title,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = cardColor,
                            textAlign = TextAlign.Center,
                            lineHeight = 34.sp
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = mainMessage,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF3E2723), // Deep brown
                            textAlign = TextAlign.Center,
                            lineHeight = 32.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}
