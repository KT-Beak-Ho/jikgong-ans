package com.billcorea.jikgong.presentation.company.auth.common.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.ui.theme.AppTypography

@Composable
fun CommonButton(
  text: String,
  onClick: () -> Unit,
  enabled: Boolean = true,
  isLoading: Boolean = false,
  modifier: Modifier = Modifier
) {
  Button(
    onClick = onClick,
    enabled = enabled && !isLoading,
    modifier = modifier.fillMaxWidth()
  ) {
    if (isLoading) {
      CircularProgressIndicator(
        modifier = Modifier.size(20.dp),
        color = Color.White
      )
    } else {
      Text(
        text = text,
        style = AppTypography.labelMedium
      )
    }
  }
}