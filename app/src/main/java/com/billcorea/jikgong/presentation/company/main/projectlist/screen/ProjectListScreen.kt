package com.billcorea.jikgong.presentation.company.main.projectlist.screen

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.company.main.projectlist.model.*
import com.billcorea.jikgong.presentation.company.main.projectlist.viewmodel.ProjectListViewModel
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.appColorScheme
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun ProjectListScreen(
  navController: NavController,
  viewModel: ProjectListViewModel = viewModel()
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val selectedFilter by viewModel.selectedFilter.collectAsStateWithLifecycle()

  ProjectListContent(
    uiState = uiState,
    selectedFilter = selectedFilter,
    navController = navController,
    onFilterSelected = viewModel::selectFilter,
    onProjectClick = { projectId ->
      navController.navigate("company/projectdetail/$projectId")
    },
    onEditClick = { projectId ->
      navController.navigate("company/projectedit/$projectId")
    },
    onRecruitClick = { projectId ->
      navController.navigate("company/recruit/$projectId")
    },
    onRefresh = viewModel::loadProjects,
    onToggleBookmark = viewModel::toggleBookmark
  )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
private fun ProjectListContent(
  uiState: ProjectListUiState,
  selectedFilter: ProjectFilter,
  navController: NavController,
  onFilterSelected: (ProjectFilter) -> Unit,
  onProjectClick: (String) -> Unit,
  onEditClick: (String) -> Unit,
  onRecruitClick: (String) -> Unit,
  onRefresh: () -> Unit,
  onToggleBookmark: (String) -> Unit
) {
  val pullRefreshState = rememberPullRefreshState(
    refreshing = uiState.isLoading,
    onRefresh = onRefresh
  )

  Box(
    modifier = Modifier
      .fillMaxSize()
      .pullRefresh(pullRefreshState)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFF5F5F5))
    ) {
      // 필터 탭
      ProjectFilterTabs(
        selectedFilter = selectedFilter,
        stats = uiState.stats,
        onFilterSelected = onFilterSelected
      )

      // 모집중 프로젝트 수 헤더
      AnimatedVisibility(
        visible = selectedFilter == ProjectFilter.ALL || selectedFilter == ProjectFilter.RECRUITING
      ) {
        RecruitingProjectsHeader(
          count = uiState.stats.recruitingProjects,
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
      }

      // 프로젝트 목록
      when {
        uiState.error != null -> {
          ErrorMessage(
            message = uiState.error,
            onRetry = onRefresh,
            modifier = Modifier.fillMaxSize()
          )
        }
        uiState.filteredProjects.isEmpty() && !uiState.isLoading -> {
          EmptyProjectList(
            filter = selectedFilter,
            modifier = Modifier.fillMaxSize()
          )
        }
        else -> {
          LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            items(
              items = uiState.filteredProjects,
              key = { it.id }
            ) { project ->
              ProjectCard(
                project = project,
                onProjectClick = { onProjectClick(project.id) },
                onEditClick = { onEditClick(project.id) },
                onRecruitClick = { onRecruitClick(project.id) },
                onToggleBookmark = { onToggleBookmark(project.id) }
              )
            }
          }
        }
      }
    }

    // Pull to Refresh Indicator
    PullRefreshIndicator(
      refreshing = uiState.isLoading,
      state = pullRefreshState,
      modifier = Modifier.align(Alignment.TopCenter),
      backgroundColor = Color.White,
      contentColor = appColorScheme.primary
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProjectListTopBar() {
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
      IconButton(onClick = { /* 그리드/리스트 뷰 전환 */ }) {
        Icon(
          imageVector = Icons.Default.GridView,
          contentDescription = "뷰 전환",
          tint = Color.Gray
        )
      }
      IconButton(onClick = { /* 메뉴 */ }) {
        Icon(
          imageVector = Icons.Default.Menu,
          contentDescription = "메뉴",
          tint = Color.Gray
        )
      }
    },
    colors = TopAppBarDefaults.topAppBarColors(
      containerColor = Color.White
    )
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProjectFilterTabs(
  selectedFilter: ProjectFilter,
  stats: ProjectStats,
  onFilterSelected: (ProjectFilter) -> Unit
) {
  val filters = listOf(
    ProjectFilter.ALL to stats.totalProjects,
    ProjectFilter.RECRUITING to stats.recruitingProjects,
    ProjectFilter.IN_PROGRESS to stats.activeProjects,
    ProjectFilter.COMPLETED to stats.completedProjects
  )

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .background(Color.White)
      .padding(horizontal = 16.dp, vertical = 8.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    filters.forEach { (filter, count) ->
      FilterChip(
        selected = selectedFilter == filter,
        onClick = { onFilterSelected(filter) },
        label = {
          Text(
            text = "${filter.displayName} ($count)",
            style = AppTypography.labelMedium
          )
        },
        colors = FilterChipDefaults.filterChipColors(
          selectedContainerColor = appColorScheme.primary,
          selectedLabelColor = Color.White,
          containerColor = appColorScheme.surfaceVariant,
          labelColor = appColorScheme.onSurfaceVariant
        )
      )
    }
  }
}

@Composable
private fun RecruitingProjectsHeader(
  count: Int,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier,
    colors = CardDefaults.cardColors(
      containerColor = appColorScheme.primaryContainer
    ),
    shape = RoundedCornerShape(12.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "모집중 프로젝트",
          style = AppTypography.titleMedium.copy(
            color = appColorScheme.onPrimaryContainer
          )
        )
        Text(
          text = "총 ${count}개",
          style = AppTypography.bodyMedium.copy(
            color = appColorScheme.onPrimaryContainer
          )
        )
      }
      Text(
        text = count.toString(),
        style = AppTypography.headlineMedium.copy(
          fontWeight = FontWeight.Bold,
          color = appColorScheme.primary
        )
      )
    }
  }
}

@Composable
private fun ProjectCard(
  project: Project,
  onProjectClick: () -> Unit,
  onEditClick: () -> Unit,
  onRecruitClick: () -> Unit,
  onToggleBookmark: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onProjectClick() },
    colors = CardDefaults.cardColors(
      containerColor = Color.White
    ),
    shape = RoundedCornerShape(12.dp),
    elevation = CardDefaults.cardElevation(
      defaultElevation = 2.dp
    )
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      // 제목과 상태
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = project.title,
            style = AppTypography.titleMedium.copy(
              fontWeight = FontWeight.Bold
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
          )
          if (project.isUrgent) {
            Spacer(modifier = Modifier.height(4.dp))
            StatusChip(
              text = "긴급",
              backgroundColor = Color(0xFFFFEBEE),
              textColor = Color(0xFFF44336)
            )
          }
        }
        Row(
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          // 북마크 버튼
          IconButton(
            onClick = onToggleBookmark,
            modifier = Modifier.size(32.dp)
          ) {
            Icon(
              imageVector = if (project.isBookmarked) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
              contentDescription = if (project.isBookmarked) "북마크 해제" else "북마크",
              tint = if (project.isBookmarked) appColorScheme.primary else Color.Gray
            )
          }

          StatusChip(
            text = when (project.status) {
              "RECRUITING" -> "모집중"
              "IN_PROGRESS" -> "진행중"
              "COMPLETED" -> "완료"
              else -> "기타"
            },
            backgroundColor = when (project.status) {
              "RECRUITING" -> Color(0xFFE8F5E9)
              "IN_PROGRESS" -> Color(0xFFFFF3E0)
              "COMPLETED" -> Color(0xFFF3E5F5)
              else -> Color(0xFFF5F5F5)
            },
            textColor = when (project.status) {
              "RECRUITING" -> Color(0xFF4CAF50)
              "IN_PROGRESS" -> Color(0xFFFF9800)
              "COMPLETED" -> Color(0xFF9C27B0)
              else -> Color.Gray
            }
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // 위치
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.LocationOn,
          contentDescription = null,
          tint = Color(0xFFE91E63),
          modifier = Modifier.size(16.dp)
        )
        Text(
          text = project.location,
          style = AppTypography.bodyMedium.copy(
            color = Color.Gray
          ),
          modifier = Modifier.padding(start = 4.dp)
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      // 기간
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.CalendarToday,
          contentDescription = null,
          tint = appColorScheme.primary,
          modifier = Modifier.size(16.dp)
        )
        val dateFormatter = DateTimeFormatter.ofPattern("MM/dd")
        Text(
          text = "${project.startDate.format(dateFormatter)} ~ ${project.endDate.format(dateFormatter)}",
          style = AppTypography.bodyMedium.copy(
            color = Color.Gray
          ),
          modifier = Modifier.padding(start = 4.dp)
        )
      }

      // 지원자 수
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.People,
          contentDescription = null,
          tint = Color(0xFF9C27B0),
          modifier = Modifier.size(16.dp)
        )
        Text(
          text = "${project.currentApplicants}/${project.maxApplicants}명",
          style = AppTypography.bodyMedium.copy(
            color = Color.Gray
          ),
          modifier = Modifier.padding(start = 4.dp)
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      // 일당
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "💰",
          style = AppTypography.bodyLarge
        )
        Text(
          text = "${formatCurrency(project.wage)}/일",
          style = AppTypography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            color = appColorScheme.primary
          ),
          modifier = Modifier.padding(start = 8.dp)
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // 버튼들
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        OutlinedButton(
          onClick = onEditClick,
          modifier = Modifier.weight(1f),
          colors = ButtonDefaults.outlinedButtonColors(
            contentColor = appColorScheme.primary
          ),
          border = ButtonDefaults.outlinedButtonBorder.copy(
            width = 1.dp,
            brush = androidx.compose.ui.graphics.SolidColor(appColorScheme.primary)
          )
        ) {
          Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(text = "수정")
        }

        Button(
          onClick = onRecruitClick,
          modifier = Modifier.weight(1f),
          colors = ButtonDefaults.buttonColors(
            containerColor = appColorScheme.primary
          )
        ) {
          Icon(
            imageVector = Icons.Default.PersonAdd,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = Color.Red
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(text = "인력 모집")
        }
      }
    }
  }
}

@Composable
private fun StatusChip(
  text: String,
  backgroundColor: Color,
  textColor: Color
) {
  Surface(
    color = backgroundColor,
    shape = RoundedCornerShape(4.dp)
  ) {
    Text(
      text = text,
      color = textColor,
      style = AppTypography.labelSmall.copy(
        fontWeight = FontWeight.Medium
      ),
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    )
  }
}

@Composable
private fun ErrorMessage(
  message: String,
  onRetry: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Icon(
      imageVector = Icons.Default.Error,
      contentDescription = null,
      tint = appColorScheme.error,
      modifier = Modifier.size(48.dp)
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
      text = message,
      style = AppTypography.bodyLarge,
      textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(16.dp))
    Button(onClick = onRetry) {
      Text("다시 시도")
    }
  }
}

@Composable
private fun EmptyProjectList(
  filter: ProjectFilter,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    Icon(
      imageVector = Icons.Default.Assignment,
      contentDescription = null,
      tint = Color.Gray,
      modifier = Modifier.size(64.dp)
    )
    Spacer(modifier = Modifier.height(16.dp))
    Text(
      text = when (filter) {
        ProjectFilter.ALL -> "등록된 프로젝트가 없습니다"
        ProjectFilter.RECRUITING -> "모집중인 프로젝트가 없습니다"
        ProjectFilter.IN_PROGRESS -> "진행중인 프로젝트가 없습니다"
        ProjectFilter.COMPLETED -> "완료된 프로젝트가 없습니다"
        ProjectFilter.BOOKMARKED -> "북마크한 프로젝트가 없습니다"
      },
      style = AppTypography.bodyLarge,
      color = Color.Gray,
      textAlign = TextAlign.Center
    )
  }
}

private fun formatCurrency(amount: Int): String {
  val formatter = NumberFormat.getInstance(Locale.KOREA)
  return formatter.format(amount) + "원"
}

// Preview
@Preview(showBackground = true)
@Composable
fun ProjectListScreenPreview() {
  MaterialTheme {
    ProjectListContent(
      uiState = ProjectListUiState(
        filteredProjects = listOf(
          Project(
            id = "1",
            title = "사하구 낙동5블럭 낙동강 온도 측정 센터 신축공사",
            company = "대한건설(주)",
            location = "부산 사하구",
            category = "REBAR_WORKER",
            status = "RECRUITING",
            startDate = java.time.LocalDateTime.now().plusDays(3),
            endDate = java.time.LocalDateTime.now().plusDays(45),
            wage = 510000,
            description = "35층 아파트 신축공사 철근 조립 및 설치 작업",
            requirements = listOf("철근 작업 경력 2년 이상", "건설기능사 자격증"),
            benefits = listOf("4대보험 완비", "중식 제공", "교통비 지원"),
            currentApplicants = 3,
            maxApplicants = 15,
            isBookmarked = false,
            isUrgent = true,
            createdAt = java.time.LocalDateTime.now(),
            updatedAt = java.time.LocalDateTime.now()
          ),
          Project(
            id = "2",
            title = "인천 물류센터 건설",
            company = "서울전기공사",
            location = "인천 연수구",
            category = "ELECTRICIAN",
            status = "IN_PROGRESS",
            startDate = java.time.LocalDateTime.now().minusDays(5),
            endDate = java.time.LocalDateTime.now().plusDays(25),
            wage = 300000,
            description = "대형 쇼핑몰 전기설비 설치 및 배선 작업",
            requirements = listOf("전기기능사 자격증 필수"),
            benefits = listOf("주말 특근비 별도", "숙박 제공"),
            currentApplicants = 12,
            maxApplicants = 30,
            isBookmarked = true,
            isUrgent = false,
            createdAt = java.time.LocalDateTime.now(),
            updatedAt = java.time.LocalDateTime.now()
          )
        ),
        stats = ProjectStats(
          totalProjects = 4,
          activeProjects = 1,
          recruitingProjects = 2,
          completedProjects = 1,
          totalApplicants = 15,
          averageWage = 405000
        ),
        isLoading = false,
        error = null
      ),
      selectedFilter = ProjectFilter.ALL,
      navController = rememberNavController(),
      onFilterSelected = {},
      onProjectClick = {},
      onEditClick = {},
      onRecruitClick = {},
      onRefresh = {},
      onToggleBookmark = {}
    )
  }
}