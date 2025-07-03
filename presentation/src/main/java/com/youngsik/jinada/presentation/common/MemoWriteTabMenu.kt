package com.youngsik.jinada.presentation.common

import androidx.annotation.StringRes
import com.youngsik.jinada.presentation.R


enum class MemoWriteTabMenu(@StringRes val titleResId: Int) {
    KEYBOARD(R.string.tab_keyboard),
    HANDWRITING(R.string.tab_handwriting)
}