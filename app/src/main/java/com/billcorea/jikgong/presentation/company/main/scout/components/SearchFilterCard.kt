// app/src/main/java/com/billcorea/jikgong/presentation/company/main/scout/components/SearchFilterCard.kt
package com.billcorea.jikgong.presentation.company.main.scout.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.presentation.company.main.scout.data.SearchFilters
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme

@Composable
fun SearchFilterCard(
    searchFilters: SearchFilters,
    onFilterChange: (SearchFilters) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = appColorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 헤더
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "필터",
                        tint = appColorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "검색 조건",
                        style = AppTypography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = appColorScheme.onSurface
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // 활성 필터 개수 표시
                    if (searchFilters.hasActiveFilters) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = appColorScheme.primary
                        ) {
                            Text(
                                text = "${searchFilters.activeFiltersCount}",
                                style = AppTypography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = appColorScheme.onPrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // 확장/축소 버튼
                    IconButton(
                        onClick = { isExpanded = !isExpanded }
                    ) {
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (isExpanded) "접기" else "펼치기",
                            tint = appColorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // 기본 필터 (항상 보이는 것들)
            BasicFilters(
                searchFilters = searchFilters,
                onFilterChange = onFilterChange
            )

            // 확장된 필터들
            if (isExpanded) {
                AdvancedFilters(
                    searchFilters = searchFilters,
                    onFilterChange = onFilterChange
                )
            }

            // 필터 리셋 버튼
            if (searchFilters.hasActiveFilters) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = {
                            onFilterChange(SearchFilters())
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "초기화",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("필터 초기화")
                    }
                }
            }
        }
    }
}

@Composable
private fun BasicFilters(
    searchFilters: SearchFilters,
    onFilterChange: (SearchFilters) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 검색 반경
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "검색 반경",
                    style = AppTypography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
                Text(
                    text = "${searchFilters.radius}km",
                    style = AppTypography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = appColorScheme.primary
                )
            }

            Slider(
                value = searchFilters.radius.toFloat(),
                onValueChange = { newRadius ->
                    onFilterChange(searchFilters.copy(radius = newRadius.toInt()))
                },
                valueRange = 1f..50f,
                steps = 48,
                colors = SliderDefaults.colors(
                    thumbColor = appColorScheme.primary,
                    activeTrackColor = appColorScheme.primary
                )
            )
        }

        // 직종 필터
        FilterChipSection(
            title = "직종",
            options = listOf("전체", "보통인부", "철근공", "콘크리트공", "용접공", "전기공"),
            selectedOption = searchFilters.jobType,
            onOptionSelected = { jobType ->
                onFilterChange(searchFilters.copy(jobType = jobType))
            }
        )
    }
}

@Composable
private fun AdvancedFilters(
    searchFilters: SearchFilters,
    onFilterChange: (SearchFilters) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 경력 필터
        FilterChipSection(
            title = "경력",
            options = listOf("상관없음", "신입", "1년+", "3년+", "5년+", "10년+"),
            selectedOption = searchFilters.experience,
            onOptionSelected = { experience ->
                onFilterChange(searchFilters.copy(experience = experience))
            }
        )

        // 평점 필터
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "최소 평점",
                    style = AppTypography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
                Text(
                    text = if (searchFilters.minRating > 0) "${String.format("%.1f", searchFilters.minRating)}점 이상" else "상관없음",
                    style = AppTypography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = appColorScheme.primary
                )
            }

            Slider(
                value = searchFilters.minRating,
                onValueChange = { newRating ->
                    onFilterChange(searchFilters.copy(minRating = newRating))
                },
                valueRange = 0f..5f,
                steps = 9,
                colors = SliderDefaults.colors(
                    thumbColor = appColorScheme.primary,
                    activeTrackColor = appColorScheme.primary
                )
            )
        }

        // 온라인 상태 필터
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "온라인 인력만",
                style = AppTypography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                )
            )

            Switch(
                checked = searchFilters.isOnlineOnly,
                onCheckedChange = { isOnlineOnly ->
                    onFilterChange(searchFilters.copy(isOnlineOnly = isOnlineOnly))
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = appColorScheme.primary,
                    checkedTrackColor = appColorScheme.primary.copy(alpha = 0.5f)
                )
            )
        }

        // 신규 인력 필터
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "신규 인력만",
                style = AppTypography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                )
            )

            Switch(
                checked = searchFilters.isNewWorkerOnly,
                onCheckedChange = { isNewWorkerOnly ->
                    onFilterChange(searchFilters.copy(isNewWorkerOnly = isNewWorkerOnly))
                },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = appColorScheme.primary,
                    checkedTrackColor = appColorScheme.primary.copy(alpha = 0.5f)
                )
            )
        }
    }
}

@Composable
private fun FilterChipSection(
    title: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            style = AppTypography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
            )
        )

        // 첫 번째 줄 (최대 3개)
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            options.take(3).forEach { option ->
                FilterChip(
                    selected = selectedOption == option,
                    onClick = { onOptionSelected(option) },
                    label = {
                        Text(
                            text = option,
                            style = AppTypography.labelMedium
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = appColorScheme.primary,
                        selectedLabelColor = appColorScheme.onPrimary,
                        containerColor = appColorScheme.surfaceVariant,
                        labelColor = appColorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // 두 번째 줄 (나머지)
        if (options.size > 3) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                options.drop(3).forEach { option ->
                    FilterChip(
                        selected = selectedOption == option,
                        onClick = { onOptionSelected(option) },
                        label = {
                            Text(
                                text = option,
                                style = AppTypography.labelMedium
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = appColorScheme.primary,
                            selectedLabelColor = appColorScheme.onPrimary,
                            containerColor = appColorScheme.surfaceVariant,
                            labelColor = appColorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }

                // 빈 공간 채우기
                repeat(3 - options.drop(3).size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Preview(name = "기본 상태", showBackground = true)
@Composable
fun SearchFilterCardDefaultPreview() {
    Jikgong1111Theme {
        SearchFilterCard(
            searchFilters = SearchFilters(),
            onFilterChange = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(name = "필터 적용됨", showBackground = true)
@Composable
fun SearchFilterCardWithFiltersPreview() {
    Jikgong1111Theme {
        SearchFilterCard(
            searchFilters = SearchFilters(
                radius = 15,
                jobType = "철근공",
                experience = "3년+",
                minRating = 4.0f,
                isOnlineOnly = true,
                isNewWorkerOnly = false
            ),
            onFilterChange = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(name = "다크 테마", showBackground = true)
@Composable
fun SearchFilterCardDarkPreview() {
    Jikgong1111Theme(darkTheme = true) {
        SearchFilterCard(
            searchFilters = SearchFilters(
                radius = 20,
                jobType = "용접공",
                experience = "5년+",
                minRating = 4.5f,
                isOnlineOnly = false,
                isNewWorkerOnly = true
            ),
            onFilterChange = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}