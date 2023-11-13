package com.sbma.linkup.presentation.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
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
import androidx.compose.runtime.collectAsState
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
import coil.compose.rememberAsyncImagePainter
import com.sbma.linkup.R
import com.sbma.linkup.application.AppViewModelProvider
import com.sbma.linkup.card.Card
import com.sbma.linkup.card.CardViewModel
import com.sbma.linkup.connection.ConnectionViewModel
import com.sbma.linkup.user.User
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects
import java.util.UUID


const val MY_PACKAGE = "com.sbma.linkup"
@Composable
fun ConnectionUserProfileScreenProvider(
    user: User,
    connectionIdParam: String?,
    onBackClick: (() -> Unit)?
) {
    val userConnectionViewModel: ConnectionViewModel =
        viewModel(factory = AppViewModelProvider.Factory)
    val userCardViewModel: CardViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val connectionUser =
        userConnectionViewModel.allItemsStream(user.id).collectAsState(initial = null)

    val connection = (connectionUser.value ?: mapOf()).entries.toList().find { mapEntry ->
        connectionIdParam?.let { mapEntry.key.id == UUID.fromString(it) } ?: false
    }
    connection?.let {
        val userCards =
            userCardViewModel.allItemsStream(it.value.id).collectAsState(initial = listOf())
        HomeScreen(
            it.value,
            canEdit = false,
            onEditClick = null,
            onBackClick = onBackClick,
            canGoBack = true,
        )
    }
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

fun Context.createImageFile(): File {

    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    user: User,
    canEdit: Boolean,
    onEditClick: (() -> Unit)? = null,
    canGoBack: Boolean,
    onBackClick: (() -> Unit)? = null,

) {

    val context = LocalContext.current
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        "$MY_PACKAGE.provider", file
    )

    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                bitmap = loadBitmap(context, uri)
            }
        }

    var capturedImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            capturedImageUri = uri
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
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var hasCamPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

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
                        onClick = { launcher.launch("image/*") },
                    ) {

                        Text(text = "Gallery")
                    }

                }



            }

            bitmap?.let { btm ->
                Image(
                    bitmap = btm.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(400.dp)
                )
            }

            if (capturedImageUri.path?.isNotEmpty() == true) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp, 8.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(capturedImageUri),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center)
                    )

                }
            }
        }

    }
    fun uploadImageToApi(imageUri: Uri, authToken: String) {
        val file = File(imageUri.path!!)
        val apiUrl = "https://draw2form.ericaskari.com/api/upload"

        val client = OkHttpClient()

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("image", file.name, RequestBody.create("image/*".toMediaTypeOrNull(), file))
            .addFormDataPart("key", "value")
            .build()

        val request = Request.Builder()
            .url(apiUrl)
            .header("Authorization", "Bearer $authToken")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                // Handle success
                val responseData = response.body?.string()
                println("Upload successful: $responseData")
            }

            override fun onFailure(call: Call, e: IOException) {
                // Handle failure
                println("Upload failed: ${e.message}")
            }
        })
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
    val cards = remember {
        mutableListOf(
            Card(
                UUID.randomUUID(),
                user.value.id,
                "About Me",
                "Sebubebu is a results-driven Marketing Manager with a passion " +
                        "for leveraging innovative strategies to drive brand growth and " +
                        "customer engagement in the ever-evolving digital landscape." +
                        "Feel free to reach me out for any specific queries.",
                "facebook"
            ),
            Card(
                UUID.randomUUID(),
                user.value.id,
                "Title",
                "UI/UX Designer and Graphic Designer",
                "title"
            ),
            Card(
                UUID.randomUUID(),
                user.value.id,
                "Phone Number",
                "+358245304934",
                "phone"
            ),
            Card(
                UUID.randomUUID(),
                user.value.id,
                "Email",
                "email@email.com",
                "email"
            ),
            Card(
                UUID.randomUUID(),
                user.value.id,
                "Location",
                "Pohjoisesplanadi 31, 00100 Helsinki, Finland",
                "location"
            ),
            Card(
                UUID.randomUUID(),
                user.value.id,
                "Facebook",
                "https://facebook.com/something",
                "facebook"
            ),
            Card(
                UUID.randomUUID(),
                user.value.id,
                "Instagram",
                "https://instagram.com/something",
                "instagram"
            ),
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

