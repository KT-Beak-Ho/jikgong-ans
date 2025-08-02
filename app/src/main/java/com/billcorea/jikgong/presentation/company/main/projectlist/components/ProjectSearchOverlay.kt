package com.billcorea.jikgong.presentation.company.main.projectlist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.appColorScheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectSearchOverlay(
  searchQuery: String,
  suggestions: List<String>,
  onSearchQueryChange: (String) -> Unit,
  onSuggestionClick: (String) -> Unit,
  onCloseSearch: () -> Unit,
  modifier: Modifier = Modifier
) {
  val focusRequester = remember { FocusRequester() }
  val keyboardController = LocalSoftwareKeyboardController.current

  LaunchedEffect(Unit) {
    focusRequester.requestFocus()
  }

  Column(
    modifier = modifier
      .fillMaxWidth()
      .background(
        color = appColorScheme.surface,
        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
      )
      .padding(16.dp)
  ) {
    // 검색 바
    OutlinedTextField(
      value = searchQuery,
      onValueChange = onSearchQueryChange,
      modifier = Modifier
        .fillMaxWidth()
        .focusRequester(focusRequester),
      placeholder = {
        Text(
          text = "프로젝트명, 지역, 업종으로 검색",
          color = appColorScheme.onSurfaceVariant
        )
      },
      leadingIcon = {
        Icon(
          imageVector = Icons.Default.Search,
          contentDescription = "검색",
          tint = appColorScheme.primary
        )
      },
      trailingIcon = {
        Row {
          if (searchQuery.isNotEmpty()) {
            IconButton(
              onClick = { onSearchQueryChange("") }
            ) {
              Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "지우기",
                tint = appColorScheme.onSurfaceVariant
              )
            }
          }
          IconButton(
            onClick = {
              keyboardController?.hide()
              onCloseSearch()
            }
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "닫기",
              tint = appColorScheme.onSurfaceVariant
            )
          }
        }
      },
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = appColorScheme.primary,
        unfocusedBorderColor = appColorScheme.outline
      ),
      shape = RoundedCornerShape(12.dp),
      singleLine = true
    )

    // 검색 제안 목록
    if (suggestions.isNotEmpty() && searchQuery.isNotEmpty()) {
      Spacer(modifier = Modifier.height(12.dp))

      Text(
        text = "추천 검색어",
        style = AppTypography.labelMedium.copy(
          fontWeight = FontWeight.Bold
        ),
        color = appColorScheme.onSurfaceVariant,
        modifier = Modifier.padding(horizontal = 4.dp)
      )

      Spacer(modifier = Modifier.height(8.dp))

      LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.heightIn(max = 200.dp)
      ) {
        items(suggestions) { suggestion ->
          SuggestionItem(
            suggestion = suggestion,
            searchQuery = searchQuery,
            onClick = { onSuggestionClick(suggestion) }
          )
        }
      }
    }

    // 인기 검색어 (검색어가 없을 때)
    if (searchQuery.isEmpty()) {
      Spacer(modifier = Modifier.height(16.dp))

      Text(
        text = "인기 검색어",
        style = AppTypography.labelMedium.copy(
          fontWeight = FontWeight.Bold
        ),
        color = appColorScheme.onSurfaceVariant,
        modifier = Modifier.padding(horizontal = 4.dp)
      )

      Spacer(modifier = Modifier.height(8.dp))

      val popularSearches = listOf(
        "아파트 건설", "도로 공사", "인테리어", "전기 공사",
        "부산", "서울", "인천", "대구"
      )

      LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.heightIn(max = 200.dp)
      ) {
        items(popularSearches.chunked(2)) { rowItems ->
          Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            rowItems.forEach { item ->
              AssistChip(
                onClick = { onSuggestionClick(item) },
                label = { Text(item) },
                leadingIcon = {
                  Icon(
                    imageVector = Icons.Default.TrendingUp,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                  )
                },
                colors = AssistChipDefaults.assistChipColors(
                  containerColor = appColorScheme.primaryContainer,
                  labelColor = appColorScheme.onPrimaryContainer
                ),
                modifier = Modifier.weight(1f)
              )
            }
            // 홀수 개수일 때 남는 공간 채우기
            if (rowItems.size == 1) {
              Spacer(modifier = Modifier.weight(1f))
            }
          }
        }
      }
    }
  }
}

@Composable
private fun SuggestionItem(
  suggestion: String,
  searchQuery: String,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onClick() }
      .clip(RoundedCornerShape(8.dp))
      .padding(horizontal = 12.dp, vertical = 8.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    Icon(
      imageVector = Icons.Default.History,
      contentDescription = null,
      modifier = Modifier.size(16.dp),
      tint = appColorScheme.onSurfaceVariant
    )

    Text(
      text = suggestion,
      style = AppTypography.bodyMedium,
      color = appColorScheme.onSurface,
      modifier = Modifier.weight(1f)
    )

    Icon(
      imageVector = Icons.Default.NorthWest,
      contentDescription = "적용",
      modifier = Modifier.size(14.dp),
      tint = appColorScheme.onSurfaceVariant
    )
  }
}