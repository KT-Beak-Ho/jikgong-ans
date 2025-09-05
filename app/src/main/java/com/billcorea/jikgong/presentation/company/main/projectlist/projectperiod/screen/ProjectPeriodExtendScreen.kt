package com.billcorea.jikgong.presentation.company.main.projectlist.projectperiod.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.annotation.Destination
import com.billcorea.jikgong.presentation.company.main.common.BackNavigationTopBar
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.SimpleProject
import com.billcorea.jikgong.api.models.sampleDataFactory.CompanyMockDataFactory
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun ProjectPeriodExtendScreen(
  navController: NavController,
  projectId: String,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  
  // 샘플 프로젝트 데이터 - 실제로는 repository에서 가져와야 함
  var project by remember { 
    mutableStateOf(
      SimpleProject(
        id = projectId,
        title = "아파트 신축공사 철근 작업자 모집",
        company = "대한건설(주)",
        location = "서울시 강남구 역삼동",
        category = "철근공",
        status = "IN_PROGRESS",
        startDate = "2025-08-08",
        endDate = "2025-09-20",
        wage = 200000,
        currentApplicants = 8,
        maxApplicants = 15,
        isUrgent = true
      )
    )
  }
  
  var selectedExtensionDays by remember { mutableStateOf(0) }
  var extensionReason by remember { mutableStateOf("") }
  var showConfirmationDialog by remember { mutableStateOf(false) }
  var showSuccessDialog by remember { mutableStateOf(false) }
  
  // 연장 기간 옵션
  val extensionOptions = listOf(
    Pair(7, "1주일"),
    Pair(14, "2주일"),
    Pair(30, "1개월"),
    Pair(60, "2개월"),
    Pair(90, "3개월"),
    Pair(0, "직접 입력")
  )
  
  // 원본 종료일과 새 종료일 계산
  val originalEndDate = LocalDate.parse(project.endDate)
  val newEndDate = if (selectedExtensionDays > 0) {
    originalEndDate.plusDays(selectedExtensionDays.toLong())
  } else {
    originalEndDate
  }
  
  Scaffold(
    modifier = modifier.fillMaxSize(),
    topBar = {
      BackNavigationTopBar(
        title = "프로젝트 기간 연장",
        onBackClick = { navController.popBackStack() }
      )
    }
  ) { innerPadding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .background(Color(0xFFF8F9FA)),
      contentPadding = PaddingValues(16.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      item {
        // 프로젝트 정보 카드
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp)
          ) {
            Text(
              text = "프로젝트 정보",
              style = AppTypography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF1A1D29)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
              text = project.title,
              style = AppTypography.bodyLarge,
              fontWeight = FontWeight.Medium,
              color = Color(0xFF1A1D29)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Column {
                Text(
                  text = "현재 종료일",
                  style = AppTypography.bodySmall,
                  color = Color(0xFF6B7280)
                )
                Text(
                  text = project.endDate,
                  style = AppTypography.bodyMedium,
                  fontWeight = FontWeight.Medium,
                  color = Color(0xFF1A1D29)
                )
              }
              
              if (selectedExtensionDays > 0) {
                Column(horizontalAlignment = Alignment.End) {
                  Text(
                    text = "연장 후 종료일",
                    style = AppTypography.bodySmall,
                    color = Color(0xFF6B7280)
                  )
                  Text(
                    text = newEndDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    style = AppTypography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4B7BFF)
                  )
                }
              }
            }
          }
        }
      }
      
      item {
        // 연장 기간 선택 카드
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp)
          ) {
            Text(
              text = "연장 기간 선택",
              style = AppTypography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF1A1D29)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // 연장 기간 옵션들
            extensionOptions.forEach { (days, label) ->
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .clickable {
                    selectedExtensionDays = days
                  }
                  .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                RadioButton(
                  selected = selectedExtensionDays == days,
                  onClick = { selectedExtensionDays = days },
                  colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFF4B7BFF)
                  )
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = label,
                    style = AppTypography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1A1D29)
                  )
                  
                  if (days > 0) {
                    Text(
                      text = "${days}일 연장",
                      style = AppTypography.bodySmall,
                      color = Color(0xFF6B7280)
                    )
                  }
                }
                
                if (days > 0 && selectedExtensionDays == days) {
                  Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF4B7BFF).copy(alpha = 0.1f)
                  ) {
                    Text(
                      text = "+${days}일",
                      style = AppTypography.bodySmall,
                      fontWeight = FontWeight.Bold,
                      color = Color(0xFF4B7BFF),
                      modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                  }
                }
              }
            }
          }
        }
      }
      
      item {
        // 연장 이유 입력 카드
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp)
          ) {
            Text(
              text = "연장 이유 *",
              style = AppTypography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF1A1D29)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
              text = "프로젝트 기간을 연장하는 구체적인 이유를 작성해주세요.",
              style = AppTypography.bodySmall,
              color = Color(0xFF6B7280)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            OutlinedTextField(
              value = extensionReason,
              onValueChange = { extensionReason = it },
              modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
              placeholder = {
                Text(
                  text = "예: 날씨 악화로 인한 공사 지연, 자재 수급 문제, 설계 변경 등",
                  style = AppTypography.bodySmall
                )
              },
              colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF4B7BFF),
                cursorColor = Color(0xFF4B7BFF)
              ),
              shape = RoundedCornerShape(8.dp),
              singleLine = false,
              maxLines = 4
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
              text = "${extensionReason.length}/200",
              style = AppTypography.bodySmall,
              color = if (extensionReason.length > 200) Color.Red else Color(0xFF6B7280),
              textAlign = TextAlign.End,
              modifier = Modifier.fillMaxWidth()
            )
          }
        }
      }
      
      item {
        // 주의사항 카드
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(16.dp),
            verticalAlignment = Alignment.Top
          ) {
            Icon(
              Icons.Default.Warning,
              contentDescription = null,
              modifier = Modifier.size(20.dp),
              tint = Color(0xFFFF9800)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column {
              Text(
                text = "주의사항",
                style = AppTypography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE65100)
              )
              
              Spacer(modifier = Modifier.height(4.dp))
              
              Text(
                text = "• 기간을 연장하면 취소할 수 없습니다.\n• 연장된 기간은 모든 일자리 모집에 영향을 줍니다.\n• 연장 이유는 기록으로 남아 관리됩니다.",
                style = AppTypography.bodySmall,
                color = Color(0xFFBF360C)
              )
            }
          }
        }
      }
      
      item {
        Spacer(modifier = Modifier.height(16.dp))
      }
      
      item {
        // 연장하기 버튼
        val isFormValid = selectedExtensionDays > 0 && 
                         extensionReason.isNotBlank() && 
                         extensionReason.length <= 200
        
        Button(
          onClick = { showConfirmationDialog = true },
          enabled = isFormValid,
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF4B7BFF),
            disabledContainerColor = Color(0xFFE5E5E5)
          ),
          shape = RoundedCornerShape(8.dp)
        ) {
          Text(
            text = "기간 연장하기",
            style = AppTypography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = if (isFormValid) Color.White else Color(0xFF9E9E9E)
          )
        }
      }
    }
  }
  
  // 최종 확인 다이얼로그
  if (showConfirmationDialog) {
    AlertDialog(
      onDismissRequest = { showConfirmationDialog = false },
      icon = {
        Icon(
          Icons.Default.Warning,
          contentDescription = null,
          tint = Color(0xFFFF9800),
          modifier = Modifier.size(32.dp)
        )
      },
      title = {
        Text(
          text = "프로젝트 기간 연장",
          style = AppTypography.titleLarge,
          fontWeight = FontWeight.Bold,
          textAlign = TextAlign.Center
        )
      },
      text = {
        Column {
          Text(
            text = "정말로 프로젝트 기간을 연장하시겠습니까?",
            style = AppTypography.bodyLarge,
            textAlign = TextAlign.Center
          )
          
          Spacer(modifier = Modifier.height(16.dp))
          
          Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFFF5F5F5)
          ) {
            Column(
              modifier = Modifier.padding(12.dp)
            ) {
              Text(
                text = "연장 정보",
                style = AppTypography.bodyMedium,
                fontWeight = FontWeight.Bold
              )
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = "• 연장 기간: ${selectedExtensionDays}일",
                style = AppTypography.bodySmall
              )
              Text(
                text = "• 새 종료일: ${newEndDate.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"))}",
                style = AppTypography.bodySmall
              )
              Text(
                text = "• 연장 이유: ${extensionReason.take(30)}${if (extensionReason.length > 30) "..." else ""}",
                style = AppTypography.bodySmall
              )
            }
          }
          
          Spacer(modifier = Modifier.height(12.dp))
          
          Text(
            text = "⚠️ 한 번 연장하면 취소할 수 없습니다",
            style = AppTypography.bodySmall,
            color = Color.Red,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            showConfirmationDialog = false
            // 프로젝트 데이터 업데이트
            val updatedProject = project.copy(
              endDate = newEndDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            )
            project = updatedProject
            
            // SharedPreferences를 통한 데이터 전달
            val prefs = context.getSharedPreferences("project_updates", android.content.Context.MODE_PRIVATE)
            prefs.edit().apply {
              putString("updated_project_id", updatedProject.id)
              putString("updated_end_date", updatedProject.endDate)
              putLong("update_timestamp", System.currentTimeMillis())
              apply()
            }
            
            showSuccessDialog = true
          },
          colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFFF9800)
          )
        ) {
          Text("확인", color = Color.White)
        }
      },
      dismissButton = {
        TextButton(
          onClick = { showConfirmationDialog = false }
        ) {
          Text("취소", color = Color(0xFF4B7BFF))
        }
      }
    )
  }
  
  // 성공 다이얼로그
  if (showSuccessDialog) {
    AlertDialog(
      onDismissRequest = { 
        showSuccessDialog = false
        navController.popBackStack()
      },
      icon = {
        Icon(
          Icons.Default.CheckCircle,
          contentDescription = null,
          tint = Color(0xFF4CAF50),
          modifier = Modifier.size(32.dp)
        )
      },
      title = {
        Text(
          text = "기간 연장 완료",
          style = AppTypography.titleLarge,
          fontWeight = FontWeight.Bold,
          textAlign = TextAlign.Center
        )
      },
      text = {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
            text = "프로젝트 기간이 성공적으로 연장되었습니다.",
            style = AppTypography.bodyLarge,
            textAlign = TextAlign.Center
          )
          
          Spacer(modifier = Modifier.height(12.dp))
          
          Text(
            text = "새 종료일: ${newEndDate.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"))}",
            style = AppTypography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4B7BFF),
            textAlign = TextAlign.Center
          )
        }
      },
      confirmButton = {
        Button(
          onClick = { 
            showSuccessDialog = false
            navController.popBackStack()
          },
          colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF4CAF50)
          )
        ) {
          Text("확인", color = Color.White)
        }
      }
    )
  }
}

@Preview(showBackground = true)
@Composable
fun ProjectPeriodExtendScreenPreview() {
  Jikgong1111Theme {
    ProjectPeriodExtendScreen(
      navController = rememberNavController(),
      projectId = "1"
    )
  }
}