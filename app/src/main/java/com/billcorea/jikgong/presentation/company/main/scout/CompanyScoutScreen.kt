package com.billcorea.jikgong.presentation.company.main.scout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.billcorea.jikgong.presentation.company.main.common.ScoutTopBar
import com.billcorea.jikgong.api.models.sampleDataFactory.CompanyMockDataFactory
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.Worker
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.Proposal
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.ProposalStatus
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
    
    // 새로고침 완료 알림을 위한 상태
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = modifier,
        topBar = {
            Column {
                // 통일된 상단바
                ScoutTopBar(
                    title = "스카웃",
                    onSettingsClick = {
                        // TODO: 설정 화면으로 이동
                    },
                    onRefreshClick = {
                        viewModel.refreshWorkers()
                    },
                    isRefreshing = uiState.isLoading
                )

                // 현재 위치 표시
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    shadowElevation = 0.dp
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "위치",
                            modifier = Modifier.size(16.dp),
                            tint = Color(0xFF4B7BFF)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = uiState.currentLocation,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Black
                        )
                    }
                }


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
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
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
                    workers = uiState.filteredWorkers,
                    isLoading = uiState.isLoading,
                    onWorkerClick = { worker ->
                        viewModel.showWorkerDetail(worker)
                    },
                    onScoutClick = { worker ->
                        viewModel.sendScoutProposal(worker)
                    },
                    onRefresh = {
                        viewModel.refreshWorkers()
                        // 새로고침이 완료된 후 알림 표시
                        coroutineScope.launch {
                            kotlinx.coroutines.delay(1200) // 새로고침 시뮬레이션
                            snackbarHostState.showSnackbar(
                                message = "인력 새로고침이 완료되었습니다.",
                                duration = SnackbarDuration.Short
                            )
                        }
                    },
                    onFilterClick = {
                        viewModel.toggleFilterDialog()
                    },
                    isFilterActive = uiState.isFilterActive
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
            onScoutConfirm = { wage, message, projectId, selectedDate ->
                viewModel.confirmScoutProposal(
                    worker = selectedWorker,
                    wage = wage,
                    message = message
                )
            }
        )
    }

    // 필터 다이얼로그
    if (uiState.showFilterDialog) {
        WorkerFilterDialog(
            currentFilters = uiState.currentFilters,
            onDismiss = { viewModel.toggleFilterDialog() },
            onApplyFilters = { filters ->
                viewModel.applyFilters(filters)
                viewModel.toggleFilterDialog()
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
    onScoutConfirm: (wage: String, message: String, projectId: String, selectedDate: String) -> Unit
) {
    var wage by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var selectedProjectId by remember { mutableStateOf("") }
    var selectedDates by remember { mutableStateOf(setOf<java.time.LocalDate>()) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    
    // 진행 중인 프로젝트 목록 가져오기 (RECRUITING 또는 IN_PROGRESS 상태)
    val activeProjects = remember {
        CompanyMockDataFactory.getProjectsByStatus("RECRUITING") + 
        CompanyMockDataFactory.getProjectsByStatus("IN_PROGRESS")
    }
    
    // 선택된 프로젝트의 날짜 범위 계산
    val projectDates = remember(selectedProjectId) {
        if (selectedProjectId.isNotEmpty()) {
            val project = activeProjects.find { it.id == selectedProjectId }
            project?.let { p ->
                val startDate = java.time.LocalDate.parse(p.startDate)
                val endDate = java.time.LocalDate.parse(p.endDate)
                generateSequence(startDate) { it.plusDays(1) }
                    .takeWhile { !it.isAfter(endDate) }
                    .toList()
            } ?: emptyList()
        } else emptyList()
    }

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

            Spacer(modifier = Modifier.height(12.dp))

            // 프로젝트 선택
            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = if (selectedProjectId.isEmpty()) "" else 
                        activeProjects.find { it.id == selectedProjectId }?.title ?: "",
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("스카웃할 현장 선택") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF4B7BFF),
                        unfocusedBorderColor = Color(0xFFE5E8EB)
                    )
                )
                
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    activeProjects.forEach { project ->
                        DropdownMenuItem(
                            text = { Text(project.title) },
                            onClick = {
                                selectedProjectId = project.id
                                selectedDates = emptySet() // 프로젝트 변경시 날짜 초기화
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 날짜 선택 (달력으로 여러 날짜 선택 가능)
            OutlinedTextField(
                value = if (selectedDates.isEmpty()) "" else 
                    "${selectedDates.size}개 날짜 선택됨 (${selectedDates.minOrNull()}${if (selectedDates.size > 1) " 외 ${selectedDates.size - 1}개" else ""})",
                onValueChange = { },
                readOnly = true,
                label = { Text("근무 날짜 선택") },
                trailingIcon = { 
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.DateRange, contentDescription = "달력")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF4B7BFF),
                    unfocusedBorderColor = Color(0xFFE5E8EB)
                )
            )
            
            // 달력 다이얼로그
            if (showDatePicker) {
                val selectedProject = activeProjects.find { it.id == selectedProjectId }
                DatePickerDialog(
                    onDateSelected = { dates ->
                        selectedDates = dates
                        showDatePicker = false
                    },
                    onDismiss = { showDatePicker = false },
                    projectStartDate = selectedProject?.startDate,
                    projectEndDate = selectedProject?.endDate,
                    initialSelectedDates = selectedDates
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 스카웃 제안 버튼
            Button(
                onClick = { 
                    showConfirmDialog = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4B7BFF)
                ),
                enabled = wage.isNotEmpty() && message.isNotEmpty() && 
                         selectedProjectId.isNotEmpty() && selectedDates.isNotEmpty()
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

    // 최종 확인 다이얼로그
    if (showConfirmDialog) {
        val selectedProject = activeProjects.find { it.id == selectedProjectId }
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = {
                Text(
                    text = "스카웃 제안 확인",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text("다음 내용으로 스카웃 제안을 보내시겠습니까?", 
                         style = MaterialTheme.typography.bodyMedium)
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text("• 프로젝트: ${selectedProject?.title ?: ""}",
                         style = MaterialTheme.typography.bodyMedium,
                         fontWeight = FontWeight.Medium)
                    
                    Text("• 날짜: ${selectedDates.joinToString(", ")}",
                         style = MaterialTheme.typography.bodyMedium,
                         fontWeight = FontWeight.Medium)
                    
                    Text("• 일당: $wage",
                         style = MaterialTheme.typography.bodyMedium,
                         fontWeight = FontWeight.Medium)
                         
                    Text("• 근무시간: 06:30~15:00",
                         style = MaterialTheme.typography.bodyMedium,
                         fontWeight = FontWeight.Medium)
                         
                    Text("• 장소: ${selectedProject?.location ?: ""}",
                         style = MaterialTheme.typography.bodyMedium,
                         fontWeight = FontWeight.Medium)
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Text("• 메시지: $message",
                         style = MaterialTheme.typography.bodyMedium,
                         fontWeight = FontWeight.Medium)
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text("⚠️ 지원 후에는 취소가 어렵습니다",
                         style = MaterialTheme.typography.bodySmall,
                         color = Color(0xFFFF5722))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showConfirmDialog = false }
                ) {
                    Text("취소", color = Color.Gray)
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showConfirmDialog = false
                        onScoutConfirm(wage, message, selectedProjectId, selectedDates.joinToString(","))
                    }
                ) {
                    Text("스카웃", color = Color(0xFF4B7BFF), fontWeight = FontWeight.Bold)
                }
            }
        )
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
                ScoutTopBar(
                    title = "스카웃",
                    onSettingsClick = {
                        // TODO: 설정 화면으로 이동
                    },
                    onRefreshClick = {},
                    isRefreshing = false
                )

                // 현재 위치 표시
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    shadowElevation = 0.dp
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(horizontal = 20.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "위치",
                            modifier = Modifier.size(16.dp),
                            tint = Color(0xFF4B7BFF)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "현재 위치",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Black
                        )
                    }
                }


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
            onScoutConfirm = { _, _, _, _ -> onWorkerDismiss() }
        )
    }
}

@Composable
private fun WorkerFilterDialog(
    currentFilters: WorkerFilters,
    onDismiss: () -> Unit,
    onApplyFilters: (WorkerFilters) -> Unit
) {
    var jobTypeFilter by remember { mutableStateOf(currentFilters.jobTypes) }
    var minExperience by remember { mutableStateOf(currentFilters.minExperience.toString()) }
    var selectedDistanceOption by remember { 
        mutableStateOf(
            when (currentFilters.maxDistance) {
                Double.MAX_VALUE -> 0 // 상관없음
                1.0 -> 1 // 1km이내
                10.0 -> 2 // 10km이내
                else -> 0
            }
        ) 
    }
    var minRating by remember { mutableStateOf(currentFilters.minRating.toString()) }
    var availableOnly by remember { mutableStateOf(currentFilters.availableOnly) }
    
    val distanceOptions = listOf("상관없음", "1km이내", "10km이내")
    
    val allJobTypes = listOf("철근공", "형틀목공", "타일공", "전기공", "배관공", "도장공", "조적공", "미장공", "일반", "기타")
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "인력 필터",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 직종 필터
                item {
                    Text(
                        text = "직종 선택",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    allJobTypes.chunked(3).forEach { rowJobTypes ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowJobTypes.forEach { jobType ->
                                val isSelected = jobTypeFilter.contains(jobType)
                                FilterChip(
                                    onClick = {
                                        jobTypeFilter = if (isSelected) {
                                            jobTypeFilter - jobType
                                        } else {
                                            jobTypeFilter + jobType
                                        }
                                    },
                                    label = { Text(jobType) },
                                    selected = isSelected,
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Color(0xFF4B7BFF),
                                        selectedLabelColor = Color.White
                                    ),
                                    modifier = Modifier.weight(1f, false)
                                )
                            }
                            // 남은 공간 채우기
                            repeat(3 - rowJobTypes.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
                
                // 경력 필터
                item {
                    OutlinedTextField(
                        value = minExperience,
                        onValueChange = { minExperience = it },
                        label = { Text("최소 경력 (년)") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4B7BFF)
                        )
                    )
                }
                
                // 거리 필터
                item {
                    Text(
                        text = "거리 설정",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    distanceOptions.forEachIndexed { index, option ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedDistanceOption == index,
                                onClick = { selectedDistanceOption = index },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Color(0xFF4B7BFF)
                                )
                            )
                            Text(
                                text = option,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
                
                // 평점 필터
                item {
                    OutlinedTextField(
                        value = minRating,
                        onValueChange = { minRating = it },
                        label = { Text("최소 평점") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4B7BFF)
                        )
                    )
                }
                
                // 이용 가능한 인력만
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "이용 가능한 인력만",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Switch(
                            checked = availableOnly,
                            onCheckedChange = { availableOnly = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color(0xFF4B7BFF)
                            )
                        )
                    }
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소", color = Color.Gray)
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val maxDistance = when (selectedDistanceOption) {
                        0 -> Double.MAX_VALUE // 상관없음
                        1 -> 1.0 // 1km이내
                        2 -> 10.0 // 10km이내
                        else -> Double.MAX_VALUE
                    }
                    
                    val filters = WorkerFilters(
                        jobTypes = jobTypeFilter,
                        minExperience = minExperience.toIntOrNull() ?: 0,
                        maxDistance = maxDistance,
                        minRating = minRating.toFloatOrNull() ?: 0f,
                        availableOnly = availableOnly
                    )
                    onApplyFilters(filters)
                }
            ) {
                Text(
                    "적용",
                    color = Color(0xFF4B7BFF),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}

@Composable
private fun DatePickerDialog(
    onDateSelected: (Set<java.time.LocalDate>) -> Unit,
    onDismiss: () -> Unit,
    projectStartDate: String?,
    projectEndDate: String?,
    initialSelectedDates: Set<java.time.LocalDate>
) {
    var selectedDates by remember { mutableStateOf(initialSelectedDates) }
    
    // 프로젝트 날짜 범위 설정 (기본값: 오늘부터 30일)
    val startDate = projectStartDate?.let { java.time.LocalDate.parse(it) } ?: java.time.LocalDate.now()
    val endDate = projectEndDate?.let { java.time.LocalDate.parse(it) } ?: java.time.LocalDate.now().plusDays(30)
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "근무 날짜 선택",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text(
                    text = "근무할 날짜를 선택하세요 (여러 날짜 선택 가능)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // 간단한 날짜 선택기 (실제 구현에서는 더 정교한 달력 컴포넌트 사용)
                LazyColumn(
                    modifier = Modifier.heightIn(max = 300.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val dateRange = generateSequence(startDate) { it.plusDays(1) }
                        .takeWhile { it <= endDate }
                        .toList()
                        
                    items(dateRange) { date ->
                        val isSelected = selectedDates.contains(date)
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedDates = if (isSelected) {
                                        selectedDates - date
                                    } else {
                                        selectedDates + date
                                    }
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) {
                                    Color(0xFF4B7BFF).copy(alpha = 0.1f)
                                } else {
                                    Color.White
                                }
                            ),
                            border = if (isSelected) {
                                androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF4B7BFF))
                            } else {
                                androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray)
                            }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${date.monthValue}월 ${date.dayOfMonth}일 (${
                                        when (date.dayOfWeek.value) {
                                            1 -> "월"
                                            2 -> "화"
                                            3 -> "수"
                                            4 -> "목"
                                            5 -> "금"
                                            6 -> "토"
                                            7 -> "일"
                                            else -> ""
                                        }
                                    })",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (isSelected) Color(0xFF4B7BFF) else Color.Black,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                                
                                if (isSelected) {
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color(0xFF4B7BFF),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
                
                if (selectedDates.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "${selectedDates.size}개 날짜 선택됨",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF4B7BFF),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소", color = Color.Gray)
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDateSelected(selectedDates)
                },
                enabled = selectedDates.isNotEmpty()
            ) {
                Text(
                    "확인", 
                    color = if (selectedDates.isNotEmpty()) Color(0xFF4B7BFF) else Color.Gray,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}