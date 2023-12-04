package com.draw2form.ai.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.draw2form.ai.api.ApiForm
import com.draw2form.ai.presentation.ui.theme.ErrorColor
import com.draw2form.ai.presentation.ui.theme.SuccessColor

@Composable
fun MyFormItem(form: ApiForm, onFormClick: () -> Unit) {
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
                        text = "Status: ",
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

            Button(
                onClick = { onFormClick() },
                modifier = Modifier
                    .height(35.dp)
                    .width(90.dp)
                    .padding(end = 10.dp)
            ) {
                Text(text = "Open", fontSize = 10.sp, modifier = Modifier.fillMaxSize())
            }
        }
    }

}