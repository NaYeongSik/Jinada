package com.youngsik.jinada.presentation.common

import androidx.annotation.StringRes
import com.youngsik.jinada.presentation.R

enum class StatTabMenu(@StringRes val titleResId: Int) {
    TOTALLY(R.string.tab_totally),
    MONTHLY(R.string.tab_monthly),
    WEEKLY(R.string.tab_weekly)
}