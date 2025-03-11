package tech.devbashar.interactivetaskmanager.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color

data class OnBoardModel(
    @DrawableRes val imageRes: Int,
    val color: Color,
    @StringRes val title: Int,
    @StringRes val description: Int,
)
