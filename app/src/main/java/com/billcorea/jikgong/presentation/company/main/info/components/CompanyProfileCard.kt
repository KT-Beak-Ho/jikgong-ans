package com.billcorea.jikgong.presentation.company.main.info.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.ui.theme.*

@Composable
fun ServiceInfoCard(
  modifier: Modifier = Modifier
) {
  val services = listOf(
    "AI 기반 인력 매칭",
    "실시간 프로젝트 관리",
    "디지털 근로계약",
    "안전 교육 플랫폼"
  )

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
        text = "제공 서비스",
        style = AppTypography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = AppColors.textPrimary
      )

      Spacer(modifier = Modifier.height(16.dp))

      services.forEach { service ->
        ServiceItem(service)
      }
    }
  }
}

@Composable
private fun ServiceItem(service: String) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 8.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Box(
      modifier = Modifier
        .size(8.dp)
        .clip(CircleShape)
        .background(AppColors.primary)
    )
    Text(
      text = service,
      style = AppTypography.bodyMedium,
      color = AppColors.textPrimary,
      modifier = Modifier.padding(start = 12.dp)
    )
  }
}
