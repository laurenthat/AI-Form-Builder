package com.sbma.linkup.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import timber.log.Timber

@SuppressLint("QueryPermissionsNeeded")

fun openEmail(context: Context, emailAddress: String) {

    val intent = Intent(Intent.ACTION_SENDTO)
    intent.data = Uri.parse("mailto:$emailAddress")


    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        Timber.d("No Email related app to open")
        Toast.makeText(context, "Something went wrong, could not open app", Toast.LENGTH_SHORT).show()
    }
}

fun initiatePhoneCall(context: Context, phoneNumber: String) {
    val intent = Intent(Intent.ACTION_DIAL)
    intent.data = Uri.parse("tel:$phoneNumber")
    context.startActivity(intent)
}

@SuppressLint("QueryPermissionsNeeded")
fun openSocialMedia(context:Context, urlLink:String) {

    val intent = Intent(Intent.ACTION_VIEW)
    intent.data=Uri.parse(urlLink)
    if (intent.resolveActivity(context.packageManager) != null) {
        context.startActivity(intent)
    } else {
        Toast.makeText(context, "Something went wrong, could not open app", Toast.LENGTH_SHORT).show()
    }
}