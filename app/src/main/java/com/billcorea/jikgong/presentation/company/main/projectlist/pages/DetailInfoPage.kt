package com.billcorea.jikgong.presentation.company.main.projectlist.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.presentation.company.auth.common.components.CommonTextInput
import com.billcorea.jikgong.presentation.company.main.projectlist.components.FormSection
import com.billcorea.jikgong.presentation.company.main.projectlist.components.InfoCard
import com.billcorea.jikgong.presentation.company.main.projectlist.data.TeamInfo
import com.billcorea.jikgong.presentation.company.main.projectlist.shared.ProjectRegistrationSharedEvent
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme

@Composable
fun DetailInfoPage(
    teamInfo: TeamInfo,
    validationErrors: Map<String, String>,
    onEvent: (ProjectRegistrationSharedEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // 페이지 헤더
        PageHeader(
            title = "팀 정보",
            subtitle = "필요한 인력 정보를 입력해주세요"
        )

        // 인원 정보
        FormSection(
            title = "인원 정보",
            isRequired = true,
            icon = Icons.Default.Group
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 총 인원수
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "총 인원수",
                        style = AppTypography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                        modifier = Modifier.weight(1f)
                    )

                    NumberSelector(
                        value = teamInfo.totalWorkers,
                        onValueChange = {
                            onEvent(ProjectRegistrationSharedEvent.UpdateTotalWorkers(it))
                        },
                        minValue = 1,
                        maxValue = 100
                    )
                }

                // 모집 인원수
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "모집 인원수",
                        style = AppTypography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                        modifier = Modifier.weight(1f)
                    )

                    NumberSelector(
                        value = teamInfo.requiredWorkers,
                        onValueChange = {
                            onEvent(ProjectRegistrationSharedEvent.UpdateRequiredWorkers(it))
                        },
                        minValue = 1,
                        maxValue = teamInfo.totalWorkers.coerceAtLeast(1)
                    )
                }

                // 유효성 검증 에러 표시
                validationErrors["totalWorkers"]?.let { error ->
                    Text(
                        text = error,
                        style = AppTypography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                validationErrors["requiredWorkers"]?.let { error ->
                    Text(
                        text = error,
                        style = AppTypography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        // 선호 조건
        FormSection(
            title = "선호 조건",
            icon = Icons.Default.Person
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 선호 연령대
                CommonTextInput(
                    value = teamInfo.preferredAge,
                    onChange = { onEvent(ProjectRegistrationSharedEvent.UpdatePreferredAge(it)) },
                    labelMainText = "선호 연령대",
                    placeholder = "예: 20-40세",
                    modifier = Modifier.fillMaxWidth()
                )

                // 경력 필요 여부
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "경력 필요",
                        style = AppTypography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                    )

                    Switch(
                        checked = teamInfo.experienceRequired,
                        onCheckedChange = {
                            onEvent(ProjectRegistrationSharedEvent.UpdateExperienceRequired(it))
                        }
                    )
                }
            }
        }

        // 요구사항
        FormSection(
            title = "요구사항"
        ) {
            RequirementsList(
                items = teamInfo.requirements,
                onAddItem = { item ->
                    onEvent(ProjectRegistrationSharedEvent.AddRequirement(item))
                },
                onRemoveItem = { index ->
                    onEvent(ProjectRegistrationSharedEvent.RemoveRequirement(index))
                },
                placeholder = "예: 안전화 필수 착용"
            )
        }

        // 제공사항
        FormSection(
            title = "제공사항"
        ) {
            RequirementsList(
                items = teamInfo.providedItems,
                onAddItem = { item ->
                    onEvent(ProjectRegistrationSharedEvent.AddProvidedItem(item))
                },
                onRemoveItem = { index ->
                    onEvent(ProjectRegistrationSharedEvent.RemoveProvidedItem(index))
                },
                placeholder = "예: 중식 제공, 교통비 지급"
            )
        }
    }
}

@Composable
private fun PageHeader(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            style = AppTypography.headlineSmall.copy(
                fontWeight = FontWeight.Bold
            ),
            color = appColorScheme.onSurface
        )

        Text(
            text = subtitle,
            style = AppTypography.bodyLarge,
            color = appColorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun NumberSelector(
    value: Int,
    onValueChange: (Int) -> Unit,
    minValue: Int = 0,
    maxValue: Int = 999,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                if (value > minValue) onValueChange(value - 1)
            },
            enabled = value > minValue
        ) {
            Icon(
                imageVector = Icons.Default.Remove,
                contentDescription = "감소"
            )
        }

        OutlinedTextField(
            value = value.toString(),
            onValueChange = { newValue ->
                newValue.toIntOrNull()?.let { intValue ->
                    if (intValue in minValue..maxValue) {
                        onValueChange(intValue)
                    }
                }
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.width(80.dp),
            textStyle = AppTypography.bodyLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            singleLine = true
        )

        IconButton(
            onClick = {
                if (value < maxValue) onValueChange(value + 1)
            },
            enabled = value < maxValue
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "증가"
            )
        }
    }
}

@Composable
private fun RequirementsList(
    items: List<String>,
    onAddItem: (String) -> Unit,
    onRemoveItem: (Int) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    var newItem by remember { mutableStateOf("") }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 새 항목 추가
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = newItem,
                onValueChange = { newItem = it },
                placeholder = { Text(placeholder) },
                modifier = Modifier.weight(1f),
                singleLine = true
            )

            IconButton(
                onClick = {
                    if (newItem.isNotBlank()) {
                        onAddItem(newItem.trim())
                        newItem = ""
                    }
                },
                enabled = newItem.isNotBlank()
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "추가"
                )
            }
        }

        // 기존 항목들
        items.forEachIndexed { index, item ->
            RequirementItem(
                text = item,
                onRemove = { onRemoveItem(index) }
            )
        }

        if (items.isEmpty()) {
            InfoCard(
                title = "항목 없음",
                subtitle = "위에서 새로운 항목을 추가해보세요"
            ) {}
        }
    }
}

@Composable
private fun RequirementItem(
    text: String,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = appColorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                style = AppTypography.bodyMedium,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Remove,
                    contentDescription = "제거",
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailInfoPagePreview() {
    Jikgong1111Theme {
        DetailInfoPage(
            teamInfo = TeamInfo(
                totalWorkers = 10,
                requiredWorkers = 5,
                preferredAge = "20-40세",
                experienceRequired = true,
                requirements = listOf("안전화 필수", "작업복 착용"),
                providedItems = listOf("중식 제공", "교통비 지급")
            ),
            validationErrors = emptyMap(),
            onEvent = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}