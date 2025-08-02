// ========================================
// 📄 ProjectListScreen.kt - 최종 메인 화면
// ========================================
package com.billcorea.jikgong.presentation.company.main.projectlist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.company.main.projectlist.components.*
import com.billcorea.jikgong.presentation.company.main.projectlist.uistate.ProjectListEvent
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator

/**
 * 프로젝트 목록 화면 - 직직직 건설업 인력 매칭 플랫폼
 * 이미지 UI를 참고하여 깔끔하고 직관적인 디자인으로 구현
 * 바텀 네비게이션에서 "project_list" 라우트로 호출됨
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectListScreen(
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier,
    showBottomBar: Boolean = true,
    viewModel: ProjectListViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
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

    // 스크롤 위치 변화 감지
    LaunchedEffect(scrollState.firstVisibleItemIndex) {
        viewModel.onEvent(ProjectListEvent.OnScrollPositionChanged(scrollState.firstVisibleItemIndex))
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "프로젝트 목록",
                        style = AppTypography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.onEvent(ProjectListEvent.ToggleSearch) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "검색"
                        )
                    }
                    IconButton(
                        onClick = { viewModel.onEvent(ProjectListEvent.ToggleSortDialog) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Sort,
                            contentDescription = "정렬"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = appColorScheme.surface,
                    titleContentColor = appColorScheme.onSurface
                )
            )
        },
        floatingActionButton = {
            if (uiState.fabVisible && !uiState.isSearchVisible) {
                ExtendedFloatingActionButton(
                    onClick = { viewModel.onEvent(ProjectListEvent.CreateNewProject) },
                    text = { Text("프로젝트 등록") },
                    icon = {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "프로젝트 등록"
                        )
                    },
                    containerColor = appColorScheme.primary,
                    contentColor = appColorScheme.onPrimary
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            // 메인 콘텐츠
            ProjectListContent(
                uiState = uiState,
                scrollState = scrollState,
                onEvent = { event -> viewModel.onEvent(event) },
                showBottomBar = showBottomBar,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )

            // 스크롤바
            if (canScroll) {
                CustomScrollBar(
                    scrollProgress = scrollProgress,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .fillMaxHeight()
                        .width(6.dp)
                        .padding(
                            end = 4.dp,
                            top = 16.dp,
                            bottom = if (showBottomBar) 120.dp else 40.dp
                        )
                )
            }

            // 검색 오버레이
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
                    onCloseSearch = {
                        viewModel.onEvent(ProjectListEvent.ToggleSearch)
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            // 에러 스낵바
            uiState.errorMessage?.let { errorMessage ->
                LaunchedEffect(errorMessage) {
                    // TODO: 스낵바 표시 로직
                    viewModel.onEvent(ProjectListEvent.DismissError)
                }
            }
        }
    }
}

@Composable
private fun ProjectListContent(
    uiState: com.billcorea.jikgong.presentation.company.main.projectlist.uistate.ProjectListUiState,
    scrollState: androidx.compose.foundation.lazy.LazyListState,
    onEvent: (com.billcorea.jikgong.presentation.company.main.projectlist.uistate.ProjectListEvent) -> Unit,
    showBottomBar: Boolean,
    modifier: Modifier = Modifier
) {
    if (uiState.isLoading) {
        // 로딩 상태
        ProjectListLoadingState(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        )
    } else if (uiState.isEmpty) {
        // 빈 상태
        ProjectListEmptyState(
            isSearching = uiState.isSearching,
            isFiltered = uiState.isFiltered,
            onCreateProject = { onEvent(ProjectListEvent.CreateNewProject) },
            onClearFilters = { onEvent(ProjectListEvent.ClearFilters) },
            modifier = modifier.fillMaxSize()
        )
    } else {
        // 데이터가 있는 상태
        LazyColumn(
            state = scrollState,
            modifier = modifier
                .fillMaxSize()
                .padding(end = if (scrollState.canScrollForward || scrollState.canScrollBackward) 10.dp else 0.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp,
                bottom = if (showBottomBar) 120.dp else 40.dp
            )
        ) {
            // 프로젝트 통계 카드 (검색 중이 아닐 때만 표시)
            if (!uiState.isSearching) {
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

            // 목록 헤더
            item {
                ProjectListHeader(
                    totalCount = uiState.filteredProjects.size,
                    isSearching = uiState.isSearching,
                    onSearchClick = { onEvent(ProjectListEvent.ToggleSearch) }
                )
            }

            // 프로젝트 카드 목록
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
                        // TODO: 더보기 메뉴 표시
                    }
                )
            }

            // 더 로드하기 (페이징)
            if (uiState.canLoadMore) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (uiState.isLoadingMore) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            TextButton(
                                onClick = { onEvent(ProjectListEvent.LoadMoreProjects) }
                            ) {
                                Text("더 보기")
                            }
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

    // Pull to Refresh
    if (uiState.isRefreshing) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            ProjectListLoadingState(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
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
        // 빈 상태를 위한 별도 컴포저블
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

@Preview(name = "검색 결과 없음", showBackground = true, heightDp = 800)
@Composable
fun ProjectListScreenSearchEmptyPreview() {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            ProjectListEmptyState(
                isSearching = true,
                isFiltered = false,
                onCreateProject = {},
                onClearFilters = {},
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}.Center
) {
    CircularProgressIndicator()
}
}
}

// ========================================
// 📄 Preview 컴포저블들
// ========================================
@Preview(name = "데이터 있음", showBackground = true, heightDp = 800)
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

@Preview(name = "로딩 상태", showBackground = true, heightDp = 800)
@Composable
fun ProjectListScreenLoadingPreview() {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme {
        // 로딩 상태를 위한 별도 컴포저블
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment