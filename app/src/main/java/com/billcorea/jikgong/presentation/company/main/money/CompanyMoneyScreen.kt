package com.billcorea.jikgong.presentation.company.main.money

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.company.main.money.components.ProjectPaymentCard
import com.billcorea.jikgong.presentation.company.main.money.components.ProjectPaymentFilterBar
import com.billcorea.jikgong.presentation.company.main.money.components.ProjectPaymentSummaryCard
import com.billcorea.jikgong.presentation.company.main.money.components.EmptyMoneyState
import com.billcorea.jikgong.presentation.company.main.money.components.ScrollBar
import com.billcorea.jikgong.presentation.company.main.money.data.ProjectPaymentSampleData
import com.billcorea.jikgong.presentation.company.main.money.data.ProjectPaymentStatus
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import com.ramcosta.composedestinations.annotation.Destination
import androidx.navigation.NavController
import com.billcorea.jikgong.presentation.company.main.common.CompanyBottomBar

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
            val originalData = ProjectPaymentSampleData.getSampleProjectPayments()
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
            ProjectPaymentSampleData.getSampleProjectPaymentSummary()
        } else {
            ProjectPaymentSampleData.getEmptyProjectPaymentSummary()
        }
    }

    var selectedStatus by remember { mutableStateOf<ProjectPaymentStatus?>(null) }

    // 필터링된 프로젝트 목록
    val filteredProjects = remember(selectedStatus) {
        if (selectedStatus == null) {
            projectPayments
        } else {
            projectPayments.filter { it.status == selectedStatus }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
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
            // 상단바
            TopAppBar(
                title = {
                    Text(
                        text = "임금 관리",
                        style = AppTypography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            // TODO: 검색 기능 구현
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "검색"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = appColorScheme.surface,
                    titleContentColor = appColorScheme.onSurface
                )
            )

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
    projectPayments: List<com.billcorea.jikgong.presentation.company.main.money.data.ProjectPaymentData>,
    summary: com.billcorea.jikgong.presentation.company.main.money.data.ProjectPaymentSummary,
    selectedStatus: ProjectPaymentStatus?,
    onStatusSelected: (ProjectPaymentStatus?) -> Unit,
    onPaymentAction: (com.billcorea.jikgong.presentation.company.main.money.data.ProjectPaymentData, String) -> Unit,
    modifier: Modifier = Modifier,
    showBottomBar: Boolean = true
) {
    val scrollState = rememberLazyListState()

    // 스크롤 진행률 계산
    val scrollProgress by remember {
        derivedStateOf {
            if (scrollState.layoutInfo.totalItemsCount <= 1) return@derivedStateOf 0f

            val firstVisibleItemIndex = scrollState.firstVisibleItemIndex
            val firstVisibleItemScrollOffset = scrollState.firstVisibleItemScrollOffset
            val itemHeight = scrollState.layoutInfo.visibleItemsInfo.firstOrNull()?.size ?: 1

            val totalScrollableHeight = (scrollState.layoutInfo.totalItemsCount - 1) * itemHeight
            val currentScrollOffset = firstVisibleItemIndex * itemHeight + firstVisibleItemScrollOffset

            if (totalScrollableHeight > 0) {
                (currentScrollOffset.toFloat() / totalScrollableHeight.toFloat()).coerceIn(0f, 1f)
            } else {
                0f
            }
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
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = 8.dp,
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
                        style = AppTypography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = appColorScheme.onSurface
                    )

                    Text(
                        text = "총 ${projectPayments.size}건",
                        style = AppTypography.bodyMedium,
                        color = appColorScheme.onSurfaceVariant
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

        // 스크롤바
        if (canScroll) {
            ScrollBar(
                scrollProgress = scrollProgress,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()
                    .width(8.dp)
                    .padding(
                        end = 4.dp,
                        top = 16.dp,
                        bottom = if (showBottomBar) 120.dp else 40.dp
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