package com.billcorea.jikgong.presentation.company.main.projectlist.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.presentation.company.main.projectlist.data.ProjectData
import com.billcorea.jikgong.presentation.company.main.projectlist.data.ProjectStatus
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import java.text.NumberFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectCard(
    project: ProjectData,
    onProjectClick: (ProjectData) -> Unit,
    onProjectAction: (ProjectData, String) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        onClick = { onProjectClick(project) },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = appColorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // 헤더: 제목과 메뉴
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = project.title,
                        style = AppTypography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = appColorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = project.location,
                        style = AppTypography.bodyMedium,
                        color = appColorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Box {
                    IconButton(
                        onClick = { showMenu = true },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "메뉴",
                            tint = appColorScheme.onSurfaceVariant
                        )
                    }

                    ProjectCardMenu(
                        expanded = showMenu,
                        onDismiss = { showMenu = false },
                        onEdit = {
                            onProjectAction(project, "edit")
                            showMenu = false
                        },
                        onDelete = {
                            onProjectAction(project, "delete")
                            showMenu = false
                        },
                        onRecruit = if (project.status == ProjectStatus.RECRUITING) {
                            {
                                onProjectAction(project, "recruit")
                                showMenu = false
                            }
                        } else null
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 상태 및 진행률
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProjectStatusChip(
                    status = project.status,
                    modifier = Modifier
                )

                Text(
                    text = "${project.completedWorkers}/${project.totalWorkers}명",
                    style = AppTypography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = appColorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 진행률 바
            LinearProgressIndicator(
                progress = project.progressPercentage / 100f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = when (project.status) {
                    ProjectStatus.RECRUITING -> Color(0xFFFFA726)
                    ProjectStatus.IN_PROGRESS -> appColorScheme.primary
                    ProjectStatus.COMPLETED -> Color(0xFF4CAF50)
                    ProjectStatus.CANCELLED -> appColorScheme.error
                },
                trackColor = appColorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 프로젝트 정보
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ProjectInfoItem(
                    icon = Icons.Default.CalendarToday,
                    title = "작업 기간",
                    value = "${project.startDate.format(DateTimeFormatter.ofPattern("MM/dd"))} - ${project.endDate.format(DateTimeFormatter.ofPattern("MM/dd"))}",
                    modifier = Modifier.weight(1f)
                )

                ProjectInfoItem(
                    icon = Icons.Default.AttachMoney,
                    title = "일급",
                    value = "${NumberFormat.getNumberInstance(Locale.KOREA).format(project.dailyWage)}원",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ProjectInfoItem(
                    icon = Icons.Default.Work,
                    title = "근무 시간",
                    value = project.workTimeDisplay,
                    modifier = Modifier.weight(1f)
                )

                ProjectInfoItem(
                    icon = Icons.Default.LocationOn,
                    title = "거리",
                    value = "${project.distance}km",
                    modifier = Modifier.weight(1f)
                )
            }

            // 급구 태그 (조건부 표시)
            if (project.isUrgent) {
                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFFF5722).copy(alpha = 0.1f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Speed,
                            contentDescription = "급구",
                            tint = Color(0xFFFF5722),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "급구",
                            style = AppTypography.labelMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color(0xFFFF5722)
                        )
                    }
                }
            }

            // 액션 버튼 (모집중인 경우만)
            if (project.status == ProjectStatus.RECRUITING) {
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onProjectAction(project, "recruit") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = appColorScheme.primary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "인력 모집",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "인력 모집하기",
                        style = AppTypography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun ProjectStatusChip(
    status: ProjectStatus,
    modifier: Modifier = Modifier
) {
    val (text, color) = when (status) {
        ProjectStatus.RECRUITING -> "모집중" to Color(0xFFFFA726)
        ProjectStatus.IN_PROGRESS -> "진행중" to appColorScheme.primary
        ProjectStatus.COMPLETED -> "완료" to Color(0xFF4CAF50)
        ProjectStatus.CANCELLED -> "취소" to appColorScheme.error
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = color.copy(alpha = 0.1f)
    ) {
        Text(
            text = text,
            style = AppTypography.labelMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = color,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
        )
    }
}

@Composable
private fun ProjectInfoItem(
    icon: ImageVector,
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = appColorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = title,
                style = AppTypography.labelSmall,
                color = appColorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = value,
            style = AppTypography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            color = appColorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun ProjectCardMenu(
    expanded: Boolean,
    onDismiss: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onRecruit: (() -> Unit)? = null
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismiss
    ) {
        DropdownMenuItem(
            text = { Text("수정하기") },
            onClick = onEdit,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "수정"
                )
            }
        )

        onRecruit?.let { recruit ->
            DropdownMenuItem(
                text = { Text("인력 모집") },
                onClick = recruit,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "인력 모집"
                    )
                }
            )
        }

        Divider()

        DropdownMenuItem(
            text = {
                Text(
                    text = "삭제하기",
                    color = appColorScheme.error
                )
            },
            onClick = onDelete,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "삭제",
                    tint = appColorScheme.error
                )
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProjectCardPreview() {
    val today = LocalDate.now()
    val sampleProject = ProjectData(
        id = "project1",
        title = "사하구 낙동5블럭 낙동강 온도 측정 센터 신축공사",
        description = "친환경 온도 측정 센터 건립을 위한 신축 공사입니다.",
        location = "부산 사하구",
        detailAddress = "부산광역시 사하구 낙동대로 123",
        distance = 2.5,
        jobTypes = listOf(),
        totalWorkers = 15,
        completedWorkers = 2,
        dailyWage = 130000,
        startDate = today.plusDays(3),
        endDate = today.plusDays(25),
        startTime = "08:00",
        endTime = "17:00",
        status = ProjectStatus.RECRUITING,
        isUrgent = true,
        requirements = listOf("안전화 필수", "작업복 착용"),
        providedItems = listOf("중식 제공", "교통비 지급"),
        notes = "신축 공사로 깔끔한 작업 환경입니다.",
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )

    val sampleProject2 = sampleProject.copy(
        id = "project2",
        title = "직공센터 공사",
        status = ProjectStatus.IN_PROGRESS,
        completedWorkers = 6,
        isUrgent = false
    )

    Jikgong1111Theme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProjectCard(
                project = sampleProject,
                onProjectClick = {}
            )

            ProjectCard(
                project = sampleProject2,
                onProjectClick = {}
            )
        }
    }
}