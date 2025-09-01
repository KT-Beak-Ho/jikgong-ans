package com.billcorea.jikgong.presentation.company.main.projectlist.projectDetail.screen.worker

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
import com.billcorea.jikgong.network.data.CompanyMockDataFactory
import com.billcorea.jikgong.network.models.ConfirmedWorker
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
  // 날짜별 확정 근로자 데이터 (캐시됨)
  val confirmedWorkersByDate = CompanyMockDataFactory.getConfirmedWorkersByDate().mapKeys { 
    LocalDate.parse(it.key) 
  }
  
  // 데이터 일관성 테스트 (한 번만 실행)
  LaunchedEffect(Unit) {
    CompanyMockDataFactory.testDataConsistency()
  }
  
  // 현재 선택된 날짜 (실시간 업데이트를 위해 remember 제거)
  val effectiveDate = try {
    selectedDate?.takeIf { it.isNotBlank() }?.let { 
      LocalDate.parse(it) 
    } ?: LocalDate.parse("2025-08-01") // 데이터 범위 내 기본 날짜 사용
  } catch (e: Exception) {
    println("Error parsing selectedDate: $selectedDate, using default date 2025-08-01")
    LocalDate.parse("2025-08-01") // 에러 시에도 데이터 범위 내 날짜 사용
  }
  
  // 선택된 날짜에 따른 근로자 목록 (디버깅 정보 포함)
  val confirmedWorkers = confirmedWorkersByDate[effectiveDate] ?: emptyList()
  
  // 디버깅: 현재 데이터 상태 확인
  println("=== WorkerInfoScreen Debug ===")
  println("selectedDate parameter: $selectedDate")
  println("effectiveDate calculated: $effectiveDate")
  println("confirmedWorkers.size: ${confirmedWorkers.size}")
  println("confirmedWorkers.names: ${confirmedWorkers.map { it.name }}")
  println("all available dates: ${confirmedWorkersByDate.keys.sorted()}")
  println("data for this date exists: ${confirmedWorkersByDate.containsKey(effectiveDate)}")
  println("workDayId: $workDayId")
  println("=================================")

  // 제목에 날짜 정보 포함
  val titleWithDate = "출근확정 근로자 정보 (${effectiveDate.format(DateTimeFormatter.ofPattern("MM/dd"))})"

  Scaffold(
    modifier = modifier.fillMaxSize(),
    topBar = {
      BackNavigationTopBar(
        title = titleWithDate,
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
      
      // 선택된 날짜 정보 표시 (WorkerManagementScreen과 동일한 스타일)
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp)
      ) {
        Text(
          text = effectiveDate.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")),
          style = AppTypography.bodyMedium,
          fontWeight = FontWeight.Medium
        )
      }
      
      HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
      
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