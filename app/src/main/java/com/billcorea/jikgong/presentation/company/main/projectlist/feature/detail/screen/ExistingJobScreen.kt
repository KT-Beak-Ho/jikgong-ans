package com.billcorea.jikgong.presentation.company.main.projectlist.feature.detail.screen

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
import java.text.NumberFormat
import java.util.*

// 기존 일자리 데이터
data class ExistingJob(
  val id: String,
  val title: String,
  val workPeriod: String,
  val dailyWage: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExistingJobScreen(
  navController: NavController,
  modifier: Modifier = Modifier
) {
  // 샘플 기존 일자리 데이터 (빈 리스트로 시작해서 없는 경우 테스트 가능)
  val existingJobs = remember {
    listOf(
      ExistingJob("1", "아파트 신축공사 철근 작업자 모집", "2025-08-01 ~ 2025-08-31", 200000),
      ExistingJob("2", "사무실 인테리어 목공 인력 모집", "2025-08-05 ~ 2025-08-20", 180000),
      ExistingJob("3", "상가건물 전기공 모집", "2025-08-10 ~ 2025-08-25", 220000)
    )
  }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    topBar = {
      BackNavigationTopBar(
        title = "기존공고 이용",
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
      
      if (existingJobs.isEmpty()) {
        // 등록된 일자리가 없는 경우
        Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "등록된 일자리가 없습니다",
            style = AppTypography.bodyLarge,
            color = Color.Gray
          )
        }
      } else {
        // 등록된 일자리가 있는 경우
        LazyColumn(
          modifier = Modifier.fillMaxSize(),
          contentPadding = PaddingValues(16.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          item {
            // 총 건수 표시
            Row(
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "총 ",
                style = AppTypography.bodyMedium,
                color = Color.Gray
              )
              Text(
                text = "${existingJobs.size}건",
                style = AppTypography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = appColorScheme.primary
              )
            }
          }
          
          item {
            // 안내 카드 (회색 배경)
            Card(
              modifier = Modifier.fillMaxWidth(),
              shape = RoundedCornerShape(12.dp),
              colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
              elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(16.dp)
              ) {
                Text(
                  text = "기존에 등록하신 일자리에서 원하는 부분을 수정해 공고를 다시 등록할 수 있어요",
                  style = AppTypography.bodyMedium,
                  color = Color(0xFF666666),
                  lineHeight = AppTypography.bodyMedium.lineHeight
                )
              }
            }
          }
          
          items(existingJobs) { job ->
            ExistingJobCard(job = job)
          }
        }
      }
    }
  }
}

@Composable
private fun ExistingJobCard(
  job: ExistingJob
) {
  val numberFormat = NumberFormat.getNumberInstance(Locale.KOREA)
  
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
      // 근무 기간
      Text(
        text = job.workPeriod,
        style = AppTypography.bodySmall,
        color = Color.Gray
      )
      
      Spacer(modifier = Modifier.height(4.dp))
      
      // 작업 이름
      Text(
        text = job.title,
        style = AppTypography.bodyMedium,
        fontWeight = FontWeight.Medium,
        color = Color.Black
      )
      
      Spacer(modifier = Modifier.height(8.dp))
      
      // 일급 카드 (하늘색 배경, 푸른색 글자)
      Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)) // 하늘색 배경
      ) {
        Box(
          modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
          Text(
            text = "일급 ${numberFormat.format(job.dailyWage)}원",
            style = AppTypography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1976D2) // 푸른색 글자
          )
        }
      }
    }
  }
}

@Preview
@Composable
fun ExistingJobScreenPreview() {
  Jikgong1111Theme {
    ExistingJobScreen(
      navController = rememberNavController()
    )
  }
}