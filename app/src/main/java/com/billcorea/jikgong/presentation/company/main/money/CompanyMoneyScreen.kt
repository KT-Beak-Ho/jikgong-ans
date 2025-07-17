package com.billcorea.jikgong.presentation.company.main.money

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.company.main.money.components.PaymentCard
import com.billcorea.jikgong.presentation.company.main.money.data.PaymentStatus
import com.billcorea.jikgong.presentation.company.main.money.shared.CompanyMoneySharedEvent
import com.billcorea.jikgong.presentation.company.main.money.shared.CompanyMoneySharedViewModel
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

    // 네비게이션 이벤트 처리
    LaunchedEffect(shouldNavigateToDetail) {
        shouldNavigateToDetail?.let { paymentId ->
            // 지급 상세 화면으로 이동
            // navigator.navigate(PaymentDetailScreenDestination(paymentId))
            viewModel.clearNavigationEvents()
        }
    }

    LaunchedEffect(shouldNavigateToWorkerDetail) {
        shouldNavigateToWorkerDetail?.let { workerId ->
            // 근로자 상세 화면으로 이동
            // navigator.navigate(WorkerDetailScreenDestination(workerId))
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

    Column(
        modifier = modifier.fillMaxSize()
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
                // 멀티 선택 모드 토글
                IconButton(
                    onClick = {
                        viewModel.onEvent(CompanyMoneySharedEvent.ToggleMultiSelectMode)
                    }
                ) {
                    Icon(
                        imageVector = if (uiState.isMultiSelectMode) {
                            Icons.Default.CheckCircle
                        } else {
                            Icons.Default.Checklist
                        },
                        contentDescription = "다중 선택",
                        tint = if (uiState.isMultiSelectMode) {
                            appColorScheme.primary
                        } else {
                            appColorScheme.onSurface
                        }
                    )
                }

                // 필터
                IconButton(
                    onClick = {
                        viewModel.onEvent(CompanyMoneySharedEvent.ShowFilterDialog)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "필터",
                        tint = if (uiState.hasActiveFilters) {
                            appColorScheme.primary
                        } else {
                            appColorScheme.onSurface
                        }
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = appColorScheme.surface,
                titleContentColor = appColorScheme.onSurface
            )
        )

        // 요약 정보 카드
        uiState.summary?.let { summary ->
            UrgentPaymentCard(
                urgentCount = summary.urgentPaymentsCount,
                totalPendingAmount = summary.totalPendingAmount,
                onViewUrgentPayments = {
                    viewModel.onEvent(CompanyMoneySharedEvent.FilterByStatus(PaymentStatus.URGENT))
                }
            )
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
        when (uiState.selectedTabIndex) {
            0 -> PaymentListContent(
                uiState = uiState,
                viewModel = viewModel,
                modifier = Modifier.fillMaxSize()
            )
            1 -> PaymentCalendarContent(
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
    if (urgentCount > 0) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFEBEE) // 연한 빨간색 배경
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
            CircularProgressIndicator()
        }
    } else if (uiState.filteredPayments.isEmpty()) {
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
                    text = if (uiState.hasActiveFilters) {
                        "필터 조건에 맞는 지급 내역이 없습니다"
                    } else {
                        "지급할 임금이 없습니다"
                    },
                    style = AppTypography.titleMedium,
                    color = appColorScheme.onSurfaceVariant
                )

                Text(
                    text = if (uiState.hasActiveFilters) {
                        "필터를 변경하거나 초기화해보세요"
                    } else {
                        "프로젝트 완료 후 임금을 지급하세요"
                    },
                    style = AppTypography.bodyMedium,
                    color = appColorScheme.onSurfaceVariant
                )

                if (uiState.hasActiveFilters) {
                    Spacer(modifier = Modifier.height(16.dp))
                    TextButton(
                        onClick = {
                            viewModel.onEvent(CompanyMoneySharedEvent.ClearFilters)
                        }
                    ) {
                        Text("필터 초기화")
                    }
                }
            }
        }
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

@Preview(showBackground = true)
@Composable
fun CompanyMoneyScreenPreview() {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme {
        CompanyMoneyScreen(
            navigator = navigator,
            viewModel = CompanyMoneySharedViewModel()
        )
    }
}