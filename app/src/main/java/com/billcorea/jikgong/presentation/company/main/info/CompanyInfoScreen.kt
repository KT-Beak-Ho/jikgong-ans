// app/src/main/java/com/billcorea/jikgong/presentation/company/main/info/CompanyInfoScreen.kt
package com.billcorea.jikgong.presentation.company.main.info

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Composable
fun CompanyInfoScreen(
    navigator: DestinationsNavigator,
    viewModel: CompanyInfoSharedViewModel = koinViewModel(),
    modifier: Modifier = Modifier,
    showBottomBar: Boolean = true
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

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            CompanyInfoContent(
                uiState = uiState,
                onEvent = viewModel::onEvent,
                showBottomBar = showBottomBar,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun CompanyInfoContent(
    uiState: com.billcorea.jikgong.presentation.company.main.info.shared.CompanyInfoSharedUiState,
    onEvent: (CompanyInfoSharedEvent) -> Unit,
    showBottomBar: Boolean,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberLazyListState()

    // 스크롤 진행률 계산
    val scrollProgress by remember {
        derivedStateOf {
            if (scrollState.layoutInfo.totalItemsCount <= 1) return@derivedStateOf 0f

            val firstVisibleItemIndex = scrollState.firstVisibleItemIndex
            val firstVisibleItemScrollOffset = scrollState.firstVisibleItemScrollOffset
            val itemHeight = scrollState.layoutInfo.visibleItemsInfo.firstOrNull()?.size ?: 1

            val totalScrollableHeight = (scrollState.layoutInfo.totalItemsCount - 1) * itemHeight
            val currentScrollOffset = firstVisibleItemIndex * itemHeight + firstVisibleItemScrollOffset

            if (totalScrollableHeight > 0) {
                (currentScrollOffset.toFloat() / totalScrollableHeight.toFloat()).coerceIn(0f, 1f)
            } else {
                0f
            }
        }
    }

    // 스크롤 가능 여부 확인
    val canScroll by remember {
        derivedStateOf {
            scrollState.layoutInfo.totalItemsCount > 0 &&
                    (scrollState.canScrollForward || scrollState.canScrollBackward || scrollState.firstVisibleItemScrollOffset > 0)
        }
    }

    Box(modifier = modifier) {
        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp,
                    end = if (canScroll) 28.dp else 16.dp, // 스크롤바 공간 확보
                    top = 16.dp,
                    bottom = 16.dp
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(
                bottom = if (showBottomBar) 100.dp else 20.dp // 바텀 네비게이션 공간 확보
            )
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
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedButton(
                    onClick = {
                        onEvent(CompanyInfoSharedEvent.Logout)
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
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "로그아웃",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "로그아웃",
                            style = AppTypography.titleMedium.copy(
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }

            // 추가 여백 (미래 확장을 위한 공간)
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // 스크롤바
        if (canScroll) {
            ScrollBar(
                scrollProgress = scrollProgress,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()
                    .width(8.dp)
                    .padding(
                        end = 4.dp,
                        top = 20.dp,
                        bottom = if (showBottomBar) 120.dp else 40.dp
                    )
            )
        }
    }
}

@Composable
private fun ScrollBar(
    scrollProgress: Float,
    modifier: Modifier = Modifier,
    isVisible: Boolean = true
) {
    if (isVisible) {
        Box(
            modifier = modifier
                .alpha(if (scrollProgress > 0f || scrollProgress < 1f) 0.8f else 0.4f)
        ) {
            // 스크롤바 트랙
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = appColorScheme.outline.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(4.dp)
                    )
            )

            // 스크롤바 썸 (thumb)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.15f) // 썸의 높이 (전체의 15%)
                    .offset(y = (scrollProgress * 85).dp) // 85% = 100% - 15%
                    .background(
                        color = appColorScheme.primary.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        }
    }
}

// Preview 컴포저블들
@Preview(name = "기본 화면", showBackground = true, heightDp = 800)
@Composable
fun CompanyInfoScreenPreview() {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme {
        CompanyInfoScreen(
            navigator = navigator,
            viewModel = CompanyInfoSharedViewModel(),
            showBottomBar = true
        )
    }
}

@Preview(name = "바텀바 없음", showBackground = true, heightDp = 800)
@Composable
fun CompanyInfoScreenNoBottomBarPreview() {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme {
        CompanyInfoScreen(
            navigator = navigator,
            viewModel = CompanyInfoSharedViewModel(),
            showBottomBar = false
        )
    }
}

@Preview(name = "로딩 상태", showBackground = true, heightDp = 800)
@Composable
fun CompanyInfoScreenLoadingPreview() {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Preview(name = "다크 테마", showBackground = true, heightDp = 800)
@Composable
fun CompanyInfoScreenDarkPreview() {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme(darkTheme = true) {
        CompanyInfoScreen(
            navigator = navigator,
            viewModel = CompanyInfoSharedViewModel(),
            showBottomBar = true
        )
    }
}

@Preview(name = "태블릿 크기", showBackground = true, widthDp = 800, heightDp = 1200)
@Composable
fun CompanyInfoScreenTabletPreview() {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme {
        CompanyInfoScreen(
            navigator = navigator,
            viewModel = CompanyInfoSharedViewModel(),
            showBottomBar = true
        )
    }
}