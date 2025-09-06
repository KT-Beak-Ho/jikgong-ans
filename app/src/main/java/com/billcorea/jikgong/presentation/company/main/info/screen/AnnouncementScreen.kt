package com.billcorea.jikgong.presentation.company.main.info.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.company.main.info.data.Announcement
import com.billcorea.jikgong.presentation.company.main.info.data.AnnouncementContent
import com.billcorea.jikgong.presentation.company.main.info.data.AnnouncementType
import com.billcorea.jikgong.presentation.company.main.info.data.NewAnnouncementContent
import com.billcorea.jikgong.presentation.company.main.info.popup.AnnouncementDetailDialog
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun AnnouncementScreen(
    navigator: DestinationsNavigator,
    navController: NavController
) {
    var selectedAnnouncement by remember { mutableStateOf<Announcement?>(null) }
    var selectedType by remember { mutableStateOf<AnnouncementType?>(null) }
    
    val announcements = remember {
        if (selectedType == null) {
            NewAnnouncementContent.announcements
        } else {
            NewAnnouncementContent.getAnnouncementsByType(selectedType!!)
        }
    }
    
    val pinnedAnnouncements = announcements.filter { it.isPinned }
    val regularAnnouncements = announcements.filter { !it.isPinned }
    
    // 선택된 공지사항이 있을 때 상세 다이얼로그 표시
    selectedAnnouncement?.let { announcement ->
        AnnouncementDetailDialog(
            announcement = announcement,
            onDismiss = { selectedAnnouncement = null },
            onMarkAsRead = { /* 읽음 처리 로직 */ }
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "공지사항",
                        style = AppTypography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFF1A1D29)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navigator.navigateUp() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "뒤로가기",
                            tint = Color(0xFF1A1D29)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 필터 칩
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedType == null,
                        onClick = { selectedType = null },
                        label = {
                            Text(
                                text = "전체",
                                style = AppTypography.labelMedium
                            )
                        }
                    )
                    
                    AnnouncementType.entries.forEach { type ->
                        FilterChip(
                            selected = selectedType == type,
                            onClick = { selectedType = type },
                            label = {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = when(type) {
                                            AnnouncementType.CONSTRUCTION_NEWS -> "🏢"
                                            AnnouncementType.LAW_REGULATION -> "⚖️"
                                            AnnouncementType.SERVICE_UPDATE -> "🚀"
                                            AnnouncementType.SAFETY_INFO -> "⚠️"
                                            AnnouncementType.INDUSTRY_TREND -> "📈"
                                            AnnouncementType.POLICY_CHANGE -> "📄"
                                        }
                                    )
                                    Text(
                                        text = when(type) {
                                            AnnouncementType.CONSTRUCTION_NEWS -> "건설뉴스"
                                            AnnouncementType.LAW_REGULATION -> "법령규정"
                                            AnnouncementType.SERVICE_UPDATE -> "서비스"
                                            AnnouncementType.SAFETY_INFO -> "안전정보"
                                            AnnouncementType.INDUSTRY_TREND -> "업계동향"
                                            AnnouncementType.POLICY_CHANGE -> "정책변경"
                                        },
                                        style = AppTypography.labelMedium
                                    )
                                }
                            }
                        )
                    }
                }
            }
            
            // 고정된 공지사항
            if (pinnedAnnouncements.isNotEmpty()) {
                item {
                    Text(
                        text = "📌 중요 공지",
                        style = AppTypography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFF1A1D29)
                    )
                }
                
                items(
                    items = pinnedAnnouncements,
                    key = { it.id }
                ) { announcement ->
                    AnnouncementItem(
                        announcement = announcement,
                        isPinned = true,
                        onClick = { selectedAnnouncement = announcement }
                    )
                }
                
                item {
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = Color(0xFFE5E7EB)
                    )
                }
            }
            
            // 일반 공지사항
            if (regularAnnouncements.isNotEmpty()) {
                item {
                    Text(
                        text = "📢 일반 공지",
                        style = AppTypography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFF1A1D29)
                    )
                }
                
                items(
                    items = regularAnnouncements,
                    key = { it.id }
                ) { announcement ->
                    AnnouncementItem(
                        announcement = announcement,
                        isPinned = false,
                        onClick = { selectedAnnouncement = announcement }
                    )
                }
            }
            
            // 하단 여백
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun AnnouncementItem(
    announcement: Announcement,
    isPinned: Boolean = false,
    onClick: () -> Unit
) {
    OutlinedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = if (isPinned) Color(0xFFFFF7ED) else Color.White
        ),
        border = BorderStroke(
            width = if (isPinned) 2.dp else 1.dp,
            color = if (isPinned) Color(0xFFF59E0B) else Color(0xFFE5E7EB)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isPinned) 3.dp else 1.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 헤더 (타입, 우선순위, 고정 표시)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 타입 아이콘
                    Text(
                        text = announcement.typeIcon,
                        style = AppTypography.bodyMedium
                    )
                    
                    // 타입 배지
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFFF3F4F6)
                    ) {
                        Text(
                            text = announcement.typeDisplayName,
                            style = AppTypography.labelSmall.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = Color(0xFF374151),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    
                    // 우선순위 배지
                    if (announcement.priority.ordinal >= 1) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = announcement.priorityColor.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = announcement.priorityDisplayName,
                                style = AppTypography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = announcement.priorityColor,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                
                // 고정 아이콘
                if (isPinned) {
                    Icon(
                        imageVector = Icons.Default.PushPin,
                        contentDescription = "고정됨",
                        tint = Color(0xFFF59E0B),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 제목
            Text(
                text = announcement.title,
                style = AppTypography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color(0xFF1A1D29),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(6.dp))
            
            // 요약
            Text(
                text = announcement.summary,
                style = AppTypography.bodyMedium,
                color = Color(0xFF6B7280),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 하단 정보 (날짜, 조회수, 읽음 상태)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = announcement.createdAt.format(DateTimeFormatter.ofPattern("MM.dd")),
                        style = AppTypography.bodySmall,
                        color = Color(0xFF9CA3AF)
                    )
                    
                    Text(
                        text = "조회 ${announcement.viewCount}",
                        style = AppTypography.bodySmall,
                        color = Color(0xFF9CA3AF)
                    )
                }
                
                // 읽지 않은 공지사항 표시
                if (!announcement.isRead) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFFEF4444)
                    ) {
                        Text(
                            text = "NEW",
                            style = AppTypography.labelSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnnouncementScreenPreview() {
    Jikgong1111Theme {
        val navController = rememberNavController()
        val navigator = navController.toDestinationsNavigator()
        
        AnnouncementScreen(
            navigator = navigator,
            navController = navController
        )
    }
}