package com.billcorea.jikgong.presentation.company.auth.common.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.ui.theme.appColorScheme

@Composable
fun CommonTextInput(
  value: String,
  onChange: (String) -> Unit,
  modifier: Modifier = Modifier,
  labelMainText: String = "",
  labelAppendText: String = "",
  labelAppendTextColor: Color = appColorScheme.primary,
  placeholder: String = "",
  validationError: String? = null,
  enabled: Boolean = true,
  readOnly: Boolean = false,
  maxLines: Int = 1,
  keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
  keyboardActions: KeyboardActions = KeyboardActions.Default,
  leadingIcon: @Composable (() -> Unit)? = null,
  trailingIcon: @Composable (() -> Unit)? = null
) {
  Column(modifier = modifier) {
    // 라벨
    if (labelMainText.isNotEmpty()) {
      LabelText(
        mainText = labelMainText,
        appendText = labelAppendText,
        appendTextColor = labelAppendTextColor
      )
      Spacer(modifier = Modifier.height(8.dp))
    }

    // 텍스트 필드
    OutlinedTextField(
      value = value,
      onValueChange = onChange,
      placeholder = if (placeholder.isNotEmpty()) {
        { Text(placeholder) }
      } else null,
      enabled = enabled,
      readOnly = readOnly,
      maxLines = maxLines,
      keyboardOptions = keyboardOptions,
      keyboardActions = keyboardActions,
      leadingIcon = leadingIcon,
      trailingIcon = trailingIcon,
      isError = validationError != null,
      supportingText = validationError?.let {
        { Text(text = it) }
      },
      modifier = Modifier.fillMaxWidth(),
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = appColorScheme.primary,
        unfocusedBorderColor = appColorScheme.outline,
        errorBorderColor = appColorScheme.error,
        focusedLabelColor = appColorScheme.primary,
        unfocusedLabelColor = appColorScheme.onSurfaceVariant
      )
    )
  }
}