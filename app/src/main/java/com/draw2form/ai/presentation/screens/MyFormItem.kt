package com.draw2form.ai.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.draw2form.ai.R
import com.draw2form.ai.api.ApiForm
import com.draw2form.ai.presentation.ui.theme.DarkColor
import com.draw2form.ai.presentation.ui.theme.ErrorColor
import com.draw2form.ai.presentation.ui.theme.SuccessColor
import com.draw2form.ai.presentation.ui.theme.TransparentColor

@Composable
fun MyFormItem(form: ApiForm, onFormClick: () -> Unit, onShareButtonClick: (id:String) -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()

    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .padding(10.dp, 12.dp, 20.dp, 12.dp)
            ) {

                Text(
                    text = form.name,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )

                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(R.string.status) + ": ",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(end = 10.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = form.status,
                        color = when (form.status.uppercase()) {
                            "PUBLISHED" -> SuccessColor
                            else -> ErrorColor
                        },
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            when {
                form.status.equals("PUBLISHED", ignoreCase = true) -> {
                    SmallIconContainer(
                        onClick = { onShareButtonClick(form.id) },
                        icon = Icons.Default.Share,
                        contentDescription = "Share"
                    )
                }
                else -> {
                    SmallIconContainer(
                        onClick = { onFormClick() },
                        icon = Icons.Default.Edit,
                        contentDescription = "Edit"
                    )
                }
            }
        }
    }
}

@Composable
fun SmallIconContainer(
    onClick: () -> Unit,
    icon: ImageVector,
    contentDescription: String
) {
    Box(
        modifier = Modifier
            .padding(end = 10.dp)
            .clickable { onClick() }
            .size(36.dp)
            .background(
                color = TransparentColor,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = DarkColor,
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.Center)
        )
    }
}
