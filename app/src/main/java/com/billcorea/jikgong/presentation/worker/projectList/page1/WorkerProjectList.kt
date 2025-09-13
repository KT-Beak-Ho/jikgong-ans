package com.billcorea.jikgong.presentation.worker.projectList.page1

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.worker.common.WorkerBottomNav
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.billcorea.jikgong.utils.MainViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import org.koin.androidx.compose.koinViewModel


data class JobPosting(
    val id: Int,
    val title: String,
    val company: String,
    val location: String,
    val distance: String,
    val date: String,
    val time: String,
    val salary: String,
    val tags: List<String>,
    val applicants: Int,
    val imageRes: Int? = null,
    val isBookmarked: Boolean = false,
    val status: String = "RECRUITING",
    val isUrgent: Boolean = false,
    val category: String = "건설업"
)

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun WorkerProjectList(
    viewModel : MainViewModel = koinViewModel(),
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current
    val sp = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp
    val screenHeight = config.screenHeightDp
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var loginIdOrPhone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val _loginResult = viewModel.loginResult.observeAsState()
    val _loginError = viewModel.loginError.observeAsState()
    val loginResult = _loginResult.value
    val loginError = _loginError.value
    var selectedLocation by remember { mutableStateOf("부산 사하구") }

    /*
    val jobPostings = listOf(
        JobPosting(
            id = 1,
            title = "사하구 낙동5분락 낙동강 온도 측정 센터 신직급사",
            company = "(주)직공간집",
            location = "부산 사하구 하단동",
            distance = "3.2km",
            date = "1월 25일~27일",
            time = "06:30 ~ 15:00",
            salary = "100,000원",
            tags = listOf("식사제공", "무료주차", "퇴직정소 지원"),
            applicants = 10,
            imageRes = null,
            status = "RECRUITING",
            isUrgent = true,
            category = "건설업"
        ),
        JobPosting(
            id = 2,
            title = "프로젝트명 프로젝트명 프로젝트명",
            company = "회사명",
            location = "부산 사하구 하단동",
            distance = "3.2km",
            date = "1월 26일",
            time = "07:30 ~ 16:00",
            salary = "120,000원",
            tags = listOf("식사제공"),
            applicants = 2,
            imageRes = null,
            status = "RECRUITING",
            isUrgent = false,
            category = "토목업"
        ),
        JobPosting(
            id = 3,
            title = "프로젝트명 프로젝트명 프로젝트명",
            company = "회사명",
            location = "부산 사하구 하단동",
            distance = "3.2km",
            date = "1월 26일",
            time = "07:30 ~ 16:00",
            salary = "120,000원",
            tags = listOf("식사제공"),
            applicants = 2,
            imageRes = null,
            isBookmarked = true,
            status = "IN_PROGRESS",
            isUrgent = false,
            category = "건설업"
        )

    ) 

    val jobPostings = listOf<JobPosting>()

    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedLocation,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "위치 선택",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "검색",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "알림",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF4B7BFF)
                )
            )
        },
        bottomBar = {
            WorkerBottomNav(
                /* modifier = Modifier
                    .fillMaxWidth()
                    .height((screenHeight * .10).dp)
                    .padding(5.dp), */
                navigator = navigator,
                doWorkerProjectList = {},
                doWorkerMyjob = {},
                doWorkerEarning = {},
                doWorkerProfile = {}
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF8F9FA))
        ) {
            // 탭
            val recruitingCount = jobPostings.count { it.status == "RECRUITING" }
            val inProgressCount = jobPostings.count { it.status == "IN_PROGRESS" }
            val totalCount = jobPostings.size

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("전체 ($totalCount)") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("모집중 ($recruitingCount)") }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("진행중 ($inProgressCount)") }
                )
            }

            // 모집중 프로젝트 헤더
            if (selectedTab == 0 || selectedTab == 1) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = appColorScheme.primaryContainer.copy(alpha = 0.3f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "모집중 프로젝트",
                            style = AppTypography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = appColorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "총 ${recruitingCount}개",
                            style = AppTypography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = appColorScheme.primary
                        )
                    }
                }
            }

            // 채용 공고 리스트
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val filteredJobs = when (selectedTab) {
                    1 -> jobPostings.filter { it.status == "RECRUITING" }
                    2 -> jobPostings.filter { it.status == "IN_PROGRESS" }
                    else -> jobPostings
                }

                if (filteredJobs.isNotEmpty()) {
                    items(filteredJobs) { job ->
                        JobPostingCard(job = job)
                    }
                }

                item {
                    Text(
                        text = "00km 이내에 있는 일자리를 모두 찾았습니다.",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp)
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}

@Composable
private fun JobPostingCard(
    job: JobPosting
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { },
        shape = RoundedCornerShape(16.dp),
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
            // 상단 헤더
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    // 상태 뱃지들
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        if (job.isUrgent) {
                            Badge(
                                containerColor = Color(0xFFFF5252),
                                contentColor = Color.White
                            ) {
                                Text("긴급", fontSize = 12.sp)
                            }
                        }
                        Badge(
                            containerColor = when(job.status) {
                                "RECRUITING" -> Color(0xFF4B7BFF)
                                "IN_PROGRESS" -> Color(0xFF4CAF50)
                                else -> Color(0xFF9E9E9E)
                            },
                            contentColor = Color.White
                        ) {
                            Text(
                                when(job.status) {
                                    "RECRUITING" -> "모집중"
                                    "IN_PROGRESS" -> "진행중"
                                    else -> "완료"
                                },
                                fontSize = 12.sp
                            )
                        }
                        Badge(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        ) {
                            Text(job.category, fontSize = 12.sp)
                        }
                    }

                    Text(
                        text = job.title,
                        style = AppTypography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (job.isBookmarked) {
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = "북마크됨",
                        tint = Color(0xFF4B7BFF),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 정보 섹션
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                InfoRow(
                    icon = Icons.Outlined.LocationOn,
                    text = "${job.location} (${job.distance})"
                )
                InfoRow(
                    icon = Icons.Outlined.CalendarToday,
                    text = job.date
                )
                InfoRow(
                    icon = Icons.Outlined.Schedule,
                    text = job.time
                )
                InfoRow(
                    icon = Icons.Outlined.AttachMoney,
                    text = job.salary
                )
                InfoRow(
                    icon = Icons.Outlined.Business,
                    text = job.company
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 태그들
            if (job.tags.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    job.tags.take(3).forEach { tag ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFF0F0F0),
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = tag,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 12.sp,
                                color = Color(0xFF666666)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 하단 정보 및 버튼
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "지원자 ${job.applicants}명",
                    fontSize = 13.sp,
                    color = Color.Gray
                )
                
                Button(
                    onClick = { },
                    modifier = Modifier.height(36.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4B7BFF)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (job.status == "RECRUITING") "지원하기" else "상세보기",
                        style = AppTypography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = text,
            style = AppTypography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun FilterChip(
    text: String,
    hasDropdown: Boolean
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFFF5F5F5),
        modifier = Modifier.height(36.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                fontSize = 14.sp,
                color = Color.Black
            )
            if (hasDropdown) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "드롭다운",
                    modifier = Modifier.size(16.dp),
                    tint = Color.Gray
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "일자리 목록 - 데이터 있음")
@Composable
fun WorkerProjectListPreview() {
    val viewModel = MainViewModel()
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme {
        WorkerProjectList(viewModel, navigator, modifier = Modifier.padding(3.dp))
    }
}

@Preview(showBackground = true, name = "일자리 카드 - 긴급 모집중")
@Composable
fun JobPostingCardUrgentPreview() {
    Jikgong1111Theme {
        JobPostingCard(
            job = JobPosting(
                id = 1,
                title = "사하구 낙동5분락 낙동강 온도 측정 센터 신직급사",
                company = "(주)직공간집",
                location = "부산 사하구 하단동",
                distance = "3.2km",
                date = "1월 25일~27일",
                time = "06:30 ~ 15:00",
                salary = "100,000원",
                tags = listOf("식사제공", "무료주차", "퇴직정소 지원"),
                applicants = 10,
                status = "RECRUITING",
                isUrgent = true,
                category = "건설업"
            )
        )
    }
}

@Preview(showBackground = true, name = "일자리 카드 - 진행중 북마크")
@Composable
fun JobPostingCardInProgressPreview() {
    Jikgong1111Theme {
        JobPostingCard(
            job = JobPosting(
                id = 2,
                title = "신축 아파트 건설 프로젝트 - 철근 작업자 모집",
                company = "대한건설",
                location = "부산 해운대구 우동",
                distance = "5.1km",
                date = "1월 28일~2월 15일",
                time = "07:30 ~ 17:00",
                salary = "150,000원",
                tags = listOf("식사제공", "숙박제공"),
                applicants = 25,
                status = "IN_PROGRESS",
                isUrgent = false,
                category = "토목업",
                isBookmarked = true
            )
        )
    }
}

@Preview(showBackground = true, name = "일자리 카드 - 일반 모집")
@Composable
fun JobPostingCardNormalPreview() {
    Jikgong1111Theme {
        JobPostingCard(
            job = JobPosting(
                id = 3,
                title = "도로 포장 작업",
                company = "부산도로공사",
                location = "부산 남구 대연동",
                distance = "2.8km",
                date = "2월 1일",
                time = "08:00 ~ 16:00",
                salary = "120,000원",
                tags = listOf("교통비지원"),
                applicants = 5,
                status = "RECRUITING",
                isUrgent = false,
                category = "토목업"
            )
        )
    }
}
