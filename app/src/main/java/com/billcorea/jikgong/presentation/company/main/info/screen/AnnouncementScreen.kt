package com.billcorea.jikgong.presentation.company.main.info.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class Announcement(
    val id: String,
    val title: String,
    val content: String,
    val isImportant: Boolean,
    val createdAt: LocalDateTime,
    val isRead: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun AnnouncementScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val announcements = remember {
        listOf(
            Announcement(
                id = "1",
                title = "[중요] 직공 앱 업데이트 안내",
                content = "안녕하세요, 직공입니다.\n\n더 나은 서비스 제공을 위해 앱 업데이트를 진행합니다.\n\n주요 업데이트 내용:\n• 스카웃 기능 개선\n• 급여 관리 시스템 업데이트\n• 사용자 인터페이스 개선\n• 보안 강화\n\n업데이트 일시: 2025년 8월 30일 오전 2시~6시\n업데이트 기간 중 서비스 이용에 제한이 있을 수 있습니다.\n\n문의사항이 있으시면 고객센터로 연락 부탁드립니다.\n\n감사합니다.",
                isImportant = true,
                createdAt = LocalDateTime.now().minusDays(1)
            ),
            Announcement(
                id = "2",
                title = "8월 급여 지급 일정 안내",
                content = "8월 급여 지급 일정을 안내드립니다.\n\n지급 일정: 2025년 8월 31일 오후 3시\n지급 방법: 등록된 계좌로 자동 입금\n\n급여 명세서는 앱 내에서 확인하실 수 있습니다.\n\n문의사항이 있으시면 고객센터로 연락 부탁드립니다.",
                isImportant = false,
                createdAt = LocalDateTime.now().minusDays(3)
            ),
            Announcement(
                id = "3",
                title = "추석 연휴 운영 안내",
                content = "추석 연휴 기간 중 고객센터 운영 시간을 안내드립니다.\n\n휴무 기간: 9월 16일(토) ~ 9월 18일(월)\n정상 운영: 9월 19일(화)부터\n\n연휴 기간 중 긴급 문의는 1:1 문의하기를 이용해 주세요.\n\n즐거운 추석 명절 보내세요!",
                isImportant = false,
                createdAt = LocalDateTime.now().minusDays(5)
            ),
            Announcement(
                id = "4",
                title = "[이벤트] 스카웃 성공 보너스 이벤트",
                content = "스카웃 성공 보너스 이벤트를 진행합니다!\n\n이벤트 기간: 8월 1일 ~ 8월 31일\n혜택: 스카웃 성공 시 추가 포인트 지급\n\n• 첫 스카웃 성공: 1,000포인트\n• 5회 성공: 추가 5,000포인트\n• 10회 성공: 추가 10,000포인트\n\n포인트는 다음 달 급여와 함께 지급됩니다.\n\n많은 참여 부탁드립니다!",
                isImportant = false,
                createdAt = LocalDateTime.now().minusDays(7),
                isRead = true
            ),
            Announcement(
                id = "5",
                title = "개인정보처리방침 개정 안내",
                content = "개인정보처리방침이 개정됨을 안내드립니다.\n\n시행일: 2025년 8월 15일\n\n주요 개정 내용:\n• 개인정보 수집 항목 명시\n• 개인정보 보관 기간 단축\n• 마케팅 정보 수신 동의 분리\n\n자세한 내용은 앱 설정 > 약관 및 정책에서 확인하실 수 있습니다.",
                isImportant = false,
                createdAt = LocalDateTime.now().minusDays(14),
                isRead = true
            )
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            BackNavigationTopBar(
                title = "공지사항",
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF8F9FA)),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(1.dp)
        ) {
            items(announcements) { announcement ->
                AnnouncementItem(
                    announcement = announcement,
                    onClick = {
                        // TODO: Navigate to announcement detail
                    }
                )
            }
        }
    }
}

@Composable
private fun AnnouncementItem(
    announcement: Announcement,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (announcement.isImportant) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFFF5252)
                        ) {
                            Text(
                                text = "중요",
                                color = Color.White,
                                style = AppTypography.bodySmall,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    
                    if (!announcement.isRead) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(
                                    color = appColorScheme.primary,
                                    shape = RoundedCornerShape(3.dp)
                                )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    
                    Text(
                        text = announcement.title,
                        style = AppTypography.bodyLarge,
                        fontWeight = if (!announcement.isRead) FontWeight.Bold else FontWeight.Normal,
                        color = if (!announcement.isRead) Color.Black else Color.Gray,
                        maxLines = 1
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = announcement.content.lines().firstOrNull()?.take(50) + 
                          if (announcement.content.length > 50) "..." else "",
                    style = AppTypography.bodySmall,
                    color = Color.Gray,
                    maxLines = 1
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = announcement.createdAt.format(
                        DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")
                    ),
                    style = AppTypography.bodySmall,
                    color = Color.Gray
                )
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnnouncementScreenPreview() {
    Jikgong1111Theme {
        AnnouncementScreen(
            navController = rememberNavController()
        )
    }
}