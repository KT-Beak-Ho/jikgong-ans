package com.billcorea.jikgong.presentation.company.main.projectlist.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.presentation.company.main.projectlist.data.*
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.96f else 1f,
        animationSpec = tween(durationMillis = 150),
        label = "card_scale"
    )

    val bookmarkColor by animateColorAsState(
        targetValue = if (project.isBookmarked) Color(0xFFFF6B35) else appColorScheme.onSurfaceVariant,
        animationSpec = tween(durationMillis = 200),
        label = "bookmark_color"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable {
                isPressed = true
                onCardClick()
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = appColorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp,
            pressedElevation = 8.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 상단: 카테고리 배지 + 북마크
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProjectCategoryBadge(category = project.category)

                IconButton(
                    onClick = onBookmarkClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (project.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "북마크",
                        tint = bookmarkColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // 프로젝트 제목
            Text(
                text = project.title,
                style = AppTypography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = appColorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // 위치 정보
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

            // 기간 정보
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = "기간",
                    tint = appColorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "${project.startDate.format(DateTimeFormatter.ofPattern("MM/dd"))} ~ ${project.endDate.format(DateTimeFormatter.ofPattern("MM/dd"))}",
                    style = AppTypography.bodyMedium,
                    color = appColorScheme.onSurfaceVariant
                )
            }

            // 임금 및 상태
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "일당",
                        style = AppTypography.bodySmall,
                        color = appColorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${String.format("%,d", project.dailyWage)}원",
                        style = AppTypography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = appColorScheme.primary
                    )
                }

                ProjectStatusBadge(status = project.status)
            }

            // 하단: 액션 버튼들
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onApplyClick,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = appColorScheme.primary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("지원하기")
                }

                OutlinedButton(
                    onClick = onShareClick,
                    modifier = Modifier.size(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "공유",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ProjectCategoryBadge(
    category: ProjectCategory,
    modifier: Modifier = Modifier
) {
    val backgroundColor = when (category) {
        ProjectCategory.CIVIL_ENGINEERING -> Color(0xFF4CAF50)
        ProjectCategory.BUILDING -> Color(0xFF2196F3)
        ProjectCategory.ELECTRICAL -> Color(0xFFFFC107)
        ProjectCategory.PLUMBING -> Color(0xFF9C27B0)
        ProjectCategory.INTERIOR -> Color(0xFFE91E63)
        ProjectCategory.LANDSCAPING -> Color(0xFF8BC34A)
        ProjectCategory.ROAD_CONSTRUCTION -> Color(0xFF607D8B)
        ProjectCategory.OTHER -> Color(0xFF757575)
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = backgroundColor.copy(alpha = 0.1f)
    ) {
        Text(
            text = category.displayName,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = AppTypography.labelSmall.copy(fontWeight = FontWeight.Medium),
            color = backgroundColor
        )
    }
}

@Composable
private fun ProjectStatusBadge(
    status: ProjectStatus,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor, text) = when (status) {
        ProjectStatus.RECRUITING -> Triple(
            Color(0xFF4CAF50).copy(alpha = 0.1f),
            Color(0xFF4CAF50),
            "모집중"
        )
        ProjectStatus.IN_PROGRESS -> Triple(
            Color(0xFF2196F3).copy(alpha = 0.1f),
            Color(0xFF2196F3),
            "진행중"
        )
        ProjectStatus.COMPLETED -> Triple(
            Color(0xFF757575).copy(alpha = 0.1f),
            Color(0xFF757575),
            "완료"
        )
        ProjectStatus.PAUSED -> Triple(
            Color(0xFFFFC107).copy(alpha = 0.1f),
            Color(0xFFF57C00),
            "일시중단"
        )
        ProjectStatus.CANCELLED -> Triple(
            Color(0xFFF44336).copy(alpha = 0.1f),
            Color(0xFFF44336),
            "취소"
        )
    }

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = AppTypography.labelMedium.copy(fontWeight = FontWeight.Medium),
            color = textColor
        )
    }
}

@Preview
@Composable
fun ProjectCardPreview() {
    Jikgong1111Theme {
        ProjectCard(
            project = Project(
                id = "1",
                title = "강남구 신축 아파트 건설 현장 철근공 모집",
                description = "20층 규모의 신축 아파트 건설 현장",
                location = "서울시 강남구 역삼동",
                category = ProjectCategory.BUILDING,
                status = ProjectStatus.RECRUITING,
                dailyWage = 150000,
                startDate = LocalDateTime.now(),
                endDate = LocalDateTime.now().plusDays(30),
                requiredWorkers = 5,
                appliedWorkers = 2,
                companyName = "삼성건설",
                contactNumber = "010-1234-5678",
                requirements = listOf("철근 작업 경험 3년 이상"),
                benefits = listOf("식사 제공", "교통비 지급"),
                isBookmarked = true,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            ),
            onCardClick = {},
            onBookmarkClick = {},
            onApplyClick = {},
            onShareClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}