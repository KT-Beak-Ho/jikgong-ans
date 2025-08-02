// ========================================
// 📄 최종 수정된 ProjectListScreen.kt
// ========================================
package com.billcorea.jikgong.presentation.company.main.projectlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.billcorea.jikgong.presentation.company.main.projectlist.components.*
import com.billcorea.jikgong.presentation.company.main.projectlist.data.ProjectStatus
import com.billcorea.jikgong.presentation.company.main.projectlist.uistate.ProjectListEvent
import com.billcorea.jikgong.presentation.company.main.projectlist.uistate.ProjectListUiState
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectListScreen(
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier,
    showBottomBar: Boolean = true,
    viewModel: ProjectListViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberLazyListState()

    // 스크롤 진행률 및 가능 여부
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
            } else 0f
        }
    }

    val canScroll by remember {
        derivedStateOf {
            scrollState.layoutInfo.totalItemsCount > 0 &&
              (scrollState.canScrollForward || scrollState.canScrollBackward)
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingCreateButton(
                onClick = { viewModel.onEvent(ProjectListEvent.CreateNewProject) },
                modifier = Modifier.padding(bottom = if (showBottomBar) 80.dp else 16.dp)
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // 상단 헤더
                ProjectListHeader(
                    onSearchClick = { viewModel.onEvent(ProjectListEvent.ToggleSearch) },
                    searchQuery = uiState.searchQuery,
                    onSearchQueryChange = { query ->
                        viewModel.onEvent(ProjectListEvent.SearchProjects(query))
                    },
                    isSearchVisible = uiState.isSearchVisible,
                    onClearSearch = { viewModel.onEvent(ProjectListEvent.ClearSearch) }
                )

                // 메인 컨텐츠
                if (uiState.filteredProjects.isEmpty() && !uiState.isLoading) {
                    EmptyProjectState(
                        onCreateProjectClick = {
                            viewModel.onEvent(ProjectListEvent.CreateNewProject)
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    ScrollableProjectContent(
                        uiState = uiState,
                        scrollState = scrollState,
                        onEvent = viewModel::onEvent,
                        showBottomBar = showBottomBar
                    )
                }
            }

            // 커스텀 스크롤바
            if (canScroll) {
                CustomScrollBar(
                    scrollProgress = scrollProgress,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .fillMaxHeight()
                        .width(8.dp)
                        .padding(
                            end = 4.dp,
                            top = 16.dp,
                            bottom = if (showBottomBar) 120.dp else 40.dp
                        )
                )
            }
        }
    }
}

// 커스텀 스크롤바 컴포넌트 (로컬 정의)
@Composable
private fun CustomScrollBar(
    scrollProgress: Float,
    modifier: Modifier = Modifier,
    isVisible: Boolean = true
) {
    if (isVisible && scrollProgress >= 0f && scrollProgress <= 1f) {
        Box(
            modifier = modifier
                .alpha(if (scrollProgress > 0f || scrollProgress < 1f) 0.8f else 0.4f)
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
                    .fillMaxHeight(0.2f) // 썸의 높이 (전체의 20%)
                    .offset(y = (scrollProgress * 80).dp) // 80% = 100% - 20%
                    .background(
                        color = appColorScheme.primary.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        }
    }
}

@Composable
private fun ScrollableProjectContent(
    uiState: ProjectListUiState,
    scrollState: androidx.compose.foundation.lazy.LazyListState,
    onEvent: (ProjectListEvent) -> Unit,
    showBottomBar: Boolean
) {
    LazyColumn(
        state = scrollState,
        modifier = Modifier
            .fillMaxSize()
            .padding(end = 12.dp), // 스크롤바 공간
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            top = 8.dp,
            bottom = if (showBottomBar) 120.dp else 40.dp
        )
    ) {
        // 프로젝트 통계 카드
        if (!uiState.isSearchVisible) {
            item {
                ProjectSummaryCard(summary = uiState.summary)
            }
        }

        // 필터 바
        item {
            ProjectFilterBar(
                selectedStatus = uiState.selectedFilter,
                onStatusSelected = { status ->
                    onEvent(ProjectListEvent.FilterByStatus(status))
                }
            )
        }

        // 프로젝트 목록 헤더
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (uiState.searchQuery.isEmpty()) "모집 중인 프로젝트" else "검색 결과",
                    style = AppTypography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = appColorScheme.onSurface
                )
                Text(
                    text = "총 ${uiState.filteredProjects.size}건",
                    style = AppTypography.bodyMedium,
                    color = appColorScheme.onSurfaceVariant
                )
            }
        }

        // 로딩 상태
        if (uiState.isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        } else {
            // 프로젝트 카드 목록
            items(
                items = uiState.filteredProjects,
                key = { it.id }
            ) { project ->
                ProjectCard(
                    project = project,
                    onProjectClick = { onEvent(ProjectListEvent.SelectProject(it.id)) },
                    onQuickApplyClick = { onEvent(ProjectListEvent.QuickApply(it.id)) }
                )
            }
        }

        // 하단 여백
        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}