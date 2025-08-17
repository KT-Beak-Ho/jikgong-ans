package com.billcorea.jikgong.presentation.company.auth.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.company.auth.login.shared.CompanyLoginSharedEvent
import com.billcorea.jikgong.presentation.company.auth.login.shared.CompanyLoginSharedViewModel
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Destination
@Composable
fun CompanyLoginScreen(
    navigator: DestinationsNavigator,
    companyLoginViewModel: CompanyLoginSharedViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by companyLoginViewModel.uiState.collectAsStateWithLifecycle()

    // 네비게이션 이벤트 처리 - 뒤로가기
    LaunchedEffect(uiState.shouldNavigateBack) {
        if (uiState.shouldNavigateBack) {
            navigator.navigateUp()
            companyLoginViewModel.clearNavigationEvents()
        }
    }

    // 에러 다이얼로그 처리
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

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "기업 로그인",
                    style = AppTypography.headlineMedium,
                    color = appColorScheme.primary
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "로그인 화면 구현 예정",
                    style = AppTypography.bodyLarge,
                    color = appColorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        companyLoginViewModel.onEvent(CompanyLoginSharedEvent.PreviousPage)
                    }
                ) {
                    Text("뒤로가기")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CompanyLoginScreenPreview() {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme {
        CompanyLoginScreen(
            navigator = navigator
        )
    }
}