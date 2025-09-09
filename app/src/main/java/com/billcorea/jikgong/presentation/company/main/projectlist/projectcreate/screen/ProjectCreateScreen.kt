package com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.ProjectCategory
import com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.model.DatePickerType
import com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.model.ProjectCreateEvent
import com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.viewmodel.ProjectCreateViewModel
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectCreateScreen(
    onDismiss: () -> Unit,
    onSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ProjectCreateViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // 프로젝트 생성 성공 시 콜백 호출
    LaunchedEffect(uiState.isLoading) {
        // TODO: 실제 성공 상태 추가 시 처리
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.9f)
                .padding(16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                // 헤더
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "새 프로젝트 등록",
                        style = AppTypography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    
                    IconButton(onClick = onDismiss) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "닫기",
                            tint = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 폼 내용
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 프로젝트 제목
                    item {
                        OutlinedTextField(
                            value = uiState.title,
                            onValueChange = { viewModel.onEvent(ProjectCreateEvent.UpdateTitle(it)) },
                            label = { Text("프로젝트 제목 *") },
                            placeholder = { Text("예: 강남구 아파트 신축공사") },
                            isError = uiState.validationErrors.title != null,
                            supportingText = uiState.validationErrors.title?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }

                    // 카테고리 선택
                    item {
                        CategorySelector(
                            selectedCategory = uiState.category,
                            onCategorySelected = { viewModel.onEvent(ProjectCreateEvent.UpdateCategory(it)) },
                            showSelector = uiState.showCategorySelector,
                            onShowSelector = { viewModel.onEvent(ProjectCreateEvent.ShowCategorySelector) },
                            onHideSelector = { viewModel.onEvent(ProjectCreateEvent.HideCategorySelector) }
                        )
                    }

                    // 위치
                    item {
                        OutlinedTextField(
                            value = uiState.location,
                            onValueChange = { viewModel.onEvent(ProjectCreateEvent.UpdateLocation(it)) },
                            label = { Text("위치 *") },
                            placeholder = { Text("예: 서울시 강남구 역삼동") },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.LocationOn,
                                    contentDescription = null,
                                    tint = Color.Gray
                                )
                            },
                            isError = uiState.validationErrors.location != null,
                            supportingText = uiState.validationErrors.location?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }

                    // 일당
                    item {
                        OutlinedTextField(
                            value = uiState.wage,
                            onValueChange = { viewModel.onEvent(ProjectCreateEvent.UpdateWage(it)) },
                            label = { Text("일당 *") },
                            placeholder = { Text("150000") },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.AttachMoney,
                                    contentDescription = null,
                                    tint = Color.Gray
                                )
                            },
                            suffix = { Text("원") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            isError = uiState.validationErrors.wage != null,
                            supportingText = uiState.validationErrors.wage?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }

                    // 날짜 선택
                    item {
                        DateRangeSelector(
                            startDate = uiState.startDate,
                            endDate = uiState.endDate,
                            onStartDateClick = { viewModel.onEvent(ProjectCreateEvent.ShowStartDatePicker) },
                            onEndDateClick = { viewModel.onEvent(ProjectCreateEvent.ShowEndDatePicker) },
                            startDateError = uiState.validationErrors.startDate,
                            endDateError = uiState.validationErrors.endDate
                        )
                    }

                    // 최대 지원자 수
                    item {
                        OutlinedTextField(
                            value = uiState.maxApplicants,
                            onValueChange = { viewModel.onEvent(ProjectCreateEvent.UpdateMaxApplicants(it)) },
                            label = { Text("최대 지원자 수 *") },
                            placeholder = { Text("20") },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.People,
                                    contentDescription = null,
                                    tint = Color.Gray
                                )
                            },
                            suffix = { Text("명") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            isError = uiState.validationErrors.maxApplicants != null,
                            supportingText = uiState.validationErrors.maxApplicants?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }

                    // 긴급 채용 여부
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = uiState.isUrgent,
                                onCheckedChange = { viewModel.onEvent(ProjectCreateEvent.UpdateIsUrgent(it)) }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "긴급 채용",
                                style = AppTypography.bodyMedium
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFFF6B6B).copy(alpha = 0.1f)
                            ) {
                                Text(
                                    text = "우선 노출",
                                    style = AppTypography.bodySmall,
                                    color = Color(0xFFFF6B6B),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }

                    // 프로젝트 설명 (선택사항)
                    item {
                        OutlinedTextField(
                            value = uiState.description,
                            onValueChange = { viewModel.onEvent(ProjectCreateEvent.UpdateDescription(it)) },
                            label = { Text("프로젝트 설명 (선택사항)") },
                            placeholder = { Text("프로젝트에 대한 추가 정보를 입력해주세요") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            maxLines = 4
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 에러 메시지
                uiState.error?.let { error ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            text = error,
                            style = AppTypography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                // 버튼들
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("취소")
                    }

                    Button(
                        onClick = { viewModel.onEvent(ProjectCreateEvent.CreateProject) },
                        modifier = Modifier.weight(1f),
                        enabled = !uiState.isLoading && viewModel.isFormValid(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4B7BFF)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (uiState.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("등록", color = Color.White)
                        }
                    }
                }
            }
        }
    }

    // 날짜 선택기
    if (uiState.showDatePicker) {
        DatePickerDialog(
            onDateSelected = { date ->
                when (uiState.datePickerType) {
                    DatePickerType.START_DATE -> viewModel.onEvent(ProjectCreateEvent.UpdateStartDate(date))
                    DatePickerType.END_DATE -> viewModel.onEvent(ProjectCreateEvent.UpdateEndDate(date))
                }
                viewModel.onEvent(ProjectCreateEvent.HideDatePicker)
            },
            onDismiss = { viewModel.onEvent(ProjectCreateEvent.HideDatePicker) },
            title = when (uiState.datePickerType) {
                DatePickerType.START_DATE -> "시작일 선택"
                DatePickerType.END_DATE -> "종료일 선택"
            }
        )
    }
}

@Composable
private fun CategorySelector(
    selectedCategory: ProjectCategory,
    onCategorySelected: (ProjectCategory) -> Unit,
    showSelector: Boolean,
    onShowSelector: () -> Unit,
    onHideSelector: () -> Unit
) {
    Column {
        OutlinedTextField(
            value = selectedCategory.displayName,
            onValueChange = { },
            label = { Text("카테고리 *") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = onShowSelector) {
                    Icon(
                        if (showSelector) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "카테고리 선택"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onShowSelector),
            singleLine = true
        )

        if (showSelector) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                LazyColumn {
                    items(ProjectCategory.values().toList()) { category ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onCategorySelected(category)
                                    onHideSelector()
                                }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = category == selectedCategory,
                                onClick = {
                                    onCategorySelected(category)
                                    onHideSelector()
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = category.displayName,
                                style = AppTypography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DateRangeSelector(
    startDate: String,
    endDate: String,
    onStartDateClick: () -> Unit,
    onEndDateClick: () -> Unit,
    startDateError: String?,
    endDateError: String?
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = if (startDate.isNotEmpty()) {
                try {
                    LocalDate.parse(startDate).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                } catch (e: Exception) {
                    startDate
                }
            } else "",
            onValueChange = { },
            label = { Text("시작일 *") },
            readOnly = true,
            leadingIcon = {
                Icon(
                    Icons.Outlined.DateRange,
                    contentDescription = null,
                    tint = Color.Gray
                )
            },
            isError = startDateError != null,
            supportingText = startDateError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
            modifier = Modifier
                .weight(1f)
                .clickable(onClick = onStartDateClick),
            singleLine = true
        )

        OutlinedTextField(
            value = if (endDate.isNotEmpty()) {
                try {
                    LocalDate.parse(endDate).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                } catch (e: Exception) {
                    endDate
                }
            } else "",
            onValueChange = { },
            label = { Text("종료일 *") },
            readOnly = true,
            leadingIcon = {
                Icon(
                    Icons.Outlined.DateRange,
                    contentDescription = null,
                    tint = Color.Gray
                )
            },
            isError = endDateError != null,
            supportingText = endDateError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
            modifier = Modifier
                .weight(1f)
                .clickable(onClick = onEndDateClick),
            singleLine = true
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerDialog(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit,
    title: String
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.let { millis ->
                    val date = java.time.Instant.ofEpochMilli(millis)
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDate()
                    onDateSelected(date.toString())
                }
            }) {
                Text("확인")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            title = {
                Text(
                    text = title,
                    modifier = Modifier.padding(16.dp)
                )
            }
        )
    }
}

@Preview
@Composable
fun ProjectCreateScreenPreview() {
    Jikgong1111Theme {
        ProjectCreateScreen(
            onDismiss = {},
            onSuccess = {}
        )
    }
}