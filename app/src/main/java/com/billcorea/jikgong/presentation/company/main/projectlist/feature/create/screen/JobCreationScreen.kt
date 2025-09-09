package com.billcorea.jikgong.presentation.company.main.projectlist.feature.create.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.compose.ui.unit.sp
import com.billcorea.jikgong.presentation.company.main.common.CompanyTopBar
import com.billcorea.jikgong.presentation.company.main.projectlist.feature.create.model.*
import com.billcorea.jikgong.presentation.company.main.projectlist.feature.create.components.UrgentRecruitmentDialog
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.LocalTime
import java.util.*
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.foundation.layout.ExperimentalLayoutApi

// 기존 공고 데이터 (PreviousJobPostsScreen과 동일한 구조)
data class PreviousJobPost(
  val id: String,
  val title: String,
  val category: String,
  val location: String,
  val wage: Int,
  val workPeriod: String,
  val maxWorkers: Int,
  val completedDate: java.time.LocalDate,
  val totalApplicants: Int
)

// 샘플 데이터에서 기존 공고를 ID로 찾는 함수
fun getPreviousJobPostById(id: String): PreviousJobPost? {
  val samplePosts = listOf(
    PreviousJobPost(
      id = "1",
      title = "아파트 신축공사 철근 작업자 모집",
      category = "철근공",
      location = "서울시 강남구 역삼동",
      wage = 200000,
      workPeriod = "2025-07-15 ~ 2025-08-30",
      maxWorkers = 15,
      completedDate = java.time.LocalDate.now().minusDays(7),
      totalApplicants = 23
    ),
    PreviousJobPost(
      id = "2",
      title = "사무실 인테리어 목공 인력 모집",
      category = "목공",
      location = "서울시 서초구 서초동",
      wage = 180000,
      workPeriod = "2025-06-20 ~ 2025-07-25",
      maxWorkers = 10,
      completedDate = java.time.LocalDate.now().minusDays(14),
      totalApplicants = 18
    ),
    PreviousJobPost(
      id = "3",
      title = "상가건물 전기공 모집",
      category = "전기공",
      location = "서울시 용산구 이태원동",
      wage = 220000,
      workPeriod = "2025-05-10 ~ 2025-06-30",
      maxWorkers = 8,
      completedDate = java.time.LocalDate.now().minusDays(21),
      totalApplicants = 15
    ),
    PreviousJobPost(
      id = "4",
      title = "아파트 리모델링 미장공 모집",
      category = "미장공",
      location = "서울시 마포구 홍대입구",
      wage = 190000,
      workPeriod = "2025-04-01 ~ 2025-05-20",
      maxWorkers = 12,
      completedDate = java.time.LocalDate.now().minusDays(35),
      totalApplicants = 20
    )
  )
  return samplePosts.find { it.id == id }
}

// 실제 사용될 Screen 함수 (Navigation에서 사용)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobCreationScreen(
  onNavigateBack: () -> Unit,
  reuseJobPostId: String? = null,
  projectStartDate: String? = null,
  projectEndDate: String? = null
) {
  // 기존 공고 데이터를 가져와서 초기값으로 설정
  val initialUiState = remember(reuseJobPostId) {
    if (reuseJobPostId != null) {
      // 실제로는 reuseJobPostId를 사용해서 데이터를 가져와야 하지만, 
      // 지금은 샘플 데이터를 사용
      getPreviousJobPostById(reuseJobPostId)?.let { jobPost ->
        ProjectCreateUiState(
          projectName = jobPost.title,
          category = jobPost.category,
          workLocation = jobPost.location,
          wage = jobPost.wage.toString(),
          maxApplicants = jobPost.maxWorkers.toString(),
          description = "기존 공고에서 복사된 내용입니다.",
          isFormValid = false,
          isLoading = false
        )
      } ?: ProjectCreateUiState(
        projectName = "",
        category = "",
        maxApplicants = "",
        wage = "",
        startDate = null,
        isFormValid = false,
        isLoading = false
      )
    } else {
      ProjectCreateUiState(
        projectName = "",
        category = "",
        maxApplicants = "",
        wage = "",
        startDate = null,
        isFormValid = false,
        isLoading = false
      )
    }
  }
  
  // 실제 구현에서는 ViewModel을 사용하되, Preview에서는 사용하지 않음
  JobCreationScreenContent(
    uiState = initialUiState,
    onEvent = { },
    onNavigateBack = onNavigateBack,
    isReuseMode = reuseJobPostId != null,
    projectStartDate = projectStartDate,
    projectEndDate = projectEndDate
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobCreationScreenContent(
  uiState: ProjectCreateUiState,
  onEvent: (ProjectCreateEvent) -> Unit,
  onNavigateBack: () -> Unit,
  isReuseMode: Boolean = false,
  projectStartDate: String? = null,
  projectEndDate: String? = null
) {
  ProjectCreateScreenContent(
    uiState = uiState,
    onEvent = onEvent,
    onNavigateBack = onNavigateBack,
    isReuseMode = isReuseMode,
    projectStartDate = projectStartDate,
    projectEndDate = projectEndDate
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProjectCreateScreenContent(
  uiState: ProjectCreateUiState,
  onEvent: (ProjectCreateEvent) -> Unit,
  onNavigateBack: () -> Unit,
  isReuseMode: Boolean = false,
  projectStartDate: String? = null,
  projectEndDate: String? = null
) {
  Scaffold(
    topBar = {
      CompanyTopBar(
        title = if (isReuseMode) "공고 재등록" else "일자리 등록",
        showBackButton = true,
        onBackClick = onNavigateBack,
        actions = {
          IconButton(onClick = onNavigateBack) {
            Icon(Icons.Default.Close, contentDescription = "닫기")
          }
        }
      )
    },
    bottomBar = {
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
            onClick = onNavigateBack,
            modifier = Modifier.weight(1f)
          ) {
            Text("취소")
          }
          var showValidationDialog by remember { mutableStateOf(false) }
          Button(
            onClick = { 
              // 필수 필드 검증
              val missingFields = mutableListOf<String>()
              
              // TODO: 실제 값들을 검증하도록 수정 필요
              if (uiState.projectName.isBlank()) missingFields.add("제목")
              if (uiState.category.isBlank()) missingFields.add("직종")
              if (uiState.maxApplicants.isBlank()) missingFields.add("모집인원")
              if (uiState.wage.isBlank()) missingFields.add("일급")
              
              if (missingFields.isEmpty()) {
                onEvent(ProjectCreateEvent.CreateProject)
              } else {
                showValidationDialog = true
              }
            },
            modifier = Modifier.weight(1f),
            enabled = !uiState.isLoading, // 항상 활성화 (로딩 중일 때만 비활성화)
            colors = ButtonDefaults.buttonColors(
              containerColor = Color(0xFF4B7BFF)
            )
          ) {
            if (uiState.isLoading) {
              CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = Color.White
              )
            } else {
              Text("등록하기")
            }
          }
          
          // 필수 필드 미입력 다이얼로그
          if (showValidationDialog) {
            AlertDialog(
              onDismissRequest = { showValidationDialog = false },
              title = { 
                Text(
                  "등록 오류",
                  fontWeight = FontWeight.Bold
                )
              },
              text = { 
                Column {
                  Text("다음 항목들을 입력해주세요:")
                  Spacer(modifier = Modifier.height(8.dp))
                  val missingFields = mutableListOf<String>()
                  if (uiState.projectName.isBlank()) missingFields.add("제목")
                  if (uiState.category.isBlank()) missingFields.add("직종")
                  if (uiState.maxApplicants.isBlank()) missingFields.add("모집인원")
                  if (uiState.wage.isBlank()) missingFields.add("일급")
                  
                  missingFields.forEach { field ->
                    Text(
                      "• $field",
                      color = Color(0xFFE57373),
                      fontSize = 14.sp
                    )
                  }
                }
              },
              confirmButton = {
                TextButton(
                  onClick = { showValidationDialog = false }
                ) {
                  Text("확인")
                }
              },
              containerColor = Color.White
            )
          }
        }
      }
    }
  ) { paddingValues ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
        .background(Color(0xFFF5F5F5)),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      // 모집 요강 제목
      item {
        Text(
          "모집 요강",
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF1A1A1A),
          modifier = Modifier.padding(vertical = 8.dp)
        )
      }
      
      // 제목
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              "제목 *",
              fontSize = 14.sp,
              fontWeight = FontWeight.Medium,
              color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
              value = uiState.projectName,
              onValueChange = {
                onEvent(ProjectCreateEvent.UpdateProjectName(it))
              },
              modifier = Modifier.fillMaxWidth(),
              placeholder = { Text("예: 아파트 신축공사 철근공 모집") },
              singleLine = true
            )
          }
        }
      }

      // 직종 (다중 선택 가능)
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              "직종 * (복수 선택 가능)",
              fontSize = 14.sp,
              fontWeight = FontWeight.Medium,
              color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            // 선택된 직종들 표시
            var selectedJobTypes by remember { mutableStateOf(setOf<String>()) }
            if (selectedJobTypes.isNotEmpty()) {
              Text(
                text = "선택된 직종: ${selectedJobTypes.joinToString(", ")}",
                fontSize = 12.sp,
                color = Color(0xFF2196F3),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
              )
            }
            
            // 직종 선택 - 세로 3줄로 배치
            val jobTypes = listOf("철근공", "목공", "전기공", "배관공", "타일공", "보통인부", "미장공", "용접공", "기타")
            Column(
              verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              // 첫 번째 줄 (3개)
              Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                jobTypes.take(3).forEach { jobType ->
                  FilterChip(
                    selected = selectedJobTypes.contains(jobType),
                    onClick = {
                      selectedJobTypes = if (selectedJobTypes.contains(jobType)) {
                        selectedJobTypes - jobType
                      } else {
                        selectedJobTypes + jobType
                      }
                    },
                    label = { 
                      Text(
                        text = jobType,
                        fontSize = 14.sp,
                        maxLines = 1
                      )
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                  )
                }
              }
              // 두 번째 줄 (3개)
              Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                jobTypes.drop(3).take(3).forEach { jobType ->
                  FilterChip(
                    selected = selectedJobTypes.contains(jobType),
                    onClick = {
                      selectedJobTypes = if (selectedJobTypes.contains(jobType)) {
                        selectedJobTypes - jobType
                      } else {
                        selectedJobTypes + jobType
                      }
                    },
                    label = { 
                      Text(
                        text = jobType,
                        fontSize = 14.sp,
                        maxLines = 1
                      )
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                  )
                }
              }
              // 세 번째 줄 (3개)
              Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
              ) {
                jobTypes.drop(6).forEach { jobType ->
                  FilterChip(
                    selected = selectedJobTypes.contains(jobType),
                    onClick = {
                      selectedJobTypes = if (selectedJobTypes.contains(jobType)) {
                        selectedJobTypes - jobType
                      } else {
                        selectedJobTypes + jobType
                      }
                    },
                    label = { 
                      Text(
                        text = jobType,
                        fontSize = 14.sp,
                        maxLines = 1
                      )
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                  )
                }
                // 빈 공간 채우기
                repeat(3 - jobTypes.drop(6).size) {
                  Spacer(modifier = Modifier.weight(1f))
                }
              }
            }
          }
        }
      }

      // 모집 인원 및 일급
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  "모집 인원 *",
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Medium,
                  color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                var recruitmentCountLocal by remember { mutableStateOf("") }
                OutlinedTextField(
                  value = recruitmentCountLocal,
                  onValueChange = { input ->
                    // 숫자만 입력 허용
                    val numericValue = input.filter { it.isDigit() }
                    if (numericValue.length <= 3) { // 최대 3자리 제한
                      recruitmentCountLocal = numericValue
                    }
                  },
                  modifier = Modifier.fillMaxWidth(),
                  placeholder = { Text("15") },
                  suffix = { Text("명") },
                  keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                  singleLine = true
                )
              }
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  "일급 *",
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Medium,
                  color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                var wageLocal by remember { mutableStateOf("") }
                OutlinedTextField(
                  value = if (wageLocal.isNotEmpty()) {
                    NumberFormat.getNumberInstance(Locale.KOREA).format(
                      wageLocal.toLongOrNull() ?: 0
                    )
                  } else "",
                  onValueChange = { input ->
                    val numericValue = input.replace(",", "").filter { it.isDigit() }
                    if (numericValue.length <= 8) { // 최대 8자리 제한 (99,999,999원)
                      wageLocal = numericValue
                    }
                  },
                  modifier = Modifier.fillMaxWidth(),
                  placeholder = { Text("200,000") },
                  suffix = { Text("원") },
                  keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                  singleLine = true
                )
              }
            }
          }
        }
      }

      // 작업일
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              "작업일 *",
              fontSize = 14.sp,
              fontWeight = FontWeight.Medium,
              color = Color.Gray
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            // 달력 UI
            var selectedDates by remember { mutableStateOf(setOf<LocalDate>()) }
            val calendarStartDate = remember(projectStartDate) {
              projectStartDate?.let { LocalDate.parse(it) } ?: LocalDate.of(2025, 1, 1)
            }
            val calendarEndDate = remember(projectEndDate) {
              projectEndDate?.let { LocalDate.parse(it) } ?: LocalDate.of(2025, 12, 31)
            }
            
            WorkDateCalendar(
              selectedDates = selectedDates,
              onDateSelected = { date ->
                selectedDates = if (selectedDates.contains(date)) {
                  selectedDates - date
                } else {
                  selectedDates + date
                }
              },
              projectStartDate = calendarStartDate,
              projectEndDate = calendarEndDate,
              modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 선택된 날짜 표시
            SelectedDatesDisplay(
              selectedDates = selectedDates,
              modifier = Modifier.fillMaxWidth()
            )
          }
        }
      }

      // 근무시간
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              "근무시간 *",
              fontSize = 14.sp,
              fontWeight = FontWeight.Medium,
              color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            WorkHoursTimePicker(
              startTime = LocalTime.of(8, 0), // TODO: 상태 추가 필요
              endTime = LocalTime.of(18, 0), // TODO: 상태 추가 필요
              onStartTimeSelected = { time ->
                // TODO: 시작시간 선택 이벤트 처리
              },
              onEndTimeSelected = { time ->
                // TODO: 종료시간 선택 이벤트 처리
              },
              modifier = Modifier.fillMaxWidth()
            )
          }
        }
      }

      // 근무환경
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              "근무환경",
              fontSize = 14.sp,
              fontWeight = FontWeight.Medium,
              color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            var workEnvironment by remember { mutableStateOf("") }
            OutlinedTextField(
              value = workEnvironment,
              onValueChange = { workEnvironment = it },
              modifier = Modifier.fillMaxWidth(),
              placeholder = { Text("작업 환경에 대한 설명을 입력하세요") },
              minLines = 3,
              maxLines = 5
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
              onClick = { /* TODO: 사진 추가 기능 */ },
              modifier = Modifier.fillMaxWidth()
            ) {
              Icon(Icons.Default.Add, contentDescription = null)
              Spacer(modifier = Modifier.width(8.dp))
              Text("사진 추가")
            }
          }
        }
      }

      // 픽업장소
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            var pickupProvided by remember { mutableStateOf(false) }
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  "픽업장소 제공",
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Medium,
                  color = Color.Gray
                )
                Text(
                  text = if (pickupProvided) "픽업장소를 제공합니다" else "픽업장소를 제공하지 않습니다",
                  fontSize = 12.sp,
                  color = if (pickupProvided) Color(0xFF4CAF50) else Color(0xFF757575)
                )
              }
              Switch(
                checked = pickupProvided,
                onCheckedChange = { pickupProvided = it }
              )
            }
            // TODO: 픽업장소 제공시 추가 필드들
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
              onClick = { /* TODO: 픽업장소 추가 */ },
              modifier = Modifier.fillMaxWidth()
            ) {
              Icon(Icons.Default.Add, contentDescription = null)
              Spacer(modifier = Modifier.width(8.dp))
              Text("픽업장소 추가")
            }
          }
        }
      }

      // 식사제공 여부
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
          var mealProvided by remember { mutableStateOf(false) }
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                "식사제공 여부",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
              )
              Text(
                text = if (mealProvided) "식사를 제공합니다" else "식사를 제공하지 않습니다",
                fontSize = 12.sp,
                color = if (mealProvided) Color(0xFF4CAF50) else Color(0xFF757575)
              )
            }
            Switch(
              checked = mealProvided,
              onCheckedChange = { mealProvided = it }
            )
          }
        }
      }

      // 주차공간 제공여부
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              "주차공간 제공여부",
              fontSize = 14.sp,
              fontWeight = FontWeight.Medium,
              color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            var selectedParkingOption by remember { mutableStateOf("") }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              listOf("무료주차", "유료주차", "제공 안함").forEach { option ->
                FilterChip(
                  selected = selectedParkingOption == option,
                  onClick = { selectedParkingOption = option },
                  label = { Text(option) }
                )
              }
            }
            Spacer(modifier = Modifier.height(8.dp))
            var parkingDescription by remember { mutableStateOf("") }
            OutlinedTextField(
              value = parkingDescription,
              onValueChange = { parkingDescription = it },
              modifier = Modifier.fillMaxWidth(),
              placeholder = { Text("주차공간에 대한 추가 설명") },
              minLines = 2,
              maxLines = 3
            )
          }
        }
      }

      // 준비물
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              "준비물",
              fontSize = 14.sp,
              fontWeight = FontWeight.Medium,
              color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            var requirements by remember { mutableStateOf("") }
            OutlinedTextField(
              value = requirements,
              onValueChange = { requirements = it },
              modifier = Modifier.fillMaxWidth(),
              placeholder = { Text("예: 신분증, 이수증, 작업복, 안전화 등") },
              minLines = 3,
              maxLines = 5
            )
          }
        }
      }

      // 담당자 정보
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              "담당자 정보",
              fontSize = 14.sp,
              fontWeight = FontWeight.Medium,
              color = Color.Gray
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
              var managerName by remember { mutableStateOf("") }
              var contactNumber by remember { mutableStateOf("") }
              OutlinedTextField(
                value = managerName,
                onValueChange = { managerName = it },
                modifier = Modifier.weight(1f),
                label = { Text("담당자명 *") },
                placeholder = { Text("홍길동") },
                singleLine = true
              )
              OutlinedTextField(
                value = contactNumber,
                onValueChange = { input ->
                  // 전화번호 형식 자동 적용 (010-1234-5678)
                  val numericValue = input.filter { it.isDigit() }
                  val formattedNumber = when {
                    numericValue.length <= 3 -> numericValue
                    numericValue.length <= 7 -> "${numericValue.substring(0, 3)}-${numericValue.substring(3)}"
                    numericValue.length <= 11 -> "${numericValue.substring(0, 3)}-${numericValue.substring(3, 7)}-${numericValue.substring(7)}"
                    else -> "${numericValue.substring(0, 3)}-${numericValue.substring(3, 7)}-${numericValue.substring(7, 11)}"
                  }
                  if (numericValue.length <= 11) {
                    contactNumber = formattedNumber
                  }
                },
                modifier = Modifier.weight(1f),
                label = { Text("연락처 *") },
                placeholder = { Text("010-1234-5678") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true
              )
            }
          }
        }
      }

      // 긴급 모집
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                "긴급 모집으로 표시",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
              )
              Text(
                "상단에 긴급 뱃지가 표시됩니다",
                fontSize = 12.sp,
                color = Color.Gray
              )
            }
            var isUrgentLocal by remember { mutableStateOf(false) }
            var showUrgentDialog by remember { mutableStateOf(false) }
            Switch(
              checked = isUrgentLocal,
              onCheckedChange = { newValue ->
                if (newValue) {
                  showUrgentDialog = true
                } else {
                  isUrgentLocal = false
                }
              }
            )
            
            // 긴급 모집 확인 다이얼로그
            if (showUrgentDialog) {
              UrgentRecruitmentDialog(
                onConfirm = {
                  isUrgentLocal = true
                  showUrgentDialog = false
                },
                onDismiss = {
                  isUrgentLocal = false
                  showUrgentDialog = false
                }
              )
            }
          }
        }
      }
    }
  }

  // 성공 다이얼로그
  if (uiState.showSuccessDialog) {
    AlertDialog(
      onDismissRequest = { },
      icon = {
        Icon(
          Icons.Default.CheckCircle,
          contentDescription = null,
          modifier = Modifier.size(48.dp),
          tint = Color(0xFF4CAF50)
        )
      },
      title = {
        Text(
          "프로젝트 등록 완료",
          textAlign = TextAlign.Center
        )
      },
      text = {
        Text(
          "프로젝트가 성공적으로 등록되었습니다",
          textAlign = TextAlign.Center
        )
      },
      confirmButton = {
        TextButton(
          onClick = {
            onEvent(ProjectCreateEvent.DismissSuccessDialog)
          }
        ) {
          Text("확인")
        }
      }
    )
  }

  // 에러 메시지
  uiState.errorMessage?.let { error ->
    AlertDialog(
      onDismissRequest = {
        onEvent(ProjectCreateEvent.DismissError)
      },
      title = { Text("오류") },
      text = { Text(error) },
      confirmButton = {
        TextButton(
          onClick = {
            onEvent(ProjectCreateEvent.DismissError)
          }
        ) {
          Text("확인")
        }
      }
    )
  }

  // 긴급 모집 상태를 아이템에서 관리하도록 수정
  // (중복 선언 제거)
}

@Preview(showBackground = true, heightDp = 800)
@Composable
fun JobCreationScreenPreview() {
  // Preview를 위한 완전한 UI 렌더링 (상태 관리 포함)
  MaterialTheme {
    var projectTitle by remember { mutableStateOf("아파트 신축공사 철근공 모집") }
    var selectedJobType by remember { mutableStateOf("철근공") }
    var recruitmentCount by remember { mutableStateOf("") }
    var dailyWage by remember { mutableStateOf("") }
    var workEnvironment by remember { mutableStateOf("실내 작업 환경이며, 안전장비 착용 필수입니다.") }
    var pickupProvided by remember { mutableStateOf(true) }
    var mealProvided by remember { mutableStateOf(false) }
    var selectedParkingOption by remember { mutableStateOf("무료주차") }
    var parkingDescription by remember { mutableStateOf("건물 지하 주차장 이용 가능") }
    var requirements by remember { mutableStateOf("신분증, 안전화, 작업복") }
    var managerName by remember { mutableStateOf("김현수") }
    var contactNumber by remember { mutableStateOf("010-1234-5678") }
    var isUrgent by remember { mutableStateOf(false) }
    
    Surface(
      modifier = Modifier.fillMaxSize(),
      color = Color(0xFFF5F5F5)
    ) {
      LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        // 타이틀
        item {
          Text(
            "모집 요강",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A),
            modifier = Modifier.padding(vertical = 8.dp)
          )
        }
        
        // 제목
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Text(
                "제목 *",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
              )
              Spacer(modifier = Modifier.height(8.dp))
              var projectTitleLocal by remember { mutableStateOf("아파트 신축공사 철근공 모집") }
              OutlinedTextField(
                value = projectTitleLocal,
                onValueChange = { projectTitleLocal = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("예: 아파트 신축공사 철근공 모집") },
                singleLine = true
              )
            }
          }
        }

        // 직종 선택 (다중 선택)
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Text(
                "직종 * (복수 선택 가능)",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
              )
              Spacer(modifier = Modifier.height(8.dp))
              
              var selectedJobTypes by remember { mutableStateOf(setOf("철근공", "목공")) }
              if (selectedJobTypes.isNotEmpty()) {
                Text(
                  text = "선택된 직종: ${selectedJobTypes.joinToString(", ")}",
                  fontSize = 12.sp,
                  color = Color(0xFF2196F3),
                  fontWeight = FontWeight.Medium,
                  modifier = Modifier.padding(bottom = 8.dp)
                )
              }
              
              val jobTypes = listOf("철근공", "목공", "전기공", "배관공", "타일공", "보통인부", "미장공", "용접공", "기타")
              Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // 첫 번째 줄
                Row(
                  horizontalArrangement = Arrangement.spacedBy(8.dp),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  jobTypes.take(3).forEach { jobType ->
                    FilterChip(
                      selected = selectedJobTypes.contains(jobType),
                      onClick = {
                        selectedJobTypes = if (selectedJobTypes.contains(jobType)) {
                          selectedJobTypes - jobType
                        } else {
                          selectedJobTypes + jobType
                        }
                      },
                      label = { Text(jobType, fontSize = 14.sp, maxLines = 1) },
                      modifier = Modifier.weight(1f),
                      shape = RoundedCornerShape(12.dp)
                    )
                  }
                }
                // 두 번째 줄
                Row(
                  horizontalArrangement = Arrangement.spacedBy(8.dp),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  jobTypes.drop(3).take(3).forEach { jobType ->
                    FilterChip(
                      selected = selectedJobTypes.contains(jobType),
                      onClick = {
                        selectedJobTypes = if (selectedJobTypes.contains(jobType)) {
                          selectedJobTypes - jobType
                        } else {
                          selectedJobTypes + jobType
                        }
                      },
                      label = { Text(jobType, fontSize = 14.sp, maxLines = 1) },
                      modifier = Modifier.weight(1f),
                      shape = RoundedCornerShape(12.dp)
                    )
                  }
                }
                // 세 번째 줄
                Row(
                  horizontalArrangement = Arrangement.spacedBy(8.dp),
                  modifier = Modifier.fillMaxWidth()
                ) {
                  jobTypes.drop(6).forEach { jobType ->
                    FilterChip(
                      selected = selectedJobTypes.contains(jobType),
                      onClick = {
                        selectedJobTypes = if (selectedJobTypes.contains(jobType)) {
                          selectedJobTypes - jobType
                        } else {
                          selectedJobTypes + jobType
                        }
                      },
                      label = { Text(jobType, fontSize = 14.sp, maxLines = 1) },
                      modifier = Modifier.weight(1f),
                      shape = RoundedCornerShape(12.dp)
                    )
                  }
                  repeat(3 - jobTypes.drop(6).size) {
                    Spacer(modifier = Modifier.weight(1f))
                  }
                }
              }
            }
          }
        }

        // 모집 인원 및 일급
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
              ) {
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    "모집 인원 *",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                  )
                  Spacer(modifier = Modifier.height(8.dp))
                  OutlinedTextField(
                    value = recruitmentCount,
                    onValueChange = { input ->
                      val numericValue = input.filter { it.isDigit() }
                      if (numericValue.length <= 3) {
                        recruitmentCount = numericValue
                      }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("15") },
                    suffix = { Text("명") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                  )
                }
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    "일급 *",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                  )
                  Spacer(modifier = Modifier.height(8.dp))
                  OutlinedTextField(
                    value = if (dailyWage.isNotEmpty()) {
                      NumberFormat.getNumberInstance(Locale.KOREA).format(
                        dailyWage.toLongOrNull() ?: 0
                      )
                    } else "",
                    onValueChange = { input ->
                      val numericValue = input.replace(",", "").filter { it.isDigit() }
                      if (numericValue.length <= 8) {
                        dailyWage = numericValue
                      }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("200,000") },
                    suffix = { Text("원") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                  )
                }
              }
            }
          }
        }

        // 근무환경 (수정 가능)
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Text(
                "근무환경",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
              )
              Spacer(modifier = Modifier.height(8.dp))
              OutlinedTextField(
                value = workEnvironment,
                onValueChange = { workEnvironment = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("작업 환경에 대한 설명을 입력하세요") },
                minLines = 3,
                maxLines = 5
              )
            }
          }
        }

        // 픽업장소 제공 (토글 기능)
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  "픽업장소 제공",
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Medium,
                  color = Color.Gray
                )
                Text(
                  text = if (pickupProvided) "픽업장소를 제공합니다" else "픽업장소를 제공하지 않습니다",
                  fontSize = 12.sp,
                  color = if (pickupProvided) Color(0xFF4CAF50) else Color(0xFF757575)
                )
              }
              Switch(
                checked = pickupProvided,
                onCheckedChange = { pickupProvided = it }
              )
            }
          }
        }

        // 식사제공 여부 (토글 기능)
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  "식사제공 여부",
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Medium,
                  color = Color.Gray
                )
                Text(
                  text = if (mealProvided) "식사를 제공합니다" else "식사를 제공하지 않습니다",
                  fontSize = 12.sp,
                  color = if (mealProvided) Color(0xFF4CAF50) else Color(0xFF757575)
                )
              }
              Switch(
                checked = mealProvided,
                onCheckedChange = { mealProvided = it }
              )
            }
          }
        }

        // 주차공간 제공여부
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Text(
                "주차공간 제공여부",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
              )
              Spacer(modifier = Modifier.height(8.dp))
              Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("무료주차", "유료주차", "제공 안함").forEach { option ->
                  FilterChip(
                    selected = selectedParkingOption == option,
                    onClick = { selectedParkingOption = option },
                    label = { Text(option) }
                  )
                }
              }
              Spacer(modifier = Modifier.height(8.dp))
              OutlinedTextField(
                value = parkingDescription,
                onValueChange = { parkingDescription = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("주차공간에 대한 추가 설명") },
                minLines = 2,
                maxLines = 3
              )
            }
          }
        }

        // 준비물
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Text(
                "준비물",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
              )
              Spacer(modifier = Modifier.height(8.dp))
              OutlinedTextField(
                value = requirements,
                onValueChange = { requirements = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("예: 신분증, 이수증, 작업복, 안전화 등") },
                minLines = 3,
                maxLines = 5
              )
            }
          }
        }

        // 담당자 정보
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Text(
                "담당자 정보",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
              )
              Spacer(modifier = Modifier.height(12.dp))
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
              ) {
                OutlinedTextField(
                  value = managerName,
                  onValueChange = { managerName = it },
                  modifier = Modifier.weight(1f),
                  label = { Text("담당자명 *") },
                  placeholder = { Text("홍길동") },
                  singleLine = true
                )
                OutlinedTextField(
                  value = contactNumber,
                  onValueChange = { input ->
                    val numericValue = input.filter { it.isDigit() }
                    val formattedNumber = when {
                      numericValue.length <= 3 -> numericValue
                      numericValue.length <= 7 -> "${numericValue.substring(0, 3)}-${numericValue.substring(3)}"
                      numericValue.length <= 11 -> "${numericValue.substring(0, 3)}-${numericValue.substring(3, 7)}-${numericValue.substring(7)}"
                      else -> "${numericValue.substring(0, 3)}-${numericValue.substring(3, 7)}-${numericValue.substring(7, 11)}"
                    }
                    if (numericValue.length <= 11) {
                      contactNumber = formattedNumber
                    }
                  },
                  modifier = Modifier.weight(1f),
                  label = { Text("연락처 *") },
                  placeholder = { Text("010-1234-5678") },
                  keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                  singleLine = true
                )
              }
            }
          }
        }

        // 긴급 모집
        item {
          Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White)
          ) {
            var showUrgentDialogPreview by remember { mutableStateOf(false) }
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column {
                Text(
                  "긴급 모집으로 표시",
                  fontSize = 16.sp,
                  fontWeight = FontWeight.Medium
                )
                Text(
                  "상단에 긴급 뱃지가 표시됩니다",
                  fontSize = 12.sp,
                  color = Color.Gray
                )
              }
              Switch(
                checked = isUrgent,
                onCheckedChange = { newValue ->
                  if (newValue) {
                    showUrgentDialogPreview = true
                  } else {
                    isUrgent = false
                  }
                }
              )
            }
            
            // Preview용 긴급 모집 다이얼로그
            if (showUrgentDialogPreview) {
              UrgentRecruitmentDialog(
                onConfirm = {
                  isUrgent = true
                  showUrgentDialogPreview = false
                },
                onDismiss = {
                  isUrgent = false
                  showUrgentDialogPreview = false
                }
              )
            }
          }
        }
        
        // 등록 성공 메시지 및 유효성 검사 결과
        item {
          var showSuccessDialog by remember { mutableStateOf(false) }
          var showValidationDialogPreview by remember { mutableStateOf(false) }
          
          Column {
            // 등록 성공 다이얼로그 예시
            OutlinedButton(
              onClick = { showSuccessDialog = true },
              modifier = Modifier.fillMaxWidth()
            ) {
              Text("등록 성공 다이얼로그 보기")
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // 유효성 검사 다이얼로그 예시
            OutlinedButton(
              onClick = { showValidationDialogPreview = true },
              modifier = Modifier.fillMaxWidth()
            ) {
              Text("유효성 검사 다이얼로그 보기")
            }
          }
          
          // 성공 다이얼로그
          if (showSuccessDialog) {
            AlertDialog(
              onDismissRequest = { showSuccessDialog = false },
              icon = {
                Icon(
                  Icons.Default.CheckCircle,
                  contentDescription = null,
                  modifier = Modifier.size(48.dp),
                  tint = Color(0xFF4CAF50)
                )
              },
              title = {
                Text(
                  "프로젝트 등록 완료",
                  textAlign = TextAlign.Center
                )
              },
              text = {
                Text(
                  "프로젝트가 성공적으로 등록되었습니다",
                  textAlign = TextAlign.Center
                )
              },
              confirmButton = {
                TextButton(
                  onClick = { showSuccessDialog = false }
                ) {
                  Text("확인")
                }
              }
            )
          }
          
          // 유효성 검사 다이얼로그
          if (showValidationDialogPreview) {
            AlertDialog(
              onDismissRequest = { showValidationDialogPreview = false },
              title = { 
                Text(
                  "등록 오류",
                  fontWeight = FontWeight.Bold
                )
              },
              text = { 
                Column {
                  Text("다음 항목들을 입력해주세요:")
                  Spacer(modifier = Modifier.height(8.dp))
                  listOf("제목", "직종", "모집인원", "일급").forEach { field ->
                    Text(
                      "• $field",
                      color = Color(0xFFE57373),
                      fontSize = 14.sp
                    )
                  }
                }
              },
              confirmButton = {
                TextButton(
                  onClick = { showValidationDialogPreview = false }
                ) {
                  Text("확인")
                }
              },
              containerColor = Color.White
            )
          }
        }
      }
    }
  }
}

@Composable
fun WorkDateCalendar(
  selectedDates: Set<LocalDate>,
  onDateSelected: (LocalDate) -> Unit,
  projectStartDate: LocalDate,
  projectEndDate: LocalDate,
  modifier: Modifier = Modifier
) {
  val currentDate = LocalDate.now()
  val currentMonth = remember { mutableStateOf(currentDate.withDayOfMonth(1)) }
  
  Column(modifier = modifier) {
    // 월 헤더
    Card(
      colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
      elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = {
            currentMonth.value = currentMonth.value.minusMonths(1)
          }
        ) {
          Icon(Icons.Default.ChevronLeft, contentDescription = "이전 달")
        }
        
        Text(
          text = currentMonth.value.format(DateTimeFormatter.ofPattern("yyyy년 MM월")),
          fontSize = 16.sp,
          fontWeight = FontWeight.Bold
        )
        
        IconButton(
          onClick = {
            currentMonth.value = currentMonth.value.plusMonths(1)
          }
        ) {
          Icon(Icons.Default.ChevronRight, contentDescription = "다음 달")
        }
      }
    }
    
    Spacer(modifier = Modifier.height(8.dp))
    
    // 요일 헤더
    Row(modifier = Modifier.fillMaxWidth()) {
      listOf("일", "월", "화", "수", "목", "금", "토").forEach { day ->
        Box(
          modifier = Modifier.weight(1f),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = day,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray,
            modifier = Modifier.padding(8.dp)
          )
        }
      }
    }
    
    // 달력 그리드
    val firstDayOfMonth = currentMonth.value
    val lastDayOfMonth = firstDayOfMonth.withDayOfMonth(firstDayOfMonth.lengthOfMonth())
    val startDate = firstDayOfMonth.minusDays(firstDayOfMonth.dayOfWeek.value % 7.toLong())
    val endDate = lastDayOfMonth.plusDays(6 - lastDayOfMonth.dayOfWeek.value % 7.toLong())
    
    var currentWeekStart = startDate
    while (currentWeekStart <= endDate) {
      Row(modifier = Modifier.fillMaxWidth()) {
        repeat(7) { dayIndex ->
          val date = currentWeekStart.plusDays(dayIndex.toLong())
          val isCurrentMonth = date.monthValue == firstDayOfMonth.monthValue
          val isSelected = selectedDates.contains(date)
          val isToday = date == currentDate
          val isPast = date.isBefore(currentDate)
          val isWithinProjectRange = !date.isBefore(projectStartDate) && !date.isAfter(projectEndDate)
          val isSelectable = isCurrentMonth && !isPast && isWithinProjectRange
          
          Box(
            modifier = Modifier
              .weight(1f)
              .aspectRatio(1f)
              .padding(2.dp)
              .clip(CircleShape)
              .then(
                if (isSelected) {
                  Modifier.background(Color(0xFF4B7BFF))
                } else if (isToday && isWithinProjectRange) {
                  Modifier.border(2.dp, Color(0xFF4B7BFF), CircleShape)
                } else {
                  Modifier
                }
              )
              .clickable(
                enabled = isSelectable
              ) {
                if (isSelectable) {
                  onDateSelected(date)
                }
              },
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = date.dayOfMonth.toString(),
              fontSize = 14.sp,
              color = when {
                !isCurrentMonth -> Color.LightGray
                isPast -> Color.LightGray
                !isWithinProjectRange -> Color.LightGray
                isSelected -> Color.White
                isToday -> Color(0xFF4B7BFF)
                else -> Color.Black
              },
              fontWeight = if (isSelected || (isToday && isWithinProjectRange)) FontWeight.Bold else FontWeight.Normal
            )
          }
        }
      }
      currentWeekStart = currentWeekStart.plusWeeks(1)
    }
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SelectedDatesDisplay(
  selectedDates: Set<LocalDate>,
  modifier: Modifier = Modifier
) {
  if (selectedDates.isNotEmpty()) {
    Column(modifier = modifier) {
      Text(
        text = "선택된 작업일: ${selectedDates.size}일",
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        color = Color(0xFF4B7BFF)
      )
      
      Spacer(modifier = Modifier.height(8.dp))
      
      FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        selectedDates.sortedBy { it }.forEach { date ->
          Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFE3F2FD),
            modifier = Modifier.padding(2.dp)
          ) {
            Text(
              text = date.format(DateTimeFormatter.ofPattern("MM/dd")),
              fontSize = 12.sp,
              color = Color(0xFF1976D2),
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }
      }
    }
  } else {
    Text(
      text = "작업일을 선택해주세요",
      fontSize = 14.sp,
      color = Color.Gray,
      modifier = modifier
    )
  }
}

@Composable
fun WorkHoursTimePicker(
  startTime: LocalTime,
  endTime: LocalTime,
  onStartTimeSelected: (LocalTime) -> Unit,
  onEndTimeSelected: (LocalTime) -> Unit,
  modifier: Modifier = Modifier
) {
  var showStartTimePicker by remember { mutableStateOf(false) }
  var showEndTimePicker by remember { mutableStateOf(false) }
  
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    // 시작시간
    OutlinedTextField(
      value = startTime.format(DateTimeFormatter.ofPattern("HH:mm")),
      onValueChange = { },
      modifier = Modifier.weight(1f),
      label = { Text("시작시간") },
      readOnly = true,
      trailingIcon = {
        Icon(Icons.Default.Schedule, contentDescription = null)
      },
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color(0xFF4B7BFF),
        focusedLabelColor = Color(0xFF4B7BFF)
      ),
      interactionSource = remember { MutableInteractionSource() }
        .also { interactionSource ->
          LaunchedEffect(interactionSource) {
            interactionSource.interactions.collect { interaction ->
              if (interaction is PressInteraction.Release) {
                showStartTimePicker = true
              }
            }
          }
        }
    )
    
    Text(
      text = "~",
      fontSize = 16.sp,
      fontWeight = FontWeight.Medium
    )
    
    // 종료시간
    OutlinedTextField(
      value = endTime.format(DateTimeFormatter.ofPattern("HH:mm")),
      onValueChange = { },
      modifier = Modifier.weight(1f),
      label = { Text("종료시간") },
      readOnly = true,
      trailingIcon = {
        Icon(Icons.Default.Schedule, contentDescription = null)
      },
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color(0xFF4B7BFF),
        focusedLabelColor = Color(0xFF4B7BFF)
      ),
      interactionSource = remember { MutableInteractionSource() }
        .also { interactionSource ->
          LaunchedEffect(interactionSource) {
            interactionSource.interactions.collect { interaction ->
              if (interaction is PressInteraction.Release) {
                showEndTimePicker = true
              }
            }
          }
        }
    )
  }
  
  // 시작시간 피커 다이얼로그
  if (showStartTimePicker) {
    TimePickerDialog(
      initialTime = startTime,
      onTimeSelected = { selectedTime ->
        onStartTimeSelected(selectedTime)
        showStartTimePicker = false
      },
      onDismiss = { showStartTimePicker = false }
    )
  }
  
  // 종료시간 피커 다이얼로그
  if (showEndTimePicker) {
    TimePickerDialog(
      initialTime = endTime,
      onTimeSelected = { selectedTime ->
        onEndTimeSelected(selectedTime)
        showEndTimePicker = false
      },
      onDismiss = { showEndTimePicker = false }
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
  initialTime: LocalTime,
  onTimeSelected: (LocalTime) -> Unit,
  onDismiss: () -> Unit
) {
  val timePickerState = rememberTimePickerState(
    initialHour = initialTime.hour,
    initialMinute = initialTime.minute
  )

  AlertDialog(
    onDismissRequest = onDismiss,
    title = { Text("시간 선택") },
    text = {
      TimePicker(state = timePickerState)
    },
    confirmButton = {
      TextButton(
        onClick = {
          val selectedTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
          onTimeSelected(selectedTime)
        }
      ) {
        Text("확인")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("취소")
      }
    }
  )
}