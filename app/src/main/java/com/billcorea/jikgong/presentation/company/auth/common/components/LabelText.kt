package com.billcorea.jikgong.presentation.company.auth.common.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.em
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.appColorScheme

@Composable
fun LabelText(
  modifier: Modifier = Modifier,
  mainText: String,                                //  main 문자열
  mainTextColor: Color = appColorScheme.primary,   //  main 문자열 색상
  appendText: String,                              //  추가 문자열 ( main 문자열 뒤에 붙임 )
  appendTextColor: Color = appColorScheme.primary, //  추가 문자열 색상 ( 기본값은 main 문자열 색상과 동일 )
) {
  val annotatedText = buildAnnotatedString {
    //  main 문자열
    withStyle(style = SpanStyle(color = mainTextColor)) {
      append(mainText)
    }
    //  추가 문자열
    if (appendText.isNotEmpty()) {
      withStyle(style = SpanStyle(color = appendTextColor)) {
        append(" $appendText")
      }
    }
  }

  Text(
    text = annotatedText,
    lineHeight = 1.25.em,
    style = AppTypography.bodyMedium,
    modifier = modifier.fillMaxWidth()
  )
}