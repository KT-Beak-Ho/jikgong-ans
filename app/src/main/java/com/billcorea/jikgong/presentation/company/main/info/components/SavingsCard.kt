package com.billcorea.jikgong.presentation.company.main.info.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.billcorea.jikgong.network.models.CompanyData

@Composable
fun SavingsCard(
  companyData: CompanyData,
  formatCurrency: (Long) -> String,
  modifier: Modifier = Modifier
) {
  // 애니메이션 효과
  val infiniteTransition = rememberInfiniteTransition()
  val shimmerTranslateAnim = infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 1000f,
    animationSpec = infiniteRepeatable(
      animation = tween(1500, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    )
  )

  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 20.dp)
      .shadow(
        elevation = 12.dp,
        shape = RoundedCornerShape(20.dp),
        spotColor = Color(0x337C3AED)
      )
      .animateContentSize(),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(containerColor = Color.Transparent)
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .background(
          Brush.linearGradient(
            colors = listOf(
              Color(0xFF7C3AED),
              Color(0xFF8B5CF6),
              Color(0xFF9333EA)
            )
          )
        )
        .padding(20.dp)
    ) {
      Column {
        Text(
          text = "💰 이번 달 절약한 중개수수료",
          fontSize = 13.sp,
          color = Color.White.copy(alpha = 0.95f),
          fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = formatCurrency(companyData.monthlySavings),
          fontSize = 32.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White,
          letterSpacing = (-0.5).sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
          horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          Text(
            text = "전월 대비 +${companyData.previousMonthGrowth}%",
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.9f)
          )
          Text(
            text = "•",
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.6f)
          )
          Text(
            text = "목표 달성률 ${companyData.targetAchievementRate}%",
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.9f)
          )
        }
      }
    }
  }
}