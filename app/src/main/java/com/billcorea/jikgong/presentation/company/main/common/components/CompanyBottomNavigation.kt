package com.billcorea.jikgong.presentation.company.main.common.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme

// 바텀 네비게이션 탭 데이터 클래스
data class CompanyBottomNavTab(
    val route: String,
    val title: String,
    val icon: ImageVector
)

// 바텀 네비게이션 탭 정의
object CompanyBottomNavTabs {
    val PROJECT_LIST = CompanyBottomNavTab(
        route = "company_project_list",
        title = "프로젝트 목록",
        icon = Icons.Default.List
    )

    val SCOUT = CompanyBottomNavTab(
        route = "company_scout",
        title = "인력 스카웃",
        icon = Icons.Default.Search
    )

    val MONEY = CompanyBottomNavTab(
        route = "company_money",
        title = "지급 관리",
        icon = Icons.Default.AttachMoney
    )

    val INFO = CompanyBottomNavTab(
        route = "company_info",
        title = "사업자 정보",
        icon = Icons.Default.AccountCircle
    )

    val tabs = listOf(PROJECT_LIST, SCOUT, MONEY, INFO)
}

@Composable
fun CompanyBottomNavigation(
    currentRoute: String,
    onTabSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = appColorScheme.surface,
        contentColor = appColorScheme.primary,
        tonalElevation = NavigationBarDefaults.Elevation
    ) {
        CompanyBottomNavTabs.tabs.forEach { tab ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.title,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(text = tab.title)
                },
                selected = currentRoute == tab.route,
                onClick = {
                    onTabSelected(tab.route)
                }
            )
        }
    }
}

// Preview 컴포저블들
@Preview(name = "프로젝트 목록 선택", showBackground = true)
@Composable
fun CompanyBottomNavigationProjectListPreview() {
    Jikgong1111Theme {
        CompanyBottomNavigation(
            currentRoute = CompanyBottomNavTabs.PROJECT_LIST.route,
            onTabSelected = {}
        )
    }
}

@Preview(name = "인력 스카웃 선택", showBackground = true)
@Composable
fun CompanyBottomNavigationScoutPreview() {
    Jikgong1111Theme {
        CompanyBottomNavigation(
            currentRoute = CompanyBottomNavTabs.SCOUT.route,
            onTabSelected = {}
        )
    }
}

@Preview(name = "지급 관리 선택", showBackground = true)
@Composable
fun CompanyBottomNavigationMoneyPreview() {
    Jikgong1111Theme {
        CompanyBottomNavigation(
            currentRoute = CompanyBottomNavTabs.MONEY.route,
            onTabSelected = {}
        )
    }
}

@Preview(name = "사업자 정보 선택", showBackground = true)
@Composable
fun CompanyBottomNavigationInfoPreview() {
    Jikgong1111Theme {
        CompanyBottomNavigation(
            currentRoute = CompanyBottomNavTabs.INFO.route,
            onTabSelected = {}
        )
    }
}

@Preview(name = "다크 테마", showBackground = true)
@Composable
fun CompanyBottomNavigationDarkPreview() {
    Jikgong1111Theme(darkTheme = true) {
        CompanyBottomNavigation(
            currentRoute = CompanyBottomNavTabs.PROJECT_LIST.route,
            onTabSelected = {}
        )
    }
}

@Preview(name = "모든 탭 표시", showBackground = true, widthDp = 360)
@Composable
fun CompanyBottomNavigationAllTabsPreview() {
    Jikgong1111Theme {
        CompanyBottomNavigation(
            currentRoute = "", // 아무것도 선택되지 않은 상태
            onTabSelected = {}
        )
    }
}