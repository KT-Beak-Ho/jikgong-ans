package com.billcorea.jikgong.presentation.company.main.projectlist.projectDetail.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.billcorea.jikgong.presentation.company.main.common.BackNavigationTopBar
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.billcorea.jikgong.api.models.sampleDataFactory.CompanyMockDataFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// 퇴근 체크용 근로자 데이터
data class CheckoutWorker(
  val id: String,
  val name: String,
  val age: Int,
  val gender: String, // "남", "여"
  var checkoutStatus: CheckoutStatus = CheckoutStatus.NONE
)

enum class CheckoutStatus {
  NONE,        // 아무것도 선택안됨
  EARLY_LEAVE, // 조퇴
  NORMAL_LEAVE // 정상퇴근
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
  navController: NavController,
  workDayId: String,
  selectedDate: String? = null,
  modifier: Modifier = Modifier
) {
  // 날짜별 확정 근로자 데이터
  val confirmedWorkersByDate = CompanyMockDataFactory.getConfirmedWorkersByDate().mapKeys { 
    LocalDate.parse(it.key) 
  }
  
  // 현재 선택된 날짜 (디버깅 정보 추가)
  val effectiveDate = try {
    val parsedDate = selectedDate?.takeIf { it.isNotBlank() }?.let { 
      LocalDate.parse(it) 
    } ?: LocalDate.parse("2025-08-01")
    
    println("=== CheckoutScreen Debug ===")
    println("selectedDate parameter: $selectedDate")
    println("effectiveDate: $parsedDate")
    println("===========================")
    
    parsedDate
  } catch (e: Exception) {
    println("Error parsing selectedDate: $selectedDate, using default date")
    LocalDate.parse("2025-08-01")
  }
  
  // 해당 날짜의 확정 근로자를 퇴근체크 데이터로 변환
  val workers = remember(effectiveDate) {
    val confirmedWorkers = confirmedWorkersByDate[effectiveDate] ?: emptyList()
    mutableStateListOf(
      *confirmedWorkers.map { worker ->
        CheckoutWorker(
          id = worker.id,
          name = worker.name,
          age = worker.age,
          gender = worker.gender,
          checkoutStatus = CheckoutStatus.NONE
        )
      }.toTypedArray()
    )
  }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    topBar = {
      BackNavigationTopBar(
        title = "퇴근확인 (${effectiveDate.format(DateTimeFormatter.ofPattern("MM/dd"))})",
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
      
      // 총 인원수와 안내 문구
      Column(
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
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
          text = "! 조퇴는 임금이 지급되지 않습니다",
          style = AppTypography.bodySmall,
          color = Color.Gray
        )
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
            CheckoutWorkerCard(
              worker = worker,
              onCheckoutChange = { newStatus ->
                val index = workers.indexOf(worker)
                if (index >= 0) {
                  workers[index] = worker.copy(checkoutStatus = newStatus)
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
private fun CheckoutWorkerCard(
  worker: CheckoutWorker,
  onCheckoutChange: (CheckoutStatus) -> Unit
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
      // 상단: 이름과 퇴근 상태
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
        
        // 퇴근 상태 표시
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = when (worker.checkoutStatus) {
            CheckoutStatus.EARLY_LEAVE -> Color(0xFFF44336) // 빨간색
            CheckoutStatus.NORMAL_LEAVE -> Color(0xFF2196F3) // 파란색
            CheckoutStatus.NONE -> Color(0xFF4CAF50) // 초록색 (기본)
          }
        ) {
          Text(
            text = when (worker.checkoutStatus) {
              CheckoutStatus.EARLY_LEAVE -> "조퇴"
              CheckoutStatus.NORMAL_LEAVE -> "정상퇴근"
              CheckoutStatus.NONE -> "출근 중"
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
        Text(
          text = "만 ${worker.age}세 • ${worker.gender}",
          style = AppTypography.bodyMedium,
          color = Color.Gray
        )
        
        // 우하단: 조퇴/퇴근 버튼
        Row(
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          // 조퇴 버튼
          OutlinedButton(
            onClick = { onCheckoutChange(CheckoutStatus.EARLY_LEAVE) },
            modifier = Modifier.height(32.dp),
            colors = ButtonDefaults.outlinedButtonColors(
              containerColor = if (worker.checkoutStatus == CheckoutStatus.EARLY_LEAVE) 
                appColorScheme.primary else Color.Transparent,
              contentColor = if (worker.checkoutStatus == CheckoutStatus.EARLY_LEAVE) 
                Color.White else Color.Black
            ),
            border = BorderStroke(1.dp, Color.Gray),
            shape = RoundedCornerShape(6.dp)
          ) {
            Text(
              text = "조퇴",
              style = AppTypography.bodySmall,
              fontWeight = FontWeight.Medium
            )
          }
          
          // 퇴근 버튼
          OutlinedButton(
            onClick = { onCheckoutChange(CheckoutStatus.NORMAL_LEAVE) },
            modifier = Modifier.height(32.dp),
            colors = ButtonDefaults.outlinedButtonColors(
              containerColor = if (worker.checkoutStatus == CheckoutStatus.NORMAL_LEAVE) 
                appColorScheme.primary else Color.Transparent,
              contentColor = if (worker.checkoutStatus == CheckoutStatus.NORMAL_LEAVE) 
                Color.White else Color.Black
            ),
            border = BorderStroke(1.dp, Color.Gray),
            shape = RoundedCornerShape(6.dp)
          ) {
            Text(
              text = "퇴근",
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
fun CheckoutScreenPreview() {
  Jikgong1111Theme {
    CheckoutScreen(
      navController = rememberNavController(),
      workDayId = "1",
      selectedDate = "2025-08-01"
    )
  }
}