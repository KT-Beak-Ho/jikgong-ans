package com.billcorea.jikgong.presentation.company.auth.common.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.ui.theme.appColorScheme

@Composable
fun CommonTextInput(
  modifier: Modifier = Modifier,
  value: String = "",
  onChange: (String) -> Unit,
  labelMainText: String = "",
  labelAppendText: String = "",
  labelAppendTextColor: Color = appColorScheme.primary,
  placeholder: String = "",
  maxLines: Int = 1,
  minLines: Int = 1,
) {
  // maxLines가 minLines보다 작은 경우 자동으로 minLines의 3배로 설정
  val adjustedMaxLines = if (maxLines < minLines) {
    minLines * 3
  } else {
    maxLines
  }
  Column(modifier = modifier) {
    //  라벨
    LabelText(
      mainText = labelMainText,
      appendText = labelAppendText,
      appendTextColor = labelAppendTextColor
    )
    Spacer(modifier = Modifier.padding(5.dp))
    //  내용 출력
    OutlinedTextField(
      value = value,
      onValueChange = onChange,
      placeholder = { Text(placeholder) },
      keyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Next
      ),
      maxLines = adjustedMaxLines,
      minLines = minLines,
      modifier = Modifier.fillMaxWidth()
    )
  }
}
