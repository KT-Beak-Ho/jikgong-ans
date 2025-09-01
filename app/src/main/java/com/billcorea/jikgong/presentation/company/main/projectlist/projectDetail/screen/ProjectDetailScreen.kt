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
import com.billcorea.jikgong.ui.theme.appColorScheme
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
  // 샘플 프로젝트 데이터
  val project = remember {
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
  }

  // 샘플 일자별 작업 데이터
  val workDays = remember {
    listOf(
      // 진행중
      WorkDay("1", "보통인부 15명 모집", LocalDate.now(), "08:00", "18:00", "2025-08-01 ~ 2025-08-07", 12, 10, 15, "IN_PROGRESS"),
      WorkDay("2", "철근공 10명 모집", LocalDate.now().plusDays(1), "08:00", "18:00", "2025-08-02 ~ 2025-08-08", 8, 8, 10, "IN_PROGRESS"),
      // 예정
      WorkDay("3", "목공 20명 모집", LocalDate.now().plusDays(7), "08:00", "18:00", "2025-08-10 ~ 2025-08-14", 5, 0, 20, "UPCOMING"),
      WorkDay("4", "전기공 20명 모집", LocalDate.now().plusDays(8), "08:00", "18:00", "2025-08-11 ~ 2025-08-15", 3, 0, 20, "UPCOMING"),
      WorkDay("5", "미장공 15명 모집", LocalDate.now().plusDays(14), "09:00", "17:00", "2025-08-15 ~ 2025-08-20", 0, 0, 15, "UPCOMING"),
      // 마감
      WorkDay("6", "보통인부 15명 모집", LocalDate.now().minusDays(3), "08:00", "18:00", "2025-07-25 ~ 2025-08-01", 15, 15, 15, "COMPLETED"),
      WorkDay("7", "철근공 12명 모집", LocalDate.now().minusDays(7), "08:00", "18:00", "2025-07-20 ~ 2025-07-27", 12, 12, 12, "COMPLETED")
    )
  }

  var selectedTab by remember { mutableStateOf(0) }
  val bottomSheetState = rememberModalBottomSheetState()
  val scope = rememberCoroutineScope()
  var showBottomSheet by remember { mutableStateOf(false) }
  var selectedWorkDay by remember { mutableStateOf<WorkDay?>(null) }
  var showJobRegistrationBottomSheet by remember { mutableStateOf(false) }
  val jobRegistrationBottomSheetState = rememberModalBottomSheetState()
  
  // 지원자 데이터 (날짜별)
  val applicantsByDate = remember {
    CompanyMockDataFactory.getApplicantWorkersByDate().mapKeys { 
      LocalDate.parse(it.key) 
    }
  }
  
  // 날짜별 지원자 수 계산 함수
  fun getApplicantsCountForDate(date: LocalDate): Int {
    return (applicantsByDate[date] ?: emptyList()).size
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
      // 탭
      TabRow(
        selectedTabIndex = selectedTab,
        containerColor = Color.White
      ) {
        Tab(
          selected = selectedTab == 0,
          onClick = { selectedTab = 0 },
          text = { 
            Text("진행중 (${workDays.count { it.status == "IN_PROGRESS" }})")
          }
        )
        Tab(
          selected = selectedTab == 1,
          onClick = { selectedTab = 1 },
          text = { 
            Text("예정 (${workDays.count { it.status == "UPCOMING" }})")
          }
        )
        Tab(
          selected = selectedTab == 2,
          onClick = { selectedTab = 2 },
          text = { 
            Text("마감 (${workDays.count { it.status == "COMPLETED" }})")
          }
        )
      }

      // 작업 일자 목록
      LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        val filteredWorkDays = when (selectedTab) {
          0 -> workDays.filter { it.status == "IN_PROGRESS" }
          1 -> workDays.filter { it.status == "UPCOMING" }
          2 -> workDays.filter { it.status == "COMPLETED" }
          else -> workDays
        }

        // 날짜별로 그룹화
        val groupedByMonth = filteredWorkDays.groupBy { 
          it.date.format(DateTimeFormatter.ofPattern("yyyy년 MM월"))
        }

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
        
        Divider(modifier = Modifier.padding(horizontal = 20.dp))
        
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
        
        Divider(modifier = Modifier.padding(horizontal = 20.dp))
        
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
private fun WorkDayCard(
  workDay: WorkDay,
  wage: Int,
  applicantsCount: Int = 0,
  onMenuClick: () -> Unit,
  onWorkerManageClick: () -> Unit
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
              text = "수락 대기 중인 지원자 ${applicantsCount}명이 있습니다",
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