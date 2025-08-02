// app/src/main/java/com/billcorea/jikgong/presentation/company/main/info/CompanyInfoScreen.kt
package com.billcorea.jikgong.presentation.company.main.info

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.company.main.info.shared.CompanyInfoSharedViewModel
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Composable
fun CompanyInfoScreen(
    navigator: DestinationsNavigator,
    viewModel: CompanyInfoSharedViewModel = koinViewModel(),
    modifier: Modifier = Modifier,
    showBottomBar: Boolean = true
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(appColorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 프로필 헤더
        item {
            ProfileHeader(
                companyName = "대한건설 주식회사",
                businessNumber = "123-45-67890",
                savedWorkersCount = 15
            )
        }

        // 주요 기능 섹션
        item {
            MainFeaturesSection(
                onSavedWorkersClick = { /* 저장한 인부 화면으로 이동 */ },
                onProjectHistoryClick = { /* 프로젝트 히스토리 화면으로 이동 */ },
                onPaymentHistoryClick = { /* 지급 히스토리 화면으로 이동 */ }
            )
        }

        // 설정 섹션
        item {
            SectionHeader(title = "설정")
        }

        items(getSettingsMenuItems()) { menuItem ->
            MenuItemCard(
                menuItem = menuItem,
                onClick = menuItem.onClick
            )
        }

        // 지원 섹션
        item {
            SectionHeader(title = "지원")
        }

        items(getSupportMenuItems()) { menuItem ->
            MenuItemCard(
                menuItem = menuItem,
                onClick = menuItem.onClick
            )
        }

        // 정보 섹션
        item {
            SectionHeader(title = "정보")
        }

        items(getInfoMenuItems()) { menuItem ->
            MenuItemCard(
                menuItem = menuItem,
                onClick = menuItem.onClick
            )
        }

        // 로그아웃 버튼
        item {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = { /* 로그아웃 로직 */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFFF44336)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = "로그아웃",
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "로그아웃",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // 하단 여백
        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun ProfileHeader(
    companyName: String,
    businessNumber: String,
    savedWorkersCount: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = appColorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 프로필 이미지 (임시)
                Surface(
                    modifier = Modifier.size(80.dp),
                    shape = CircleShape,
                    color = appColorScheme.primaryContainer
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Business,
                            contentDescription = "회사 로고",
                            tint = appColorScheme.primary,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(20.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = companyName,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = appColorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "사업자번호: $businessNumber",
                        style = MaterialTheme.typography.bodyMedium,
                        color = appColorScheme.onSurface.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = appColorScheme.primaryContainer.copy(alpha = 0.3f)
                    ) {
                        Text(
                            text = "저장한 인부 ${savedWorkersCount}명",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = appColorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                IconButton(
                    onClick = { /* 프로필 편집 */ }
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "프로필 편집",
                        tint = appColorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

@Composable
private fun MainFeaturesSection(
    onSavedWorkersClick: () -> Unit,
    onProjectHistoryClick: () -> Unit,
    onPaymentHistoryClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = appColorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "주요 기능",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = appColorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FeatureButton(
                    icon = Icons.Default.BookmarkAdded,
                    title = "저장한 인부",
                    subtitle = "15명",
                    onClick = onSavedWorkersClick,
                    modifier = Modifier.weight(1f)
                )

                FeatureButton(
                    icon = Icons.Default.History,
                    title = "프로젝트",
                    subtitle = "8개 완료",
                    onClick = onProjectHistoryClick,
                    modifier = Modifier.weight(1f)
                )

                FeatureButton(
                    icon = Icons.Default.Payment,
                    title = "지급 내역",
                    subtitle = "이번 달",
                    onClick = onPaymentHistoryClick,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun FeatureButton(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable { onClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(48.dp),
            shape = CircleShape,
            color = appColorScheme.primaryContainer.copy(alpha = 0.3f)
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = appColorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = appColorScheme.onSurface
        )

        Text(
            text = subtitle,
            style = MaterialTheme.typography.labelSmall,
            color = appColorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = appColorScheme.onSurface,
        modifier = Modifier.padding(horizontal = 4.dp)
    )
}

@Composable
private fun MenuItemCard(
    menuItem: InfoMenuItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = appColorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = appColorScheme.primaryContainer.copy(alpha = 0.2f)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = menuItem.icon,
                        contentDescription = menuItem.title,
                        tint = appColorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = menuItem.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = appColorScheme.onSurface
                )

                if (menuItem.subtitle.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = menuItem.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = appColorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            // 배지 (있는 경우)
            menuItem.badge?.let { badge ->
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFF44336)
                ) {
                    Text(
                        text = badge,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "이동",
                tint = appColorScheme.onSurface.copy(alpha = 0.4f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// 메뉴 아이템 데이터 클래스
data class InfoMenuItem(
    val icon: ImageVector,
    val title: String,
    val subtitle: String = "",
    val badge: String? = null,
    val onClick: () -> Unit = {}
)

// 설정 메뉴 아이템들
private fun getSettingsMenuItems(): List<InfoMenuItem> {
    return listOf(
        InfoMenuItem(
            icon = Icons.Default.Notifications,
            title = "알림 설정",
            subtitle = "푸시 알림, 이메일 알림"
        ),
        InfoMenuItem(
            icon = Icons.Default.Language,
            title = "언어 설정",
            subtitle = "한국어"
        ),
        InfoMenuItem(
            icon = Icons.Default.Security,
            title = "보안 설정",
            subtitle = "비밀번호, 생체인증"
        ),
        InfoMenuItem(
            icon = Icons.Default.AccountBalance,
            title = "계좌 관리",
            subtitle = "지급 계좌 설정"
        )
    )
}

// 지원 메뉴 아이템들
private fun getSupportMenuItems(): List<InfoMenuItem> {
    return listOf(
        InfoMenuItem(
            icon = Icons.Default.Campaign,
            title = "공지사항",
            subtitle = "새로운 소식과 업데이트",
            badge = "2"
        ),
        InfoMenuItem(
            icon = Icons.Default.Event,
            title = "이벤트",
            subtitle = "진행 중인 이벤트 확인",
            badge = "1"
        ),
        InfoMenuItem(
            icon = Icons.Default.Help,
            title = "고객센터",
            subtitle = "문의하기, 자주 묻는 질문"
        ),
        InfoMenuItem(
            icon = Icons.Default.Chat,
            title = "1:1 문의",
            subtitle = "실시간 채팅 상담"
        )
    )
}

// 정보 메뉴 아이템들
private fun getInfoMenuItems(): List<InfoMenuItem> {
    return listOf(
        InfoMenuItem(
            icon = Icons.Default.Description,
            title = "이용약관",
            subtitle = "서비스 이용약관"
        ),
        InfoMenuItem(
            icon = Icons.Default.PrivacyTip,
            title = "개인정보처리방침",
            subtitle = "개인정보 보호 정책"
        ),
        InfoMenuItem(
            icon = Icons.Default.Info,
            title = "앱 정보",
            subtitle = "버전 1.0.0"
        ),
        InfoMenuItem(
            icon = Icons.Default.Star,
            title = "앱 평가",
            subtitle = "플레이스토어에서 평가하기"
        )
    )
}

// Preview 컴포저블들
@Preview(name = "기본 화면", showBackground = true, heightDp = 800)
@Composable
fun CompanyInfoScreenPreview() {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme {
        CompanyInfoScreen(
            navigator = navigator,
            showBottomBar = false
        )
    }
}

@Preview(name = "프로필 헤더", showBackground = true)
@Composable
fun ProfileHeaderPreview() {
    Jikgong1111Theme {
        ProfileHeader(
            companyName = "대한건설 주식회사",
            businessNumber = "123-45-67890",
            savedWorkersCount = 15
        )
    }
}

@Preview(name = "주요 기능 섹션", showBackground = true)
@Composable
fun MainFeaturesSectionPreview() {
    Jikgong1111Theme {
        MainFeaturesSection(
            onSavedWorkersClick = {},
            onProjectHistoryClick = {},
            onPaymentHistoryClick = {}
        )
    }
}

@Preview(name = "메뉴 아이템", showBackground = true)
@Composable
fun MenuItemCardPreview() {
    Jikgong1111Theme {
        MenuItemCard(
            menuItem = InfoMenuItem(
                icon = Icons.Default.Notifications,
                title = "알림 설정",
                subtitle = "푸시 알림, 이메일 알림",
                badge = "2"
            ),
            onClick = {}
        )
    }
}

@Preview(name = "다크 테마", showBackground = true, heightDp = 800)
@Composable
fun CompanyInfoScreenDarkPreview() {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme(darkTheme = true) {
        CompanyInfoScreen(
            navigator = navigator,
            showBottomBar = false
        )
    }
}