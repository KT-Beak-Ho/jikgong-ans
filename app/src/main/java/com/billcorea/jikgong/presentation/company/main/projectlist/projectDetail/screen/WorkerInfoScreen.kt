package com.billcorea.jikgong.presentation.company.main.projectlist.projectDetail.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.billcorea.jikgong.presentation.company.main.common.BackNavigationTopBar

// 확정 근로자 데이터
data class ConfirmedWorker(
  val id: String,
  val name: String,
  val age: Int,
  val gender: String, // "남", "여"
  val experience: Int, // 경력 년수
  val attendanceRate: Int, // 출석률 (0-100)
  val totalWorkDays: Int, // 총 출역 회수
  val phoneNumber: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerInfoScreen(
  navController: NavController,
  workDayId: String,
  modifier: Modifier = Modifier
) {
  // 샘플 확정 근로자 데이터
  val confirmedWorkers = remember {
    listOf(
      ConfirmedWorker("1", "김철수", 30, "남", 5, 95, 24, "010-1234-5678"),
      ConfirmedWorker("2", "이영희", 28, "여", 3, 88, 18, "010-2345-6789"),
      ConfirmedWorker("3", "박민수", 35, "남", 8, 92, 35, "010-3456-7890"),
      ConfirmedWorker("4", "정수연", 25, "여", 2, 85, 12, "010-4567-8901"),
      ConfirmedWorker("5", "최동현", 42, "남", 12, 98, 48, "010-5678-9012"),
      ConfirmedWorker("6", "한미영", 33, "여", 6, 90, 28, "010-6789-0123"),
      ConfirmedWorker("7", "장준호", 29, "남", 4, 75, 20, "010-7890-1234"),
      ConfirmedWorker("8", "윤서진", 31, "여", 7, 93, 32, "010-8901-2345")
    )
  }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    topBar = {
      BackNavigationTopBar(
        title = "출근확정 근로자 정보",
        onBackClick = { navController.popBackStack() }
      )
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .background(Color(0xFFF8F9FA))
    ) {
      Divider(thickness = 1.dp, color = Color.LightGray)
      
      // 총 인원수 표시
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 12.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "총 ",
            style = AppTypography.bodyMedium,
            color = Color.Gray
          )
          Text(
            text = "${confirmedWorkers.size}명",
            style = AppTypography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4B7BFF)
          )
        }
      }
      
      // 근로자 리스트
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        items(confirmedWorkers) { worker ->
          WorkerCard(worker = worker)
        }
      }
    }
  }
}

@Composable
private fun WorkerCard(
  worker: ConfirmedWorker
) {
  // 출석률에 따른 배경색 결정
  fun getAttendanceBadgeColor(rate: Int): Color {
    return when {
      rate >= 90 -> Color(0xFF4CAF50) // 초록색
      rate >= 80 -> Color(0xFF2196F3) // 파란색
      rate >= 70 -> Color(0xFFFF9800) // 주황색
      else -> Color(0xFFF44336) // 빨간색
    }
  }

  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.Top
    ) {
      // 아바타
      Box(
        modifier = Modifier
          .size(48.dp)
          .clip(CircleShape)
          .background(Color(0xFF4B7BFF).copy(alpha = 0.1f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          Icons.Default.Person,
          contentDescription = null,
          modifier = Modifier.size(28.dp),
          tint = Color(0xFF4B7BFF)
        )
      }
      
      Spacer(modifier = Modifier.width(12.dp))
      
      // 근로자 정보
      Column(
        modifier = Modifier.weight(1f)
      ) {
        // 이름과 출석률
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = worker.name,
            style = AppTypography.titleMedium,
            fontWeight = FontWeight.Bold
          )
          
          // 출석률 뱃지
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = getAttendanceBadgeColor(worker.attendanceRate)
          ) {
            Text(
              text = "${worker.attendanceRate}% 출석",
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              style = AppTypography.bodySmall,
              color = Color.White,
              fontWeight = FontWeight.Medium
            )
          }
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // 기본 정보
        Text(
          text = "만 ${worker.age}세 • ${worker.gender} • 경력 ${worker.experience}년",
          style = AppTypography.bodyMedium,
          color = Color.Gray
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // 출역 정보와 전화번호
        Text(
          text = "총 출역 ${worker.totalWorkDays}회 | ${worker.phoneNumber}",
          style = AppTypography.bodySmall,
          color = Color.Gray
        )
      }
    }
  }
}

@Preview
@Composable
fun WorkerInfoScreenPreview() {
  Jikgong1111Theme {
    WorkerInfoScreen(
      navController = rememberNavController(),
      workDayId = "1"
    )
  }
}