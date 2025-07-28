package com.billcorea.jikgong.presentation.company.main.projectlist.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Restaurant
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
import com.billcorea.jikgong.presentation.company.main.projectlist.data.PaymentType
import com.billcorea.jikgong.presentation.company.main.projectlist.data.ProjectRegistrationData
import com.billcorea.jikgong.presentation.company.main.projectlist.shared.ProjectRegistrationSharedEvent
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun ReviewPage(
    projectData: ProjectRegistrationData,
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
            title = "급여 정보",
            subtitle = "급여 및 혜택 정보를 입력해주세요"
        )

        // 급여 유형
        FormSection(
            title = "급여 유형",
            isRequired = true,
            icon = Icons.Default.AttachMoney
        ) {
            PaymentTypeSelector(
                selectedPaymentType = projectData.moneyInfo.paymentType,
                onPaymentTypeSelected = {
                    onEvent(ProjectRegistrationSharedEvent.UpdatePaymentType(it))
                }
            )
        }

        // 급여 금액 - if 표현식 오류 수정
        projectData.moneyInfo.paymentType?.let { paymentType ->
            FormSection(
                title = "급여 금액",
                isRequired = true,
                icon = Icons.Default.AttachMoney
            ) {
                when (paymentType) {
                    PaymentType.DAILY -> {
                        WageInput(
                            label = "일당",
                            value = projectData.moneyInfo.dailyWage,
                            onValueChange = {
                                onEvent(ProjectRegistrationSharedEvent.UpdateDailyWage(it))
                            },
                            validationError = validationErrors["dailyWage"],
                            suffix = "원/일"
                        )
                    }
                    PaymentType.HOURLY -> {
                        WageInput(
                            label = "시급",
                            value = projectData.moneyInfo.hourlyWage,
                            onValueChange = {
                                onEvent(ProjectRegistrationSharedEvent.UpdateHourlyWage(it))
                            },
                            suffix = "원/시간"
                        )
                    }
                    PaymentType.PROJECT -> {
                        WageInput(
                            label = "프로젝트 금액",
                            value = projectData.moneyInfo.projectWage,
                            onValueChange = {
                                onEvent(ProjectRegistrationSharedEvent.UpdateProjectWage(it))
                            },
                            suffix = "원/프로젝트"
                        )
                    }
                }
            }
        }

        // 추가 혜택
        FormSection(
            title = "추가 혜택",
            icon = Icons.Default.Restaurant
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 교통비
                WageInput(
                    label = "교통비",
                    value = projectData.moneyInfo.transportationFee,
                    onValueChange = {
                        onEvent(ProjectRegistrationSharedEvent.UpdateTransportationFee(it))
                    },
                    suffix = "원",
                    isRequired = false
                )

                // 식사 제공
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "식사 제공",
                        style = AppTypography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                    )

                    Switch(
                        checked = projectData.moneyInfo.mealProvided,
                        onCheckedChange = {
                            onEvent(ProjectRegistrationSharedEvent.UpdateMealProvided(it))
                        }
                    )
                }

                // 숙박 제공
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "숙박 제공",
                        style = AppTypography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                    )

                    Switch(
                        checked = projectData.moneyInfo.accommodationProvided,
                        onCheckedChange = {
                            onEvent(ProjectRegistrationSharedEvent.UpdateAccommodationProvided(it))
                        }
                    )
                }
            }
        }

        // 특이사항
        FormSection(
            title = "특이사항"
        ) {
            CommonTextInput(
                value = projectData.moneyInfo.notes,
                onChange = { onEvent(ProjectRegistrationSharedEvent.UpdateNotes(it)) },
                placeholder = "추가로 전달하고 싶은 내용이 있다면 입력해주세요",
                maxLines = 3,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // 프로젝트 요약
        ProjectSummaryCard(projectData = projectData)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PaymentTypeSelector(
    selectedPaymentType: PaymentType?,
    onPaymentTypeSelected: (PaymentType) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedPaymentType?.displayName ?: "",
            onValueChange = {},
            readOnly = true,
            placeholder = { Text("급여 유형을 선택해주세요") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            PaymentType.values().forEach { paymentType ->
                DropdownMenuItem(
                    text = { Text(paymentType.displayName) },
                    onClick = {
                        onPaymentTypeSelected(paymentType)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun WageInput(
    label: String,
    value: Long,
    onValueChange: (Long) -> Unit,
    suffix: String,
    isRequired: Boolean = true,
    validationError: String? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = label,
                style = AppTypography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                modifier = Modifier.weight(1f)
            )

            if (isRequired) {
                Text(
                    text = "*",
                    style = AppTypography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        OutlinedTextField(
            value = if (value > 0) NumberFormat.getNumberInstance(Locale.KOREA).format(value) else "",
            onValueChange = { newValue ->
                val cleanValue = newValue.replace(",", "").replace(" ", "")
                cleanValue.toLongOrNull()?.let { longValue ->
                    if (longValue >= 0) onValueChange(longValue)
                } ?: run {
                    if (cleanValue.isEmpty()) onValueChange(0L)
                }
            },
            placeholder = { Text("0") },
            suffix = { Text(suffix) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = validationError != null,
            supportingText = validationError?.let { { Text(it) } },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
    }
}

@Composable
private fun ProjectSummaryCard(
    projectData: ProjectRegistrationData,
    modifier: Modifier = Modifier
) {
    InfoCard(
        title = "프로젝트 요약",
        subtitle = "입력하신 정보를 확인해주세요"
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 기본 정보
            SummaryItem(
                label = "프로젝트",
                value = projectData.requiredInfo.projectTitle.ifEmpty { "미입력" }
            )

            SummaryItem(
                label = "작업 유형",
                value = projectData.requiredInfo.workType?.displayName ?: "미입력"
            )

            SummaryItem(
                label = "근무지",
                value = projectData.requiredInfo.location.ifEmpty { "미입력" }
            )

            // 기간
            val dateRange = if (projectData.requiredInfo.startDate != null && projectData.requiredInfo.endDate != null) {
                "${projectData.requiredInfo.startDate!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))} ~ " +
                        "${projectData.requiredInfo.endDate!!.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}"
            } else {
                "미입력"
            }

            SummaryItem(
                label = "근무 기간",
                value = dateRange
            )

            // 인원
            SummaryItem(
                label = "모집 인원",
                value = "${projectData.teamInfo.requiredWorkers}/${projectData.teamInfo.totalWorkers}명"
            )

            // 급여
            val wageText = when (projectData.moneyInfo.paymentType) {
                PaymentType.DAILY -> "${NumberFormat.getNumberInstance(Locale.KOREA).format(projectData.moneyInfo.dailyWage)}원/일"
                PaymentType.HOURLY -> "${NumberFormat.getNumberInstance(Locale.KOREA).format(projectData.moneyInfo.hourlyWage)}원/시간"
                PaymentType.PROJECT -> "${NumberFormat.getNumberInstance(Locale.KOREA).format(projectData.moneyInfo.projectWage)}원/프로젝트"
                null -> "미입력"
            }

            SummaryItem(
                label = "급여",
                value = wageText
            )
        }
    }
}

@Composable
private fun SummaryItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = AppTypography.bodyMedium,
            color = appColorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            style = AppTypography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = appColorScheme.onSurface
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewPagePreview() {
    Jikgong1111Theme {
        ReviewPage(
            projectData = ProjectRegistrationData(),
            validationErrors = emptyMap(),
            onEvent = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}