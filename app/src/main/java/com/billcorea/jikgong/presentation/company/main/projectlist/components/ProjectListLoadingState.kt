// ========================================
// 📄 components/ProjectListLoadingState.kt
// ========================================
package com.billcorea.jikgong.presentation.company.main.projectlist.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.ui.theme.appColorScheme

/**
 * 프로젝트 목록 로딩 상태
 */
@Composable
fun ProjectListLoadingState(
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    repeat(3) {
      ProjectCardSkeleton()
    }
  }
}

@Composable
private fun ProjectCardSkeleton() {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(
      containerColor = appColorScheme.surface
    )
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      // 제목 스켈레톤
      Box(
        modifier = Modifier
          .fillMaxWidth(0.8f)
          .height(20.dp)
          .clip(RoundedCornerShape(4.dp))
          .shimmerEffect()
      )

      // 위치 스켈레톤
      Box(
        modifier = Modifier
          .fillMaxWidth(0.4f)
          .height(16.dp)
          .clip(RoundedCornerShape(4.dp))
          .shimmerEffect()
      )

      Spacer(modifier = Modifier.height(8.dp))

      // 정보 스켈레톤
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Box(
          modifier = Modifier
            .width(80.dp)
            .height(16.dp)
            .clip(RoundedCornerShape(4.dp))
            .shimmerEffect()
        )
        Box(
          modifier = Modifier
            .width(60.dp)
            .height(16.dp)
            .clip(RoundedCornerShape(4.dp))
            .shimmerEffect()
        )
      }

      // 임금 스켈레톤
      Box(
        modifier = Modifier
          .fillMaxWidth(0.6f)
          .height(24.dp)
          .clip(RoundedCornerShape(4.dp))
          .shimmerEffect()
      )
    }
  }
}

@Composable
private fun Modifier.shimmerEffect(): Modifier {
  val shimmerColors = listOf(
    appColorScheme.surfaceVariant.copy(alpha = 0.3f),
    appColorScheme.surface.copy(alpha = 0.5f),
    appColorScheme.surfaceVariant.copy(alpha = 0.3f),
  )

  val transition = rememberInfiniteTransition(label = "shimmer")
  val translateAnim = transition.animateFloat(
    initialValue = 0f,
    targetValue = 1000f,
    animationSpec = infiniteRepeatable(
      animation = tween(1200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "shimmer"
  )

  val brush = Brush.linearGradient(
    colors = shimmerColors,
    start = Offset.Zero,
    end = Offset(x = translateAnim.value, y = translateAnim.value)
  )

  return background(brush)
}