package com.billcorea.jikgong.presentation.company.auth.join.page1.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.billcorea.jikgong.R
import com.billcorea.jikgong.presentation.company.auth.common.constants.JoinConstants
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.appColorScheme

@Composable
fun VerificationCodeInput(
    verificationCode: String,
    onVerificationCodeChange: (String) -> Unit,
    validationError: String? = null,
    onDone: () -> Unit = {},
    authCode: String? = null,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = modifier) {
        Spacer(modifier = Modifier.padding(5.dp))

        Text(
            text = stringResource(R.string.secretCode),
            color = appColorScheme.primary,
            lineHeight = 1.25.em,
            style = AppTypography.bodyMedium,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(5.dp))

        OutlinedTextField(
            value = verificationCode,
            onValueChange = { newValue ->
                // 6자리 숫자만 입력 허용
                if (newValue.length <= JoinConstants.SECRET_CODE_LENGTH && newValue.all { it.isDigit() }) {
                    onVerificationCodeChange(newValue)
                }
            },
            placeholder = {
                Text(text = stringResource(R.string.enterSecretCode))
            },
            maxLines = 1,
            enabled = !authCode.isNullOrEmpty(),    //  인증번호 받으면 활성화
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    onDone()
                }
            ),
            isError = validationError != null,  // 에러 상태 표시
            supportingText = validationError?.let {  // 에러 메시지 표시
                { Text(text = it) }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}