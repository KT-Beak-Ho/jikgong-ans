package com.billcorea.jikgong.presentation.company.main.projectlist.projectDetail.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.company.main.common.BackNavigationTopBar
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// 지원자 데이터
data class ApplicantWorker(
  val id: String,
  val name: String,
  val age: Int,
  val gender: String, // "남", "여"
  val experience: Int, // 경력 년수
  val attendanceRate: Int, // 출석률 (0-100)
  val totalWorkDays: Int, // 총 출역 회수
  val phoneNumber: String,
  val isSelected: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerManagementScreen(
  navController: NavController,
  workDayId: String,
  modifier: Modifier = Modifier
) {
  // 임시 데이터 - 실제로는 workDayId를 통해 데이터를 가져와야 함
  val workDay = remember {
    WorkDay(
      id = workDayId,
      title = "보통인부 15명 모집",
      date = LocalDate.now(),
      startTime = "08:00",
      endTime = "18:00",
      recruitPeriod = "2025-08-01 ~ 2025-08-07",
      applicants = 12,
      confirmed = 10,
      maxWorkers = 15,
      status = "IN_PROGRESS"
    )
  }
  
  var selectedManagementTab by remember { mutableStateOf(0) }
  var selectedDateIndex by remember { mutableStateOf(0) }
  
  // 지원자 상태 관리
  val applicantsByDate = remember {
    mapOf(
      LocalDate.parse("2025-08-01") to listOf(
        ApplicantWorker("1", "홍길동", 28, "남", 3, 85, 15, "010-1111-2222"),
        ApplicantWorker("2", "김영희", 32, "여", 5, 92, 22, "010-2222-3333"),
        ApplicantWorker("3", "박철수", 29, "남", 2, 78, 12, "010-3333-4444")
      ),
      LocalDate.parse("2025-08-02") to listOf(
        ApplicantWorker("4", "이민수", 35, "남", 7, 88, 28, "010-4444-5555"),
        ApplicantWorker("5", "정수현", 27, "여", 4, 95, 18, "010-5555-6666"),
        ApplicantWorker("9", "강민호", 30, "남", 6, 82, 25, "010-6666-7777"),
        ApplicantWorker("10", "송유진", 26, "여", 2, 89, 10, "010-7777-8888"),
        ApplicantWorker("11", "김태준", 33, "남", 8, 76, 32, "010-8888-9999"),
        ApplicantWorker("12", "이소영", 29, "여", 3, 93, 16, "010-9999-0000"),
        ApplicantWorker("13", "박지훈", 31, "남", 5, 87, 20, "010-0000-1111")
      ),
      LocalDate.parse("2025-08-03") to emptyList(),
      LocalDate.parse("2025-08-04") to listOf(
        ApplicantWorker("6", "최하나", 31, "여", 4, 90, 19, "010-1122-3344")
      )
    )
  }
  
  var selectedApplicants by remember { mutableStateOf<List<ApplicantWorker>>(emptyList()) }
  var showDialog by remember { mutableStateOf(false) }
  var dialogAction by remember { mutableStateOf("") } // "수락" 또는 "거절"
  
  // 모집 기간 파싱 (예: "2025-08-01 ~ 2025-08-07")
  val dateRange = remember(workDay) {
    val dates = workDay.recruitPeriod.split(" ~ ")
    if (dates.size == 2) {
      try {
        val startDate = LocalDate.parse(dates[0])
        val endDate = LocalDate.parse(dates[1])
        var currentDate = startDate
        val dateList = mutableListOf<LocalDate>()
        while (!currentDate.isAfter(endDate)) {
          dateList.add(currentDate)
          currentDate = currentDate.plusDays(1)
        }
        dateList
      } catch (e: Exception) {
        listOf(workDay.date)
      }
    } else {
      listOf(workDay.date)
    }
  }
  
  // 날짜별 색상 결정 함수
  fun getDateColor(date: LocalDate, isSelected: Boolean): Color {
    return when (date.dayOfWeek.value) {
      6 -> Color(0xFF0066CC) // 토요일 - 파란색
      7 -> Color(0xFFCC0000) // 일요일 - 빨간색
      else -> if (isSelected) appColorScheme.primary else Color.Black // 평일 - 검정색
    }
  }
  
  // 8월 2일 확정인부 없음 예시를 위한 체크
  fun hasConfirmedWorkers(date: LocalDate): Boolean {
    // 8월 2일은 확정인부가 없다고 가정
    if (date.monthValue == 8 && date.dayOfMonth == 2) {
      return false
    }
    return workDay.confirmed > 0
  }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    topBar = {
      BackNavigationTopBar(
        title = workDay.title,
        onBackClick = { navController.popBackStack() }
      )
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .background(Color.White)
    ) {
      
      // 탭 (확정인부, 인부지원 현황)
      TabRow(
        selectedTabIndex = selectedManagementTab,
        containerColor = Color.White,
        modifier = Modifier.fillMaxWidth()
      ) {
        Tab(
          selected = selectedManagementTab == 0,
          onClick = { selectedManagementTab = 0 },
          text = { Text("확정인부") }
        )
        Tab(
          selected = selectedManagementTab == 1,
          onClick = { selectedManagementTab = 1 },
          text = { Text("인부지원 현황") }
        )
      }
      
      // 날짜 선택 탭 (스크롤 가능)
      if (dateRange.size > 1) {
        ScrollableTabRow(
          selectedTabIndex = selectedDateIndex,
          containerColor = Color.White,
          edgePadding = 8.dp,
          modifier = Modifier.fillMaxWidth()
        ) {
          dateRange.forEachIndexed { index, date ->
            Tab(
              selected = selectedDateIndex == index,
              onClick = { selectedDateIndex = index },
              text = { 
                val applicantsForDate = applicantsByDate[date] ?: emptyList()
                val hasApplicants = applicantsForDate.isNotEmpty()
                
                Row(
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = if (selectedManagementTab == 1 && hasApplicants) {
                      "${date.format(DateTimeFormatter.ofPattern("MM/dd"))} (${applicantsForDate.size})"
                    } else {
                      date.format(DateTimeFormatter.ofPattern("MM/dd"))
                    },
                    style = AppTypography.bodySmall,
                    fontWeight = if (selectedDateIndex == index) FontWeight.Bold else FontWeight.Normal,
                    color = getDateColor(date, selectedDateIndex == index)
                  )
                  
                  // 인부지원현황 탭이고 지원자가 있을 때 빨간 점 표시
                  if (selectedManagementTab == 1 && hasApplicants) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Badge(
                      modifier = Modifier.size(8.dp),
                      containerColor = Color(0xFFF44336)
                    ) {
                      // 빈 내용 (점만 표시)
                    }
                  }
                }
              }
            )
          }
        }
      }
      
      // 선택된 날짜 정보
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 12.dp)
      ) {
        Text(
          text = dateRange.getOrNull(selectedDateIndex)?.format(
            DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
          ) ?: workDay.date.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")),
          style = AppTypography.bodyMedium,
          fontWeight = FontWeight.Medium
        )
      }
      
      Divider(thickness = 0.5.dp, color = Color.LightGray)
      
      // 탭에 따른 내용 표시
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .weight(1f),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        when (selectedManagementTab) {
          0 -> { // 확정인부 탭
            val currentDate = dateRange.getOrNull(selectedDateIndex) ?: workDay.date
            
            if (!hasConfirmedWorkers(currentDate)) {
              // 확정인부가 없는 경우
              item {
                Box(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 48.dp),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = "확정된 인부가 없습니다",
                    style = AppTypography.bodyLarge,
                    color = Color.Gray
                  )
                }
              }
            } else {
              // 확정인부가 있는 경우 - 4개 카드 표시
              item {
                // 출근 확정된 근로자 카드
                Card(
                  modifier = Modifier.fillMaxWidth(),
                  shape = RoundedCornerShape(8.dp),
                  colors = CardDefaults.cardColors(containerColor = Color.White),
                  elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                  Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                      "출근 확정된 근로자 ${workDay.confirmed}명 확인하기",
                      style = AppTypography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                      onClick = { 
                        navController.navigate("worker_info/${workDay.id}")
                      },
                      modifier = Modifier.fillMaxWidth(),
                      shape = RoundedCornerShape(4.dp)
                    ) {
                      Text("출근확정 근로자 정보")
                    }
                  }
                }
              }
              
              item {
                // 출근 여부 확인 카드
                Card(
                  modifier = Modifier.fillMaxWidth(),
                  shape = RoundedCornerShape(8.dp),
                  colors = CardDefaults.cardColors(containerColor = Color.White),
                  elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                  Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                      "근로자의 출근 여부 확인하기",
                      style = AppTypography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                      onClick = { 
                        navController.navigate("attendance_check/${workDay.id}")
                      },
                      modifier = Modifier.fillMaxWidth(),
                      shape = RoundedCornerShape(4.dp)
                    ) {
                      Text("출근확인")
                    }
                  }
                }
              }
              
              item {
                // 퇴근 여부 확인 카드
                Card(
                  modifier = Modifier.fillMaxWidth(),
                  shape = RoundedCornerShape(8.dp),
                  colors = CardDefaults.cardColors(containerColor = Color.White),
                  elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                  Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                      "근로자 퇴근여부 확인하기",
                      style = AppTypography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                      onClick = { 
                        navController.navigate("checkout/${workDay.id}")
                      },
                      modifier = Modifier.fillMaxWidth(),
                      shape = RoundedCornerShape(4.dp)
                    ) {
                      Text("퇴근확인")
                    }
                  }
                }
              }
              
              item {
                // 지급내역 확인 카드
                Card(
                  modifier = Modifier.fillMaxWidth(),
                  shape = RoundedCornerShape(8.dp),
                  colors = CardDefaults.cardColors(containerColor = Color.White),
                  elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                  Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                      "지급내역 확인하기",
                      style = AppTypography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                      onClick = { 
                        navController.navigate("payment_summary/${workDay.id}")
                      },
                      modifier = Modifier.fillMaxWidth(),
                      shape = RoundedCornerShape(4.dp)
                    ) {
                      Text("지급내역서 보기")
                    }
                  }
                }
              }
              
              // 자동 임금 지급 안내
              item {
                Box(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = "18:00시까지 확인하지 않을경우 자동으로 임금이 지급됩니다.",
                    style = AppTypography.bodySmall,
                    color = Color(0xFFFF6B00) // 주황색
                  )
                }
              }
            }
          }
          1 -> { // 인부지원 현황 탭
            val currentDate = dateRange.getOrNull(selectedDateIndex) ?: workDay.date
            val applicantsForCurrentDate = applicantsByDate[currentDate] ?: emptyList()
            
            if (applicantsForCurrentDate.isEmpty()) {
              // 지원한 인부가 없는 경우
              item {
                Box(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 48.dp),
                  contentAlignment = Alignment.Center
                ) {
                  Text(
                    text = "지원한 인부가 없습니다",
                    style = AppTypography.bodyLarge,
                    color = Color.Gray
                  )
                }
              }
            } else {
              // 지원한 인부가 있는 경우
              items(applicantsForCurrentDate) { applicant ->
                ApplicantCard(
                  applicant = applicant,
                  onSelectionChanged = { isSelected ->
                    selectedApplicants = if (isSelected) {
                      selectedApplicants + applicant
                    } else {
                      selectedApplicants - applicant
                    }
                  }
                )
              }
            }
          }
        }
      }
      
      // 선택된 지원자가 있을 때 하단 버튼 표시
      if (selectedApplicants.isNotEmpty() && selectedManagementTab == 1) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          OutlinedButton(
            onClick = { 
              dialogAction = "거절"
              showDialog = true
            },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(8.dp)
          ) {
            Text(
              text = "거절(${selectedApplicants.size})",
              color = Color(0xFFF44336),
              fontWeight = FontWeight.Medium
            )
          }
          
          Button(
            onClick = { 
              dialogAction = "수락"
              showDialog = true
            },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = appColorScheme.primary)
          ) {
            Text(
              text = "수락(${selectedApplicants.size})",
              color = Color.White,
              fontWeight = FontWeight.Medium
            )
          }
        }
      }
    }
  }
  
  // 확인 다이얼로그
  if (showDialog) {
    ApplicantActionDialog(
      action = dialogAction,
      selectedApplicants = selectedApplicants,
      onDismiss = { showDialog = false },
      onConfirm = {
        // TODO: 수락/거절 처리
        selectedApplicants = emptyList()
        showDialog = false
      }
    )
  }
}

@Composable
private fun ApplicantCard(
  applicant: ApplicantWorker,
  onSelectionChanged: (Boolean) -> Unit
) {
  var isSelected by remember { mutableStateOf(false) }
  
  // 출석률에 따른 배경색 결정
  fun getAttendanceBadgeColor(rate: Int): Color {
    return when {
      rate >= 90 -> Color(0xFF4CAF50) // 초록색
      rate >= 80 -> Color(0xFF2196F3) // 파란색
      rate >= 70 -> Color(0xFFFF9800) // 주황색
      else -> Color(0xFFF44336) // 빨간색
    }
  }

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable {
        isSelected = !isSelected
        onSelectionChanged(isSelected)
      },
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(start = 8.dp, top = 16.dp, end = 16.dp, bottom = 16.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // 체크박스
      Checkbox(
        checked = isSelected,
        onCheckedChange = { 
          isSelected = it
          onSelectionChanged(it)
        },
        modifier = Modifier.align(Alignment.CenterVertically)
      )
      
      Spacer(modifier = Modifier.width(3.dp))
      
      // 아바타
      Box(
        modifier = Modifier
          .size(48.dp)
          .clip(CircleShape)
          .background(appColorScheme.primary.copy(alpha = 0.1f))
          .align(Alignment.CenterVertically),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          Icons.Default.Person,
          contentDescription = null,
          modifier = Modifier.size(28.dp),
          tint = appColorScheme.primary
        )
      }
      
      Spacer(modifier = Modifier.width(12.dp))
      
      // 근로자 정보
      Column(
        modifier = Modifier.weight(1f)
      ) {
        // 이름과 출석률
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = applicant.name,
            style = AppTypography.titleMedium,
            fontWeight = FontWeight.Bold
          )
          
          // 출석률 뱃지
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = getAttendanceBadgeColor(applicant.attendanceRate)
          ) {
            Text(
              text = "${applicant.attendanceRate}% 출석",
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              style = AppTypography.bodySmall,
              color = Color.White,
              fontWeight = FontWeight.Medium
            )
          }
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // 기본 정보
        Text(
          text = "만 ${applicant.age}세 • ${applicant.gender} • 경력 ${applicant.experience}년",
          style = AppTypography.bodyMedium,
          color = Color.Gray
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // 출역 정보와 전화번호
        Text(
          text = "총 출역 ${applicant.totalWorkDays}회 | ${applicant.phoneNumber}",
          style = AppTypography.bodySmall,
          color = Color.Gray
        )
      }
    }
  }
}

@Composable
private fun ApplicantActionDialog(
  action: String,
  selectedApplicants: List<ApplicantWorker>,
  onDismiss: () -> Unit,
  onConfirm: () -> Unit
) {
  // 출석률에 따른 배경색 결정
  fun getAttendanceBadgeColor(rate: Int): Color {
    return when {
      rate >= 90 -> Color(0xFF4CAF50) // 초록색
      rate >= 80 -> Color(0xFF2196F3) // 파란색
      rate >= 70 -> Color(0xFFFF9800) // 주황색
      else -> Color(0xFFF44336) // 빨간색
    }
  }
  
  Dialog(onDismissRequest = onDismiss) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
      Column(
        modifier = Modifier.padding(24.dp)
      ) {
        // 제목 (가운데 정렬, 크기 80%로 축소)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.Center,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "인부 지원 $action ",
            style = AppTypography.titleMedium, // titleLarge에서 titleMedium으로 축소
            fontWeight = FontWeight.Bold
          )
          Text(
            text = "${selectedApplicants.size}",
            style = AppTypography.titleMedium, // titleLarge에서 titleMedium으로 축소
            fontWeight = FontWeight.Bold,
            color = if (action == "수락") appColorScheme.primary else Color(0xFFF44336)
          )
        }
        
        Spacer(modifier = Modifier.height(24.dp)) // 16dp에서 24dp로 1.5배 증가
        
        // 선택된 근로자 목록
        selectedApplicants.forEachIndexed { index, applicant ->
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = applicant.name,
              style = AppTypography.bodyMedium,
              fontWeight = FontWeight.Bold // Bold 처리
            )
            
            Surface(
              shape = RoundedCornerShape(12.dp),
              color = getAttendanceBadgeColor(applicant.attendanceRate)
            ) {
              Text(
                text = "${applicant.attendanceRate}% 출석",
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                style = AppTypography.bodySmall,
                color = Color.White,
                fontWeight = FontWeight.Medium
              )
            }
          }
          
          // 마지막 항목이 아닐 때만 구분선 추가
          if (index < selectedApplicants.size - 1) {
            Divider(
              modifier = Modifier.padding(vertical = 6.dp),
              color = Color.Gray.copy(alpha = 0.3f), // 더 진한 색으로 변경
              thickness = 1.dp // 두께도 증가
            )
          }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        Divider()
        Spacer(modifier = Modifier.height(16.dp))
        
        // 확인 문구 (가운데 정렬)
        Box(
          modifier = Modifier.fillMaxWidth(),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "${action}하시겠습니까?",
            style = AppTypography.bodyLarge
          )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // 버튼들
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          OutlinedButton(
            onClick = onDismiss,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(8.dp)
          ) {
            Text("취소")
          }
          
          Button(
            onClick = onConfirm,
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = if (action == "수락") appColorScheme.primary else Color(0xFFF44336)
            )
          ) {
            Text(
              text = action,
              color = Color.White
            )
          }
        }
      }
    }
  }
}

@Preview
@Composable
fun WorkerManagementScreenPreview() {
  Jikgong1111Theme {
    WorkerManagementScreen(
      navController = rememberNavController(),
      workDayId = "1"
    )
  }
}