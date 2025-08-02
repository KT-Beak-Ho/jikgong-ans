// ========================================
// 📄 components/ProjectCard.kt
// ========================================
package com.billcorea.jikgong.presentation.company.main.projectlist.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.presentation.company.main.projectlist.data.Project
import com.billcorea.jikgong.presentation.company.main.projectlist.data.ProjectStatus
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.appColorScheme
import java.text.NumberFormat
import java.util.*

/**
 * 프로젝트 카드 컴포넌트 - 이미지 UI 스타일 적용
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectCard(
    project: Project,
    onProjectClick: (Project) -> Unit,
    onBookmarkClick: (Project) -> Unit,
    onApplyClick: (Project) -> Unit,
    onMoreClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onProjectClick(project) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = appColorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 8.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 상단 헤더 (제목, 상태, 북마크)
            ProjectCardHeader(
                project = project,
                onBookmarkClick = onBookmarkClick,
                onMoreClick = onMoreClick
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 위치 정보
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "위치",
                    tint = appColorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = project.location.shortAddress,
                    style = AppTypography.bodyMedium,
                    color = appColorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 중간 정보 (기간, 인원, 임금)
            ProjectCardMiddleInfo(project = project)

            Spacer(modifier = Modifier.height(12.dp))

            // 하단 액션 버튼들
            ProjectCardActions(
                project = project,
                onApplyClick = onApplyClick
            )
        }
    }
}

@Composable
private fun ProjectCardHeader(
    project: Project,
    onBookmarkClick: (Project) -> Unit,
    onMoreClick: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        // 제목과 상태
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = project.title,
                    style = AppTypography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = appColorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                if (project.isUrgent) {
                    Spacer(modifier = Modifier.width(8.dp))
                    AssistChip(
                        onClick = { },
                        label = {
                            Text(
                                text = "긴급",
                                style = AppTypography.labelSmall
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = Color(0xFFFFEBEE),
                            labelColor = Color(0xFFD32F2F)
                        ),
                        modifier = Modifier.height(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            ProjectStatusChip(status = project.status)
        }

        // 북마크와 더보기 버튼
        Row {
            IconButton(
                onClick = { onBookmarkClick(project) },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = if (project.isBookmarked) {
                        Icons.Default.Bookmark
                    } else {
                        Icons.Outlined.BookmarkBorder
                    },
                    contentDescription = if (project.isBookmarked) "북마크 해제" else "북마크",
                    tint = if (project.isBookmarked) {
                        appColorScheme.primary
                    } else {
                        appColorScheme.onSurfaceVariant
                    },
                    modifier = Modifier.size(20.dp)
                )
            }

            IconButton(
                onClick = { onMoreClick(project.id) },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "더보기",
                    tint = appColorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun ProjectCardMiddleInfo(project: Project) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // 프로젝트 기간
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "기간",
                    tint = appColorScheme.onSurfaceVariant,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${project.startDate.monthValue}/${project.startDate.dayOfMonth} ~ ${project.endDate.monthValue}/${project.endDate.dayOfMonth}",
                    style = AppTypography.bodySmall,
                    color = appColorScheme.onSurfaceVariant
                )
            }
        }

        // 모집 인원
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Group,
                    contentDescription = "인원",
                    tint = appColorScheme.onSurfaceVariant,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${project.appliedWorkers}/${project.requiredWorkers}명",
                    style = AppTypography.bodySmall,
                    color = appColorScheme.onSurfaceVariant
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // 일당 정보 (강조)
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.AttachMoney,
            contentDescription = "임금",
            tint = appColorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(project.dailyWage)}원/일",
            style = AppTypography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = appColorScheme.primary
        )
    }
}

@Composable
private fun ProjectCardActions(
    project: Project,
    onApplyClick: (Project) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 회사 정보
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = project.companyName,
                style = AppTypography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = appColorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "평점",
                    tint = Color(0xFFFFB300),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = project.companyRating.toString(),
                    style = AppTypography.bodySmall,
                    color = appColorScheme.onSurfaceVariant
                )
            }
        }

        // 지원 버튼
        if (project.canApply) {
            Button(
                onClick = { onApplyClick(project) },
                modifier = Modifier.height(36.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = appColorScheme.primary
                )
            ) {
                Text(
                    text = "인력 모집",
                    style = AppTypography.labelMedium
                )
            }
        } else {
            OutlinedButton(
                onClick = { },
                enabled = false,
                modifier = Modifier.height(36.dp)
            ) {
                Text(
                    text = when (project.status) {
                        ProjectStatus.COMPLETED -> "완료"
                        ProjectStatus.CANCELLED -> "취소"
                        ProjectStatus.IN_PROGRESS -> "진행중"
                        else -> "마감"
                    },
                    style = AppTypography.labelMedium
                )
            }
        }
    }
}