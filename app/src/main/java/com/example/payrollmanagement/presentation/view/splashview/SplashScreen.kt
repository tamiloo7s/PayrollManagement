package com.example.payrollmanagement.presentation.view.splashview

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.payrollmanagement.R
import kotlinx.coroutines.launch

@Composable
fun SplashScreen() {

    val alpha = remember { Animatable(0f) }
    val scale = remember { Animatable(0.7f) }

    LaunchedEffect(Unit) {
        launch {
            alpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 3000,
                    easing = FastOutSlowInEasing
                )
            )
        }

        launch {
            scale.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = 3000,
                    easing = FastOutSlowInEasing
                )
            )
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.applogo),
            contentDescription = "app_logo",
            modifier = Modifier
                .size(200.dp)
                .graphicsLayer {
                    this.alpha = alpha.value
                    scaleX = scale.value
                    scaleY = scale.value
                }
        )
    }
}