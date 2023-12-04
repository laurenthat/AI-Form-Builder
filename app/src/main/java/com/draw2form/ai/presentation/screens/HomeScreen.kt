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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.draw2form.ai.api.ApiForm
import com.draw2form.ai.application.AppViewModelProvider
import com.draw2form.ai.upload.FileUtils
import com.draw2form.ai.user.User
import com.draw2form.ai.user.UserViewModel
import com.draw2form.ai.util.createImageFile
import com.draw2form.ai.util.createMultipartBody
import com.draw2form.ai.util.loadBitmap
import okhttp3.MultipartBody
import timber.log.Timber
import java.util.Objects
import java.util.UUID

const val MY_PACKAGE = "com.draw2form.ai"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    user: User,
    canEdit: Boolean,
    onEditClick: (() -> Unit)? = null,
    canGoBack: Boolean,
    onSuccessUpload: ((form: ApiForm) -> Unit)? = null,
    onBackClick: (() -> Unit)? = null,
    onFormClick: (ApiForm) -> Unit
) {
    val context = LocalContext.current
    val userViewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory)

    var showProcessButton by remember { mutableStateOf(false) }

    // Preview
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

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

    //Draft forms
    val userForms = userViewModel.apiUserForms.collectAsState(emptyList())
    val forms = userForms.value


    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            println("Gallery: $uri")
            uri?.let {
                try {
                    galleryImageUri = it

                    val file = FileUtils.getFileFromUri(context, uri)

                    val selectedImageFileInfo = createMultipartBody(file.absolutePath)

                    galleryImageFileInfo = selectedImageFileInfo

                    bitmap = loadBitmap(context, uri)
                    Timber.d("Gallery Image $galleryImageUri")
                } catch (e: Exception) {
                    Timber.e(e, "Image loading failed.")
                }
            }
        }


    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                capturedImageUri?.let { uri ->
                    capturedImageAbsolutePath?.let { absPath ->
                        try {
                            capturedImageFileInfo = createMultipartBody(absPath)
                            bitmap = loadBitmap(context, uri)
                            showProcessButton = true
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
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,

            ) {
            ProfileCard(user = user)
            //Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            Text(
                text = "Your Drafts",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 10.dp, start = 16.dp, end = 10.dp)
            )

            if (forms.isEmpty()) {
                NoDraftCard()
            } else {
                CategorizedLazyRow(
                    forms = forms, modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp), onFormClick
                )
            }

            LaunchedEffect(true) {
                userViewModel.getUserForms()
            }

            Text(
                text = "Upload New sketch",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp)
            )
            UploadCard(
                onCameraClick = {
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
                onGalleryClick = {
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

//                    val hasReadExternalStoragePermission =
//
//                        ContextCompat.checkSelfPermission(
//                            context,
//                            Manifest.permission.READ_EXTERNAL_STORAGE
//                        )

                    if (hasReadExternalStoragePermission == PackageManager.PERMISSION_GRANTED) {
                        showProcessButton = true
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
                cameraText = "Camera",
                galleryText = "Gallery"

            )
            if (showProcessButton) {
                Button(
                    onClick = {
                        Timber.d("process button clicked")
                        if (capturedImageFileInfo == null) {
                            Timber.d("process button clicked but captured file is null.")
                        } else if (galleryImageFileInfo == null) {
                            Timber.d("process button clicked but gallery file is null.")
                        }

                        val multipart = capturedImageFileInfo ?: galleryImageFileInfo

                        multipart?.let {
                            Timber.d("Launch effect called: $it")
                            userViewModel.uploadFormImage(it) {
                                onSuccessUpload?.let { method ->
                                    method(it)
                                }
                            }
                            Timber.d("File send to Api")
                        }
                    },
                    modifier = Modifier
                        .padding(start = 16.dp, top = 8.dp, end = 16.dp)
                        .fillMaxWidth(),
                ) {
                    Text("Process")
                }
            }
            bitmap?.let { btm ->
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = "Image Preview",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Image(
                        bitmap = btm.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier.size(400.dp)
                    )
                }

            }
        }
    }
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
        onFormClick = {}
    )
}

