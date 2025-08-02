package com.billcorea.jikgong.presentation.company.main.projectlist.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.appColorScheme

@Composable
fun ProjectLoadingState(
  modifier: Modifier = Modifier,
  message: String = "프로젝트를 불러오는 중..."
) {
  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      CircularProgressIndicator(
        modifier = Modifier.size(48.dp),
        color = appColorScheme.primary,
        strokeWidth = 4.dp
      )

      Text(
        text = message,
        style = AppTypography.bodyLarge.copy(fontWeight = FontWeight.Medium),
        color = appColorScheme.onSurfaceVariant
      )
    }
  }
}