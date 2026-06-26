package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_settings")
data class SettingsEntity(
    @PrimaryKey val id: Int = 0,
    val birthdayMonth: Int = 11, // November (1-12)
    val birthdayDay: Int = 22,
    val anniversaryMonth: Int = 6, // June (1-12)
    val anniversaryDay: Int = 26,
    val isMusicEnabled: Boolean = false,
    val isNotificationEnabled: Boolean = true
)
