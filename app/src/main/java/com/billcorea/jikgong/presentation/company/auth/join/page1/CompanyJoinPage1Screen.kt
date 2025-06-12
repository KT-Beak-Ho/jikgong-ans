package com.billcorea.jikgong.presentation.company.auth.join.page1

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.R
import com.billcorea.jikgong.presentation.company.auth.common.components.CommonButton
import com.billcorea.jikgong.presentation.company.auth.common.components.CommonTopBar
import com.billcorea.jikgong.presentation.company.auth.common.constants.JoinConstants
import com.billcorea.jikgong.presentation.company.auth.join.page1.components.PhoneNumberInput
import com.billcorea.jikgong.presentation.company.auth.join.page1.components.VerificationCodeInput
import com.billcorea.jikgong.presentation.company.auth.join.shared.CompanyJoinSharedViewModel
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import org.koin.androidx.compose.koinViewModel
import androidx.core.content.edit
import com.billcorea.jikgong.presentation.company.auth.join.shared.CompanyJoinNavigationEvent
import com.billcorea.jikgong.presentation.company.auth.join.shared.CompanyJoinSharedEvent

@Destination
@Composable
fun CompanyJoinPage1Screen(
    companyJoinViewModel: CompanyJoinSharedViewModel = koinViewModel(),
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier
) {
    val uiState by companyJoinViewModel.uiState.collectAsStateWithLifecycle()
    val navigationEvent by companyJoinViewModel.navigationEvent.collectAsStateWithLifecycle()
    //  LiveData
    val authCode by companyJoinViewModel.authCode.observeAsState()

    // 페이지 실행 시 초기화
    LaunchedEffect(Unit) {
        companyJoinViewModel.onEvent(CompanyJoinSharedEvent.ResetJoin1Flow)
    }

    // 네비게이션 이벤트 처리
    LaunchedEffect(navigationEvent) {
        when (navigationEvent) {
            is CompanyJoinNavigationEvent.NavigateToPage -> {
                when ((navigationEvent as CompanyJoinNavigationEvent.NavigateToPage).page) {
                    2 -> {

//                        navigator.navigate(JoinPage2Destination)
                    }
                    // 추후 다른 페이지들 추가
                }
                companyJoinViewModel.clearNavigationEvent()
            }
            is CompanyJoinNavigationEvent.NavigateBack -> {
                navigator.navigateUp()
                companyJoinViewModel.clearNavigationEvent()
            }
            is CompanyJoinNavigationEvent.JoinSuccess -> {
                // 회원가입 성공 처리 (추후 구현)
                companyJoinViewModel.clearNavigationEvent()
            }
            null -> { /* 이벤트 없음 */ }
        }
    }

    // 🚨 에러 다이얼로그 처리
    uiState.errorMessage?.let { message ->
        AlertDialog(
            onDismissRequest = {
                companyJoinViewModel.onEvent(CompanyJoinSharedEvent.ClearError)
            },
            title = { Text("알림") },
            text = { Text(message) },
            confirmButton = {
                TextButton(
                    onClick = {
                        companyJoinViewModel.onEvent(CompanyJoinSharedEvent.ClearError)
                    }
                ) {
                    Text("확인")
                }
            }
        )
    }

    //  화면 시작
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 20.dp),
        //  상단바
        topBar = {
            CommonTopBar(
                currentPage = uiState.currentPage,
                totalPages = JoinConstants.TOTAL_PAGES,
                onBackClick = {
                    companyJoinViewModel.onEvent(CompanyJoinSharedEvent.PreviousPage)
                }
            )
        },
        //  하단바
        bottomBar = {
            //  다음 버튼
            CommonButton(
                text = stringResource(R.string.next),
                onClick = {
                    companyJoinViewModel.onEvent(CompanyJoinSharedEvent.NextPage)
                },
                enabled = uiState.isPage1Complete,
                modifier =
                    Modifier
                    .fillMaxWidth()
                    .padding(WindowInsets.navigationBars.asPaddingValues())
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    ) {
        //  중앙 (메인)
        innerPadding ->
        //  전화 번호 입력 섹션
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            //  전화 번호 입력 헤더 메시지
            Text(
                text = stringResource(R.string.enterPhoneNumber),
                color = appColorScheme.primary,
                lineHeight = 1.33.em,
                style = AppTypography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(align = Alignment.CenterVertically)
            )

            Spacer(modifier = Modifier.padding(8.dp))

            // 전화 번호 입력 받는 섹션
            PhoneNumberInput(
                phoneNumber = uiState.phoneNumber,
                onPhoneNumberChange = {
                    companyJoinViewModel.onEvent(CompanyJoinSharedEvent.UpdatePhoneNumber(it))
                },
                onRequestCode = {
                    companyJoinViewModel.onEvent(CompanyJoinSharedEvent.RequestVerificationCode)
                },
                onFocusChanged = { isFocused ->
                    // 포커스 변경 시 인증 단계 초기화 (기존 로직 유지)
                    if (isFocused && uiState.isSecurityStepActive) {
                        companyJoinViewModel.onEvent(CompanyJoinSharedEvent.UpdatePhoneNumber(uiState.phoneNumber))
                    }
                },
                isValidPhoneNumber = uiState.isValidPhoneNumber,
                isWaiting = uiState.isWaiting,
                validationError = uiState.validationErrors["phoneNumber"]
            )

            //  인증번호 입력 섹션 (조건부 표시)
            if (uiState.isSecurityStepActive) {
                VerificationCodeInput(
                    verificationCode = uiState.verificationCode,
                    onVerificationCodeChange = {
                        companyJoinViewModel.onEvent(CompanyJoinSharedEvent.UpdateVerificationCode(it))
                    },
                    onDone = {
                        companyJoinViewModel.onEvent(CompanyJoinSharedEvent.VerifyPhoneNumber)
                    },
                    authCode = authCode,
                    validationError = uiState.validationErrors["verificationCode"]  // 에러 메시지 전달
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun JoinPage1ScreenPreview() {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme {
        CompanyJoinPage1Screen(
            companyJoinViewModel = CompanyJoinSharedViewModel(), // ViewModel 직접 생성
            navigator = navigator,
            modifier = Modifier.padding(3.dp)
        )
    }
}