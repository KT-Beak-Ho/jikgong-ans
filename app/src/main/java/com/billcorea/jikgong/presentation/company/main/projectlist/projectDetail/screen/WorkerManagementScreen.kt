package com.billcorea.jikgong.presentation.company.main.projectlist.projectDetail.screen.worker

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
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
      date = LocalDate.parse("2025-08-01"), // 데이터 범위 내 기본 날짜로 변경
      startTime = "08:00",
      endTime = "18:00",
      recruitPeriod = "2025-08-01 ~ 2025-08-07",
      applicants = 12,
      confirmed = 10,
      maxWorkers = 15,
      status = "IN_PROGRESS"
    )
  }
  
  var selectedManagementTab by remember { mutableIntStateOf(0) }
  var selectedDateIndex by remember { mutableIntStateOf(0) }
  
  // 확정 근로자 데이터 (날짜별) - 매번 새로 로드하여 최신 데이터 보장
  val confirmedWorkersByDate = CompanyMockDataFactory.getConfirmedWorkersByDate().mapKeys { 
    LocalDate.parse(it.key) 
  }

  // 지원자 상태 관리 - 매번 새로 로드하여 최신 데이터 보장
  val applicantsByDate = CompanyMockDataFactory.getApplicantWorkersByDate().mapKeys { 
    LocalDate.parse(it.key) 
  }
  
  // 데이터 일관성 테스트 (한 번만 실행)
  LaunchedEffect(Unit) {
    CompanyMockDataFactory.testDataConsistency()
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
  
  // 날짜별 확정인부 체크
  fun hasConfirmedWorkers(date: LocalDate): Boolean {
    return (confirmedWorkersByDate[date] ?: emptyList()).isNotEmpty()
  }
  
  // 날짜별 확정인부 수 계산
  fun getConfirmedWorkersCount(date: LocalDate): Int {
    return (confirmedWorkersByDate[date] ?: emptyList()).size
  }
  
  // 디버깅: 현재 데이터 상태 확인
  LaunchedEffect(selectedDateIndex, selectedManagementTab) {
    println("=== WorkerManagementScreen Debug ===")
    println("selectedDateIndex: $selectedDateIndex")
    println("selectedManagementTab: $selectedManagementTab")
    println("currentDate: $currentDate")
    println("dateRange: ${dateRange.map { it.toString() }}")
    println("dateRange size: ${dateRange.size}")
    println("confirmedWorkers count: ${getConfirmedWorkersCount(currentDate)}")
    println("confirmedWorkers names: ${(confirmedWorkersByDate[currentDate] ?: emptyList()).map { it.name }}")
    println("applicants count: ${(applicantsByDate[currentDate] ?: emptyList()).size}")
    println("all confirmed dates: ${confirmedWorkersByDate.keys.sorted()}")
    println("all applicant dates: ${applicantsByDate.keys.sorted()}")
    println("navigation will pass: ${currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}")
    println("===================================")
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
            val confirmedCount = getConfirmedWorkersCount(currentDate)
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
            val applicantCount = (applicantsByDate[currentDate] ?: emptyList()).size
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
            if (!hasConfirmedWorkers(currentDate)) {
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
                      "출근 확정된 근로자 ${getConfirmedWorkersCount(currentDate)}명 확인하기",
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
                        navController.navigate("attendance_check/${workDay.id}?selectedDate=${currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}")
                      },
                      modifier = Modifier.fillMaxWidth(),
                      shape = RoundedCornerShape(4.dp),
                      colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B7BFF))
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
                        navController.navigate("checkout/${workDay.id}?selectedDate=${currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}")
                      },
                      modifier = Modifier.fillMaxWidth(),
                      shape = RoundedCornerShape(4.dp),
                      colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B7BFF))
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
                        navController.navigate("payment_summary/${workDay.id}?selectedDate=${currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}")
                      },
                      modifier = Modifier.fillMaxWidth(),
                      shape = RoundedCornerShape(4.dp),
                      colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B7BFF))
                    ) {
                      Text("지급내역서 보기")
                    }
                  }
                }
              }
              
            }
          }
          1 -> { // 인부지원 현황 탭
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
      rate >= 80 -> Color(0xFF4B7BFF) // 파란색
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