package com.billcorea.jikgong.presentation.company.main.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.company.main.common.CompanyBottomBar
import com.billcorea.jikgong.presentation.company.main.info.CompanyInfoScreen
//import com.billcorea.jikgong.presentation.company.main.money.MoneyManagementScreen
import com.billcorea.jikgong.presentation.company.main.projectlist.screen.ProjectListScreen
//import com.billcorea.jikgong.presentation.company.main.scout.ScoutScreen

@Composable
fun CompanyMainScreen(
  navController: NavHostController = rememberNavController()
) {
  Scaffold(
    bottomBar = {
      CompanyBottomBar(navController = navController)
    }
  ) { paddingValues ->
    NavHost(
      navController = navController,
      startDestination = "company/projectlist",
      modifier = Modifier.padding(paddingValues)
    ) {
      composable("company/projectlist") {
        ProjectListScreen(navController = navController)
      }


      composable("company/projectdetail/{projectId}") { backStackEntry ->
        val projectId = backStackEntry.arguments?.getString("projectId") ?: ""
        // TODO: ProjectDetailScreen(projectId = projectId, navController = navController)
      }
    }
  }
}