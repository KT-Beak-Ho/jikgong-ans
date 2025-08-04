// app/src/main/java/com/billcorea/jikgong/presentation/company/main/projectlist/ProjectListScreen.kt
package com.billcorea.jikgong.presentation.company.main.projectlist

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
import com.billcorea.jikgong.ui.theme.appColorScheme
import java.text.NumberFormat
import java.util.*

// 임시 데이터 클래스
data class SimpleProject(
  val id: String,
  val title: String,
  val location: String,
  val status: String,
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

  // 샘플 데이터
  val projects = remember {
    listOf(
      SimpleProject(
        id = "1",
        title = "사하구 낙동5블럭 낙동강 온도 측정 센터 신축공사",
        location = "부산 사하구",
        status = "RECRUITING",
        wage = 510000,
        currentApplicants = 3,
        maxApplicants = 15,
        isUrgent = true
      ),
      SimpleProject(
        id = "2",
        title = "인천 물류센터 건설",
        location = "인천 연수구",
        status = "IN_PROGRESS",
        wage = 300000,
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
          text = "프로젝트 등록",
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
          text = { Text("모집중 (2)") }
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
              text = "총 2개",
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
        items(projects) { project ->
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
          Text(
            text = project.title,
            style = AppTypography.titleMedium,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
          )
          Spacer(modifier = Modifier.height(4.dp))
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Outlined.LocationOn,
              contentDescription = null,
              modifier = Modifier.size(16.dp),
              tint = Color.Gray
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = project.location,
              style = AppTypography.bodyMedium,
              color = Color.Gray
            )
          }
        }

        // 상태 뱃지
        Column(
          horizontalAlignment = Alignment.End,
          verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          if (project.isUrgent) {
            Surface(
              shape = RoundedCornerShape(4.dp),
              color = Color(0xFFFFEBEE)
            ) {
              Text(
                text = "긴급",
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                style = AppTypography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFD32F2F)
              )
            }
          }

          val statusText = when (project.status) {
            "RECRUITING" -> "모집중"
            "IN_PROGRESS" -> "진행중"
            else -> "완료"
          }
          val statusColor = when (project.status) {
            "RECRUITING" -> Color(0xFF1976D2)
            "IN_PROGRESS" -> Color(0xFFF57C00)
            else -> Color(0xFF388E3C)
          }

          Surface(
            shape = RoundedCornerShape(4.dp),
            color = statusColor.copy(alpha = 0.1f)
          ) {
            Text(
              text = statusText,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              style = AppTypography.labelMedium,
              fontWeight = FontWeight.Medium,
              color = statusColor
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // 프로젝트 정보
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Outlined.Group,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = Color.Gray
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "${project.currentApplicants}/${project.maxApplicants}명",
            style = AppTypography.bodySmall,
            color = if (project.currentApplicants >= project.maxApplicants)
              Color(0xFFE91E63) else Color.Gray
          )
        }

        Text(
          text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(project.wage)}원/일",
          style = AppTypography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = appColorScheme.primary
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      // 액션 버튼
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        OutlinedButton(
          onClick = { },
          modifier = Modifier.weight(1f),
          shape = RoundedCornerShape(8.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text("수정")
        }

        Button(
          onClick = { },
          modifier = Modifier.weight(1f),
          shape = RoundedCornerShape(8.dp),
          enabled = project.status == "RECRUITING"
        ) {
          Icon(
            imageVector = Icons.Default.PersonAdd,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text("인력 모집")
        }
      }
    }
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ProjectListScreenPreview() {
  com.billcorea.jikgong.ui.theme.Jikgong1111Theme {
    // Preview에서는 실제 NavController 대신 mock 사용
    ProjectListScreen(
      navController = rememberNavController()
    )
  }
}