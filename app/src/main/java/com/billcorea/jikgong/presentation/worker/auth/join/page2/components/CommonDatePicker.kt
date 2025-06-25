package com.billcorea.jikgong.presentation.worker.auth.join.page2.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.R
import com.billcorea.jikgong.presentation.company.auth.common.components.LabelText
import com.billcorea.jikgong.ui.theme.appColorScheme

@Composable
fun CommonDatePicker(
  modifier: Modifier = Modifier,
  value: String = "",
  onClick: () -> Unit,
  labelMainText: String = "",
  labelAppendText: String = "",
  labelAppendTextColor: Color = appColorScheme.primary,
  placeholder: String = "",
  validationError: String? = null,
  enabled: Boolean = true,
  trailingIcon: @Composable (() -> Unit)? = {
    Icon(
      painter = painterResource(R.drawable.ic_keyboard_arrow_down_24dp),
      contentDescription = "Arrow Down",
      tint = if (enabled) appColorScheme.onSurface else appColorScheme.onSurface.copy(alpha = 0.38f)
    )
  }
) {
  Column(modifier = modifier) {
    // 라벨
    LabelText(
      mainText = labelMainText,
      appendText = labelAppendText,
      appendTextColor = labelAppendTextColor
    )

    Spacer(modifier = Modifier.padding(5.dp))

    // 선택 가능한 필드
    OutlinedTextField(
      value = value,
      onValueChange = { }, // 읽기 전용이므로 빈 함수
      placeholder = { Text(placeholder) },
      readOnly = true,
      enabled = enabled,
      modifier = Modifier
        .fillMaxWidth()
        .clickable(enabled = enabled) { onClick() },

      isError = validationError != null,
      supportingText = validationError?.let {
        { Text(text = it) }
      },
      trailingIcon = trailingIcon,
      colors = OutlinedTextFieldDefaults.colors(
        disabledTextColor = appColorScheme.onSurface,
        disabledBorderColor = appColorScheme.outline,
        disabledPlaceholderColor = appColorScheme.onSurfaceVariant,
        disabledTrailingIconColor = appColorScheme.onSurfaceVariant
      )
    )
  }
}