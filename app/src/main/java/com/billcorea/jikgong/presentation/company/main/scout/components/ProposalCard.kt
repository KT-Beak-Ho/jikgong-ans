package com.billcorea.jikgong.presentation.company.main.scout.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.Proposal
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.ProposalStatus
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.text.NumberFormat
import java.util.*

// 금액에서 숫자 추출 및 형식화 함수
private fun formatProposalWage(wageString: String): String {
    // "일당 150000원", "일당 150000", "150000원" 등에서 숫자를 추출
    val numberRegex = "(\\d+)".toRegex()
    val matchResult = numberRegex.find(wageString)
    
    return if (matchResult != null) {
        val amount = matchResult.value.toIntOrNull() ?: return wageString
        "${amount}원"
    } else {
        wageString // 파싱 실패시 원본 반환
    }
}

@Composable
fun ProposalCard(
  proposal: Proposal,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .clickable { onClick() },
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = Color.White
    ),
    elevation = CardDefaults.cardElevation(
      defaultElevation = 2.dp
    )
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      // 상단: 상태 뱃지와 날짜
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // 상태 뱃지
        StatusBadge(status = proposal.status)

        // 제안 날짜
        Text(
          text = proposal.toDisplayInfo(),
          style = MaterialTheme.typography.bodySmall,
          color = Color.Gray
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      // 노동자 정보
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = proposal.workerName,
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Bold
            )
          )

          Spacer(modifier = Modifier.height(4.dp))

          Text(
            text = proposal.jobTypes.joinToString(" · "),
            style = MaterialTheme.typography.bodyMedium.copy(
              fontWeight = FontWeight.Medium
            ),
            color = Color.Black
          )

          Spacer(modifier = Modifier.height(8.dp))

          // 상세 정보 카드
          Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFFF8F9FA)
          ) {
            Column(
              modifier = Modifier.padding(12.dp),
              verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
              ) {
                InfoItem(
                  icon = "📍",
                  label = "거리",
                  value = proposal.distance,
                  modifier = Modifier.weight(1f)
                )
                InfoItem(
                  icon = "⭐",
                  label = "평점",
                  value = "4.5", // Mock data
                  modifier = Modifier.weight(1f)
                )
              }
              
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
              ) {
                InfoItem(
                  icon = "💼",
                  label = "경력",
                  value = "5년", // Mock data
                  modifier = Modifier.weight(1f)
                )
                InfoItem(
                  icon = "✅",
                  label = "완료",
                  value = "52건", // Mock data
                  modifier = Modifier.weight(1f)
                )
              }
              
              // 제안 금액을 더 눈에 띄게
              Divider(
                modifier = Modifier.padding(vertical = 4.dp),
                color = Color(0xFFE0E0E0),
                thickness = 0.5.dp
              )
              
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "제안 일당",
                  style = MaterialTheme.typography.bodySmall,
                  color = Color.Gray
                )
                Text(
                  text = formatProposalWage(proposal.proposedWage),
                  style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                  ),
                  color = Color(0xFF4B7BFF)
                )
              }
            }
          }
        }
      }

      // 메시지
      if (proposal.message.isNotEmpty()) {
        Spacer(modifier = Modifier.height(12.dp))

        Surface(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(8.dp),
          color = Color(0xFFF5F5F5)
        ) {
          Text(
            text = proposal.message,
            modifier = Modifier.padding(12.dp),
            style = MaterialTheme.typography.bodySmall,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
          )
        }
      }

      // 수락된 경우 연락처 표시
      if (proposal.status == ProposalStatus.ACCEPTED && proposal.workerPhone != null) {
        Spacer(modifier = Modifier.height(12.dp))

        Surface(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable { /* TODO: 전화 걸기 */ },
          color = Color(0xFF4B7BFF).copy(alpha = 0.1f)
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
          ) {
            Icon(
              imageVector = Icons.Default.Phone,
              contentDescription = "전화",
              modifier = Modifier.size(16.dp),
              tint = Color(0xFF4B7BFF)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = proposal.workerPhone,
              style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
              ),
              color = Color(0xFF4B7BFF)
            )
          }
        }
      }

      // 응답 시간 (수락/거절된 경우)
      if (proposal.respondedAt != null) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "응답: ${proposal.respondedAt.format(DateTimeFormatter.ofPattern("MM월 dd일 HH:mm"))}",
          style = MaterialTheme.typography.labelSmall,
          color = Color.Gray
        )
      }

      // 거절 사유 (거절된 경우)
      if (proposal.status == ProposalStatus.REJECTED && proposal.rejectReason != null) {
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(8.dp),
          color = Color(0xFFFFEBEE)
        ) {
          Text(
            text = "거절 사유: ${proposal.rejectReason}",
            modifier = Modifier.padding(12.dp),
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFFC62828)
          )
        }
      }
    }
  }
}

@Composable
private fun InfoItem(
  icon: String,
  label: String,
  value: String,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(4.dp)
  ) {
    Text(
      text = icon,
      style = MaterialTheme.typography.bodySmall
    )
    Column {
      Text(
        text = label,
        style = MaterialTheme.typography.labelSmall,
        color = Color.Gray
      )
      Text(
        text = value,
        style = MaterialTheme.typography.bodySmall.copy(
          fontWeight = FontWeight.Medium
        ),
        color = Color.Black
      )
    }
  }
}

@Composable
private fun StatusBadge(status: ProposalStatus) {
  val (backgroundColor, textColor, text) = when (status) {
    ProposalStatus.PENDING -> Triple(
      Color(0xFFFFF3E0),
      Color(0xFFFF6F00),
      "대기중"
    )
    ProposalStatus.ACCEPTED -> Triple(
      Color(0xFFE8F5E9),
      Color(0xFF2E7D32),
      "수락됨"
    )
    ProposalStatus.REJECTED -> Triple(
      Color(0xFFFFEBEE),
      Color(0xFFC62828),
      "거절됨"
    )
  }

  Surface(
    shape = RoundedCornerShape(12.dp),
    color = backgroundColor
  ) {
    Text(
      text = text,
      modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
      style = MaterialTheme.typography.labelSmall.copy(
        fontWeight = FontWeight.Bold
      ),
      color = textColor
    )
  }
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F8FA)
@Composable
fun ProposalCardPendingPreview() {
  Jikgong1111Theme {
    ProposalCard(
      proposal = Proposal(
        id = "1",
        workerId = "worker1",
        workerName = "김철수",
        proposedWage = "일당 20만원",
        message = "프로젝트에 꼭 필요한 인력입니다. 좋은 조건으로 모시고 싶습니다.",
        status = ProposalStatus.PENDING,
        createdAt = LocalDateTime.now().minusHours(2),
        respondedAt = null,
        jobTypes = listOf("철근공", "형틀목공"),
        distance = "2.5km",
        workerPhone = null,
        rejectReason = null
      ),
      onClick = {},
      modifier = Modifier.padding(16.dp)
    )
  }
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F8FA)
@Composable
fun ProposalCardAcceptedPreview() {
  Jikgong1111Theme {
    ProposalCard(
      proposal = Proposal(
        id = "2",
        workerId = "worker2",
        workerName = "이영희",
        proposedWage = "일당 18만원",
        message = "경력이 풍부하신 분을 찾고 있습니다.",
        status = ProposalStatus.ACCEPTED,
        createdAt = LocalDateTime.now().minusDays(1),
        respondedAt = LocalDateTime.now().minusHours(3),
        jobTypes = listOf("타일공"),
        distance = "1.2km",
        workerPhone = "010-1234-5678",
        rejectReason = null
      ),
      onClick = {},
      modifier = Modifier.padding(16.dp)
    )
  }
}

@Preview(showBackground = true, backgroundColor = 0xFFF7F8FA)
@Composable
fun ProposalCardRejectedPreview() {
  Jikgong1111Theme {
    ProposalCard(
      proposal = Proposal(
        id = "3",
        workerId = "worker3",
        workerName = "박민수",
        proposedWage = "일당 15만원",
        message = "함께 일하고 싶습니다.",
        status = ProposalStatus.REJECTED,
        createdAt = LocalDateTime.now().minusDays(2),
        respondedAt = LocalDateTime.now().minusDays(1),
        jobTypes = listOf("전기공"),
        distance = "3.5km",
        workerPhone = null,
        rejectReason = "일정이 맞지 않습니다"
      ),
      onClick = {},
      modifier = Modifier.padding(16.dp)
    )
  }
}