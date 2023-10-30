package com.sbma.linkup.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.sbma.linkup.util.toImageVector
import com.sbma.linkup.util.toPictureResource

@Composable
fun CardIcon(picture: String?, modifier: Modifier = Modifier) {
    picture?.let {
        it.toPictureResource()?.let { resourceId ->
            Image(
                painterResource(resourceId),
                contentDescription = "Icon name",
                modifier = modifier
            )

        }
            ?: it.toImageVector()?.let { imageVector ->
                Icon(
                    imageVector = imageVector,
                    contentDescription = "Delete",
                    modifier = modifier
                )

            }
    }
}