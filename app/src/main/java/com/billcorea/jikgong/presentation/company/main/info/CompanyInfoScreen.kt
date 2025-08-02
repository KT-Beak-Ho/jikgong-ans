package com.billcorea.jikgong.presentation.company.main.info

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyInfoScreen(
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier,
    showBottomBar: Boolean = true
) {
    // 샘플 데이터
    val companyName = "한국건설(주)"
    val businessNumber = "123-45-67890"
    val savedWorkersCount = 24

    // 메뉴 아이템들
    val menuItems = listOf(
        InfoMenuItem(
            title = "프로젝트 관리",
            description = "등록된 프로젝트 현황 및 관리",
            icon = Icons.Default.Work,
            onClick = { /* 프로젝트 관리 화면으로 이동 */ }
        ),
        InfoMenuItem(
            title = "인력 현황",
            description = "모집한 인력 및 진행 현황",
            icon = Icons.Default.People,
            onClick = { /* 인력 현황 화면으로 이동 */ }
        ),
        InfoMenuItem(
            title = "결제 내역",
            description = "임금 지급 및 결제 기록",
            icon = Icons.Default.Payment,
            onClick = { /* 결제 내역 화면으로 이동 */ }
        ),
        InfoMenuItem(
            title = "설정",
            description = "알림 설정 및 기타 옵션",
            icon = Icons.Default.Settings,
            onClick = { /* 설정 화면으로 이동 */ }
        ),
        InfoMenuItem(
            title = "고객 지원",
            description = "문의사항 및 도움말",
            icon = Icons.AutoMirrored.Filled.Help, // 수정됨
            onClick = { /* 고객 지원 화면으로 이동 */ }
        ),
        InfoMenuItem(
            title = "채팅 상담",
            description = "실시간 고객 상담 서비스",
            icon = Icons.AutoMirrored.Filled.Chat, // 수정됨
            onClick = { /* 채팅 상담 화면으로 이동 */ }
        )
    )

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 16.dp,
                bottom = if (showBottomBar) 100.dp else 40.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 상단바
            item {
                TopAppBar(
                    title = {
                        Text(
                            text = "기업 정보",
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
            }

            // 회사 정보 카드
            item {
                CompanyInfoCard(
                    companyName = companyName,
                    businessNumber = businessNumber,
                    savedWorkersCount = savedWorkersCount
                )
            }

            // 메뉴 아이템들
            items(menuItems) { menuItem ->
                InfoMenuCard(menuItem = menuItem)
            }

            // 로그아웃 버튼
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = appColorScheme.errorContainer.copy(alpha = 0.3f)
                    ),
                    onClick = { /* 로그아웃 처리 */ }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Logout, // 수정됨
                            contentDescription = "로그아웃",
                            tint = appColorScheme.error,
                            modifier = Modifier.size(24.dp)
                        )

                        Text(
                            text = "로그아웃",
                            style = AppTypography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = appColorScheme.error
                        )
                    }
                }
            }
        }
    }
}

