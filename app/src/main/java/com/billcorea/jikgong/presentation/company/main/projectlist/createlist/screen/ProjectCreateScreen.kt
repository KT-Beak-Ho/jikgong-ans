// app/src/main/java/com/billcorea/jikgong/presentation/company/main/projectlist/createlist/screen/ProjectCreateScreen.kt
package com.billcorea.jikgong.presentation.company.main.projectlist.createlist.screen

import android.app.DatePickerDialog
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.company.main.projectlist.createlist.viewmodel.ProjectCreateViewModel
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.appColorScheme
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectCreateScreen(
  navController: NavController,
  modifier: Modifier = Modifier,
  viewModel: ProjectCreateViewModel = koinViewModel()
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val scrollState = rememberScrollState()

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "프로젝트 등록",
            style = AppTypography.titleLarge,
            fontWeight = FontWeight.Bold
          )
        },
        navigationIcon = {
          IconButton(onClick = { navController.navigateUp() }) {
            Icon(
              imageVector = Icons.Default.ArrowBack,
              contentDescription = "뒤로가기"
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = Color.White
        )
      )
    },
    bottomBar = {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(Color.White)
          .padding(16.dp)
      ) {
        Button(
          onClick = {
            viewModel.onCreateProject {
              // 성공 시 콜백
              navController.navigateUp()
            }
          },
          modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = appColorScheme.primary
          ),
          enabled = !uiState.isLoading
        ) {
          if (uiState.isLoading) {
            CircularProgressIndicator(
              color = Color.White,
              modifier = Modifier.size(24.dp)
            )
          } else {
            Text(
              text = "등록",
              style = AppTypography.titleMedium,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }
  ) { innerPadding ->
    Column(
      modifier = modifier
        .fillMaxSize()
        .padding(innerPadding)
        .verticalScroll(scrollState)
        .background(Color(0xFFF8F9FA))
    ) {
      // 에러 메시지
      AnimatedVisibility(visible = uiState.errorMessage != null) {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFEBEE)
          )
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Error,
              contentDescription = null,
              tint = Color(0xFFD32F2F),
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = uiState.errorMessage ?: "",
              color = Color(0xFFD32F2F),
              style = AppTypography.bodyMedium
            )
          }
        }
      }

      // 입력 폼
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
          containerColor = Color.White
        )
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
          verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
          // 프로젝트 이름
          ProjectInputField(
            label = "프로젝트 이름",
            value = uiState.projectName,
            onValueChange = viewModel::updateProjectName,
            placeholder = "프로젝트 이름 작성",
            isError = uiState.projectNameError != null,
            errorMessage = uiState.projectNameError
          )

          // 착공일
          DatePickerField(
            label = "착공일",
            date = uiState.startDate,
            onDateSelected = viewModel::updateStartDate,
            isError = uiState.startDateError != null,
            errorMessage = uiState.startDateError
          )

          // 준공일
          DatePickerField(
            label = "준공일",
            date = uiState.endDate,
            onDateSelected = viewModel::updateEndDate,
            minDate = uiState.startDate?.plusDays(1),
            isError = uiState.endDateError != null,
            errorMessage = uiState.endDateError
          )

          // 작업장소
          LocationSearchField(
            label = "작업장소",
            location = uiState.location,
            onLocationClick = {
              // TODO: 지도 API 연동
              navController.navigate("company/mapsearch")
            },
            isError = uiState.locationError != null,
            errorMessage = uiState.locationError
          )

          // 상세 주소 (선택사항)
          if (uiState.location.isNotBlank()) {
            ProjectInputField(
              label = "상세 주소",
              value = uiState.locationDetail,
              onValueChange = viewModel::updateLocationDetail,
              placeholder = "상세 주소 입력 (선택)",
              isRequired = false
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(100.dp))
    }
  }
}

@Composable
private fun ProjectInputField(
  label: String,
  value: String,
  onValueChange: (String) -> Unit,
  placeholder: String,
  isError: Boolean = false,
  errorMessage: String? = null,
  isRequired: Boolean = true
) {
  Column {
    Row(
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = label,
        style = AppTypography.titleMedium,
        fontWeight = FontWeight.Medium
      )
      if (isRequired) {
        Text(
          text = " *",
          color = Color(0xFFD32F2F),
          style = AppTypography.titleMedium
        )
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    OutlinedTextField(
      value = value,
      onValueChange = onValueChange,
      modifier = Modifier.fillMaxWidth(),
      placeholder = {
        Text(
          text = placeholder,
          color = Color.Gray
        )
      },
      isError = isError,
      singleLine = true,
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = appColorScheme.primary,
        unfocusedBorderColor = Color(0xFFE0E0E0),
        errorBorderColor = Color(0xFFD32F2F)
      ),
      shape = RoundedCornerShape(8.dp)
    )

    if (errorMessage != null) {
      Text(
        text = errorMessage,
        color = Color(0xFFD32F2F),
        style = AppTypography.bodySmall,
        modifier = Modifier.padding(top = 4.dp)
      )
    }
  }
}

@Composable
private fun DatePickerField(
  label: String,
  date: LocalDate?,
  onDateSelected: (LocalDate) -> Unit,
  minDate: LocalDate? = null,
  isError: Boolean = false,
  errorMessage: String? = null
) {
  val context = LocalContext.current
  val calendar = Calendar.getInstance()

  if (minDate != null) {
    calendar.set(minDate.year, minDate.monthValue - 1, minDate.dayOfMonth)
  }

  val datePickerDialog = DatePickerDialog(
    context,
    { _, year, month, dayOfMonth ->
      onDateSelected(LocalDate.of(year, month + 1, dayOfMonth))
    },
    calendar.get(Calendar.YEAR),
    calendar.get(Calendar.MONTH),
    calendar.get(Calendar.DAY_OF_MONTH)
  ).apply {
    minDate?.let {
      datePicker.minDate = calendar.timeInMillis
    }
  }

  Column {
    Row(
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = label,
        style = AppTypography.titleMedium,
        fontWeight = FontWeight.Medium
      )
      Text(
        text = " *",
        color = Color(0xFFD32F2F),
        style = AppTypography.titleMedium
      )
    }

    Spacer(modifier = Modifier.height(8.dp))

    OutlinedTextField(
      value = date?.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")) ?: "",
      onValueChange = { },
      modifier = Modifier
        .fillMaxWidth()
        .clickable { datePickerDialog.show() },
      enabled = false,
      placeholder = {
        Text(
          text = "클릭하여 ${label} 선택",
          color = Color.Gray
        )
      },
      trailingIcon = {
        Icon(
          imageVector = Icons.Default.CalendarToday,
          contentDescription = "날짜 선택",
          tint = appColorScheme.primary
        )
      },
      isError = isError,
      colors = OutlinedTextFieldDefaults.colors(
        disabledTextColor = Color.Black,
        disabledBorderColor = if (isError) Color(0xFFD32F2F) else Color(0xFFE0E0E0),
        disabledTrailingIconColor = appColorScheme.primary
      ),
      shape = RoundedCornerShape(8.dp)
    )

    if (errorMessage != null) {
      Text(
        text = errorMessage,
        color = Color(0xFFD32F2F),
        style = AppTypography.bodySmall,
        modifier = Modifier.padding(top = 4.dp)
      )
    }
  }
}

@Composable
private fun LocationSearchField(
  label: String,
  location: String,
  onLocationClick: () -> Unit,
  isError: Boolean = false,
  errorMessage: String? = null
) {
  Column {
    Row(
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = label,
        style = AppTypography.titleMedium,
        fontWeight = FontWeight.Medium
      )
      Text(
        text = " *",
        color = Color(0xFFD32F2F),
        style = AppTypography.titleMedium
      )
    }

    Spacer(modifier = Modifier.height(8.dp))

    OutlinedTextField(
      value = location,
      onValueChange = { },
      modifier = Modifier
        .fillMaxWidth()
        .clickable { onLocationClick() },
      enabled = false,
      placeholder = {
        Text(
          text = "클릭하여 작업장소 검색",
          color = Color.Gray
        )
      },
      trailingIcon = {
        Icon(
          imageVector = Icons.Default.Search,
          contentDescription = "장소 검색",
          tint = appColorScheme.primary
        )
      },
      isError = isError,
      colors = OutlinedTextFieldDefaults.colors(
        disabledTextColor = Color.Black,
        disabledBorderColor = if (isError) Color(0xFFD32F2F) else Color(0xFFE0E0E0),
        disabledTrailingIconColor = appColorScheme.primary
      ),
      shape = RoundedCornerShape(8.dp)
    )

    if (errorMessage != null) {
      Text(
        text = errorMessage,
        color = Color(0xFFD32F2F),
        style = AppTypography.bodySmall,
        modifier = Modifier.padding(top = 4.dp)
      )
    }
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ProjectCreateScreenPreview() {
  com.billcorea.jikgong.ui.theme.Jikgong1111Theme {
    ProjectCreateScreen(
      navController = rememberNavController()
    )
  }
}