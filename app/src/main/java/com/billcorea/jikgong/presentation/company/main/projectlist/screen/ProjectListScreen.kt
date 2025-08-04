package com.billcorea.jikgong.presentation.company.main.projectlist.screen

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
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
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun ProjectListScreen(
  navController: NavController,
  viewModel: ProjectListViewModel = viewModel()
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val selectedFilter by viewModel.selectedFilter.collectAsStateWithLifecycle()
  val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

  var showCreateDialog by remember { mutableStateOf(false) }

  Scaffold(
    topBar = {
      ProjectListTopBar(
        stats = uiState.stats,
        onNotificationClick = { /* TODO */ },
        onSettingsClick = { /* TODO */ }
      )
    },
    floatingActionButton = {
      ExtendedFloatingActionButton(
        onClick = { showCreateDialog = true },
        modifier = Modifier
          .navigationBarsPadding()
          .padding(bottom = 80.dp),
        containerColor = Color(0xFF1E88E5),
        contentColor = Color.White,
        shape = RoundedCornerShape(16.dp),
        elevation = FloatingActionButtonDefaults.elevation(8.dp)
      ) {
        Icon(
          imageVector = Icons.Filled.Add,
          contentDescription = "프로젝트 생성",
          modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "프로젝트 생성",
          fontSize = 16.sp,
          fontWeight = FontWeight.Medium
        )
      }
    }
  ) { paddingValues ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFF5F5F5))
        .padding(paddingValues)
    ) {
      // 검색바 및 필터
      SearchAndFilterSection(
        searchQuery = searchQuery,
        selectedFilter = selectedFilter,
        onSearchQueryChange = viewModel::updateSearchQuery,
        onFilterSelect = viewModel::selectFilter
      )

      // 프로젝트 리스트
      when {
        uiState.isLoading -> {
          Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
          ) {
            CircularProgressIndicator(
              color = Color(0xFF1E88E5)
            )
          }
        }
        uiState.error != null -> {
          ErrorState(
            message = uiState.error,
            onRetry = viewModel::refreshProjects
          )
        }
        uiState.filteredProjects.isEmpty() -> {
          EmptyState(
            message = if (searchQuery.isNotEmpty()) {
              "검색 결과가 없습니다"
            } else {
              "등록된 프로젝트가 없습니다"
            }
          )
        }
        else -> {
          LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
              horizontal = 16.dp,
              vertical = 8.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            items(
              items = uiState.filteredProjects,
              key = { it.id }
            ) { project ->
              ProjectCard(
                project = project,
                onClick = {
                  navController.navigate("company/projectdetail/${project.id}")
                },
                onBookmarkClick = {
                  viewModel.toggleBookmark(project.id)
                }
              )
            }
          }
        }
      }
    }
  }

  if (showCreateDialog) {
    CreateProjectDialog(
      onDismiss = { showCreateDialog = false },
      onCreate = { /* TODO */ }
    )
  }
}

@Composable
private fun ProjectListTopBar(
  stats: ProjectStats,
  onNotificationClick: () -> Unit,
  onSettingsClick: () -> Unit
) {
  Surface(
    modifier = Modifier.fillMaxWidth(),
    color = Color.White,
    shadowElevation = 4.dp
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .statusBarsPadding()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "프로젝트 목록",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF212121)
          )
          Text(
            text = "총 ${stats.totalProjects}개 프로젝트",
            fontSize = 14.sp,
            color = Color(0xFF757575)
          )
        }

        Row(
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          IconButton(onClick = onNotificationClick) {
            BadgedBox(
              badge = {
                Badge(
                  containerColor = Color(0xFFFF5252)
                ) {
                  Text("3", fontSize = 10.sp)
                }
              }
            ) {
              Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "알림",
                tint = Color(0xFF424242)
              )
            }
          }
          IconButton(onClick = onSettingsClick) {
            Icon(
              imageVector = Icons.Outlined.Settings,
              contentDescription = "설정",
              tint = Color(0xFF424242)
            )
          }
        }
      }

      // 통계 카드
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp)
          .padding(bottom = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        StatsCard(
          modifier = Modifier.weight(1f),
          title = "모집중",
          value = stats.recruitingProjects.toString(),
          color = Color(0xFF4CAF50)
        )
        StatsCard(
          modifier = Modifier.weight(1f),
          title = "진행중",
          value = stats.activeProjects.toString(),
          color = Color(0xFF2196F3)
        )
        StatsCard(
          modifier = Modifier.weight(1f),
          title = "평균 일당",
          value = NumberFormat.getInstance(Locale.KOREA)
            .format(stats.averageWage) + "원",
          color = Color(0xFFFF9800)
        )
      }
    }
  }
}

@Composable
private fun StatsCard(
  title: String,
  value: String,
  color: Color,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier,
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(
      containerColor = color.copy(alpha = 0.1f)
    ),
    elevation = CardDefaults.cardElevation(0.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = title,
        fontSize = 12.sp,
        color = Color(0xFF616161)
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = value,
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = color
      )
    }
  }
}

@Composable
private fun SearchAndFilterSection(
  searchQuery: String,
  selectedFilter: ProjectFilter,
  onSearchQueryChange: (String) -> Unit,
  onFilterSelect: (ProjectFilter) -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .background(Color.White)
      .padding(16.dp)
  ) {
    // 검색바
    OutlinedTextField(
      value = searchQuery,
      onValueChange = onSearchQueryChange,
      modifier = Modifier.fillMaxWidth(),
      placeholder = {
        Text(
          text = "프로젝트명, 회사명, 위치로 검색",
          color = Color(0xFF9E9E9E)
        )
      },
      leadingIcon = {
        Icon(
          imageVector = Icons.Filled.Search,
          contentDescription = "검색",
          tint = Color(0xFF757575)
        )
      },
      trailingIcon = {
        if (searchQuery.isNotEmpty()) {
          IconButton(onClick = { onSearchQueryChange("") }) {
            Icon(
              imageVector = Icons.Filled.Clear,
              contentDescription = "지우기",
              tint = Color(0xFF757575)
            )
          }
        }
      },
      shape = RoundedCornerShape(12.dp),
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color(0xFF1E88E5),
        unfocusedBorderColor = Color(0xFFE0E0E0)
      ),
      singleLine = true
    )

    Spacer(modifier = Modifier.height(12.dp))

    // 필터 칩
    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      items(ProjectFilter.values().size) { index ->
        val filter = ProjectFilter.values()[index]
        FilterChip(
          selected = selectedFilter == filter,
          onClick = { onFilterSelect(filter) },
          label = {
            Text(
              text = filter.displayName,
              fontSize = 14.sp
            )
          },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Color(0xFF1E88E5),
            selectedLabelColor = Color.White,
            containerColor = Color(0xFFF5F5F5),
            labelColor = Color(0xFF616161)
          ),
          border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selectedFilter == filter,
            borderColor = Color.Transparent,
            selectedBorderColor = Color.Transparent
          )
        )
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProjectCard(
  project: Project,
  onClick: () -> Unit,
  onBookmarkClick: () -> Unit
) {
  Card(
    onClick = onClick,
    modifier = Modifier
      .fillMaxWidth()
      .shadow(
        elevation = 4.dp,
        shape = RoundedCornerShape(16.dp),
        spotColor = Color(0x1A000000)
      ),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = Color.White
    )
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      // 헤더
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            if (project.isUrgent) {
              Badge(
                containerColor = Color(0xFFFF5252),
                contentColor = Color.White
              ) {
                Text(
                  text = "긴급",
                  fontSize = 11.sp,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }
            StatusBadge(status = project.status)
          }

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = project.title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF212121),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
          )

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = project.company,
            fontSize = 14.sp,
            color = Color(0xFF757575)
          )
        }

        IconButton(
          onClick = onBookmarkClick,
          modifier = Modifier.size(32.dp)
        ) {
          Icon(
            imageVector = if (project.isBookmarked) {
              Icons.Filled.Bookmark
            } else {
              Icons.Outlined.BookmarkBorder
            },
            contentDescription = "북마크",
            tint = if (project.isBookmarked) {
              Color(0xFFFFD600)
            } else {
              Color(0xFF9E9E9E)
            }
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // 정보 섹션
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        InfoItem(
          icon = Icons.Outlined.LocationOn,
          text = project.location
        )
        InfoItem(
          icon = Icons.Outlined.DateRange,
          text = "${project.startDate.format(DateTimeFormatter.ofPattern("MM/dd"))} ~ ${
            project.endDate.format(DateTimeFormatter.ofPattern("MM/dd"))
          }"
        )
      }

      Spacer(modifier = Modifier.height(8.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        InfoItem(
          icon = Icons.Outlined.Groups,
          text = "${project.currentApplicants}/${project.maxApplicants}명"
        )
        InfoItem(
          icon = Icons.Outlined.AttachMoney,
          text = "${NumberFormat.getInstance(Locale.KOREA).format(project.wage)}원/일"
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      // 하단 액션
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        OutlinedButton(
          onClick = { /* TODO: 지원자 보기 */ },
          modifier = Modifier.weight(1f),
          shape = RoundedCornerShape(8.dp),
          border = BorderStroke(1.dp, Color(0xFFE0E0E0))
        ) {
          Icon(
            imageVector = Icons.Outlined.People,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "지원자 보기",
            fontSize = 14.sp
          )
        }

        Button(
          onClick = { /* TODO: 프로젝트 관리 */ },
          modifier = Modifier.weight(1f),
          shape = RoundedCornerShape(8.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF1E88E5)
          )
        ) {
          Icon(
            imageVector = Icons.Outlined.Edit,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "프로젝트 관리",
            fontSize = 14.sp
          )
        }
      }
    }
  }
}

@Composable
private fun StatusBadge(status: String) {
  val (text, color) = when (status) {
    "RECRUITING" -> "모집중" to Color(0xFF4CAF50)
    "IN_PROGRESS" -> "진행중" to Color(0xFF2196F3)
    "COMPLETED" -> "완료" to Color(0xFF9E9E9E)
    else -> "기타" to Color(0xFF757575)
  }

  Surface(
    shape = RoundedCornerShape(4.dp),
    color = color.copy(alpha = 0.1f)
  ) {
    Text(
      text = text,
      fontSize = 12.sp,
      fontWeight = FontWeight.Medium,
      color = color,
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    )
  }
}

@Composable
private fun InfoItem(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  text: String
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(4.dp)
  ) {
    Icon(
      imageVector = icon,
      contentDescription = null,
      tint = Color(0xFF757575),
      modifier = Modifier.size(16.dp)
    )
    Text(
      text = text,
      fontSize = 13.sp,
      color = Color(0xFF616161),
      maxLines = 1,
      overflow = TextOverflow.Ellipsis
    )
  }
}

@Composable
private fun EmptyState(message: String) {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .padding(32.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      Icon(
        imageVector = Icons.Outlined.FolderOpen,
        contentDescription = null,
        modifier = Modifier.size(64.dp),
        tint = Color(0xFFBDBDBD)
      )
      Text(
        text = message,
        fontSize = 16.sp,
        color = Color(0xFF757575),
        textAlign = TextAlign.Center
      )
    }
  }
}

@Composable
private fun ErrorState(
  message: String,
  onRetry: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .padding(32.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      Icon(
        imageVector = Icons.Outlined.ErrorOutline,
        contentDescription = null,
        modifier = Modifier.size(64.dp),
        tint = Color(0xFFFF5252)
      )
      Text(
        text = message,
        fontSize = 16.sp,
        color = Color(0xFF757575),
        textAlign = TextAlign.Center
      )
      Button(
        onClick = onRetry,
        colors = ButtonDefaults.buttonColors(
          containerColor = Color(0xFF1E88E5)
        )
      ) {
        Text("다시 시도")
      }
    }
  }
}

@Composable
private fun CreateProjectDialog(
  onDismiss: () -> Unit,
  onCreate: () -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = "새 프로젝트 생성",
        fontWeight = FontWeight.Bold
      )
    },
    text = {
      Text("프로젝트 생성 기능은 준비 중입니다.")
    },
    confirmButton = {
      TextButton(onClick = onDismiss) {
        Text("확인")
      }
    }
  )
}

@Preview(showBackground = true)
@Composable
fun ProjectListScreenPreview() {
  ProjectListScreen(navController = rememberNavController())
}