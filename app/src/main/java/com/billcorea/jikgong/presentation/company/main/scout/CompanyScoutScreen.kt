package com.billcorea.jikgong.presentation.company.main.scout

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.company.main.scout.pages.WorkerListPage
import com.billcorea.jikgong.presentation.company.main.scout.pages.ProposalListPage
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyScoutScreen(
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("인력 목록", "제안 목록")

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // 상단바
        TopAppBar(
            title = {
                Text(
                    text = "인력 스카웃",
                    style = AppTypography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = appColorScheme.surface,
                titleContentColor = appColorScheme.onSurface
            )
        )

        // 탭바
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier.fillMaxWidth(),
            containerColor = appColorScheme.surface,
            contentColor = appColorScheme.primary
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                            text = title,
                            style = AppTypography.titleMedium.copy(
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal
                            )
                        )
                    }
                )
            }
        }

        // 탭 콘텐츠
        when (selectedTabIndex) {
            0 -> WorkerListPage(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            )
            1 -> ProposalListPage(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CompanyScoutScreenPreview() {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme {
        CompanyScoutScreen(navigator = navigator)
    }
}