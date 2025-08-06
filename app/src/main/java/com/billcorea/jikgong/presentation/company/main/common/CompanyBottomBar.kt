// app/src/main/java/com/billcorea/jikgong/presentation/company/common/CompanyBottomBar.kt
package com.billcorea.jikgong.presentation.company.commonc

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.ui.theme.appColorScheme

enum class CompanyBottomNavItem(
  val route: String,
  val title: String,
  val selectedIcon: ImageVector,
  val unselectedIcon: ImageVector
) {
  PROJECTS(
    route = "company/projectlist",
    title = "프로젝트 목록",
    selectedIcon = Icons.Filled.Work,
    unselectedIcon = Icons.Outlined.Work
  ),
  SCOUT(
    route = "company/scout",
    title = "인력 스카우트",
    selectedIcon = Icons.Filled.PersonSearch,
    unselectedIcon = Icons.Outlined.PersonSearch
  ),
  MONEY(
    route = "company/money",
    title = "자금 관리",
    selectedIcon = Icons.Filled.AccountBalance,
    unselectedIcon = Icons.Outlined.AccountBalance
  ),
  INFO(
    route = "company/info",
    title = "사업자 정보",
    selectedIcon = Icons.Filled.Person,
    unselectedIcon = Icons.Outlined.Person
  )
}

@Composable
fun CompanyBottomBar(
  navController: NavController,
  currentRoute: String,
  modifier: Modifier = Modifier
) {
  NavigationBar(
    modifier = modifier,
    containerColor = Color.White,
    tonalElevation = 8.dp
  ) {
    CompanyBottomNavItem.entries.forEach { item ->
      val selected = currentRoute.startsWith(item.route)

      NavigationBarItem(
        icon = {
          Icon(
            imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
            contentDescription = item.title,
            modifier = Modifier.size(24.dp)
          )
        },
        label = {
          Text(
            text = item.title,
            fontSize = 11.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            maxLines = 1
          )
        },
        selected = selected,
        onClick = {
          if (!selected) {
            navController.navigate(item.route) {
              // 백스택 관리
              launchSingleTop = true
              restoreState = true
            }
          }
        },
        colors = NavigationBarItemDefaults.colors(
          selectedIconColor = appColorScheme.primary,
          selectedTextColor = appColorScheme.primary,
          indicatorColor = appColorScheme.primaryContainer.copy(alpha = 0.3f),
          unselectedIconColor = Color(0xFF757575),
          unselectedTextColor = Color(0xFF757575)
        )
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun CompanyBottomBarPreview() {
  com.billcorea.jikgong.ui.theme.Jikgong1111Theme {
    CompanyBottomBar(
      navController = rememberNavController(),
      currentRoute = "company/projectlist"
    )
  }
}