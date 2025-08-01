// app/src/main/java/com/billcorea/jikgong/presentation/company/main/scout/CompanyScoutScreen.kt
package com.billcorea.jikgong.presentation.company.main.scout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.company.main.scout.components.WorkerCard
import com.billcorea.jikgong.presentation.company.main.scout.components.EmptyScoutState
import com.billcorea.jikgong.presentation.company.main.scout.components.SearchFilterCard
import com.billcorea.jikgong.presentation.company.main.scout.components.ScoutScrollBar
import com.billcorea.jikgong.presentation.company.main.scout.shared.CompanyScoutSharedEvent
import com.billcorea.jikgong.presentation.company.main.scout.shared.CompanyScoutSharedViewModel
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyScoutScreen(
    navigator: DestinationsNavigator,
    viewModel: CompanyScoutSharedViewModel = koinViewModel(),
    modifier: Modifier = Modifier,
    showBottomBar: Boolean = true
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // 에러 다이얼로그
    uiState.errorMessage?.let { message ->
        AlertDialog(
            onDismissRequest = {
                viewModel.onEvent(CompanyScoutSharedEvent.ClearError)
            },
            title = { Text("오류") },
            text = { Text(message) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.onEvent(CompanyScoutSharedEvent.ClearError)
                    }
                ) {
                    Text("확인")
                }
            }
        )
    }

    // 제안서 전송 다이얼로그
    if (uiState.showProposalDialog) {
        ProposalSendDialog(
            workerName = uiState.selectedWorkerName ?: "",
            onConfirm = { message ->
                viewModel.onEvent(CompanyScoutSharedEvent.ConfirmSendProposal(message))
            },
            onDismiss = {
                viewModel.onEvent(CompanyScoutSharedEvent.DismissProposalDialog)
            }
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "인력 스카웃",
                        style = AppTypography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                actions = {
                    // 검색 버튼
                    IconButton(
                        onClick = {
                            viewModel.onEvent(CompanyScoutSharedEvent.ToggleSearchMode)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "검색"
                        )
                    }

                    // 새로고침 버튼
                    IconButton(
                        onClick = {
                            viewModel.onEvent(CompanyScoutSharedEvent.RefreshData)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "새로고침"
                        )
                    }

                    // 알림 버튼
                    IconButton(
                        onClick = {
                            // 알림 화면으로 이동
                        }
                    ) {
                        BadgedBox(
                            badge = {
                                if (uiState.pendingProposalsCount > 0) {
                                    Badge {
                                        Text(
                                            text = uiState.pendingProposalsCount.toString(),
                                            style = AppTypography.labelSmall
                                        )
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "알림"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = appColorScheme.surface,
                    titleContentColor = appColorScheme.onSurface
                )
            )
        },
        floatingActionButton = {
            if (uiState.hasWorkers) {
                FloatingActionButton(
                    onClick = {
                        viewModel.onEvent(CompanyScoutSharedEvent.CreateBulkProposal)
                    },
                    containerColor = appColorScheme.primary,
                    contentColor = appColorScheme.onPrimary
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "일괄 제안"
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (!uiState.hasWorkers && !uiState.isLoading) {
                EmptyScoutState(
                    onSearchClick = {
                        viewModel.onEvent(CompanyScoutSharedEvent.StartSearch)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            } else {
                ScoutContentWithScrollbar(
                    uiState = uiState,
                    onEvent = viewModel::onEvent,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    showBottomBar = showBottomBar
                )
            }
        }
    }
}

@Composable
private fun ScoutContentWithScrollbar(
    uiState: com.billcorea.jikgong.presentation.company.main.scout.shared.CompanyScoutSharedUiState,
    onEvent: (CompanyScoutSharedEvent) -> Unit,
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

    Box(modifier = modifier) {
        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp,
                    end = if (canScroll) 28.dp else 16.dp,
                    top = 8.dp,
                    bottom = 8.dp
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(
                bottom = if (showBottomBar) 120.dp else 40.dp
            )
        ) {
            // 검색 필터
            item {
                SearchFilterCard(
                    searchFilters = uiState.searchFilters,
                    onFilterChange = { filters ->
                        onEvent(CompanyScoutSharedEvent.UpdateSearchFilters(filters))
                    }
                )
            }

            // 결과 헤더
            item {
                ScoutResultHeader(
                    totalCount = uiState.filteredWorkers.size,
                    searchFilters = uiState.searchFilters
                )
            }

            // 인력 목록
            items(
                items = uiState.filteredWorkers,
                key = { it.id }
            ) { worker ->
                WorkerCard(
                    worker = worker,
                    onScoutClick = { clickedWorker ->
                        onEvent(CompanyScoutSharedEvent.SendProposal(clickedWorker.id))
                    },
                    onFavoriteClick = { clickedWorker ->
                        onEvent(CompanyScoutSharedEvent.ToggleFavorite(clickedWorker.id))
                    },
                    onProfileClick = { clickedWorker ->
                        onEvent(CompanyScoutSharedEvent.ViewWorkerProfile(clickedWorker.id))
                    }
                )
            }

            // 하단 여백
            item {
                Spacer(modifier = Modifier.height(40.dp))
            }
        }

        // 스크롤바
        if (canScroll) {
            ScoutScrollBar(
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

@Composable
private fun ScoutResultHeader(
    totalCount: Int,
    searchFilters: com.billcorea.jikgong.presentation.company.main.scout.data.SearchFilters,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = appColorScheme.primaryContainer.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(12.dp)
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
                    text = "검색 결과",
                    style = AppTypography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = appColorScheme.onSurface
                )
                Text(
                    text = "총 ${totalCount}명의 인력",
                    style = AppTypography.bodyMedium,
                    color = appColorScheme.onSurfaceVariant
                )
            }

            if (searchFilters.hasActiveFilters) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = appColorScheme.primary.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = "필터 적용됨",
                        style = AppTypography.labelMedium.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = appColorScheme.primary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ProposalSendDialog(
    workerName: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var message by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("스카웃 제안")
        },
        text = {
            Column {
                Text("${workerName}님에게 스카웃 제안을 보내시겠습니까?")
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text("메시지 (선택사항)") },
                    placeholder = { Text("함께 일하고 싶습니다.") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(message) }
            ) {
                Text("전송")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}

@Preview(name = "기본 화면", showBackground = true, heightDp = 800)
@Composable
fun CompanyScoutScreenPreview() {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme {
        CompanyScoutScreen(
            navigator = navigator,
            showBottomBar = false
        )
    }
}

@Preview(name = "바텀바 포함", showBackground = true, heightDp = 800)
@Composable
fun CompanyScoutScreenWithBottomBarPreview() {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme {
        CompanyScoutScreen(
            navigator = navigator,
            showBottomBar = true
        )
    }
}

@Preview(name = "다크 테마", showBackground = true, heightDp = 800)
@Composable
fun CompanyScoutScreenDarkPreview() {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme(darkTheme = true) {
        CompanyScoutScreen(
            navigator = navigator,
            showBottomBar = false
        )
    }
}