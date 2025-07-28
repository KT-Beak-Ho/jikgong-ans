package com.billcorea.jikgong.presentation.company.main.projectlist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme

@Composable
fun ProjectRegistrationBottomBar(
    currentPage: Int,
    totalPages: Int,
    isNextEnabled: Boolean,
    isLoading: Boolean = false,
    nextButtonText: String = "다음",
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    onPageClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = appColorScheme.surface,
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // 페이지 선택 영역
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(totalPages) { index ->
                    val page = index + 1
                    val isSelected = page == currentPage
                    val isCompleted = page < currentPage

                    PageButton(
                        page = page,
                        isSelected = isSelected,
                        isCompleted = isCompleted,
                        onClick = { onPageClick(page) }
                    )

                    if (index < totalPages - 1) {
                        PageConnector(isCompleted = page < currentPage)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 네비게이션 버튼들
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 이전 버튼
                if (currentPage > 1) {
                    OutlinedButton(
                        onClick = onPreviousClick,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = appColorScheme.onSurface
                        )
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "이전",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "이전",
                            style = AppTypography.labelMedium
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.width(16.dp))

                // 다음/등록 버튼
                Button(
                    onClick = onNextClick,
                    enabled = isNextEnabled && !isLoading,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = appColorScheme.primary,
                        contentColor = appColorScheme.onPrimary
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = appColorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "등록 중...",
                            style = AppTypography.labelMedium
                        )
                    } else {
                        Text(
                            text = nextButtonText,
                            style = AppTypography.labelMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        if (currentPage < totalPages) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "다음",
                                modifier = Modifier.size(16.dp)
                            )
                        } else {
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "등록",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PageButton(
    page: Int,
    isSelected: Boolean,
    isCompleted: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when {
        isCompleted -> appColorScheme.primary
        isSelected -> appColorScheme.primary
        else -> appColorScheme.surfaceVariant
    }

    val contentColor = when {
        isCompleted -> appColorScheme.onPrimary
        isSelected -> appColorScheme.onPrimary
        else -> appColorScheme.onSurfaceVariant
    }

    Surface(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .clickable { onClick() },
        color = backgroundColor,
        shape = CircleShape
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            if (isCompleted) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "완료",
                    tint = contentColor,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text(
                    text = page.toString(),
                    style = AppTypography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = contentColor
                )
            }
        }
    }
}

@Composable
private fun PageConnector(
    isCompleted: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .width(40.dp)
            .height(4.dp)
            .background(
                color = if (isCompleted) appColorScheme.primary else appColorScheme.surfaceVariant,
                shape = RoundedCornerShape(2.dp)
            )
    )
}

@Preview(showBackground = true)
@Composable
fun ProjectRegistrationBottomBarPreview() {
    Jikgong1111Theme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 첫 번째 페이지
            ProjectRegistrationBottomBar(
                currentPage = 1,
                totalPages = 3,
                isNextEnabled = true,
                isLoading = false,
                nextButtonText = "다음",
                onPreviousClick = {},
                onNextClick = {},
                onPageClick = {}
            )

            // 두 번째 페이지
            ProjectRegistrationBottomBar(
                currentPage = 2,
                totalPages = 3,
                isNextEnabled = true,
                isLoading = false,
                nextButtonText = "다음",
                onPreviousClick = {},
                onNextClick = {},
                onPageClick = {}
            )

            // 마지막 페이지 (등록 중)
            ProjectRegistrationBottomBar(
                currentPage = 3,
                totalPages = 3,
                isNextEnabled = true,
                isLoading = true,
                nextButtonText = "등록하기",
                onPreviousClick = {},
                onNextClick = {},
                onPageClick = {}
            )
        }
    }
}