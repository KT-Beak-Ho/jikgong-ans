// ========================================
// 📄 ProjectListScreen.kt - 완전 개선 버전
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
 * 🏗️ 직직직 프로젝트 목록 화면
 *
 * 건설업 인력 매칭 플랫폼 "직직직"의 메인 프로젝트 목록 화면
 * - 이미지 UI 참고하여 깔끔하고 직관적인 디자인
 * - 여백 활용한 직관적 레이아웃
 * - 핵심 기능 우선 배치로 빠른 접근성 제공
 * - 사용자 친화적 인터페이스로 복잡한 과정 단순화
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

    // 스크롤 진행률 계산 (커스텀 스크롤바용)
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

    // 🎨 메인 스캐폴드
    Scaffold(
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
                    // 검색 버튼
                    IconButton(
                        onClick = {
                            viewModel.onEvent(ProjectListEvent.ToggleSearch)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "검색"
                        )
                    }

                    // 필터 버튼
                    IconButton(
                        onClick = {
                            viewModel.onEvent(ProjectListEvent.ShowFilterDialog)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "필터"
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
            // 🚀 프로젝트 등록 FAB
            if (!uiState.isSearchVisible) {
                QuickActionButton(
                    onClick = {
                        viewModel.onEvent(ProjectListEvent.CreateNewProject)
                    },
                    text = "프로젝트 등록",
                    icon = Icons.Default.Add,
                    modifier = Modifier.padding(bottom = if (showBottomBar) 80.dp else 16.dp)
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            // 메인 컨텐츠
            ProjectListContent(
                uiState = uiState,
                scrollState = scrollState,
                onEvent = viewModel::onEvent,
                showBottomBar = showBottomBar,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )

            // 커스텀 스크롤바
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

            // 에러 처리
            uiState.errorMessage?.let { errorMessage ->
                LaunchedEffect(errorMessage) {
                    // TODO: 스낵바 표시 로직
                    viewModel.onEvent(ProjectListEvent.DismissError)
                }
            }
        }
    }
}

/**
 * 📋 메인 컨텐츠 영역
 */
@Composable
private fun ProjectListContent(
    uiState: com.billcorea.jikgong.presentation.company.main.projectlist.uistate.ProjectListUiState,
    scrollState: androidx.compose.foundation.lazy.LazyListState,
    onEvent: (ProjectListEvent) -> Unit,
    showBottomBar: Boolean,
    modifier: Modifier = Modifier
) {
    when {
        // 로딩 상태
        uiState.isLoading -> {
            ProjectListLoadingState(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp)
            )
        }

        // 빈 상태
        uiState.isEmpty -> {
            ProjectListEmptyState(
                isSearching = uiState.isSearching,
                isFiltered = uiState.isFiltered,
                onCreateProject = {
                    onEvent(ProjectListEvent.CreateNewProject)
                },
                onClearFilters = {
                    onEvent(ProjectListEvent.ClearFilters)
                },
                modifier = modifier.fillMaxSize()
            )
        }

        // 데이터가 있는 상태
        else -> {
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
                // 📊 프로젝트 통계 카드 (검색 중이 아닐 때만 표시)
                if (!uiState.isSearching) {
                    item {
                        ProjectSummaryCard(
                            summary = uiState.summary,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // 🏷️ 필터 탭 바
                item {
                    ProjectFilterTabBar(
                        selectedStatus = uiState.selectedFilter,
                        projectCounts = uiState.projects.groupBy { it.status }.mapValues { it.value.size },
                        onStatusSelected = { status ->
                            onEvent(ProjectListEvent.FilterByStatus(status))
                        }
                    )
                }

                // 📝 목록 헤더
                item {
                    ProjectListHeader(
                        totalCount = uiState.filteredProjects.size,
                        isSearching = uiState.isSearching,
                        onSearchClick = {
                            onEvent(ProjectListEvent.ToggleSearch)
                        }
                    )
                }

                // 🏗️ 프로젝트 카드 목록
                items(
                    items = uiState.filteredProjects,
                    key = { project -> project.id }
                ) { project ->
                    ProjectCard(
                        project = project,
                        onProjectClick = { selectedProject ->
                            onEvent(ProjectListEvent.SelectProject(selectedProject.id))
                        },
                        onBookmarkClick = { selectedProject ->
                            onEvent(ProjectListEvent.ToggleBookmark(selectedProject.id))
                        },
                        onApplyClick = { selectedProject ->
                            onEvent(ProjectListEvent.QuickApply(selectedProject.id))
                        },
                        onMoreClick = { projectId ->
                            // TODO: 더보기 메뉴 표시
                        }
                    )
                }

                // 📄 더 로드하기 (페이징)
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
                                    onClick = {
                                        onEvent(ProjectListEvent.LoadMoreProjects)
                                    }
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
    }

    // Pull to Refresh 처리
    if (uiState.isRefreshing) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

// ========================================
// 🎭 Preview 컴포저블들
// ========================================

@Preview(name = "프로젝트 목록 - 데이터 있음", showBackground = true, heightDp = 800)
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

@Preview(name = "프로젝트 목록 - 로딩 상태", showBackground = true, heightDp = 800)
@Composable
fun ProjectListScreenLoadingPreview() {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme {
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

@Preview(name = "프로젝트 목록 - 빈 상태", showBackground = true, heightDp = 800)
@Composable
fun ProjectListScreenEmptyPreview() {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme {
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

@Preview(name = "프로젝트 목록 - 검색 결과 없음", showBackground = true, heightDp = 800)
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
}