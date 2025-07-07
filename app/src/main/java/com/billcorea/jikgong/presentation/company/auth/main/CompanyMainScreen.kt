package com.billcorea.jikgong.presentation.company.auth.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.company.auth.main.common.components.CompanyBottomNavigation
import com.billcorea.jikgong.presentation.company.auth.main.common.components.CompanyBottomNavTabs
import com.billcorea.jikgong.presentation.company.auth.main.projectlist.CompanyProjectListScreen
import com.billcorea.jikgong.presentation.company.auth.main.scout.CompanyScoutScreen
import com.billcorea.jikgong.presentation.company.auth.main.money.CompanyMoneyScreen
import com.billcorea.jikgong.presentation.company.auth.main.info.CompanyInfoScreen
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator

@Destination
@Composable
fun CompanyMainScreen(
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier
) {
    var currentRoute by remember { mutableStateOf(CompanyBottomNavTabs.PROJECT_LIST.route) }

    Scaffold(
        bottomBar = {
            CompanyBottomNavigation(
                currentRoute = currentRoute,
                onTabSelected = { route ->
                    currentRoute = route
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentRoute) {
                CompanyBottomNavTabs.PROJECT_LIST.route -> {
                    CompanyProjectListScreen(navigator = navigator)
                }
                CompanyBottomNavTabs.SCOUT.route -> {
                    CompanyScoutScreen(navigator = navigator)
                }
                CompanyBottomNavTabs.MONEY.route -> {
                    CompanyMoneyScreen(navigator = navigator)
                }
                CompanyBottomNavTabs.INFO.route -> {
                    CompanyInfoScreen(navigator = navigator)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CompanyMainScreenPreview() {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme {
        CompanyMainScreen(navigator = navigator)
    }
}