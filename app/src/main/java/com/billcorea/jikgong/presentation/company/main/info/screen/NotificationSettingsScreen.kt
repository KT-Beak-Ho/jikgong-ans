package com.billcorea.jikgong.presentation.company.main.info.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.company.main.common.BackNavigationTopBar
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.ramcosta.composedestinations.annotation.Destination

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun NotificationSettingsScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var pushEnabled by remember { mutableStateOf(true) }
    var scoutEnabled by remember { mutableStateOf(true) }
    var paymentEnabled by remember { mutableStateOf(true) }
    var announcementEnabled by remember { mutableStateOf(false) }
    var marketingEnabled by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            BackNavigationTopBar(
                title = "알림 설정",
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "푸시 알림",
                            style = AppTypography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        NotificationToggleItem(
                            title = "전체 푸시 알림",
                            description = "앱의 모든 알림을 받습니다",
                            isEnabled = pushEnabled,
                            onToggle = { pushEnabled = it }
                        )
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "상세 알림 설정",
                            style = AppTypography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        NotificationToggleItem(
                            title = "스카웃 알림",
                            description = "새로운 스카웃 요청이 있을 때 알림을 받습니다",
                            isEnabled = scoutEnabled && pushEnabled,
                            onToggle = { scoutEnabled = it },
                            enabled = pushEnabled
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 16.dp),
                            thickness = 1.dp,
                            color = Color.Gray.copy(alpha = 0.2f)
                        )

                        NotificationToggleItem(
                            title = "급여 지급 알림",
                            description = "급여가 지급되었을 때 알림을 받습니다",
                            isEnabled = paymentEnabled && pushEnabled,
                            onToggle = { paymentEnabled = it },
                            enabled = pushEnabled
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 16.dp),
                            thickness = 1.dp,
                            color = Color.Gray.copy(alpha = 0.2f)
                        )

                        NotificationToggleItem(
                            title = "공지사항 알림",
                            description = "새로운 공지사항이 있을 때 알림을 받습니다",
                            isEnabled = announcementEnabled && pushEnabled,
                            onToggle = { announcementEnabled = it },
                            enabled = pushEnabled
                        )

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 16.dp),
                            thickness = 1.dp,
                            color = Color.Gray.copy(alpha = 0.2f)
                        )

                        NotificationToggleItem(
                            title = "마케팅 알림",
                            description = "이벤트 및 혜택 정보를 받습니다",
                            isEnabled = marketingEnabled && pushEnabled,
                            onToggle = { marketingEnabled = it },
                            enabled = pushEnabled
                        )
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF8F9FA)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "📱 알림 설정 안내",
                            style = AppTypography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        Text(
                            text = "• 알림을 받으려면 기기 설정에서 푸시 알림을 허용해주세요\n• 중요한 업무 관련 알림은 꺼지지 않습니다\n• 야간 시간(22:00~08:00)에는 긴급 알림만 발송됩니다",
                            style = AppTypography.bodySmall,
                            color = Color.Gray,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationToggleItem(
    title: String,
    description: String,
    isEnabled: Boolean,
    onToggle: (Boolean) -> Unit,
    enabled: Boolean = true
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = AppTypography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = if (enabled) Color.Black else Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = AppTypography.bodySmall,
                color = Color.Gray,
                lineHeight = 16.sp
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Switch(
            checked = isEnabled,
            onCheckedChange = onToggle,
            enabled = enabled,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = appColorScheme.primary,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color.Gray.copy(alpha = 0.5f)
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationSettingsScreenPreview() {
    Jikgong1111Theme {
        NotificationSettingsScreen(
            navController = rememberNavController()
        )
    }
}