package com.billcorea.jikgong.presentation.company.main.scout.presentation.component

import androidx.compose.foundation.background
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
    return when {
        // "20만원" 형식 처리
        wageString.contains("만원") -> {
            val numberRegex = "(\\d+)만원".toRegex()
            val matchResult = numberRegex.find(wageString)
            if (matchResult != null) {
                val amount = matchResult.groupValues[1].toIntOrNull()
                if (amount != null) {
                    "${NumberFormat.getNumberInstance(Locale.KOREA).format(amount * 10000)}원"
                } else {
                    wageString
                }
            } else {
                wageString
            }
        }
        // "200000원" 형식 처리 (6자리 이상)
        wageString.contains("원") -> {
            val numberRegex = "(\\d{6,})원".toRegex()
            val matchResult = numberRegex.find(wageString)
            if (matchResult != null) {
                val amount = matchResult.groupValues[1].toIntOrNull()
                if (amount != null) {
                    "${NumberFormat.getNumberInstance(Locale.KOREA).format(amount)}원"
                } else {
                    wageString
                }
            } else {
                wageString
            }
        }
        // 기타 경우 원본 반환
        else -> wageString
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
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(
      containerColor = Color.White
    ),
    elevation = CardDefaults.cardElevation(
      defaultElevation = 1.dp
    )
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp)
    ) {
      // 상단: 이름, 업종, 상태를 한 줄에
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
          // 이름 (더 큰 폰트)
          Text(
            text = proposal.workerName,
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.ExtraBold
            ),
            color = Color.Black
          )
          
          // 업종 뱃지들 (눈에 띄게)
          proposal.jobTypes.take(2).forEach { jobType ->
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

        StatusBadge(status = proposal.status)
      }

      Spacer(modifier = Modifier.height(8.dp))

      // 핵심 정보를 컴팩트하게 배치
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // 왼쪽: 거리, 평점, 경력
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
              text = proposal.distance,
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
              text = "4.5",
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
              text = "5년", // Mock data - 실제로는 proposal에서 가져와야 함
              style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
              ),
              color = Color(0xFF666666)
            )
          }
        }

        // 오른쪽: 제안 금액 (매우 강조)
        Surface(
          shape = RoundedCornerShape(8.dp),
          color = Color(0xFF4B7BFF)
        ) {
          Text(
            text = formatProposalWage(proposal.proposedWage),
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.ExtraBold
            ),
            color = Color.White
          )
        }
      }

      // 날짜 정보 (컴팩트하게)
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = proposal.toDisplayInfo(),
        style = MaterialTheme.typography.labelMedium,
        color = Color(0xFF888888)
      )

      // 메시지 (더 컴팩트하게)
      if (proposal.message.isNotEmpty()) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "💬 ${proposal.message}",
          style = MaterialTheme.typography.bodySmall.copy(
            fontWeight = FontWeight.Medium
          ),
          color = Color(0xFF555555),
          maxLines = 2,
          overflow = TextOverflow.Ellipsis
        )
      }

      // 수락된 경우 연락처 표시 (인라인으로)
      if (proposal.status == ProposalStatus.ACCEPTED && proposal.workerPhone != null) {
        Spacer(modifier = Modifier.height(6.dp))
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .clickable { /* TODO: 전화 걸기 */ }
            .background(Color(0xFF4B7BFF).copy(alpha = 0.1f))
            .padding(8.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Phone,
            contentDescription = "전화",
            modifier = Modifier.size(14.dp),
            tint = Color(0xFF4B7BFF)
          )
          Text(
            text = proposal.workerPhone,
            style = MaterialTheme.typography.bodySmall.copy(
              fontWeight = FontWeight.Bold
            ),
            color = Color(0xFF4B7BFF)
          )
        }
      }

      // 거절 사유 (인라인으로, 더 눈에 띄게)
      if (proposal.status == ProposalStatus.REJECTED && proposal.rejectReason != null) {
        Spacer(modifier = Modifier.height(6.dp))
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Text(
            text = "❌ ${proposal.rejectReason}",
            style = MaterialTheme.typography.bodySmall.copy(
              fontWeight = FontWeight.Medium
            ),
            color = Color(0xFFE53E3E)
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
    horizontalArrangement = Arrangement.spacedBy(6.dp)
  ) {
    Text(
      text = icon,
      style = MaterialTheme.typography.bodyMedium
    )
    Column {
      Text(
        text = label,
        style = MaterialTheme.typography.labelMedium,
        color = Color.Gray
      )
      Text(
        text = value,
        style = MaterialTheme.typography.bodyMedium.copy(
          fontWeight = FontWeight.Bold
        ),
        color = Color.Black
      )
    }
  }
}

@Composable
private fun StatusBadge(status: ProposalStatus) {
  val (backgroundColor, textColor, text, icon) = when (status) {
    ProposalStatus.PENDING -> Quadruple(
      Color(0xFFFF6F00),
      Color.White,
      "대기중",
      "⏳"
    )
    ProposalStatus.ACCEPTED -> Quadruple(
      Color(0xFF00C853),
      Color.White,
      "수락",
      "✅"
    )
    ProposalStatus.REJECTED -> Quadruple(
      Color(0xFFE53E3E),
      Color.White,
      "거절",
      "❌"
    )
  }

  Surface(
    shape = RoundedCornerShape(16.dp),
    color = backgroundColor
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
      Text(
        text = icon,
        style = MaterialTheme.typography.labelSmall
      )
      Text(
        text = text,
        style = MaterialTheme.typography.labelMedium.copy(
          fontWeight = FontWeight.ExtraBold
        ),
        color = textColor
      )
    }
  }
}

// Helper data class for 4 values
private data class Quadruple<A, B, C, D>(
  val first: A,
  val second: B,
  val third: C,
  val fourth: D
)

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