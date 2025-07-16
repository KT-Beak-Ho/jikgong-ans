// app/src/main/java/com/billcorea/jikgong/presentation/company/main/info/CompanyInfoScreen.kt
package com.billcorea.jikgong.presentation.company.main.info

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.company.main.info.components.InfoCard
import com.billcorea.jikgong.presentation.company.main.info.components.InfoHeader
import com.billcorea.jikgong.presentation.company.main.info.shared.CompanyInfoSharedEvent
import com.billcorea.jikgong.presentation.company.main.info.shared.CompanyInfoSharedViewModel
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Destination
@Composable
fun CompanyInfoScreen(
    navigator: DestinationsNavigator,
    viewModel: CompanyInfoSharedViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val shouldNavigateToLogin by viewModel.shouldNavigateToLogin.collectAsStateWithLifecycle()
    val selectedMenuId by viewModel.selectedMenuId.collectAsStateWithLifecycle()

    // 네비게이션 이벤트 처리
    LaunchedEffect(shouldNavigateToLogin) {
        if (shouldNavigateToLogin) {
            // 로그인 화면으로 이동
            // navigator.navigate(LoginScreenDestination)
            viewModel.clearNavigationEvents()
        }
    }

    LaunchedEffect(selectedMenuId) {
        selectedMenuId?.let { menuId ->
            // 각 메뉴별 네비게이션 처리
            when (menuId) {
                "saved_workers" -> {
                    // navigator.navigate(SavedWorkersScreenDestination)
                }
                "notifications" -> {
                    // navigator.navigate(NotificationSettingsScreenDestination)
                }
                "events" -> {
                    // navigator.navigate(EventsScreenDestination)
                }
                "announcements" -> {
                    // navigator.navigate(AnnouncementsScreenDestination)
                }
                "customer_service" -> {
                    // navigator.navigate(CustomerServiceScreenDestination)
                }
                "policies" -> {
                    // navigator.navigate(PoliciesScreenDestination)
                }
            }
            viewModel.clearNavigationEvents()
        }
    }

    // 에러 다이얼로그
    uiState.errorMessage?.let { message ->
        AlertDialog(
            onDismissRequest = {
                viewModel.onEvent(CompanyInfoSharedEvent.ClearError)
            },
            title = { Text("오류") },
            text = { Text(message) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.onEvent(CompanyInfoSharedEvent.ClearError)
                    }
                ) {
                    Text("확인")
                }
            }
        )
    }

    if (uiState.isLoading) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 프로필 헤더
            item {
                InfoHeader(companyInfo = uiState.companyInfo)
            }

            // 메뉴 아이템들
            items(uiState.menuItems) { menuItem ->
                InfoCard(menuItem = menuItem)
            }

            // 로그아웃 버튼
            item {
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = {
                        viewModel.onEvent(CompanyInfoSharedEvent.Logout)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFD32F2F)
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = Color(0xFFD32F2F)
                    )
                ) {
                    Text(
                        text = "로그아웃",
                        style = AppTypography.titleMedium.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CompanyInfoScreenPreview() {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme {
        CompanyInfoScreen(
            navigator = navigator,
            viewModel = CompanyInfoSharedViewModel()
        )
    }
}