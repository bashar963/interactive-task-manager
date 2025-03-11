package tech.devbashar.interactivetaskmanager.presentation.screens.task.list

import android.text.format.DateFormat
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Date

@Composable
fun TaskHeader(
    completedTasks: Int,
    totalTasks: Int,
) {
    val currentDate = Date()
    val dayName = DateFormat.format("dd EEEE", currentDate).toString()
    val monthAndYear = DateFormat.format("MMMM yyyy", currentDate).toString()

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = dayName,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Light,
        )
        Text(
            text = monthAndYear,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold,
        )
        Spacer(modifier = Modifier.size(16.dp))
        TaskProgressBar(completedTasks, totalTasks)
    }
}

@Composable
fun TaskProgressBar(
    completedTasks: Int,
    totalTasks: Int,
) {
    val progress = if (totalTasks > 0) completedTasks / totalTasks.toFloat() else 0f

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
    )

    val size = 140.dp
    val strokeWidth = 24.dp
    val backgroundColor = MaterialTheme.colorScheme.primary
    val progressColor = MaterialTheme.colorScheme.error
    Box(
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(size).padding(12.dp)) {
            val radius = size.toPx() / 2
            drawCircle(
                color = backgroundColor,
                radius = radius - strokeWidth.toPx() / 2,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round),
            )

            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = animatedProgress * 360,
                useCenter = false,
                style = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round),
            )
        }

        Text(
            modifier = Modifier.animateContentSize(),
            text = "${(animatedProgress * 100).toInt()}%",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}
