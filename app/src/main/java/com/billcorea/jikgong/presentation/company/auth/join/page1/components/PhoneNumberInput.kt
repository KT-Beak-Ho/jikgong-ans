package com.billcorea.jikgong.presentation.company.auth.join.page1.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.billcorea.jikgong.R
import com.billcorea.jikgong.presentation.company.auth.common.components.CommonButton
import com.billcorea.jikgong.presentation.company.auth.common.constants.JoinConstants
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.appColorScheme

@Composable
fun PhoneNumberInput(
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    onRequestCode: () -> Unit,
    onFocusChanged: (Boolean) -> Unit = {},
    validationError: String? = null,
    isWaiting:Boolean = false,
    isValidPhoneNumber:Boolean = false,
    modifier: Modifier = Modifier
) {
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp
    val focusRequester = remember { FocusRequester() }

    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.telnumber),
            color = appColorScheme.primary,
            lineHeight = 1.25.em,
            style = AppTypography.bodyMedium,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(5.dp))

        //  전화번호 입력
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = onPhoneNumberChange,
            placeholder = {
                Text(text = stringResource(R.string.enterForNumberOnly))
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { onRequestCode() }
            ),
            maxLines = 1,
            enabled = !isWaiting,
            isError = validationError != null,
            supportingText = validationError?.let {
                { Text(text = it) }
            },
            modifier = Modifier
                .fillMaxWidth()  // 전체 너비 사용
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    onFocusChanged(focusState.isFocused)
                }
                .clickable { focusRequester.requestFocus() }
        )

        Spacer(modifier = Modifier.padding(5.dp))

        //  인증번호 받기 버튼
        CommonButton(
            text = stringResource(R.string.getSecretCode),
            onClick = onRequestCode,
            enabled = phoneNumber.isNotEmpty() && isValidPhoneNumber,
            isLoading = isWaiting,
            modifier = Modifier.fillMaxWidth()  // 전체 너비 사용
        )
    }
}