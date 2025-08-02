// ========================================
// 📄 common/components/ScrollBar.kt (공통 컴포넌트)
// ========================================
package com.billcorea.jikgong.presentation.company.main.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.ui.theme.appColorScheme

@Composable
fun ScrollBar(
  scrollProgress: Float,
  modifier: Modifier = Modifier,
  isVisible: Boolean = true
) {
  if (isVisible && scrollProgress >= 0f && scrollProgress <= 1f) {
    Box(
      modifier = modifier
        .alpha(if (scrollProgress > 0f || scrollProgress < 1f) 0.8f else 0.4f)
    ) {
      // 스크롤바 트랙
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(
            color = appColorScheme.outline.copy(alpha = 0.2f),
            shape = RoundedCornerShape(4.dp)
          )
      )

      // 스크롤바 썸
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .fillMaxHeight(0.2f) // 썸의 높이 (전체의 20%)
          .offset(y = (scrollProgress * 80).dp) // 80% = 100% - 20%
          .background(
            color = appColorScheme.primary.copy(alpha = 0.8f),
            shape = RoundedCornerShape(4.dp)
          )
      )
    }
  }
}