package com.billcorea.jikgong.presentation.company.main.projectlist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.presentation.company.main.projectlist.data.Project
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.appColorScheme
import java.text.NumberFormat
import java.util.*

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
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = appColorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // 헤더 - 상태 배지 + 긴급 여부
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 상태 배지
                    AssistChip(
                        onClick = { },
                        label = {
                            Text(
                                text = project.status.displayName,
                                style = AppTypography.labelSmall,
                                color = Color.White
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = project.status.color,
                            labelColor = Color.White
                        ),
                        modifier = Modifier.height(24.dp)
                    )

                    // 긴급 배지
                    if (project.isUrgent) {
                        AssistChip(
                            onClick = { },
                            label = {
                                Text(
                                    text = "긴급",
                                    style = AppTypography.labelSmall,
                                    color = Color.White
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = "긴급",
                                    modifier = Modifier.size(12.dp),
                                    tint = Color.White
                                )
                            },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = appColorScheme.error,
                                labelColor = Color.White
                            ),
                            modifier = Modifier.height(24.dp)
                        )
                    }
                }

                // 북마크 버튼
                IconButton(
                    onClick = { onBookmarkClick(project) },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = if (project.isBookmarked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "북마크",
                        tint = if (project.isBookmarked) appColorScheme.error else appColorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 프로젝트 제목
            Text(
                text = project.title,
                style = AppTypography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = appColorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 위치 정보
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "위치",
                    modifier = Modifier.size(16.dp),
                    tint = appColorScheme.primary
                )
                Text(
                    text = project.location,
                    style = AppTypography.bodyMedium,
                    color = appColorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 기간 정보
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = "기간",
                    modifier = Modifier.size(16.dp),
                    tint = appColorScheme.primary
                )
                Text(
                    text = "${project.startDate} ~ ${project.endDate}",
                    style = AppTypography.bodyMedium,
                    color = appColorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 일당 및 모집 정보
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "일당",
                        style = AppTypography.labelSmall,
                        color = appColorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(project.dailyWage)}원",
                        style = AppTypography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = appColorScheme.primary
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "모집 현황",
                        style = AppTypography.labelSmall,
                        color = appColorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${project.currentWorkers}/${project.requiredWorkers}명",
                        style = AppTypography.titleSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = if (project.currentWorkers >= project.requiredWorkers)
                            appColorScheme.tertiary else appColorScheme.secondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 모집률 프로그래스 바
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "모집률",
                        style = AppTypography.labelSmall,
                        color = appColorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${(project.recruitmentRate * 100).toInt()}%",
                        style = AppTypography.labelSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = appColorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = project.recruitmentRate,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = when {
                        project.recruitmentRate >= 1.0f -> appColorScheme.tertiary
                        project.recruitmentRate >= 0.7f -> appColorScheme.secondary
                        else -> appColorScheme.primary
                    },
                    trackColor = appColorScheme.outline.copy(alpha = 0.3f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 액션 버튼들
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 지원하기 버튼
                Button(
                    onClick = { onApplyClick(project) },
                    modifier = Modifier.weight(1f),
                    enabled = project.canApply,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = appColorScheme.primary,
                        contentColor = appColorScheme.onPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (project.canApply) "인력 모집" else "모집 완료",
                        style = AppTypography.labelMedium.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                }

                // 더보기 버튼
                OutlinedButton(
                    onClick = { onMoreClick(project.id) },
                    modifier = Modifier.width(80.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = appColorScheme.onSurfaceVariant
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "더보기",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
