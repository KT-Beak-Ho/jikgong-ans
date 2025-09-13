package com.billcorea.jikgong.presentation.company.main.projectlist.feature.detail.screen

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.company.main.common.BackNavigationTopBar
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.billcorea.jikgong.api.models.sampleDataFactory.CompanyMockDataFactory
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.ApplicantWorker
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.WorkDay
import com.billcorea.jikgong.presentation.company.main.projectlist.data.WorkerAttendanceInfo
import com.billcorea.jikgong.presentation.company.main.projectlist.data.DateStatus
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkerManagementScreen(
  navController: NavController,
  workDayId: String,
  modifier: Modifier = Modifier
) {
  // 오늘 날짜 정의 - CompanyMockDataFactory에서 가져오기
  val TODAY = CompanyMockDataFactory.getTodayDate()
  
  // WorkDay 데이터 - CompanyMockDataFactory에서 가져오기
  val workDay = remember {
    CompanyMockDataFactory.getWorkDayById(workDayId)
  }
  
  // 출근/퇴근 상태 데이터 - CompanyMockDataFactory에서 가져오기
  var attendanceStatus by remember(workDayId) {
    mutableStateOf(CompanyMockDataFactory.getWorkerAttendanceStatus())
  }
  
  var selectedManagementTab by remember(workDayId) { mutableIntStateOf(0) }
  var selectedDateIndex by remember(workDayId) { mutableIntStateOf(0) }
  
  var selectedApplicants by remember(workDayId) { mutableStateOf<List<ApplicantWorker>>(emptyList()) }
  var showDialog by remember(workDayId) { mutableStateOf(false) }
  var dialogAction by remember(workDayId) { mutableStateOf("") } // "수락" 또는 "거절"
  
  // 지원자와 확정 인부 데이터 새로고침을 위한 상태
  var refreshTrigger by remember(workDayId) { mutableIntStateOf(0) }
  
  // WorkDay 기반 확정 근로자 데이터 - 실제 모집 기간에 맞춘 데이터 사용
  val confirmedWorkers = remember(workDayId, refreshTrigger) {
    CompanyMockDataFactory.getConfirmedWorkersForWorkDay(workDayId)
  }

  // WorkDay 기반 지원자 데이터 - 실제 모집 기간에 맞춘 데이터 사용
  val applicantWorkers = remember(workDayId, refreshTrigger) {
    CompanyMockDataFactory.getApplicantWorkersForWorkDay(workDayId)
  }
  
  // WorkDay 데이터 일관성 테스트 (한 번만 실행)
  LaunchedEffect(Unit) {
    CompanyMockDataFactory.testWorkDayDataConsistency()
  }
  
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
      } catch (_: Exception) {
        listOf(workDay.date)
      }
    } else {
      listOf(workDay.date)
    }
  }
  
  // 현재 선택된 날짜
  val currentDate = dateRange.getOrNull(selectedDateIndex) ?: workDay.date
  
  // 날짜별 색상 결정 함수
  fun getDateColor(date: LocalDate, isSelected: Boolean): Color {
    return when (date.dayOfWeek.value) {
      6 -> Color(0xFF0066CC) // 토요일 - 파란색
      7 -> Color(0xFFCC0000) // 일요일 - 빨간색
      else -> if (isSelected) appColorScheme.primary else Color.Black // 평일 - 검정색
    }
  }
  
  // WorkDay 기반 확정인부 체크
  fun hasConfirmedWorkers(): Boolean {
    return confirmedWorkers.isNotEmpty()
  }
  
  // WorkDay 기반 확정인부 수 계산
  fun getConfirmedWorkersCount(): Int {
    return confirmedWorkers.size
  }
  
  // 날짜 상태 판단 함수
  fun getDateStatus(date: LocalDate): DateStatus {
    return when {
      date.isBefore(TODAY) -> DateStatus.PAST
      date.isEqual(TODAY) -> DateStatus.TODAY
      else -> DateStatus.FUTURE
    }
  }
  
  // 특정 날짜의 출근/퇴근 상태 가져오기
  fun getAttendanceStatusForDate(date: LocalDate): WorkerAttendanceInfo {
    return attendanceStatus[date] ?: WorkerAttendanceInfo()
  }
  
  // 출근 상태 업데이트 함수
  fun updateAttendanceStatus(date: LocalDate, status: WorkerAttendanceInfo) {
    attendanceStatus = attendanceStatus + (date to status)
  }
  
  // 디버깅: 현재 데이터 상태 확인 (WorkDay 기반)
  LaunchedEffect(selectedDateIndex, selectedManagementTab) {
    println("=== WorkerManagementScreen Debug (WorkDay-based) ===")
    println("workDayId: $workDayId")
    println("selectedDateIndex: $selectedDateIndex")
    println("selectedManagementTab: $selectedManagementTab")
    println("currentDate: $currentDate")
    println("dateRange: ${dateRange.map { it.toString() }}")
    println("dateRange size: ${dateRange.size}")
    println("confirmedWorkers count: ${getConfirmedWorkersCount()}")
    println("confirmedWorkers names: ${confirmedWorkers.map { it.name }}")
    println("applicants count: ${applicantWorkers.size}")
    println("recruitment period: ${workDay.recruitPeriod}")
    println("navigation will pass: ${currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}")
    println("===================================================")
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
          onClick = { 
            selectedManagementTab = 0
            selectedApplicants = emptyList() // 탭 변경 시 선택 초기화
          },
          text = { 
            val confirmedCount = getConfirmedWorkersCount()
            Text("확정인부 ($confirmedCount)")
          }
        )
        Tab(
          selected = selectedManagementTab == 1,
          onClick = { 
            selectedManagementTab = 1
            selectedApplicants = emptyList() // 탭 변경 시 선택 초기화
          },
          text = { 
            val applicantCount = applicantWorkers.size
            Text("인부지원 현황 ($applicantCount)")
          }
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
              onClick = { 
                selectedDateIndex = index
                selectedApplicants = emptyList() // 날짜 변경 시 선택 초기화
              },
              text = { 
                // WorkDay 기반에서는 모든 날짜가 동일한 데이터를 보여줌
                val hasApplicants = applicantWorkers.isNotEmpty()
                
                Row(
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Text(
                    text = if (selectedManagementTab == 1 && hasApplicants) {
                      "${date.format(DateTimeFormatter.ofPattern("MM/dd"))} (${applicantWorkers.size})"
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
      
      HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
      
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
            if (!hasConfirmedWorkers()) {
              // 확정인부가 없는 경우
              item {
                Box(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 48.dp),
                  contentAlignment = Alignment.Center
                ) {
                  Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                  ) {
                    Text(
                      text = "${currentDate.format(DateTimeFormatter.ofPattern("MM월 dd일"))}에 확정된 인부가 없습니다",
                      style = AppTypography.bodyLarge,
                      color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                      text = "다른 날짜를 선택하거나 인부지원 현황을 확인해보세요",
                      style = AppTypography.bodySmall,
                      color = Color.Gray
                    )
                  }
                }
              }
            } else {
              // 확정인부가 있는 경우 - 날짜별로 다른 카드 표시
              val dateStatus = getDateStatus(currentDate)
              val attendanceStatus = getAttendanceStatusForDate(currentDate)
              
              item {
                // 출근 확정된 근로자 카드 (모든 날짜에 표시)
                Card(
                  modifier = Modifier.fillMaxWidth(),
                  shape = RoundedCornerShape(8.dp),
                  colors = CardDefaults.cardColors(containerColor = Color.White),
                  elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                  Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                      "출근 확정된 근로자 ${getConfirmedWorkersCount()}명 확인하기",
                      style = AppTypography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                      onClick = { 
                        navController.navigate("worker_info/${workDay.id}?selectedDate=${currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}")
                      },
                      modifier = Modifier.fillMaxWidth(),
                      shape = RoundedCornerShape(4.dp),
                      colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B7BFF))
                    ) {
                      Text("출근확정 근로자 정보")
                    }
                  }
                }
              }
              
              // 출근 여부 확인 카드 (과거/현재 날짜는 항상 표시, 미래 날짜는 데이터가 있을 때만 표시)
              if (dateStatus == DateStatus.PAST || dateStatus == DateStatus.TODAY || attendanceStatus.hasCheckedIn || attendanceStatus.hasCheckedOut || attendanceStatus.hasPaymentRecord) {
                item {
                  Column {
                    AttendanceCheckCard(
                      type = "출근",
                      description = when {
                        attendanceStatus.hasCheckedIn -> "출근 내역"
                        dateStatus == DateStatus.PAST -> "출근 내역 필요"
                        dateStatus == DateStatus.TODAY -> "근로자의 출근 여부 확인하기"
                        else -> "출근 내역 대기"
                      },
                      buttonText = when {
                        attendanceStatus.hasCheckedIn -> "출근 내역 보기"
                        dateStatus == DateStatus.PAST -> "출근 내역 보기"
                        dateStatus == DateStatus.TODAY -> "출근 내역"
                        else -> "출근 내역 대기"
                      },
                      buttonColor = Color(0xFF4B7BFF),
                      isEnabled = true, // 모든 날짜에서 정보 열람 가능
                      onClick = {
                        if (dateStatus == DateStatus.TODAY) {
                          // 출근 확인 처리
                          updateAttendanceStatus(currentDate, attendanceStatus.copy(hasCheckedIn = true))
                        }
                        navController.navigate("attendance_check/${workDay.id}?selectedDate=${currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}")
                      }
                    )
                    
                    // 출근 확인이 필요한 근로자가 있을 때 경고 메시지 표시
                    if (!attendanceStatus.hasCheckedIn && confirmedWorkers.isNotEmpty()) {
                      Spacer(modifier = Modifier.height(8.dp))
                      Text(
                        text = "출근 확인 필요한 근로자 존재",
                        style = AppTypography.bodySmall,
                        color = Color(0xFFFF0000),
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                      )
                    }
                  }
                }
              }
              
              // 퇴근 여부 확인 카드 (과거/현재 날짜는 항상 표시, 미래 날짜는 출근 확인 후 표시)
              if (dateStatus == DateStatus.PAST || dateStatus == DateStatus.TODAY || attendanceStatus.hasCheckedIn || attendanceStatus.hasCheckedOut) {
                item {
                  AttendanceCheckCard(
                    type = "퇴근",
                    description = when {
                      attendanceStatus.hasCheckedOut -> "퇴근 내역"
                      dateStatus == DateStatus.PAST -> "퇴근 내역 필요"
                      dateStatus == DateStatus.TODAY -> "근로자 퇴근여부 확인하기"
                      else -> "퇴근 내역 대기"
                    },
                    buttonText = when {
                      attendanceStatus.hasCheckedOut -> "퇴근 내역 보기"
                      dateStatus == DateStatus.PAST -> "퇴근 내역 보기"
                      dateStatus == DateStatus.TODAY -> "퇴근 내역"
                      else -> "퇴근 내역 대기"
                    },
                    buttonColor = Color(0xFF4B7BFF),
                    isEnabled = true, // 모든 날짜에서 정보 열람 가능
                    onClick = {
                      if (dateStatus == DateStatus.TODAY) {
                        // 퇴근 확인 처리
                        updateAttendanceStatus(currentDate, attendanceStatus.copy(hasCheckedOut = true))
                      }
                      navController.navigate("checkout/${workDay.id}?selectedDate=${currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}")
                    }
                  )
                }
              }
              
              // 지급내역 확인 카드 (과거/현재 날짜는 항상 표시, 미래 날짜는 지급내역이 있을 때만 표시)
              if (dateStatus == DateStatus.PAST || dateStatus == DateStatus.TODAY || attendanceStatus.hasPaymentRecord) {
                item {
                  AttendanceCheckCard(
                    type = "지급내역",
                    description = if (attendanceStatus.hasPaymentRecord) "지급 내역" else "지급 내역 필요",
                    buttonText = if (attendanceStatus.hasPaymentRecord) "지급 내역 보기" else "지급 내역 보기",
                    buttonColor = Color(0xFF4B7BFF),
                    isEnabled = true,
                    onClick = {
                      if (!attendanceStatus.hasPaymentRecord) {
                        // 지급내역 확인 처리
                        updateAttendanceStatus(currentDate, attendanceStatus.copy(hasPaymentRecord = true))
                      }
                      navController.navigate("payment_summary/${workDay.id}?selectedDate=${currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}")
                    }
                  )
                }
              }
              
              // 미래 날짜에 대한 안내 메시지 (과거 데이터가 없는 경우에만 표시)
              if (dateStatus == DateStatus.FUTURE && !attendanceStatus.hasCheckedIn && !attendanceStatus.hasCheckedOut && !attendanceStatus.hasPaymentRecord) {
                item {
                  Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                  ) {
                    Column(
                      modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                      horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                      Icon(
                        Icons.Default.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = Color(0xFF9E9E9E)
                      )
                      Spacer(modifier = Modifier.height(8.dp))
                      Text(
                        text = "아직 해당 날짜가 되지 않았습니다",
                        style = AppTypography.bodyMedium,
                        color = Color(0xFF6B7280),
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                      )
                      Spacer(modifier = Modifier.height(4.dp))
                      Text(
                        text = "출근/퇴근 확인 및 지급내역은\n해당 날짜 이후에 확인 가능합니다",
                        style = AppTypography.bodySmall,
                        color = Color(0xFF9E9E9E),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                      )
                    }
                  }
                }
              }
            }
          }
          1 -> { // 인부지원 현황 탭
            // WorkDay 기반 지원자 데이터 사용
            if (applicantWorkers.isEmpty()) {
              // 지원한 인부가 없는 경우
              item {
                Box(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 48.dp),
                  contentAlignment = Alignment.Center
                ) {
                  Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                  ) {
                    Text(
                      text = "${currentDate.format(DateTimeFormatter.ofPattern("MM월 dd일"))}에 지원한 인부가 없습니다",
                      style = AppTypography.bodyLarge,
                      color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                      text = "다른 날짜를 선택해보세요",
                      style = AppTypography.bodySmall,
                      color = Color.Gray
                    )
                  }
                }
              }
            } else {
              // 지원한 인부가 있는 경우
              items(applicantWorkers) { applicant ->
                ApplicantCard(
                  applicant = applicant,
                  isSelected = selectedApplicants.contains(applicant),
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
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B7BFF))
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
        // 수락/거절 처리
        if (dialogAction == "수락") {
          CompanyMockDataFactory.acceptApplicants(workDay.id, selectedApplicants)
        } else {
          CompanyMockDataFactory.rejectApplicants(workDay.id, selectedApplicants)
        }
        
        // UI 상태 초기화 및 데이터 새로고침
        selectedApplicants = emptyList()
        showDialog = false
        refreshTrigger++ // 데이터 새로고침 트리거
      }
    )
  }
}

@Composable
private fun AttendanceCheckCard(
  type: String,
  description: String,
  buttonText: String,
  buttonColor: Color,
  isEnabled: Boolean,
  onClick: () -> Unit
) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(8.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Text(
        text = description,
        style = AppTypography.bodyMedium
      )
      Spacer(modifier = Modifier.height(12.dp))
      Button(
        onClick = onClick,
        enabled = isEnabled,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(4.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = if (isEnabled) buttonColor else Color.Gray,
          disabledContainerColor = Color.Gray
        )
      ) {
        Text(
          text = buttonText,
          color = if (isEnabled) Color.White else Color.White.copy(alpha = 0.6f)
        )
      }
    }
  }
}

@Composable
private fun ApplicantCard(
  applicant: ApplicantWorker,
  isSelected: Boolean,
  onSelectionChanged: (Boolean) -> Unit
) {
  
  // 출석률에 따른 배경색 결정
  fun getAttendanceBadgeColor(rate: Int): Color {
    return when {
      rate >= 90 -> Color(0xFF4CAF50) // 초록색
      rate >= 80 -> Color(0xFF4B7BFF) // 파란색
      rate >= 70 -> Color(0xFFFF9800) // 주황색
      else -> Color(0xFFF44336) // 빨간색
    }
  }

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable {
        onSelectionChanged(!isSelected)
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
        
        // 기본 정보 (직종과 기술 수준 포함)
        Text(
          text = "${applicant.jobType ?: "일반"} (${applicant.skill ?: "중급"}) • 만 ${applicant.age}세 • ${applicant.gender} • 경력 ${applicant.experience}년",
          style = AppTypography.bodyMedium,
          color = Color.Gray
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // 평점과 거리 정보
        Row(
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "⭐ ${String.format("%.1f", applicant.rating)}",
            style = AppTypography.bodySmall,
            color = Color(0xFFFF9800)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "📍 ${String.format("%.1f", applicant.distance)}km",
            style = AppTypography.bodySmall,
            color = Color.Gray
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = applicant.workPreference ?: "혼합",
            style = AppTypography.bodySmall,
            color = Color(0xFF4B7BFF),
            fontWeight = FontWeight.Medium
          )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // 출역 정보와 전화번호
        Text(
          text = "총 출역 ${applicant.totalWorkDays}회 | ${applicant.phoneNumber}",
          style = AppTypography.bodySmall,
          color = Color.Gray
        )
        
        // 자격증 정보 (있을 경우만 표시)
        if (applicant.certifications.isNotEmpty()) {
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = "🏆 ${applicant.certifications.joinToString(", ")}",
            style = AppTypography.bodySmall,
            color = Color(0xFF4CAF50),
            fontWeight = FontWeight.Medium
          )
        }
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
      rate >= 80 -> Color(0xFF4B7BFF) // 파란색
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
            HorizontalDivider(
              modifier = Modifier.padding(vertical = 6.dp),
              color = Color.Gray.copy(alpha = 0.3f), // 더 진한 색으로 변경
              thickness = 1.dp // 두께도 증가
            )
          }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider()
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