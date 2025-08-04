// app/src/main/java/com/billcorea/jikgong/presentation/company/common/CompanyBottomBar.kt
package com.billcorea.jikgong.presentation.company.common

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.billcorea.jikgong.presentation.destinations.*

enum class CompanyBottomNavItem(
  val route: String,
  val title: String,
  val selectedIcon: ImageVector,
  val unselectedIcon: ImageVector
) {
  PROJECTS(
    route = "company/projectlist",
    title = "프로젝트",
    selectedIcon = Icons.Filled.Work,
    unselectedIcon = Icons.Outlined.Work
  ),
  SCOUT(
    route = "company/scout",
    title = "인재스카우트",
    selectedIcon = Icons.Filled.PersonSearch,
    unselectedIcon = Icons.Outlined.PersonSearch
  ),
  MONEY(
    route = "company/money",
    title = "자금관리",
    selectedIcon = Icons.Filled.AccountBalance,
    unselectedIcon = Icons.Outlined.AccountBalance
  ),
  INFO(
    route = "company/info",
    title = "마이페이지",
    selectedIcon = Icons.Filled.Person,
    unselectedIcon = Icons.Outlined.Person
  )
}

@Composable
fun CompanyBottomBar(
  navigator: DestinationsNavigator,
  currentRoute: String,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier.fillMaxWidth(),
    color = Color.White,
    shadowElevation = 8.dp
  ) {
    NavigationBar(
      modifier = Modifier.height(80.dp),
      containerColor = Color.White,
      tonalElevation = 0.dp
    ) {
      CompanyBottomNavItem.entries.forEach { item ->
        val selected = currentRoute == item.route

        NavigationBarItem(
          icon = {
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.Center
            ) {
              Icon(
                imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                contentDescription = item.title,
                modifier = Modifier.size(28.dp)
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = item.title,
                fontSize = 11.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                color = if (selected) Color(0xFF1976D2) else Color(0xFF757575)
              )
            }
          },
          selected = selected,
          onClick = {
            if (!selected) {
              when (item) {
                CompanyBottomNavItem.PROJECTS -> {
                  navigator.navigate(CompanyProjectListScreenDestination) {
                    popUpTo(0)
                    launchSingleTop = true
                  }
                }
                CompanyBottomNavItem.SCOUT -> {
                  // TODO: navigator.navigate(CompanyScoutScreenDestination)
                }
                CompanyBottomNavItem.MONEY -> {
                  // TODO: navigator.navigate(CompanyMoneyScreenDestination)
                }
                CompanyBottomNavItem.INFO -> {
                  // TODO: navigator.navigate(CompanyInfoScreenDestination)
                }
              }
            }
          },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = Color(0xFF1976D2),
            unselectedIconColor = Color(0xFF757575),
            indicatorColor = Color.Transparent
          )
        )
      }
    }
  }
}