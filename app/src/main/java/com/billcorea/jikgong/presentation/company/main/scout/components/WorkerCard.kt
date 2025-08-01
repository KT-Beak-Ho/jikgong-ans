// app/src/main/java/com/billcorea/jikgong/presentation/company/main/scout/components/WorkerCard.kt
package com.billcorea.jikgong.presentation.company.main.scout.components

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
import com.billcorea.jikgong.presentation.company.main.scout.data.WorkerData
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerCard(
    worker: WorkerData,
    onScoutClick: (WorkerData) -> Unit,
    onFavoriteClick: (WorkerData) -> Unit,
    onProfileClick: (WorkerData) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { onProfileClick(worker) },
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
            // 헤더 - 이름, 온라인 상태, 즐겨찾기
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = worker.name,
                            style = AppTypography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = appColorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        // 온라인 상태 표시
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = if (worker.isOnline) Color(0xFF4CAF50) else Color(0xFF9E9E9E)
                        ) {
                            Text(
                                text = if (worker.isOnline) "온라인" else "오프라인",
                                style = AppTypography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        // 신규 인력 표시
                        if (worker.isNewWorker) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFF2196F3)
                            ) {
                                Text(
                                    text = "신규",
                                    style = AppTypography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // 경력 정보
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Work,
                            contentDescription = null,
                            tint = appColorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "${worker.experience}년 경력",
                            style = AppTypography.bodyMedium,
                            color = appColorScheme.onSurfaceVariant
                        )
                    }
                }

                // 평점과 즐겨찾기
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "평점",
                            tint = Color(0xFFFFB300),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = String.format("%.1f", worker.rating),
                            style = AppTypography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = appColorScheme.onSurface
                        )
                    }

                    IconButton(
                        onClick = { onFavoriteClick(worker) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = if (worker.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "즐겨찾기",
                            tint = if (worker.isFavorite) Color(0xFFEF5350) else appColorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

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
                    text = "${worker.location} • ${String.format("%.1f", worker.distance)}km",
                    style = AppTypography.bodyMedium,
                    color = appColorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 스킬 태그들
            if (worker.skills.isNotEmpty()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    worker.skills.take(3).forEach { skill ->
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = appColorScheme.primaryContainer.copy(alpha = 0.3f)
                        ) {
                            Text(
                                text = skill,
                                style = AppTypography.labelMedium,
                                color = appColorScheme.primary,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }

                    // 더 많은 스킬이 있을 경우
                    if (worker.skills.size > 3) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = appColorScheme.surfaceVariant.copy(alpha = 0.5f)
                        ) {
                            Text(
                                text = "+${worker.skills.size - 3}",
                                style = AppTypography.labelMedium,
                                color = appColorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // 추가 정보 (전화번호나 마지막 활동 시간)
            if (worker.phone != null || worker.lastActiveAt != null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (worker.phone != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = "전화",
                                tint = appColorScheme.onSurfaceVariant,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = worker.phone!!,
                                style = AppTypography.bodySmall,
                                color = appColorScheme.onSurfaceVariant
                            )
                        }
                    }

                    if (worker.lastActiveAt != null && !worker.isOnline) {
                        Text(
                            text = "최근 활동: ${formatLastActive(worker.lastActiveAt!!)}",
                            style = AppTypography.bodySmall,
                            color = appColorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // 액션 버튼
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 프로필 보기 버튼
                OutlinedButton(
                    onClick = { onProfileClick(worker) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = appColorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "프로필",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "프로필",
                        style = AppTypography.labelMedium
                    )
                }

                // 스카웃 제안 버튼
                Button(
                    onClick = { onScoutClick(worker) },
                    modifier = Modifier.weight(1.5f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = appColorScheme.primary,
                        contentColor = appColorScheme.onPrimary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = "스카웃",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "스카웃 제안",
                        style = AppTypography.labelLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun formatLastActive(lastActiveAt: LocalDateTime): String {
    val now = LocalDateTime.now()
    val minutes = java.time.Duration.between(lastActiveAt, now).toMinutes()

    return when {
        minutes < 60 -> "${minutes}분 전"
        minutes < 1440 -> "${minutes / 60}시간 전"
        else -> "${minutes / 1440}일 전"
    }
}

@Preview(name = "온라인 인력", showBackground = true)
@Composable
fun WorkerCardOnlinePreview() {
    Jikgong1111Theme {
        WorkerCard(
            worker = WorkerData(
                id = "worker1",
                name = "김철수",
                experience = 5,
                location = "서울 강남구",
                distance = 2.5,
                rating = 4.8f,
                skills = listOf("보통인부", "철근공", "콘크리트공"),
                isOnline = true,
                isFavorite = false,
                isNewWorker = true,
                phone = "010-1234-5678"
            ),
            onScoutClick = {},
            onFavoriteClick = {},
            onProfileClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(name = "오프라인 인력", showBackground = true)
@Composable
fun WorkerCardOfflinePreview() {
    Jikgong1111Theme {
        WorkerCard(
            worker = WorkerData(
                id = "worker2",
                name = "이영희",
                experience = 3,
                location = "서울 서초구",
                distance = 3.2,
                rating = 4.6f,
                skills = listOf("콘크리트공", "타일공", "배관공", "전기공", "도장공"),
                isOnline = false,
                isFavorite = true,
                isNewWorker = false,
                lastActiveAt = LocalDateTime.now().minusHours(2)
            ),
            onScoutClick = {},
            onFavoriteClick = {},
            onProfileClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(name = "즐겨찾기 인력", showBackground = true)
@Composable
fun WorkerCardFavoritePreview() {
    Jikgong1111Theme {
        WorkerCard(
            worker = WorkerData(
                id = "worker3",
                name = "박민수",
                experience = 8,
                location = "서울 송파구",
                distance = 5.1,
                rating = 4.9f,
                skills = listOf("용접공", "전기공"),
                isOnline = true,
                isFavorite = true,
                isNewWorker = false,
                phone = "010-9876-5432"
            ),
            onScoutClick = {},
            onFavoriteClick = {},
            onProfileClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(name = "다크 테마", showBackground = true)
@Composable
fun WorkerCardDarkPreview() {
    Jikgong1111Theme(darkTheme = true) {
        WorkerCard(
            worker = WorkerData(
                id = "worker4",
                name = "정수진",
                experience = 2,
                location = "서울 마포구",
                distance = 1.8,
                rating = 4.3f,
                skills = listOf("보통인부"),
                isOnline = false,
                isFavorite = false,
                isNewWorker = true,
                lastActiveAt = LocalDateTime.now().minusDays(1)
            ),
            onScoutClick = {},
            onFavoriteClick = {},
            onProfileClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}