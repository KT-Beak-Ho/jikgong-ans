package com.billcorea.jikgong.presentation.company.main.money

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.R
import com.billcorea.jikgong.presentation.company.main.money.components.ProjectPaymentCard
import com.billcorea.jikgong.presentation.company.main.money.components.PaymentFilterBar
import com.billcorea.jikgong.presentation.company.main.money.components.PaymentSummaryCard
import com.billcorea.jikgong.presentation.company.main.money.data.ProjectPaymentSampleData
import com.billcorea.jikgong.presentation.company.main.money.data.ProjectPaymentStatus
import com.billcorea.jikgong.presentation.company.main.money.data.PaymentStatus
import com.billcorea.jikgong.presentation.company.main.money.shared.CompanyMoneySharedEvent
import com.billcorea.jikgong.presentation.company.main.money.shared.CompanyMoneySharedViewModel
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Destination(route = "company_money")
@Composable
fun CompanyMoneyScreen(
    navigator: DestinationsNavigator,
    viewModel: CompanyMoneySharedViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    modifier: Modifier = Modifier
) {
    var currentRoute by remember { mutableStateOf(com.billcorea.jikgong.presentation.company.main.common.components.CompanyBottomNavTabs.MONEY.route) }

    // 프로젝트별 샘플 데이터 사용
    val projectPayments = remember { ProjectPaymentSampleData.getSampleProjectPayments() }
    val summary = remember { ProjectPaymentSampleData.getSampleProjectPaymentSummary() }
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
            com.billcorea.jikgong.presentation.company.main.common.components.CompanyBottomNavigation(
                currentRoute = currentRoute,
                onTabSelected = { route ->
                    currentRoute = route
                    // TODO: 다른 화면으로 네비게이션 처리
                }
            )
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
                EmptyState(
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                // 데이터가 있는 상태 - 커스텀 스크롤바와 함께
                ScrollableContentWithScrollbar(
                    projectPayments = filteredProjects,
                    summary = summary,
                    selectedStatus = selectedStatus,
                    onStatusSelected = { status -> selectedStatus = status },
                    onPaymentAction = { project, action ->
                        // TODO: 임금 지급 액션 처리
                    },
                    modifier = Modifier.fillMaxSize()
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
    modifier: Modifier = Modifier
) {
    val scrollState = rememberLazyListState()

    // 스크롤 진행률 계산
    val scrollProgress by remember {
        derivedStateOf {
            if (scrollState.layoutInfo.totalItemsCount == 0) return@derivedStateOf 0f

            val visibleItemsInfo = scrollState.layoutInfo.visibleItemsInfo
            if (visibleItemsInfo.isEmpty()) return@derivedStateOf 0f

            val totalItems = scrollState.layoutInfo.totalItemsCount
            val lastVisibleItem = visibleItemsInfo.lastOrNull()?.index ?: 0

            if (totalItems <= 1) return@derivedStateOf 0f

            lastVisibleItem.toFloat() / (totalItems - 1)
        }
    }

    // 스크롤 가능 여부 확인
    val canScroll by remember {
        derivedStateOf {
            scrollState.canScrollForward || scrollState.canScrollBackward
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            state = scrollState,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(start = 16.dp, end = 28.dp, top = 16.dp, bottom = 16.dp)
        ) {
            // 상단 통계 카드
            item {
                ProjectPaymentSummaryCard(summary = summary)
            }

            // 필터 바 (프로젝트용으로 수정)
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
            items(projectPayments) { projectPayment ->
                ProjectPaymentCard(
                    projectPayment = projectPayment,
                    onPaymentAction = onPaymentAction
                )
            }

            // 하단 여백
            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }

        // 세로 스크롤바
        if (canScroll) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .fillMaxHeight()
                    .width(12.dp)
                    .padding(end = 4.dp, top = 8.dp, bottom = 8.dp)
            ) {
                // 스크롤바 트랙
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = appColorScheme.outline.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(6.dp)
                        )
                )

                // 스크롤바 썸
                val thumbHeight = 0.3f
                val thumbPosition = scrollProgress * (1f - thumbHeight)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(thumbHeight)
                        .offset(y = (thumbPosition * 100).dp)
                        .background(
                            color = appColorScheme.primary.copy(alpha = 0.6f),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .alpha(if (canScroll) 1f else 0f)
                )
            }
        }
    }
}

@Composable
private fun ProjectPaymentSummaryCard(
    summary: com.billcorea.jikgong.presentation.company.main.money.data.ProjectPaymentSummary,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = appColorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // 상단 지급률과 금액
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "지급률",
                    style = AppTypography.bodyMedium,
                    color = appColorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )

                Text(
                    text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(summary.monthlyTotal)}원",
                    style = AppTypography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = appColorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 통계 정보
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SummaryStatItem(
                    title = "지급 대기",
                    count = summary.pendingProjects,
                    amount = summary.pendingAmount,
                    color = Color(0xFFFFA726)
                )

                SummaryStatItem(
                    title = "지급 완료",
                    count = summary.completedProjects,
                    amount = summary.completedAmount,
                    color = Color(0xFF66BB6A)
                )

                SummaryStatItem(
                    title = "전체 현장",
                    count = summary.totalProjects,
                    amount = summary.totalAmount,
                    color = appColorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun SummaryStatItem(
    title: String,
    count: Int,
    amount: Long,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = AppTypography.labelMedium,
            color = appColorScheme.onPrimaryContainer.copy(alpha = 0.7f)
        )

        Text(
            text = "${count}건",
            style = AppTypography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = color
        )

        Text(
            text = "${NumberFormat.getNumberInstance(Locale.KOREA).format(amount)}원",
            style = AppTypography.bodySmall,
            color = appColorScheme.onPrimaryContainer.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun ProjectPaymentFilterBar(
    selectedStatus: ProjectPaymentStatus?,
    onStatusSelected: (ProjectPaymentStatus?) -> Unit,
    modifier: Modifier = Modifier
) {
    val filterItems = listOf(
        null to "전체",
        ProjectPaymentStatus.PENDING to "지급 대기",
        ProjectPaymentStatus.PROCESSING to "처리중",
        ProjectPaymentStatus.COMPLETED to "지급 완료",
        ProjectPaymentStatus.FAILED to "지급 실패"
    )

    val scrollState = rememberLazyListState()

    // 스크롤 진행률 계산
    val scrollProgress by remember {
        derivedStateOf {
            if (scrollState.layoutInfo.totalItemsCount == 0) return@derivedStateOf 0f

            val visibleItemsInfo = scrollState.layoutInfo.visibleItemsInfo
            if (visibleItemsInfo.isEmpty()) return@derivedStateOf 0f

            val totalItems = scrollState.layoutInfo.totalItemsCount
            val firstVisibleItem = scrollState.firstVisibleItemIndex
            val firstVisibleItemOffset = scrollState.firstVisibleItemScrollOffset
            val itemWidth = visibleItemsInfo.firstOrNull()?.size ?: 1

            val scrollOffset = firstVisibleItem + (firstVisibleItemOffset.toFloat() / itemWidth)
            (scrollOffset / (totalItems - 1).coerceAtLeast(1)).coerceIn(0f, 1f)
        }
    }

    val canScroll by remember {
        derivedStateOf {
            scrollState.layoutInfo.totalItemsCount > 0 &&
                    scrollState.canScrollForward || scrollState.canScrollBackward
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        androidx.compose.foundation.lazy.LazyRow(
            state = scrollState,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            userScrollEnabled = true
        ) {
            items(filterItems) { (status, label) ->
                FilterChip(
                    onClick = { onStatusSelected(status) },
                    label = {
                        Text(
                            text = label,
                            style = AppTypography.labelMedium.copy(
                                fontWeight = if (selectedStatus == status) {
                                    FontWeight.Bold
                                } else {
                                    FontWeight.Normal
                                }
                            )
                        )
                    },
                    selected = selectedStatus == status,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = appColorScheme.primary,
                        selectedLabelColor = appColorScheme.onPrimary
                    ),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier.defaultMinSize(minWidth = 80.dp)
                )
            }
        }

        // 가로 스크롤바
        if (canScroll) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .padding(horizontal = 16.dp, vertical = 2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = appColorScheme.outline.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(2.dp)
                        )
                )

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.3f)
                        .offset(x = (scrollProgress * 70).dp)
                        .background(
                            color = appColorScheme.primary.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(2.dp)
                        )
                )
            }
        }
    }
}

@Composable
private fun EmptyState(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 40.dp)
        ) {
            Text(
                text = "임금 지급 내역이 없습니다.",
                style = AppTypography.bodyLarge,
                color = appColorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
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
            navigator = navigator
        )
    }
}

@Preview(name = "빈 상태", showBackground = true, heightDp = 800)
@Composable
fun CompanyMoneyScreenEmptyPreview() {
    Jikgong1111Theme {
        EmptyState(
            modifier = Modifier.fillMaxSize()
        )
    }
}