package com.billcorea.jikgong.presentation.company.main.projectlist.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.annotation.Destination
import com.billcorea.jikgong.presentation.company.main.common.CompanyBottomBar
import com.billcorea.jikgong.presentation.company.main.common.SimpleCompanyTopBar
import com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.screen.ProjectCreateDialog
import com.billcorea.jikgong.presentation.destinations.ProjectDetailScreenDestination
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.billcorea.jikgong.api.models.sampleDataFactory.CompanyMockDataFactory
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.SimpleProject
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun ProjectListScreen(
  navController: NavController,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableStateOf(0) }
  var showCreateDialog by remember { mutableStateOf(false) }

  // 중앙화된 샘플 데이터 사용
  val projects = remember {
    CompanyMockDataFactory.getSimpleProjects()
  }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    topBar = {
      SimpleCompanyTopBar(title = "프로젝트 목록")
    },
    bottomBar = {
      CompanyBottomBar(
        navController = navController,
        currentRoute = "project_list_screen"
      )
    },
    floatingActionButton = {
      ExtendedFloatingActionButton(
        onClick = { showCreateDialog = true },
        containerColor = appColorScheme.primary,
        contentColor = Color.White,
        modifier = Modifier.padding(bottom = 80.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Add,
          contentDescription = "프로젝트 등록"
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "새 프로젝트",
          style = AppTypography.bodyLarge,
          fontWeight = FontWeight.Medium
        )
      }
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .background(Color(0xFFF8F9FA))
    ) {
      // 탭
      val recruitingCount = projects.count { it.status == "RECRUITING" }
      val inProgressCount = projects.count { it.status == "IN_PROGRESS" }
      val completedCount = projects.count { it.status == "COMPLETED" }
      val totalCount = recruitingCount + inProgressCount + completedCount

      TabRow(
        selectedTabIndex = selectedTab,
        containerColor = Color.White
      ) {
        Tab(
          selected = selectedTab == 0,
          onClick = { selectedTab = 0 },
          text = { Text("전체 ($totalCount)") }
        )
        Tab(
          selected = selectedTab == 1,
          onClick = { selectedTab = 1 },
          text = { Text("모집중 ($recruitingCount)") }
        )
        Tab(
          selected = selectedTab == 2,
          onClick = { selectedTab = 2 },
          text = { Text("진행중 ($inProgressCount)") }
        )
        Tab(
          selected = selectedTab == 3,
          onClick = { selectedTab = 3 },
          text = { Text("완료 ($completedCount)") }
        )
      }

      // 모집중 프로젝트 헤더
      if (selectedTab == 0 || selectedTab == 1) {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(
            containerColor = appColorScheme.primaryContainer.copy(alpha = 0.3f)
          )
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "모집중 프로젝트",
              style = AppTypography.bodyLarge,
              fontWeight = FontWeight.Medium,
              color = appColorScheme.primary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "총 ${recruitingCount}개",
              style = AppTypography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = appColorScheme.primary
            )
          }
        }
      }

      // 프로젝트 목록
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        val filteredProjects = when (selectedTab) {
          1 -> projects.filter { it.status == "RECRUITING" }
          2 -> projects.filter { it.status == "IN_PROGRESS" }
          3 -> projects.filter { it.status == "COMPLETED" }
          else -> projects
        }

        items(filteredProjects) { project ->
          ProjectCard(
            project = project,
            onClick = {
              navController.navigate(ProjectDetailScreenDestination(projectId = project.id).route)
            },
            navController = navController
          )
        }
      }
    }
  }

  // 프로젝트 생성 다이얼로그
  if (showCreateDialog) {
    ProjectCreateDialog(
      onDismiss = { showCreateDialog = false },
      onConfirm = { projectData -> showCreateDialog = false
      }
    )
  }
}

@Composable
private fun ProjectCard(
  project: SimpleProject,
  onClick: () -> Unit,
  navController: NavController
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() },
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = Color.White
    ),
    elevation = CardDefaults.cardElevation(
      defaultElevation = 2.dp
    )
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      // 상단 헤더
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        Column(modifier = Modifier.weight(1f)) {
          // 상태 뱃지들
          Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 8.dp)
          ) {
            if (project.isUrgent) {
              Badge(
                containerColor = Color(0xFFFF5252),
                contentColor = Color.White
              ) {
                Text("긴급", fontSize = 12.sp)
              }
            }
            Badge(
              containerColor = when(project.status) {
                "RECRUITING" -> Color(0xFF2196F3)
                "IN_PROGRESS" -> Color(0xFF4CAF50)
                else -> Color(0xFF9E9E9E)
              },
              contentColor = Color.White
            ) {
              Text(
                when(project.status) {
                  "RECRUITING" -> "모집중"
                  "IN_PROGRESS" -> "진행중"
                  else -> "완료"
                },
                fontSize = 12.sp
              )
            }
            Badge(
              containerColor = MaterialTheme.colorScheme.secondaryContainer,
              contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            ) {
              Text(project.category, fontSize = 12.sp)
            }
          }

          Text(
            text = project.title,
            style = AppTypography.titleMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
          )
        }

        // 북마크 버튼
        IconButton(
          onClick = { /* 북마크 토글 */ },
          modifier = Modifier.size(24.dp)
        ) {
          Icon(
            imageVector = if (project.isBookmarked)
              Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
            contentDescription = "북마크",
            tint = if (project.isBookmarked)
              MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // 정보 섹션
      Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        InfoRow(
          icon = Icons.Outlined.Business,
          text = project.company
        )
        InfoRow(
          icon = Icons.Outlined.LocationOn,
          text = project.location
        )
        InfoRow(
          icon = Icons.Outlined.AttachMoney,
          text = "일당 ${NumberFormat.getNumberInstance(Locale.KOREA).format(project.wage)}원"
        )
        InfoRow(
          icon = Icons.Outlined.CalendarMonth,
          text = "${project.startDate} ~ ${project.endDate}"
        )
      }

      // 모집 현황 프로그레스
      Column {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              Icons.Outlined.Groups,
              contentDescription = null,
              modifier = Modifier.size(16.dp),
              tint = Color.Gray
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              "${project.currentApplicants}/${project.maxApplicants}명",
              fontSize = 13.sp,
              color = Color.Gray
            )
          }
          Text(
            "${(project.currentApplicants.toFloat() / project.maxApplicants * 100).toInt()}%",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF2196F3)
          )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
          progress = { project.currentApplicants.toFloat() / project.maxApplicants },
          modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(RoundedCornerShape(3.dp)),
          color = when {
            project.currentApplicants >= project.maxApplicants -> Color(0xFF4CAF50)
            project.currentApplicants >= project.maxApplicants * 0.8 -> Color(0xFFFFC107)
            else -> Color(0xFF2196F3)
          },
          trackColor = Color(0xFFE0E0E0)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
          onClick = { 
            navController.navigate(ProjectDetailScreenDestination(projectId = project.id).route)
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = appColorScheme.primary
          ),
          shape = RoundedCornerShape(8.dp)
        ) {
          Text(
            text = "프로젝트 관리",
            style = AppTypography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = Color.White
          )
        }
      }
    }
  }
}

@Composable
private fun InfoRow(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  text: String
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    Icon(
      imageVector = icon,
      contentDescription = null,
      modifier = Modifier.size(18.dp),
      tint = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Text(
      text = text,
      style = AppTypography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
  }
}

@Preview(showBackground = true)
@Composable
fun ProjectListScreenPreview() {
  Jikgong1111Theme {
    ProjectListScreen(
      navController = rememberNavController()
    )
  }
}