package com.youngsik.jinada.data.datasource.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.youngsik.domain.model.DataResourceResult
import com.youngsik.jinada.data.datasource.DataStoreDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "jinada_settings")
class DataStoreDataSourceImpl(private val context: Context): DataStoreDataSource {
    private object PreferencesKeys {
        val NICKNAME = stringPreferencesKey("nickname")
        val NOTIFICATION_ENABLED = booleanPreferencesKey("notification_enabled")
    }
    override suspend fun setNickname(nickname: String):DataResourceResult<Unit> = runCatching {
        context.dataStore.edit { settings ->
            settings[PreferencesKeys.NICKNAME] = nickname
        }
        DataResourceResult.Success(Unit)
    }.getOrElse { DataResourceResult.Failure(it) }


    override suspend fun setNotificationEnabled(enabled: Boolean): DataResourceResult<Unit> = runCatching {
        context.dataStore.edit { settings ->
            settings[PreferencesKeys.NOTIFICATION_ENABLED] = enabled
        }
        DataResourceResult.Success(Unit)
    }.getOrElse { DataResourceResult.Failure(it) }


    override suspend fun getNickname(): DataResourceResult<String> = runCatching {
        val nicknameFlow: Flow<String> = context.dataStore.data.map { preferences -> preferences[PreferencesKeys.NICKNAME] ?: "" }
        val nickname = nicknameFlow.first()
        DataResourceResult.Success(nickname)
    }.getOrElse { DataResourceResult.Failure(it) }


    override suspend fun getNotificationEnabled(): DataResourceResult<Boolean> = runCatching {
        val notificationEnabledFlow: Flow<Boolean> = context.dataStore.data.map { preferences -> preferences[PreferencesKeys.NOTIFICATION_ENABLED] ?: false }
        val notificationEnabled = notificationEnabledFlow.first()
        DataResourceResult.Success(notificationEnabled)
    }.getOrElse { DataResourceResult.Failure(it) }


}