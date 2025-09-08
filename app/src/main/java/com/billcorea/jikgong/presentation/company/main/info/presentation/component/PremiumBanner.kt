package com.billcorea.jikgong.presentation.company.main.info.presentation.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PremiumBanner(
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  // 펄스 애니메이션
  val infiniteTransition = rememberInfiniteTransition()
  val scale = infiniteTransition.animateFloat(
    initialValue = 1f,
    targetValue = 1.02f,
    animationSpec = infiniteRepeatable(
      animation = tween(1000),
      repeatMode = RepeatMode.Reverse
    )
  )

  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 20.dp)
      .scale(scale.value)
      .clickable { onClick() },
    shape = RoundedCornerShape(20.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
  ) {
    Box(
      contentAlignment = Alignment.Center,
      modifier = Modifier
        .fillMaxWidth()
        .background(
          Brush.horizontalGradient(
            colors = listOf(
              Color(0xFF10B981),
              Color(0xFF14B8A6)
            )
          )
        )
        .padding(vertical = 24.dp, horizontal = 20.dp)
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
      ) {
        Text(
          text = "🚀",
          fontSize = 24.sp,
          modifier = Modifier.padding(end = 8.dp)
        )
        Text(
          text = "프리미엄 업그레이드로 더 많은 혜택을!",
          fontSize = 15.sp,
          fontWeight = FontWeight.SemiBold,
          color = Color.White,
          textAlign = TextAlign.Center
        )
      }
    }
  }
}