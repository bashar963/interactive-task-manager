package tech.devbashar.interactivetaskmanager.presentation.components.widgets

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PageIndicator(
    size: Int,
    selectedIndex: Int,
) {
    val state = rememberLazyListState()
    LazyRow(
        state = state,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items(
            count = size,
        ) {
            val isSelected = selectedIndex == it
            val boxSize by animateDpAsState(
                targetValue = if (isSelected) 12.dp else 8.dp,
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
            )
            val color by animateColorAsState(
                targetValue =
                    if (isSelected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.outlineVariant
                    },
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
            )
            Box(
                modifier =
                    Modifier.padding(4.dp)
                        .size(boxSize)
                        .background(
                            color = color,
                            shape = CircleShape,
                        ),
            )
        }
    }
}
