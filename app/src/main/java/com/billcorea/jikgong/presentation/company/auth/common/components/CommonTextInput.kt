package com.billcorea.jikgong.presentation.company.auth.common.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
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
  isPassword: Boolean = false,  // 비밀번호 모드 추가
  keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
  keyboardActions: KeyboardActions = KeyboardActions.Default,
  leadingIcon: @Composable (() -> Unit)? = null,
  trailingIcon: @Composable (() -> Unit)? = null
) {
  // 비밀번호 표시/숨김 상태
  var passwordVisible by remember { mutableStateOf(false) }
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
      visualTransformation = if (isPassword && !passwordVisible) {
        PasswordVisualTransformation()
      } else {
        VisualTransformation.None
      },
      keyboardOptions = if (isPassword) {
        KeyboardOptions(keyboardType = KeyboardType.Password)
      } else {
        keyboardOptions
      },
      keyboardActions = keyboardActions,
      leadingIcon = leadingIcon,
      trailingIcon = if (isPassword) {
        {
          IconButton(onClick = { passwordVisible = !passwordVisible }) {
            Icon(
              imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
              contentDescription = if (passwordVisible) "비밀번호 숨기기" else "비밀번호 보기"
            )
          }
        }
      } else {
        trailingIcon
      },
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