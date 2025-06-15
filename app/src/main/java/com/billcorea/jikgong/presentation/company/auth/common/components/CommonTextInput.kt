package com.billcorea.jikgong.presentation.company.auth.common.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.presentation.common.KeyboardConstants
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
  validationError: String? = null,
  keyboardOptions: KeyboardOptions = KeyboardConstants.Options.DEFAULT,
  keyboardActions: KeyboardActions = KeyboardConstants.Actions.NONE,
) {

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
      keyboardOptions = keyboardOptions,
      keyboardActions = keyboardActions,
      maxLines = maxLines,
      modifier = modifier,
      isError = validationError != null,
      supportingText = validationError?.let {
        { Text(text = it) }
      },
    )
  }
}
