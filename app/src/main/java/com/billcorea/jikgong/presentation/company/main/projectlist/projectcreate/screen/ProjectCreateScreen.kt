// app/src/main/java/com/billcorea/jikgong/presentation/company/main/projectlist/projectcreate/screen/ProjectCreateScreen.kt
package com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.screen

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.model.ProjectCreateEvent
import com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.model.ProjectCreateUiEvent
import com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.viewmodel.ProjectCreateViewModel
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ProjectCreateScreen(
  navController: NavController,
  viewModel: ProjectCreateViewModel = hiltViewModel()
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  var showStartDatePicker by remember { mutableStateOf(false) }
  var showEndDatePicker by remember { mutableStateOf(false) }
  var showMapDialog by remember { mutableStateOf(false) }

  LaunchedEffect(viewModel) {
    viewModel.uiEvent.collectLatest { event ->
      when (event) {
        is ProjectCreateUiEvent.NavigateBack -> {
          navController.popBackStack()
        }
        is ProjectCreateUiEvent.ShowError -> {
          // Toast나 SnackBar로 에러 표시
        }
      }
    }
  }

  ProjectCreateContent(
    uiState = uiState,
    onEvent = viewModel::handleEvent,
    onBackClick = { navController.popBackStack() },
    onShowStartDatePicker = { showStartDatePicker = true },
    onShowEndDatePicker = { showEndDatePicker = true },
    onShowMapDialog = { showMapDialog = true }
  )

  // 날짜 선택 다이얼로그
  if (showStartDatePicker) {
    DatePickerDialog(
      onDateSelected = { date ->
        viewModel.handleEvent(ProjectCreateEvent.UpdateStartDate(date))
        showStartDatePicker = false
      },
      onDismiss = { showStartDatePicker = false },
      title = "착공일 선택"
    )
  }

  if (showEndDatePicker) {
    DatePickerDialog(
      onDateSelected = { date ->
        viewModel.handleEvent(ProjectCreateEvent.UpdateEndDate(date))
        showEndDatePicker = false
      },
      onDismiss = { showEndDatePicker = false },
      title = "준공일 선택"
    )
  }

  // 지도 검색 다이얼로그
  if (showMapDialog) {
    MapSearchDialog(
      onLocationSelected = { location ->
        viewModel.handleEvent(ProjectCreateEvent.UpdateWorkLocation(location))
        showMapDialog = false
      },
      onDismiss = { showMapDialog = false }
    )
  }

  // 성공 다이얼로그
  if (uiState.showSuccessDialog) {
    ProjectCreateSuccessDialog(
      onConfirm = {
        viewModel.handleEvent(ProjectCreateEvent.DismissSuccessDialog)
      }
    )
  }

  // 에러 다이얼로그
  uiState.errorMessage?.let { errorMessage ->
    AlertDialog(
      onDismissRequest = { viewModel.handleEvent(ProjectCreateEvent.DismissError) },
      title = { Text("오류") },
      text = { Text(errorMessage) },
      confirmButton = {
        TextButton(
          onClick = { viewModel.handleEvent(ProjectCreateEvent.DismissError) }
        ) {
          Text("확인")
        }
      }
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProjectCreateContent(
  uiState: com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.model.ProjectCreateUiState,
  onEvent: (ProjectCreateEvent) -> Unit,
  onBackClick: () -> Unit,
  onShowStartDatePicker: () -> Unit,
  onShowEndDatePicker: () -> Unit,
  onShowMapDialog: () -> Unit
) {
  val dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd")

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "프로젝트 등록",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
          )
        },
        navigationIcon = {
          IconButton(onClick = onBackClick) {
            Icon(Icons.Default.ArrowBack, contentDescription = "뒤로가기")
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = Color.White
        )
      )
    }
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(paddingValues)
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .verticalScroll(rememberScrollState())
          .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        // 안내 텍스트
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2196F3).copy(alpha = 0.1f)
          ),
          shape = RoundedCornerShape(12.dp)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              Icons.Default.Info,
              contentDescription = null,
              tint = Color(0xFF2196F3),
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
              text = "프로젝트 기본 정보를 입력해주세요",
              fontSize = 14.sp,
              color = Color(0xFF2196F3)
            )
          }
        }

        // 프로젝트 이름
        OutlinedTextField(
          value = uiState.projectName,
          onValueChange = {
            onEvent(ProjectCreateEvent.UpdateProjectName(it))
          },
          label = { Text("프로젝트 이름") },
          placeholder = { Text("예: 강남 오피스텔 신축공사") },
          modifier = Modifier.fillMaxWidth(),
          leadingIcon = {
            Icon(
              Icons.Default.Assignment,
              contentDescription = null,
              tint = Color(0xFF757575)
            )
          },
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF2196F3),
            focusedLabelColor = Color(0xFF2196F3)
          ),
          singleLine = true
        )

        // 착공일
        OutlinedTextField(
          value = uiState.startDate?.format(dateFormatter) ?: "",
          onValueChange = { },
          label = { Text("착공일") },
          placeholder = { Text("날짜를 선택해주세요") },
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onShowStartDatePicker() },
          enabled = false,
          readOnly = true,
          leadingIcon = {
            Icon(
              Icons.Default.CalendarToday,
              contentDescription = null,
              tint = Color(0xFF757575)
            )
          },
          trailingIcon = {
            IconButton(onClick = onShowStartDatePicker) {
              Icon(
                Icons.Default.DateRange,
                contentDescription = "날짜 선택",
                tint = Color(0xFF2196F3)
              )
            }
          },
          colors = OutlinedTextFieldDefaults.colors(
            disabledBorderColor = Color(0xFFBDBDBD),
            disabledTextColor = Color.Black,
            disabledLabelColor = Color(0xFF757575)
          )
        )

        // 준공일
        OutlinedTextField(
          value = uiState.endDate?.format(dateFormatter) ?: "",
          onValueChange = { },
          label = { Text("준공일") },
          placeholder = { Text("날짜를 선택해주세요") },
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onShowEndDatePicker() },
          enabled = false,
          readOnly = true,
          leadingIcon = {
            Icon(
              Icons.Default.Event,
              contentDescription = null,
              tint = Color(0xFF757575)
            )
          },
          trailingIcon = {
            IconButton(onClick = onShowEndDatePicker) {
              Icon(
                Icons.Default.DateRange,
                contentDescription = "날짜 선택",
                tint = Color(0xFF2196F3)
              )
            }
          },
          colors = OutlinedTextFieldDefaults.colors(
            disabledBorderColor = Color(0xFFBDBDBD),
            disabledTextColor = Color.Black,
            disabledLabelColor = Color(0xFF757575)
          ),
          isError = uiState.startDate != null && uiState.endDate != null &&
            uiState.endDate.isBefore(uiState.startDate),
          supportingText = if (uiState.startDate != null && uiState.endDate != null &&
            uiState.endDate.isBefore(uiState.startDate)) {
            { Text("준공일은 착공일 이후여야 합니다", color = Color.Red) }
          } else null
        )

        // 작업장소
        OutlinedTextField(
          value = uiState.workLocation,
          onValueChange = { },
          label = { Text("작업장소") },
          placeholder = { Text("지도에서 검색해주세요") },
          modifier = Modifier
            .fillMaxWidth()
            .clickable { onShowMapDialog() },
          enabled = false,
          readOnly = true,
          leadingIcon = {
            Icon(
              Icons.Default.LocationOn,
              contentDescription = null,
              tint = Color(0xFF757575)
            )
          },
          trailingIcon = {
            IconButton(onClick = onShowMapDialog) {
              Icon(
                Icons.Default.Map,
                contentDescription = "지도 검색",
                tint = Color(0xFF2196F3)
              )
            }
          },
          colors = OutlinedTextFieldDefaults.colors(
            disabledBorderColor = Color(0xFFBDBDBD),
            disabledTextColor = Color.Black,
            disabledLabelColor = Color(0xFF757575)
          )
        )

        Spacer(modifier = Modifier.weight(1f))

        // 등록 버튼
        Button(
          onClick = { onEvent(ProjectCreateEvent.CreateProject) },
          modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
          enabled = uiState.isFormValid && !uiState.isLoading,
          colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF2196F3),
            disabledContainerColor = Color(0xFFBDBDBD)
          ),
          shape = RoundedCornerShape(12.dp)
        ) {
          if (uiState.isLoading) {
            CircularProgressIndicator(
              modifier = Modifier.size(24.dp),
              color = Color.White,
              strokeWidth = 2.dp
            )
          } else {
            Icon(Icons.Default.Check, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "프로젝트 등록",
              fontSize = 16.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
  onDateSelected: (LocalDate) -> Unit,
  onDismiss: () -> Unit,
  title: String
) {
  val datePickerState = rememberDatePickerState()

  DatePickerDialog(
    onDismissRequest = onDismiss,
    confirmButton = {
      TextButton(
        onClick = {
          datePickerState.selectedDateMillis?.let {
            val date = LocalDate.ofEpochDay(it / (24 * 60 * 60 * 1000))
            onDateSelected(date)
          }
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

@Composable
fun MapSearchDialog(
  onLocationSelected: (String) -> Unit,
  onDismiss: () -> Unit
) {
  var searchQuery by remember { mutableStateOf("") }

  // 샘플 검색 결과
  val sampleLocations = listOf(
    "서울특별시 강남구 테헤란로 123",
    "서울특별시 강남구 역삼동 234-5",
    "서울특별시 서초구 서초대로 456",
    "부산광역시 해운대구 마린시티 789",
    "인천광역시 연수구 송도동 123-4"
  )

  val filteredLocations = remember(searchQuery) {
    if (searchQuery.isBlank()) sampleLocations
    else sampleLocations.filter { it.contains(searchQuery, ignoreCase = true) }
  }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.8f),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(16.dp)
      ) {
        // 헤더
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "작업장소 검색",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
          )
          IconButton(onClick = onDismiss) {
            Icon(Icons.Default.Close, contentDescription = "닫기")
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 검색바
        OutlinedTextField(
          value = searchQuery,
          onValueChange = { searchQuery = it },
          placeholder = { Text("주소를 입력하세요") },
          modifier = Modifier.fillMaxWidth(),
          leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = null)
          },
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF2196F3)
          ),
          singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 검색 결과
        LazyColumn(
          modifier = Modifier.weight(1f),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          items(filteredLocations.size) { index ->
            Card(
              onClick = { onLocationSelected(filteredLocations[index]) },
              modifier = Modifier.fillMaxWidth(),
              colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF5F5F5)
              )
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  Icons.Default.LocationOn,
                  contentDescription = null,
                  tint = Color(0xFFE91E63),
                  modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                  text = filteredLocations[index],
                  fontSize = 14.sp
                )
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun ProjectCreateSuccessDialog(
  onConfirm: () -> Unit
) {
  Dialog(onDismissRequest = { }) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // 성공 아이콘
        Surface(
          modifier = Modifier.size(80.dp),
          shape = RoundedCornerShape(40.dp),
          color = Color(0xFF4CAF50).copy(alpha = 0.1f)
        ) {
          Box(contentAlignment = Alignment.Center) {
            Icon(
              Icons.Default.CheckCircle,
              contentDescription = null,
              modifier = Modifier.size(48.dp),
              tint = Color(0xFF4CAF50)
            )
          }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
          text = "프로젝트 등록 완료",
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = "프로젝트가 성공적으로 등록되었습니다.",
          fontSize = 14.sp,
          color = Color(0xFF757575),
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
          onClick = onConfirm,
          modifier = Modifier.fillMaxWidth(),
          colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF2196F3)
          ),
          shape = RoundedCornerShape(8.dp)
        ) {
          Text(
            text = "확인",
            fontWeight = FontWeight.Bold
          )
        }
      }
    }
  }
}

@Preview(showBackground = true, name = "프로젝트 등록 화면")
@Composable
fun ProjectCreateScreenPreview() {
  MaterialTheme {
    ProjectCreateScreen(navController = rememberNavController())
  }
}