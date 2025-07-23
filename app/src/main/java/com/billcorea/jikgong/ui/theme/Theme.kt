package com.billcorea.jikgong.presentation.company.main.money

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.company.main.money.components.PaymentCard
import com.billcorea.jikgong.presentation.company.main.money.data.PaymentStatus
import com.billcorea.jikgong.presentation.company.main.money.shared.CompanyMoneySharedEvent
import com.billcorea.jikgong.presentation.company.main.money.shared.CompanyMoneySharedViewModel
import com.billcorea.jikgong.presentation.company.main.money.shared.CompanyMoneySharedUiState
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import org.koin.androidx.compose.koinViewModel
import java.text.NumberFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun CompanyMoneyScreen(
    navigator: DestinationsNavigator,
    viewModel: CompanyMoneySharedViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val shouldNavigateToDetail by viewModel.shouldNavigateToDetail.collectAsStateWithLifecycle()
    val shouldNavigateToWorkerDetail by viewModel.shouldNavigateToWorkerDetail.collectAsStateWithLifecycle()

    // 데이터 상태 토글을 위한 State (테스트용)
    var hasData by remember { mutableStateOf(false) } // 기본값을 false로 변경

    // 하단 시트 상태
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )
    var showBottomSheet by remember { mutableStateOf(false) }

    // 네비게이션 이벤트 처리
    LaunchedEffect(shouldNavigateToDetail) {
        shouldNavigateToDetail?.let { paymentId ->
            viewModel.clearNavigationEvents()
        }
    }

    LaunchedEffect(shouldNavigateToWorkerDetail) {
        shouldNavigateToWorkerDetail?.let { workerId ->
            viewModel.clearNavigationEvents()
        }
    }

    // 에러 다이얼로그
    uiState.errorMessage?.let { message ->
        AlertDialog(
            onDismissRequest = {
                viewModel.onEvent(CompanyMoneySharedEvent.ClearError)
            },
            title = { Text("오류") },
            text = { Text(message) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.onEvent(CompanyMoneySharedEvent.ClearError)
                    }
                ) {
                    Text("확인")
                }
            }
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // 상단바
            TopAppBar(
                title = {
                    Text(
                        text = "지급내역",
                        style = AppTypography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        ),
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    // 빈 공간 (뒤로가기 버튼 없음)
                },
                actions = {
                    // 데이터 토글 버튼 (테스트용)
                    TextButton(
                        onClick = { hasData = !hasData }
                    ) {
                        Text(
                            text = if (hasData) "데이터 제거" else "데이터 추가",
                            fontSize = 12.sp,
                            color = appColorScheme.primary
                        )
                    }

                    // 알림 아이콘
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "알림",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )

            // 컨텐츠는 hasData 상태에 따라 결정
            if (hasData) {
                // 데이터가 있는 경우: 기존 UI 표시
                DataAvailableContent(
                    uiState = uiState,
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                // 데이터가 없는 경우: 빈 상태 UI 표시
                NoDataAvailableContent(
                    onBottomSheetOpen = { showBottomSheet = true },
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // 하단 시트
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = bottomSheetState,
                modifier = Modifier.fillMaxSize()
            ) {
                BottomSheetContent(
                    onDismiss = { showBottomSheet = false }
                )
            }
        }
    }
}

@Composable
private fun NoDataAvailableContent(
    onBottomSheetOpen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // 지급 주기 섹션
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "지급 주기",
                    style = AppTypography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    ),
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "0일",
                    style = AppTypography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = appColorScheme.primary
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 48시간 이내 입금 섹션
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "48시간 이내 입금",
                            style = AppTypography.titleMedium.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            ),
                            color = Color.Black
                        )
                        Text(
                            text = "48시간 내에 입금할 일감이 있습니다.",
                            style = AppTypography.bodyMedium.copy(
                                fontSize = 12.sp
                            ),
                            color = Color.Gray
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.HelpOutline,
                        contentDescription = "도움말",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 지급내역 섹션
        Text(
            text = "지급내역",
            style = AppTypography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            ),
            color = Color.Black,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "받은 지급내역서가 없습니다.",
                    style = AppTypography.bodyMedium.copy(
                        fontSize = 14.sp
                    ),
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // 지급 내역 버튼 섹션
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = onBottomSheetOpen,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = appColorScheme.primary
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "지급",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            OutlinedButton(
                onClick = onBottomSheetOpen,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "캘린더",
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun BottomSheetContent(
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // 상단 핸들
        Box(
            modifier = Modifier
                .width(40.dp)
                .height(4.dp)
                .background(Color.Gray.copy(alpha = 0.3f), RoundedCornerShape(2.dp))
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 제목
        Text(
            text = "지급 관리 기능",
            style = AppTypography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = Color.Black,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // 기능 목록
        val features = listOf(
            Pair(Icons.Default.Payment, "임금 지급 관리"),
            Pair(Icons.Default.Schedule, "지급 일정 관리"),
            Pair(Icons.Default.Assessment, "지급 내역 조회"),
            Pair(Icons.Default.Notifications, "지급 알림 설정"),
            Pair(Icons.Default.CalendarMonth, "캘린더 뷰"),
            Pair(Icons.Default.FileDownload, "내역 내보내기")
        )

        features.forEach { (icon, title) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = appColorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = title,
                    style = AppTypography.bodyLarge,
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 안내 메시지
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFFF3F4F6)
        ) {
            Text(
                text = "💡 프로젝트에 인력을 고용하면 임금 지급 기능을 사용할 수 있습니다.",
                style = AppTypography.bodyMedium,
                color = Color(0xFF6B7280),
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 닫기 버튼
        Button(
            onClick = onDismiss,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = appColorScheme.primary
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = "확인",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun DataAvailableContent(
    uiState: CompanyMoneySharedUiState,
    viewModel: CompanyMoneySharedViewModel,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // 요약 정보 카드
        uiState.summary?.let { summary ->
            if (summary.urgentPaymentsCount > 0) {
                UrgentPaymentCard(
                    urgentCount = summary.urgentPaymentsCount,
                    totalPendingAmount = summary.totalPendingAmount,
                    onViewUrgentPayments = {
                        viewModel.onEvent(CompanyMoneySharedEvent.FilterByStatus(PaymentStatus.URGENT))
                    }
                )
            }
        }

        // 선택된 항목 액션 바 (멀티 선택 모드일 때)
        if (uiState.isMultiSelectMode && uiState.selectedPaymentIds.isNotEmpty()) {
            SelectedPaymentsActionBar(
                selectedCount = uiState.selectedPaymentsInfo.first,
                totalAmount = uiState.selectedPaymentsInfo.second,
                onProcessSelected = {
                    viewModel.onEvent(CompanyMoneySharedEvent.ProcessSelectedPayments)
                },
                onClearSelection = {
                    viewModel.onEvent(CompanyMoneySharedEvent.ClearSelection)
                }
            )
        }

        // 하단 탭바
        TabRow(
            selectedTabIndex = uiState.selectedTabIndex,
            modifier = Modifier.fillMaxWidth(),
            containerColor = appColorScheme.surface,
            contentColor = appColorScheme.primary
        ) {
            Tab(
                selected = uiState.selectedTabIndex == 0,
                onClick = {
                    viewModel.onEvent(CompanyMoneySharedEvent.ChangeTab(0))
                },
                text = {
                    Text(
                        text = "지급 목록 (${uiState.filteredPayments.size})",
                        style = AppTypography.titleMedium.copy(
                            fontWeight = if (uiState.selectedTabIndex == 0) FontWeight.Bold else FontWeight.Normal
                        )
                    )
                }
            )

            Tab(
                selected = uiState.selectedTabIndex == 1,
                onClick = {
                    viewModel.onEvent(CompanyMoneySharedEvent.ChangeTab(1))
                },
                text = {
                    Text(
                        text = "캘린더",
                        style = AppTypography.titleMedium.copy(
                            fontWeight = if (uiState.selectedTabIndex == 1) FontWeight.Bold else FontWeight.Normal
                        )
                    )
                }
            )
        }

        // 탭 콘텐츠
        if (uiState.selectedTabIndex == 0) {
            PaymentListContent(
                uiState = uiState,
                viewModel = viewModel,
                modifier = Modifier.fillMaxSize()
            )
        } else if (uiState.selectedTabIndex == 1) {
            PaymentCalendarContent(
                uiState = uiState,
                viewModel = viewModel,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
private fun UrgentPaymentCard(
    urgentCount: Int,
    totalPendingAmount: Long,
    onViewUrgentPayments: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFEBEE)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AccessTime,
                contentDescription = "긴급",
                tint = Color(0xFFD32F2F),
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "48시간 이내 지급 필요",
                    style = AppTypography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD32F2F)
                    )
                )
                Text(
                    text = "${urgentCount}건 (${NumberFormat.getNumberInstance(Locale.KOREA).format(totalPendingAmount)}원)",
                    style = AppTypography.bodySmall.copy(
                        color = Color(0xFF757575)
                    )
                )
            }

            TextButton(
                onClick = onViewUrgentPayments
            ) {
                Text(
                    text = "확인하기",
                    color = Color(0xFFD32F2F),
                    style = AppTypography.labelMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Composable
private fun SelectedPaymentsActionBar(
    selectedCount: Int,
    totalAmount: Long,
    onProcessSelected: () -> Unit,
    onClearSelection: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = appColorScheme.primaryContainer,
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "${selectedCount}건 선택",
                    style = AppTypography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = appColorScheme.onPrimaryContainer
                )
                Text(
                    text = "총 ${NumberFormat.getNumberInstance(Locale.KOREA).format(totalAmount)}원",
                    style = AppTypography.bodySmall,
                    color = appColorScheme.onPrimaryContainer
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextButton(onClick = onClearSelection) {
                    Text("취소")
                }

                Button(
                    onClick = onProcessSelected,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = appColorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Payment,
                        contentDescription = "지급 처리",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("지급 처리")
                }
            }
        }
    }
}

@Composable
private fun PaymentListContent(
    uiState: CompanyMoneySharedUiState,
    viewModel: CompanyMoneySharedViewModel,
    modifier: Modifier = Modifier
) {
    if (uiState.isLoading) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "지급 정보를 불러오는 중...",
                    style = AppTypography.bodyMedium,
                    color = appColorScheme.onSurfaceVariant
                )
            }
        }
    } else if (uiState.filteredPayments.isEmpty()) {
        EmptyPaymentListContent(
            hasActiveFilters = uiState.hasActiveFilters,
            onClearFilters = {
                viewModel.onEvent(CompanyMoneySharedEvent.ClearFilters)
            },
            modifier = modifier
        )
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(
                items = uiState.filteredPayments,
                key = { it.id }
            ) { payment ->
                PaymentCard(
                    payment = payment,
                    isSelected = payment.id in uiState.selectedPaymentIds,
                    isMultiSelectMode = uiState.isMultiSelectMode,
                    onPaymentClick = {
                        viewModel.onEvent(CompanyMoneySharedEvent.ShowPaymentDetail(it.id))
                    },
                    onToggleSelection = { paymentId ->
                        viewModel.onEvent(CompanyMoneySharedEvent.TogglePaymentSelection(paymentId))
                    },
                    onProcessPayment = { paymentId ->
                        viewModel.onEvent(CompanyMoneySharedEvent.ProcessSinglePayment(paymentId))
                    },
                    onMarkUrgent = { paymentId ->
                        viewModel.onEvent(CompanyMoneySharedEvent.MarkAsUrgent(paymentId))
                    }
                )
            }
        }
    }
}

@Composable
private fun EmptyPaymentListContent(
    hasActiveFilters: Boolean,
    onClearFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Payment,
                contentDescription = "지급 목록",
                modifier = Modifier.size(64.dp),
                tint = appColorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (hasActiveFilters) {
                    "필터 조건에 맞는 지급 내역이 없습니다"
                } else {
                    "지급할 임금이 없습니다"
                },
                style = AppTypography.titleMedium,
                color = appColorScheme.onSurfaceVariant
            )

            Text(
                text = if (hasActiveFilters) {
                    "필터를 변경하거나 초기화해보세요"
                } else {
                    "프로젝트 완료 후 임금을 지급하세요"
                },
                style = AppTypography.bodyMedium,
                color = appColorScheme.onSurfaceVariant
            )

            if (hasActiveFilters) {
                Spacer(modifier = Modifier.height(16.dp))
                TextButton(onClick = onClearFilters) {
                    Text("필터 초기화")
                }
            }
        }
    }
}

@Composable
private fun PaymentCalendarContent(
    uiState: CompanyMoneySharedUiState,
    viewModel: CompanyMoneySharedViewModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = "지급 캘린더",
                modifier = Modifier.size(64.dp),
                tint = appColorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "캘린더 뷰 준비 중",
                style = AppTypography.titleMedium,
                color = appColorScheme.onSurfaceVariant
            )

            Text(
                text = "임금 지급 일정을 달력으로 확인하세요",
                style = AppTypography.bodyMedium,
                color = appColorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(name = "데이터 없음", showBackground = true)
@Composable
fun CompanyMoneyScreenNoDataPreview() {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme {
        CompanyMoneyScreen(
            navigator = navigator,
            viewModel = CompanyMoneySharedViewModel()
        )
    }
}

@Preview(name = "데이터 있음", showBackground = true)
@Composable
fun CompanyMoneyScreenWithDataPreview() {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme {
        // 데이터가 있는 상태로 시작하려면 hasData를 true로 설정
        CompanyMoneyScreen(
            navigator = navigator,
            viewModel = CompanyMoneySharedViewModel()
        )
    }
}