package com.draw2form.ai.presentation.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.draw2form.ai.R
import com.draw2form.ai.api.ApiUploadedFile
import com.draw2form.ai.application.AppViewModelProvider
import com.draw2form.ai.upload.FileUtils
import com.draw2form.ai.user.User
import com.draw2form.ai.user.UserViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects
import java.util.UUID


const val MY_PACKAGE = "com.draw2form.ai"

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

private fun loadBitmap(context: Context, uri: Uri): Bitmap? {
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

@Composable
fun ProfileCard(user: User) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
    ) {
        Column(modifier = Modifier.padding(15.dp)) {
            Text(
                text = "Hello, " + user.name,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = "Welcome to AI Form Builder",
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        AsyncImage(
            model = user.picture,
            contentDescription = "profile photo",
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(50.dp))
        )
    }
}

@Composable
fun UploadCard(
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit,
    cameraText: String,
    galleryText: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(140.dp),
        elevation = CardDefaults.cardElevation(),
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
                verticalAlignment = Alignment.Top // Align items at the top
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
                    onClick = onCameraClick,
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
                    onClick = onGalleryClick,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    user: User,
    canEdit: Boolean,
    onEditClick: (() -> Unit)? = null,
    canGoBack: Boolean,
    onSuccessUpload: ((uploadedFile: ApiUploadedFile) -> Unit)? = null,
    onBackClick: (() -> Unit)? = null,
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileCard(user = user)
            //Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(modifier = Modifier.fillMaxWidth())
            Text(
                text = "Your Drafts",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.padding(top = 10.dp, start = 10.dp, end = 10.dp)
            )
            DraftCard(myDraft = emptyDraft)



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
                    val hasReadExternalStoragePermission =
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )

                    if (hasReadExternalStoragePermission == PackageManager.PERMISSION_GRANTED) {
                        showProcessButton = true
                        launcher.launch("image/*")

                    } else {
                        permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
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
                        .padding(top = 8.dp)
                        .fillMaxWidth(),
                ) {
                    Text("Process")
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
}

val emptyDraft = listOf<Draft>()

//Mock data to be replaced by real data
data class Draft(
    val title: String,
)

val sampleDrafts = listOf(
    Draft(title = "Draft 1"),
    Draft(title = "Draft 2"),
    Draft(title = "Draft 3"),

    )

@Composable
fun DraftCard(myDraft: List<Draft>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation()
    ) {
        if (myDraft.isEmpty()) {
            // Show Lottie animation for empty draft
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(R.drawable.empty_draft),
                    contentDescription = null,
                    modifier = Modifier
                        .height(90.dp)
                        .width(100.dp)
                )
                Text("No drafts found", fontSize = 16.sp)
            }
        } else {

            LazyRow {
                items(myDraft) { draft ->
                    DraftItem(draft = draft)

                }
            }
        }
    }
}

@Composable
fun DraftItem(draft: Draft) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = draft.title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.width(16.dp))
        Button(
            onClick = { /* Should direct to continue editing */ },
            modifier = Modifier
                .height(40.dp)
                .width(100.dp)
        ) {
            Text(text = "Edit Draft")
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
    )
}

