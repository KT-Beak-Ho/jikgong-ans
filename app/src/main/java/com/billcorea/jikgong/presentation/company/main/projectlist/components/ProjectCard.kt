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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.presentation.company.main.projectlist.data.ProjectRegistrationData
import com.billcorea.jikgong.presentation.company.main.projectlist.data.ProjectRegistrationSampleData
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
    onProjectAction: (ProjectData, String) -> Unit,
    modifier: Modifier = Modifier
) {
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
            // 헤더: 제목과 상태
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

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "위치",
                            tint = appColorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = project.location,
                            style = AppTypography.bodyMedium,
                            color = appColorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // 상태 태그
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = when (project.status) {
                        ProjectStatus.RECRUITING -> Color(0xFF4CAF50).copy(alpha = 0.1f)
                        ProjectStatus.IN_PROGRESS -> Color(0xFF2196F3).copy(alpha = 0.1f)
                        ProjectStatus.COMPLETED -> Color(0xFF9E9E9E).copy(alpha = 0.1f)
                        ProjectStatus.CANCELLED -> Color(0xFFEF5350).copy(alpha = 0.1f)
                    }
                ) {
                    Text(
                        text = when (project.status) {
                            ProjectStatus.RECRUITING -> "모집중"
                            ProjectStatus.IN_PROGRESS -> "진행중"
                            ProjectStatus.COMPLETED -> "완료"
                            ProjectStatus.CANCELLED -> "취소"
                        },
                        style = AppTypography.labelMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = when (project.status) {
                            ProjectStatus.RECRUITING -> Color(0xFF4CAF50)
                            ProjectStatus.IN_PROGRESS -> Color(0xFF2196F3)
                            ProjectStatus.COMPLETED -> Color(0xFF9E9E9E)
                            ProjectStatus.CANCELLED -> Color(0xFFEF5350)
                        },
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 프로젝트 정보
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // 기간
                InfoItem(
                    icon = Icons.Default.DateRange,
                    label = "기간",
                    value = "${project.startDate.format(DateTimeFormatter.ofPattern("MM/dd"))} ~ ${project.endDate.format(DateTimeFormatter.ofPattern("MM/dd"))}",
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(16.dp))

                // 인원
                InfoItem(
                    icon = Icons.Default.Group,
                    label = "인원",
                    value = "${project.completedWorkers}/${project.totalWorkers}명",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 급여 정보
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AttachMoney,
                        contentDescription = "급여",
                        tint = appColorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(project.dailyWage)}원/일",
                        style = AppTypography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = appColorScheme.primary
                    )
                }

                // 긴급 태그
                if (project.isUrgent) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFEF5350)
                    ) {
                        Text(
                            text = "긴급",
                            style = AppTypography.labelSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // 액션 버튼들 (상태에 따라)
            if (project.status == ProjectStatus.RECRUITING) {
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { onProjectAction(project, "edit") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "수정",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("수정")
                    }

                    Button(
                        onClick = { onProjectAction(project, "recruit") },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Group,
                            contentDescription = "모집",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("인력 모집")
                    }
                }
            }

            // 설명이 있는 경우 표시
            if (project.description.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = appColorScheme.surfaceVariant.copy(alpha = 0.3f)
                ) {
                    Text(
                        text = project.description,
                        style = AppTypography.bodySmall,
                        color = appColorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(12.dp)
                    )
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
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = appColorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = label,
                style = AppTypography.labelMedium,
                color = appColorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = value,
            style = AppTypography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            color = appColorScheme.onSurface
        )
    }
}

// 프로젝트 데이터 모델 (기존 파일에서 import하지 못할 경우를 대비)
data class ProjectData(
    val id: String,
    val title: String,
    val description: String,
    val location: String,
    val detailAddress: String,
    val distance: Double,
    val jobTypes: List<String>,
    val totalWorkers: Int,
    val completedWorkers: Int,
    val dailyWage: Long,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val startTime: String,
    val endTime: String,
    val status: ProjectStatus,
    val isUrgent: Boolean,
    val requirements: List<String>,
    val providedItems: List<String>,
    val notes: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

enum class ProjectStatus {
    RECRUITING,    // 모집중
    IN_PROGRESS,   // 진행중
    COMPLETED,     // 완료
    CANCELLED      // 취소
}

@Preview(showBackground = true)
@Composable
fun ProjectCardPreview() {
    Jikgong1111Theme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 모집중 프로젝트
            ProjectCard(
                project = ProjectData(
                    id = "1",
                    title = "강남구 아파트 신축 공사",
                    description = "고급 아파트 신축을 위한 건설 프로젝트입니다. 경험 있는 인력을 모집합니다.",
                    location = "서울 강남구",
                    detailAddress = "테헤란로 123",
                    distance = 2.5,
                    jobTypes = listOf("보통인부", "철근공"),
                    totalWorkers = 15,
                    completedWorkers = 3,
                    dailyWage = 150000L,
                    startDate = LocalDate.now().plusDays(3),
                    endDate = LocalDate.now().plusDays(30),
                    startTime = "08:00",
                    endTime = "17:00",
                    status = ProjectStatus.RECRUITING,
                    isUrgent = true,
                    requirements = listOf("안전화 필수"),
                    providedItems = listOf("중식 제공"),
                    notes = "깔끔한 작업 환경",
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now()
                ),
                onProjectClick = {},
                onProjectAction = { _, _ -> }
            )

            // 완료된 프로젝트
            ProjectCard(
                project = ProjectData(
                    id = "2",
                    title = "부산 교량 보수 공사",
                    description = "교량 안전성 강화를 위한 보수 작업",
                    location = "부산 해운대구",
                    detailAddress = "해운대로 456",
                    distance = 15.2,
                    jobTypes = listOf("용접공"),
                    totalWorkers = 8,
                    completedWorkers = 8,
                    dailyWage = 180000L,
                    startDate = LocalDate.now().minusDays(20),
                    endDate = LocalDate.now().minusDays(1),
                    startTime = "08:00",
                    endTime = "17:00",
                    status = ProjectStatus.COMPLETED,
                    isUrgent = false,
                    requirements = listOf(),
                    providedItems = listOf(),
                    notes = "",
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now()
                ),
                onProjectClick = {},
                onProjectAction = { _, _ -> }
            )
        }
    }
}