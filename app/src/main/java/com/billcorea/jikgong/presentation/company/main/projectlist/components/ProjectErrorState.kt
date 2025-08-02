package com.billcorea.jikgong.presentation.company.main.projectlist.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.appColorScheme

@Composable
fun ProjectErrorState(
  errorMessage: String,
  onRetryClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
      modifier = Modifier.padding(horizontal = 24.dp)
    ) {
      // 에러 아이콘
      Surface(
        modifier = Modifier.size(120.dp),
        shape = RoundedCornerShape(60.dp),
        color = appColorScheme.errorContainer.copy(alpha = 0.3f)
      ) {
        Icon(
          imageVector = Icons.Default.ErrorOutline,
          contentDescription = "에러",
          modifier = Modifier
            .size(60.dp)
            .padding(30.dp),
          tint = appColorScheme.error
        )
      }

      Spacer(modifier = Modifier.height(32.dp))

      // 에러 제목
      Text(
        text = "문제가 발생했습니다",
        style = AppTypography.headlineSmall.copy(fontWeight = FontWeight.Bold),
        color = appColorScheme.onSurface,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(12.dp))

      // 에러 메시지
      Text(
        text = errorMessage,
        style = AppTypography.bodyLarge,
        color = appColorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        lineHeight = AppTypography.bodyLarge.lineHeight
      )

      Spacer(modifier = Modifier.height(32.dp))

      // 재시도 버튼
      Button(
        onClick = onRetryClick,
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = appColorScheme.primary
        )
      ) {
        Icon(
          imageVector = Icons.Default.Refresh,
          contentDescription = "재시도",
          modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "다시 시도",
          style = AppTypography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
        )
      }
    }
  }
}
