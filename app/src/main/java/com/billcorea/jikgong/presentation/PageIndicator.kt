package com.billcorea.jikgong.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
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
    modifier: Modifier = Modifier,
    selectedColor: Color = appColorScheme.primary,
    defaultColor: Color = appColorScheme.onSurface.copy(alpha = 0.2f)
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(space),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        repeat(numberOfPages) { index ->
            val isSelected = index == selectedPage
            PageIndicatorView(
                isSelected = isSelected,
                selectedColor = selectedColor,
                defaultColor = defaultColor,
                defaultRadius = defaultRadius,
                selectedLength = selectedLength,
                animationDurationInMillis = animationDurationInMillis,
            )
        }
    }
}

@Composable
private fun PageIndicatorView(
    isSelected: Boolean,
    selectedColor: Color,
    defaultColor: Color,
    defaultRadius: Dp,
    selectedLength: Dp,
    animationDurationInMillis: Int,
    modifier: Modifier = Modifier,
) {
    val color: Color by animateColorAsState(
        targetValue = if (isSelected) selectedColor else defaultColor,
        animationSpec = tween(
            durationMillis = animationDurationInMillis,
        ),
        label = "PageIndicatorColor"
    )
    val width: Float by animateFloatAsState(
        targetValue = if (isSelected) selectedLength.value else defaultRadius.value,
        animationSpec = tween(
            durationMillis = animationDurationInMillis,
        ),
        label = "PageIndicatorWidth"
    )

    Canvas(
        modifier = modifier.size(
            width = selectedLength,
            height = defaultRadius,
        ),
    ) {
        drawIndicator(
            color = color,
            radius = defaultRadius.toPx(),
            width = width.dp.toPx(),
        )
    }
}

private fun DrawScope.drawIndicator(
    color: Color,
    radius: Float,
    width: Float,
) {
    val rect = RoundRect(
        left = 0f,
        top = 0f,
        right = width,
        bottom = radius * 2,
        cornerRadius = CornerRadius(radius, radius)
    )
    drawRoundRect(
        color = color,
        cornerRadius = CornerRadius(radius, radius),
        //size = rect.size(200.dp, 50.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun PageIndicatorPreview() {
    Jikgong1111Theme {
        PageIndicator(
            numberOfPages = 5,
            selectedPage = 2,
            modifier = Modifier.size(200.dp, 50.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PageIndicatorFirstPagePreview() {
    Jikgong1111Theme {
        PageIndicator(
            numberOfPages = 3,
            selectedPage = 0,
            modifier = Modifier.size(150.dp, 40.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PageIndicatorLastPagePreview() {
    Jikgong1111Theme {
        PageIndicator(
            numberOfPages = 4,
            selectedPage = 3,
            modifier = Modifier.size(180.dp, 40.dp)
        )
    }
}