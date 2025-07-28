package com.billcorea.jikgong.presentation.company.main.projectlist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.presentation.company.main.projectlist.data.ProjectRegistrationData
import com.billcorea.jikgong.presentation.company.main.projectlist.pages.BasicInfoPage
import com.billcorea.jikgong.presentation.company.main.projectlist.pages.DetailInfoPage
import com.billcorea.jikgong.presentation.company.main.projectlist.pages.ReviewPage
import com.billcorea.jikgong.presentation.company.main.projectlist.shared.ProjectRegistrationSharedEvent
import com.billcorea.jikgong.ui.theme.appColorScheme

@Composable
fun ProjectRegistrationForm(
    projectData: ProjectRegistrationData,
    currentPage: Int,
    validationErrors: Map<String, String>,
    onEvent: (ProjectRegistrationSharedEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberLazyListState()

    // 스크롤 진행률 계산
    val scrollProgress by remember {
        derivedStateOf {
            if (scrollState.layoutInfo.totalItemsCount <= 1) return@derivedStateOf 0f

            val firstVisibleItemIndex = scrollState.firstVisibleItemIndex
            val firstVisibleItemScrollOffset = scrollState.firstVisibleItemScrollOffset
            val itemHeight = scrollState.layoutInfo.visibleItemsInfo.firstOrNull()?.size ?: 1

            val totalScrollableHeight = (scrollState.layoutInfo.totalItemsCount - 1) * itemHeight
            val currentScrollOffset = firstVisibleItemIndex * itemHeight + firstVisibleItemScrollOffset

            if (totalScrollableHeight > 0) {
                (currentScrollOffset.toFloat() / totalScrollableHeight.toFloat()).coerceIn(0f, 1f)
            } else {
                0f
            }
        }
    }

    // 스크롤 가능 여부 확인
    val canScroll by remember {
        derivedStateOf {
            scrollState.layoutInfo.totalItemsCount > 0 &&
                    (scrollState.canScrollForward || scrollState.canScrollBackward || scrollState.firstVisibleItemScrollOffset > 0)
        }
    }

    Box(modifier = modifier) {
        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp,
                    end = if (canScroll) 28.dp else 16.dp, // 스크롤바 공간 확보
                    top = 16.dp,
                    bottom = 16.dp
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 100.dp) // 하단바 공간 확보
        ) {
            item {
                when (currentPage) {
                    1 -> BasicInfoPage(
                        requiredInfo = projectData.requiredInfo,
                        validationErrors = validationErrors,
                        onEvent = onEvent,
                        modifier = Modifier.fillMaxWidth()
                    )
                    2 -> DetailInfoPage(
                        teamInfo = projectData.teamInfo,
                        validationErrors = validationErrors,
                        onEvent = onEvent,
                        modifier = Modifier.fillMaxWidth()
                    )
                    3 -> ReviewPage(
                        projectData = projectData,
                        validationErrors = validationErrors,
                        onEvent = onEvent,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // 스크롤바
        if (canScroll) {
            ProjectRegistrationScrollBar(
                scrollProgress = scrollProgress,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()
                    .width(8.dp)
                    .padding(
                        end = 4.dp,
                        top = 20.dp,
                        bottom = 120.dp // 하단바 공간 확보
                    )
            )
        }
    }
}

@Composable
fun ProjectRegistrationScrollBar(
    scrollProgress: Float,
    modifier: Modifier = Modifier,
    isVisible: Boolean = true
) {
    if (isVisible) {
        Box(
            modifier = modifier
        ) {
            // 스크롤바 트랙
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = appColorScheme.outline.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(4.dp)
                    )
            )

            // 스크롤바 썸
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.15f) // 썸의 높이 (전체의 15%)
                    .offset(y = (scrollProgress * 85).dp) // 85% = 100% - 15%
                    .background(
                        color = appColorScheme.primary.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        }
    }
}