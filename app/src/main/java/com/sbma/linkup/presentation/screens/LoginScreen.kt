package com.sbma.linkup.presentation.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sbma.linkup.R
import com.sbma.linkup.presentation.components.SignInGoogleButton
import com.sbma.linkup.presentation.ui.theme.LinkUpTheme

@Composable
fun LoginScreen() {
    val context = LocalContext.current
    val intent = remember { Intent(Intent.ACTION_VIEW, Uri.parse("https://sbma.ericaskari.com/auth/google")) }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.login_lottie))
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth().padding(5.dp)
    ) {
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,

        )
        Text(text = stringResource(R.string.login))
        Column(
            modifier = Modifier
                .padding(top = 20.dp)
        ) {
            SignInGoogleButton {
                context.startActivity(intent)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LinkUpTheme {
        Surface {
            LoginScreen()
        }
    }
}
