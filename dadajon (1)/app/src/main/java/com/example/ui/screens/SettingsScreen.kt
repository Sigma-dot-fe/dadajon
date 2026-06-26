package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SettingsEntity
import com.example.ui.components.GlassCard

@Composable
fun SettingsScreen(
    settings: SettingsEntity,
    onUpdateBirthday: (Int, Int) -> Unit,
    onUpdateAnniversary: (Int, Int) -> Unit,
    onToggleMusic: (Boolean) -> Unit,
    onToggleNotifications: (Boolean) -> Unit
) {
    val scrollState = rememberScrollState()

    val monthNames = listOf(
        "Yanvar", "Fevral", "Mart", "Aprel", "May", "Iyun",
        "Iyil", "Avgust", "Sentyabr", "Oktyabr", "Noyabr", "Dekabr"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Sozlamalar",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF8D493A),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // 1. Birthday Settings Card
        SettingsSectionCard(title = "Tug'ilgan kun sanasi") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Day selector
                NumberSelector(
                    label = "Kun",
                    value = settings.birthdayDay,
                    range = 1..31,
                    onValueChange = { onUpdateBirthday(it, settings.birthdayMonth) }
                )

                // Month selector
                MonthSelector(
                    label = "Oy",
                    value = settings.birthdayMonth,
                    monthNames = monthNames,
                    onValueChange = { onUpdateBirthday(settings.birthdayDay, it) }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 2. Anniversary Settings Card
        SettingsSectionCard(title = "Nikoh yilligi sanasi") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Day selector
                NumberSelector(
                    label = "Kun",
                    value = settings.anniversaryDay,
                    range = 1..31,
                    onValueChange = { onUpdateAnniversary(it, settings.anniversaryMonth) }
                )

                // Month selector
                MonthSelector(
                    label = "Oy",
                    value = settings.anniversaryMonth,
                    monthNames = monthNames,
                    onValueChange = { onUpdateAnniversary(settings.anniversaryDay, it) }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 3. System Switches Card
        SettingsSectionCard(title = "Tizim funksiyalari") {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Music Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Musiqa ijrosi", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF3E2723))
                        Text(text = "Ilovada tinchlantiruvchi musiqa", fontSize = 13.sp, color = Color(0xFF5D4037).copy(alpha = 0.7f))
                    }
                    Switch(
                        checked = settings.isMusicEnabled,
                        onCheckedChange = onToggleMusic,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color(0xFF8D493A),
                            checkedTrackColor = Color(0xFF8D493A).copy(alpha = 0.5f),
                            uncheckedThumbColor = Color(0xFFD0B8A8),
                            uncheckedTrackColor = Color(0xFFD0B8A8).copy(alpha = 0.3f)
                        )
                    )
                }

                HorizontalDivider(color = Color(0xFF8D493A).copy(alpha = 0.15f), thickness = 1.dp)

                // Notifications Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Eslatmalar", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF3E2723))
                        Text(text = "Tug'ilgan kun va yillik xabarlari", fontSize = 13.sp, color = Color(0xFF5D4037).copy(alpha = 0.7f))
                    }
                    Switch(
                        checked = settings.isNotificationEnabled,
                        onCheckedChange = onToggleNotifications,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color(0xFF8D493A),
                            checkedTrackColor = Color(0xFF8D493A).copy(alpha = 0.5f),
                            uncheckedThumbColor = Color(0xFFD0B8A8),
                            uncheckedTrackColor = Color(0xFFD0B8A8).copy(alpha = 0.3f)
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun SettingsSectionCard(title: String, content: @Composable () -> Unit) {
    GlassCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF8D493A),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            content()
        }
    }
}

@Composable
fun NumberSelector(
    label: String,
    value: Int,
    range: IntRange,
    onValueChange: (Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(100.dp)
    ) {
        Text(text = label, fontSize = 13.sp, color = Color(0xFF5D4037).copy(alpha = 0.7f))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = { if (value > range.first) onValueChange(value - 1) },
                colors = IconButtonDefaults.iconButtonColors(contentColor = Color(0xFF8D493A))
            ) {
                Icon(imageVector = Icons.Default.Remove, contentDescription = "Kamaytirish")
            }
            Text(
                text = value.toString(),
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF3E2723),
                modifier = Modifier.width(32.dp),
                textAlign = TextAlign.Center
            )
            IconButton(
                onClick = { if (value < range.last) onValueChange(value + 1) },
                colors = IconButtonDefaults.iconButtonColors(contentColor = Color(0xFF8D493A))
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Ko'paytirish")
            }
        }
    }
}

@Composable
fun MonthSelector(
    label: String,
    value: Int,
    monthNames: List<String>,
    onValueChange: (Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(140.dp)
    ) {
        Text(text = label, fontSize = 13.sp, color = Color(0xFF5D4037).copy(alpha = 0.7f))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = { if (value > 1) onValueChange(value - 1) },
                colors = IconButtonDefaults.iconButtonColors(contentColor = Color(0xFF8D493A))
            ) {
                Icon(imageVector = Icons.Default.Remove, contentDescription = "Kamaytirish")
            }
            Text(
                text = monthNames[value - 1],
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF3E2723),
                modifier = Modifier.width(68.dp),
                textAlign = TextAlign.Center
            )
            IconButton(
                onClick = { if (value < 12) onValueChange(value + 1) },
                colors = IconButtonDefaults.iconButtonColors(contentColor = Color(0xFF8D493A))
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Ko'paytirish")
            }
        }
    }
}
