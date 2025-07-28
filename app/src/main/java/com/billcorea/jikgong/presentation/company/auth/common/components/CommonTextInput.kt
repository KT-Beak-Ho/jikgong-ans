package com.billcorea.jikgong.presentation.company.auth.common.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme

@Composable
fun CommonTextInput(
  value: String,
  onChange: (String) -> Unit,
  modifier: Modifier = Modifier,
  labelMainText: String? = null,
  placeholder: String = "",
  validationError: String? = null,
  isRequired: Boolean = false,
  enabled: Boolean = true,
  readOnly: Boolean = false,
  singleLine: Boolean = true,
  maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
  keyboardType: KeyboardType = KeyboardType.Text,
  visualTransformation: VisualTransformation = VisualTransformation.None,
  trailingIcon: @Composable (() -> Unit)? = null,
  leadingIcon: @Composable (() -> Unit)? = null,
  supportingText: @Composable (() -> Unit)? = null
) {
  Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(4.dp)
  ) {
    // 라벨 (있는 경우)
    if (labelMainText != null) {
      Row {
        Text(
          text = labelMainText,
          style = AppTypography.bodyMedium.copy(
            fontWeight = FontWeight.Medium
          ),
          color = appColorScheme.onSurface
        )
        if (isRequired) {
          Text(
            text = " *",
            style = AppTypography.bodyMedium.copy(
              fontWeight = FontWeight.Bold
            ),
            color = Color(0xFFEF5350)
          )
        }
      }
      Spacer(modifier = Modifier.height(4.dp))
    }

    // 텍스트 필드
    OutlinedTextField(
      value = value,
      onValueChange = onChange,
      modifier = Modifier.fillMaxWidth(),
      placeholder = if (placeholder.isNotEmpty()) {
        { Text(placeholder) }
      } else null,
      isError = validationError != null,
      enabled = enabled,
      readOnly = readOnly,
      singleLine = singleLine,
      maxLines = maxLines,
      keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
      visualTransformation = visualTransformation,
      trailingIcon = trailingIcon,
      leadingIcon = leadingIcon,
      supportingText = supportingText ?: if (validationError != null) {
        {
          Text(
            text = validationError,
            style = AppTypography.bodySmall,
            color = MaterialTheme.colorScheme.error
          )
        }
      } else null,
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = appColorScheme.primary,
        unfocusedBorderColor = appColorScheme.outline,
        errorBorderColor = Color(0xFFEF5350),
        focusedLabelColor = appColorScheme.primary,
        unfocusedLabelColor = appColorScheme.onSurfaceVariant,
        errorLabelColor = Color(0xFFEF5350)
      )
    )
  }
}

@Preview(showBackground = true)
@Composable
fun CommonTextInputPreview() {
  Jikgong1111Theme {
    Column(
      modifier = Modifier.padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // 기본 입력 필드
      CommonTextInput(
        value = "",
        onChange = {},
        labelMainText = "프로젝트 제목",
        placeholder = "제목을 입력하세요",
        isRequired = true
      )

      // 값이 있는 입력 필드
      CommonTextInput(
        value = "강남구 아파트 신축 공사",
        onChange = {},
        labelMainText = "프로젝트 제목",
        placeholder = "제목을 입력하세요"
      )

      // 오류가 있는 입력 필드
      CommonTextInput(
        value = "짧음",
        onChange = {},
        labelMainText = "프로젝트 제목",
        placeholder = "제목을 입력하세요",
        validationError = "제목은 5자 이상 입력해주세요",
        isRequired = true
      )

      // 여러 줄 입력 필드
      CommonTextInput(
        value = "여러 줄로\n입력할 수 있는\n텍스트 필드입니다.",
        onChange = {},
        labelMainText = "상세 설명",
        placeholder = "상세 내용을 입력하세요",
        singleLine = false,
        maxLines = 3
      )
    }
  }
}