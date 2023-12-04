package com.draw2form.ai.presentation.screens

import android.graphics.Bitmap
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.draw2form.ai.api.ApiForm
import com.draw2form.ai.application.AppViewModelProvider
import com.draw2form.ai.user.User
import com.draw2form.ai.user.UserViewModel
import okhttp3.MultipartBody
import timber.log.Timber
import java.util.UUID


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
    val userViewModel: UserViewModel = viewModel(factory = AppViewModelProvider.Factory)

    var showProcessButton by remember { mutableStateOf(false) }

    // Preview
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }

    var fileToUpload by remember { mutableStateOf<MultipartBody.Part?>(null) }

    //Draft forms
    val userForms = userViewModel.apiUserForms.collectAsState(emptyList())
    val forms = userForms.value

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
                onBitmapReady = {
                    bitmap = it
                },
                onMultipartBodyReady = {
                    fileToUpload = it
                    showProcessButton = true
                },
                cameraText = "Camera",
                galleryText = "Gallery"
            )
            if (showProcessButton) {
                Button(
                    onClick = {
                        Timber.d("process button clicked")
                        if (fileToUpload == null) {
                            Timber.d("process button clicked but captured file is null.")
                        }

                        fileToUpload?.let {
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

