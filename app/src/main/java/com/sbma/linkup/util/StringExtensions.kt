package com.sbma.linkup.util

import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.sbma.linkup.R
import com.sbma.linkup.presentation.icons.Aboutme
import com.sbma.linkup.presentation.icons.Address
import com.sbma.linkup.presentation.icons.Mail
import com.sbma.linkup.presentation.icons.Phone
import com.sbma.linkup.presentation.icons.Title
import com.sbma.linkup.presentation.icons.Website
import java.util.UUID


fun String?.toPictureResource(): Int? =
    when (this) {
        "facebook" -> R.drawable.facebook
        "instagram" -> R.drawable.instagram
        "linkedin" -> R.drawable.linkedin
        "youtube" -> R.drawable.youtube
        "twitter" -> R.drawable.twitter
        "discord" -> R.drawable.discord
        "github" -> R.drawable.github
        "snapchat" -> R.drawable.snapchat
        "pinterest" -> R.drawable.pinterest
        "tiktok" -> R.drawable.tiktok
        "telegram" -> R.drawable.telegram
        "reddit" -> R.drawable.reddit
        else -> null
    }
fun String?.toImageVector(): ImageVector? =
    when (this) {
        "phone" -> Icons.Filled.Phone
        "email" -> Icons.Filled.Mail
        "title" -> Icons.Filled.Title
        "about" -> Icons.Filled.Aboutme
        "address" -> Icons.Filled.Address
        "website" -> Icons.Filled.Website

            else -> null
    }

fun String.uuidOrNull(): UUID? {
    return try {
        UUID.fromString(this)
    } catch (exception: IllegalArgumentException) {
        null
    }
}

