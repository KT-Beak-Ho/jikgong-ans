package com.billcorea.jikgong.presentation.company.main.scout.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.presentation.company.main.scout.components.WorkerCard
import com.billcorea.jikgong.presentation.company.main.scout.components.EmptyState
import com.billcorea.jikgong.network.models.Worker
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme

@Composable
fun WorkerListPage(
    modifier: Modifier = Modifier,
    workers: List<Worker>,
    isLoading: Boolean,
    onWorkerClick: (Worker) -> Unit,
    onScoutClick: (Worker) -> Unit,
    onRefresh: () -> Unit,
    onFilterClick: () -> Unit = {},
    isFilterActive: Boolean = false
) {
    if (isLoading && workers.isEmpty()) {
        // 초기 로딩
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = Color(0xFF4B7BFF)
            )
        }
    } else if (workers.isEmpty()) {
        // 빈 상태
        Box(
            modifier = modifier.fillMaxSize()
        ) {
            EmptyState(
                title = "주변에 인력이 없습니다",
                description = "위치 설정을 확인하거나\n검색 반경을 늘려보세요",
                icon = "👷"
            )
        }
    } else {
        // 인력 목록
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 상단 정보
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "주변 ${workers.size}명의 인력",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Medium
                            )
                        )

                        // 새로고침 버튼
                        if (!isLoading) {
                            IconButton(
                                onClick = onRefresh,
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "새로고침",
                                    tint = Color(0xFF4B7BFF)
                                )
                            }
                        }
                    }

                    // 필터 버튼
                    TextButton(onClick = onFilterClick) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "필터",
                                color = if (isFilterActive) Color(0xFF4B7BFF) else Color.Gray,
                                fontWeight = if (isFilterActive) FontWeight.Bold else FontWeight.Normal
                            )
                            if (isFilterActive) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFF4B7BFF),
                                    modifier = Modifier.size(6.dp)
                                ) {}
                            }
                        }
                    }
                }

                // 로딩 인디케이터
                if (isLoading && workers.isNotEmpty()) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFF4B7BFF)
                    )
                }
            }

            // 노동자 카드 목록
            items(
                items = workers,
                key = { it.id }
            ) { worker ->
                WorkerCard(
                    worker = worker,
                    onCardClick = { onWorkerClick(worker) },
                    onScoutClick = { onScoutClick(worker) }
                )
            }

            // 하단 여백
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Preview(name = "인력 목록 - 데이터 있음", showBackground = true, backgroundColor = 0xFFF7F8FA)
@Composable
fun WorkerListPageWithDataPreview() {
    Jikgong1111Theme {
        WorkerListPage(
            workers = listOf(
                Worker(
                    id = "1",
                    name = "김철수",
                    jobTypes = listOf("철근공", "형틀목공"),
                    experience = 5,
                    distance = 0.8,
                    rating = 4.8f,
                    introduction = "성실하고 꼼꼼한 작업을 약속드립니다.",
                    desiredWage = "일당 18만원",
                    isAvailable = true,
                    completedProjects = 52
                ),
                Worker(
                    id = "2",
                    name = "이영희",
                    jobTypes = listOf("타일공"),
                    experience = 3,
                    distance = 1.2,
                    rating = 4.5f,
                    introduction = "깔끔한 마감 처리가 장점입니다.",
                    desiredWage = "일당 15만원",
                    isAvailable = true,
                    completedProjects = 28
                ),
                Worker(
                    id = "3",
                    name = "박민수",
                    jobTypes = listOf("전기공", "배관공"),
                    experience = 8,
                    distance = 2.5,
                    rating = 4.9f,
                    introduction = "다년간의 경험으로 신속 정확한 작업 보장합니다.",
                    desiredWage = "일당 20만원",
                    isAvailable = false,
                    completedProjects = 103
                ),
                Worker(
                    id = "4",
                    name = "정수진",
                    jobTypes = listOf("도장공"),
                    experience = 2,
                    distance = 1.8,
                    rating = 4.3f,
                    introduction = "꼼꼼한 작업으로 만족도 높은 결과물을 제공합니다.",
                    desiredWage = "협의 가능",
                    isAvailable = true,
                    completedProjects = 15
                ),
                Worker(
                    id = "5",
                    name = "최영호",
                    jobTypes = listOf("조적공", "미장공"),
                    experience = 10,
                    distance = 3.2,
                    rating = 4.7f,
                    introduction = "20년 경력의 베테랑입니다.",
                    desiredWage = "일당 22만원",
                    isAvailable = true,
                    completedProjects = 156
                )
            ),
            isLoading = false,
            onWorkerClick = {},
            onScoutClick = {},
            onRefresh = {},
            onFilterClick = {},
            isFilterActive = false
        )
    }
}

@Preview(name = "인력 목록 - 데이터 없음", showBackground = true, backgroundColor = 0xFFF7F8FA)
@Composable
fun WorkerListPageEmptyPreview() {
    Jikgong1111Theme {
        WorkerListPage(
            workers = emptyList(),
            isLoading = false,
            onWorkerClick = {},
            onScoutClick = {},
            onRefresh = {},
            onFilterClick = {},
            isFilterActive = false
        )
    }
}

