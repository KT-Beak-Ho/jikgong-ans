package com.billcorea.jikgong.presentation.company.auth.main.projectlist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.billcorea.jikgong.presentation.company.auth.main.projectlist.data.ProjectData
import com.billcorea.jikgong.presentation.company.auth.main.projectlist.data.ProjectPriority
import com.billcorea.jikgong.presentation.company.auth.main.projectlist.data.ProjectSampleData
import com.billcorea.jikgong.presentation.company.auth.main.projectlist.data.ProjectStatus
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectCard(
    project: ProjectData,
    onProjectClick: (ProjectData) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onProjectClick(project) },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 상단: 제목과 상태
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = project.title,
                    style = AppTypography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = appColorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // 우선순위 아이콘 (긴급한 경우만)
                if (project.priority == ProjectPriority.URGENT) {
                    Icon(
                        imageVector = Icons.Default.PriorityHigh,
                        contentDescription = "긴급",
                        tint = Color.Red,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 상태 태그
            Surface(
                modifier = Modifier.clip(RoundedCornerShape(6.dp)),
                color = project.statusColor.copy(alpha = 0.1f)
            ) {
                Text(
                    text = project.statusDisplayName,
                    style = AppTypography.labelSmall.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = project.statusColor,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 위치 정보
            Row(
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
                    text = project.location,
                    style = AppTypography.bodySmall,
                    color = appColorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 기간 정보
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CalendarToday,
                    contentDescription = "기간",
                    tint = appColorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${project.startDate.format(DateTimeFormatter.ofPattern("MM/dd"))} - ${project.endDate.format(DateTimeFormatter.ofPattern("MM/dd"))} (${project.durationDays}일)",
                    style = AppTypography.bodySmall,
                    color = appColorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 인력 정보
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.People,
                    contentDescription = "인력",
                    tint = appColorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "필요 인력: ${project.totalCurrentWorkers}/${project.totalRequiredWorkers}명",
                    style = AppTypography.bodySmall,
                    color = appColorScheme.onSurfaceVariant
                )
            }

            // 모집 진행률 (모집중인 경우만)
            if (project.status == ProjectStatus.RECRUITING) {
                Spacer(modifier = Modifier.height(12.dp))

                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "모집 진행률",
                            style = AppTypography.labelSmall,
                            color = appColorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "${(project.recruitmentProgress * 100).toInt()}%",
                            style = AppTypography.labelSmall.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = appColorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    LinearProgressIndicator(
                        progress = project.recruitmentProgress,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = appColorScheme.primary,
                        trackColor = appColorScheme.surfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 예산 정보
            Text(
                text = "예산: ${NumberFormat.getNumberInstance(Locale.KOREA).format(project.totalBudget)}원",
                style = AppTypography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = appColorScheme.primary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProjectCardPreview() {
    Jikgong1111Theme {
        ProjectCard(
            project = ProjectSampleData.getSampleProjects().first(),
            onProjectClick = {}
        )
    }
}