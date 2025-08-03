package com.billcorea.jikgong.presentation.company.main.common.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.company.main.info.CompanyInfoScreen
import com.billcorea.jikgong.presentation.company.main.money.CompanyMoneyScreen
import com.billcorea.jikgong.presentation.company.main.projectlist.ProjectListScreen
import com.billcorea.jikgong.presentation.company.main.scout.CompanyScoutScreen
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator

/**
 * 기업용 바텀 네비게이션 탭 정의
 */
object CompanyBottomNavTabs {
    const val PROJECT_LIST = "project_list"
    const val SCOUT = "scout"
    const val MONEY = "money"
    const val INFO = "info"
}

/**
 * 🏢 기업용 바텀 네비게이션 컨테이너 - 수정된 버전
 *
 * 바텀바 네비게이션 문제 해결:
 * - 올바른 라우트 정의
 * - 네비게이션 스택 관리 개선
 * - 각 화면별 연결 확인
 */
@Destination(route = "company_main")
@Composable
fun CompanyBottomNavContainer(
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: CompanyBottomNavTabs.PROJECT_LIST

    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            CompanyBottomNavigation(
                currentRoute = currentRoute,
                onTabSelected = { route ->
                    navigateToTab(navController, route)
                }
            )
        }
    ) { innerPadding ->
        CompanyBottomNavHost(
            navController = navController,
            navigator = navigator,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}

/**
 * 바텀 네비게이션 탭 이동 처리 - 개선된 버전
 */
private fun navigateToTab(navController: NavController, route: String) {
    val currentRoute = navController.currentDestination?.route

    if (currentRoute != route) {
        navController.navigate(route) {
            // 시작 목적지까지 모든 항목을 팝
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            // 같은 탭을 다시 선택했을 때 새 인스턴스를 생성하지 않음
            launchSingleTop = true
            // 이전에 저장된 상태를 복원
            restoreState = true
        }
    }
}

/**
 * 기업용 바텀 네비게이션 호스트 - 모든 화면 연결
 */
@Composable
private fun CompanyBottomNavHost(
    navController: NavHostController,
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = CompanyBottomNavTabs.PROJECT_LIST,
        modifier = modifier
    ) {
        // 📋 프로젝트 목록 화면
        composable(CompanyBottomNavTabs.PROJECT_LIST) {
            ProjectListScreen(
                navigator = navigator,
                showBottomBar = false // 컨테이너에서 이미 표시
            )
        }

        // 🕵️ 인력 스카웃 화면
        composable(CompanyBottomNavTabs.SCOUT) {
            CompanyScoutScreen(
                navigator = navigator
            )
        }

        // 💰 임금 관리 화면
        composable(CompanyBottomNavTabs.MONEY) {
            CompanyMoneyScreen(
                navigator = navigator
            )
        }

        // ℹ️ 사업자 정보 화면
        composable(CompanyBottomNavTabs.INFO) {
            CompanyInfoScreen(
                navigator = navigator
            )
        }
    }
}

@Preview(name = "기업 메인 화면", showBackground = true, heightDp = 800)
@Composable
fun CompanyBottomNavContainerPreview() {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme {
        CompanyBottomNavContainer(
            navigator = navigator
        )
    }
}