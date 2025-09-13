package com.billcorea.jikgong.presentation.company.main.projectlist.feature.detail.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
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
import com.billcorea.jikgong.presentation.company.main.common.BackNavigationTopBar
import com.billcorea.jikgong.api.models.sampleDataFactory.CompanyMockDataFactory
import com.billcorea.jikgong.presentation.company.main.projectlist.data.AttendanceWorker
import com.billcorea.jikgong.presentation.company.main.projectlist.data.AttendanceStatus
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceCheckScreen(
  navController: NavController,
  workDayId: String,
  selectedDate: String? = null,
  modifier: Modifier = Modifier
) {
  // WorkDay 기반 확정 근로자 데이터
  val confirmedWorkers = remember(workDayId) {
    CompanyMockDataFactory.getConfirmedWorkersForWorkDay(workDayId)
  }
  
  // 현재 선택된 날짜 (디버깅 정보 추가)
  val effectiveDate = try {
    val parsedDate = selectedDate?.takeIf { it.isNotBlank() }?.let { 
      LocalDate.parse(it) 
    } ?: LocalDate.parse("2025-08-01")
    
    println("=== AttendanceCheckScreen Debug ===")
    println("selectedDate parameter: $selectedDate")
    println("effectiveDate: $parsedDate")
    println("===================================")
    
    parsedDate
  } catch (e: Exception) {
    println("Error parsing selectedDate: $selectedDate, using default date")
    LocalDate.parse("2025-08-01")
  }
  
  // 확정 근로자를 출근체크 데이터로 변환
  val workers = remember(workDayId, confirmedWorkers) {
    mutableStateListOf(
      *confirmedWorkers.map { worker ->
        AttendanceWorker(
          id = worker.id,
          name = worker.name,
          age = worker.age,
          gender = worker.gender,
          phoneNumber = worker.phoneNumber,
          attendanceStatus = AttendanceStatus.NONE
        )
      }.toTypedArray()
    )
  }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    topBar = {
      BackNavigationTopBar(
        title = "출근확인 (${effectiveDate.format(DateTimeFormatter.ofPattern("MM/dd"))})",
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
      HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
      
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
      if (workers.isEmpty()) {
        // 확정 근로자가 없는 경우 안내 메시지
        Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 48.dp),
          contentAlignment = Alignment.Center
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(
              text = "${effectiveDate.format(DateTimeFormatter.ofPattern("MM월 dd일"))}에 확정된 근로자가 없습니다",
              style = AppTypography.bodyLarge,
              color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "다른 날짜를 선택해보세요",
              style = AppTypography.bodySmall,
              color = Color.Gray
            )
          }
        }
      } else {
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
            AttendanceStatus.NONE -> Color(0xFF4B7BFF) // 파란색 (기본)
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
      workDayId = "1",
      selectedDate = "2025-08-01"
    )
  }
}