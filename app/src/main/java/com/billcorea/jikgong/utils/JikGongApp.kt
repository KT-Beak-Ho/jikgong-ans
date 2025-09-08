package com.billcorea.jikgong.utils

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.company.main.projectlist.presentation.screen.ProjectListScreen
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme

@Composable
fun JikGongApp() {
    Jikgong1111Theme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()

            // ProjectListScreen 직접 호출
            ProjectListScreen(navController = navController)
        }
    }
}