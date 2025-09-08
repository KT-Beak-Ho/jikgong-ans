package com.billcorea.jikgong.presentation.company.main.info.feature.dialog.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.billcorea.jikgong.presentation.company.main.info.data.Announcement
import com.billcorea.jikgong.presentation.company.main.info.data.AnnouncementContent
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import java.time.format.DateTimeFormatter

@Composable
fun AnnouncementDetailDialog(
    announcement: Announcement,
    onDismiss: () -> Unit,
    onMarkAsRead: (String) -> Unit = {}
) {
    // 다이얼로그가 열릴 때 읽음 처리
    if (!announcement.isRead) {
        onMarkAsRead(announcement.id)
    }
    
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.9f),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // 헤더
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        // 타입 및 우선순위 표시
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // 타입 아이콘
                            Text(
                                text = announcement.typeIcon,
                                style = AppTypography.titleMedium
                            )
                            
                            // 타입 배지
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFFF3F4F6)
                            ) {
                                Text(
                                    text = announcement.typeDisplayName,
                                    style = AppTypography.labelMedium.copy(
                                        fontWeight = FontWeight.Medium
                                    ),
                                    color = Color(0xFF374151),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
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
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // 제목
                        Text(
                            text = announcement.title,
                            style = AppTypography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color(0xFF1A1D29)
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // 작성일 및 조회수
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = announcement.createdAt.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")),
                                style = AppTypography.bodySmall,
                                color = Color(0xFF6B7280)
                            )
                            
                            Text(
                                text = "조회 ${announcement.viewCount}",
                                style = AppTypography.bodySmall,
                                color = Color(0xFF6B7280)
                            )
                            
                            // 유효기간
                            announcement.validUntil?.let { validUntil ->
                                Text(
                                    text = "~${validUntil.format(DateTimeFormatter.ofPattern("MM.dd"))}까지",
                                    style = AppTypography.bodySmall,
                                    color = Color(0xFF059669)
                                )
                            }
                        }
                    }
                    
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "닫기",
                            tint = Color(0xFF6B7280)
                        )
                    }
                }

                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color(0xFFE5E7EB)
                )

                // 본문 내용
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        Text(
                            text = announcement.content,
                            style = AppTypography.bodyMedium.copy(
                                lineHeight = AppTypography.bodyMedium.lineHeight * 1.6
                            ),
                            color = Color(0xFF374151)
                        )
                    }
                    
                    // 첨부파일이 있는 경우
                    if (announcement.attachmentUrls.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = Color(0xFFE5E7EB)
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text(
                                text = "첨부파일",
                                style = AppTypography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color(0xFF1A1D29)
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            announcement.attachmentUrls.forEachIndexed { index, url ->
                                OutlinedCard(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = CardDefaults.outlinedCardColors(
                                        containerColor = Color(0xFFFBFCFF)
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "📎",
                                            style = AppTypography.bodyMedium
                                        )
                                        
                                        Spacer(modifier = Modifier.width(8.dp))
                                        
                                        Text(
                                            text = "첨부파일_${index + 1}",
                                            style = AppTypography.bodyMedium,
                                            color = Color(0xFF4B7BFF),
                                            modifier = Modifier.weight(1f)
                                        )
                                        
                                        TextButton(
                                            onClick = { /* 다운로드 처리 */ }
                                        ) {
                                            Text(
                                                text = "다운로드",
                                                style = AppTypography.labelMedium
                                            )
                                        }
                                    }
                                }
                                
                                if (index < announcement.attachmentUrls.lastIndex) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        }
                    }
                }

                // 하단 버튼
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color(0xFFE5E7EB)
                )
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4B7BFF)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "확인",
                            style = AppTypography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AnnouncementDetailDialogPreview() {
    Jikgong1111Theme {
        AnnouncementDetailDialog(
            announcement = AnnouncementContent.announcements.first(),
            onDismiss = {}
        )
    }
}