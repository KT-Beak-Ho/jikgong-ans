package com.billcorea.jikgong.presentation.company.main.scout.feature.pages

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
import com.billcorea.jikgong.presentation.company.main.scout.presentation.component.ProposalCard
import com.billcorea.jikgong.presentation.company.main.scout.presentation.component.EmptyState
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.Proposal
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.ProposalStatus
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
            // 새로고침 버튼
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "총 ${proposals.size}건의 제안",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        
                        if (proposals.isNotEmpty()) {
                            Text(
                                text = "대기 ${pendingProposals.size} · 수락 ${acceptedProposals.size} · 거절 ${rejectedProposals.size}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                    }

                    if (!isLoading) {
                        IconButton(onClick = onRefresh) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "새로고침",
                                tint = Color(0xFF4B7BFF)
                            )
                        }
                    } else {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color(0xFF4B7BFF),
                            strokeWidth = 2.dp
                        )
                    }
                }
            }
            
            // 로딩 인디케이터 (새로고침 중)
            if (isLoading && proposals.isNotEmpty()) {
                item {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color(0xFF4B7BFF)
                    )
                }
            }

            // 대기중 제안
            if (pendingProposals.isNotEmpty()) {
                item {
                    SectionHeader(title = "대기중 (${pendingProposals.size})")
                }

                items(
                    items = pendingProposals,
                    key = { it.id }
                ) { proposal ->
                    ProposalCard(
                        proposal = proposal,
                        onClick = { onProposalClick(proposal) }
                    )
                }
            }

            // 수락된 제안
            if (acceptedProposals.isNotEmpty()) {
                item {
                    SectionHeader(title = "수락됨 (${acceptedProposals.size})")
                }

                items(
                    items = acceptedProposals,
                    key = { it.id }
                ) { proposal ->
                    ProposalCard(
                        proposal = proposal,
                        onClick = { onProposalClick(proposal) }
                    )
                }
            }

            // 거절된 제안
            if (rejectedProposals.isNotEmpty()) {
                item {
                    SectionHeader(title = "거절됨 (${rejectedProposals.size})")
                }

                items(
                    items = rejectedProposals,
                    key = { it.id }
                ) { proposal ->
                    ProposalCard(
                        proposal = proposal,
                        onClick = { onProposalClick(proposal) }
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
private fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = title,
        modifier = modifier.padding(vertical = 8.dp),
        style = MaterialTheme.typography.titleSmall.copy(
            fontWeight = FontWeight.Bold
        ),
        color = Color.Black
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F8FA)
@Composable
fun ProposalListPagePreview() {
    Jikgong1111Theme {
        ProposalListPage(
            proposals = listOf(
                Proposal(
                    id = "1",
                    workerId = "worker1",
                    workerName = "김철수",
                    proposedWage = "20만원",
                    message = "프로젝트에 꼭 필요한 인력입니다.",
                    status = ProposalStatus.PENDING,
                    createdAt = LocalDateTime.now().minusHours(2),
                    respondedAt = null,
                    jobTypes = listOf("철근공", "형틀목공"),
                    distance = "2.5km",
                    workerPhone = null,
                    rejectReason = null
                ),
                Proposal(
                    id = "2",
                    workerId = "worker2",
                    workerName = "이영희",
                    proposedWage = "18만원",
                    message = "경력이 풍부하신 분을 찾고 있습니다.",
                    status = ProposalStatus.ACCEPTED,
                    createdAt = LocalDateTime.now().minusDays(1),
                    respondedAt = LocalDateTime.now().minusHours(3),
                    jobTypes = listOf("타일공"),
                    distance = "1.2km",
                    workerPhone = "010-1234-5678",
                    rejectReason = null
                ),
                Proposal(
                    id = "3",
                    workerId = "worker3",
                    workerName = "박민수",
                    proposedWage = "22만원",
                    message = "함께 일하고 싶습니다.",
                    status = ProposalStatus.REJECTED,
                    createdAt = LocalDateTime.now().minusDays(2),
                    respondedAt = LocalDateTime.now().minusDays(1),
                    jobTypes = listOf("전기공"),
                    distance = "3.5km",
                    workerPhone = null,
                    rejectReason = "일정이 맞지 않습니다"
                )
            ),
            isLoading = false,
            onProposalClick = {},
            onRefresh = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F8FA)
@Composable
fun ProposalListPageEmptyPreview() {
    Jikgong1111Theme {
        ProposalListPage(
            proposals = emptyList(),
            isLoading = false,
            onProposalClick = {},
            onRefresh = {}
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F8FA)
@Composable
fun ProposalListPageLoadingPreview() {
    Jikgong1111Theme {
        ProposalListPage(
            proposals = emptyList(),
            isLoading = true,
            onProposalClick = {},
            onRefresh = {}
        )
    }
}