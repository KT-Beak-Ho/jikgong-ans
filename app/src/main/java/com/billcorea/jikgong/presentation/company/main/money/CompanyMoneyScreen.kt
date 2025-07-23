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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.company.main.money.components.PaymentCard
import com.billcorea.jikgong.presentation.company.main.money.data.PaymentStatus
import com.billcorea.jikgong.presentation.company.main.money.data.PaymentSampleData
import com.billcorea.jikgong.presentation.company.main.money.shared.CompanyMoneySharedEvent
import com.billcorea.jikgong.presentation.company.main.money.shared.CompanyMoneySharedViewModel
import com.billcorea.jikgong.presentation.company.main.money.shared.CompanyMoneySharedUiState
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.JikgongTheme
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

    CompanyMoneyScreenContent(
        uiState = uiState,
        viewModel = viewModel,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CompanyMoneyScreenContent(
    uiState: CompanyMoneySharedUiState,
    viewModel: CompanyMoneySharedViewModel? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // 상단바 - Figma 디자인에 맞게 수정
        TopAppBar(
            title = {
                Text(
                    text = "지급 내역",
                    style = AppTypography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    color = appColorScheme.onSurface
                )
            },
            actions = {
                // 검색 버튼
                IconButton(
                    onClick = {
                        // TODO: 검색 기능 구현
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "검색",
                        tint = appColorScheme.onSurface
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.White,
                titleContentColor = appColorScheme.onSurface
            )
        )

        // 긴급 지급 알림 카드 (48시간 이내 지급 필요)
        uiState.summary?.let { summary ->
            if (summary.urgentPaymentsCount > 0) {
                UrgentPaymentCard(
                    urgentCount = summary.urgentPaymentsCount,
                    totalPendingAmount = summary.totalPendingAmount,
                    onViewUrgentPayments = {
                        viewModel?.onEvent(CompanyMoneySharedEvent.FilterByStatus(PaymentStatus.URGENT))
                    }
                )
            }
        }

        // 메인 콘텐츠
        if (uiState.isLoading) {
            LoadingContent(modifier = Modifier.fillMaxSize())
        } else if (uiState.filteredPayments.isEmpty()) {
            EmptyContent(
                hasFilters = uiState.hasActiveFilters,
                onClearFilters = {
                    viewModel?.onEvent(CompanyMoneySharedEvent.ClearFilters)
                },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            PaymentListContent(
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
            containerColor = Color(0x1AFF5722) // 연한 주황색 배경
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                tint = Color(0xFFFF5722),
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "48시간 이내 지급 필요",
                    style = AppTypography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFF5722)
                    )
                )
                Text(
                    text = "${urgentCount}건 • ${NumberFormat.getNumberInstance(Locale.KOREA).format(totalPendingAmount)}원",
                    style = AppTypography.bodySmall.copy(
                        color = Color(0xFF757575)
                    )
                )
            }

            TextButton(
                onClick = onViewUrgentPayments,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color(0xFFFF5722)
                )
            ) {
                Text(
                    text = "확인하기",
                    style = AppTypography.labelMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Composable
private fun LoadingContent(
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
            CircularProgressIndicator(
                color = appColorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "지급 내역을 불러오는 중...",
                style = AppTypography.bodyMedium,
                color = appColorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun EmptyContent(
    hasFilters: Boolean,
    onClearFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(40.dp)
        ) {
            // Figma 디자인의 빈 상태 이미지 대신 아이콘 사용
            Icon(
                imageVector = Icons.Default.Payment,
                contentDescription = "지급 내역 없음",
                modifier = Modifier.size(64.dp),
                tint = Color(0xFFBDBDBD)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = if (hasFilters) {
                    "조건에 맞는 지급 내역이 없습니다"
                } else {
                    "0원"
                },
                style = AppTypography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                ),
                color = appColorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (hasFilters) {
                    "다른 조건으로 검색해보세요"
                } else {
                    "아직 지급 내역이 없습니다"
                },
                style = AppTypography.bodyLarge.copy(
                    fontSize = 16.sp
                ),
                color = Color(0xFF757575),
                textAlign = TextAlign.Center
            )

            if (hasFilters) {
                Spacer(modifier = Modifier.height(24.dp))
                OutlinedButton(
                    onClick = onClearFilters,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = appColorScheme.primary
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        width = 1.dp
                    )
                ) {
                    Text(
                        text = "전체 보기",
                        style = AppTypography.labelMedium.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun PaymentListContent(
    uiState: CompanyMoneySharedUiState,
    viewModel: CompanyMoneySharedViewModel?,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 지급 건수 헤더
        item {
            Text(
                text = "총 ${uiState.filteredPayments.size}건",
                style = AppTypography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = appColorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // 지급 목록
        items(
            items = uiState.filteredPayments,
            key = { it.id }
        ) { payment ->
            PaymentCard(
                payment = payment,
                isSelected = payment.id in uiState.selectedPaymentIds,
                isMultiSelectMode = uiState.isMultiSelectMode,
                onPaymentClick = {
                    viewModel?.onEvent(CompanyMoneySharedEvent.ShowPaymentDetail(it.id))
                },
                onToggleSelection = { paymentId ->
                    viewModel?.onEvent(CompanyMoneySharedEvent.TogglePaymentSelection(paymentId))
                },
                onProcessPayment = { paymentId ->
                    viewModel?.onEvent(CompanyMoneySharedEvent.ProcessSinglePayment(paymentId))
                },
                onMarkUrgent = { paymentId ->
                    viewModel?.onEvent(CompanyMoneySharedEvent.MarkAsUrgent(paymentId))
                }
            )
        }
    }
}

// 프리뷰들
@Preview(name = "데이터 있는 상태", showBackground = true)
@Composable
fun CompanyMoneyScreenWithDataPreview() {
    val sampleUiState = CompanyMoneySharedUiState(
        payments = PaymentSampleData.getSamplePayments(),
        filteredPayments = PaymentSampleData.getSamplePayments(),
        summary = PaymentSampleData.getSampleSummary(),
        isLoading = false
    )

    JikgongTheme {
        CompanyMoneyScreenContent(
            uiState = sampleUiState
        )
    }
}

@Preview(name = "빈 상태", showBackground = true)
@Composable
fun CompanyMoneyScreenEmptyPreview() {
    val emptyUiState = CompanyMoneySharedUiState(
        payments = emptyList(),
        filteredPayments = emptyList(),
        summary = PaymentSampleData.getEmptySummary(),
        isLoading = false
    )

    JikgongTheme {
        CompanyMoneyScreenContent(
            uiState = emptyUiState
        )
    }
}

@Preview(name = "로딩 상태", showBackground = true)
@Composable
fun CompanyMoneyScreenLoadingPreview() {
    val loadingUiState = CompanyMoneySharedUiState(
        payments = emptyList(),
        filteredPayments = emptyList(),
        isLoading = true
    )

    JikgongTheme {
        CompanyMoneyScreenContent(
            uiState = loadingUiState
        )
    }
}

@Preview(name = "긴급 알림", showBackground = true)
@Composable
fun UrgentPaymentCardPreview() {
    JikgongTheme {
        UrgentPaymentCard(
            urgentCount = 3,
            totalPendingAmount = 1293800L,
            onViewUrgentPayments = {}
        )
    }
}