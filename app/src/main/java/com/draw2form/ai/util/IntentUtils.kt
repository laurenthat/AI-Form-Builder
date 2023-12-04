package com.draw2form.ai.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

@SuppressLint("QueryPermissionsNeeded")

fun openEmail(context: Context, emailAddress: String) {

    val intent = Intent(Intent.ACTION_SENDTO)
    intent.data = Uri.parse("mailto:$emailAddress")


    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        Timber.d("No Email related app to open")
        Toast.makeText(context, "Something went wrong, could not open app", Toast.LENGTH_SHORT)
            .show()
    }
}

fun initiatePhoneCall(context: Context, phoneNumber: String) {
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:$phoneNumber")
    context.startActivity(intent)
}

@SuppressLint("QueryPermissionsNeeded")
fun openSocialMedia(context: Context, urlLink: String) {

    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(urlLink)
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        Toast.makeText(context, "Something went wrong, could not open app", Toast.LENGTH_SHORT)
            .show()
    }
}

fun Context.createImageFile(): File {

    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "draw2form_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
    return image

}

fun loadBitmap(context: Context, uri: Uri): Bitmap? {
    return if (Build.VERSION.SDK_INT < 28) {
        MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
    } else {
        val source = ImageDecoder.createSource(context.contentResolver, uri)
        ImageDecoder.decodeBitmap(source)
    }
}

fun createMultipartBody(imageAbsolutePath: String): MultipartBody.Part {

    val file = File(imageAbsolutePath)
    val mediaType = "multipart/form-data".toMediaTypeOrNull()

    val requestBody = file.asRequestBody(mediaType)
    return MultipartBody.Part.createFormData(name = "image", file.name, requestBody)
}