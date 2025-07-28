package com.billcorea.jikgong.presentation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme

@Composable
fun PageIndicator(
    numberOfPages: Int,
    selectedPage: Int,
    defaultRadius: Dp = 8.dp,
    selectedLength: Dp = 24.dp,
    space: Dp = 8.dp,
    animationDurationInMillis: Int = 300,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space),
        modifier = modifier
    ) {
        for (i in 0 until numberOfPages) {
            val isSelected = i == selectedPage
            PageIndicatorView(
                isSelected = isSelected,
                defaultRadius = defaultRadius,
                selectedLength = selectedLength,
                animationDurationInMillis = animationDurationInMillis
            )
        }
    }
}

@Composable
private fun PageIndicatorView(
    isSelected: Boolean,
    defaultRadius: Dp,
    selectedLength: Dp,
    animationDurationInMillis: Int,
    modifier: Modifier = Modifier
) {
    val color = if (isSelected) appColorScheme.primary else appColorScheme.outline.copy(alpha = 0.5f)
    val width by animateDpAsState(
        targetValue = if (isSelected) selectedLength else defaultRadius,
        animationSpec = tween(durationMillis = animationDurationInMillis),
        label = "width"
    )

    Box(
        modifier = modifier
            .size(width = width, height = defaultRadius)
            .clip(RoundedCornerShape(defaultRadius))
            .background(color = color)
    )
}

@Preview(showBackground = true)
@Composable
fun PageIndicatorPreview() {
    Jikgong1111Theme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 첫 번째 페이지 선택
            PageIndicator(
                numberOfPages = 3,
                selectedPage = 0
            )

            // 두 번째 페이지 선택
            PageIndicator(
                numberOfPages = 3,
                selectedPage = 1
            )

            // 세 번째 페이지 선택
            PageIndicator(
                numberOfPages = 3,
                selectedPage = 2
            )
        }
    }
}