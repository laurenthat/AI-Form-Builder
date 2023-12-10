package com.draw2form.ai.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.draw2form.ai.R

@Composable
fun NoDraftCard() {
    ListItem(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(10.dp)),
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        leadingContent = {
            Image(
                painter = painterResource(R.drawable.empty_draft),
                contentDescription = null,
                modifier = Modifier
                    .height(80.dp)
                    .width(90.dp)
            )
        },
        headlineContent = {
            Text("No drafts found", fontSize = 16.sp)
        }
    )
}