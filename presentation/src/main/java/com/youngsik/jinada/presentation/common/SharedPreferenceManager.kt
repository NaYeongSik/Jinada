package com.youngsik.jinada.presentation.common

import android.content.Context
import androidx.core.content.edit

class SharedPreferenceManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("jinada_pref", Context.MODE_PRIVATE)

    fun getNickname(): String? = sharedPreferences.getString("nickname", null)
    fun setNickname(nickname: String) = sharedPreferences.edit { putString("nickname", nickname) }
}