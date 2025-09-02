package com.billcorea.jikgong.presentation.company.main.money

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.company.main.money.components.ProjectPaymentCard
import com.billcorea.jikgong.presentation.company.main.money.components.ProjectPaymentFilterBar
import com.billcorea.jikgong.presentation.company.main.money.components.ProjectPaymentSummaryCard
import com.billcorea.jikgong.presentation.company.main.money.components.EmptyMoneyState
import com.billcorea.jikgong.presentation.company.main.money.components.ScrollBar
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

    val projectPayments = remember {
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

    val summary = remember {
        if (hasData) {
            CompanyMockDataFactory.getProjectPaymentSummary()
        } else {
            CompanyMockDataFactory.getEmptyProjectPaymentSummary()
        }
    }

    var selectedStatus by remember { mutableStateOf<ProjectPaymentStatus?>(null) }

    // 필터링 및 정렬된 프로젝트 목록
    val filteredProjects = remember(selectedStatus) {
        val filtered = if (selectedStatus == null) {
            projectPayments
        } else {
            projectPayments.filter { it.status == selectedStatus }
        }
        
        // 우선순위에 따른 정렬: 지급 대기 > 처리중 > 연체 > 지급 실패 > 지급 완료
        filtered.sortedWith(compareBy<ProjectPaymentData> { project ->
            when (project.status) {
                ProjectPaymentStatus.PENDING -> 1    // 지급 대기 (최우선)
                ProjectPaymentStatus.PROCESSING -> 2  // 처리중
                ProjectPaymentStatus.OVERDUE -> 3     // 연체
                ProjectPaymentStatus.FAILED -> 4      // 지급 실패
                ProjectPaymentStatus.COMPLETED -> 5   // 지급 완료 (마지막)
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
                    // TODO: 검색 기능 구현
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
                        // TODO: 프로젝트 생성 화면으로 이동
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
                        // TODO: 임금 지급 액션 처리
                    },
                    modifier = Modifier.fillMaxSize(),
                    showBottomBar = showBottomBar
                )
            }
        }
    }
}

@Composable
private fun ScrollableContentWithScrollbar(
    projectPayments: List<ProjectPaymentData>,
    summary: ProjectPaymentSummary,
    selectedStatus: ProjectPaymentStatus?,
    onStatusSelected: (ProjectPaymentStatus?) -> Unit,
    onPaymentAction: (ProjectPaymentData, String) -> Unit,
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
                ProjectPaymentSummaryCard(summary = summary)
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