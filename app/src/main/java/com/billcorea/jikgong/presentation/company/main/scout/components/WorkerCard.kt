package com.billcorea.jikgong.presentation.company.main.scout.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.billcorea.jikgong.presentation.company.main.scout.data.Worker
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
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // 상단 정보
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // 왼쪽: 노동자 정보
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // 이름
                        Text(
                            text = worker.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        )

                        // 가능 여부 뱃지
                        if (worker.isAvailable) {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFF4B7BFF).copy(alpha = 0.1f)
                            ) {
                                Text(
                                    text = "가능",
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF4B7BFF)
                                )
                            }
                        } else {
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color.Gray.copy(alpha = 0.1f)
                            ) {
                                Text(
                                    text = "진행중",
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.Gray
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // 직종
                    Text(
                        text = worker.jobTypes.joinToString(" · "),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // 거리와 경력
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "📍 ${worker.distance}km",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                        Text(
                            text = "경력 ${worker.experience}년",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }

                // 오른쪽: 평점
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "⭐",
                            fontSize = 14.sp
                        )
                        Text(
                            text = "${worker.rating}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    if (worker.completedProjects > 0) {
                        Text(
                            text = "${worker.completedProjects}건 완료",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                    }
                }
            }

            // 자기소개 (있을 경우)
            worker.introduction?.let { intro ->
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = intro,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // 희망 일당
            worker.desiredWage?.let { wage ->
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFF5F5F5)
                ) {
                    Text(
                        text = "💰 $wage",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 스카웃하기 버튼
            Button(
                onClick = onScoutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4B7BFF),
                    disabledContainerColor = Color.Gray
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = worker.isAvailable
            ) {
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