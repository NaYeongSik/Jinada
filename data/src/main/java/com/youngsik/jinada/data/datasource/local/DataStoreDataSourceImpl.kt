package com.youngsik.jinada.data.datasource.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.youngsik.domain.model.DataResourceResult
import com.youngsik.jinada.data.datasource.DataStoreDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "jinada_settings")
class DataStoreDataSourceImpl(private val context: Context): DataStoreDataSource {

    companion object{
        const val NICKNAME = "nickname"
        const val UUID = "uuid"
        const val CLOSER_NOTIFICATION_ENABLED = "closer_notification_enabled"
        const val DAILY_NOTIFICATION_ENABLED = "daily_notification_enabled"
        const val CLOSER_MEMO_SEARCHING_RANGE = "closer_Memo_Searching_Range"
        const val CLOSER_MEMO_NOTI_RANGE = "closer_Memo_Noti_Range"
    }
    private object PreferencesKeys {
        val NICKNAME = stringPreferencesKey("nickname")
        val UUID = stringPreferencesKey("uuid")
        val CLOSER_NOTIFICATION_ENABLED = booleanPreferencesKey("closer_notification_enabled")
        val DAILY_NOTIFICATION_ENABLED = booleanPreferencesKey("daily_notification_enabled")
        val CLOSER_NOTIFICATION_RANGE = floatPreferencesKey("closer_notification_range")
        val SEARCH_CLOSER_MEMO_RANGE = floatPreferencesKey("search_closer_memo_range")
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



    override suspend fun getUserInfo(): DataResourceResult<Map<String,String>> = runCatching {
        val nicknameFlow: Flow<String> = context.dataStore.data.map { preferences -> preferences[PreferencesKeys.NICKNAME] ?: "" }
        val uuidFlow: Flow<String> = context.dataStore.data.map { preferences -> preferences[PreferencesKeys.UUID] ?: "" }
        val nickname = nicknameFlow.first()
        val uuid = uuidFlow.first()

        val userInfo = mapOf(
            NICKNAME to nickname,
            UUID to uuid
        )

        DataResourceResult.Success(userInfo)
    }.getOrElse { DataResourceResult.Failure(it) }


    override suspend fun getNotificationEnabled(): DataResourceResult<Map<String,Boolean>> = runCatching {
        val closerNotificationEnabledFlow: Flow<Boolean> = context.dataStore.data.map { preferences -> preferences[PreferencesKeys.CLOSER_NOTIFICATION_ENABLED] ?: false }
        val dailyNotificationEnabledFlow: Flow<Boolean> = context.dataStore.data.map { preferences -> preferences[PreferencesKeys.DAILY_NOTIFICATION_ENABLED] ?: false }
        val closerNotificationEnabled = closerNotificationEnabledFlow.first()
        val dailyNotificationEnabled = dailyNotificationEnabledFlow.first()

        val notificationEnabled = mapOf(
            CLOSER_NOTIFICATION_ENABLED to closerNotificationEnabled,
            DAILY_NOTIFICATION_ENABLED to dailyNotificationEnabled
        )

        DataResourceResult.Success(notificationEnabled)
    }.getOrElse { DataResourceResult.Failure(it) }

    override suspend fun getRangeOption(): DataResourceResult<Map<String, Float>> = runCatching {
        val closerMemoRangeFlow: Flow<Float> = context.dataStore.data.map { preferences -> preferences[PreferencesKeys.CLOSER_NOTIFICATION_RANGE] ?: 0.3f }
        val closerNotiRangeFlow: Flow<Float> = context.dataStore.data.map { preferences -> preferences[PreferencesKeys.SEARCH_CLOSER_MEMO_RANGE] ?: 0.3f }
        val closerMemoRange = closerMemoRangeFlow.first()
        val closerNotiRange = closerNotiRangeFlow.first()

        val rangeOption = mapOf(
            CLOSER_NOTIFICATION_ENABLED to closerMemoRange,
            DAILY_NOTIFICATION_ENABLED to closerNotiRange
        )

        DataResourceResult.Success(rangeOption)
    }.getOrElse { DataResourceResult.Failure(it) }

}