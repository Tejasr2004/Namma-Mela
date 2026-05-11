package com.nammamela.feature.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashComplete: () -> Unit
) {
    // For a real app, this should load a raw resource (e.g. R.raw.curtain_rise)
    // Using a placeholder spec since actual file is not present.
    val composition by rememberLottieComposition(LottieCompositionSpec.Url("https://assets.lottiefiles.com/packages/lf20_curtain_rise.json"))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1
    )

    // Safety timeout to navigate if animation fails to load/play
    LaunchedEffect(Unit) {
        delay(3000) // 3 second timeout
        onSplashComplete()
    }

    LaunchedEffect(progress) {
        if (progress == 1f) {
            delay(300) 
            onSplashComplete()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress }
        )
    }
}
