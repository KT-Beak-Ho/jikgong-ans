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
import java.text.NumberFormat
import java.util.*

// 지급 근로자 데이터
data class PaymentWorker(
  val id: String,
  val name: String,
  val age: Int,
  val gender: String, // "남", "여"
  val paymentAmount: Int, // 지급 금액 (0이면 미지급)
  val attendanceStatus: String // "정상출근", "조퇴", "결근"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentSummaryScreen(
  navController: NavController,
  workDayId: String,
  modifier: Modifier = Modifier
) {
  // 샘플 근로자 데이터
  val workers = remember {
    listOf(
      PaymentWorker("1", "김철수", 30, "남", 200000, "정상출근"),
      PaymentWorker("2", "이영희", 28, "여", 200000, "정상출근"),
      PaymentWorker("3", "박민수", 35, "남", 200000, "정상출근"),
      PaymentWorker("4", "정수연", 25, "여", 200000, "정상출근"),
      PaymentWorker("5", "최동현", 42, "남", 200000, "정상출근"),
      PaymentWorker("6", "한미영", 33, "여", 0, "조퇴"),
      PaymentWorker("7", "장준호", 29, "남", 0, "결근"),
      PaymentWorker("8", "윤서진", 31, "여", 0, "조퇴")
    )
  }
  
  // 정상출근자와 조퇴/결근자 분리
  val normalAttendanceWorkers = workers.filter { it.paymentAmount > 0 }
  val absentWorkers = workers.filter { it.paymentAmount == 0 }
  
  // 총 지급액 계산
  val totalPayment = normalAttendanceWorkers.sumOf { it.paymentAmount }
  
  // 숫자 포맷터
  val numberFormat = NumberFormat.getNumberInstance(Locale.KOREA)

  Scaffold(
    modifier = modifier.fillMaxSize(),
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "지급내역서 보기",
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
    },
    bottomBar = {
      // 하단 버튼
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        OutlinedButton(
          onClick = { /* TODO: 변경요청 처리 */ },
          modifier = Modifier.weight(1f),
          shape = RoundedCornerShape(8.dp)
        ) {
          Text(
            text = "변경요청",
            style = AppTypography.bodyMedium,
            fontWeight = FontWeight.Medium
          )
        }
        
        Button(
          onClick = { /* TODO: 입금 처리 */ },
          modifier = Modifier.weight(1f),
          shape = RoundedCornerShape(8.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = appColorScheme.primary
          )
        ) {
          Text(
            text = "이대로 입금할게요",
            style = AppTypography.bodyMedium,
            fontWeight = FontWeight.Medium
          )
        }
      }
    }
  ) { innerPadding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .background(Color(0xFFF8F9FA)),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // 지급 총액
      item {
        Column {
          Divider(thickness = 1.dp, color = Color.LightGray)
          Spacer(modifier = Modifier.height(16.dp))
          
          Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
          ) {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
              contentAlignment = Alignment.Center
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "지급 총액 ",
                  style = AppTypography.titleLarge,
                  color = Color.Black
                )
                Text(
                  text = "${numberFormat.format(totalPayment)}원",
                  style = AppTypography.titleLarge,
                  fontWeight = FontWeight.Bold,
                  color = appColorScheme.primary
                )
              }
            }
          }
          
          Spacer(modifier = Modifier.height(16.dp))
        }
      }
      
      // 정상출근 섹션
      if (normalAttendanceWorkers.isNotEmpty()) {
        item {
          Row(
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "정상출근 ",
              style = AppTypography.bodyMedium,
              color = Color.Black
            )
            Text(
              text = "${normalAttendanceWorkers.size}명",
              style = AppTypography.bodyMedium,
              fontWeight = FontWeight.Bold,
              color = appColorScheme.primary
            )
          }
        }
        
        items(normalAttendanceWorkers) { worker ->
          PaymentWorkerCard(worker = worker)
        }
      }
      
      // 조퇴 및 결근 섹션
      if (absentWorkers.isNotEmpty()) {
        item {
          Spacer(modifier = Modifier.height(8.dp))
          Row(
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "조퇴 및 결근 ",
              style = AppTypography.bodyMedium,
              color = Color.Black
            )
            Text(
              text = "${absentWorkers.size}명",
              style = AppTypography.bodyMedium,
              fontWeight = FontWeight.Bold,
              color = Color(0xFFF44336) // 빨간색
            )
          }
        }
        
        items(absentWorkers) { worker ->
          PaymentWorkerCard(worker = worker)
        }
      }
      
      // 하단 여백 (버튼 공간 확보)
      item {
        Spacer(modifier = Modifier.height(80.dp))
      }
    }
  }
}

@Composable
private fun PaymentWorkerCard(
  worker: PaymentWorker
) {
  val numberFormat = NumberFormat.getNumberInstance(Locale.KOREA)
  
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
      verticalAlignment = Alignment.CenterVertically
    ) {
      // 아바타
      Box(
        modifier = Modifier
          .size(48.dp)
          .clip(CircleShape)
          .background(appColorScheme.primary.copy(alpha = 0.1f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          Icons.Default.Person,
          contentDescription = null,
          modifier = Modifier.size(28.dp),
          tint = appColorScheme.primary
        )
      }
      
      Spacer(modifier = Modifier.width(12.dp))
      
      // 근로자 정보
      Column(
        modifier = Modifier.weight(1f)
      ) {
        Text(
          text = worker.name,
          style = AppTypography.titleMedium,
          fontWeight = FontWeight.Bold
        )
        Text(
          text = "만 ${worker.age}세 • ${worker.gender}",
          style = AppTypography.bodySmall,
          color = Color.Gray
        )
      }
      
      // 지급 금액
      Text(
        text = if (worker.paymentAmount > 0) {
          "${numberFormat.format(worker.paymentAmount)}원"
        } else {
          "-원"
        },
        style = AppTypography.bodyMedium,
        fontWeight = FontWeight.Bold,
        color = if (worker.paymentAmount > 0) Color.Black else Color.Gray
      )
    }
  }
}

@Preview
@Composable
fun PaymentSummaryScreenPreview() {
  Jikgong1111Theme {
    PaymentSummaryScreen(
      navController = rememberNavController(),
      workDayId = "1"
    )
  }
}