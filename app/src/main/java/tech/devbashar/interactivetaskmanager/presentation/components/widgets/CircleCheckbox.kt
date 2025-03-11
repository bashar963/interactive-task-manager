package tech.devbashar.interactivetaskmanager.presentation.components.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import compose.icons.FeatherIcons
import compose.icons.feathericons.Circle

@Composable
fun CircleCheckbox(
    checked: Boolean,
    enabled: Boolean = true,
    onCheckedChange: () -> Unit,
) {
    val color = MaterialTheme.colorScheme
    val imageVector = if (checked) Icons.Filled.CheckCircle else FeatherIcons.Circle
    val tint = if (checked) color.primary.copy(alpha = 0.8f) else color.onBackground.copy(alpha = 0.8f)
    val background = if (checked) color.onPrimary else Color.Transparent

    IconButton(
        onClick = { onCheckedChange() },
        modifier = Modifier.offset(x = 4.dp, y = 4.dp),
        enabled = enabled,
    ) {
        Icon(
            imageVector = imageVector,
            tint = tint,
            modifier = Modifier.background(background, shape = CircleShape),
            contentDescription = "checkbox",
        )
    }
}
