package com.billcorea.jikgong.presentation.company.main.projectlist.components

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.appColorScheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectListHeader(
  onSearchClick: () -> Unit,
  searchQuery: String,
  onSearchQueryChange: (String) -> Unit,
  isSearchVisible: Boolean,
  onClearSearch: () -> Unit,
  modifier: Modifier = Modifier
) {
  Column(modifier = modifier) {
    // 기본 헤더
    AnimatedVisibility(
      visible = !isSearchVisible,
      enter = fadeIn() + slideInVertically(),
      exit = fadeOut() + slideOutVertically()
    ) {
      TopAppBar(
        title = {
          Text(
            text = "프로젝트 목록",
            style = AppTypography.titleLarge.copy(fontWeight = FontWeight.Bold)
          )
        },
        actions = {
          IconButton(onClick = onSearchClick) {
            Icon(
              imageVector = Icons.Default.Search,
              contentDescription = "검색"
            )
          }
          IconButton(onClick = { /* TODO: 알림 */ }) {
            Icon(
              imageVector = Icons.Default.Notifications,
              contentDescription = "알림"
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = appColorScheme.surface,
          titleContentColor = appColorScheme.onSurface
        )
      )
    }

    // 검색 헤더
    AnimatedVisibility(
      visible = isSearchVisible,
      enter = fadeIn() + slideInVertically(),
      exit = fadeOut() + slideOutVertically()
    ) {
      TopAppBar(
        title = {
          OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = { Text("프로젝트, 지역, 직종으로 검색...") },
            leadingIcon = {
              Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "검색"
              )
            },
            trailingIcon = {
              if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { onSearchQueryChange("") }) {
                  Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "검색어 지우기"
                  )
                }
              }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { /* 검색 실행 */ }),
            modifier = Modifier.fillMaxWidth()
          )
        },
        navigationIcon = {
          IconButton(onClick = onClearSearch) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack, // 수정됨
              contentDescription = "검색 닫기"
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = appColorScheme.surface
        )
      )
    }
  }
}
