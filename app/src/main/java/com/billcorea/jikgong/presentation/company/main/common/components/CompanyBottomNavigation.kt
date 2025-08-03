package com.billcorea.jikgong.presentation.company.main.common.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.billcorea.jikgong.ui.theme.appColorScheme

data class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val badge: Int? = null
)

object CompanyBottomNavItems {
    val items = listOf(
        BottomNavItem(
            route = CompanyBottomNavTabs.PROJECT_LIST,
            title = "프로젝트",
            icon = Icons.Default.Assignment
        ),
        BottomNavItem(
            route = CompanyBottomNavTabs.SCOUT,
            title = "스카웃",
            icon = Icons.Default.Search // PersonSearch 대신 Search 사용
        ),
        BottomNavItem(
            route = CompanyBottomNavTabs.MONEY,
            title = "임금관리",
            icon = Icons.Default.AttachMoney
        ),
        BottomNavItem(
            route = CompanyBottomNavTabs.INFO,
            title = "정보",
            icon = Icons.Default.Info
        )
    )
}

@Composable
fun CompanyBottomNavigation(
    currentRoute: String?,
    onTabSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier,
        containerColor = appColorScheme.surface,
        contentColor = appColorScheme.onSurface
    ) {
        CompanyBottomNavItems.items.forEach { item ->
            val isSelected = currentRoute == item.route

            NavigationBarItem(
                icon = {
                    BadgedBox(
                        badge = {
                            if (item.badge != null && item.badge > 0) {
                                Badge {
                                    Text(
                                        text = if (item.badge > 99) "99+" else item.badge.toString()
                                    )
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title
                        )
                    }
                },
                label = {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                selected = isSelected,
                onClick = { onTabSelected(item.route) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = appColorScheme.primary,
                    selectedTextColor = appColorScheme.primary,
                    unselectedIconColor = appColorScheme.onSurfaceVariant,
                    unselectedTextColor = appColorScheme.onSurfaceVariant,
                    indicatorColor = appColorScheme.primaryContainer
                )
            )
        }
    }
}