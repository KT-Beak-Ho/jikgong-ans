package com.billcorea.jikgong.presentation.company.main.projectlist.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.presentation.company.main.projectlist.data.Project
import com.billcorea.jikgong.presentation.company.main.projectlist.data.ProjectStatus
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.appColorScheme
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun ProjectCard(
    project: Project,
    onProjectClick: (Project) -> Unit,
    onQuickApplyClick: (Project) -> Unit,
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
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 헤더 (제목, 상태)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = project.title,
                        style = AppTypography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = appColorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "위치",
                            modifier = Modifier.size(16.dp),
                            tint = appColorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = project.location,
                            style = AppTypography.bodyMedium,
                            color = appColorScheme.onSurfaceVariant
                        )
                    }
                }

                // 상태 뱃지
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = project.status.color.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = project.status.displayName,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = AppTypography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = project.status.color
                    )
                }
            }

            // 작업 정보
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                InfoItem(
                    icon = Icons.Default.Work,
                    label = "직종",
                    value = project.workType
                )
                InfoItem(
                    icon = Icons.Default.People,
                    label = "모집",
                    value = "${project.currentCount}/${project.recruitCount}명"
                )
                InfoItem(
                    icon = Icons.Default.AttachMoney,
                    label = "일당",
                    value = NumberFormat.getNumberInstance(Locale.KOREA)
                        .format(project.dailyWage) + "원"
                )
            }

            // 기간 정보
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = "기간",
                    modifier = Modifier.size(16.dp),
                    tint = appColorScheme.onSurfaceVariant
                )
                Text(
                    text = "${project.startDate.format(DateTimeFormatter.ofPattern("MM/dd"))} ~ " +
                      "${project.endDate.format(DateTimeFormatter.ofPattern("MM/dd"))}",
                    style = AppTypography.bodyMedium,
                    color = appColorScheme.onSurfaceVariant
                )

                if (project.isUrgent) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = appColorScheme.error.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "긴급",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = AppTypography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = appColorScheme.error
                        )
                    }
                }
            }

            // 하단 액션
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 필수 기술
                if (project.requiredSkills.isNotEmpty()) {
                    Text(
                        text = "필요: ${project.requiredSkills.joinToString(", ")}",
                        style = AppTypography.bodySmall,
                        color = appColorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }

                // 빠른 지원 버튼
                if (project.status == ProjectStatus.RECRUITING) {
                    Button(
                        onClick = { onQuickApplyClick(project) },
                        modifier = Modifier.height(32.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = appColorScheme.primary
                        )
                    ) {
                        Text(
                            text = "빠른 지원",
                            style = AppTypography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(14.dp),
            tint = appColorScheme.primary
        )
        Column {
            Text(
                text = label,
                style = AppTypography.labelSmall,
                color = appColorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = AppTypography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                color = appColorScheme.onSurface
            )
        }
    }
}