package com.youngsik.jinada.data.datasource.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.youngsik.domain.model.DataResourceResult
import com.youngsik.domain.model.UserInfo
import com.youngsik.domain.model.UserSettings
import com.youngsik.jinada.data.datasource.DataStoreDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "jinada_settings")
class DataStoreDataSourceImpl(private val context: Context): DataStoreDataSource {

    private object PreferencesKeys {
        val NICKNAME = stringPreferencesKey("nickname")
        val UUID = stringPreferencesKey("uuid")
        val CLOSER_NOTIFICATION_ENABLED = booleanPreferencesKey("closer_notification_enabled")
        val DAILY_NOTIFICATION_ENABLED = booleanPreferencesKey("daily_notification_enabled")
        val CLOSER_NOTIFICATION_RANGE = floatPreferencesKey("closer_notification_range")
        val SEARCH_CLOSER_MEMO_RANGE = floatPreferencesKey("search_closer_memo_range")
    }

    override val userInfoFlow: Flow<UserInfo> = context.dataStore.data.map { preferences ->
        UserInfo(
            uuid = preferences[PreferencesKeys.UUID] ?: "",
            nickName = preferences[PreferencesKeys.NICKNAME] ?: ""
        )
    }

    override val settingsFlow: Flow<UserSettings> = context.dataStore.data.map { preferences ->
        UserSettings(
            closerNotificationEnabled = preferences[PreferencesKeys.CLOSER_NOTIFICATION_ENABLED] ?: true,
            dailyNotificationEnabled = preferences[PreferencesKeys.DAILY_NOTIFICATION_ENABLED] ?: true,
            closerMemoSearchingRange = preferences[PreferencesKeys.SEARCH_CLOSER_MEMO_RANGE] ?: 0.3f,
            closerMemoNotiRange = preferences[PreferencesKeys.CLOSER_NOTIFICATION_RANGE] ?: 0.3f
        )
    }


    override suspend fun setUserInfo(nickname: String, uuid: String):DataResourceResult<Unit> = runCatching {
        context.dataStore.edit { settings ->
            settings[PreferencesKeys.NICKNAME] = nickname
            settings[PreferencesKeys.UUID] = uuid
        }
        DataResourceResult.Success(Unit)
    }.getOrElse { DataResourceResult.Failure(it) }


    override suspend fun setNotificationEnabled(isCheckedCloserNoti: Boolean, isCheckedDailyNoti: Boolean): DataResourceResult<Unit> = runCatching {
        context.dataStore.edit { settings ->
            settings[PreferencesKeys.CLOSER_NOTIFICATION_ENABLED] = isCheckedCloserNoti
            settings[PreferencesKeys.DAILY_NOTIFICATION_ENABLED] = isCheckedDailyNoti
        }
        DataResourceResult.Success(Unit)
    }.getOrElse { DataResourceResult.Failure(it) }

    override suspend fun setRangeOption(closerMemoSearchingRange: Float, closerMemoNotiRange: Float): DataResourceResult<Unit> = runCatching {
        context.dataStore.edit { settings ->
            settings[PreferencesKeys.SEARCH_CLOSER_MEMO_RANGE] = closerMemoSearchingRange
            settings[PreferencesKeys.CLOSER_NOTIFICATION_RANGE] = closerMemoNotiRange
        }
        DataResourceResult.Success(Unit)

    }.getOrElse { DataResourceResult.Failure(it) }

}