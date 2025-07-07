package com.billcorea.jikgong.presentation.company.auth.main.projectlist.components



import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.presentation.company.auth.main.projectlist.data.ProjectStatus
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectFilterBar(
    selectedStatus: ProjectStatus?,
    onStatusSelected: (ProjectStatus?) -> Unit,
    modifier: Modifier = Modifier
) {
    val filterItems = listOf(
        null to "전체",
        ProjectStatus.RECRUITING to "모집중",
        ProjectStatus.IN_PROGRESS to "진행중",
        ProjectStatus.PLANNING to "계획중",
        ProjectStatus.COMPLETED to "완료"
    )

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
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
                shape = RoundedCornerShape(8.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProjectFilterBarPreview() {
    Jikgong1111Theme {
        Column(
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Text(
                text = "필터 없음",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            ProjectFilterBar(
                selectedStatus = null,
                onStatusSelected = {}
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "모집중 필터 선택",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
            ProjectFilterBar(
                selectedStatus = ProjectStatus.RECRUITING,
                onStatusSelected = {}
            )
        }
    }
}