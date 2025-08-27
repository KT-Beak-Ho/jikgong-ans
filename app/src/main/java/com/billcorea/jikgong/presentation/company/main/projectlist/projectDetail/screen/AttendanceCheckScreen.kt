package com.billcorea.jikgong.presentation.company.main.projectlist.projectDetail.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme

// 출근 체크용 근로자 데이터
data class AttendanceWorker(
  val id: String,
  val name: String,
  val age: Int,
  val gender: String, // "남", "여"
  val phoneNumber: String,
  var attendanceStatus: AttendanceStatus = AttendanceStatus.NONE
)

enum class AttendanceStatus {
  NONE,        // 아무것도 선택안됨 (출근전)
  NOT_ARRIVED, // 결근
  ARRIVED      // 출근
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceCheckScreen(
  navController: NavController,
  workDayId: String,
  modifier: Modifier = Modifier
) {
  // 샘플 근로자 데이터 (remember로 상태 유지)
  val workers = remember {
    mutableStateListOf(
      AttendanceWorker("1", "김철수", 30, "남", "010-1234-5678", AttendanceStatus.ARRIVED),
      AttendanceWorker("2", "이영희", 28, "여", "010-2345-6789", AttendanceStatus.NONE),
      AttendanceWorker("3", "박민수", 35, "남", "010-3456-7890", AttendanceStatus.NOT_ARRIVED),
      AttendanceWorker("4", "정수연", 25, "여", "010-4567-8901", AttendanceStatus.NONE),
      AttendanceWorker("5", "최동현", 42, "남", "010-5678-9012", AttendanceStatus.ARRIVED),
      AttendanceWorker("6", "한미영", 33, "여", "010-6789-0123", AttendanceStatus.NOT_ARRIVED),
      AttendanceWorker("7", "장준호", 29, "남", "010-7890-1234", AttendanceStatus.NONE),
      AttendanceWorker("8", "윤서진", 31, "여", "010-8901-2345", AttendanceStatus.ARRIVED)
    )
  }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "출근확인",
            style = AppTypography.titleLarge,
            fontWeight = FontWeight.Bold
          )
        },
        navigationIcon = {
          IconButton(onClick = { navController.popBackStack() }) {
            Icon(
              Icons.Default.ArrowBack,
              contentDescription = "뒤로가기"
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = Color.White
        )
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
            text = "${workers.size}명",
            style = AppTypography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = appColorScheme.primary
          )
        }
      }
      
      // 근로자 리스트
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        items(workers) { worker ->
          AttendanceWorkerCard(
            worker = worker,
            onAttendanceChange = { newStatus ->
              val index = workers.indexOf(worker)
              if (index >= 0) {
                workers[index] = worker.copy(attendanceStatus = newStatus)
              }
            }
          )
        }
      }
    }
  }
}

@Composable
private fun AttendanceWorkerCard(
  worker: AttendanceWorker,
  onAttendanceChange: (AttendanceStatus) -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      // 상단: 이름과 출근 상태
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
        
        // 출근 상태 뱃지
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = when (worker.attendanceStatus) {
            AttendanceStatus.NONE -> Color(0xFF2196F3) // 파란색 (기본)
            AttendanceStatus.NOT_ARRIVED -> Color(0xFFF44336) // 빨간색
            AttendanceStatus.ARRIVED -> Color(0xFF4CAF50) // 초록색
          }
        ) {
          Text(
            text = when (worker.attendanceStatus) {
              AttendanceStatus.NONE -> "출근 전"
              AttendanceStatus.NOT_ARRIVED -> "결근"
              AttendanceStatus.ARRIVED -> "출근"
            },
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 3.dp),
            style = AppTypography.bodySmall,
            color = Color.White,
            fontWeight = FontWeight.Medium
          )
        }
      }
      
      Spacer(modifier = Modifier.height(12.dp))
      
      // 하단: 기본 정보와 버튼들
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // 좌하단: 기본 정보
        Column {
          Text(
            text = "만 ${worker.age}세 • ${worker.gender}",
            style = AppTypography.bodyMedium,
            color = Color.Gray
          )
          Text(
            text = worker.phoneNumber,
            style = AppTypography.bodySmall,
            color = Color.Gray
          )
        }
        
        // 우하단: 출근/결근 버튼
        Row(
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          // 결근 버튼
          OutlinedButton(
            onClick = { onAttendanceChange(AttendanceStatus.NOT_ARRIVED) },
            modifier = Modifier.height(32.dp),
            colors = ButtonDefaults.outlinedButtonColors(
              containerColor = if (worker.attendanceStatus == AttendanceStatus.NOT_ARRIVED) 
                appColorScheme.primary else Color.Transparent,
              contentColor = if (worker.attendanceStatus == AttendanceStatus.NOT_ARRIVED) 
                Color.White else Color.Black
            ),
            border = BorderStroke(1.dp, Color.Gray),
            shape = RoundedCornerShape(6.dp)
          ) {
            Text(
              text = "결근",
              style = AppTypography.bodySmall,
              fontWeight = FontWeight.Medium
            )
          }
          
          // 출근 버튼
          OutlinedButton(
            onClick = { onAttendanceChange(AttendanceStatus.ARRIVED) },
            modifier = Modifier.height(32.dp),
            colors = ButtonDefaults.outlinedButtonColors(
              containerColor = if (worker.attendanceStatus == AttendanceStatus.ARRIVED) 
                appColorScheme.primary else Color.Transparent,
              contentColor = if (worker.attendanceStatus == AttendanceStatus.ARRIVED) 
                Color.White else Color.Black
            ),
            border = BorderStroke(1.dp, Color.Gray),
            shape = RoundedCornerShape(6.dp)
          ) {
            Text(
              text = "출근",
              style = AppTypography.bodySmall,
              fontWeight = FontWeight.Medium
            )
          }
        }
      }
    }
  }
}

@Preview
@Composable
fun AttendanceCheckScreenPreview() {
  Jikgong1111Theme {
    AttendanceCheckScreen(
      navController = rememberNavController(),
      workDayId = "1"
    )
  }
}