// 📍 경로: app/src/main/java/com/billcorea/jikgong/presentation/company/main/projectlist/screen/ProjectListScreen.kt

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
import com.billcorea.jikgong.presentation.company.common.CompanyBottomBar
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import java.text.NumberFormat
import java.util.*

// 기존 paste.txt의 SimpleProject 데이터 클래스 사용
data class SimpleProject(
  val id: String,
  val title: String,
  val company: String,
  val location: String,
  val category: String,
  val status: String,
  val startDate: String,
  val endDate: String,
  val wage: Int,
  val currentApplicants: Int,
  val maxApplicants: Int,
  val isUrgent: Boolean = false,
  val isBookmarked: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectListScreen(
  navController: NavController,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableStateOf(0) }
  var showCreateDialog by remember { mutableStateOf(false) }

  // 샘플 데이터 (기존 paste.txt의 mockProjects와 동일)
  val projects = remember {
    listOf(
      SimpleProject(
        id = "1",
        title = "아파트 신축공사 철근 작업자 모집",
        company = "대한건설(주)",
        location = "서울시 강남구 역삼동",
        category = "철근공",
        status = "RECRUITING",
        startDate = "2025-08-08",
        endDate = "2025-09-20",
        wage = 200000,
        currentApplicants = 8,
        maxApplicants = 15,
        isUrgent = true
      ),
      SimpleProject(
        id = "2",
        title = "사무실 인테리어 목공 인력 모집",
        company = "현대인테리어",
        location = "서울시 종로구",
        category = "목공",
        status = "IN_PROGRESS",
        startDate = "2025-08-10",
        endDate = "2025-08-25",
        wage = 180000,
        currentApplicants = 12,
        maxApplicants = 12,
        isBookmarked = true
      )
    )
  }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    bottomBar = {
      CompanyBottomBar(
        navController = navController,
        currentRoute = "company/projectlist"
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
      // 상단 제목
      TopAppBar(
        title = {
          Text(
            text = "프로젝트 목록",
            style = AppTypography.titleLarge,
            fontWeight = FontWeight.Bold
          )
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = Color.White
        )
      )

      // 탭
      TabRow(
        selectedTabIndex = selectedTab,
        containerColor = Color.White
      ) {
        Tab(
          selected = selectedTab == 0,
          onClick = { selectedTab = 0 },
          text = { Text("전체 (${projects.size})") }
        )
        Tab(
          selected = selectedTab == 1,
          onClick = { selectedTab = 1 },
          text = { Text("모집중 (1)") }
        )
        Tab(
          selected = selectedTab == 2,
          onClick = { selectedTab = 2 },
          text = { Text("진행중 (1)") }
        )
        Tab(
          selected = selectedTab == 3,
          onClick = { selectedTab = 3 },
          text = { Text("완료 (0)") }
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
              text = "총 ${projects.count { it.status == "RECRUITING" }}개",
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
              navController.navigate("company/projectdetail/${project.id}")
            }
          )
        }
      }
    }
  }

  // 프로젝트 생성 다이얼로그
  if (showCreateDialog) {
    AlertDialog(
      onDismissRequest = { showCreateDialog = false },
      title = {
        Text(
          text = "프로젝트 등록",
          style = AppTypography.titleLarge,
          fontWeight = FontWeight.Bold
        )
      },
      text = {
        Text(
          text = "새로운 프로젝트를 등록하시겠습니까?",
          style = AppTypography.bodyMedium
        )
      },
      confirmButton = {
        TextButton(
          onClick = {
            showCreateDialog = false
            navController.navigate("company/projectcreate")
          }
        ) {
          Text("등록하기", color = appColorScheme.primary)
        }
      },
      dismissButton = {
        TextButton(onClick = { showCreateDialog = false }) {
          Text("취소")
        }
      }
    )
  }
}

@Composable
private fun ProjectCard(
  project: SimpleProject,
  onClick: () -> Unit
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

      Spacer(modifier = Modifier.height(12.dp))

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