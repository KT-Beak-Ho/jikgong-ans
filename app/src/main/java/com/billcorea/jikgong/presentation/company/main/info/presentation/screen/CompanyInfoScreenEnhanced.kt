package com.billcorea.jikgong.presentation.company.main.info.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.HelpOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch

/**
 * 향상된 Company Info 화면
 * - 명확한 네비게이션
 * - 프로필 수정, 알림 설정, 약관 화면 이동
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyInfoScreenEnhanced(
    navController: NavController,
    onLogout: () -> Unit = {}
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "내 정보",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 프로필 섹션
            item {
                ProfileSection(
                    onEditClick = {
                        // 프로필 수정 화면으로 이동
                        navigateToProfileEdit(navController)
                    }
                )
            }
            
            // 회사 정보 카드
            item {
                CompanyInfoCard()
            }
            
            // 설정 메뉴
            item {
                SettingsSection(
                    onNotificationClick = {
                        // 알림 설정 화면으로 이동
                        navigateToNotificationSettings(navController)
                    },
                    onTermsClick = {
                        // 약관 화면으로 이동
                        navigateToTerms(navController)
                    },
                    onPrivacyClick = {
                        // 개인정보처리방침 화면으로 이동
                        navigateToPrivacyPolicy(navController)
                    },
                    onCustomerServiceClick = {
                        // 고객센터 화면으로 이동
                        navigateToCustomerService(navController)
                    },
                    onLogoutClick = {
                        showLogoutDialog = true
                    }
                )
            }
            
            // 앱 정보
            item {
                AppInfoSection()
            }
        }
    }
    
    // 로그아웃 확인 다이얼로그
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            icon = {
                Icon(
                    Icons.Default.Logout,
                    contentDescription = null,
                    tint = Color(0xFFE57373)
                )
            },
            title = { Text("로그아웃") },
            text = { Text("정말 로그아웃 하시겠습니까?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                        scope.launch {
                            snackbarHostState.showSnackbar("로그아웃되었습니다")
                        }
                    }
                ) {
                    Text("로그아웃", color = Color(0xFFE57373))
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("취소")
                }
            }
        )
    }
}

/**
 * 프로필 섹션
 */
@Composable
private fun ProfileSection(
    onEditClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "대림건설(주)",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "사업자번호: 123-45-67890",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Text(
                    text = "대표자: 홍길동",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
            
            IconButton(
                onClick = onEditClick,
                modifier = Modifier
                    .size(48.dp)
            ) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "프로필 수정",
                    tint = Color(0xFF4B7BFF)
                )
            }
        }
    }
}

/**
 * 회사 정보 카드
 */
@Composable
private fun CompanyInfoCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF0F7FF)
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = "플랫폼 수수료 절감",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color(0xFF4B7BFF)
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                InfoItem(
                    label = "이번 달 절감액",
                    value = "₩500,000",
                    color = Color(0xFF4CAF50)
                )
                InfoItem(
                    label = "누적 절감액",
                    value = "₩3,500,000",
                    color = Color(0xFF4B7BFF)
                )
            }
        }
    }
}

/**
 * 정보 아이템
 */
@Composable
private fun InfoItem(
    label: String,
    value: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = color
        )
    }
}

/**
 * 설정 섹션
 */
@Composable
private fun SettingsSection(
    onNotificationClick: () -> Unit,
    onTermsClick: () -> Unit,
    onPrivacyClick: () -> Unit,
    onCustomerServiceClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column {
            SettingsItem(
                icon = Icons.Default.Notifications,
                title = "알림 설정",
                subtitle = "푸시 알림, 이메일 알림 관리",
                onClick = onNotificationClick
            )
            
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 20.dp),
                color = Color.LightGray.copy(alpha = 0.3f)
            )
            
            SettingsItem(
                icon = Icons.Default.Description,
                title = "이용약관",
                subtitle = "서비스 이용약관 확인",
                onClick = onTermsClick
            )
            
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 20.dp),
                color = Color.LightGray.copy(alpha = 0.3f)
            )
            
            SettingsItem(
                icon = Icons.Default.Lock,
                title = "개인정보처리방침",
                subtitle = "개인정보 보호 정책",
                onClick = onPrivacyClick
            )
            
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 20.dp),
                color = Color.LightGray.copy(alpha = 0.3f)
            )
            
            SettingsItem(
                icon = Icons.Outlined.HelpOutline,
                title = "고객센터",
                subtitle = "도움말 및 문의",
                onClick = onCustomerServiceClick
            )
            
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 20.dp),
                color = Color.LightGray.copy(alpha = 0.3f)
            )
            
            SettingsItem(
                icon = Icons.Default.Logout,
                title = "로그아웃",
                subtitle = null,
                onClick = onLogoutClick,
                tint = Color(0xFFE57373)
            )
        }
    }
}

/**
 * 설정 아이템
 */
@Composable
private fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit,
    tint: Color = Color.Black
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = if (tint == Color.Black) Color(0xFF4B7BFF) else tint,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = tint
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
        
        Icon(
            Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )
    }
}

/**
 * 앱 정보 섹션
 */
@Composable
private fun AppInfoSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "직직직 사업자 앱",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        Text(
            text = "버전 1.0.0",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}

/**
 * 네비게이션 함수들
 * 실제 구현 시 각 화면으로 연결
 */
private fun navigateToProfileEdit(navController: NavController) {
    // 프로필 수정 화면으로 이동
    navController.navigate("profile_edit")
}

private fun navigateToNotificationSettings(navController: NavController) {
    // 알림 설정 화면으로 이동
    navController.navigate("notification_settings")
}

private fun navigateToTerms(navController: NavController) {
    // 이용약관 화면으로 이동
    navController.navigate("terms_and_conditions")
}

private fun navigateToPrivacyPolicy(navController: NavController) {
    // 개인정보처리방침 화면으로 이동
    navController.navigate("privacy_policy")
}

private fun navigateToCustomerService(navController: NavController) {
    // 고객센터 화면으로 이동
    navController.navigate("customer_service")
}