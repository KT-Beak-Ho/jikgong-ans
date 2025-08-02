package com.billcorea.jikgong.presentation.company.main.common.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import com.billcorea.jikgong.ui.theme.appColorScheme

/**
 * 바텀 네비게이션 아이템 데이터 클래스
 */
data class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val badge: Int? = null
)

/**
 * 기업용 바텀 네비게이션 아이템들
 */
object CompanyBottomNavItems {
    val items = listOf(
        BottomNavItem(
            route = "project_list",
            title = "프로젝트",
            icon = Icons.AutoMirrored.Filled.List // 수정됨
        ),
        BottomNavItem(
            route = "money",
            title = "임금관리",
            icon = Icons.Default.AttachMoney
        ),
        BottomNavItem(
            route = "scout",
            title = "스카웃",
            icon = Icons.Default.PersonSearch
        ),
        BottomNavItem(
            route = "info",
            title = "정보",
            icon = Icons.Default.Info
        )
    )
}

@Composable
fun CompanyBottomNavigation(
    currentRoute: String?,
    onRouteSelected: (String) -> Unit,
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier
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
                        style = androidx.compose.material3.MaterialTheme.typography.labelSmall
                    )
                },
                selected = isSelected,
                onClick = { onRouteSelected(item.route) },
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
