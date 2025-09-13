package com.billcorea.jikgong.presentation.company.main.projectlist.feature.detail.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.presentation.company.main.common.BackNavigationTopBar
import com.billcorea.jikgong.api.models.sampleDataFactory.CompanyMockDataFactory
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.ConfirmedWorker
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerInfoScreen(
  navController: NavController,
  workDayId: String,
  selectedDate: String? = null,
  modifier: Modifier = Modifier
) {
  // WorkDay 기반으로 확정 근로자 데이터 조회
  val confirmedWorkers = remember(workDayId) {
    CompanyMockDataFactory.getConfirmedWorkersForWorkDay(workDayId)
  }
  
  // WorkDay 정보 조회 (제목과 날짜 표시용)
  val workDayInfo = remember(workDayId) {
    // 모든 프로젝트에서 해당 WorkDay 검색
    val allProjects = listOf("project_001", "project_002", "project_003", "project_004", "project_005", "project_006", "project_007")
    var foundWorkDay: com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.WorkDay? = null
    
    for (projectId in allProjects) {
      val workDays = CompanyMockDataFactory.getWorkDaysForProject(projectId)
      foundWorkDay = workDays.find { it.id == workDayId }
      if (foundWorkDay != null) break
    }
    foundWorkDay
  }
  
  // 데이터 일관성 테스트 (WorkDay 기반으로 변경)
  LaunchedEffect(Unit) {
    CompanyMockDataFactory.testWorkDayDataConsistency()
  }
  
  // WorkDay 날짜 정보 (fallback 처리)
  val workDayDate = workDayInfo?.date ?: LocalDate.now()
  val workDayTitle = workDayInfo?.title ?: "일자리 정보"
  
  // 디버깅: 현재 데이터 상태 확인
  println("=== WorkerInfoScreen Debug (WorkDay-based) ===")
  println("workDayId: $workDayId")
  println("workDayTitle: $workDayTitle")
  println("workDayDate: $workDayDate")
  println("confirmedWorkers.size: ${confirmedWorkers.size}")
  println("confirmedWorkers.names: ${confirmedWorkers.map { it.name }}")
  println("maxWorkers: ${workDayInfo?.maxWorkers ?: 0}")
  println("confirmed: ${workDayInfo?.confirmed ?: 0}")
  println("============================================")

  // 제목에 날짜와 일자리 정보 포함
  val titleWithInfo = "출근확정 근로자 (${workDayDate.format(DateTimeFormatter.ofPattern("MM/dd"))})"

  Scaffold(
    modifier = modifier.fillMaxSize(),
    topBar = {
      BackNavigationTopBar(
        title = titleWithInfo,
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
      
      // WorkDay 정보 표시
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp)
      ) {
        Column {
          Text(
            text = workDayDate.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")),
            style = AppTypography.bodyMedium,
            fontWeight = FontWeight.Medium
          )
          Text(
            text = workDayTitle,
            style = AppTypography.bodySmall,
            color = Color.Gray
          )
        }
      }
      
      HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
      
      // 총 인원수 표시 (WorkDay 기반 정보 포함)
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 12.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "출근 확정된 근로자 ",
            style = AppTypography.bodyMedium,
            color = Color.Gray
          )
          Text(
            text = "${confirmedWorkers.size}명",
            style = AppTypography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4B7BFF)
          )
          if (workDayInfo != null) {
            Text(
              text = " / ${workDayInfo.maxWorkers}명",
              style = AppTypography.bodyMedium,
              color = Color.Gray
            )
          }
        }
      }
      
      // 근로자 리스트
      if (confirmedWorkers.isEmpty()) {
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
              text = "이 일자리에 확정된 근로자가 없습니다",
              style = AppTypography.bodyLarge,
              color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = workDayTitle,
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
          items(confirmedWorkers) { worker ->
            WorkerCard(worker = worker)
          }
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
        
        // 기본 정보 (직종 정보 포함)
        Text(
          text = "${worker.jobType ?: "일반"} (${worker.skill ?: "중급"}) • 만 ${worker.age}세 • ${worker.gender} • 경력 ${worker.experience}년",
          style = AppTypography.bodyMedium,
          color = Color.Gray
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // 평점과 거리 정보
        Row(
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "⭐ ${String.format("%.1f", worker.rating)}",
            style = AppTypography.bodySmall,
            color = Color(0xFFFF9800)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "📍 ${String.format("%.1f", worker.distance)}km",
            style = AppTypography.bodySmall,
            color = Color.Gray
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = worker.workPreference ?: "혼합",
            style = AppTypography.bodySmall,
            color = Color(0xFF4B7BFF),
            fontWeight = FontWeight.Medium
          )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // 출역 정보와 전화번호
        Text(
          text = "총 출역 ${worker.totalWorkDays}회 | ${worker.phoneNumber}",
          style = AppTypography.bodySmall,
          color = Color.Gray
        )
        
        // 자격증 정보 (있을 경우만 표시)
        if (worker.certifications.isNotEmpty()) {
          Spacer(modifier = Modifier.height(4.dp))
          Text(
            text = "🏆 ${worker.certifications.joinToString(", ")}",
            style = AppTypography.bodySmall,
            color = Color(0xFF4CAF50),
            fontWeight = FontWeight.Medium
          )
        }
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
      workDayId = "1",
      selectedDate = "2025-08-01"
    )
  }
}

@Preview
@Composable
fun WorkerInfoScreenPreview2() {
  Jikgong1111Theme {
    WorkerInfoScreen(
      navController = rememberNavController(),
      workDayId = "1",
      selectedDate = "2025-08-02"
    )
  }
}

@Preview
@Composable
fun WorkerInfoScreenPreview3() {
  Jikgong1111Theme {
    WorkerInfoScreen(
      navController = rememberNavController(),
      workDayId = "1",
      selectedDate = "2025-08-04"
    )
  }
}

@Preview
@Composable
fun WorkerInfoScreenPreview4() {
  Jikgong1111Theme {
    WorkerInfoScreen(
      navController = rememberNavController(),
      workDayId = "1",
      selectedDate = "2025-08-07"
    )
  }
}