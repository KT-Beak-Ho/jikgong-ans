package com.billcorea.jikgong.presentation.company.main.money

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.company.main.money.pages.PaymentListPage
import com.billcorea.jikgong.presentation.company.main.money.pages.PaymentCalendarPage
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyMoneyScreen(
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier
) {
    var selectedTabIndex by remember { mutableStateOf(0) }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // 상단바
        TopAppBar(
            title = {
                Text(
                    text = "임금 관리",
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

        // 긴급 알림 카드
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFEBEE) // 연한 빨간색 배경
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = "긴급",
                    tint = Color(0xFFD32F2F),
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "48시간 이내 지급 필요",
                        style = AppTypography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFD32F2F)
                        )
                    )
                    Text(
                        text = "3건의 임금 지급이 대기 중입니다",
                        style = AppTypography.bodySmall.copy(
                            color = Color(0xFF757575)
                        )
                    )
                }

                TextButton(
                    onClick = { /* 긴급 지급 처리 */ }
                ) {
                    Text(
                        text = "처리하기",
                        color = Color(0xFFD32F2F),
                        style = AppTypography.labelMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        // 하단 탭바
        NavigationBar(
            containerColor = appColorScheme.surface
        ) {
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.List,
                        contentDescription = "목록",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = "지급 목록",
                        style = AppTypography.labelSmall
                    )
                },
                selected = selectedTabIndex == 0,
                onClick = { selectedTabIndex = 0 }
            )

            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "캘린더",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = "캘린더",
                        style = AppTypography.labelSmall
                    )
                },
                selected = selectedTabIndex == 1,
                onClick = { selectedTabIndex = 1 }
            )
        }

        // 탭 콘텐츠
        when (selectedTabIndex) {
            0 -> PaymentListPage(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            )
            1 -> PaymentCalendarPage(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CompanyMoneyScreenPreview() {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme {
        CompanyMoneyScreen(navigator = navigator)
    }
}