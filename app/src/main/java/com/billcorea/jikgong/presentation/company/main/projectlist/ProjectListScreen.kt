// ========================================
// 📄 개선된 ProjectListScreen.kt
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.company.main.projectlist.components.*
import com.billcorea.jikgong.presentation.company.main.projectlist.data.ProjectStatus
import com.billcorea.jikgong.presentation.company.main.projectlist.uistate.ProjectListEvent
import com.billcorea.jikgong.presentation.company.main.projectlist.uistate.ProjectListUiState
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator

/**
 * 프로젝트 목록 화면 - 직직직 건설업 인력 매칭 플랫폼
 * 이미지 UI를 참고하여 깔끔하고 직관적인 디자인으로 구현
 */
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

    // 스크롤 진행률 계산
    val scrollProgress by remember {
        derivedStateOf {
            if (scrollState.layoutInfo.totalItemsCount <= 1) return@derivedStateOf 0f
            val firstVisibleItemIndex = scrollState.firstVisibleItemIndex
            val firstVisibleItemScrollOffset = scrollState.firstVisibleItemScrollOffset
            val itemHeight = scrollState.layoutInfo.visibleItemsInfo.firstOrNull()?.size ?: 0

            val totalScrollableHeight = (scrollState.layoutInfo.totalItemsCount - 1) * itemHeight
            val currentScrollOffset = firstVisibleItemIndex * itemHeight + firstVisibleItemScrollOffset

            if (totalScrollableHeight > 0) {
                (currentScrollOffset.toFloat() / totalScrollableHeight.toFloat()).coerceIn(0f, 1f)
            } else {
                0f
            }
        }
    }

    // 스크롤 가능 여부
    val canScroll by remember {
        derivedStateOf {
            scrollState.layoutInfo.totalItemsCount > 0 &&
              (scrollState.canScrollForward || scrollState.canScrollBackward || scrollState.firstVisibleItemScrollOffset > 0)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        // 메인 콘텐츠
        ProjectListContent(
            uiState = uiState,
            scrollState = scrollState,
            onEvent = { event -> viewModel.onEvent(event) },
            showBottomBar = showBottomBar
        )

        // 플로팅 액션 버튼 - 프로젝트 등록
        if (uiState.fabVisible && !uiState.isSearchVisible) {
            ExtendedFloatingActionButton(
                onClick = { viewModel.onEvent(ProjectListEvent.CreateNewProject) },
                text = { Text("프로젝트 등록") },
                icon = { Icon(Icons.Default.Add, contentDescription = "프로젝트 등록") },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(
                        end = 16.dp,
                        bottom = if (showBottomBar) 120.dp else 40.dp
                    ),
                containerColor = appColorScheme.primary,
                contentColor = appColorScheme.onPrimary
            )
        }

        // 스크롤바
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

        // 검색 바 오버레이
        if (uiState.isSearchVisible) {
            ProjectSearchOverlay(
                searchQuery = uiState.searchQuery,
                suggestions = uiState.searchSuggestions,
                onSearchQueryChange = { query ->
                    viewModel.onEvent(ProjectListEvent.SearchProjects(query))
                },
                onSuggestionClick = { suggestion ->
                    viewModel.onEvent(ProjectListEvent.SelectSearchSuggestion(suggestion))
                },
                onCloseSearch = { viewModel.onEvent(ProjectListEvent.ToggleSearch) },
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }

        // 에러 스낵바
        uiState.errorMessage?.let { errorMessage ->
            LaunchedEffect(errorMessage) {
                // 스낵바 표시 로직
                viewModel.onEvent(ProjectListEvent.DismissError)
            }
        }
    }
}

@Composable
private fun ProjectListContent(
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
        // 헤더 - 프로젝트 목록 타이틀
        item {
            ProjectListHeader(
                totalCount = uiState.filteredProjects.size,
                isSearching = uiState.isSearching,
                onSearchClick = { onEvent(ProjectListEvent.ToggleSearch) }
            )
        }

        // 프로젝트 통계 카드 (검색 중이 아닐 때만 표시)
        if (!uiState.isSearchVisible && !uiState.isSearching) {
            item {
                ProjectSummaryCard(
                    summary = uiState.summary,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // 필터 탭 바
        item {
            ProjectFilterTabBar(
                selectedStatus = uiState.selectedFilter,
                projectCounts = uiState.projects.groupBy { it.status }.mapValues { it.value.size },
                onStatusSelected = { status ->
                    onEvent(ProjectListEvent.FilterByStatus(status))
                }
            )
        }

        // 로딩 상태
        if (uiState.isLoading) {
            item {
                ProjectListLoadingState(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
        }
        // 빈 상태
        else if (uiState.isEmpty) {
            item {
                ProjectListEmptyState(
                    isSearching = uiState.isSearching,
                    isFiltered = uiState.isFiltered,
                    onCreateProject = { onEvent(ProjectListEvent.CreateNewProject) },
                    onClearFilters = { onEvent(ProjectListEvent.ClearFilters) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                )
            }
        }
        // 프로젝트 카드 목록
        else {
            items(
                items = uiState.filteredProjects,
                key = { it.id }
            ) { project ->
                ProjectCard(
                    project = project,
                    onProjectClick = { onEvent(ProjectListEvent.SelectProject(it.id)) },
                    onBookmarkClick = { onEvent(ProjectListEvent.ToggleBookmark(it.id)) },
                    onApplyClick = { onEvent(ProjectListEvent.QuickApply(it.id)) },
                    onMoreClick = { projectId ->
                        // 더보기 메뉴 표시 로직
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // 더 불러오기 로딩
            if (uiState.isLoadingMore) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    }
                }
            }
        }

        // 하단 여백
        item {
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun CustomScrollBar(
    scrollProgress: Float,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        // 배경
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = appColorScheme.outline.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(4.dp)
                )
        )

        // 스크롤 인디케이터
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f) // 썸의 높이
                .offset(y = (scrollProgress * 80).dp) // 80% = 100% - 20%
                .background(
                    color = appColorScheme.primary.copy(alpha = 0.8f),
                    shape = RoundedCornerShape(4.dp)
                )
        )
    }
}

// ========================================
// 📄 프리뷰
// ========================================

@Preview(name = "기본 화면 - 데이터 있음", showBackground = true, heightDp = 800)
@Composable
fun ProjectListScreenWithDataPreview() {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme {
        ProjectListScreen(
            navigator = navigator,
            showBottomBar = false
        )
    }
}

@Preview(name = "검색 상태", showBackground = true, heightDp = 800)
@Composable
fun ProjectListScreenSearchPreview() {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = appColorScheme.background
        ) {
            ProjectListScreen(
                navigator = navigator,
                showBottomBar = false
            )
        }
    }
}

@Preview(name = "빈 상태", showBackground = true, heightDp = 800)
@Composable
fun ProjectListScreenEmptyPreview() {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = appColorScheme.background
        ) {
            // 빈 상태를 시뮬레이션하는 별도 컴포저블
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                ProjectListEmptyState(
                    isSearching = false,
                    isFiltered = false,
                    onCreateProject = {},
                    onClearFilters = {},
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Preview(name = "로딩 상태", showBackground = true, heightDp = 800)
@Composable
fun ProjectListScreenLoadingPreview() {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = appColorScheme.background
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                ProjectListLoadingState(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}