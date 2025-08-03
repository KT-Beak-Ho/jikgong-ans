package com.billcorea.jikgong.presentation.company.main.projectlist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.company.main.projectlist.components.*
import com.billcorea.jikgong.presentation.company.main.projectlist.uistate.ProjectListEvent
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectListScreen(
  navigator: DestinationsNavigator,
  modifier: Modifier = Modifier,
  viewModel: ProjectListViewModel = viewModel()
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val scrollState = rememberLazyListState()

  val fabVisible by remember {
    derivedStateOf {
      scrollState.firstVisibleItemIndex < 3
    }
  }

  Column(
    modifier = modifier.fillMaxSize()
  ) {
    // 상단바
    ProjectListTopBar(
      onSearchClick = {
        viewModel.onEvent(ProjectListEvent.ToggleSearch)
      },
      onSortClick = {
        viewModel.onEvent(ProjectListEvent.ToggleSortDialog)
      },
      onFilterClick = {
        viewModel.onEvent(ProjectListEvent.ShowFilterDialog)
      }
    )

    // 요약 정보 카드
    if (!uiState.isSearching) {
      ProjectListSummary(
        summary = uiState.summary,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
      )
    }

    // 필터 칩들
    AnimatedVisibility(
      visible = !uiState.isSearching,
      enter = slideInVertically() + fadeIn(),
      exit = slideOutVertically() + fadeOut()
    ) {
      ProjectFilterChips(
        selectedFilter = uiState.selectedFilter,
        onFilterSelected = { filter ->
          viewModel.onEvent(ProjectListEvent.FilterByStatus(filter))
        },
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
      )
    }

    // 검색바
    AnimatedVisibility(
      visible = uiState.isSearching,
      enter = slideInVertically() + fadeIn(),
      exit = slideOutVertically() + fadeOut()
    ) {
      ProjectSearchBar(
        query = uiState.searchQuery,
        suggestions = uiState.searchSuggestions,
        onQueryChange = { query ->
          viewModel.onEvent(ProjectListEvent.SearchProjects(query))
        },
        onSuggestionClick = { suggestion ->
          viewModel.onEvent(ProjectListEvent.SelectSearchSuggestion(suggestion))
        },
        onCloseSearch = {
          viewModel.onEvent(ProjectListEvent.ToggleSearch)
        },
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
      )
    }

    // 메인 컨텐츠
    Box(modifier = Modifier.weight(1f)) {
      ProjectListContent(
        uiState = uiState,
        scrollState = scrollState,
        onEvent = viewModel::onEvent,
        modifier = Modifier.fillMaxSize()
      )

      // FAB
      AnimatedVisibility(
        visible = fabVisible && !uiState.isSearching,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
        modifier = Modifier
          .align(Alignment.BottomEnd)
          .padding(20.dp)
      ) {
        ExtendedFloatingActionButton(
          onClick = {
            viewModel.onEvent(ProjectListEvent.CreateNewProject)
          },
          icon = {
            Icon(
              imageVector = Icons.Default.Add,
              contentDescription = "프로젝트 등록"
            )
          },
          text = {
            Text("프로젝트 등록")
          },
          containerColor = appColorScheme.primary,
          contentColor = Color.White
        )
      }
    }

    // 정렬 다이얼로그
    if (uiState.showSortDialog) {
      ProjectSortDialog(
        currentSortBy = uiState.sortBy,
        onSortSelected = { sortBy ->
          viewModel.onEvent(ProjectListEvent.SortBy(sortBy))
        },
        onDismiss = {
          viewModel.onEvent(ProjectListEvent.ToggleSortDialog)
        }
      )
    }

    // 에러 처리
    uiState.errorMessage?.let { errorMessage ->
      LaunchedEffect(errorMessage) {
        // TODO: 스낵바 표시
        viewModel.onEvent(ProjectListEvent.DismissError)
      }
    }
  }
}

@Composable
private fun ProjectListContent(
  uiState: com.billcorea.jikgong.presentation.company.main.projectlist.uistate.ProjectListUiState,
  scrollState: androidx.compose.foundation.lazy.LazyListState,
  onEvent: (ProjectListEvent) -> Unit,
  modifier: Modifier = Modifier
) {
  when {
    // 로딩 상태
    uiState.isLoading -> {
      ProjectListLoadingState(
        modifier = modifier.padding(20.dp)
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
        modifier = modifier
      )
    }

    // 데이터가 있는 상태
    else -> {
      LazyColumn(
        state = scrollState,
        modifier = modifier,
        contentPadding = PaddingValues(
          horizontal = 20.dp,
          vertical = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        // 프로젝트 카드들
        items(
          items = uiState.filteredProjects,
          key = { it.id }
        ) { project ->
          ProjectCard(
            project = project,
            onCardClick = {
              onEvent(ProjectListEvent.SelectProject(project.id))
            },
            onBookmarkClick = {
              onEvent(ProjectListEvent.ToggleBookmark(project.id))
            },
            onApplyClick = {
              onEvent(ProjectListEvent.QuickApply(project.id))
            },
            onShareClick = {
              onEvent(ProjectListEvent.ShareProject(project.id))
            }
          )
        }

        // 더 로드하기
        if (uiState.canLoadMore) {
          item {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
              contentAlignment = Alignment.Center
            ) {
              if (uiState.isLoadingMore) {
                CircularProgressIndicator(
                  modifier = Modifier.size(24.dp),
                  color = appColorScheme.primary
                )
              } else {
                LaunchedEffect(Unit) {
                  onEvent(ProjectListEvent.LoadMoreProjects)
                }
              }
            }
          }
        }

        // 하단 여백
        item {
          Spacer(modifier = Modifier.height(80.dp))
        }
      }
    }
  }
}

@Preview
@Composable
fun ProjectListScreenPreview() {
  val navController = rememberNavController()
  val navigator = navController.toDestinationsNavigator()

  Jikgong1111Theme {
    ProjectListScreen(navigator = navigator)
  }
}