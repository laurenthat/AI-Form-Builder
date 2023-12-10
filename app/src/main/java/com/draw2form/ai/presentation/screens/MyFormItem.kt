package com.draw2form.ai.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.draw2form.ai.api.ApiForm
import com.draw2form.ai.presentation.icons.Forms

@Composable
fun MyFormItem(form: ApiForm, onFormClick: () -> Unit, onShareButtonClick: (id: String) -> Unit) {
    ListItem(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(10.dp)),
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        leadingContent = {
            Icon(
                imageVector = Icons.Filled.Forms,
                contentDescription = "Form",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(50.dp),
            )
        },
        headlineContent = {
            Text(
                text = form.name,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        supportingContent = {
            SuggestionChip(
                onClick = { },
                shape = RoundedCornerShape(15.dp),
                border = BorderStroke(0.dp, Color.Transparent),
                label = { Text(text = form.status, fontSize = 12.sp, color = Color.Black) },
                colors = SuggestionChipDefaults.suggestionChipColors(
                    containerColor =
                    if (form.status == "DRAFT") Color(
                        255, 247, 147
                    ) else Color(182, 253, 192)
                ),
                modifier = Modifier
            )
        },
        trailingContent = {
            when {
                form.status.equals("PUBLISHED", ignoreCase = true) -> {
                    SmallIconContainer(
                        onClick = { onShareButtonClick(form.id) },
                        icon = Icons.Default.Share,
                        contentDescription = "Share",
                    )
                }

                else -> {
                    SmallIconContainer(
                        onClick = { onFormClick() },
                        icon = Icons.Default.Edit,
                        contentDescription = "Edit",
                    )
                }
            }
        }
    )
}

@Composable
fun SmallIconContainer(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String,
) {
    Box(
        modifier = Modifier
            .clickable { onClick() }
            .size(20.dp)
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.Center)
        )
    }
}
