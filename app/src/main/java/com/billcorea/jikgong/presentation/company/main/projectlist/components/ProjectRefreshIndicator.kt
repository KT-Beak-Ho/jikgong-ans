package com.billcorea.jikgong.presentation.company.main.projectlist.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.appColorScheme
import kotlinx.coroutines.delay

@Composable
fun ProjectRefreshIndicator(
  isRefreshing: Boolean,
  onRefresh: () -> Unit,
  modifier: Modifier = Modifier
) {
  var rotation by remember { mutableStateOf(0f) }

  LaunchedEffect(isRefreshing) {
    if (isRefreshing) {
      while (isRefreshing) {
        rotation += 360f
        delay(1000)
      }
    }
  }

  Card(
    modifier = modifier,
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(
      containerColor = appColorScheme.surfaceVariant.copy(alpha = 0.5f)
    )
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = if (isRefreshing) "새로고침 중..." else "최신 프로젝트 확인",
          style = AppTypography.titleSmall.copy(fontWeight = FontWeight.Bold),
          color = appColorScheme.onSurface
        )
        Text(
          text = if (isRefreshing) "프로젝트 목록을 업데이트하고 있습니다" else "새로운 프로젝트가 있는지 확인해보세요",
          style = AppTypography.bodyMedium,
          color = appColorScheme.onSurfaceVariant
        )
      }

      if (isRefreshing) {
        CircularProgressIndicator(
          modifier = Modifier.size(24.dp),
          color = appColorScheme.primary,
          strokeWidth = 2.dp
        )
      } else {
        FilledTonalIconButton(
          onClick = onRefresh,
          colors = IconButtonDefaults.filledTonalIconButtonColors(
            containerColor = appColorScheme.primaryContainer.copy(alpha = 0.5f)
          )
        ) {
          Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "새로고침",
            modifier = Modifier
              .size(20.dp)
              .rotate(rotation),
            tint = appColorScheme.primary
          )
        }
      }
    }
  }
}