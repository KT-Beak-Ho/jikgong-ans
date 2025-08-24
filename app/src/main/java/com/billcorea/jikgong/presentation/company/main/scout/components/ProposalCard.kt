package com.billcorea.jikgong.presentation.company.main.scout.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
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
import com.billcorea.jikgong.presentation.company.main.scout.data.Proposal
import com.billcorea.jikgong.presentation.company.main.scout.data.ProposalStatus
import com.billcorea.jikgong.presentation.company.main.scout.data.toDisplayInfo
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.Duration

@Composable
fun ProposalCard(
  modifier: Modifier = Modifier,
  proposal: Proposal,
  onCardClick: () -> Unit
) {
  val displayInfo = proposal.status.toDisplayInfo()

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
            // 상태 아이콘
            Text(
              text = displayInfo.icon,
              fontSize = 20.sp
            )

            // 이름
            Text(
              text = proposal.workerName,
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
              )
            )

            // 상태 뱃지
            Surface(
              shape = RoundedCornerShape(4.dp),
              color = Color(displayInfo.color).copy(alpha = 0.1f)
            ) {
              Text(
                text = displayInfo.text,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                style = MaterialTheme.typography.labelSmall,
                color = Color(displayInfo.color)
              )
            }
          }

          Spacer(modifier = Modifier.height(4.dp))

          // 직종
          Text(
            text = proposal.jobTypes.joinToString(" · "),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black
          )

          Spacer(modifier = Modifier.height(4.dp))

          // 제안 시간
          Text(
            text = getTimeAgo(proposal.createdAt),
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
          )
        }

        // 오른쪽: 제안 임금
        Column(
          horizontalAlignment = Alignment.End
        ) {
          Text(
            text = proposal.proposedWage,
            style = MaterialTheme.typography.bodyMedium.copy(
              fontWeight = FontWeight.Bold
            ),
            color = Color(0xFF4B7BFF)
          )

          Text(
            text = "📍 ${proposal.distance}km",
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray
          )
        }
      }

      // 메시지
      Spacer(modifier = Modifier.height(12.dp))
      Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFF5F5F5)
      ) {
        Text(
          text = proposal.message,
          modifier = Modifier.padding(12.dp),
          style = MaterialTheme.typography.bodySmall,
          color = Color.Gray,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis
        )
      }

      // 수락된 경우 연락처 표시
      if (proposal.status == ProposalStatus.ACCEPTED && proposal.workerPhone != null) {
        Spacer(modifier = Modifier.height(12.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          // 연락처
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFF66BB6A).copy(alpha = 0.1f)
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Phone,
                contentDescription = "전화",
                modifier = Modifier.size(16.dp),
                tint = Color(0xFF66BB6A)
              )
              Text(
                text = proposal.workerPhone,
                style = MaterialTheme.typography.bodyMedium.copy(
                  fontWeight = FontWeight.Medium
                ),
                color = Color(0xFF66BB6A)
              )
            }
          }

          // 응답 시간
          proposal.respondedAt?.let { respondedAt ->
            Text(
              text = "응답: ${getTimeAgo(respondedAt)}",
              style = MaterialTheme.typography.labelSmall,
              color = Color.Gray
            )
          }
        }
      }

      // 거절된 경우 사유 표시
      if (proposal.status == ProposalStatus.REJECTED && proposal.rejectReason != null) {
        Spacer(modifier = Modifier.height(12.dp))
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = Color(0xFFEF5350).copy(alpha = 0.1f)
        ) {
          Text(
            text = "거절 사유: ${proposal.rejectReason}",
            modifier = Modifier.padding(12.dp),
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFFEF5350)
          )
        }
      }
    }
  }
}

// 시간 계산 함수
private fun getTimeAgo(dateTime: LocalDateTime): String {
  val now = LocalDateTime.now()
  val duration = Duration.between(dateTime, now)

  return when {
    duration.toMinutes() < 1 -> "방금 전"
    duration.toMinutes() < 60 -> "${duration.toMinutes()}분 전"
    duration.toHours() < 24 -> "${duration.toHours()}시간 전"
    duration.toDays() < 7 -> "${duration.toDays()}일 전"
    else -> dateTime.format(DateTimeFormatter.ofPattern("MM월 dd일"))
  }
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F8FA)
@Composable
fun ProposalCardPreview() {
  Jikgong1111Theme {
    Column(
      modifier = Modifier.padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      // 수락된 제안
      ProposalCard(
        proposal = Proposal(
          id = "1",
          workerId = "1",
          workerName = "김철수",
          proposedWage = "일당 18만원",
          message = "현재 진행 중인 강남구 프로젝트에 참여 부탁드립니다. 경력자 우대합니다.",
          status = ProposalStatus.ACCEPTED,
          createdAt = LocalDateTime.now().minusHours(2),
          respondedAt = LocalDateTime.now().minusHours(1),
          jobTypes = listOf("철근공", "형틀목공"),
          distance = 0.8,
          workerPhone = "010-1234-5678"
        ),
        onCardClick = {}
      )

      // 대기중인 제안
      ProposalCard(
        proposal = Proposal(
          id = "2",
          workerId = "2",
          workerName = "이영희",
          proposedWage = "일당 15만원",
          message = "다음 주 월요일부터 시작하는 프로젝트입니다. 숙련된 타일공을 찾고 있습니다.",
          status = ProposalStatus.PENDING,
          createdAt = LocalDateTime.now().minusMinutes(30),
          jobTypes = listOf("타일공"),
          distance = 1.2
        ),
        onCardClick = {}
      )

      // 거절된 제안
      ProposalCard(
        proposal = Proposal(
          id = "3",
          workerId = "3",
          workerName = "박민수",
          proposedWage = "일당 20만원",
          message = "긴급 프로젝트입니다. 전기 작업 경험이 풍부한 분을 모십니다.",
          status = ProposalStatus.REJECTED,
          createdAt = LocalDateTime.now().minusDays(1),
          respondedAt = LocalDateTime.now().minusHours(12),
          jobTypes = listOf("전기공"),
          distance = 2.5,
          rejectReason = "일정이 맞지 않습니다"
        ),
        onCardClick = {}
      )
    }
  }
}