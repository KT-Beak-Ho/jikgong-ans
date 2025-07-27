package com.billcorea.jikgong.presentation.company.main.projectlist.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.presentation.company.main.projectlist.data.ProjectStatus
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.appColorScheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectFilterTabs(
    selectedStatus: ProjectStatus?,
    onStatusSelected: (ProjectStatus?) -> Unit,
    projectCounts: Map<ProjectStatus, Int> = emptyMap(),
    modifier: Modifier = Modifier
) {
    val filterItems = listOf(
        null to "전체",
        ProjectStatus.RECRUITING to "모집중",
        ProjectStatus.IN_PROGRESS to "진행중",
        ProjectStatus.COMPLETED to "완료",
        ProjectStatus.CANCELLED to "취소"
    )

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(filterItems) { (status, label) ->
            val count = if (status == null) {
                projectCounts.values.sum()
            } else {
                projectCounts[status] ?: 0
            }

            FilterChip(
                onClick = { onStatusSelected(status) },
                label = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
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

                        if (count > 0) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (selectedStatus == status) {
                                    appColorScheme.onPrimary.copy(alpha = 0.2f)
                                } else {
                                    appColorScheme.primary.copy(alpha = 0.1f)
                                }
                            ) {
                                Text(
                                    text = count.toString(),
                                    style = AppTypography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = if (selectedStatus == status) {
                                        appColorScheme.onPrimary
                                    } else {
                                        appColorScheme.primary
                                    },
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
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
                    selectedTrailingIconColor = appColorScheme.onPrimary,
                    containerColor = appColorScheme.surface,
                    labelColor = appColorScheme.onSurfaceVariant
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = selectedStatus == status,
                    borderColor = if (selectedStatus == status) {
                        appColorScheme.primary
                    } else {
                        appColorScheme.outline.copy(alpha = 0.5f)
                    },
                    selectedBorderColor = appColorScheme.primary
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.defaultMinSize(minWidth = 60.dp)
            )
        }
    }
}