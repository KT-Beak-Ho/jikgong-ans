package com.billcorea.jikgong.presentation.company.main.projectlist.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.presentation.company.auth.common.components.CommonTextInput
import com.billcorea.jikgong.presentation.company.main.projectlist.components.FormSection
import com.billcorea.jikgong.presentation.company.main.projectlist.data.JobType
import com.billcorea.jikgong.presentation.company.main.projectlist.data.RequiredInfo
import com.billcorea.jikgong.presentation.company.main.projectlist.shared.ProjectRegistrationSharedEvent
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun BasicInfoPage(
    requiredInfo: RequiredInfo,
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
            title = "기본 정보",
            subtitle = "프로젝트의 기본 정보를 입력해주세요"
        )

        // 프로젝트 제목
        FormSection(
            title = "프로젝트 제목",
            isRequired = true,
            icon = Icons.Default.Work
        ) {
            CommonTextInput(
                value = requiredInfo.projectTitle,
                onChange = { onEvent(ProjectRegistrationSharedEvent.UpdateProjectTitle(it)) },
                placeholder = "예: 강남구 아파트 신축 공사",
                validationError = validationErrors["projectTitle"],
                modifier = Modifier.fillMaxWidth()
            )
        }

        // 작업 유형
        FormSection(
            title = "작업 유형",
            isRequired = true,
            icon = Icons.Default.Work
        ) {
            JobTypeSelector(
                selectedJobType = requiredInfo.workType,
                onJobTypeSelected = { onEvent(ProjectRegistrationSharedEvent.UpdateWorkType(it)) }
            )
        }

        // 근무지
        FormSection(
            title = "근무지",
            isRequired = true,
            icon = Icons.Default.LocationOn
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CommonTextInput(
                    value = requiredInfo.location,
                    onChange = { onEvent(ProjectRegistrationSharedEvent.UpdateLocation(it)) },
                    placeholder = "시/구/동 까지 입력해주세요",
                    validationError = validationErrors["location"],
                    modifier = Modifier.fillMaxWidth()
                )

                CommonTextInput(
                    value = requiredInfo.detailAddress,
                    onChange = { onEvent(ProjectRegistrationSharedEvent.UpdateDetailAddress(it)) },
                    placeholder = "상세 주소 (선택사항)",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // 근무 기간
        FormSection(
            title = "근무 기간",
            isRequired = true,
            icon = Icons.Default.CalendarToday
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DatePickerField(
                    label = "시작일",
                    selectedDate = requiredInfo.startDate,
                    onDateSelected = { onEvent(ProjectRegistrationSharedEvent.UpdateStartDate(it)) },
                    modifier = Modifier.weight(1f)
                )

                DatePickerField(
                    label = "종료일",
                    selectedDate = requiredInfo.endDate,
                    onDateSelected = { onEvent(ProjectRegistrationSharedEvent.UpdateEndDate(it)) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // 근무 시간
        FormSection(
            title = "근무 시간",
            isRequired = true,
            icon = Icons.Default.AccessTime
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TimePickerField(
                    label = "시작 시간",
                    selectedTime = requiredInfo.startTime,
                    onTimeSelected = { onEvent(ProjectRegistrationSharedEvent.UpdateStartTime(it)) },
                    modifier = Modifier.weight(1f)
                )

                TimePickerField(
                    label = "종료 시간",
                    selectedTime = requiredInfo.endTime,
                    onTimeSelected = { onEvent(ProjectRegistrationSharedEvent.UpdateEndTime(it)) },
                    modifier = Modifier.weight(1f)
                )
            }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun JobTypeSelector(
    selectedJobType: JobType?,
    onJobTypeSelected: (JobType) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedJobType?.displayName ?: "",
            onValueChange = {},
            readOnly = true,
            placeholder = { Text("작업 유형을 선택해주세요") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            JobType.values().forEach { jobType ->
                DropdownMenuItem(
                    text = { Text(jobType.displayName) },
                    onClick = {
                        onJobTypeSelected(jobType)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun DatePickerField(
    label: String,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = selectedDate?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) ?: "",
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            placeholder = { Text("날짜 선택") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker = true }
        )

        if (showDatePicker) {
            DatePickerDialog(
                onDateSelected = { date ->
                    date?.let {
                        onDateSelected(LocalDate.ofEpochDay(it / 86400000))
                    }
                    showDatePicker = false
                },
                onDismiss = { showDatePicker = false }
            )
        }
    }
}

@Composable
private fun TimePickerField(
    label: String,
    selectedTime: LocalTime?,
    onTimeSelected: (LocalTime) -> Unit,
    modifier: Modifier = Modifier
) {
    var showTimePicker by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        OutlinedTextField(
            value = selectedTime?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: "",
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            placeholder = { Text("시간 선택") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showTimePicker = true }
        )

        if (showTimePicker) {
            TimePickerDialog(
                onTimeSelected = { hour, minute ->
                    onTimeSelected(LocalTime.of(hour, minute))
                    showTimePicker = false
                },
                onDismiss = { showTimePicker = false }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerDialog(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onDateSelected(datePickerState.selectedDateMillis) }) {
                Text("확인")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimePickerDialog(
    onTimeSelected: (Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    val timePickerState = rememberTimePickerState()

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onTimeSelected(timePickerState.hour, timePickerState.minute)
                }
            ) {
                Text("확인")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        },
        text = {
            TimePicker(state = timePickerState)
        }
    )
}

@Preview(showBackground = true)
@Composable
fun BasicInfoPagePreview() {
    Jikgong1111Theme {
        BasicInfoPage(
            requiredInfo = RequiredInfo(
                projectTitle = "강남구 아파트 신축 공사",
                workType = JobType.GENERAL_WORKER,
                location = "서울시 강남구",
                detailAddress = "테헤란로 123",
                startDate = LocalDate.now(),
                endDate = LocalDate.now().plusDays(30)
            ),
            validationErrors = emptyMap(),
            onEvent = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}