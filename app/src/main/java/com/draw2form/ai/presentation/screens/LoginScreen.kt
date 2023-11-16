package com.draw2form.ai.presentation.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.draw2form.ai.R
import com.draw2form.ai.presentation.components.SignInGoogleButton
import com.draw2form.ai.presentation.ui.theme.LinkUpTheme

@Composable
fun LoginScreen() {
    val context = LocalContext.current
    val intent = remember {
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://draw2form.ericaskari.com/api/auth/google")
        )
    }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.login_lottie))

    val (checkedState, onStateChange) = remember { mutableStateOf(false) }

    LazyColumn() {
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                LottieAnimation(
                    modifier = Modifier
                        .size(width = 350.dp, height = 350.dp),
                    composition = composition,
                    iterations = LottieConstants.IterateForever,

                    )
                Spacer(modifier = Modifier.height(50.dp))
                Text(
                    text = "AI FORM BUILDER",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(100.dp))

                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .toggleable(
                            value = checkedState,
                            onValueChange = { onStateChange(!checkedState) },
                        )
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Checkbox(
                        checked = checkedState,
                        onCheckedChange = null
                    )
                    Text(
                        text = "I accept the terms and conditions",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(80.dp))
                Text(
                    text = stringResource(R.string.login),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 16.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))
                Column(
                    modifier = Modifier
                        .padding(top = 20.dp)
                ) {
                    SignInGoogleButton(enabled = checkedState) {
                        context.startActivity(intent)
                    }
                }
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
