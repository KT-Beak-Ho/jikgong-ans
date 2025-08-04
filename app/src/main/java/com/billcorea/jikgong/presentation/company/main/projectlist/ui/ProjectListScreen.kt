package com.billcorea.jikgong.presentation.company.main.projectlist.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.billcorea.jikgong.presentation.company.main.projectlist.shared.ProjectListViewModel
import com.billcorea.jikgong.presentation.company.main.projectlist.components.ProjectCard
import com.billcorea.jikgong.presentation.company.main.projectlist.uistate.ProjectListEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectListScreen(
  viewModel: ProjectListViewModel = viewModel()
) {
  val uiState by viewModel.uiState.collectAsState()
  val listState = rememberLazyListState()
  var showSearch by remember { mutableStateOf(false) }
  var showFilters by remember { mutableStateOf(false) }

  // 무한 스크롤 감지
  LaunchedEffect(listState) {
    snapshotFlow { listState.layoutInfo.visibleItemsInfo }
      .collect { visibleItems ->
        if (visibleItems.isNotEmpty()) {
          val lastVisibleIndex = visibleItems.last().index
          val totalItems = listState.layoutInfo.totalItemsCount

          if (lastVisibleIndex >= totalItems - 3 && uiState.canLoadMore && !uiState.isLoadingMore) {
            viewModel.onEvent(ProjectListEvent.LoadMoreProjects)
          }
        }
      }
  }

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "프로젝트 목록",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A)
          )
        },
        actions = {
          IconButton(onClick = {
            showSearch = !showSearch
            viewModel.onEvent(ProjectListEvent.ToggleSearch)
          }) {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = "검색",
              tint = if (showSearch) Color(0xFF3366FF) else Color(0xFF666666)
            )
          }
          IconButton(onClick = { showFilters = !showFilters }) {
            Icon(
              imageVector = Icons.Default.FilterList,
              contentDescription = "필터",
              tint = if (showFilters) Color(0xFF3366FF) else Color(0xFF666666)
            )
          }
          IconButton(onClick = { /* 더보기 메뉴 */ }) {
            Icon(
              imageVector = Icons.Default.MoreVert,
              contentDescription = "더보기",
              tint = Color(0xFF666666)
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = Color.White
        )
      )
    }
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .background(Color(0xFFF8F9FA))
    ) {

      // 요약 통계 카드
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp, vertical = 16.dp),
        colors = CardDefaults.cardColors(
          containerColor = Color(0xFF3366FF).copy(alpha = 0.05f)
        ),
        shape = RoundedCornerShape(12.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          horizontalArrangement = Arrangement.SpaceEvenly
        ) {
          SummaryItem(title = "전체", value = uiState.summary.totalProjects, color = Color(0xFF333333))
          SummaryItem(title = "모집중", value = uiState.summary.recruitingCount, color = Color(0xFF00AA44))
          SummaryItem(title = "진행중", value = uiState.summary.inProgressCount, color = Color(0xFF3366FF))
          SummaryItem(title = "완료", value = uiState.summary.completedCount, color = Color(0xFF666666))
        }
      }

      // 검색 바 (토글)
      AnimatedVisibility(
        visible = showSearch,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut()
      ) {
        OutlinedTextField(
          value = uiState.searchQuery,
          onValueChange = { viewModel.onEvent(ProjectListEvent.SearchProjects(it)) },
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
          placeholder = {
            Text(
              text = "프로젝트 검색 (제목, 위치, 회사명)",
              color = Color.Gray,
              fontSize = 14.sp
            )
          },
          leadingIcon = {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = "검색",
              tint = Color.Gray
            )
          },
          trailingIcon = {
            if (uiState.searchQuery.isNotEmpty()) {
              IconButton(onClick = { viewModel.onEvent(ProjectListEvent.SearchProjects("")) }) {
                Icon(
                  imageVector = Icons.Default.Clear,
                  contentDescription = "검색어 지우기",
                  tint = Color.Gray
                )
              }
            }
          },
          singleLine = true,
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF3366FF),
            unfocusedBorderColor = Color(0xFFE0E0E0)
          )
        )
      }

      // 필터 섹션 (토글)
      AnimatedVisibility(
        visible = showFilters,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut()
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
        ) {
          Text(
            text = "빠른 필터",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF333333),
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
          )

          Row(
            modifier = Modifier.padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            FilterChip(
              selected = uiState.selectedStatus == null,
              onClick = { viewModel.onEvent(ProjectListEvent.FilterByStatus(null)) },
              label = { Text("전체", fontSize = 13.sp) }
            )
            FilterChip(
              selected = uiState.selectedStatus == "RECRUITING",
              onClick = { viewModel.onEvent(ProjectListEvent.FilterByStatus("RECRUITING")) },
              label = { Text("모집중", fontSize = 13.sp) }
            )
            FilterChip(
              selected = uiState.selectedStatus == "IN_PROGRESS",
              onClick = { viewModel.onEvent(ProjectListEvent.FilterByStatus("IN_PROGRESS")) },
              label = { Text("진행중", fontSize = 13.sp) }
            )
            FilterChip(
              selected = uiState.showBookmarkedOnly,
              onClick = { viewModel.onEvent(ProjectListEvent.ToggleBookmarkFilter) },
              label = { Text("북마크", fontSize = 13.sp) }
            )
          }
        }
      }

      // 결과 개수 표시
      Text(
        text = "총 ${uiState.filteredProjects.size}개의 프로젝트",
        fontSize = 14.sp,
        color = Color.Gray,
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
      )

      // 프로젝트 목록
      Box(modifier = Modifier.fillMaxSize()) {
        when {
          uiState.isLoading -> {
            // 로딩 상태
            Column(
              modifier = Modifier
                .fillMaxSize()
                .padding(40.dp),
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.Center
            ) {
              CircularProgressIndicator(
                color = Color(0xFF3366FF),
                modifier = Modifier.size(48.dp)
              )
              Spacer(modifier = Modifier.height(16.dp))
              Text(
                text = "프로젝트를 불러오는 중...",
                color = Color(0xFF666666)
              )
            }
          }

          uiState.filteredProjects.isEmpty() && !uiState.isLoading -> {
            // 빈 상태
            Column(
              modifier = Modifier
                .fillMaxSize()
                .padding(40.dp),
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.Center
            ) {
              Icon(
                imageVector = Icons.Default.Assignment,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = Color(0xFFE0E0E0)
              )
              Spacer(modifier = Modifier.height(24.dp))
              Text(
                text = "검색 결과가 없습니다",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF666666)
              )
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = "다른 검색어나 필터를 사용해보세요",
                fontSize = 14.sp,
                color = Color(0xFF999999)
              )
            }
          }

          else -> {
            // 프로젝트 목록
            LazyColumn(
              state = listState,
              contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
              verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
              items(
                items = uiState.filteredProjects,
                key = { it.id }
              ) { project ->
                ProjectCard(
                  project = project,
                  onBookmarkClick = {
                    viewModel.onEvent(ProjectListEvent.ToggleBookmark(project.id))
                  },
                  onApplyClick = {
                    viewModel.onEvent(ProjectListEvent.ApplyToProject(project.id))
                  }
                )
              }

              // 로딩 더 표시
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
                      color = Color(0xFF3366FF)
                    )
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun SummaryItem(
  title: String,
  value: Int,
  color: Color
) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(
      text = value.toString(),
      fontSize = 20.sp,
      fontWeight = FontWeight.Bold,
      color = color
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = title,
      fontSize = 12.sp,
      color = Color(0xFF666666)
    )
  }
}

@Preview(showBackground = true)
@Composable
fun ProjectListScreenPreview() {
  MaterialTheme {
    ProjectListScreen()
  }
}
