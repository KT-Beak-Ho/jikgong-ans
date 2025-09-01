package com.billcorea.jikgong.presentation.company.main.money.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.PaymentStatus
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentFilterBar(
    selectedStatus: PaymentStatus?,
    onStatusSelected: (PaymentStatus?) -> Unit,
    modifier: Modifier = Modifier
) {
    val filterItems = listOf(
        null to "전체",
        PaymentStatus.PENDING to "지급 대기",
        PaymentStatus.PROCESSING to "처리중",
        PaymentStatus.COMPLETED to "지급 완료",
        PaymentStatus.FAILED to "지급 실패"
    )

    val scrollState = rememberLazyListState()

    // 스크롤 진행률 계산
    val scrollProgress by remember {
        derivedStateOf {
            if (scrollState.layoutInfo.totalItemsCount == 0) return@derivedStateOf 0f

            val visibleItemsInfo = scrollState.layoutInfo.visibleItemsInfo
            if (visibleItemsInfo.isEmpty()) return@derivedStateOf 0f

            val totalItems = scrollState.layoutInfo.totalItemsCount
            val firstVisibleItem = scrollState.firstVisibleItemIndex
            val firstVisibleItemOffset = scrollState.firstVisibleItemScrollOffset
            val itemWidth = visibleItemsInfo.firstOrNull()?.size ?: 1

            val scrollOffset = firstVisibleItem + (firstVisibleItemOffset.toFloat() / itemWidth)
            (scrollOffset / (totalItems - 1).coerceAtLeast(1)).coerceIn(0f, 1f)
        }
    }

    // 스크롤 가능 여부 확인
    val canScroll by remember {
        derivedStateOf {
            scrollState.layoutInfo.totalItemsCount > 0 &&
                    scrollState.canScrollForward || scrollState.canScrollBackward
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        LazyRow(
            state = scrollState,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            userScrollEnabled = true
        ) {
            items(filterItems) { (status, label) ->
                FilterChip(
                    onClick = { onStatusSelected(status) },
                    label = {
                        Text(
                            text = label,
                            style = AppTypography.labelMedium.copy(
                                fontWeight = if (selectedStatus == status) {
                                    FontWeight.Bold
                                } else {
                                    FontWeight.Normal
                                }
                            )
                        )
                    },
                    selected = selectedStatus == status,
                    trailingIcon = if (selectedStatus == status && status != null) {
                        {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "필터 제거",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    } else null,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = appColorScheme.primary,
                        selectedLabelColor = appColorScheme.onPrimary,
                        selectedTrailingIconColor = appColorScheme.onPrimary
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = selectedStatus == status,
                        borderColor = if (selectedStatus == status) {
                            appColorScheme.primary
                        } else {
                            appColorScheme.outline
                        }
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.defaultMinSize(minWidth = 80.dp)
                )
            }
        }

        // 가로 스크롤바
        if (canScroll) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .padding(horizontal = 16.dp, vertical = 2.dp)
            ) {
                // 스크롤바 트랙
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = appColorScheme.outline.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(2.dp)
                        )
                )

                // 스크롤바 썸
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.3f) // 썸의 너비 (전체의 30%)
                        .offset(x = (scrollProgress * 70).dp) // 70% = 100% - 30%
                        .background(
                            color = appColorScheme.primary.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(2.dp)
                        )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentFilterBarPreview() {
    Jikgong1111Theme {
        Column(
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Text(
                text = "필터 없음",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            PaymentFilterBar(
                selectedStatus = null,
                onStatusSelected = {}
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "지급 대기 필터 선택",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            PaymentFilterBar(
                selectedStatus = PaymentStatus.PENDING,
                onStatusSelected = {}
            )
        }
    }
}