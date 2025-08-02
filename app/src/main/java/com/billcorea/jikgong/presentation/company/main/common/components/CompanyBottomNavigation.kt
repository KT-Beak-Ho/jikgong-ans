// app/src/main/java/com/billcorea/jikgong/presentation/company/main/common/components/CompanyBottomNavigation.kt
package com.billcorea.jikgong.presentation.company.main.common.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme

// 바텀 네비게이션 탭 데이터 클래스
data class CompanyBottomNavTab(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

// 바텀 네비게이션 탭 정의 (토스 스타일)
object CompanyBottomNavTabs {
    val PROJECT_LIST = CompanyBottomNavTab(
        route = "company_project_list",
        title = "프로젝트",
        selectedIcon = Icons.Filled.List,
        unselectedIcon = Icons.Outlined.List
    )

    val SCOUT = CompanyBottomNavTab(
        route = "company_scout",
        title = "인력찾기",
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Outlined.Search
    )

    val MONEY = CompanyBottomNavTab(
        route = "company_money",
        title = "지급관리",
        selectedIcon = Icons.Filled.AttachMoney,
        unselectedIcon = Icons.Outlined.AttachMoney
    )

    val INFO = CompanyBottomNavTab(
        route = "company_info",
        title = "내정보",
        selectedIcon = Icons.Filled.AccountCircle,
        unselectedIcon = Icons.Outlined.AccountCircle
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
        tonalElevation = 8.dp
    ) {
        CompanyBottomNavTabs.tabs.forEach { tab ->
            val isSelected = currentRoute == tab.route

            // 애니메이션 색상
            val iconColor by animateColorAsState(
                targetValue = if (isSelected) appColorScheme.primary else appColorScheme.onSurface.copy(alpha = 0.6f),
                animationSpec = tween(200),
                label = "iconColor"
            )

            val textColor by animateColorAsState(
                targetValue = if (isSelected) appColorScheme.primary else appColorScheme.onSurface.copy(alpha = 0.6f),
                animationSpec = tween(200),
                label = "textColor"
            )

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                        contentDescription = tab.title,
                        modifier = Modifier.size(24.dp),
                        tint = iconColor
                    )
                },
                label = {
                    Text(
                        text = tab.title,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        color = textColor
                    )
                },
                selected = isSelected,
                onClick = {
                    onTabSelected(tab.route)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = appColorScheme.primary,
                    selectedTextColor = appColorScheme.primary,
                    unselectedIconColor = appColorScheme.onSurface.copy(alpha = 0.6f),
                    unselectedTextColor = appColorScheme.onSurface.copy(alpha = 0.6f),
                    indicatorColor = appColorScheme.primary.copy(alpha = 0.1f)
                )
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

@Preview(name = "인력찾기 선택", showBackground = true)
@Composable
fun CompanyBottomNavigationScoutPreview() {
    Jikgong1111Theme {
        CompanyBottomNavigation(
            currentRoute = CompanyBottomNavTabs.SCOUT.route,
            onTabSelected = {}
        )
    }
}

@Preview(name = "지급관리 선택", showBackground = true)
@Composable
fun CompanyBottomNavigationMoneyPreview() {
    Jikgong1111Theme {
        CompanyBottomNavigation(
            currentRoute = CompanyBottomNavTabs.MONEY.route,
            onTabSelected = {}
        )
    }
}

@Preview(name = "내정보 선택", showBackground = true)
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