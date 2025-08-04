// app/src/main/java/com/billcorea/jikgong/presentation/company/main/navigation/CompanyNavigation.kt
package com.billcorea.jikgong.presentation.company.main.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.billcorea.jikgong.presentation.company.main.projectlist.screen.ProjectListScreen

@Composable
fun CompanyNavigation(
  navController: NavHostController,
  startDestination: String = "company/projectlist"
) {
  NavHost(
    navController = navController,
    startDestination = startDestination
  ) {
    composable("company/projectlist") {
      ProjectListScreen(navController = navController)
    }

    composable("company/scout") {
      // TODO: Scout Screen
    }

    composable("company/money") {
      // TODO: Money Management Screen
    }

    composable("company/info") {
      // TODO: Info/MyPage Screen
    }

    composable("company/projectdetail/{projectId}") { backStackEntry ->
      val projectId = backStackEntry.arguments?.getString("projectId") ?: ""
      // TODO: Project Detail Screen
    }
  }
}