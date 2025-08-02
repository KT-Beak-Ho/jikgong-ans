// ========================================
// 📄 components/ProjectListEmptyState.kt
// ========================================
package com.billcorea.jikgong.presentation.company.main.projectlist.components

import androidx.compose.foundation.layout.*
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

/**
 * 프로젝트 목록 빈 상태 화면
 */
@Composable
fun ProjectListEmptyState(
  isSearching: Boolean,
  isFiltered: Boolean,
  onCreateProject: () -> Unit,
  onClearFilters: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(32.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.Center
  ) {
    // 아이콘
    Icon(
      imageVector = when {
        isSearching -> Icons.Default.SearchOff
        isFiltered -> Icons.Default.FilterListOff
        else -> Icons.Default.Assignment
      },
      contentDescription = null,
      modifier = Modifier.size(80.dp),
      tint = appColorScheme.onSurfaceVariant.copy(alpha = 0.6f)
    )

    Spacer(modifier = Modifier.height(16.dp))

    // 제목
    Text(
      text = when {
        isSearching -> "검색 결과가 없습니다"
        isFiltered -> "해당 조건의 프로젝트가 없습니다"
        else -> "등록된 프로젝트가 없습니다"
      },
      style = AppTypography.titleLarge.copy(
        fontWeight = FontWeight.Bold
      ),
      color = appColorScheme.onSurface,
      textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(8.dp))

    // 설명
    Text(
      text = when {
        isSearching -> "다른 키워드로 검색해 보세요"
        isFiltered -> "필터 조건을 변경하거나 초기화해 보세요"
        else -> "새로운 프로젝트를 등록하여\n인력 모집을 시작해 보세요"
      },
      style = AppTypography.bodyLarge,
      color = appColorScheme.onSurfaceVariant,
      textAlign = TextAlign.Center
    )

    Spacer(modifier = Modifier.height(24.dp))

    // 액션 버튼
    when {
      isFiltered -> {
        OutlinedButton(
          onClick = onClearFilters,
          modifier = Modifier.fillMaxWidth(0.6f)
        ) {
          Icon(
            imageVector = Icons.Default.FilterListOff,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text("필터 초기화")
        }
      }
      !isSearching -> {
        Button(
          onClick = onCreateProject,
          modifier = Modifier.fillMaxWidth(0.6f),
          colors = ButtonDefaults.buttonColors(
            containerColor = appColorScheme.primary
          )
        ) {
          Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text("프로젝트 등록")
        }
      }
    }
  }
}

// ========================================
// 📄 components/ProjectListLoadingState.kt
// ========================================
package com.billcorea.jikgong.presentation.company.main.projectlist.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.ui.theme.appColorScheme

/**
 * 프로젝트 목록 로딩 상태
 */
@Composable
fun ProjectListLoadingState(
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    repeat(3) {
      ProjectCardSkeleton()
    }
  }
}

@Composable
private fun ProjectCardSkeleton() {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(
      containerColor = appColorScheme.surface
    )
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      // 제목 스켈레톤
      Box(
        modifier = Modifier
          .fillMaxWidth(0.8f)
          .height(20.dp)
          .clip(RoundedCornerShape(4.dp))
          .shimmerEffect()
      )

      // 위치 스켈레톤
      Box(
        modifier = Modifier
          .fillMaxWidth(0.4f)
          .height(16.dp)
          .clip(RoundedCornerShape(4.dp))
          .shimmerEffect()
      )

      Spacer(modifier = Modifier.height(8.dp))

      // 정보 스켈레톤
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Box(
          modifier = Modifier
            .width(80.dp)
            .height(16.dp)
            .clip(RoundedCornerShape(4.dp))
            .shimmerEffect()
        )
        Box(
          modifier = Modifier
            .width(60.dp)
            .height(16.dp)
            .clip(RoundedCornerShape(4.dp))
            .shimmerEffect()
        )
      }

      // 임금 스켈레톤
      Box(
        modifier = Modifier
          .fillMaxWidth(0.6f)
          .height(24.dp)
          .clip(RoundedCornerShape(4.dp))
          .shimmerEffect()
      )
    }
  }
}

@Composable
private fun Modifier.shimmerEffect(): Modifier {
  return this.then(
    Modifier.placeholder(
      visible = true,
      color = appColorScheme.surfaceVariant.copy(alpha = 0.3f),
      highlight = PlaceholderHighlight.shimmer(
        highlightColor = appColorScheme.surface
      )
    )
  )
}

// ========================================
// 📄 components/CustomScrollBar.kt
// ========================================
package com.billcorea.jikgong.presentation.company.main.projectlist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.ui.theme.appColorScheme

/**
 * 커스텀 스크롤바 컴포넌트
 */
@Composable
fun CustomScrollBar(
  scrollProgress: Float,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(4.dp))
      .background(appColorScheme.outline.copy(alpha = 0.2f))
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(scrollProgress.coerceIn(0.1f, 1f))
        .clip(RoundedCornerShape(4.dp))
        .background(appColorScheme.primary.copy(alpha = 0.8f))
    )
  }
}

// ========================================
// 📄 components/ProjectListHeader.kt
// ========================================
package com.billcorea.jikgong.presentation.company.main.projectlist.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.appColorScheme

/**
 * 프로젝트 목록 헤더
 */
@Composable
fun ProjectListHeader(
  totalCount: Int,
  isSearching: Boolean,
  onSearchClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column {
      Text(
        text = if (isSearching) "검색 결과" else "프로젝트 목록",
        style = AppTypography.titleLarge.copy(
          fontWeight = FontWeight.Bold
        ),
        color = appColorScheme.onSurface
      )
      if (totalCount > 0) {
        Text(
          text = "총 ${totalCount}건",
          style = AppTypography.bodyMedium,
          color = appColorScheme.onSurfaceVariant
        )
      }
    }

    IconButton(
      onClick = onSearchClick
    ) {
      Icon(
        imageVector = Icons.Default.Search,
        contentDescription = "검색",
        tint = appColorScheme.onSurface
      )
    }
  }
}