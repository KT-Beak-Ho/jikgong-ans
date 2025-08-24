package com.billcorea.jikgong.presentation.company.main.scout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.company.main.common.CompanyBottomBar
import com.billcorea.jikgong.presentation.company.main.scout.data.Worker
import com.billcorea.jikgong.presentation.company.main.scout.pages.WorkerListPage
import com.billcorea.jikgong.presentation.company.main.scout.pages.ProposalListPage
import com.billcorea.jikgong.presentation.company.main.scout.pages.LocationSettingPage
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

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
                TossStyleHeader(
                    currentLocation = uiState.currentLocation
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
            CompanyBottomBar(
                navController = navController,
                currentRoute = "company/scout"
            )
        },
        containerColor = Color(0xFFF7F8FA)
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

    if (uiState.selectedWorker != null) {
        WorkerDetailBottomSheet(
            worker = uiState.selectedWorker!!,
            onDismiss = { viewModel.dismissWorkerDetail() },
            onScoutConfirm = { wage, message ->
                viewModel.confirmScoutProposal(
                    worker = uiState.selectedWorker!!,
                    wage = wage,
                    message = message
                )
            }
        )
    }
}

@Composable
private fun TossStyleHeader(
    currentLocation: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "인력 스카웃",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "주변의 우수한 인력을 찾아보세요",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }

                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = Color(0xFFF0F0F0)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "위치",
                            modifier = Modifier.size(16.dp),
                            tint = Color(0xFF4B7BFF)
                        )
                        Text(
                            text = currentLocation,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Black
                        )
                    }
                }
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
        modifier = modifier.height(36.dp),
        onClick = onClick,
        shape = MaterialTheme.shapes.small,
        color = backgroundColor
    ) {
        Box(
            contentAlignment = Alignment.Center
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
private fun WorkerDetailBottomSheet(
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

            InfoSection(
                title = "경력",
                content = "${worker.experience}년"
            )

            InfoSection(
                title = "희망 일당",
                content = worker.desiredWage ?: "협의 가능"
            )

            InfoSection(
                title = "자기소개",
                content = worker.introduction ?: "등록된 소개가 없습니다."
            )

            Spacer(modifier = Modifier.height(24.dp))

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
private fun InfoSection(
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

@Preview(name = "스카웃 화면 - 인력 목록", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun CompanyScoutMainScreenWithDataPreview() {
    Jikgong1111Theme {
        val mockNavController = rememberNavController()
        val pagerState = rememberPagerState(initialPage = 0, pageCount = { 3 })

        // 샘플 데이터를 직접 생성
        val sampleWorkers = listOf(
            Worker(
                id = "1",
                name = "김철수",
                profileImageUrl = null,
                jobTypes = listOf("철근공", "형틀목공"),
                experience = 5,
                distance = 0.8,
                rating = 4.8f,
                introduction = "성실하고 꼼꼼한 작업을 약속드립니다. 5년간 다양한 현장에서 경험을 쌓았습니다.",
                desiredWage = "일당 18만원",
                isAvailable = true,
                lastActiveAt = LocalDateTime.now().minusHours(2),
                certifications = listOf("건설기능사", "안전교육이수"),
                completedProjects = 52,
                phoneNumber = null,
                workArea = "서울 강남구, 서초구"
            ),
            Worker(
                id = "2",
                name = "이영희",
                profileImageUrl = null,
                jobTypes = listOf("타일공"),
                experience = 3,
                distance = 1.2,
                rating = 4.5f,
                introduction = "깔끔한 마감 처리가 장점입니다. 욕실, 주방 타일 전문입니다.",
                desiredWage = "일당 15만원",
                isAvailable = true,
                lastActiveAt = LocalDateTime.now().minusMinutes(30),
                completedProjects = 28,
                workArea = "서울 서초구, 강남구"
            ),
            Worker(
                id = "3",
                name = "박민수",
                profileImageUrl = null,
                jobTypes = listOf("전기공", "배관공"),
                experience = 8,
                distance = 2.5,
                rating = 4.9f,
                introduction = "다년간의 경험으로 신속 정확한 작업 보장합니다.",
                desiredWage = "일당 20만원",
                isAvailable = false,
                lastActiveAt = LocalDateTime.now().minusDays(1),
                certifications = listOf("전기기능사", "배관기능사"),
                completedProjects = 103,
                workArea = "서울 송파구, 강동구"
            ),
            Worker(
                id = "4",
                name = "정수진",
                profileImageUrl = null,
                jobTypes = listOf("도장공"),
                experience = 2,
                distance = 1.8,
                rating = 4.3f,
                introduction = "꼼꼼한 작업으로 만족도 높은 결과물을 제공합니다.",
                desiredWage = "협의 가능",
                isAvailable = true,
                lastActiveAt = LocalDateTime.now().minusHours(5),
                completedProjects = 15,
                workArea = "서울 강동구"
            )
        )

        Scaffold(
            topBar = {
                Column {
                    TossStyleHeader(
                        currentLocation = "서울특별시 강남구"
                    )
                    ScoutTabBarExtended(
                        selectedTab = pagerState.currentPage,
                        onTabSelected = { }
                    )
                }
            },
            bottomBar = {
                CompanyBottomBar(
                    navController = mockNavController,
                    currentRoute = "company/scout"
                )
            },
            containerColor = Color(0xFFF7F8FA)
        ) { paddingValues ->
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) { page ->
                when (page) {
                    0 -> WorkerListPage(
                        workers = sampleWorkers,
                        isLoading = false,
                        onWorkerClick = { },
                        onScoutClick = { },
                        onRefresh = { }
                    )
                    1 -> ProposalListPage(
                        proposals = listOf(
                            Proposal(
                                id = "p1",
                                workerId = "1",
                                workerName = "김철수",
                                proposedWage = "일당 18만원",
                                message = "현재 진행 중인 강남구 프로젝트에 참여 부탁드립니다.",
                                status = ProposalStatus.ACCEPTED,
                                createdAt = LocalDateTime.now().minusDays(2),
                                respondedAt = LocalDateTime.now().minusDays(1),
                                jobTypes = listOf("철근공"),
                                distance = 0.8,
                                workerPhone = "010-1234-5678"
                            ),
                            Proposal(
                                id = "p2",
                                workerId = "2",
                                workerName = "이영희",
                                proposedWage = "일당 16만원",
                                message = "다음 주 월요일부터 시작하는 프로젝트입니다.",
                                status = ProposalStatus.PENDING,
                                createdAt = LocalDateTime.now().minusHours(5),
                                jobTypes = listOf("타일공"),
                                distance = 1.2
                            )
                        ),
                        isLoading = false,
                        onProposalClick = { },
                        onRefresh = { }
                    )
                    2 -> LocationSettingPage(
                        currentLocation = "서울특별시 강남구",
                        searchRadius = 10,
                        onLocationChange = { },
                        onRadiusChange = { },
                        onCurrentLocationClick = { }
                    )
                }
            }
        }
    }
}

@Preview(name = "스카웃 화면 - 제안 목록", showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun CompanyScoutMainScreenProposalPreview() {
    Jikgong1111Theme {
        val mockNavController = rememberNavController()
        val pagerState = rememberPagerState(initialPage = 1, pageCount = { 3 }) // 제안 목록 탭

        // 샘플 제안 데이터
        val sampleProposals = listOf(
            Proposal(
                id = "p1",
                workerId = "1",
                workerName = "김철수",
                proposedWage = "일당 18만원",
                message = "현재 진행 중인 강남구 프로젝트에 참여 부탁드립니다. 철근공 경력자를 찾고 있습니다.",
                status = ProposalStatus.ACCEPTED,
                createdAt = LocalDateTime.now().minusDays(2),
                respondedAt = LocalDateTime.now().minusDays(1),
                jobTypes = listOf("철근공"),
                distance = 0.8,
                workerPhone = "010-1234-5678"
            ),
            Proposal(
                id = "p2",
                workerId = "2",
                workerName = "이영희",
                proposedWage = "일당 16만원",
                message = "다음 주 월요일부터 시작하는 프로젝트입니다.",
                status = ProposalStatus.PENDING,
                createdAt = LocalDateTime.now().minusHours(5),
                jobTypes = listOf("타일공"),
                distance = 1.2
            ),
            Proposal(
                id = "p3",
                workerId = "3",
                workerName = "박민수",
                proposedWage = "일당 20만원",
                message = "긴급 프로젝트입니다. 전기 작업 경험이 풍부한 분을 모십니다.",
                status = ProposalStatus.REJECTED,
                createdAt = LocalDateTime.now().minusDays(3),
                respondedAt = LocalDateTime.now().minusDays(3),
                jobTypes = listOf("전기공"),
                distance = 2.5,
                rejectReason = "일정이 맞지 않습니다"
            ),
            Proposal(
                id = "p4",
                workerId = "4",
                workerName = "정수진",
                proposedWage = "일당 15만원",
                message = "아파트 도장 작업입니다.",
                status = ProposalStatus.PENDING,
                createdAt = LocalDateTime.now().minusHours(12),
                jobTypes = listOf("도장공"),
                distance = 1.8
            )
        )

        Scaffold(
            topBar = {
                Column {
                    TossStyleHeader(
                        currentLocation = "서울특별시 강남구"
                    )
                    ScoutTabBarExtended(
                        selectedTab = pagerState.currentPage,
                        onTabSelected = { }
                    )
                }
            },
            bottomBar = {
                CompanyBottomBar(
                    navController = mockNavController,
                    currentRoute = "company/scout"
                )
            },
            containerColor = Color(0xFFF7F8FA)
        ) { paddingValues ->
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) { page ->
                when (page) {
                    0 -> WorkerListPage(
                        workers = emptyList(), // 인력 목록은 비어있음
                        isLoading = false,
                        onWorkerClick = { },
                        onScoutClick = { },
                        onRefresh = { }
                    )
                    1 -> ProposalListPage(
                        proposals = sampleProposals, // 제안 목록 데이터
                        isLoading = false,
                        onProposalClick = { },
                        onRefresh = { }
                    )
                    2 -> LocationSettingPage(
                        currentLocation = "서울특별시 강남구",
                        searchRadius = 10,
                        onLocationChange = { },
                        onRadiusChange = { },
                        onCurrentLocationClick = { }
                    )
                }
            }
        }
    }
}