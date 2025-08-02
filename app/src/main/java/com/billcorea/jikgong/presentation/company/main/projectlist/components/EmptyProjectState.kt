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
fun ProjectListEmptyState(
  isSearching: Boolean,
  isFiltered: Boolean,
  onCreateProject: () -> Unit,
  onClearFilters: () -> Unit,
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
      // 아이콘
      Surface(
        modifier = Modifier.size(120.dp),
        shape = RoundedCornerShape(60.dp),
        color = appColorScheme.primaryContainer.copy(alpha = 0.3f)
      ) {
        Icon(
          imageVector = when {
            isSearching -> Icons.Default.SearchOff
            isFiltered -> Icons.Default.FilterList
            else -> Icons.Default.Work
          },
          contentDescription = null,
          modifier = Modifier
            .size(60.dp)
            .padding(30.dp),
          tint = appColorScheme.primary
        )
      }

      Spacer(modifier = Modifier.height(32.dp))

      // 메인 메시지
      Text(
        text = when {
          isSearching -> "검색 결과가 없습니다"
          isFiltered -> "해당 조건의 프로젝트가 없습니다"
          else -> "등록된 프로젝트가 없습니다"
        },
        style = AppTypography.headlineSmall.copy(
          fontWeight = FontWeight.Bold
        ),
        color = appColorScheme.onSurface,
        textAlign = TextAlign.Center
      )

      Spacer(modifier = Modifier.height(12.dp))

      // 서브 메시지
      Text(
        text = when {
          isSearching -> "다른 검색어로 다시 시도해보세요"
          isFiltered -> "필터 조건을 변경하거나 초기화해보세요"
          else -> "첫 번째 건설 프로젝트를\n등록해보세요"
        },
        style = AppTypography.bodyLarge,
        color = appColorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        lineHeight = AppTypography.bodyLarge.lineHeight
      )

      Spacer(modifier = Modifier.height(32.dp))

      // 혜택 카드들 (프로젝트가 없을 때만 표시)
      if (!isSearching && !isFiltered) {
        Column(
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          BenefitCard(
            icon = Icons.Default.Security,
            title = "안전한 근무환경",
            description = "AI 기반 실시간 안전 모니터링"
          )
          BenefitCard(
            icon = Icons.Default.Payment,
            title = "투명한 임금 정산",
            description = "블록체인 기반 즉시 정산 시스템"
          )
          BenefitCard(
            icon = Icons.Default.Group,
            title = "검증된 인력 매칭",
            description = "숙련도별 맞춤 인력 추천"
          )
        }

        Spacer(modifier = Modifier.height(32.dp))
      }

      // 액션 버튼
      Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        if (isFiltered) {
          OutlinedButton(
            onClick = onClearFilters,
            modifier = Modifier.height(48.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Clear,
              contentDescription = null,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("필터 초기화")
          }
        }

        if (!isSearching) {
          Button(
            onClick = onCreateProject,
            modifier = Modifier.height(48.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = appColorScheme.primary,
              contentColor = appColorScheme.onPrimary
            )
          ) {
            Icon(
              imageVector = Icons.Default.Add,
              contentDescription = null,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("프로젝트 등록")
          }
        }
      }
    }
  }
}

@Composable
private fun BenefitCard(
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  title: String,
  description: String,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier.fillMaxWidth(),
    colors = CardDefaults.cardColors(
      containerColor = appColorScheme.primaryContainer.copy(alpha = 0.1f)
    ),
    shape = RoundedCornerShape(12.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        modifier = Modifier.size(24.dp),
        tint = appColorScheme.primary
      )
      Column {
        Text(
          text = title,
          style = AppTypography.titleSmall.copy(
            fontWeight = FontWeight.Bold
          ),
          color = appColorScheme.onSurface
        )
        Text(
          text = description,
          style = AppTypography.bodySmall,
          color = appColorScheme.onSurfaceVariant
        )
      }
    }
  }
}