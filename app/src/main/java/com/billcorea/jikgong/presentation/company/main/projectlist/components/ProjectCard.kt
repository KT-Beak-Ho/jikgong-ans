// File: ProjectCard.kt
package com.billcorea.jikgong.presentation.company.main.projectlist.components

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.billcorea.jikgong.presentation.company.main.projectlist.data.*
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectCard(
    project: Project,
    onCardClick: () -> Unit,
    onBookmarkClick: () -> Unit,
    onApplyClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        onClick = onCardClick,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 8.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // 헤더 (제목, 카테고리, 북마크)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    // 프로젝트 제목
                    Text(
                        text = project.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFF1A1A1A),
                        fontSize = 16.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // 카테고리
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = project.category.icon,
                            fontSize = 12.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = project.category.displayName,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF666666),
                            fontSize = 12.sp
                        )
                    }
                }

                // 북마크 버튼
                IconButton(
                    onClick = onBookmarkClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    val bookmarkColor = if (project.isBookmarked) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color(0xFF999999)
                    }

                    Icon(
                        imageVector = if (project.isBookmarked) {
                            Icons.Default.Bookmark
                        } else {
                            Icons.Default.BookmarkBorder
                        },
                        contentDescription = "북마크",
                        tint = bookmarkColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 상태 배지
            ProjectStatusBadge(
                status = project.status,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // 위치 및 회사 정보
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "위치",
                    tint = Color(0xFF666666),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = project.location,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF666666),
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.width(12.dp))

                Icon(
                    imageVector = Icons.Default.Business,
                    contentDescription = "회사",
                    tint = Color(0xFF666666),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = project.companyName,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF666666),
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 일급 및 모집인원 정보
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "일급",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF666666),
                        fontSize = 12.sp
                    )
                    Text(
                        text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(project.dailyWage)}원",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 18.sp
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "모집인원",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF666666),
                        fontSize = 12.sp
                    )
                    Text(
                        text = "${project.appliedWorkers}/${project.requiredWorkers}명",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = Color(0xFF333333),
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 기간 정보
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = "기간",
                    tint = Color(0xFF666666),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${project.startDate.format(DateTimeFormatter.ofPattern("MM/dd"))} ~ ${project.endDate.format(DateTimeFormatter.ofPattern("MM/dd"))}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF666666),
                    fontSize = 13.sp
                )
            }

            // 상세 정보 (확장 가능)
            AnimatedVisibility(
                visible = isExpanded,
                enter = slideInVertically() + fadeIn(),
                exit = slideOutVertically() + fadeOut()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))

                    // 프로젝트 설명
                    Text(
                        text = project.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF666666),
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )

                    if (project.requirements.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "자격 요건",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = Color(0xFF333333),
                            fontSize = 13.sp
                        )

                        project.requirements.forEach { requirement ->
                            Text(
                                text = "• $requirement",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF666666),
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                            )
                        }
                    }

                    if (project.benefits.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "복리혜택",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = Color(0xFF333333),
                            fontSize = 13.sp
                        )

                        project.benefits.forEach { benefit ->
                            Text(
                                text = "• $benefit",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF666666),
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 8.dp, top = 2.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 액션 버튼들
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 지원하기 버튼
                Button(
                    onClick = onApplyClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "지원하기",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // 공유하기 버튼
                OutlinedButton(
                    onClick = onShareClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "공유",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // 더보기 버튼
                IconButton(
                    onClick = { isExpanded = !isExpanded }
                ) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (isExpanded) "접기" else "더보기",
                        tint = Color(0xFF666666)
                    )
                }
            }
        }
    }
}

@Composable
private fun ProjectStatusBadge(
    status: ProjectStatus,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when (status) {
        ProjectStatus.RECRUITING -> Color(0xFFE8F5E8)
        ProjectStatus.IN_PROGRESS -> Color(0xFFE3F2FD)
        ProjectStatus.COMPLETED -> Color(0xFFF3E5F5)
        ProjectStatus.PAUSED -> Color(0xFFFFF3E0)
        ProjectStatus.CANCELLED -> Color(0xFFFFEBEE)
    }

    val textColor = when (status) {
        ProjectStatus.RECRUITING -> Color(0xFF2E7D32)
        ProjectStatus.IN_PROGRESS -> Color(0xFF1565C0)
        ProjectStatus.COMPLETED -> Color(0xFF7B1FA2)
        ProjectStatus.PAUSED -> Color(0xFFEF6C00)
        ProjectStatus.CANCELLED -> Color(0xFFC62828)
    }

    Surface(
        modifier = modifier,
        color = backgroundColor,
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = status.displayName,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Medium
            ),
            color = textColor,
            fontSize = 11.sp
        )
    }
}

