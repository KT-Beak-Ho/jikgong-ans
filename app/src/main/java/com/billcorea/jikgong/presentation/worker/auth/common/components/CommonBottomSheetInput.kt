package com.billcorea.jikgong.presentation.worker.auth.common.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalFocusManager

@Composable
fun CommonBottomSheetInput(
  value: String,
  placeholderText: String,
  errorKey: String,
  validationErrors: Map<String, String?>,
  iconPainter: Painter,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val focusManager = LocalFocusManager.current

  Box(
    modifier = modifier
      .fillMaxWidth()
      .clickable {
        onClick()
        focusManager.clearFocus()
      }
  ) {
    OutlinedTextField(
      value = value,
      onValueChange = {}, // 읽기 전용
      placeholder = { Text(placeholderText) },
      readOnly = true,
      enabled = false,
      modifier = Modifier.fillMaxWidth(),
      isError = validationErrors[errorKey] != null,
      supportingText = validationErrors[errorKey]?.let {
        { Text(text = it) }
      },
      trailingIcon = {
        Icon(
          painter = iconPainter,
          contentDescription = "arrow down"
        )
      },
      colors = OutlinedTextFieldDefaults.colors(
        disabledTextColor = MaterialTheme.colorScheme.onSurface,
        disabledBorderColor = MaterialTheme.colorScheme.outline,
        disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
      )
    )
  }
}
