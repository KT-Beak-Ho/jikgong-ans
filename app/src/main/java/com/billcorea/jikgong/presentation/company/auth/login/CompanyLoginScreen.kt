package com.billcorea.jikgong.presentation.company.auth.login

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.afollestad.materialdialogs.MaterialDialog
import com.billcorea.jikgong.R
import com.billcorea.jikgong.api.models.response.ApiResult
import com.billcorea.jikgong.api.models.response.EmailValidationResponse
import com.billcorea.jikgong.api.models.response.LoginIdValidationResponse
import com.billcorea.jikgong.api.models.response.LoginResponse
import com.billcorea.jikgong.api.models.response.PhoneValidationResponse
import com.billcorea.jikgong.api.models.response.SmsVerificationResponse
import com.billcorea.jikgong.api.repository.join.JoinRepository
import com.billcorea.jikgong.api.repository.login.LoginRepository
import com.billcorea.jikgong.presentation.common.KeyboardConstants
import com.billcorea.jikgong.presentation.company.auth.common.components.CommonButton
import com.billcorea.jikgong.presentation.company.auth.common.components.CommonTextInput
import com.billcorea.jikgong.presentation.company.auth.common.components.CommonTopBar
import com.billcorea.jikgong.presentation.company.auth.common.constants.JoinConstants
import com.billcorea.jikgong.presentation.company.auth.join.shared.CompanyJoinSharedEvent
import com.billcorea.jikgong.presentation.company.auth.join.shared.CompanyJoinSharedViewModel
import com.billcorea.jikgong.presentation.company.auth.login.shared.CompanyLoginSharedEvent
import com.billcorea.jikgong.presentation.company.auth.login.shared.CompanyLoginSharedViewModel
import com.billcorea.jikgong.presentation.destinations.CompanyProjectListScreenDestination
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.billcorea.jikgong.utils.MainViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Destination
@Composable
fun CompanyLoginScreen(
    companyLoginViewModel: CompanyLoginSharedViewModel = koinViewModel(),
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier
) {

    val uiState by companyLoginViewModel.uiState.collectAsStateWithLifecycle()
//    val shouldNavigateToNextPage by companyLoginSharedViewModel.shouldNavigateToNextPage.collectAsStateWithLifecycle()
//    val shouldNavigateBack by companyLoginSharedViewModel.shouldNavigateBack.collectAsStateWithLifecycle()

    // 페이지 실행 시 초기화
    LaunchedEffect(Unit) {
        //  로컬 변수 초기화
        companyLoginViewModel.onEvent(CompanyLoginSharedEvent.ResetJoin1Flow)
        //  에러 변수 초기화
        companyLoginViewModel.onEvent(CompanyLoginSharedEvent.ClearError)
    }

//    LaunchedEffect(loginResult) {
//        loginResult?.let {
//            // 기업 로그인 성공 시 CompanyProjectListScreen으로 이동
//            navigator.navigate(CompanyProjectListScreenDestination)
//        }
//    }
    /** 에러 다이얼로그 처리 */
    uiState.errorMessage?.let { message ->
        AlertDialog(
            onDismissRequest = {
                companyLoginViewModel.onEvent(CompanyLoginSharedEvent.ClearError)
            },
            title = { Text("알림") },
            text = { Text(message) },
            confirmButton = {
                TextButton(
                    onClick = {
                        companyLoginViewModel.onEvent(CompanyLoginSharedEvent.ClearError)
                    }
                ) {
                    Text("확인")
                }
            }
        )
    }

    /** 화면 시작 */
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 20.dp)
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                val y = size.height - strokeWidth / 2
                drawLine(
                    color = appColorScheme.outlineVariant,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = strokeWidth
                )
            },
        topBar = {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(5.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                IconButton(onClick = {
//                    Log.e("", "backArrow")
//                    navigator.navigateUp()
//                }) {
//                    Icon(
//                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
//                        contentDescription = "Arrow Back"
//                    )
//                }
//                Text(
//                    text = "기업 로그인",
//                    color = appColorScheme.onPrimaryContainer,
//                    style = AppTypography.titleMedium,
//                )
//            }
            CommonTopBar(
                currentPage = uiState.currentPage,
                totalPages = JoinConstants.TOTAL_PAGES,
                onBackClick = {
                    companyLoginViewModel.onEvent(CompanyLoginSharedEvent.PreviousPage)
                }
            )
        }
    ) {
        /** 중앙 (메인) */
        innerPadding ->
//        Text(
//            text = stringResource(R.string.enterYourInfo),
//            color = appColorScheme.primary,
//            lineHeight = 1.33.em,
//            style = AppTypography.titleLarge,
//            modifier = Modifier
//                .fillMaxWidth()
//                .wrapContentHeight(align = Alignment.CenterVertically)
//        )
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            /** Login ID 입력 받는 섹션*/
            CommonTextInput(
                value = uiState.id,
//                labelMainText = stringResource(R.string.id),
//                labelAppendText = "*",
//                labelAppendTextColor = colorResource(R.color.secondary_B),
                placeholder = stringResource(R.string.enterId),
                validationError = uiState.validationErrors["id"],
                modifier = Modifier.fillMaxWidth(),
                onChange = {
//                    companyLoginViewModel.onEvent(CompanyJoinSharedEvent.UpdateUserId(it))
                }
            )
            Spacer(modifier = Modifier.padding(4.dp))

            /** Login Password 입력 받는 섹션*/
            CommonTextInput(
                value = uiState.password,
//                labelMainText = stringResource(R.string.password),
//                labelAppendText = "*",
//                labelAppendTextColor = colorResource(R.color.secondary_B),
                placeholder = stringResource(R.string.enterPassword),
                validationError = uiState.validationErrors["password"],
                modifier = Modifier.fillMaxWidth(),
                onChange = {
//                    companyLoginViewModel.onEvent(CompanyJoinSharedEvent.UpdateUserPassword(it))
                }
            )
            Spacer(modifier = Modifier.padding(4.dp))

//            Spacer(modifier = Modifier.padding(5.dp))
//
//            OutlinedTextField(
//                value = loginIdOrPhone,
//                onValueChange = {
//                    loginIdOrPhone = it
//                },
//                placeholder = {
//                    Text(text = stringResource(R.string.loginIdOrPhone))
//                },
//                keyboardOptions = KeyboardOptions(
//                    keyboardType = KeyboardType.Text,
//                    imeAction = ImeAction.Next
//                ),
//                maxLines = 1,
//                modifier = Modifier
//                    .width((screenWidth * .90).dp)
//                    .align(Alignment.CenterHorizontally)
//                    .focusRequester(focusRequester)
//            )
//
//            Spacer(modifier = Modifier.padding(4.dp))
//
//            OutlinedTextField(
//                value = password,
//                onValueChange = {
//                    password = it
//                },
//                placeholder = {
//                    Text(text = stringResource(R.string.password))
//                },
//                visualTransformation = PasswordVisualTransformation(),
//                maxLines = 1,
//                keyboardOptions = KeyboardOptions(
//                    keyboardType = KeyboardType.Password,
//                    imeAction = ImeAction.Done
//                ),
//                keyboardActions = KeyboardActions(onDone = {
//                    keyboardController?.hide()
//                    focusManager.clearFocus()
//                }),
//                modifier = Modifier
//                    .width((screenWidth * .90).dp)
//                    .align(Alignment.CenterHorizontally)
//            )
//
//            Spacer(modifier = Modifier.padding(10.dp))
            /** 로그인 버튼 */
            CommonButton(
                text = stringResource(R.string.login),
                onClick = {
//                    companyLoginViewModel.onEvent(CompanyJoinSharedEvent.NextPage)
                },
                enabled = uiState.isPage1Complete,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(WindowInsets.navigationBars.asPaddingValues())
//                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
//            // 로그인 버튼
//            TextButton(
//                onClick = {
//                    if (loginIdOrPhone.isNotBlank() && password.isNotBlank()) {
//                        val deviceToken = "test_device_token"
//                        viewModel.doLogin(loginIdOrPhone, password, deviceToken)
//                    } else {
//                        MaterialDialog(context).show {
//                            icon(R.drawable.ic_jikgong_white)
//                            message(R.string.errorLoginBlank)
//                            positiveButton(R.string.OK) { it.dismiss() }
//                        }
//                    }
//                },
//                modifier = Modifier
//                    .width((screenWidth * .90).dp)
//                    .align(Alignment.CenterHorizontally)
//                    .padding(WindowInsets.navigationBars.asPaddingValues())
//                    .background(appColorScheme.primary)
//            ) {
//                Text(
//                    text = stringResource(R.string.login),
//                    color = appColorScheme.onPrimary,
//                    lineHeight = 1.25.em,
//                    style = AppTypography.labelMedium,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .wrapContentWidth(Alignment.CenterHorizontally)
//                )
//            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CompanyLoginScreenPreview() {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    val emptyRepository = object : LoginRepository {
        override suspend fun requestLogin(loginIdOrPhone: String, password: String, deviceToken: String): ApiResult<LoginResponse> =
            ApiResult.Error(Exception("Preview mode"))

    }
    Jikgong1111Theme {
        CompanyLoginScreen(
            companyLoginViewModel = CompanyLoginSharedViewModel(emptyRepository),
            navigator, modifier = Modifier.padding(3.dp))
    }
}