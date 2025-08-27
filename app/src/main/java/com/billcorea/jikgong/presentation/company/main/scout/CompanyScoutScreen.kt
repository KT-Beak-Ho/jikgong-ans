package com.billcorea.jikgong.presentation.company.main.scout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.company.main.common.CompanyBottomBar
import com.billcorea.jikgong.network.models.Worker
import com.billcorea.jikgong.network.models.Proposal
import com.billcorea.jikgong.network.models.ProposalStatus
import com.billcorea.jikgong.presentation.company.main.scout.pages.WorkerListPage
import com.billcorea.jikgong.presentation.company.main.scout.pages.ProposalListPage
import com.billcorea.jikgong.presentation.company.main.scout.pages.LocationSettingPage
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@Destination(route = "company_scout_main")
@Composable
fun CompanyScoutMainScreen(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    navController: NavController,
    viewModel: CompanyScoutViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier,
        topBar = {
            Column {
                // 토스 스타일 헤더
                TossStyleHeader(
                    currentLocation = uiState.currentLocation
                )

                // 탭 바
                ScoutTabBarExtended(
                    selectedTab = pagerState.currentPage,
                    onTabSelected = { index ->
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                )
            }
        },
        bottomBar = {
            CompanyBottomBar(
                navController = navController,
                currentRoute = "company_scout_main"
            )
        },
        containerColor = Color(0xFFF7F8FA) // 토스 배경색
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) { page ->
            when (page) {
                0 -> WorkerListPage(
                    workers = uiState.workers,
                    isLoading = uiState.isLoading,
                    onWorkerClick = { worker ->
                        viewModel.showWorkerDetail(worker)
                    },
                    onScoutClick = { worker ->
                        viewModel.sendScoutProposal(worker)
                    },
                    onRefresh = {
                        viewModel.refreshWorkers()
                    }
                )
                1 -> ProposalListPage(
                    proposals = uiState.proposals,
                    isLoading = uiState.isLoading,
                    onProposalClick = { proposal ->
                        viewModel.showProposalDetail(proposal)
                    },
                    onRefresh = {
                        viewModel.refreshProposals()
                    }
                )
                2 -> LocationSettingPage(
                    currentLocation = uiState.currentLocation,
                    searchRadius = uiState.searchRadius,
                    onLocationChange = { location ->
                        viewModel.updateLocation(location)
                    },
                    onRadiusChange = { radius ->
                        viewModel.updateSearchRadius(radius)
                    },
                    onCurrentLocationClick = {
                        viewModel.getCurrentLocation()
                    }
                )
            }
        }
    }

    // 노동자 상세 바텀시트
    uiState.selectedWorker?.let { selectedWorker ->
        WorkerDetailBottomSheetContent(
            worker = selectedWorker,
            onDismiss = { viewModel.dismissWorkerDetail() },
            onScoutConfirm = { wage, message ->
                viewModel.confirmScoutProposal(
                    worker = selectedWorker,
                    wage = wage,
                    message = message
                )
            }
        )
    }
}

@Composable
private fun TossStyleHeader(
    currentLocation: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 0.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            // 타이틀
            Text(
                text = "스카웃",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 현재 위치
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFF5F5F5))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "위치",
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFF4B7BFF)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = currentLocation,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
private fun ScoutTabBarExtended(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf("인력 목록", "제안 목록", "위치 설정")

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tabs.forEachIndexed { index, title ->
                TabItem(
                    title = title,
                    isSelected = selectedTab == index,
                    onClick = { onTabSelected(index) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun TabItem(
    modifier: Modifier = Modifier,
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected) Color(0xFF4B7BFF) else Color(0xFFF5F5F5)
    val textColor = if (isSelected) Color.White else Color.Gray

    Surface(
        modifier = modifier
            .height(36.dp)
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClick() },
        color = backgroundColor
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                ),
                color = textColor
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WorkerDetailBottomSheetContent(
    worker: Worker,
    onDismiss: () -> Unit,
    onScoutConfirm: (wage: String, message: String) -> Unit
) {
    var wage by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        contentColor = Color.Black
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // 노동자 정보 헤더
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = worker.name,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "${worker.distance}km · ${worker.jobTypes.joinToString(", ")}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }

                // 평점
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = Color(0xFFF0F0F0)
                ) {
                    Text(
                        text = "⭐ ${worker.rating}",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 경력 정보
            WorkerInfoSection(
                title = "경력",
                content = "${worker.experience}년"
            )

            WorkerInfoSection(
                title = "희망 일당",
                content = worker.desiredWage ?: "협의 가능"
            )

            WorkerInfoSection(
                title = "자기소개",
                content = worker.introduction ?: "등록된 소개가 없습니다."
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 임금 입력
            OutlinedTextField(
                value = wage,
                onValueChange = { wage = it },
                label = { Text("제안 일당") },
                placeholder = { Text("예: 150,000원") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4B7BFF),
                    unfocusedBorderColor = Color(0xFFE5E8EB)
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 메시지 입력
            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                label = { Text("스카웃 메시지") },
                placeholder = { Text("노동자에게 전달할 메시지를 입력하세요") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4B7BFF),
                    unfocusedBorderColor = Color(0xFFE5E8EB)
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 스카웃 제안 버튼
            Button(
                onClick = { onScoutConfirm(wage, message) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4B7BFF)
                ),
                enabled = wage.isNotEmpty() && message.isNotEmpty()
            ) {
                Text(
                    text = "스카웃 제안하기",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun WorkerInfoSection(
    title: String,
    content: String
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = content,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

// Preview Functions
@Preview(showBackground = true, name = "Scout Screen - Worker List")
@Composable
fun CompanyScoutScreenWorkerListPreview() {
    Jikgong1111Theme {
        var selectedWorker by remember { mutableStateOf<Worker?>(null) }

        CompanyScoutMainScreenPreview(
            selectedTab = 0,
            selectedWorker = selectedWorker,
            onWorkerSelect = { selectedWorker = it },
            onWorkerDismiss = { selectedWorker = null }
        )
    }
}

@Preview(showBackground = true, name = "Scout Screen - Empty Worker List")
@Composable
fun CompanyScoutScreenEmptyWorkerListPreview() {
    Jikgong1111Theme {
        CompanyScoutMainScreenPreview(
            selectedTab = 0,
            hasWorkers = false
        )
    }
}

@Preview(showBackground = true, name = "Scout Screen - Proposal List")
@Composable
fun CompanyScoutScreenProposalListPreview() {
    Jikgong1111Theme {
        CompanyScoutMainScreenPreview(selectedTab = 1)
    }
}

@Preview(showBackground = true, name = "Scout Screen - Empty Proposal List")
@Composable
fun CompanyScoutScreenEmptyProposalListPreview() {
    Jikgong1111Theme {
        CompanyScoutMainScreenPreview(
            selectedTab = 1,
            hasProposals = false
        )
    }
}

@Preview(showBackground = true, name = "Scout Screen - Location Settings")
@Composable
fun CompanyScoutScreenLocationPreview() {
    Jikgong1111Theme {
        CompanyScoutMainScreenPreview(selectedTab = 2)
    }
}

@Composable
private fun CompanyScoutMainScreenPreview(
    selectedTab: Int = 0,
    selectedWorker: Worker? = null,
    onWorkerSelect: (Worker) -> Unit = {},
    onWorkerDismiss: () -> Unit = {},
    hasWorkers: Boolean = true,
    hasProposals: Boolean = true
) {
    val pagerState = rememberPagerState(
        initialPage = selectedTab,
        pageCount = { 3 }
    )
    val coroutineScope = rememberCoroutineScope()

    // Mock data
    val mockWorkers = if (hasWorkers) {
        listOf(
            Worker(
                id = "1",
                name = "김철수",
                jobTypes = listOf("철근공", "형틀목공"),
                experience = 5,
                distance = 0.8,
                rating = 4.8f,
                introduction = "성실하고 꼼꼼한 작업을 약속드립니다.",
                desiredWage = "일당 18만원",
                isAvailable = true,
                completedProjects = 52
            ),
            Worker(
                id = "2",
                name = "이영희",
                jobTypes = listOf("타일공"),
                experience = 3,
                distance = 1.2,
                rating = 4.5f,
                introduction = "깔끔한 마감 처리가 장점입니다.",
                desiredWage = "일당 15만원",
                isAvailable = true,
                completedProjects = 28
            ),
            Worker(
                id = "3",
                name = "박민수",
                jobTypes = listOf("전기공", "배관공"),
                experience = 8,
                distance = 2.5,
                rating = 4.9f,
                introduction = "다년간의 경험으로 신속 정확한 작업 보장합니다.",
                desiredWage = "일당 20만원",
                isAvailable = false,
                completedProjects = 103
            ),
            Worker(
                id = "4",
                name = "정수진",
                jobTypes = listOf("도장공"),
                experience = 2,
                distance = 1.8,
                rating = 4.3f,
                introduction = "꼼꼼한 작업으로 만족도 높은 결과물을 제공합니다.",
                desiredWage = "협의 가능",
                isAvailable = true,
                completedProjects = 15
            ),
            Worker(
                id = "5",
                name = "최영호",
                jobTypes = listOf("조적공", "미장공"),
                experience = 10,
                distance = 3.2,
                rating = 4.7f,
                introduction = "20년 경력의 베테랑입니다.",
                desiredWage = "일당 22만원",
                isAvailable = true,
                completedProjects = 156
            )
        )
    } else {
        emptyList()
    }

    val mockProposals = if (hasProposals) {
        listOf(
            Proposal(
                id = "1",
                workerId = "worker1",
                workerName = "김철수",
                proposedWage = "일당 20만원",
                message = "프로젝트에 꼭 필요한 인력입니다.",
                status = ProposalStatus.PENDING,
                createdAt = LocalDateTime.now().minusHours(2),
                respondedAt = null,
                jobTypes = listOf("철근공"),
                distance = "2.5km",
                workerPhone = null,
                rejectReason = null
            ),
            Proposal(
                id = "2",
                workerId = "worker2",
                workerName = "이영희",
                proposedWage = "일당 18만원",
                message = "경력이 풍부하신 분을 찾고 있습니다.",
                status = ProposalStatus.ACCEPTED,
                createdAt = LocalDateTime.now().minusDays(1),
                respondedAt = LocalDateTime.now().minusHours(3),
                jobTypes = listOf("타일공"),
                distance = "1.2km",
                workerPhone = "010-1234-5678",
                rejectReason = null
            ),
            Proposal(
                id = "3",
                workerId = "worker3",
                workerName = "박민수",
                proposedWage = "일당 15만원",
                message = "함께 일하고 싶습니다.",
                status = ProposalStatus.REJECTED,
                createdAt = LocalDateTime.now().minusDays(2),
                respondedAt = LocalDateTime.now().minusDays(1),
                jobTypes = listOf("전기공"),
                distance = "3.5km",
                workerPhone = null,
                rejectReason = "일정이 맞지 않습니다"
            )
        )
    } else {
        emptyList()
    }

    Scaffold(
        topBar = {
            Column {
                TossStyleHeader(
                    currentLocation = "서울특별시 강남구"
                )
                ScoutTabBarExtended(
                    selectedTab = pagerState.currentPage,
                    onTabSelected = { index ->
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                )
            }
        },
        bottomBar = {
            // 실제 CompanyBottomBar 사용
            val mockNavController = rememberNavController()
            CompanyBottomBar(
                navController = mockNavController,
                currentRoute = "company_scout_main"
            )
        },
        containerColor = Color(0xFFF7F8FA)
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            userScrollEnabled = true
        ) { page ->
            when (page) {
                0 -> WorkerListPage(
                    workers = mockWorkers,
                    isLoading = false,
                    onWorkerClick = onWorkerSelect,
                    onScoutClick = onWorkerSelect,
                    onRefresh = {}
                )
                1 -> ProposalListPage(
                    proposals = mockProposals,
                    isLoading = false,
                    onProposalClick = {},
                    onRefresh = {}
                )
                2 -> LocationSettingPage(
                    currentLocation = "서울특별시 강남구",
                    searchRadius = 10,
                    onLocationChange = {},
                    onRadiusChange = {},
                    onCurrentLocationClick = {}
                )
            }
        }
    }

    // Worker Detail Bottom Sheet
    selectedWorker?.let { worker ->
        WorkerDetailBottomSheetContent(
            worker = worker,
            onDismiss = onWorkerDismiss,
            onScoutConfirm = { _, _ -> onWorkerDismiss() }
        )
    }
}