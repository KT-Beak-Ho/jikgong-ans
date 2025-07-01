package com.billcorea.jikgong.presentation.worker.projectList.page1

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.billcorea.jikgong.utils.MainViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp


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
    val isBookmarked: Boolean = false
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
            imageRes = null
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
            imageRes = null
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
            isBookmarked = true
        )
    )

    Scaffold (
        modifier = modifier
            .fillMaxSize()
            .padding(top = 20.dp)
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                val y = size.height - strokeWidth / 2
                drawLine(
                    color = appColorScheme.outlineVariant,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = strokeWidth
                )
            }
        ,
        bottomBar = {
            WorkerBottomNav(
                modifier = Modifier
                    .fillMaxWidth()
                    .height((screenHeight * .10).dp)
                    .padding(5.dp),
                doWorkerProjectList = {

                },
                doWorkerMyjob = {
                },
                doWorkerEarning = {

                },
                doWorkerProfile = {

                }
            )
        }
    ) { innerPadding ->
        Column(modifier = modifier
            .fillMaxSize()
            .padding(innerPadding)) {
            Spacer(modifier = Modifier.padding(5.dp))
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
                    containerColor = Color(0xFF2E3A59)
                )
            )

            // 필터 섹션
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "거리순",
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "정렬 옵션",
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "위치",
                            tint = Color(0xFF4285F4),
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "지도",
                            fontSize = 16.sp,
                            color = Color(0xFF4285F4)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 필터 버튼들
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        text = "직종",
                        hasDropdown = true
                    )
                    FilterChip(
                        text = "날짜",
                        hasDropdown = true
                    )
                    FilterChip(
                        text = "스크랩",
                        hasDropdown = false
                    )
                    FilterChip(
                        text = "식사제공",
                        hasDropdown = false
                    )
                    FilterChip(
                        text = "주차",
                        hasDropdown = false
                    )
                }
            }

            Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)

            // 채용 공고 리스트
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(jobPostings) { job ->
                    //JobPostingCard(job = job)
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

@Preview(showBackground = true)
@Composable
fun WorkerProjectListPreview() {
    val viewModel = MainViewModel()
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme {
        WorkerProjectList(viewModel, navigator, modifier = Modifier.padding(3.dp))
    }
}
