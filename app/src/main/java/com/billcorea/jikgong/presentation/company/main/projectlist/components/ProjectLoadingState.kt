package com.billcorea.jikgong.presentation.company.main.projectlist.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.ui.theme.appColorScheme

@Composable
fun ProjectListLoadingState(
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(16.dp)
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
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = appColorScheme.surface
    )
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      // 헤더 스켈레톤
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Box(
          modifier = Modifier
            .width(80.dp)
            .height(24.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(appColorScheme.outline.copy(alpha = 0.3f))
        )
        Box(
          modifier = Modifier
            .size(24.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(appColorScheme.outline.copy(alpha = 0.3f))
        )
      }

      // 제목 스켈레톤
      Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Box(
          modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(20.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(appColorScheme.outline.copy(alpha = 0.3f))
        )
        Box(
          modifier = Modifier
            .fillMaxWidth(0.6f)
            .height(16.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(appColorScheme.outline.copy(alpha = 0.2f))
        )
      }

      // 정보 스켈레톤
      Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        repeat(2) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Box(
              modifier = Modifier
                .size(16.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(appColorScheme.outline.copy(alpha = 0.3f))
            )
            Box(
              modifier = Modifier
                .width(120.dp)
                .height(14.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(appColorScheme.outline.copy(alpha = 0.2f))
            )
          }
        }
      }

      // 일당/모집 정보 스켈레톤
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Column(
          verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Box(
            modifier = Modifier
              .width(40.dp)
              .height(12.dp)
              .clip(RoundedCornerShape(4.dp))
              .background(appColorScheme.outline.copy(alpha = 0.2f))
          )
          Box(
            modifier = Modifier
              .width(80.dp)
              .height(18.dp)
              .clip(RoundedCornerShape(4.dp))
              .background(appColorScheme.outline.copy(alpha = 0.3f))
          )
        }
        Column(
          horizontalAlignment = Alignment.End,
          verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Box(
            modifier = Modifier
              .width(50.dp)
              .height(12.dp)
              .clip(RoundedCornerShape(4.dp))
              .background(appColorScheme.outline.copy(alpha = 0.2f))
          )
          Box(
            modifier = Modifier
              .width(60.dp)
              .height(16.dp)
              .clip(RoundedCornerShape(4.dp))
              .background(appColorScheme.outline.copy(alpha = 0.3f))
          )
        }
      }

      // 프로그래스 바 스켈레톤
      Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Box(
            modifier = Modifier
              .width(40.dp)
              .height(12.dp)
              .clip(RoundedCornerShape(4.dp))
              .background(appColorScheme.outline.copy(alpha = 0.2f))
          )
          Box(
            modifier = Modifier
              .width(30.dp)
              .height(12.dp)
              .clip(RoundedCornerShape(4.dp))
              .background(appColorScheme.outline.copy(alpha = 0.2f))
          )
        }
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(RoundedCornerShape(3.dp))
            .background(appColorScheme.outline.copy(alpha = 0.3f))
        )
      }

      // 버튼 스켈레톤
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Box(
          modifier = Modifier
            .weight(1f)
            .height(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(appColorScheme.outline.copy(alpha = 0.3f))
        )
        Box(
          modifier = Modifier
            .width(80.dp)
            .height(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(appColorScheme.outline.copy(alpha = 0.2f))
        )
      }
    }
  }
}
