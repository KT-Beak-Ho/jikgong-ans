// 📍 경로: app/src/main/java/com/billcorea/jikgong/presentation/company/main/projectlist/projectcreate/screen/ProjectCreateDialog.kt

package com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.screen

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun ProjectCreateDialog(
  onDismiss: () -> Unit,
  onConfirm: (ProjectCreateData) -> Unit
) {
  var projectName by remember { mutableStateOf("") }
  var category by remember { mutableStateOf("철근공") }
  var wage by remember { mutableStateOf("200000") }
  var location by remember { mutableStateOf("") }
  var startDate by remember { mutableStateOf<LocalDate?>(null) }
  var endDate by remember { mutableStateOf<LocalDate?>(null) }
  var maxApplicants by remember { mutableStateOf("15") }
  var description by remember { mutableStateOf("") }
  var isUrgent by remember { mutableStateOf(false) }

  val context = LocalContext.current
  val scrollState = rememberScrollState()

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(
      dismissOnBackPress = true,
      dismissOnClickOutside = false,
      usePlatformDefaultWidth = false
    )
  ) {
    Card(
      modifier = Modifier
        .fillMaxWidth(0.95f)
        .fillMaxHeight(0.9f),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
      Column(
        modifier = Modifier.fillMaxSize()
      ) {
        // 헤더
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "새 프로젝트 등록",
            fontSize = 20.sp,
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

        Divider(color = Color(0xFFE0E0E0))

        // 본문 내용
        Column(
          modifier = Modifier
            .weight(1f)
            .verticalScroll(scrollState)
            .padding(20.dp),
          verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
          // 프로젝트명
          Column {
            Text(
              text = "프로젝트명",
              fontSize = 14.sp,
              color = Color.Gray,
              modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
              value = projectName,
              onValueChange = { projectName = it },
              modifier = Modifier.fillMaxWidth(),
              placeholder = { Text("예: 아파트 신축공사 철근 작업자 모집") },
              colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedBorderColor = Color(0xFF4B7BFF)
              ),
              singleLine = true
            )
          }

          // 작업 카테고리와 일당
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
          ) {
            // 작업 카테고리
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "작업 카테고리",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
              )
              Box {
                OutlinedTextField(
                  value = category,
                  onValueChange = { },
                  modifier = Modifier
                    .fillMaxWidth()
                    .clickable { /* 드롭다운 열기 */ },
                  enabled = false,
                  readOnly = true,
                  trailingIcon = {
                    Icon(
                      Icons.Default.ArrowDropDown,
                      contentDescription = null
                    )
                  },
                  colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledBorderColor = Color(0xFFE0E0E0),
                    disabledTrailingIconColor = Color.Gray
                  )
                )
              }
            }

            // 일당
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "일당",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
              )
              OutlinedTextField(
                value = if (wage.isNotEmpty()) {
                  NumberFormat.getNumberInstance(Locale.KOREA).format(wage.toLongOrNull() ?: 0)
                } else "",
                onValueChange = { input ->
                  wage = input.replace(",", "").filter { it.isDigit() }
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("200000") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                  unfocusedBorderColor = Color(0xFFE0E0E0),
                  focusedBorderColor = Color(0xFF4B7BFF)
                ),
                singleLine = true
              )
            }
          }

          // 작업 위치
          Column {
            Text(
              text = "작업 위치",
              fontSize = 14.sp,
              color = Color.Gray,
              modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
              value = location,
              onValueChange = { location = it },
              modifier = Modifier.fillMaxWidth(),
              placeholder = { Text("예: 서울시 강남구 역삼동") },
              colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedBorderColor = Color(0xFF4B7BFF)
              ),
              singleLine = true
            )
          }

          // 시작일과 종료일
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
          ) {
            // 시작일
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "시작일",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
              )
              OutlinedTextField(
                value = startDate?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) ?: "",
                onValueChange = { },
                modifier = Modifier
                  .fillMaxWidth()
                  .clickable {
                    val today = LocalDate.now()
                    DatePickerDialog(
                      context,
                      { _, year, month, dayOfMonth ->
                        startDate = LocalDate.of(year, month + 1, dayOfMonth)
                      },
                      today.year,
                      today.monthValue - 1,
                      today.dayOfMonth
                    ).show()
                  },
                enabled = false,
                readOnly = true,
                placeholder = { Text("연도-월-일") },
                trailingIcon = {
                  Icon(
                    Icons.Default.DateRange,
                    contentDescription = null,
                    tint = Color.Gray
                  )
                },
                colors = OutlinedTextFieldDefaults.colors(
                  disabledTextColor = MaterialTheme.colorScheme.onSurface,
                  disabledBorderColor = Color(0xFFE0E0E0),
                  disabledTrailingIconColor = Color.Gray
                )
              )
            }

            // 종료일
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "종료일",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 8.dp)
              )
              OutlinedTextField(
                value = endDate?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) ?: "",
                onValueChange = { },
                modifier = Modifier
                  .fillMaxWidth()
                  .clickable {
                    val today = LocalDate.now()
                    DatePickerDialog(
                      context,
                      { _, year, month, dayOfMonth ->
                        endDate = LocalDate.of(year, month + 1, dayOfMonth)
                      },
                      today.year,
                      today.monthValue - 1,
                      today.dayOfMonth
                    ).show()
                  },
                enabled = false,
                readOnly = true,
                placeholder = { Text("연도-월-일") },
                trailingIcon = {
                  Icon(
                    Icons.Default.DateRange,
                    contentDescription = null,
                    tint = Color.Gray
                  )
                },
                colors = OutlinedTextFieldDefaults.colors(
                  disabledTextColor = MaterialTheme.colorScheme.onSurface,
                  disabledBorderColor = Color(0xFFE0E0E0),
                  disabledTrailingIconColor = Color.Gray
                )
              )
            }
          }

          // 필요 인원
          Column {
            Text(
              text = "필요 인원",
              fontSize = 14.sp,
              color = Color.Gray,
              modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
              value = maxApplicants,
              onValueChange = { input ->
                maxApplicants = input.filter { it.isDigit() }
              },
              modifier = Modifier.fillMaxWidth(),
              placeholder = { Text("15") },
              keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
              colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedBorderColor = Color(0xFF4B7BFF)
              ),
              singleLine = true
            )
          }

          // 상세 설명
          Column {
            Text(
              text = "상세 설명",
              fontSize = 14.sp,
              color = Color.Gray,
              modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
              value = description,
              onValueChange = { description = it },
              modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
              placeholder = { Text("작업 내용, 필수 요구사항, 제공 사항 등을 자세히 입력해주세요") },
              colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color(0xFFE0E0E0),
                focusedBorderColor = Color(0xFF4B7BFF)
              ),
              maxLines = 5
            )
          }

          // 긴급 모집
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Checkbox(
              checked = isUrgent,
              onCheckedChange = { isUrgent = it }
            )
            Text(
              text = "긴급 모집으로 표시",
              fontSize = 14.sp
            )
          }
        }

        // 하단 버튼
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          OutlinedButton(
            onClick = onDismiss,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.outlinedButtonColors(
              contentColor = Color.Gray
            ),
            border = ButtonDefaults.outlinedButtonBorder.copy(
              brush = androidx.compose.ui.graphics.SolidColor(Color.Gray)
            )
          ) {
            Text("취소", fontSize = 16.sp)
          }
          Button(
            onClick = {
              val data = ProjectCreateData(
                projectName = projectName,
                category = category,
                wage = wage.toIntOrNull() ?: 0,
                location = location,
                startDate = startDate,
                endDate = endDate,
                maxApplicants = maxApplicants.toIntOrNull() ?: 0,
                description = description,
                isUrgent = isUrgent
              )
              onConfirm(data)
            },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
              containerColor = Color(0xFF4B7BFF)
            ),
            enabled = projectName.isNotBlank() &&
              location.isNotBlank() &&
              startDate != null &&
              endDate != null
          ) {
            Text("등록하기", fontSize = 16.sp)
          }
        }
      }
    }
  }
}

// 프로젝트 생성 데이터 클래스
data class ProjectCreateData(
  val projectName: String,
  val category: String,
  val wage: Int,
  val location: String,
  val startDate: LocalDate?,
  val endDate: LocalDate?,
  val maxApplicants: Int,
  val description: String,
  val isUrgent: Boolean
)

@Preview
@Composable
fun ProjectCreateDialogPreview() {
  Jikgong1111Theme {
    ProjectCreateDialog(
      onDismiss = { },
      onConfirm = { }
    )
  }
}