package com.draw2form.ai.presentation.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.draw2form.ai.R
import com.draw2form.ai.application.AppViewModelProvider
import com.draw2form.ai.user.User
import com.draw2form.ai.user.UserViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Objects
import java.util.UUID


const val MY_PACKAGE = "com.draw2form.ai"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreenTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    canEdit: Boolean,
    canGoBack: Boolean,
    onBackClick: (() -> Unit)?,
    onEditClick: (() -> Unit)?
) {
    MediumTopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Text(
                stringResource(R.string.home_screen),
                style = MaterialTheme.typography.labelLarge,
                fontSize = 20.sp
            )
        },
        navigationIcon = {
            if (canGoBack) {
                IconButton(
                    modifier = Modifier,
                    onClick = { onBackClick?.let { it() } }
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                    )
                }

            }
        },
        actions = {
            if (canEdit) {
                IconButton(
                    modifier = Modifier,
                    onClick = { onEditClick?.let { it() } }
                ) {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = "Edit",
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior
    )
}

var imagePath: String = ""

fun Context.createImageFile(): File {

    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "draw2form_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
     imagePath = image.absolutePath
    return image

}

private fun loadBitmap(context: Context, uri: Uri): Bitmap? {
    return if (Build.VERSION.SDK_INT < 28) {
        MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
    } else {
        val source = ImageDecoder.createSource(context.contentResolver, uri)
        ImageDecoder.decodeBitmap(source)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    user: User,
    canEdit: Boolean,
    onEditClick: (() -> Unit)? = null,
    canGoBack: Boolean,
    onBackClick: (() -> Unit)? = null,

    ) {


    var processButtonClicked by remember { mutableStateOf(false) }

    var selectedImageFileInfo by remember { mutableStateOf<MultipartBody.Part?>(null) }
    var showProcessButton by remember { mutableStateOf(false) }
    val userViewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory)

    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        "$MY_PACKAGE.provider", file
    )

    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var galleryImageUri by remember { mutableStateOf<Uri?>(null) }


    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { galUri: Uri? ->
            if (galUri != null) {
                try {
                    selectedImageFileInfo = createMultipartBody(galUri, "image")
                    bitmap = loadBitmap(context, galUri)
                    galleryImageUri = galUri
                    Timber.d("Gallery Image $galleryImageUri")
                    Log.d("DEBUG", "File Information: $selectedImageFileInfo")
                } catch (e: Exception) {
                    Timber.e(e, "Image loading failed.")
                }
            }
        }

    var capturedImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                try {
                    capturedImageUri = uri
                    selectedImageFileInfo = createMultipartBody(capturedImageUri, "image")
                    bitmap = loadBitmap(context, capturedImageUri)
                    showProcessButton = true
                    Log.d("DEBUG", "File Information from camera: $selectedImageFileInfo")
                } catch (e: Exception) {
                    Timber.e(e, "Image loading failed.")
                }
            }
        }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(uri)
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }


    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        topBar = {
            UserProfileScreenTopBar(
                canEdit = canEdit,
                onEditClick = onEditClick,
                canGoBack = canGoBack,
                onBackClick = onBackClick,
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(10.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = user.picture,
                contentDescription = "profile photo",
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(100.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Hello, " + user.name, fontSize = 25.sp, textAlign = TextAlign.Center)

            Spacer(modifier = Modifier.height(30.dp))
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(36.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Button(onClick = {
                        val permissionCheckResult =
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.CAMERA
                            )
                        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                            cameraLauncher.launch(uri)
                        } else {
                            // Request a permission
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }


                    }) {
                        Text(text = "Camera")
                    }

                    Button(
                        onClick = {
                            launcher.launch("image/*")
                            showProcessButton = true
                        }

                    ) {

                        Text(text = "Gallery")
                    }


                }
                if (showProcessButton) {
                    Button(
                        onClick = {
                            selectedImageFileInfo?.let {
                                Log.d("DEBUG", "File process clicked")
                                processButtonClicked = true
                            }
                        },
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .fillMaxWidth(),
                    ) {
                        Text("Process")
                    }
                }
                LaunchedEffect(processButtonClicked) {

                    selectedImageFileInfo?.let {
                        Log.d("DEBUG", "Launch effect called: ${selectedImageFileInfo}")
                        userViewModel.uploadFormImage(selectedImageFileInfo!!)
                        Log.d("DEBUG", "File send to Api")
                    }
                    // Reset the state after the effect is launched
                    processButtonClicked = false
                }
            }

            bitmap?.let { btm ->
                Image(
                    bitmap = btm.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(400.dp)
                )
            }
        }

    }


    @Composable
    fun ContactInfoRow(icon: ImageVector, text: String, modifier: Modifier = Modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                fontSize = 16.sp,
                modifier = Modifier.fillMaxWidth()

            )
        }
    }
}
fun createMultipartBody(uri: Uri, multipartName: String): MultipartBody.Part {

    val file = File(imagePath ?: "")

    val requestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData(name = multipartName, file.name, requestBody)
}

private fun isSupportedFileFormat(context: Context, uri: Uri): Boolean {
    val contentResolver = context.contentResolver
    val mimeTypeMap = MimeTypeMap.getSingleton()
    val fileExtension = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri))

    val supportedFormats = listOf("jpeg", "jpg", "png")

    return fileExtension?.toLowerCase(Locale.ROOT) in supportedFormats
}


@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    val user =
        remember {
            mutableStateOf(
                User(
                    UUID.randomUUID(),
                    "Sebubebu",
                    "shayne@example.com",
                    "UX/UI Designer",
                    "https://users.metropolia.fi/~mohamas/person.jpg"
                )
            )
        }
    HomeScreen(
        user.value,
        canEdit = true,
        onEditClick = {},
        canGoBack = false,
        onBackClick = null,
    )
}

