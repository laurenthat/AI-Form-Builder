package com.draw2form.ai.presentation.screens

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.draw2form.ai.R
import com.draw2form.ai.upload.FileUtils
import com.draw2form.ai.util.createImageFile
import com.draw2form.ai.util.createMultipartBody
import com.draw2form.ai.util.loadBitmap
import okhttp3.MultipartBody
import timber.log.Timber
import java.util.Objects

const val MY_PACKAGE = "com.draw2form.ai"

@Composable
fun UploadCard(
    cameraText: String,
    galleryText: String,
    onBitmapReady: (bitmap: Bitmap?) -> Unit,
    onMultipartBodyReady: (body: MultipartBody.Part) -> Unit
) {
    val context = LocalContext.current

    // Camera
    var capturedImageAbsolutePath by remember { mutableStateOf<String?>(null) }
    // Camera
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }
    // Camera
    var capturedImageFileInfo by remember { mutableStateOf<MultipartBody.Part?>(null) }


    // Gallery
    var galleryImageUri by remember { mutableStateOf<Uri?>(null) }
    // Gallery
    var galleryImageFileInfo by remember { mutableStateOf<MultipartBody.Part?>(null) }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                capturedImageUri?.let { uri ->
                    capturedImageAbsolutePath?.let { absPath ->
                        try {
                            capturedImageFileInfo = createMultipartBody(absPath)
                            onMultipartBodyReady(capturedImageFileInfo!!)
                            onBitmapReady(loadBitmap(context, uri))
//                            showProcessButton = true
                            Timber.d("File Information from camera: $capturedImageFileInfo")
                        } catch (e: Exception) {
                            Timber.e(e, "Image loading failed.")
                        }
                    }
                }

            }
        }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            println("Gallery: $uri")
            uri?.let {
                try {
                    galleryImageUri = it

                    val file = FileUtils.getFileFromUri(context, uri)

                    val selectedImageFileInfo = createMultipartBody(file.absolutePath)

                    galleryImageFileInfo = selectedImageFileInfo
                    onMultipartBodyReady(selectedImageFileInfo)

                    onBitmapReady(loadBitmap(context, uri))
                    Timber.d("Gallery Image $galleryImageUri")
                } catch (e: Exception) {
                    Timber.e(e, "Image loading failed.")
                }
            }
        }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(140.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                verticalAlignment = Alignment.Top, // Align items at the top
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icon_upload),
                    contentDescription = null,
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.padding(bottom = 16.dp, top = 8.dp)) {
                    Text(text = "Upload your image", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))


            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        val permissionCheckResult =
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.CAMERA
                            )
                        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                            val file = context.createImageFile()
                            val uri = FileProvider.getUriForFile(
                                Objects.requireNonNull(context),
                                "$MY_PACKAGE.provider",
                                file
                            )
                            capturedImageAbsolutePath = file.absolutePath
                            capturedImageUri = uri
                            cameraLauncher.launch(uri)
                        } else {
                            // Request a permission
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text = cameraText, modifier = Modifier.padding(2.dp))
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        Timber.d("onGalleryClick")
                        val hasReadExternalStoragePermission = when (Build.VERSION.SDK_INT) {
                            in 1..Build.VERSION_CODES.S_V2 -> {
                                Timber.d("Checking Manifest.permission.READ_EXTERNAL_STORAGE Permission in Android 11 And Lower")

                                ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                                )
                            }
                            // Android 12 And Higher
                            else -> {
                                Timber.d("Checking Manifest.permission.READ_MEDIA_IMAGES Permission in Android 12 And Higher")
                                ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.READ_MEDIA_IMAGES
                                )
                            }
                        }
                        Timber.d("onGalleryClick hasReadExternalStoragePermission: $hasReadExternalStoragePermission")

                        if (hasReadExternalStoragePermission == PackageManager.PERMISSION_GRANTED) {
//                            showProcessButton = true
                            launcher.launch("image/*")
                        } else {
                            when (Build.VERSION.SDK_INT) {
                                in 1..Build.VERSION_CODES.S_V2 -> {
                                    Timber.d("Requesting Permission Manifest.permission.READ_EXTERNAL_STORAGE Permission in Android 11 And Lower")
                                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

                                }
                                // Android 12 And Higher
                                else -> {
                                    Timber.d("Requesting Permission Manifest.permission.READ_MEDIA_IMAGES Permission in Android 12 And Higher")
                                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)

                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(text = galleryText, modifier = Modifier.padding(2.dp))
                }
            }

        }
    }
}