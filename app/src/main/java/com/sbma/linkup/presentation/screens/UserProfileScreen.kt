package com.sbma.linkup.presentation.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.sbma.linkup.R
import com.sbma.linkup.application.AppViewModelProvider
import com.sbma.linkup.card.Card
import com.sbma.linkup.card.CardViewModel
import com.sbma.linkup.connection.ConnectionViewModel
import com.sbma.linkup.presentation.components.CardIcon
import com.sbma.linkup.presentation.components.UserCardsList
import com.sbma.linkup.user.User
import com.sbma.linkup.util.initiatePhoneCall
import com.sbma.linkup.util.openEmail
import com.sbma.linkup.util.openSocialMedia
import java.util.UUID

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
        UserProfileScreen(
            it.value,
            userCards.value,
            canEdit = false,
            onEditClick = null,
            onBackClick = onBackClick,
            canGoBack = true
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
                text = "Profile",
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    user: User,
    userCards: List<Card>,
    canEdit: Boolean,
    onEditClick: (() -> Unit)? = null,
    canGoBack: Boolean,
    onBackClick: (() -> Unit)? = null
) {
    val ctx = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val aboutMeCard = userCards.find { it.title == "About Me" }
    val phoneNumberCard = userCards.find { it.title == "Phone" }
    val emailCard = userCards.find { it.title == "Email" }
    val addressCard = userCards.find { it.title == "Address" }
    val titleCard = userCards.find { it.title == "Title" }
    val facebook = userCards.find { it.title == "Facebook" }
    val instagram = userCards.find { it.title == "Instagram" }
    val linkedin = userCards.find { it.title == "LinkedIn" }
    val twitter = userCards.find { it.title == "Twitter" }
    val restOfTheCards = userCards
        .asSequence()
        .filter { it.title != "About Me" }
        .filter { it.title != "Phone" }
        .filter { it.title != "Email" }
        .filter { it.title != "Address" }
        .filter { it.title != "Title" }
        .filter { it.title != "Facebook" }
        .filter { it.title != "Instagram" }
        .filter { it.title != "LinkedIn" }
        .filter { it.title != "Twitter" }
        .toList()

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
                    .clip(RoundedCornerShape(50.dp))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = user.name, fontSize = 25.sp, textAlign = TextAlign.Center)

            titleCard?.let {
                Text(text = it.value, fontSize = 15.sp)
                Spacer(modifier = Modifier.height(8.dp))
            }
            if (instagram != null || facebook != null || twitter != null || linkedin != null) {
                Row {
                    instagram?.let { card ->
                        CardIcon(card.picture, modifier = Modifier
                            .size(60.dp)
                            .padding(5.dp)
                            .clickable {
                                openSocialMedia(ctx, "https://www.instagram.com/${card.value}")
                            })
                    }
                    facebook?.let { card ->
                        CardIcon(card.picture, modifier = Modifier
                            .size(60.dp)
                            .padding(5.dp)
                            .clickable {
                                openSocialMedia(ctx, "https://www.facebook.com/${card.value}")
                            })
                    }
                    twitter?.let { card ->
                        CardIcon(card.picture, modifier = Modifier
                            .size(60.dp)
                            .padding(5.dp)
                            .clickable {
                                openSocialMedia(ctx, "https://www.x.com/${card.value}")
                            })
                    }
                    linkedin?.let { card ->
                        CardIcon(card.picture, modifier = Modifier
                            .size(60.dp)
                            .padding(5.dp)
                            .clickable {
                                openSocialMedia(ctx, "https://www.linkedin.com/${card.value}")
                            })
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            aboutMeCard?.let {
                Card(
                    modifier = Modifier
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.about_me),
                            fontSize = 20.sp,
                            style = MaterialTheme.typography.labelLarge,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = it.value, fontSize = 16.sp)
                    }

                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            if (phoneNumberCard != null || emailCard != null || addressCard != null) {
                Card(
                    modifier = Modifier
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.contact_details),
                            fontSize = 20.sp,
                            style = MaterialTheme.typography.labelLarge,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        phoneNumberCard?.let {
                            ContactInfoRow(
                                icon = Icons.Filled.Call,
                                text = it.value,
                                modifier = Modifier.clickable {
                                    initiatePhoneCall(ctx, it.value)
                                }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        emailCard?.let {
                            ContactInfoRow(
                                icon = Icons.Filled.Email,
                                text = it.value,
                                modifier = Modifier.clickable {
                                    openEmail(ctx, it.value)
                                }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        addressCard?.let {
                            ContactInfoRow(
                                icon = Icons.Filled.LocationOn,
                                text = it.value
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
            UserCardsList(restOfTheCards, withLazyColumn = false)
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
    UserProfileScreen(
        user.value,
        cards,
        canEdit = true,
        onEditClick = {},
        canGoBack = false,
        onBackClick = null
    )
}

