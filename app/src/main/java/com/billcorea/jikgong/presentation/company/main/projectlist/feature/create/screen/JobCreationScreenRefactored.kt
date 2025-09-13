package com.billcorea.jikgong.presentation.company.main.projectlist.feature.create.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.billcorea.jikgong.presentation.company.main.projectlist.feature.create.viewmodel.JobCreationViewModel
import com.billcorea.jikgong.presentation.company.main.projectlist.feature.create.viewmodel.NavigationEvent
import com.billcorea.jikgong.presentation.company.main.projectlist.feature.create.components.*

/**
 * 리팩토링된 구인 공고 생성 화면
 * MVVM 패턴 적용, 상태 관리 최적화
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobCreationScreenRefactored(
    onNavigateBack: () -> Unit,
    previousJobId: String? = null,
    viewModel: JobCreationViewModel = viewModel()
) {
    // ViewModel의 상태를 관찰
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val navigationEvent by viewModel.navigationEvent.collectAsStateWithLifecycle()
    
    // 네비게이션 이벤트 처리
    LaunchedEffect(navigationEvent) {
        when (navigationEvent) {
            is NavigationEvent.NavigateBack -> {
                onNavigateBack()
                viewModel.clearNavigationEvent()
            }
            is NavigationEvent.NavigateToPreview -> {
                // TODO: 미리보기 화면으로 이동
                viewModel.clearNavigationEvent()
            }
            is NavigationEvent.NavigateToSuccess -> {
                // TODO: 성공 화면으로 이동
                viewModel.clearNavigationEvent()
            }
            null -> { /* 아무것도 하지 않음 */ }
        }
    }
    
    // 이전 공고 로드
    LaunchedEffect(previousJobId) {
        previousJobId?.let {
            viewModel.loadPreviousJobPost(it)
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("구인 공고 등록") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.Close, contentDescription = "닫기")
                    }
                },
                actions = {
                    // 임시 저장 버튼
                    TextButton(
                        onClick = { viewModel.saveDraft() }
                    ) {
                        Text("임시저장", color = Color(0xFF4B7BFF))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        },
        bottomBar = {
            JobCreationBottomBar(
                isLoading = uiState.isLoading,
                isFormValid = uiState.isFormValid,
                onCancel = onNavigateBack,
                onSubmit = { viewModel.createJobPosting() }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 기본 정보 섹션
            item {
                BasicInfoSection(
                    basicInfo = uiState.basicInfo,
                    onProjectTitleChange = viewModel::updateProjectTitle,
                    onJobTypeToggle = viewModel::toggleJobType,
                    onRecruitmentCountChange = viewModel::updateRecruitmentCount,
                    onDailyWageChange = viewModel::updateDailyWage,
                    onLocationClick = { viewModel.showLocationDialog(true) }
                )
            }
            
            // 작업 일정 섹션
            item {
                WorkScheduleSection(
                    workSchedule = uiState.workSchedule,
                    onDateToggle = viewModel::toggleDate,
                    onStartTimeClick = { viewModel.showStartTimePicker(true) },
                    onEndTimeClick = { viewModel.showEndTimePicker(true) },
                    onBreakTimeChange = viewModel::updateBreakTime
                )
            }
            
            // 작업 조건 섹션
            item {
                WorkConditionsSection(
                    workConditions = uiState.workConditions,
                    onEnvironmentChange = viewModel::updateWorkEnvironment,
                    onPickupToggle = viewModel::togglePickupProvided,
                    onPickupLocationChange = viewModel::updatePickupLocation,
                    onMealToggle = viewModel::toggleMealProvided,
                    onMealDescriptionChange = viewModel::updateMealDescription,
                    onParkingOptionChange = viewModel::updateParkingOption,
                    onParkingDescriptionChange = viewModel::updateParkingDescription
                )
            }
            
            // 연락처 정보 섹션
            item {
                ContactInfoSection(
                    contactInfo = uiState.contactInfo,
                    onManagerNameChange = viewModel::updateManagerName,
                    onContactNumberChange = viewModel::updateContactNumber
                )
            }
            
            // 추가 정보 섹션
            item {
                AdditionalInfoSection(
                    additionalInfo = uiState.additionalInfo,
                    onRequirementsChange = viewModel::updateRequirements,
                    onUrgentToggle = viewModel::toggleUrgent,
                    onUrgentReasonChange = viewModel::updateUrgentReason,
                    onPhotoAdd = viewModel::addPhotoUrl,
                    onPhotoRemove = viewModel::removePhotoUrl
                )
            }
            
            // 하단 여백
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
        
        // 다이얼로그들
        if (uiState.dialogState.showValidationDialog) {
            ValidationDialog(
                errors = uiState.dialogState.validationErrors,
                onDismiss = { viewModel.showValidationDialog(false) }
            )
        }
        
        if (uiState.dialogState.showUrgentDialog) {
            UrgentRecruitmentDialog(
                urgentReason = uiState.additionalInfo.urgentReason,
                onReasonChange = viewModel::updateUrgentReason,
                onConfirm = {
                    viewModel.showUrgentDialog(false)
                },
                onDismiss = {
                    viewModel.toggleUrgent() // 취소 시 긴급 상태도 해제
                    viewModel.showUrgentDialog(false)
                }
            )
        }
        
        if (uiState.dialogState.showStartTimePicker) {
            TimePickerDialog(
                initialTime = uiState.workSchedule.startTime,
                onTimeSelected = { time ->
                    viewModel.updateStartTime(time)
                    viewModel.showStartTimePicker(false)
                },
                onDismiss = { viewModel.showStartTimePicker(false) }
            )
        }
        
        if (uiState.dialogState.showEndTimePicker) {
            TimePickerDialog(
                initialTime = uiState.workSchedule.endTime,
                onTimeSelected = { time ->
                    viewModel.updateEndTime(time)
                    viewModel.showEndTimePicker(false)
                },
                onDismiss = { viewModel.showEndTimePicker(false) }
            )
        }
        
        if (uiState.dialogState.showSuccessDialog) {
            SuccessDialog(
                onDismiss = {
                    viewModel.showSuccessDialog(false)
                    onNavigateBack()
                }
            )
        }
        
        // 에러 스낵바
        uiState.error?.let { error ->
            Snackbar(
                modifier = Modifier.padding(16.dp),
                action = {
                    TextButton(onClick = viewModel::clearError) {
                        Text("닫기", color = Color.White)
                    }
                },
                containerColor = Color(0xFFE57373)
            ) {
                Text(error, color = Color.White)
            }
        }
    }
}

/**
 * 하단 버튼 바
 */
@Composable
private fun JobCreationBottomBar(
    isLoading: Boolean,
    isFormValid: Boolean,
    onCancel: () -> Unit,
    onSubmit: () -> Unit
) {
    Surface(
        shadowElevation = 8.dp,
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f),
                enabled = !isLoading
            ) {
                Text("취소")
            }
            
            Button(
                onClick = onSubmit,
                modifier = Modifier.weight(1f),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4B7BFF)
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("등록하기")
                }
            }
        }
    }
}

/**
 * 검증 에러 다이얼로그
 */
@Composable
private fun ValidationDialog(
    errors: List<String>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "입력 확인",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text("다음 항목들을 확인해주세요:")
                Spacer(modifier = Modifier.height(8.dp))
                errors.forEach { error ->
                    Text(
                        "• $error",
                        color = Color(0xFFE57373),
                        fontSize = 14.sp
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("확인")
            }
        },
        containerColor = Color.White
    )
}

/**
 * 성공 다이얼로그
 */
@Composable
private fun SuccessDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(48.dp)
            )
        },
        title = {
            Text(
                "등록 완료",
                fontWeight = FontWeight.Bold,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        },
        text = {
            Text(
                "구인 공고가 성공적으로 등록되었습니다.",
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("확인")
            }
        },
        containerColor = Color.White
    )
}