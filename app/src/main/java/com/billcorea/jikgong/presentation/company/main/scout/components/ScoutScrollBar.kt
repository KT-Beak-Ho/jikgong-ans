// app/src/main/java/com/billcorea/jikgong/presentation/company/main/scout/components/ScoutScrollBar.kt
package com.billcorea.jikgong.presentation.company.main.scout.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme

/**
 * 스카웃 화면용 커스텀 스크롤바
 */
@Composable
fun ScoutScrollBar(
    scrollProgress: Float,
    modifier: Modifier = Modifier,
    isVisible: Boolean = true,
    thumbHeightRatio: Float = 0.15f, // 썸의 높이 비율 (전체의 15%)
    trackAlpha: Float = 0.2f,
    thumbAlpha: Float = 0.8f,
    cornerRadius: Int = 4
) {
    if (isVisible && scrollProgress >= 0f && scrollProgress <= 1f) {
        Box(
            modifier = modifier
                .alpha(if (scrollProgress > 0f || scrollProgress < 1f) thumbAlpha else 0.4f)
        ) {
            // 스크롤바 트랙 (배경)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = appColorScheme.outline.copy(alpha = trackAlpha),
                        shape = RoundedCornerShape(cornerRadius.dp)
                    )
            )

            // 스크롤바 썸 (현재 위치 표시)
            val maxOffset = 100f - (thumbHeightRatio * 100f)
            val currentOffset = scrollProgress * maxOffset

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(thumbHeightRatio)
                    .offset(y = (currentOffset).dp)
                    .background(
                        color = appColorScheme.primary.copy(alpha = thumbAlpha),
                        shape = RoundedCornerShape(cornerRadius.dp)
                    )
            )
        }
    }
}

/**
 * 향상된 스크롤바 (애니메이션 및 상호작용 지원)
 */
@Composable
fun EnhancedScoutScrollBar(
    scrollProgress: Float,
    modifier: Modifier = Modifier,
    isVisible: Boolean = true,
    isScrolling: Boolean = false, // 스크롤 중인지 여부
    thumbHeightRatio: Float = 0.15f,
    trackAlpha: Float = 0.2f,
    thumbAlpha: Float = 0.8f,
    cornerRadius: Int = 4,
    showIndicator: Boolean = false // 스크롤 진행률 숫자 표시 여부
) {
    if (isVisible && scrollProgress >= 0f && scrollProgress <= 1f) {
        // 스크롤 중일 때 더 진하게 표시
        val actualThumbAlpha = if (isScrolling) thumbAlpha else thumbAlpha * 0.7f
        val actualTrackAlpha = if (isScrolling) trackAlpha else trackAlpha * 0.5f

        Box(
            modifier = modifier
                .alpha(
                    when {
                        isScrolling -> 1f
                        scrollProgress > 0f || scrollProgress < 1f -> actualThumbAlpha
                        else -> 0.3f
                    }
                )
        ) {
            // 스크롤바 트랙
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = appColorScheme.outline.copy(alpha = actualTrackAlpha),
                        shape = RoundedCornerShape(cornerRadius.dp)
                    )
            )

            // 스크롤바 썸
            val maxOffset = 100f - (thumbHeightRatio * 100f)
            val currentOffset = scrollProgress * maxOffset

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(thumbHeightRatio)
                    .offset(y = currentOffset.dp)
                    .background(
                        color = appColorScheme.primary.copy(alpha = actualThumbAlpha),
                        shape = RoundedCornerShape(cornerRadius.dp)
                    )
            )

            // 스크롤 진행률 표시 (선택사항)
            if (showIndicator && isScrolling) {
                ScrollProgressIndicator(
                    progress = scrollProgress,
                    modifier = Modifier
                        .offset(
                            x = (-24).dp,
                            y = currentOffset.dp + (thumbHeightRatio * 50).dp
                        )
                )
            }
        }
    }
}

/**
 * 스크롤 진행률 숫자 표시기
 */
@Composable
private fun ScrollProgressIndicator(
    progress: Float,
    modifier: Modifier = Modifier
) {
    val percentage = (progress * 100).toInt()

    androidx.compose.material3.Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = appColorScheme.primary,
        shadowElevation = 4.dp
    ) {
        androidx.compose.material3.Text(
            text = "$percentage%",
            style = com.billcorea.jikgong.ui.theme.AppTypography.labelSmall.copy(
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            ),
            color = appColorScheme.onPrimary,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

/**
 * 미니 스크롤바 (작은 화면용)
 */
@Composable
fun MiniScoutScrollBar(
    scrollProgress: Float,
    modifier: Modifier = Modifier,
    isVisible: Boolean = true,
    thumbHeightRatio: Float = 0.2f
) {
    ScoutScrollBar(
        scrollProgress = scrollProgress,
        modifier = modifier.width(4.dp),
        isVisible = isVisible,
        thumbHeightRatio = thumbHeightRatio,
        trackAlpha = 0.15f,
        thumbAlpha = 0.6f,
        cornerRadius = 2
    )
}

/**
 * 굵은 스크롤바 (큰 화면용)
 */
@Composable
fun ThickScoutScrollBar(
    scrollProgress: Float,
    modifier: Modifier = Modifier,
    isVisible: Boolean = true,
    thumbHeightRatio: Float = 0.12f
) {
    ScoutScrollBar(
        scrollProgress = scrollProgress,
        modifier = modifier.width(12.dp),
        isVisible = isVisible,
        thumbHeightRatio = thumbHeightRatio,
        trackAlpha = 0.25f,
        thumbAlpha = 0.9f,
        cornerRadius = 6
    )
}

@Preview(name = "기본 스크롤바", showBackground = true, heightDp = 400)
@Composable
fun ScoutScrollBarPreview() {
    Jikgong1111Theme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            ScoutScrollBar(
                scrollProgress = 0.3f,
                modifier = Modifier
                    .width(8.dp)
                    .fillMaxHeight()
            )
        }
    }
}

@Preview(name = "향상된 스크롤바 (스크롤 중)", showBackground = true, heightDp = 400)
@Composable
fun EnhancedScoutScrollBarActivePreview() {
    Jikgong1111Theme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            EnhancedScoutScrollBar(
                scrollProgress = 0.6f,
                isScrolling = true,
                showIndicator = true,
                modifier = Modifier
                    .width(8.dp)
                    .fillMaxHeight()
            )
        }
    }
}

@Preview(name = "향상된 스크롤바 (비활성)", showBackground = true, heightDp = 400)
@Composable
fun EnhancedScoutScrollBarInactivePreview() {
    Jikgong1111Theme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            EnhancedScoutScrollBar(
                scrollProgress = 0.8f,
                isScrolling = false,
                showIndicator = false,
                modifier = Modifier
                    .width(8.dp)
                    .fillMaxHeight()
            )
        }
    }
}

@Preview(name = "미니 스크롤바", showBackground = true, heightDp = 400)
@Composable
fun MiniScoutScrollBarPreview() {
    Jikgong1111Theme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            MiniScoutScrollBar(
                scrollProgress = 0.45f,
                modifier = Modifier.fillMaxHeight()
            )
        }
    }
}

@Preview(name = "굵은 스크롤바", showBackground = true, heightDp = 400)
@Composable
fun ThickScoutScrollBarPreview() {
    Jikgong1111Theme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            ThickScoutScrollBar(
                scrollProgress = 0.75f,
                modifier = Modifier.fillMaxHeight()
            )
        }
    }
}

@Preview(name = "다크 테마", showBackground = true, heightDp = 400)
@Composable
fun ScoutScrollBarDarkPreview() {
    Jikgong1111Theme(darkTheme = true) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ScoutScrollBar(
                    scrollProgress = 0.2f,
                    modifier = Modifier
                        .width(8.dp)
                        .fillMaxHeight()
                )

                EnhancedScoutScrollBar(
                    scrollProgress = 0.5f,
                    isScrolling = true,
                    modifier = Modifier
                        .width(8.dp)
                        .fillMaxHeight()
                )

                ThickScoutScrollBar(
                    scrollProgress = 0.8f,
                    modifier = Modifier.fillMaxHeight()
                )
            }
        }
    }
}

@Preview(name = "스크롤 진행률 표시", showBackground = true)
@Composable
fun ScrollProgressIndicatorPreview() {
    Jikgong1111Theme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ScrollProgressIndicator(progress = 0.25f)
            ScrollProgressIndicator(progress = 0.50f)
            ScrollProgressIndicator(progress = 0.75f)
            ScrollProgressIndicator(progress = 1.0f)
        }
    }
}