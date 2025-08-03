package com.billcorea.jikgong.presentation.company.main.projectlist.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme

@Composable
fun ProjectListLoadingState(
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 요약 카드 스켈레톤
    ShimmerCard(
      modifier = Modifier
        .fillMaxWidth()
        .height(120.dp)
    )

    // 프로젝트 카드 스켈레톤들
    repeat(5) {
      ProjectCardSkeleton()
    }
  }
}

@Composable
private fun ProjectCardSkeleton(
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = appColorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(
      modifier = Modifier.padding(20.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        ShimmerBox(modifier = Modifier.width(80.dp).height(24.dp))
        ShimmerBox(modifier = Modifier.size(24.dp))
      }

      ShimmerBox(modifier = Modifier.fillMaxWidth(0.8f).height(20.dp))
      ShimmerBox(modifier = Modifier.fillMaxWidth(0.6f).height(16.dp))
      ShimmerBox(modifier = Modifier.fillMaxWidth(0.5f).height(16.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        ShimmerBox(modifier = Modifier.width(80.dp).height(20.dp))
        ShimmerBox(modifier = Modifier.width(60.dp).height(28.dp))
      }

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        ShimmerBox(modifier = Modifier.weight(1f).height(40.dp))
        ShimmerBox(modifier = Modifier.size(40.dp))
      }
    }
  }
}

@Composable
private fun ShimmerCard(
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier,
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = appColorScheme.surface
    )
  ) {
    ShimmerBox(modifier = Modifier.fillMaxSize())
  }
}

@Composable
private fun ShimmerBox(
  modifier: Modifier = Modifier
) {
  val infiniteTransition = rememberInfiniteTransition(label = "shimmer")
  val alpha by infiniteTransition.animateFloat(
    initialValue = 0.2f,
    targetValue = 0.8f,
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "alpha"
  )

  val shimmerColors = listOf(
    appColorScheme.onSurface.copy(alpha = 0.1f),
    appColorScheme.onSurface.copy(alpha = 0.3f),
    appColorScheme.onSurface.copy(alpha = 0.1f)
  )

  Box(
    modifier = modifier
      .background(
        brush = Brush.linearGradient(
          colors = shimmerColors,
          start = Offset.Zero,
          end = Offset.Infinite
        ),
        shape = RoundedCornerShape(8.dp)
      )
      .alpha(alpha)
  )
}

@Preview
@Composable
fun ProjectListLoadingStatePreview() {
  Jikgong1111Theme {
    ProjectListLoadingState(modifier = Modifier.padding(16.dp))
  }
}