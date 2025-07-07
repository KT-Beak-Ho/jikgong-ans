package com.billcorea.jikgong.presentation.company.auth.main.info

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyInfoScreen(
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 프로필 헤더
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = appColorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 프로필 이미지 (플레이스홀더)
                    Surface(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape),
                        color = appColorScheme.primary
                    ) {
                        Icon(
                            imageVector = Icons.Default.Business,
                            contentDescription = "기업 프로필",
                            tint = Color.White,
                            modifier = Modifier.padding(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "(주)대한건설",
                        style = AppTypography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = appColorScheme.onPrimaryContainer
                    )

                    Text(
                        text = "사업자등록번호: 123-45-67890",
                        style = AppTypography.bodyMedium,
                        color = appColorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }
            }
        }

        // 메뉴 아이템들
        item {
            InfoMenuItem(
                icon = Icons.Default.People,
                title = "저장한 인부",
                subtitle = "15명",
                onClick = { /* 저장한 인부 화면으로 이동 */ }
            )
        }

        item {
            InfoMenuItem(
                icon = Icons.Default.Notifications,
                title = "알림 설정",
                subtitle = "푸시 알림, 이메일 알림 설정",
                onClick = { /* 알림 설정 화면으로 이동 */ }
            )
        }

        item {
            InfoMenuItem(
                icon = Icons.Default.Event,
                title = "이벤트",
                subtitle = "진행 중인 이벤트 확인",
                onClick = { /* 이벤트 화면으로 이동 */ }
            )
        }

        item {
            InfoMenuItem(
                icon = Icons.Default.Announcement,
                title = "공지사항",
                subtitle = "새로운 소식과 업데이트",
                onClick = { /* 공지사항 화면으로 이동 */ }
            )
        }

        item {
            InfoMenuItem(
                icon = Icons.Default.Help,
                title = "고객센터",
                subtitle = "문의하기, FAQ",
                onClick = { /* 고객센터 화면으로 이동 */ }
            )
        }

        item {
            InfoMenuItem(
                icon = Icons.Default.Policy,
                title = "약관 및 정책",
                subtitle = "서비스 이용약관, 개인정보처리방침",
                onClick = { /* 약관 화면으로 이동 */ }
            )
        }

        // 로그아웃 버튼
        item {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = { /* 로그아웃 처리 */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFFD32F2F)
                ),
                border = BorderStroke(
                    width = 1.dp,
                    color = Color(0xFFD32F2F)
                )
            ) {
                Text(
                    text = "로그아웃",
                    style = AppTypography.titleMedium.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}

@Composable
fun InfoMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = appColorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = appColorScheme.primary,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = AppTypography.titleMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = appColorScheme.onSurface
                )

                Text(
                    text = subtitle,
                    style = AppTypography.bodySmall,
                    color = appColorScheme.onSurfaceVariant
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "이동",
                tint = appColorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CompanyInfoScreenPreview() {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme {
        CompanyInfoScreen(navigator = navigator)
    }
}