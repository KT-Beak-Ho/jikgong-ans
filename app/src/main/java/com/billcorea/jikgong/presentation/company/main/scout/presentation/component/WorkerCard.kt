package com.billcorea.jikgong.presentation.company.main.scout.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.Worker
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme

@Composable
fun WorkerCard(
    modifier: Modifier = Modifier,
    worker: Worker,
    onCardClick: () -> Unit,
    onScoutClick: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCardClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // 상단: 이름, 업종, 상태를 한 줄에 (ProposalCard와 동일한 구조)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // 이름 (더 큰 폰트로 강조)
                    Text(
                        text = worker.name,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold
                        ),
                        color = Color.Black
                    )
                    
                    // 업종 뱃지들 (눈에 띄게)
                    worker.jobTypes.take(2).forEach { jobType ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF4B7BFF).copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = jobType,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color(0xFF4B7BFF)
                            )
                        }
                    }
                }

                // 상태 뱃지 (ProposalCard와 유사한 스타일)
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (worker.isAvailable) Color(0xFF00C853) else Color(0xFFFF6F00)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(
                            text = if (worker.isAvailable) "✅" else "⏳",
                            style = MaterialTheme.typography.labelSmall
                        )
                        Text(
                            text = if (worker.isAvailable) "가능" else "진행중",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.ExtraBold
                            ),
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 핵심 정보를 컴팩트하게 배치 (ProposalCard와 동일한 구조)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 왼쪽: 거리, 경력, 평점
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "📍",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${worker.distance}km",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = Color(0xFF666666)
                        )
                    }
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "💼",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${worker.experience}년",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = Color(0xFF666666)
                        )
                    }
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "⭐",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "${worker.rating}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = Color(0xFF666666)
                        )
                    }
                }

                // 오른쪽: 희망 임금 (강조)
                worker.desiredWage?.let { wage ->
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF4B7BFF)
                    ) {
                        Text(
                            text = wage,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold
                            ),
                            color = Color.White
                        )
                    }
                }
            }

            // 완료 프로젝트 수 (컴팩트하게)
            if (worker.completedProjects > 0) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "✅ ${worker.completedProjects}건 완료",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF888888)
                )
            }

            // 자기소개 (더 컴팩트하게)
            worker.introduction?.let { intro ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "💬 $intro",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = Color(0xFF555555),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 스카웃하기 버튼 (더 현대적으로)
            Button(
                onClick = onScoutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (worker.isAvailable) Color(0xFF4B7BFF) else Color(0xFFE0E0E0),
                    contentColor = if (worker.isAvailable) Color.White else Color(0xFF888888)
                ),
                shape = RoundedCornerShape(8.dp),
                enabled = worker.isAvailable
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (worker.isAvailable) "🎯" else "⏳",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = if (worker.isAvailable) "스카웃하기" else "진행중",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

@Preview(name = "작업자 카드 - 일반", showBackground = true, backgroundColor = 0xFFF7F8FA)
@Composable
fun WorkerCardPreview() {
    Jikgong1111Theme {
        WorkerCard(
            worker = Worker(
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
            onCardClick = {},
            onScoutClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(name = "작업자 카드 - 진행중", showBackground = true, backgroundColor = 0xFFF7F8FA)
@Composable
fun WorkerCardUnavailablePreview() {
    Jikgong1111Theme {
        WorkerCard(
            worker = Worker(
                id = "2",
                name = "박민수",
                jobTypes = listOf("전기공", "배관공"),
                experience = 8,
                distance = 2.5,
                rating = 4.9f,
                introduction = "다년간의 경험으로 신속 정확한 작업 보장합니다.",
                desiredWage = "일당 20만원",
                isAvailable = false, // 진행중 상태
                completedProjects = 103
            ),
            onCardClick = {},
            onScoutClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(name = "작업자 카드 - 희망임금 협의", showBackground = true, backgroundColor = 0xFFF7F8FA)
@Composable
fun WorkerCardNegotiablePreview() {
    Jikgong1111Theme {
        WorkerCard(
            worker = Worker(
                id = "3",
                name = "정수진",
                jobTypes = listOf("도장공"),
                experience = 2,
                distance = 1.8,
                rating = 4.3f,
                introduction = "꼼꼼한 작업으로 만족도 높은 결과물을 제공합니다.",
                desiredWage = "협의 가능", // 협의 가능
                isAvailable = true,
                completedProjects = 15
            ),
            onCardClick = {},
            onScoutClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}