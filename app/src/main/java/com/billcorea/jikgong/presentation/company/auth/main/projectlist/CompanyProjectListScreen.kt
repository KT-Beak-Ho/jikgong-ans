package com.billcorea.jikgong.presentation.company.auth.main.projectlist



import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.R
import com.billcorea.jikgong.presentation.company.auth.main.common.components.CompanyBottomNavTabs
import com.billcorea.jikgong.presentation.company.auth.main.common.components.CompanyBottomNavigation
import com.billcorea.jikgong.presentation.company.auth.main.projectlist.components.ProjectCard
import com.billcorea.jikgong.presentation.company.auth.main.projectlist.components.ProjectCreateButton
import com.billcorea.jikgong.presentation.company.auth.main.projectlist.data.ProjectData
import com.billcorea.jikgong.presentation.company.auth.main.projectlist.data.ProjectSampleData
import com.billcorea.jikgong.presentation.company.auth.main.projectlist.data.ProjectStatus
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun CompanyProjectListScreen(
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier
) {
    // 프로젝트 목록 상태 (실제로는 ViewModel에서 관리해야 함)
    var projects by remember { mutableStateOf(ProjectSampleData.getSampleProjects()) }
    var currentRoute by remember { mutableStateOf(CompanyBottomNavTabs.PROJECT_LIST.route) }
    var searchQuery by remember { mutableStateOf("") }
    var showFilterDialog by remember { mutableStateOf(false) }
    var selectedStatusFilter by remember { mutableStateOf<ProjectStatus?>(null) }

    // 필터링된 프로젝트 목록
    val filteredProjects = remember(projects, searchQuery, selectedStatusFilter) {
        projects.filter { project ->
            val matchesSearch = if (searchQuery.isBlank()) {
                true
            } else {
                project.title.contains(searchQuery, ignoreCase = true) ||
                        project.location.contains(searchQuery, ignoreCase = true)
            }

            val matchesStatus = selectedStatusFilter?.let { status ->
                project.status == status
            } ?: true

            matchesSearch && matchesStatus
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "프로젝트 관리",
                        style = AppTypography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                actions = {
                    // 검색 버튼
                    IconButton(
                        onClick = { /* TODO: 검색 화면으로 이동 */ }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "검색"
                        )
                    }

                    // 필터 버튼
                    IconButton(
                        onClick = { showFilterDialog = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "필터"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = appColorScheme.surface,
                    titleContentColor = appColorScheme.onSurface
                )
            )
        },
        bottomBar = {
            CompanyBottomNavigation(
                currentRoute = currentRoute,
                onTabSelected = { route ->
                    currentRoute = route
                    // TODO: 실제 네비게이션 구현
                    when (route) {
                        CompanyBottomNavTabs.SCOUT.route -> {
                            // 스카웃 화면으로 이동
                        }
                        CompanyBottomNavTabs.MONEY.route -> {
                            // 임금관리 화면으로 이동
                        }
                        CompanyBottomNavTabs.INFO.route -> {
                            // 정보 화면으로 이동
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // 프로젝트 생성 버튼
            ProjectCreateButton(
                onCreateProject = {
                    // TODO: 프로젝트 생성 화면으로 이동
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 프로젝트 목록 헤더
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "내 프로젝트",
                    style = AppTypography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = appColorScheme.onSurface
                )

                Text(
                    text = "${filteredProjects.size}개",
                    style = AppTypography.bodyMedium,
                    color = appColorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 프로젝트 목록
            if (filteredProjects.isEmpty()) {
                // 빈 상태
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (projects.isEmpty()) {
                                "아직 등록된 프로젝트가 없습니다"
                            } else {
                                "검색 조건에 맞는 프로젝트가 없습니다"
                            },
                            style = AppTypography.bodyLarge,
                            color = appColorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )

                        if (projects.isEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "새 프로젝트를 생성해보세요",
                                style = AppTypography.bodyMedium,
                                color = appColorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                // 프로젝트 카드 목록
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = filteredProjects,
                        key = { it.id }
                    ) { project ->
                        ProjectCard(
                            project = project,
                            onProjectClick = { selectedProject ->
                                // TODO: 프로젝트 상세 화면으로 이동
                            }
                        )
                    }

                    // 마지막 아이템 하단 여백
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }

    // 필터 다이얼로그
    if (showFilterDialog) {
        AlertDialog(
            onDismissRequest = { showFilterDialog = false },
            title = {
                Text(
                    text = "프로젝트 필터",
                    style = AppTypography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            },
            text = {
                Column {
                    Text(
                        text = "상태별 필터",
                        style = AppTypography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // 전체 옵션
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedStatusFilter == null,
                            onClick = { selectedStatusFilter = null }
                        )
                        Text(
                            text = "전체",
                            style = AppTypography.bodyMedium,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    // 상태별 옵션
                    ProjectStatus.values().forEach { status ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedStatusFilter == status,
                                onClick = { selectedStatusFilter = status }
                            )
                            Text(
                                text = when (status) {
                                    ProjectStatus.PLANNING -> "계획중"
                                    ProjectStatus.RECRUITING -> "모집중"
                                    ProjectStatus.IN_PROGRESS -> "진행중"
                                    ProjectStatus.COMPLETED -> "완료"
                                    ProjectStatus.CANCELLED -> "취소"
                                },
                                style = AppTypography.bodyMedium,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showFilterDialog = false }
                ) {
                    Text("확인")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        selectedStatusFilter = null
                        showFilterDialog = false
                    }
                ) {
                    Text("초기화")
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CompanyProjectListScreenPreview() {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme {
        CompanyProjectListScreen(
            navigator = navigator
        )
    }
}