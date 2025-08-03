package com.billcorea.jikgong.presentation.company.main.projectlist.components

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectSearchBar(
  query: String,
  suggestions: List<String>,
  onQueryChange: (String) -> Unit,
  onSuggestionClick: (String) -> Unit,
  onCloseSearch: () -> Unit,
  modifier: Modifier = Modifier
) {
  val keyboardController = LocalSoftwareKeyboardController.current

  Column(
    modifier = modifier.fillMaxWidth()
  ) {
    OutlinedTextField(
      value = query,
      onValueChange = onQueryChange,
      modifier = Modifier.fillMaxWidth(),
      placeholder = {
        Text(
          text = "프로젝트명, 위치, 회사명으로 검색",
          color = Color(0xFF999999),
          fontSize = 14.sp
        )
      },
      leadingIcon = {
        Icon(
          imageVector = Icons.Default.Search,
          contentDescription = "검색",
          tint = Color(0xFF666666)
        )
      },
      trailingIcon = {
        Row {
          if (query.isNotEmpty()) {
            IconButton(
              onClick = { onQueryChange("") }
            ) {
              Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = "검색어 지우기",
                tint = Color(0xFF666666)
              )
            }
          }
          IconButton(
            onClick = onCloseSearch
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "검색 닫기",
              tint = Color(0xFF666666)
            )
          }
        }
      },
      shape = RoundedCornerShape(12.dp),
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = Color(0xFFE0E0E0),
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White
      ),
      keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
      keyboardActions = KeyboardActions(
        onSearch = { keyboardController?.hide() }
      ),
      singleLine = true
    )

    // 검색 제안
    if (suggestions.isNotEmpty()) {
      Spacer(modifier = Modifier.height(8.dp))

      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
          containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
      ) {
        LazyColumn(
          modifier = Modifier.heightIn(max = 200.dp)
        ) {
          items(suggestions) { suggestion ->
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { onSuggestionClick(suggestion) }
                .padding(horizontal = 16.dp, vertical = 12.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = Color(0xFF999999),
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(12.dp))
              Text(
                text = suggestion,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF333333),
                fontSize = 14.sp
              )
            }
          }
        }
      }
    }
  }
}
