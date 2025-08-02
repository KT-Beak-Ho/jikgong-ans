// ========================================
// 📄 utils/LazyListExtensions.kt
// ========================================
package com.billcorea.jikgong.presentation.company.main.projectlist.utils

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable

/**
 * LazyRow/LazyColumn에서 items 함수 사용을 위한 확장 함수
 * Compose의 기본 items 함수와 동일한 기능 제공
 */
inline fun <T> LazyListScope.items(
  items: List<T>,
  noinline key: ((item: T) -> Any)? = null,
  crossinline itemContent: @Composable LazyListScope.(item: T) -> Unit
) {
  items(
    count = items.size,
    key = if (key != null) { index: Int -> key(items[index]) } else null
  ) { index ->
    itemContent(items[index])
  }
}

/**
 * LazyRow/LazyColumn에서 itemsIndexed 함수 사용을 위한 확장 함수
 */
inline fun <T> LazyListScope.itemsIndexed(
  items: List<T>,
  noinline key: ((index: Int, item: T) -> Any)? = null,
  crossinline itemContent: @Composable LazyListScope.(index: Int, item: T) -> Unit
) {
  items(
    count = items.size,
    key = if (key != null) { index: Int -> key(index, items[index]) } else null
  ) { index ->
    itemContent(index, items[index])
  }
}