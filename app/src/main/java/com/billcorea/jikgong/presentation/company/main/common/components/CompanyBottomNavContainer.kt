// ========================================
// 📄 수정된 CompanyBottomNavContainer.kt
// ========================================
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
    const val MONEY = "money"
    const val SCOUT = "scout"
    const val INFO = "info"
}

/**
 * 기업용 바텀 네비게이션 컨테이너 - 메인 진입점
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
 * 바텀 네비게이션 탭 이동 처리
 */
private fun navigateToTab(navController: NavController, route: String) {
    if (navController.currentDestination?.route != route) {
        navController.navigate(route) {
            // 백스택을 정리하여 탭 간 이동 시 스택이 쌓이지 않도록 함
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            // 동일한 탭을 다시 선택해도 새로운 인스턴스를 생성하지 않음
            launchSingleTop = true
            // 이전 상태를 복원
            restoreState = true
        }
    }
}

/**
 * 기업용 바텀 네비게이션 호스트
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
        // 프로젝트 목록 화면
        composable(CompanyBottomNavTabs.PROJECT_LIST) {
            ProjectListScreen(
                navigator = navigator,
                showBottomBar = false // 이미 컨테이너에서 표시하므로 false
            )
        }

        // 임금 관리 화면
        composable(CompanyBottomNavTabs.MONEY) {
            CompanyMoneyScreen(
                navigator = navigator,
                showBottomBar = false
            )
        }

        // 인력 스카웃 화면
        composable(CompanyBottomNavTabs.SCOUT) {
            CompanyScoutScreen(
                navigator = navigator,
                showBottomBar = false
            )
        }

        // 사업자 정보 화면
        composable(CompanyBottomNavTabs.INFO) {
            CompanyInfoScreen(
                navigator = navigator,
                showBottomBar = false
            )
        }
    }
}

@Preview(name = "기본 화면", showBackground = true, heightDp = 800)
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