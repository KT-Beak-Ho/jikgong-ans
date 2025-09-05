package com.billcorea.jikgong.presentation.company.main.projectlist.projectDetail.screen.project

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.annotation.Destination
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.SimpleProject
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.WorkDay
import com.billcorea.jikgong.api.models.sampleDataFactory.CompanyMockDataFactory
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.presentation.company.main.common.BackNavigationTopBar
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun ProjectDetailScreen(
  navController: NavController,
  projectId: String,
  modifier: Modifier = Modifier
) {
  // 실제 프로젝트 데이터 로드
  val project = remember(projectId) {
    val baseProject = CompanyMockDataFactory.getProjectById(projectId)
    if (baseProject != null) {
      SimpleProject(
        id = baseProject.id,
        title = baseProject.title,
        company = baseProject.company,
        location = baseProject.location,
        category = baseProject.category,
        status = baseProject.status,
        startDate = baseProject.startDate,
        endDate = baseProject.endDate,
        wage = baseProject.wage,
        currentApplicants = baseProject.currentApplicants,
        maxApplicants = baseProject.maxApplicants,
        isUrgent = baseProject.isUrgent,
        isBookmarked = baseProject.isBookmarked
      )
    } else {
      // 기본 프로젝트 (fallback)
      CompanyMockDataFactory.getSimpleProjects().firstOrNull() ?: SimpleProject(
        id = projectId,
        title = "프로젝트를 찾을 수 없습니다",
        company = "알 수 없음",
        location = "알 수 없음",
        category = "일반",
        status = "IN_PROGRESS",
        startDate = "2025-08-01",
        endDate = "2025-08-31",
        wage = 150000,
        currentApplicants = 0,
        maxApplicants = 10,
        isUrgent = false,
        isBookmarked = false
      )
    }
  }

  // 실제 프로젝트별 작업일 데이터
  val workDays = remember(projectId) {
    CompanyMockDataFactory.getWorkDaysForProject(projectId)
  }

  var selectedTab by remember { mutableStateOf(0) }
  val bottomSheetState = rememberModalBottomSheetState()
  val scope = rememberCoroutineScope()
  var showBottomSheet by remember { mutableStateOf(false) }
  var selectedWorkDay by remember { mutableStateOf<WorkDay?>(null) }
  var showJobRegistrationBottomSheet by remember { mutableStateOf(false) }
  val jobRegistrationBottomSheetState = rememberModalBottomSheetState()
  
  // 월별 필터링을 위한 상태
  var selectedMonth by remember { mutableStateOf<String?>(null) }
  var showMonthSelector by remember { mutableStateOf(false) }
  
  // 임금 입금 상태 데이터
  val paymentStatus = remember {
    mapOf(
      "9" to true,  // 입금 완료
      "10" to false, // 입금 대기
      "11" to true,  // 입금 완료
      "12" to false  // 입금 대기
    )
  }
  
  // 프로젝트별 등록된 노동자들의 출퇴근 데이터
  val projectWorkers = remember(projectId) {
    CompanyMockDataFactory.getProjectWorkers(projectId)
  }
  
  // 날짜별 지원자 수 계산 함수 - 프로젝트별 작업일에 따라 다름
  fun getApplicantsCountForDate(date: LocalDate): Int {
    // 해당 날짜의 WorkDay 찾기
    val workDay = workDays.find { it.date == date }
    return workDay?.applicants ?: 0
  }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    topBar = {
      BackNavigationTopBar(
        title = project.title,
        onBackClick = { navController.popBackStack() }
      )
    },
    floatingActionButton = {
      ExtendedFloatingActionButton(
        onClick = { 
          showJobRegistrationBottomSheet = true
        },
        containerColor = Color(0xFF4B7BFF),
        contentColor = Color.White,
        modifier = Modifier.height(48.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Add,
          contentDescription = "일자리 등록",
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "일자리 등록",
          style = AppTypography.bodyMedium,
          fontWeight = FontWeight.Medium
        )
      }
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .background(Color(0xFFF8F9FA))
    ) {
      // 탭과 월별 필터
      Column {
        TabRow(
          selectedTabIndex = selectedTab,
          containerColor = Color.White
        ) {
          Tab(
            selected = selectedTab == 0,
            onClick = { 
              selectedTab = 0
              selectedMonth = null
            },
            text = { 
              Text("진행중 (${workDays.count { it.status == "IN_PROGRESS" }})")
            }
          )
          Tab(
            selected = selectedTab == 1,
            onClick = { 
              selectedTab = 1
              selectedMonth = null
            },
            text = { 
              Text("예정 (${workDays.count { it.status == "UPCOMING" }})")
            }
          )
          Tab(
            selected = selectedTab == 2,
            onClick = { 
              selectedTab = 2
              selectedMonth = null
            },
            text = { 
              Text("임금입금 (${workDays.count { it.status == "COMPLETED" }})")
            }
          )
        }
        
        // 월별 필터 버튼 (예정 탭일 때만 표시)
        if (selectedTab == 1) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .background(Color.White)
              .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            val availableMonths = workDays
              .filter { it.status == "UPCOMING" }
              .map { it.date.format(DateTimeFormatter.ofPattern("yyyy년 MM월")) }
              .distinct()
              .sorted()
            
            OutlinedButton(
              onClick = { selectedMonth = null },
              colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (selectedMonth == null) Color(0xFF4B7BFF) else Color.Transparent,
                contentColor = if (selectedMonth == null) Color.White else Color.Gray
              ),
              modifier = Modifier.height(32.dp)
            ) {
              Text("전체", style = AppTypography.bodySmall)
            }
            
            // 실제 예정된 월들 표시
            availableMonths.forEach { month ->
              OutlinedButton(
                onClick = { selectedMonth = month },
                colors = ButtonDefaults.outlinedButtonColors(
                  containerColor = if (selectedMonth == month) Color(0xFF4B7BFF) else Color.Transparent,
                  contentColor = if (selectedMonth == month) Color.White else Color.Gray
                ),
                modifier = Modifier.height(32.dp)
              ) {
                Text(month.substring(5, 7) + "월", style = AppTypography.bodySmall)
              }
            }
          }
        }
      }

      // 작업 일자 목록
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        val filteredWorkDays = when (selectedTab) {
          0 -> workDays.filter { it.status == "IN_PROGRESS" }
          1 -> {
            val upcomingDays = workDays.filter { it.status == "UPCOMING" }
            if (selectedMonth != null) {
              upcomingDays.filter { 
                it.date.format(DateTimeFormatter.ofPattern("yyyy년 MM월")) == selectedMonth 
              }
            } else {
              upcomingDays
            }
          }
          2 -> workDays.filter { it.status == "COMPLETED" }
          else -> workDays
        }

        // 날짜별로 그룹화
        val groupedByMonth = filteredWorkDays.groupBy { 
          it.date.format(DateTimeFormatter.ofPattern("yyyy년 MM월"))
        }

        if (filteredWorkDays.isEmpty()) {
          // 빈 상태 표시
          item {
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 64.dp),
              contentAlignment = Alignment.Center
            ) {
              Column(
                horizontalAlignment = Alignment.CenterHorizontally
              ) {
                Icon(
                  Icons.Default.Work,
                  contentDescription = null,
                  modifier = Modifier.size(64.dp),
                  tint = Color(0xFFE0E0E0)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                  text = when (selectedTab) {
                    0 -> "진행 중인 일자리가 없습니다"
                    1 -> if (selectedMonth != null) "${selectedMonth}에 예정된 일자리가 없습니다" else "예정된 일자리가 없습니다"
                    2 -> "완료된 일자리가 없습니다"
                    else -> "일자리가 없습니다"
                  },
                  style = AppTypography.bodyLarge,
                  color = Color.Gray,
                  fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                  text = when (selectedTab) {
                    0 -> "새로운 일자리를 등록해보세요"
                    1 -> if (selectedMonth != null) "다른 월을 선택하거나 새 일자리를 등록해보세요" else "새로운 일자리를 등록해보세요"
                    2 -> "완료된 프로젝트의 임금 입금 내역을 확인하세요"
                    else -> "새로운 일자리를 등록해보세요"
                  },
                  style = AppTypography.bodyMedium,
                  color = Color.Gray
                )
              }
            }
          }
        } else {
          groupedByMonth.forEach { (month, daysInMonth) ->
            // 월 섹션 헤더
            item {
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 16.dp, vertical = 8.dp)
              ) {
                Text(
                  text = month,
                  style = AppTypography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF4B7BFF)
                )
              }
            }

            // 해당 월의 작업 카드들
            items(daysInMonth) { workDay ->
              if (selectedTab == 2) {
                // 임금입금 확인 카드
                PaymentStatusCard(
                  workDay = workDay,
                  wage = project.wage,
                  isPaymentCompleted = paymentStatus[workDay.id] ?: false,
                  onPaymentStatusChange = { 
                    // 임금 상태 변경 처리
                  },
                  onPaymentDetailsClick = {
                    // 임금 내역서 확인 - PaymentSummaryScreen으로 네비게이션
                    navController.navigate("payment_summary/${workDay.id}?selectedDate=${workDay.date}")
                  }
                )
              } else {
                WorkDayCard(
                  workDay = workDay,
                  wage = project.wage,
                  applicantsCount = getApplicantsCountForDate(workDay.date),
                  onMenuClick = {
                    selectedWorkDay = workDay
                    showBottomSheet = true
                  },
                  onWorkerManageClick = {
                    navController.navigate("worker_management/${workDay.id}")
                  }
                )
              }
            }
          }
        }
      }
    }
  }

  // Bottom Sheet
  if (showBottomSheet && selectedWorkDay != null) {
    ModalBottomSheet(
      onDismissRequest = { 
        showBottomSheet = false
        selectedWorkDay = null
      },
      sheetState = bottomSheetState,
      containerColor = Color.White
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 16.dp)
      ) {
        // 일자리 정보 수정
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clickable {
              scope.launch {
                bottomSheetState.hide()
                showBottomSheet = false
              }
            }
            .padding(horizontal = 20.dp, vertical = 16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            Icons.Outlined.Edit, 
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = Color.Gray
          )
          Spacer(modifier = Modifier.width(16.dp))
          Text(
            "일자리 정보 수정",
            style = AppTypography.bodyLarge
          )
        }
        
        HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp))
        
        // 일자리 삭제
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clickable {
              scope.launch {
                bottomSheetState.hide()
                showBottomSheet = false
              }
            }
            .padding(horizontal = 20.dp, vertical = 16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            Icons.Outlined.Delete, 
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = Color.Red
          )
          Spacer(modifier = Modifier.width(16.dp))
          Text(
            "일자리 삭제",
            style = AppTypography.bodyLarge,
            color = Color.Red
          )
        }
        
        HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp))
        
        // 일자리 재등록
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clickable {
              scope.launch {
                bottomSheetState.hide()
                showBottomSheet = false
              }
            }
            .padding(horizontal = 20.dp, vertical = 16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            Icons.Outlined.Refresh, 
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = Color.Gray
          )
          Spacer(modifier = Modifier.width(16.dp))
          Text(
            "일자리 재등록",
            style = AppTypography.bodyLarge
          )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
      }
    }
  }

  // Job Registration Options Bottom Sheet
  if (showJobRegistrationBottomSheet) {
    ModalBottomSheet(
      onDismissRequest = { 
        showJobRegistrationBottomSheet = false
      },
      sheetState = jobRegistrationBottomSheetState,
      containerColor = Color.White
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 16.dp)
      ) {
        // 제목
        Text(
          text = "일자리 등록 방법 선택",
          style = AppTypography.titleMedium,
          fontWeight = FontWeight.Bold,
          modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )
        
        // 새 공고 작성
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clickable {
              scope.launch {
                jobRegistrationBottomSheetState.hide()
                showJobRegistrationBottomSheet = false
                navController.navigate("job_registration?projectStartDate=${project.startDate}&projectEndDate=${project.endDate}")
              }
            }
            .padding(horizontal = 20.dp, vertical = 16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            Icons.Default.Create, 
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = Color(0xFF4B7BFF)
          )
          Spacer(modifier = Modifier.width(16.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              "새 공고 작성",
              style = AppTypography.bodyLarge,
              fontWeight = FontWeight.Medium
            )
            Text(
              "처음부터 새로운 공고를 작성합니다",
              style = AppTypography.bodySmall,
              color = Color.Gray
            )
          }
          Icon(
            Icons.Default.ChevronRight,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = Color.Gray
          )
        }
        
        HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp))
        
        // 기존 공고 재사용
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clickable {
              scope.launch {
                jobRegistrationBottomSheetState.hide()
                showJobRegistrationBottomSheet = false
                navController.navigate("previous_job_posts/$projectId")
              }
            }
            .padding(horizontal = 20.dp, vertical = 16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            Icons.Default.Refresh, 
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = Color(0xFF4B7BFF)
          )
          Spacer(modifier = Modifier.width(16.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              "기존 공고 재사용",
              style = AppTypography.bodyLarge,
              fontWeight = FontWeight.Medium
            )
            Text(
              "이전 공고를 복사하여 빠르게 등록합니다",
              style = AppTypography.bodySmall,
              color = Color.Gray
            )
          }
          Icon(
            Icons.Default.ChevronRight,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = Color.Gray
          )
        }
        
        HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp))
        
        // 임시저장 불러오기
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .clickable {
              scope.launch {
                jobRegistrationBottomSheetState.hide()
                showJobRegistrationBottomSheet = false
                navController.navigate("temp_save")
              }
            }
            .padding(horizontal = 20.dp, vertical = 16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            Icons.Default.Drafts, 
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = Color(0xFF4B7BFF)
          )
          Spacer(modifier = Modifier.width(16.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              "임시저장 불러오기",
              style = AppTypography.bodyLarge,
              fontWeight = FontWeight.Medium
            )
            Text(
              "임시저장된 공고를 불러와 완성합니다",
              style = AppTypography.bodySmall,
              color = Color.Gray
            )
          }
          Icon(
            Icons.Default.ChevronRight,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = Color.Gray
          )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
      }
    }
  }
}

@Composable
private fun PaymentStatusCard(
  workDay: WorkDay,
  wage: Int,
  isPaymentCompleted: Boolean,
  onPaymentStatusChange: (Boolean) -> Unit,
  onPaymentDetailsClick: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(
      containerColor = Color.White
    ),
    elevation = CardDefaults.cardElevation(
      defaultElevation = 2.dp
    )
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      // 헤더
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        Column(modifier = Modifier.weight(1f)) {
          // 모집 제목
          Text(
            text = workDay.title,
            style = AppTypography.titleMedium,
            fontWeight = FontWeight.Bold
          )
          
          Spacer(modifier = Modifier.height(4.dp))
          
          // 프로젝트 날짜
          Text(
            text = workDay.date.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")),
            style = AppTypography.bodySmall,
            color = Color.Gray
          )
        }
        
        // 입금 상태 뱃지
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = if (isPaymentCompleted) Color(0xFF4CAF50) else Color(0xFFFF9800)
        ) {
          Text(
            text = if (isPaymentCompleted) "입금완료" else "입금대기",
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = AppTypography.bodySmall,
            color = Color.White,
            fontWeight = FontWeight.Medium
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // 입금 정보
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "확정 인원: ${workDay.confirmed}명",
            style = AppTypography.bodyMedium,
            color = Color.Gray
          )
          Text(
            text = "예상 입금액: ${java.text.NumberFormat.getNumberInstance(java.util.Locale.KOREA).format(wage * workDay.confirmed)}원",
            style = AppTypography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = Color.Black
          )
        }
        
        // 입금 확인 버튼
        Button(
          onClick = { onPaymentStatusChange(!isPaymentCompleted) },
          colors = ButtonDefaults.buttonColors(
            containerColor = if (isPaymentCompleted) Color(0xFF4CAF50) else Color(0xFF4B7BFF)
          ),
          shape = RoundedCornerShape(8.dp)
        ) {
          Icon(
            if (isPaymentCompleted) Icons.Default.Check else Icons.Default.AttachMoney,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = if (isPaymentCompleted) "입금됨" else "입금하기",
            style = AppTypography.bodySmall,
            fontWeight = FontWeight.Medium
          )
        }
      }

      Spacer(modifier = Modifier.height(8.dp))

      // 임금 내역 상세보기 버튼
      OutlinedButton(
        onClick = onPaymentDetailsClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp)
      ) {
        Icon(
          Icons.Outlined.Receipt,
          contentDescription = null,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = "임금 내역서 확인",
          style = AppTypography.bodyMedium,
          fontWeight = FontWeight.Medium
        )
      }
    }
  }
}

@Composable
private fun WorkDayCard(
  workDay: WorkDay,
  wage: Int,
  applicantsCount: Int = 0,
  onMenuClick: () -> Unit,
  onWorkerManageClick: () -> Unit
) {
  val isUpcoming = workDay.status == "UPCOMING"
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(
      containerColor = Color.White
    ),
    elevation = CardDefaults.cardElevation(
      defaultElevation = 2.dp
    )
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      // 헤더
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        Column(modifier = Modifier.weight(1f)) {
          // 모집 제목
          Text(
            text = workDay.title,
            style = AppTypography.titleMedium,
            fontWeight = FontWeight.Bold
          )
        }
        
        // 더보기 버튼
        IconButton(
          onClick = onMenuClick,
          modifier = Modifier.size(24.dp)
        ) {
          Icon(
            Icons.Default.MoreVert,
            contentDescription = "메뉴",
            tint = Color.Gray
          )
        }
      }

      Spacer(modifier = Modifier.height(12.dp))
      
      // 수락 대기 알림 (지원자가 있을 경우)
      if (applicantsCount > 0) {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
          shape = RoundedCornerShape(8.dp),
          colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFF3CD)
          )
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              Icons.Filled.NotificationsActive,
              contentDescription = null,
              modifier = Modifier.size(20.dp),
              tint = Color(0xFFFF8C00)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (isUpcoming) {
                "새로운 지원자 ${applicantsCount}명이 지원했습니다"
              } else {
                "수락 대기 중인 지원자 ${applicantsCount}명이 있습니다"
              },
              style = AppTypography.bodySmall,
              color = Color(0xFF856404),
              fontWeight = FontWeight.Medium
            )
          }
        }
      }

      // 모집 기간
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        Icon(
          Icons.Outlined.DateRange,
          contentDescription = null,
          modifier = Modifier.size(16.dp),
          tint = Color.Gray
        )
        Text(
          text = "모집 기간: ${workDay.recruitPeriod}",
          style = AppTypography.bodySmall,
          color = Color.Gray
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      Spacer(modifier = Modifier.height(8.dp))

      // 인력 관리 버튼
      Button(
        onClick = onWorkerManageClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = Color(0xFF4B7BFF)
        )
      ) {
        Icon(
          Icons.Outlined.Groups,
          contentDescription = null,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = "날짜별 인력 관리",
          style = AppTypography.bodyMedium,
          fontWeight = FontWeight.Medium
        )
      }
    }
  }
}

@Preview
@Composable
fun ProjectDetailScreenPreview() {
  Jikgong1111Theme {
    ProjectDetailScreen(
      navController = rememberNavController(),
      projectId = "1"
    )
  }
}