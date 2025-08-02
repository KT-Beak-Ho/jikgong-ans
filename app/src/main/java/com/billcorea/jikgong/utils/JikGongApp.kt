package com.billcorea.jikgong.utils

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.company.main.common.components.CompanyBottomNavContainer
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.ramcosta.composedestinations.utils.toDestinationsNavigator

@Composable
fun JikGongApp() {
    Jikgong1111Theme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()
            val navigator = navController.toDestinationsNavigator()

            // 기업용 메인 화면으로 직접 이동
            CompanyBottomNavContainer(
                navigator = navigator
            )
        }
    }
}
