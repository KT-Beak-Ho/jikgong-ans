// ========================================
// 📄 components/ProjectSearchOverlay.kt
// ========================================
package com.billcorea.jikgong.presentation.company.main.projectlist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.appColorScheme

/**
 * 프로젝트 검색 오버레이
 */
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
  val focusManager = LocalFocusManager.current

  LaunchedEffect(Unit) {
    focusRequester.requestFocus()
  }

  Dialog(
    onDismissRequest = onCloseSearch,
    properties = DialogProperties(
      usePlatformDefaultWidth = false,
      decorFitsSystemWindows = false
    )
  ) {
    Column(
      modifier = modifier
        .fillMaxSize()
        .background(appColorScheme.surface)
        .padding(16.dp)
    ) {
      // 검색 입력창
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = onCloseSearch
        ) {
          Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "닫기",
            tint = appColorScheme.onSurface
          )
        }

        OutlinedTextField(
          value = searchQuery,
          onValueChange = onSearchQueryChange,
          modifier = Modifier
            .weight(1f)
            .focusRequester(focusRequester),
          placeholder = { Text("프로젝트 검색...") },
          leadingIcon = {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = "검색"
            )
          },
          trailingIcon = {
            if (searchQuery.isNotEmpty()) {
              IconButton(
                onClick = { onSearchQueryChange("") }
              ) {
                Icon(
                  imageVector = Icons.Default.Clear,
                  contentDescription = "지우기"
                )
              }
            }
          },
          keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
          ),
          keyboardActions = KeyboardActions(
            onSearch = { focusManager.clearFocus() }
          ),
          singleLine = true
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // 검색 제안어
      if (suggestions.isNotEmpty() && searchQuery.isEmpty()) {
        Text(
          text = "추천 검색어",
          style = AppTypography.titleMedium,
          color = appColorScheme.onSurface,
          modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyColumn(
          verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          items(suggestions) { suggestion ->
            SearchSuggestionItem(
              suggestion = suggestion,
              onClick = { onSuggestionClick(suggestion) }
            )
          }
        }
      }
    }
  }
}

@Composable
private fun SearchSuggestionItem(
  suggestion: String,
  onClick: () -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(8.dp))
      .clickable { onClick() }
      .padding(12.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      imageVector = Icons.Default.Search,
      contentDescription = null,
      tint = appColorScheme.onSurfaceVariant,
      modifier = Modifier.size(20.dp)
    )
    Spacer(modifier = Modifier.width(12.dp))
    Text(
      text = suggestion,
      style = AppTypography.bodyMedium,
      color = appColorScheme.onSurface
    )
  }
}