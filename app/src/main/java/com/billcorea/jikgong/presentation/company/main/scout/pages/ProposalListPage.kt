package com.billcorea.jikgong.presentation.company.main.scout.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.billcorea.jikgong.presentation.company.main.scout.components.ProposalCard
import com.billcorea.jikgong.presentation.company.main.scout.components.EmptyState
import com.billcorea.jikgong.presentation.company.main.scout.data.Proposal
import com.billcorea.jikgong.presentation.company.main.scout.data.ProposalStatus
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import java.time.LocalDateTime

@Composable
fun ProposalListPage(
    modifier: Modifier = Modifier,
    proposals: List<Proposal>,
    isLoading: Boolean,
    onProposalClick: (Proposal) -> Unit,
    onRefresh: () -> Unit
) {
    // 상태별 제안 분류
    val acceptedProposals = proposals.filter { it.status == ProposalStatus.ACCEPTED }
    val pendingProposals = proposals.filter { it.status == ProposalStatus.PENDING }
    val rejectedProposals = proposals.filter { it.status == ProposalStatus.REJECTED }

    if (isLoading && proposals.isEmpty()) {
        // 초기 로딩
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = Color(0xFF4B7BFF)
            )
        }
    } else if (proposals.isEmpty()) {
        // 빈 상태
        Box(
            modifier = modifier.fillMaxSize()
        ) {
            EmptyState(
                title = "아직 제안한 내역이 없습니다",
                description = "인력 목록에서 필요한 인력을\n스카웃해보세요",
                icon = "📋"
            )
        }
    } else {
        // 제안 목록
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 새로고침 및 로딩 표시
            item {
                if (isLoading) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        color = Color(0xFF4B7BFF)
                    )
                }
            }

            // 통계 카드
            item {
                ProposalStatisticsCard(
                    totalCount = proposals.size,
                    acceptedCount = acceptedProposals.size,
                    pendingCount = pendingProposals.size,
                    rejectedCount = rejectedProposals.size,
                    onRefresh = onRefresh,
                    isLoading = isLoading
                )
            }

            // 수락된 제안
            if (acceptedProposals.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = "수락됨",
                        count = acceptedProposals.size,
                        color = Color(0xFF66BB6A)
                    )
                }

                items(acceptedProposals) { proposal ->
                    ProposalCard(
                        proposal = proposal,
                        onCardClick = { onProposalClick(proposal) }
                    )
                }
            }

            // 대기중인 제안
            if (pendingProposals.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = "대기중",
                        count = pendingProposals.size,
                        color = Color(0xFFFFA726)
                    )
                }

                items(pendingProposals) { proposal ->
                    ProposalCard(
                        proposal = proposal,
                        onCardClick = { onProposalClick(proposal) }
                    )
                }
            }

            // 거절된 제안
            if (rejectedProposals.isNotEmpty()) {
                item {
                    SectionHeader(
                        title = "거절됨",
                        count = rejectedProposals.size,
                        color = Color(0xFFEF5350)
                    )
                }

                items(rejectedProposals) { proposal ->
                    ProposalCard(
                        proposal = proposal,
                        onCardClick = { onProposalClick(proposal) }
                    )
                }
            }

            // 하단 여백
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun ProposalStatisticsCard(
    totalCount: Int,
    acceptedCount: Int,
    pendingCount: Int,
    rejectedCount: Int,
    onRefresh: () -> Unit,
    isLoading: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "제안 현황",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                // 새로고침 버튼
                IconButton(
                    onClick = onRefresh,
                    enabled = !isLoading,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "새로고침",
                        tint = if (isLoading) Color.Gray else Color(0xFF4B7BFF)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatisticItem(
                    label = "전체",
                    value = totalCount.toString(),
                    color = Color.Black
                )
                StatisticItem(
                    label = "수락",
                    value = acceptedCount.toString(),
                    color = Color(0xFF66BB6A)
                )
                StatisticItem(
                    label = "대기",
                    value = pendingCount.toString(),
                    color = Color(0xFFFFA726)
                )
                StatisticItem(
                    label = "거절",
                    value = rejectedCount.toString(),
                    color = Color(0xFFEF5350)
                )
            }

            // 수락률
            if (totalCount > 0) {
                val acceptanceRate = (acceptedCount.toFloat() / totalCount * 100).toInt()
                LinearProgressIndicator(
                    progress = { acceptedCount.toFloat() / totalCount },
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFF66BB6A),
                    trackColor = Color(0xFFE0E0E0)
                )
                Text(
                    text = "수락률 $acceptanceRate%",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
private fun StatisticItem(
    label: String,
    value: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}

@Composable
private fun SectionHeader(
    title: String,
    count: Int,
    color: Color
) {
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
            Surface(
                shape = MaterialTheme.shapes.small,
                color = color.copy(alpha = 0.1f)
            ) {
                Text(
                    text = title,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = color
                )
            }
            Text(
                text = "${count}건",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProposalListPagePreview() {
    Jikgong1111Theme {
        ProposalListPage(
            proposals = listOf(
                Proposal(
                    id = "1",
                    workerId = "1",
                    workerName = "김철수",
                    proposedWage = "일당 18만원",
                    message = "프로젝트 참여 부탁드립니다.",
                    status = ProposalStatus.ACCEPTED,
                    createdAt = LocalDateTime.now(),
                    jobTypes = listOf("철근공"),
                    distance = 0.8,
                    workerPhone = "010-1234-5678"
                ),
                Proposal(
                    id = "2",
                    workerId = "2",
                    workerName = "이영희",
                    proposedWage = "일당 15만원",
                    message = "다음 주부터 시작하는 프로젝트입니다.",
                    status = ProposalStatus.PENDING,
                    createdAt = LocalDateTime.now().minusHours(2),
                    jobTypes = listOf("타일공"),
                    distance = 1.2
                ),
                Proposal(
                    id = "3",
                    workerId = "3",
                    workerName = "박민수",
                    proposedWage = "일당 20만원",
                    message = "긴급 프로젝트입니다.",
                    status = ProposalStatus.REJECTED,
                    createdAt = LocalDateTime.now().minusDays(1),
                    jobTypes = listOf("전기공"),
                    distance = 2.5,
                    rejectReason = "일정이 맞지 않습니다"
                )
            ),
            isLoading = false,
            onProposalClick = {},
            onRefresh = {}
        )
    }
}