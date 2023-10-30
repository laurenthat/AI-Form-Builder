package com.sbma.linkup.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sbma.linkup.card.Card
import com.sbma.linkup.presentation.ui.theme.LinkUpTheme
import java.util.UUID

@Composable
fun EditCard(
    card: Card,
    canEditTitle: Boolean,
    onCardModified: (card: Card) -> Unit,
    onDelete: (card: Card) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 1.dp, vertical = 8.dp),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(
                modifier = Modifier.padding(4.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CardIcon(
                        picture = card.picture,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(46.dp)
                    )

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 10.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        when (canEditTitle) {
                            false -> Text(text = card.title, modifier = Modifier.weight(1f))
                            else -> TextField(
                                value = card.title,
                                onValueChange = {
                                    onCardModified(card.copy(title = it))
                                },
                                label = { Text("") },
                                colors = TextFieldDefaults.colors(
                                    focusedTextColor = Color.Black,
                                    disabledTextColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent
                                )
                            )
                        }

                        HorizontalDivider(modifier = Modifier.fillMaxWidth())

                        TextField(
                            value = card.value,
                            onValueChange = { onCardModified(card.copy(value = it)) },
                            label = { Text("Value") },
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color.Black,
                                disabledTextColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            )
                        )
                    }
                    Box {
                        IconButton(
                            onClick = { onDelete(card) },
                            modifier = Modifier
                                .size(66.dp)
                                .padding(16.dp)
                                .align(Alignment.BottomEnd)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.Red
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateCardPreview() {
    LinkUpTheme {
        EditCard(
            card = Card(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Instagram",
                "https://instagram.com/something",
                "instagram"
            ),
            canEditTitle = true,
            onCardModified = {},
            onDelete = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CreateCardWithoutTitlePreview() {
    LinkUpTheme {
        EditCard(
            card = Card(
                UUID.randomUUID(),
                UUID.randomUUID(),
                "Instagram",
                "https://instagram.com/something",
                "instagram"
            ),
            canEditTitle = false,
            onCardModified = {},
            onDelete = {},
        )
    }
}