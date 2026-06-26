package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepository(private val settingsDao: SettingsDao) {
    val settingsFlow: Flow<SettingsEntity> = settingsDao.getSettingsFlow().map { entity ->
        entity ?: SettingsEntity()
    }

    suspend fun getSettingsDirect(): SettingsEntity {
        return settingsDao.getSettingsDirect() ?: SettingsEntity()
    }

    suspend fun saveSettings(settings: SettingsEntity) {
        settingsDao.saveSettings(settings)
    }
}
