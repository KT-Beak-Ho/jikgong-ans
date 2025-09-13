package com.billcorea.jikgong.presentation.company.main.money.presentation.screen

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.api.models.sampleDataFactory.CompanyMockDataFactory
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.ProjectPaymentData
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.ProjectPaymentStatus
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.ProjectPaymentSummary
import com.billcorea.jikgong.presentation.company.main.common.CompanyBottomBar
import com.billcorea.jikgong.presentation.company.main.money.feature.dialog.screen.*
import com.billcorea.jikgong.presentation.company.main.money.presentation.component.*
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.*

/**
 * 개선된 Money 화면
 * - 향상된 검색 기능
 * - 명확한 네비게이션
 * - 실시간 검색 결과
 */
@Destination
@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun CompanyMoneyScreenEnhanced(
    navigator: DestinationsNavigator,
    navController: NavController,
    modifier: Modifier = Modifier,
    showBottomBar: Boolean = true
) {
    // 상태 관리
    val hasData = remember { mutableStateOf(true) }
    val allProjectPayments = remember {
        if (hasData.value) {
            CompanyMockDataFactory.getProjectPayments()
        } else {
            emptyList()
        }
    }
    
    // 검색 상태
    var searchQuery by remember { mutableStateOf("") }
    var isSearchBarVisible by remember { mutableStateOf(false) }
    var isSearching by remember { mutableStateOf(false) }
    val searchFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    
    // 필터링된 프로젝트
    val filteredProjects = remember(searchQuery, allProjectPayments) {
        if (searchQuery.isEmpty()) {
            allProjectPayments
        } else {
            allProjectPayments.filter { project ->
                project.projectTitle.contains(searchQuery, ignoreCase = true) ||
                project.workers.any { worker ->
                    worker.workerName.contains(searchQuery, ignoreCase = true)
                }
            }
        }
    }
    
    // 상태 필터
    var selectedStatus by remember { mutableStateOf<ProjectPaymentStatus?>(null) }
    
    // 최종 필터링된 프로젝트 (검색 + 상태 필터)
    val displayedProjects = remember(filteredProjects, selectedStatus) {
        val statusFiltered = if (selectedStatus == null) {
            filteredProjects
        } else {
            filteredProjects.filter { it.status == selectedStatus }
        }
        
        // 우선순위 정렬
        statusFiltered.sortedWith(compareBy<ProjectPaymentData> { project ->
            when (project.status) {
                ProjectPaymentStatus.OVERDUE -> 0
                ProjectPaymentStatus.PENDING -> 1
                ProjectPaymentStatus.PROCESSING -> 2
                ProjectPaymentStatus.FAILED -> 3
                ProjectPaymentStatus.COMPLETED -> 4
            }
        }.thenByDescending { it.createdAt })
    }
    
    // 요약 데이터
    val summary = remember(displayedProjects) {
        if (displayedProjects.isNotEmpty()) {
            val pendingProjects = displayedProjects.filter { it.status == ProjectPaymentStatus.PENDING }
            val completedProjects = displayedProjects.filter { it.status == ProjectPaymentStatus.COMPLETED }
            
            ProjectPaymentSummary(
                totalProjects = displayedProjects.size,
                pendingPayments = pendingProjects.size,
                completedPayments = completedProjects.size,
                totalAmount = displayedProjects.sumOf { it.totalAmount },
                pendingAmount = pendingProjects.sumOf { it.totalAmount },
                paidAmount = completedProjects.sumOf { it.paidAmount }
            )
        } else {
            CompanyMockDataFactory.getEmptyProjectPaymentSummary()
        }
    }
    
    // 다이얼로그 상태
    var showPaymentConfirmDialog by remember { mutableStateOf(false) }
    var selectedProjectForPayment by remember { mutableStateOf<ProjectPaymentData?>(null) }
    var showCreateProjectDialog by remember { mutableStateOf(false) }
    
    // 검색바 애니메이션
    LaunchedEffect(isSearchBarVisible) {
        if (isSearchBarVisible) {
            delay(100)
            searchFocusRequester.requestFocus()
        }
    }
    
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color(0xFFF7F8FA),
        topBar = {
            Column {
                // 메인 탑바
                TopAppBar(
                    title = { 
                        Text(
                            "임금 관리",
                            style = AppTypography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    },
                    actions = {
                        // 검색 버튼
                        IconButton(
                            onClick = { 
                                isSearchBarVisible = !isSearchBarVisible
                                if (!isSearchBarVisible) {
                                    searchQuery = ""
                                    keyboardController?.hide()
                                }
                            }
                        ) {
                            Icon(
                                if (isSearchBarVisible) Icons.Default.Close else Icons.Default.Search,
                                contentDescription = if (isSearchBarVisible) "검색 닫기" else "검색",
                                tint = if (isSearchBarVisible) Color(0xFFE57373) else Color(0xFF4B7BFF)
                            )
                        }
                        
                        // 프로젝트 추가 버튼
                        IconButton(
                            onClick = { 
                                // 프로젝트 생성 화면으로 네비게이션
                                navigateToProjectCreate(navController)
                            }
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "프로젝트 추가",
                                tint = Color(0xFF4B7BFF)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    )
                )
                
                // 검색바 (애니메이션)
                AnimatedVisibility(
                    visible = isSearchBarVisible,
                    enter = slideInVertically() + fadeIn(),
                    exit = slideOutVertically() + fadeOut()
                ) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.White,
                        shadowElevation = 2.dp
                    ) {
                        SearchBar(
                            query = searchQuery,
                            onQueryChange = { searchQuery = it },
                            onSearch = {
                                keyboardController?.hide()
                                focusManager.clearFocus()
                            },
                            focusRequester = searchFocusRequester,
                            resultCount = if (searchQuery.isNotEmpty()) filteredProjects.size else null
                        )
                    }
                }
            }
        },
        bottomBar = {
            if (showBottomBar) {
                CompanyBottomBar(
                    navController = navController,
                    currentRoute = "company_money_screen"
                )
            }
        },
        floatingActionButton = {
            if (displayedProjects.isEmpty() && searchQuery.isEmpty()) {
                ExtendedFloatingActionButton(
                    onClick = { navigateToProjectCreate(navController) },
                    modifier = Modifier.padding(16.dp),
                    containerColor = Color(0xFF4B7BFF),
                    contentColor = Color.White
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("프로젝트 생성")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                // 검색 결과가 없을 때
                searchQuery.isNotEmpty() && filteredProjects.isEmpty() -> {
                    EmptySearchResult(
                        searchQuery = searchQuery,
                        onClearSearch = { searchQuery = "" }
                    )
                }
                
                // 프로젝트가 없을 때
                displayedProjects.isEmpty() && searchQuery.isEmpty() -> {
                    EmptyMoneyState(
                        onCreateProjectClick = { navigateToProjectCreate(navController) },
                        modifier = Modifier.fillMaxSize()
                    )
                }
                
                // 정상적인 리스트 표시
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // 요약 카드
                        item {
                            ProjectPaymentSummaryCard(
                                summary = summary,
                                onPendingPaymentsClick = { selectedStatus = ProjectPaymentStatus.PENDING },
                                onCompletedPaymentsClick = { selectedStatus = ProjectPaymentStatus.COMPLETED },
                                onAllProjectsClick = { selectedStatus = null },
                                onCompletedAmountClick = { /* 완료 금액 클릭 처리 */ }
                            )
                        }
                        
                        // 필터 바
                        item {
                            ProjectPaymentFilterBar(
                                selectedStatus = selectedStatus,
                                onStatusSelected = { selectedStatus = it }
                            )
                        }
                        
                        // 검색 결과 표시
                        if (searchQuery.isNotEmpty()) {
                            item {
                                SearchResultInfo(
                                    query = searchQuery,
                                    resultCount = filteredProjects.size
                                )
                            }
                        }
                        
                        // 프로젝트 리스트
                        items(
                            items = displayedProjects,
                            key = { it.id }
                        ) { project ->
                            ProjectPaymentCard(
                                projectPayment = project,
                                onPaymentAction = { selectedProject, action ->
                                    when (action) {
                                        "deposit" -> {
                                            selectedProjectForPayment = selectedProject
                                            showPaymentConfirmDialog = true
                                        }
                                        "view_completed" -> {
                                            // 완료된 프로젝트 상세 보기
                                            navigateToProjectDetail(navController, selectedProject.id)
                                        }
                                        else -> {
                                            // 기타 액션 처리
                                        }
                                    }
                                }
                            )
                        }
                        
                        // 하단 여백
                        item {
                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }
                }
            }
        }
    }
    
    // 지급 확인 다이얼로그
    if (showPaymentConfirmDialog && selectedProjectForPayment != null) {
        PaymentConfirmationDialog(
            project = selectedProjectForPayment!!,
            onConfirmPayment = { project ->
                // 지급 처리 로직
                // TODO: 실제 API 호출하여 지급 처리
                showPaymentConfirmDialog = false
                selectedProjectForPayment = null
            },
            onDismiss = {
                showPaymentConfirmDialog = false
                selectedProjectForPayment = null
            }
        )
    }
}

/**
 * 검색바 컴포넌트
 */
@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    focusRequester: FocusRequester,
    resultCount: Int? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester),
            placeholder = { 
                Text(
                    "프로젝트명 또는 인력명 검색",
                    color = Color.Gray
                )
            },
            leadingIcon = {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null,
                    tint = Color(0xFF4B7BFF)
                )
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(
                        onClick = { onQueryChange("") }
                    ) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = "검색어 지우기",
                            tint = Color.Gray
                        )
                    }
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = { onSearch() }
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF4B7BFF),
                unfocusedBorderColor = Color.LightGray
            ),
            shape = RoundedCornerShape(12.dp)
        )
        
        // 검색 결과 개수 표시
        if (resultCount != null) {
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color(0xFF4B7BFF)),
                color = Color(0xFF4B7BFF)
            ) {
                Text(
                    text = resultCount.toString(),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}

/**
 * 검색 결과 정보
 */
@Composable
private fun SearchResultInfo(
    query: String,
    resultCount: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE3F2FD)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Info,
                contentDescription = null,
                tint = Color(0xFF4B7BFF),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "\"$query\" 검색 결과: ${resultCount}건",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF4B7BFF)
            )
        }
    }
}

/**
 * 빈 검색 결과
 */
@Composable
private fun EmptySearchResult(
    searchQuery: String,
    onClearSearch: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.SearchOff,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = Color.LightGray
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "\"$searchQuery\"에 대한\n검색 결과가 없습니다",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "다른 검색어를 입력해보세요",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        OutlinedButton(
            onClick = onClearSearch,
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                Icons.Default.Refresh,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("검색 초기화")
        }
    }
}

/**
 * 네비게이션 함수들
 */
private fun navigateToProjectCreate(navController: NavController) {
    // JobCreationScreenRefactored로 이동
    navController.navigate("job_creation_screen")
}

private fun navigateToProjectDetail(navController: NavController, projectId: String) {
    // 프로젝트 상세 화면으로 이동
    navController.navigate("project_detail/$projectId")
}

@Preview(showBackground = true)
@Composable
fun CompanyMoneyScreenEnhancedPreview() {
    Jikgong1111Theme {
        val navController = rememberNavController()
        val navigator = navController.toDestinationsNavigator()
        
        CompanyMoneyScreenEnhanced(
            navigator = navigator,
            navController = navController
        )
    }
}