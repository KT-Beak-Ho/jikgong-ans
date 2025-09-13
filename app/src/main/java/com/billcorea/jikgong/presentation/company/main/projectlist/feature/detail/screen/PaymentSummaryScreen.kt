package com.billcorea.jikgong.presentation.company.main.projectlist.feature.detail.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.company.main.common.BackNavigationTopBar
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.billcorea.jikgong.api.models.sampleDataFactory.CompanyMockDataFactory
import com.billcorea.jikgong.presentation.company.main.projectlist.data.PaymentWorker
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentSummaryScreen(
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
    
    println("=== PaymentSummaryScreen Debug ===")
    println("selectedDate parameter: $selectedDate")
    println("effectiveDate: $parsedDate")
    println("=================================")
    
    parsedDate
  } catch (e: Exception) {
    println("Error parsing selectedDate: $selectedDate, using default date")
    LocalDate.parse("2025-08-01")
  }
  
  // 확정 근로자를 지급 데이터로 변환 (완료된 작업이므로 모든 근로자가 정상 출근)
  val workers = remember(workDayId, confirmedWorkers) {
    val jobRoles = CompanyMockDataFactory.getJobRoles()
    val workDescriptions = CompanyMockDataFactory.getWorkDescriptions()
    
    confirmedWorkers.mapIndexed { index, worker ->
      // 완료된 프로젝트이므로 모든 근로자가 정상 출근하고 임금을 받음
      PaymentWorker(
        id = worker.id,
        name = worker.name,
        age = worker.age,
        gender = worker.gender,
        paymentAmount = 200000, // 모든 근로자가 일당 20만원 받음
        attendanceStatus = "정상출근", // 모든 근로자가 정상출근
        jobRole = jobRoles[index % jobRoles.size],
        workDescription = workDescriptions[index % workDescriptions.size],
        rating = 3.5f + (index % 15) * 0.1f // 모든 근로자가 3.5~5.0 평점 받음
      )
    }
  }
  
  // 완료된 작업이므로 모든 근로자가 정상 출근 (조퇴/결근자 없음)
  val normalAttendanceWorkers = workers // 모든 근로자가 정상 출근
  
  // 총 지급액 계산
  val totalPayment = workers.sumOf { it.paymentAmount }
  
  // 숫자 포맷터
  val numberFormat = NumberFormat.getNumberInstance(Locale.KOREA)

  Scaffold(
    modifier = modifier.fillMaxSize(),
    topBar = {
      BackNavigationTopBar(
        title = "지급내역서 (${effectiveDate.format(DateTimeFormatter.ofPattern("MM/dd"))})",
        onBackClick = { navController.popBackStack() }
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
          HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
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
      
      // 정상출근 섹션 (완료된 작업이므로 모든 근로자가 정상 출근)
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
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = Color(0xFF4CAF50)
            ) {
              Text(
                text = "작업완료",
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                style = AppTypography.bodySmall,
                color = Color.White,
                fontWeight = FontWeight.Medium
              )
            }
          }
        }
        
        items(normalAttendanceWorkers) { worker ->
          PaymentWorkerCard(worker = worker)
        }
      } else {
        // 빈 상태 표시
        item {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 48.dp),
            contentAlignment = Alignment.Center
          ) {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(
                text = "해당 날짜에 확정된 근로자가 없습니다",
                style = AppTypography.bodyLarge,
                color = Color.Gray
              )
            }
          }
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
  var expanded by remember { mutableStateOf(false) }
  var currentRating by remember { mutableFloatStateOf(worker.rating) }
  
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier.fillMaxWidth()
    ) {
      // 기본 정보 행
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { expanded = !expanded }
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
        
        // 근로자 기본 정보
        Column(
          modifier = Modifier.weight(1f)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = worker.name,
              style = AppTypography.titleMedium,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
              shape = RoundedCornerShape(4.dp),
              color = appColorScheme.primary.copy(alpha = 0.1f)
            ) {
              Text(
                text = worker.jobRole,
                style = AppTypography.bodySmall,
                color = appColorScheme.primary,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }
          
          Text(
            text = "만 ${worker.age}세 • ${worker.gender}",
            style = AppTypography.bodySmall,
            color = Color.Gray
          )
          
          // 평점 표시 (정상출근자만)
          if (worker.paymentAmount > 0 && worker.rating > 0) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(top = 4.dp)
            ) {
              repeat(5) { index ->
                Icon(
                  if (index < currentRating.toInt()) Icons.Filled.Star else Icons.Outlined.Star,
                  contentDescription = null,
                  modifier = Modifier.size(16.dp),
                  tint = Color(0xFFFFC107)
                )
              }
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = String.format("%.1f", currentRating),
                style = AppTypography.bodySmall,
                color = Color.Gray
              )
            }
          }
        }
        
        // 지급 금액
        Column(
          horizontalAlignment = Alignment.End
        ) {
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
          
          // 확장 아이콘
          Icon(
            if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
            contentDescription = if (expanded) "접기" else "펼치기",
            modifier = Modifier.size(20.dp),
            tint = Color.Gray
          )
        }
      }
      
      // 확장된 상세 정보
      if (expanded) {
        Column(
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 0.dp)
        ) {
          HorizontalDivider(color = Color.LightGray)
          
          Spacer(modifier = Modifier.height(12.dp))
          
          // 업무 내용
          Text(
            text = "수행 업무",
            style = AppTypography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = Color.Black
          )
          
          Text(
            text = worker.workDescription,
            style = AppTypography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp)
          )
          
          // 평점 부여 (정상출근자만)
          if (worker.paymentAmount > 0) {
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
              text = "평점 부여",
              style = AppTypography.bodyMedium,
              fontWeight = FontWeight.Medium,
              color = Color.Black
            )
            
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(top = 8.dp)
            ) {
              Text(
                text = "작업 품질:",
                style = AppTypography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.width(60.dp)
              )
              
              LazyRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                items(5) { index ->
                  val starIndex = index + 1
                  Icon(
                    if (starIndex <= currentRating.toInt()) Icons.Filled.Star else Icons.Outlined.Star,
                    contentDescription = "${starIndex}점",
                    modifier = Modifier
                      .size(32.dp)
                      .clickable { currentRating = starIndex.toFloat() },
                    tint = if (starIndex <= currentRating.toInt()) Color(0xFFFFC107) else Color.LightGray
                  )
                }
              }
              
              Spacer(modifier = Modifier.width(8.dp))
              
              Text(
                text = String.format("%.1f점", currentRating),
                style = AppTypography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = appColorScheme.primary
              )
            }
          }
          
          Spacer(modifier = Modifier.height(16.dp))
        }
      }
    }
  }
}

@Preview
@Composable
fun PaymentSummaryScreenPreview() {
  Jikgong1111Theme {
    PaymentSummaryScreen(
      navController = rememberNavController(),
      workDayId = "1",
      selectedDate = "2025-08-01"
    )
  }
}