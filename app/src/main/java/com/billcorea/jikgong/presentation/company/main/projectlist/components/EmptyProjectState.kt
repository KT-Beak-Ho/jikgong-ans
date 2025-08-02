package com.billcorea.jikgong.presentation.company.main.projectlist.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.appColorScheme

@Composable
fun EmptyProjectState(
  onCreateProjectClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier.fillMaxSize(),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center,
      modifier = Modifier.padding(horizontal = 24.dp)
    ) {
      // 메인 아이콘
      Surface(
        modifier = Modifier.size(120.dp),
        shape = RoundedCornerShape(60.dp),
        color = appColorScheme.primaryContainer.copy(alpha = 0.3f)
      ) {
        Icon(
          imageVector = Icons.Default.Work,
          contentDescription = "프로젝트",
          modifier = Modifier
            .size(60.dp)
            .padding(30.dp),
          tint = appColorScheme.primary
        )
      }

      Spacer(modifier = Modifier.height(32.dp))

      // 메인 메시지
      Text(
        text = "등록된 프로젝트가 없습니다",
        style = AppTypography.headlineSmall.copy(fontWeight = FontWeight.Bold),
        color = appColorScheme.onSurface,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(12.dp))

      // 부제목
      Text(
        text = "첫 번째 프로젝트를 등록하고\n우수한 인력을 모집해보세요",
        style = AppTypography.bodyLarge,
        color = appColorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        lineHeight = AppTypography.bodyLarge.lineHeight
      )

      Spacer(modifier = Modifier.height(32.dp))

      // 프로젝트 생성 버튼
      Button(
        onClick = onCreateProjectClick,
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = appColorScheme.primary
        )
      ) {
        Icon(
          imageVector = Icons.Default.Add,
          contentDescription = "추가",
          modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "프로젝트 등록하기",
          style = AppTypography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
        )
      }

      Spacer(modifier = Modifier.height(24.dp))

      // 도움말 카드
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
          containerColor = appColorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
      ) {
        Column(
          modifier = Modifier.padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Text(
            text = "💡 프로젝트 등록 시 혜택",
            style = AppTypography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = appColorScheme.onSurface
          )

          BenefitItem("검증된 인력 추천")
          BenefitItem("투명한 계약 관리")
          BenefitItem("안전한 임금 결제")
          BenefitItem("실시간 현장 관리")
        }
      }
    }
  }
}

@Composable
private fun BenefitItem(text: String) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    Icon(
      imageVector = Icons.Default.Check,
      contentDescription = "체크",
      modifier = Modifier.size(16.dp),
      tint = appColorScheme.primary
    )
    Text(
      text = text,
      style = AppTypography.bodyMedium,
      color = appColorScheme.onSurfaceVariant
    )
  }
}
