package com.billcorea.jikgong.presentation

import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.R
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import kotlinx.coroutines.delay

@Composable
fun SplashScreen (modifier: Modifier, onTimeout:() -> Unit) {
    val gradientColors = listOf(
        Color(0xFF17192E),
        Color(0xFF17192E),
        Color(0xFF17192E),
        Color(0xFF17192E),
        Color(0xFF17192E),
        Color(0xFF17192E),
        Color(0xFF17192E),
        Color(0xFF17192E),
        Color(0xFF3971FF)
    )
    LaunchedEffect(key1 = true) {
        delay(3000) // 3 seconds delay
        onTimeout()
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = gradientColors,
                    start = Offset.Zero,
                    end = Offset.Infinite,
                    tileMode = TileMode.Repeated
                )
            )
            .wrapContentWidth(Alignment.CenterHorizontally)
    ) {
        Column(modifier=Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(painter = painterResource(R.drawable.ic_jikgong_white), contentDescription = "Logo")
            Image(painter = painterResource(R.drawable.ic_example), contentDescription = "example")
        }
    }
}