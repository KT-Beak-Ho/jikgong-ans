package com.billcorea.jikgong.presentation.company.auth.common.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.appColorScheme

@Composable
fun CommonButton(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  isLoading: Boolean = false
) {
  Button(
    onClick = onClick,
    enabled = enabled && !isLoading,
    modifier = modifier.height(56.dp),
    colors = ButtonDefaults.buttonColors(
      containerColor = appColorScheme.primary,
      contentColor = appColorScheme.onPrimary,
      disabledContainerColor = appColorScheme.inversePrimary,
      disabledContentColor = appColorScheme.surfaceDim
    )
  ) {
    if (isLoading) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
      ) {
        CircularProgressIndicator(
          modifier = Modifier.size(20.dp),
          strokeWidth = 2.dp,
          color = appColorScheme.onPrimary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "처리 중...",
          style = AppTypography.labelLarge.copy(
            fontWeight = FontWeight.Medium
          )
        )
      }
    } else {
      Text(
        text = text,
        style = AppTypography.labelLarge.copy(
          fontWeight = FontWeight.Medium
        )
      )
    }
  }
}