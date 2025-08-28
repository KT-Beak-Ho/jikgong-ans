
package com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.screen

import android.app.DatePickerDialog
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.company.main.common.CompanyTopBar
import com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.model.*
import com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.repository.ProjectCreateRepositoryImpl
import com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.viewmodel.ProjectCreateViewModel
import com.billcorea.jikgong.presentation.company.main.projectlist.repository.ProjectRepositoryImpl
import kotlinx.coroutines.flow.collectLatest
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectCreateScreen(
  navController: NavController,
  viewModel: ProjectCreateViewModel = androidx.lifecycle.viewmodel.compose.viewModel {
    ProjectCreateViewModel(
      ProjectCreateRepositoryImpl(ProjectRepositoryImpl())
    )
  }
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val context = LocalContext.current

  LaunchedEffect(viewModel) {
    viewModel.uiEvent.collectLatest { event ->
      when (event) {
        is ProjectCreateUiEvent.NavigateBack -> {
          navController.popBackStack()
        }
        is ProjectCreateUiEvent.ShowError -> {
          Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
        }
      }
    }
  }

  Scaffold(
    topBar = {
      CompanyTopBar(
        title = "새 프로젝트 등록",
        showBackButton = true,
        onBackClick = { navController.popBackStack() },
        actions = {
          IconButton(onClick = { navController.popBackStack() }) {
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
            onClick = { navController.popBackStack() },
            modifier = Modifier.weight(1f)
          ) {
            Text("취소")
          }
          Button(
            onClick = { viewModel.handleEvent(ProjectCreateEvent.CreateProject) },
            modifier = Modifier.weight(1f),
            enabled = uiState.isFormValid && !uiState.isLoading,
            colors = ButtonDefaults.buttonColors(
              containerColor = Color(0xFF2196F3)
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
      // 프로젝트명
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              "프로젝트명 *",
              fontSize = 14.sp,
              fontWeight = FontWeight.Medium,
              color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
              value = uiState.projectName,
              onValueChange = {
                viewModel.handleEvent(ProjectCreateEvent.UpdateProjectName(it))
              },
              modifier = Modifier.fillMaxWidth(),
              placeholder = { Text("예: 아파트 신축공사 철근 작업자 모집") },
              singleLine = true
            )
          }
        }
      }

      // 작업 카테고리
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              "작업 카테고리",
              fontSize = 14.sp,
              fontWeight = FontWeight.Medium,
              color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              listOf("철근공", "목공", "전기공", "일반").forEach { category ->
                FilterChip(
                  selected = uiState.category == category,
                  onClick = {
                    viewModel.handleEvent(ProjectCreateEvent.UpdateCategory(category))
                  },
                  label = { Text(category) }
                )
              }
            }
          }
        }
      }

      // 작업 위치
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              "작업 위치 *",
              fontSize = 14.sp,
              fontWeight = FontWeight.Medium,
              color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
              value = uiState.workLocation,
              onValueChange = {
                viewModel.handleEvent(ProjectCreateEvent.UpdateWorkLocation(it))
              },
              modifier = Modifier.fillMaxWidth(),
              placeholder = { Text("예: 서울시 강남구 역삼동") },
              singleLine = true
            )
          }
        }
      }

      // 작업 기간
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              "작업 기간 *",
              fontSize = 14.sp,
              fontWeight = FontWeight.Medium,
              color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              DatePickerField(
                label = "시작일",
                date = uiState.startDate,
                onDateSelected = {
                  viewModel.handleEvent(ProjectCreateEvent.UpdateStartDate(it))
                },
                modifier = Modifier.weight(1f)
              )
              DatePickerField(
                label = "종료일",
                date = uiState.endDate,
                onDateSelected = {
                  viewModel.handleEvent(ProjectCreateEvent.UpdateEndDate(it))
                },
                modifier = Modifier.weight(1f)
              )
            }
          }
        }
      }

      // 일당 및 인원
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
                  "일당 *",
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Medium,
                  color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                  value = if (uiState.wage.isNotEmpty()) {
                    NumberFormat.getNumberInstance(Locale.KOREA).format(
                      uiState.wage.toLongOrNull() ?: 0
                    )
                  } else "",
                  onValueChange = { input ->
                    val numericValue = input.replace(",", "").filter { it.isDigit() }
                    viewModel.handleEvent(ProjectCreateEvent.UpdateWage(numericValue))
                  },
                  modifier = Modifier.fillMaxWidth(),
                  placeholder = { Text("200,000") },
                  suffix = { Text("원") },
                  keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                  singleLine = true
                )
              }
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  "필요 인원 *",
                  fontSize = 14.sp,
                  fontWeight = FontWeight.Medium,
                  color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                  value = uiState.maxApplicants,
                  onValueChange = {
                    viewModel.handleEvent(ProjectCreateEvent.UpdateMaxApplicants(it))
                  },
                  modifier = Modifier.fillMaxWidth(),
                  placeholder = { Text("15") },
                  suffix = { Text("명") },
                  keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                  singleLine = true
                )
              }
            }
          }
        }
      }

      // 상세 설명
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              "상세 설명",
              fontSize = 14.sp,
              fontWeight = FontWeight.Medium,
              color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
              value = uiState.description,
              onValueChange = {
                viewModel.handleEvent(ProjectCreateEvent.UpdateDescription(it))
              },
              modifier = Modifier.fillMaxWidth(),
              placeholder = {
                Text("작업 내용, 필수 요구사항, 제공 사항 등을 자세히 입력해주세요")
              },
              minLines = 4,
              maxLines = 6
            )
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
            Switch(
              checked = uiState.isUrgent,
              onCheckedChange = {
                viewModel.handleEvent(ProjectCreateEvent.ToggleUrgent(it))
              }
            )
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
            viewModel.handleEvent(ProjectCreateEvent.DismissSuccessDialog)
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
        viewModel.handleEvent(ProjectCreateEvent.DismissError)
      },
      title = { Text("오류") },
      text = { Text(error) },
      confirmButton = {
        TextButton(
          onClick = {
            viewModel.handleEvent(ProjectCreateEvent.DismissError)
          }
        ) {
          Text("확인")
        }
      }
    )
  }
}

@Composable
private fun DatePickerField(
  label: String,
  date: LocalDate?,
  onDateSelected: (LocalDate) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

  OutlinedTextField(
    value = date?.format(dateFormatter) ?: "",
    onValueChange = { },
    modifier = modifier.clickable {
      val today = LocalDate.now()
      DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
          onDateSelected(LocalDate.of(year, month + 1, dayOfMonth))
        },
        date?.year ?: today.year,
        date?.monthValue?.minus(1) ?: today.monthValue - 1,
        date?.dayOfMonth ?: today.dayOfMonth
      ).show()
    },
    enabled = false,
    readOnly = true,
    placeholder = { Text(label) },
    trailingIcon = {
      Icon(Icons.Default.CalendarToday, contentDescription = null)
    },
    colors = OutlinedTextFieldDefaults.colors(
      disabledTextColor = MaterialTheme.colorScheme.onSurface,
      disabledBorderColor = MaterialTheme.colorScheme.outline,
      disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
  )
}

@Preview(showBackground = true)
@Composable
fun ProjectCreateScreenPreview() {
  ProjectCreateScreen(navController = rememberNavController())
}