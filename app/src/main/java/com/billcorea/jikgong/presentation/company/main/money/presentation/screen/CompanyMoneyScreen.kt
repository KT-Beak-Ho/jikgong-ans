package com.billcorea.jikgong.presentation.company.main.money.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.company.main.money.presentation.component.ProjectPaymentCard
import com.billcorea.jikgong.presentation.company.main.money.presentation.component.ProjectPaymentFilterBar
import com.billcorea.jikgong.presentation.company.main.money.presentation.component.ProjectPaymentSummaryCard
import com.billcorea.jikgong.presentation.company.main.money.presentation.component.EmptyMoneyState
import com.billcorea.jikgong.presentation.company.main.money.presentation.component.ScrollBar
import com.billcorea.jikgong.presentation.company.main.money.feature.dialog.screen.PendingPaymentsDialog
import com.billcorea.jikgong.presentation.company.main.money.feature.dialog.screen.CompletedProjectDetailDialog
import com.billcorea.jikgong.presentation.company.main.money.feature.dialog.screen.PaymentConfirmationDialog
import com.billcorea.jikgong.presentation.company.main.money.feature.dialog.screen.ProjectListDialog
import com.billcorea.jikgong.presentation.company.main.money.feature.dialog.screen.CompletedAmountDialog
import com.billcorea.jikgong.presentation.company.main.money.feature.dialog.screen.BulkPaymentConfirmationDialog
import com.billcorea.jikgong.presentation.company.main.money.feature.dialog.screen.ProjectWorkerListDialog
import com.billcorea.jikgong.presentation.company.main.money.feature.dialog.screen.CompletedPaymentDetailDialog
import com.billcorea.jikgong.api.models.sampleDataFactory.CompanyMockDataFactory
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.ProjectPaymentStatus
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import com.ramcosta.composedestinations.annotation.Destination
import androidx.navigation.NavController
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.ProjectPaymentData
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.ProjectPaymentSummary
import com.billcorea.jikgong.presentation.company.main.common.CompanyBottomBar
import com.billcorea.jikgong.presentation.company.main.common.SearchableTopBar
import java.text.NumberFormat
import java.util.*

@Destination
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyMoneyScreen(
    navigator: DestinationsNavigator,
    navController: NavController,
    modifier: Modifier = Modifier,
    showBottomBar: Boolean = true
) {
    // 샘플 데이터 (빈 상태 테스트를 위해 변경 가능)
    val hasData = true // false로 변경하면 빈 상태
    
    // 원본 데이터
    val allProjectPayments = remember {
        if (hasData) {
            val originalData = CompanyMockDataFactory.getProjectPayments()
            val copyData1 = originalData.map { project ->
                project.copy(
                    id = "${project.id}_copy_1",
                    projectTitle = "${project.projectTitle} (복사본1)"
                )
            }
            val copyData2 = originalData.map { project ->
                project.copy(
                    id = "${project.id}_copy_2",
                    projectTitle = "${project.projectTitle} (복사본2)"
                )
            }
            originalData + copyData1 + copyData2
        } else {
            emptyList()
        }
    }
    
    // 필터링된 데이터
    var projectPayments by remember { mutableStateOf(allProjectPayments) }
    
    // 검색 상태
    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
    
    // 검색 다이얼로그
    if (isSearching) {
        AlertDialog(
            onDismissRequest = { 
                isSearching = false
                searchQuery = ""
                projectPayments = allProjectPayments
            },
            title = {
                Text(
                    text = "프로젝트/인력 검색",
                    style = AppTypography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            },
            text = {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { query ->
                        searchQuery = query
                        // 실시간 검색 필터링
                        projectPayments = if (query.isEmpty()) {
                            allProjectPayments
                        } else {
                            allProjectPayments.filter { project ->
                                project.projectTitle.contains(query, ignoreCase = true) ||
                                project.workers.any { worker ->
                                    worker.workerName.contains(query, ignoreCase = true)
                                }
                            }
                        }
                    },
                    placeholder = { Text("프로젝트명 또는 인력명 입력") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { 
                        isSearching = false
                    }
                ) {
                    Text("확인")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { 
                        isSearching = false
                        searchQuery = ""
                        projectPayments = allProjectPayments
                    }
                ) {
                    Text("취소")
                }
            }
        )
    }

    // 실제 프로젝트 데이터를 기반으로 동적 요약 데이터 생성
    val summary = remember(projectPayments) {
        if (hasData) {
            val pendingProjects = projectPayments.filter { it.status == ProjectPaymentStatus.PENDING }
            val completedProjects = projectPayments.filter { it.status == ProjectPaymentStatus.COMPLETED }
            val totalAmount = projectPayments.sumOf { it.totalAmount }
            val paidAmount = completedProjects.sumOf { it.paidAmount }
            val pendingAmount = pendingProjects.sumOf { it.totalAmount }
            
            // 기본 템플릿을 가져와서 실제 데이터로 업데이트
            val template = CompanyMockDataFactory.getProjectPaymentSummary()
            template.copy(
                totalProjects = projectPayments.size,
                pendingPayments = pendingProjects.size,
                completedPayments = completedProjects.size,
                totalAmount = totalAmount,
                pendingAmount = pendingAmount,
                paidAmount = paidAmount
            )
        } else {
            CompanyMockDataFactory.getEmptyProjectPaymentSummary()
        }
    }

    var selectedStatus by remember { mutableStateOf<ProjectPaymentStatus?>(null) }
    var showPendingPaymentsDialog by remember { mutableStateOf(false) }
    var showCompletedPaymentsDialog by remember { mutableStateOf(false) }
    var showAllProjectsDialog by remember { mutableStateOf(false) }
    var showCompletedAmountDialog by remember { mutableStateOf(false) }
    var showPaymentConfirmDialog by remember { mutableStateOf(false) }
    var selectedProjectForPayment by remember { mutableStateOf<ProjectPaymentData?>(null) }
    var showCompletedProjectDetailDialog by remember { mutableStateOf(false) }
    var selectedCompletedProject by remember { mutableStateOf<ProjectPaymentData?>(null) }
    var showBulkPaymentConfirmDialog by remember { mutableStateOf(false) }
    var showProjectWorkerListDialog by remember { mutableStateOf(false) }
    var selectedProjectForWorkerList by remember { mutableStateOf<ProjectPaymentData?>(null) }
    var showCompletedPaymentDetailDialog by remember { mutableStateOf(false) }
    var selectedProjectForCompletedDetail by remember { mutableStateOf<ProjectPaymentData?>(null) }

    // 프로젝트 상태를 지급 완료로 변경하는 함수
    fun markProjectAsCompleted(projectId: String) {
        projectPayments = projectPayments.map { project ->
            if (project.id == projectId) {
                val currentTime = java.time.LocalDateTime.now()
                project.copy(
                    status = ProjectPaymentStatus.COMPLETED,
                    paidAmount = project.totalAmount,
                    completedAt = currentTime,
                    workers = project.workers.map { worker ->
                        worker.copy(paidAt = currentTime)
                    }
                )
            } else {
                project
            }
        }
    }

    // 여러 프로젝트를 일괄 지급 완료로 변경하는 함수
    fun markProjectsAsCompleted(projectIds: List<String>) {
        val currentTime = java.time.LocalDateTime.now()
        projectPayments = projectPayments.map { project ->
            if (project.id in projectIds) {
                project.copy(
                    status = ProjectPaymentStatus.COMPLETED,
                    paidAmount = project.totalAmount,
                    completedAt = currentTime,
                    workers = project.workers.map { worker ->
                        worker.copy(paidAt = currentTime)
                    }
                )
            } else {
                project
            }
        }
    }

    // 필터링 및 정렬된 프로젝트 목록
    val filteredProjects = remember(selectedStatus) {
        val filtered = if (selectedStatus == null) {
            projectPayments
        } else {
            projectPayments.filter { it.status == selectedStatus }
        }
        
        // 우선순위에 따른 정렬: 연체 > 지급 대기 > 처리중 > 지급 실패 > 지급 완료
        filtered.sortedWith(compareBy<ProjectPaymentData> { project ->
            when (project.status) {
                ProjectPaymentStatus.OVERDUE -> 0     // 연체 (최우선 - 신뢰도 영향)
                ProjectPaymentStatus.PENDING -> 1     // 지급 대기
                ProjectPaymentStatus.PROCESSING -> 2  // 처리중
                ProjectPaymentStatus.FAILED -> 3      // 지급 실패
                ProjectPaymentStatus.COMPLETED -> 4   // 지급 완료 (마지막)
            }
        }.thenByDescending { it.createdAt }) // 같은 상태 내에서는 최신 순으로 정렬
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = androidx.compose.ui.graphics.Color(0xFFF7F8FA), // 파란색 배경으로 변경
        topBar = {
            SearchableTopBar(
                title = "임금 관리",
                onSearchClick = {
                    // 검색 기능 - 검색 다이얼로그 표시
                    isSearching = !isSearching
                }
            )
        },
        bottomBar = {
            if (showBottomBar) {
                CompanyBottomBar(
                    navController = navController,
                    currentRoute = "company_money_screen"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 메인 컨텐츠
            if (projectPayments.isEmpty()) {
                // 빈 상태
                EmptyMoneyState(
                    onCreateProjectClick = {
                        // 프로젝트 생성 화면으로 이동
                        navController.navigate("company_project_list")
                    },
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                // 데이터가 있는 상태
                ScrollableContentWithScrollbar(
                    projectPayments = filteredProjects,
                    summary = summary,
                    selectedStatus = selectedStatus,
                    onStatusSelected = { status -> selectedStatus = status },
                    onPaymentAction = { project, action ->
                        when (action) {
                            "deposit" -> {
                                selectedProjectForPayment = project
                                showPaymentConfirmDialog = true
                            }
                            "view_completed" -> {
                                selectedProjectForCompletedDetail = project
                                showCompletedPaymentDetailDialog = true
                            }
                        }
                    },
                    onPendingPaymentsClick = { showPendingPaymentsDialog = true },
                    onCompletedPaymentsClick = { showCompletedPaymentsDialog = true },
                    onAllProjectsClick = { showAllProjectsDialog = true },
                    onCompletedAmountClick = { showCompletedAmountDialog = true },
                    modifier = Modifier.fillMaxSize(),
                    showBottomBar = showBottomBar
                )
            }
        }
    }

    // 지급 대기 프로젝트 팝업
    if (showPendingPaymentsDialog) {
        val pendingProjects = projectPayments.filter { it.status == ProjectPaymentStatus.PENDING }
        
        PendingPaymentsDialog(
            pendingProjects = pendingProjects,
            onDismiss = { showPendingPaymentsDialog = false },
            onPayAllClick = {
                showPendingPaymentsDialog = false
                showBulkPaymentConfirmDialog = true
            },
            onPayProjectClick = { project ->
                selectedProjectForPayment = project
                showPaymentConfirmDialog = true
            }
        )
    }

    // 지급 완료 프로젝트 팝업
    if (showCompletedPaymentsDialog) {
        val completedProjects = projectPayments.filter { it.status == ProjectPaymentStatus.COMPLETED }
        
        ProjectListDialog(
            title = "지급 완료 프로젝트",
            projects = completedProjects,
            onDismiss = { showCompletedPaymentsDialog = false },
            actionButtonText = "상세보기",
            onProjectAction = { project ->
                selectedCompletedProject = project
                showCompletedProjectDetailDialog = true
            }
        )
    }

    // 전체 현장 팝업
    if (showAllProjectsDialog) {
        ProjectListDialog(
            title = "전체 현장",
            projects = projectPayments,
            onDismiss = { showAllProjectsDialog = false },
            actionButtonText = "상세보기",
            onProjectAction = { project ->
                selectedProjectForWorkerList = project
                showProjectWorkerListDialog = true
            }
        )
    }

    // 직직직 혜택 팝업
    if (showCompletedAmountDialog) {
        val completedProjects = projectPayments.filter { it.status == ProjectPaymentStatus.COMPLETED }
        
        CompletedAmountDialog(
            completedProjects = completedProjects,
            monthlySavings = summary.monthlySavingsAmount,
            onDismiss = { showCompletedAmountDialog = false }
        )
    }

    // 지급 확인 팝업
    if (showPaymentConfirmDialog && selectedProjectForPayment != null) {
        PaymentConfirmationDialog(
            project = selectedProjectForPayment!!,
            onDismiss = { 
                showPaymentConfirmDialog = false
                selectedProjectForPayment = null
            },
            onConfirmPayment = { project ->
                // 프로젝트를 지급 완료 상태로 변경
                markProjectAsCompleted(project.id)
                showPaymentConfirmDialog = false
                selectedProjectForPayment = null
                showPendingPaymentsDialog = false
            }
        )
    }

    // 지급 완료 프로젝트 상세보기 팝업
    if (showCompletedProjectDetailDialog && selectedCompletedProject != null) {
        CompletedProjectDetailDialog(
            project = selectedCompletedProject!!,
            onDismiss = { 
                showCompletedProjectDetailDialog = false
                selectedCompletedProject = null
            }
        )
    }

    // 전체 지급 확인 팝업
    if (showBulkPaymentConfirmDialog) {
        val pendingProjects = projectPayments.filter { it.status == ProjectPaymentStatus.PENDING }
        
        BulkPaymentConfirmationDialog(
            projects = pendingProjects,
            onDismiss = { showBulkPaymentConfirmDialog = false },
            onConfirmBulkPayment = { projects ->
                // 모든 프로젝트를 지급 완료 상태로 변경
                markProjectsAsCompleted(projects.map { it.id })
                showBulkPaymentConfirmDialog = false
            }
        )
    }

    // 프로젝트 노동자 목록 팝업
    if (showProjectWorkerListDialog && selectedProjectForWorkerList != null) {
        ProjectWorkerListDialog(
            project = selectedProjectForWorkerList!!,
            onDismiss = {
                showProjectWorkerListDialog = false
                selectedProjectForWorkerList = null
                showAllProjectsDialog = false
            }
        )
    }

    // 입금 완료 상세 정보 팝업
    if (showCompletedPaymentDetailDialog && selectedProjectForCompletedDetail != null) {
        CompletedPaymentDetailDialog(
            project = selectedProjectForCompletedDetail!!,
            onDismiss = {
                showCompletedPaymentDetailDialog = false
                selectedProjectForCompletedDetail = null
            }
        )
    }
}

@Composable
private fun ScrollableContentWithScrollbar(
    projectPayments: List<ProjectPaymentData>,
    summary: ProjectPaymentSummary,
    selectedStatus: ProjectPaymentStatus?,
    onStatusSelected: (ProjectPaymentStatus?) -> Unit,
    onPaymentAction: (ProjectPaymentData, String) -> Unit,
    onPendingPaymentsClick: () -> Unit,
    onCompletedPaymentsClick: () -> Unit,
    onAllProjectsClick: () -> Unit,
    onCompletedAmountClick: () -> Unit,
    modifier: Modifier = Modifier,
    showBottomBar: Boolean = true
) {
    val scrollState = rememberLazyListState()

    // 스크롤 진행률 계산 - 개선된 버전
    val scrollProgress by remember {
        derivedStateOf {
            val layoutInfo = scrollState.layoutInfo
            if (layoutInfo.totalItemsCount <= 1 || layoutInfo.visibleItemsInfo.isEmpty()) {
                return@derivedStateOf 0f
            }

            val firstVisibleItemIndex = scrollState.firstVisibleItemIndex
            val firstVisibleItemScrollOffset = scrollState.firstVisibleItemScrollOffset
            val lastVisibleItemIndex = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: firstVisibleItemIndex
            
            // 전체 컨텐츠 높이 계산
            val totalItemsCount = layoutInfo.totalItemsCount
            val averageItemSize = layoutInfo.visibleItemsInfo.map { it.size }.average().toFloat()
            val totalContentHeight = totalItemsCount * averageItemSize
            val viewportHeight = layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset
            
            if (totalContentHeight <= viewportHeight) {
                return@derivedStateOf 0f
            }
            
            val scrollableHeight = totalContentHeight - viewportHeight
            val currentScrollOffset = firstVisibleItemIndex * averageItemSize + firstVisibleItemScrollOffset
            
            (currentScrollOffset / scrollableHeight).coerceIn(0f, 1f)
        }
    }

    // 스크롤 가능 여부 확인
    val canScroll by remember {
        derivedStateOf {
            scrollState.layoutInfo.totalItemsCount > 0 &&
                    (scrollState.canScrollForward || scrollState.canScrollBackward || scrollState.firstVisibleItemScrollOffset > 0)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .fillMaxSize()
                .padding(end = if (canScroll) 12.dp else 0.dp), // 스크롤바 공간 확보
            verticalArrangement = Arrangement.spacedBy(16.dp), // 카드 간격 증가
            contentPadding = PaddingValues(
                start = 20.dp, // 토스 스타일 패딩
                end = 20.dp,
                top = 12.dp,
                bottom = if (showBottomBar) 120.dp else 40.dp // 하단 네비게이션 공간 확보
            ),
            userScrollEnabled = true
        ) {
            // 상단 통계 카드
            item {
                ProjectPaymentSummaryCard(
                    summary = summary,
                    onPendingPaymentsClick = onPendingPaymentsClick,
                    onCompletedPaymentsClick = onCompletedPaymentsClick,
                    onAllProjectsClick = onAllProjectsClick,
                    onCompletedAmountClick = onCompletedAmountClick
                )
            }

            // 필터 바
            item {
                ProjectPaymentFilterBar(
                    selectedStatus = selectedStatus,
                    onStatusSelected = onStatusSelected
                )
            }

            // 프로젝트 목록 헤더
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "현장별 임금 지급",
                        style = AppTypography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.3).sp
                        ),
                        color = androidx.compose.ui.graphics.Color(0xFF1A1A1A) // 진한 검정
                    )

                    Text(
                        text = "총 ${projectPayments.size}건",
                        style = AppTypography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = androidx.compose.ui.graphics.Color(0xFF6B7280) // 토스 그레이
                    )
                }
            }

            // 프로젝트별 임금 카드 목록
            items(
                items = projectPayments,
                key = { it.id }
            ) { projectPayment ->
                ProjectPaymentCard(
                    projectPayment = projectPayment,
                    onPaymentAction = onPaymentAction
                )
            }

            // 하단 여백
            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }

        // 스크롤바 - 토스 스타일
        if (canScroll) {
            ScrollBar(
                scrollProgress = scrollProgress,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()
                    .width(6.dp) // 더 열은 스크롤바
                    .padding(
                        end = 8.dp, // 오른쪽 여백 증가
                        top = 20.dp,
                        bottom = if (showBottomBar) 140.dp else 60.dp // 하단 여백 증가
                    )
            )
        }
    }
}

@Preview(name = "데이터 있음", showBackground = true, heightDp = 800)
@Composable
fun CompanyMoneyScreenWithDataPreview() {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme {
        CompanyMoneyScreen(
            navigator = navigator,
            navController = navController,
            showBottomBar = false
        )
    }
}

@Preview(name = "빈 상태", showBackground = true, heightDp = 800)
@Composable
fun CompanyMoneyScreenEmptyPreview() {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme {
        // 빈 상태를 위한 별도 컴포저블
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            EmptyMoneyState(
                onCreateProjectClick = {},
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

