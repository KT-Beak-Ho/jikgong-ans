package com.billcorea.jikgong.presentation.company.main.scout.presentation.screen

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
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.ui.text.style.TextAlign
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
import com.billcorea.jikgong.presentation.company.main.scout.presentation.viewmodel.CompanyScoutViewModel
import com.billcorea.jikgong.presentation.company.main.scout.feature.pages.WorkerListPage
import com.billcorea.jikgong.presentation.company.main.scout.feature.pages.ProposalListPage
import com.billcorea.jikgong.presentation.company.main.scout.feature.pages.LocationSettingPage
import com.billcorea.jikgong.presentation.company.main.scout.presentation.component.WorkerDetailBottomSheet
import com.billcorea.jikgong.presentation.company.main.scout.presentation.viewmodel.WorkerFilters
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Destination(route = "company_scout_main")
@Composable
fun CompanyScoutMainScreen(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    navController: NavController,
    viewModel: CompanyScoutViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(pageCount = { 3 }) // 3개 탭: 인력 목록, 제안 목록, 위치 설정
    val coroutineScope = rememberCoroutineScope()
    
    // 새로고침 완료 알림을 위한 상태
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = modifier,
        topBar = {
            Column {
                // 통일된 상단바
                ScoutTopBar(
                    title = "스카웃"
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
                    isFilterActive = uiState.isFilterActive,
                    onAIFilterClick = {
                        viewModel.toggleAIFilterDialog()
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
        WorkerDetailBottomSheet(
            worker = selectedWorker,
            onDismiss = { viewModel.dismissWorkerDetail() },
            onScoutClick = { wage ->
                viewModel.confirmScoutProposal(
                    worker = selectedWorker,
                    wage = wage,
                    message = "안녕하세요! 저희 현장에서 함께 일하실 의향이 있으신지 문의드립니다."
                )
                
                // 성공 메시지 표시
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(
                        message = "스카웃 제안을 보냈습니다!",
                        duration = SnackbarDuration.Short
                    )
                }
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
    
    // AI 필터 다이얼로그
    if (uiState.showAIFilterDialog) {
        AIFilterDialog(
            onDismiss = { viewModel.toggleAIFilterDialog() },
            onApplyFilter = {
                viewModel.applyAIFiltering()
                viewModel.toggleAIFilterDialog()
            }
        )
    }
    
    // AI 필터링 진행중 다이얼로그
    if (uiState.isAIFiltering) {
        AlertDialog(
            onDismissRequest = { },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color(0xFF4B7BFF)
                    )
                    Text(
                        text = "AI 필터링이 진행중입니다",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Text(
                    text = "평점과 경험을 바탕으로 최적의 인력을 찾고 있습니다...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            },
            confirmButton = { },
            dismissButton = { }
        )
    }
    
    // Proposal 상세 보기 BottomSheet
    if (uiState.showProposalDetailSheet && uiState.selectedProposal != null) {
        val proposal = uiState.selectedProposal!!
        ModalBottomSheet(
            onDismissRequest = { viewModel.dismissProposalDetail() },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = Color.White
        ) {
            ProposalDetailBottomSheetContent(
                proposal = proposal,
                onDismiss = { viewModel.dismissProposalDetail() }
            )
        }
    }
}

@Composable
private fun ProposalDetailBottomSheetContent(
    proposal: Proposal,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .navigationBarsPadding()
    ) {
        // 헤더
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "제안 상세",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onDismiss) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "닫기"
                )
            }
        }
        
        Spacer(modifier = Modifier.height(20.dp))
        
        // 인력 정보
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = proposal.workerName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    proposal.jobTypes.forEach { jobType ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF4B7BFF).copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = jobType,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelMedium,
                                color = Color(0xFF4B7BFF)
                            )
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 제안 정보
        InfoRow(label = "제안 일시", value = proposal.toDisplayInfo())
        InfoRow(label = "제안 금액", value = proposal.proposedWage)
        InfoRow(label = "거리", value = proposal.distance)
        InfoRow(label = "상태", value = proposal.status.name)
        
        if (proposal.message.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "메시지",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = proposal.message,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
        
        if (proposal.status == ProposalStatus.ACCEPTED && proposal.workerPhone != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* 전화 걸기 */ },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4B7BFF))
            ) {
                Icon(imageVector = Icons.Default.Phone, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "전화 걸기: ${proposal.workerPhone}")
            }
        }
        
        if (proposal.status == ProposalStatus.REJECTED && proposal.rejectReason != null) {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEE2E2))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = null,
                        tint = Color(0xFFDC2626)
                    )
                    Text(
                        text = "거절 사유: ${proposal.rejectReason}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFFDC2626)
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
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
    var isQuickScout by remember { mutableStateOf(false) }
    
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

            // 상세 정보 섹션
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFFF8F9FA),
                        RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "상세 정보",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    WorkerInfoSection(
                        title = "경력",
                        content = "${worker.experience}년",
                        modifier = Modifier.weight(1f)
                    )
                    WorkerInfoSection(
                        title = "완료 프로젝트",
                        content = "${worker.completedProjects}건",
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    WorkerInfoSection(
                        title = "키",
                        content = "175cm", // Mock data
                        modifier = Modifier.weight(1f)
                    )
                    WorkerInfoSection(
                        title = "체형",
                        content = "보통", // Mock data
                        modifier = Modifier.weight(1f)
                    )
                }
                
                WorkerInfoSection(
                    title = "희망 일당",
                    content = worker.desiredWage ?: "협의 가능"
                )
                
                WorkerInfoSection(
                    title = "가능한 근무일",
                    content = "월, 화, 수, 금 (주 4일)" // 더 구체적인 일정 정보
                )
                
                WorkerInfoSection(
                    title = "근무 가능 시간",
                    content = "06:00 ~ 18:00"
                )
                
                WorkerInfoSection(
                    title = "자기소개",
                    content = worker.introduction ?: "등록된 소개가 없습니다."
                )
                
                // 보유 기술/자격증
                if (worker.certificates.isNotEmpty()) {
                    WorkerInfoSection(
                        title = "보유 자격증",
                        content = worker.certificates.joinToString(", ")
                    )
                }
                
                // 추가 정보들
                WorkerInfoSection(
                    title = "작업 선호도",
                    content = "실내 작업 선호, 팀워크 중시"
                )
                
                WorkerInfoSection(
                    title = "특기사항",
                    content = "안전교육 이수 완료, 응급처치 자격증 보유"
                )
                
                WorkerInfoSection(
                    title = "최근 프로젝트",
                    content = "○○아파트 신축공사 (2024.11 ~ 2024.12)"
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            // 간단한 스카웃 제안 섹션
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF8F9FA)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "빠른 스카웃 제안",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // 간단한 임금 입력
                    var quickWage by remember { mutableStateOf("") }
                    OutlinedTextField(
                        value = quickWage,
                        onValueChange = { quickWage = it },
                        label = { Text("제안 일당") },
                        placeholder = { Text("예: 150,000원") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4B7BFF),
                            unfocusedBorderColor = Color(0xFFE5E8EB)
                        )
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // 빠른 스카웃 버튼
                    Button(
                        onClick = {
                            if (quickWage.isNotEmpty()) {
                                // 빠른 스카웃을 위한 기본값 설정
                                wage = quickWage
                                message = "안녕하세요! 저희 현장에서 함께 일하실 의향이 있으신지 문의드립니다."
                                
                                // 첫 번째 활성 프로젝트를 자동 선택
                                if (activeProjects.isNotEmpty()) {
                                    selectedProjectId = activeProjects.first().id
                                    // 오늘부터 일주일을 기본 선택
                                    val today = java.time.LocalDate.now()
                                    selectedDates = setOf(today, today.plusDays(1), today.plusDays(2))
                                }
                                
                                isQuickScout = true
                                showConfirmDialog = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4B7BFF)
                        ),
                        enabled = quickWage.isNotEmpty() && worker.isAvailable
                    ) {
                        Text(
                            text = if (!worker.isAvailable) "진행중" else "바로 스카웃 제안하기",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    
                    // 상태에 따른 안내 메시지
                    if (!worker.isAvailable) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "현재 다른 현장에서 작업 중입니다.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // 상세 스카웃 버튼
                    OutlinedButton(
                        onClick = {
                            // 기존의 상세 스카웃 섹션으로 스크롤
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF4B7BFF)
                        )
                    ) {
                        Text(
                            text = "상세 스카웃 제안하기",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            Divider(color = Color(0xFFE5E8EB), thickness = 1.dp)

            Spacer(modifier = Modifier.height(24.dp))

            // 상세 스카웃 제안 섹션
            Text(
                text = "상세 스카웃 제안",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )
            
            Spacer(modifier = Modifier.height(16.dp))

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

            // 기존 스카웃 제안 확인
            val existingProposal: Proposal? = null // Mock data - no existing proposal
            
            // 스카웃 제안/취소 버튼
            if (existingProposal != null) {
                // 기존 제안이 있는 경우
                when (existingProposal.status) {
                    ProposalStatus.PENDING -> {
                        // 대기 중인 제안 - 취소 가능
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
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
                                        Icons.Default.HourglassEmpty,
                                        contentDescription = null,
                                        tint = Color(0xFFFF8C00),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "이미 스카웃 제안을 보낸 상태입니다",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF856404),
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                            
                            OutlinedButton(
                                onClick = { 
                                    // 스카웃 취소 확인 다이얼로그 표시
                                    showConfirmDialog = true
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color(0xFFE53E3E)
                                )
                            ) {
                                Text(
                                    text = "스카웃 제안 취소",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }
                    ProposalStatus.ACCEPTED -> {
                        // 수락된 제안 - 취소 불가
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFD4EDDA)
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color(0xFF155724),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "스카웃 제안이 수락되었습니다",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color(0xFF155724),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    ProposalStatus.REJECTED -> {
                        // 거절된 제안 - 다시 제안 가능
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFF8D7DA)
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.Cancel,
                                        contentDescription = null,
                                        tint = Color(0xFF721C24),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "이전 스카웃 제안이 거절되었습니다",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color(0xFF721C24),
                                            fontWeight = FontWeight.Medium
                                        )
                                        if (!existingProposal.rejectReason.isNullOrBlank()) {
                                            Text(
                                                text = "사유: ${existingProposal.rejectReason}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color(0xFF721C24)
                                            )
                                        }
                                    }
                                }
                            }
                            
                            Button(
                                onClick = { 
                                    isQuickScout = false
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
                                    text = "다시 스카웃 제안하기",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }
                }
            } else {
                // 새로운 스카웃 제안
                Button(
                    onClick = { 
                        isQuickScout = false
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
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    // 확인 다이얼로그 (스카웃 제안 또는 취소)
    if (showConfirmDialog) {
        val selectedProject = activeProjects.find { it.id == selectedProjectId }
        val isCancelAction = false // Mock data - always new proposal
        
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = {
                Text(
                    text = if (isCancelAction) "스카웃 제안 취소" else "스카웃 제안 확인",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    if (isCancelAction) {
                        Text("스카웃 제안을 취소하시겠습니까?", 
                             style = MaterialTheme.typography.bodyMedium)
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text("⚠️ 취소된 제안은 복구할 수 없습니다",
                             style = MaterialTheme.typography.bodySmall,
                             color = Color(0xFFFF5722))
                    } else {
                        if (isQuickScout) {
                            Text("빠른 스카웃 제안을 보내시겠습니까?", 
                                 style = MaterialTheme.typography.bodyMedium)
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Text("• 일당: $wage",
                                 style = MaterialTheme.typography.bodyMedium,
                                 fontWeight = FontWeight.Medium)
                            
                            Text("• 메시지: $message",
                                 style = MaterialTheme.typography.bodyMedium,
                                 fontWeight = FontWeight.Medium)
                                 
                            if (activeProjects.isNotEmpty()) {
                                Text("• 프로젝트: ${activeProjects.first().title}",
                                     style = MaterialTheme.typography.bodyMedium,
                                     fontWeight = FontWeight.Medium)
                            }
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            Text("💡 상세 정보는 나중에 조정할 수 있습니다.",
                                 style = MaterialTheme.typography.bodySmall,
                                 color = Color(0xFF4B7BFF))
                        } else {
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
                    }
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
                        if (isCancelAction) {
                            // 스카웃 제안 취소
                            // viewModel.cancelScoutProposal(existingProposal.id)
                            onDismiss()
                        } else {
                            // 새로운 스카웃 제안
                            onScoutConfirm(wage, message, selectedProjectId, selectedDates.joinToString(","))
                        }
                    }
                ) {
                    Text(
                        if (isCancelAction) "제안 취소" else "스카웃", 
                        color = if (isCancelAction) Color(0xFFE53E3E) else Color(0xFF4B7BFF), 
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        )
    }
}

@Composable
private fun WorkerInfoSection(
    title: String,
    content: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(vertical = 4.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF333333)
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
                    title = "스카웃"
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
                    onRefresh = {},
                    onAIFilterClick = {}
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
        WorkerDetailBottomSheet(
            worker = worker,
            onDismiss = onWorkerDismiss,
            onScoutClick = { _ -> onWorkerDismiss() }
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

@Composable
private fun AIFilterDialog(
    onDismiss: () -> Unit,
    onApplyFilter: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = Color(0xFF4B7BFF),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "AI 매칭",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "AI가 평점이 높은 순서로 최적의 인력을 추천해드립니다.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF4B7BFF).copy(alpha = 0.1f)
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF4B7BFF).copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = null,
                            tint = Color(0xFF4B7BFF),
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "평점, 경험, 완료 프로젝트 수를 종합적으로 분석하여 우수한 인력을 우선 표시합니다.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF4B7BFF),
                            lineHeight = 18.sp
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
                onClick = onApplyFilter
            ) {
                Text(
                    "AI 매칭 시작",
                    color = Color(0xFF4B7BFF),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}