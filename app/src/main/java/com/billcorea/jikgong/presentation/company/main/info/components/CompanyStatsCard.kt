package com.billcorea.jikgong.presentation.company.main.info.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.billcorea.jikgong.presentation.company.main.info.viewmodel.CompanyInfoUiState
import com.billcorea.jikgong.ui.theme.*

@Composable
fun CompanyStatsCard(
  stats: CompanyInfoUiState,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp)
    ) {
      Text(
        text = "사업 현황",
        style = AppTypography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = AppColors.textPrimary
      )

      Spacer(modifier = Modifier.height(16.dp))

      // 프로젝트 통계
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
      ) {
        StatItem(
          title = "전체 프로젝트",
          value = stats.totalProjects.toString(),
          color = AppColors.primary
        )
        StatItem(
          title = "진행중",
          value = stats.activeProjects.toString(),
          color = Color(0xFF4CAF50)
        )
        StatItem(
          title = "완료",
          value = stats.completedProjects.toString(),
          color = Color(0xFF757575)
        )
      }

      HorizontalDivider(
        modifier = Modifier.padding(vertical = 16.dp),
        color = AppColors.divider
      )

      // 추가 통계
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
      ) {
        StatItem(
          title = "등록 인력",
          value = "${stats.totalWorkers}명",
          color = Color(0xFFFF6B6B)
        )
        StatItem(
          title = "평균 평점",
          value = "${stats.rating}점",
          color = Color(0xFFFFA726)
        )
        StatItem(
          title = "월 매출",
          value = stats.monthlyRevenue,
          color = Color(0xFF9C27B0)
        )
      }
    }
  }
}

@Composable
private fun StatItem(
  title: String,
  value: String,
  color: Color
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      text = value,
      fontSize = 24.sp,
      fontWeight = FontWeight.Bold,
      color = color
    )
    Text(
      text = title,
      style = AppTypography.labelSmall,
      color = AppColors.textSecondary,
      modifier = Modifier.padding(top = 4.dp)
    )
  }
}