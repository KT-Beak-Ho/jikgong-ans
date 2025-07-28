package com.billcorea.jikgong.presentation.company.main.projectlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.company.main.projectlist.components.EmptyProjectRegistrationState
import com.billcorea.jikgong.presentation.company.main.projectlist.components.ProjectCard
import com.billcorea.jikgong.presentation.company.main.projectlist.components.ProjectFilterTabs
import com.billcorea.jikgong.presentation.company.main.projectlist.data.ProjectData
import com.billcorea.jikgong.presentation.company.main.projectlist.data.ProjectStatus
import com.billcorea.jikgong.presentation.destinations.ProjectRegistrationScreenDestination
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import java.time.LocalDate
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyProjectListScreen(
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier,
    showBottomBar: Boolean = true
) {
    // 샘플 데이터 직접 정의 (빈 상태 테스트를 위해 변경 가능)
    val hasData = false // true로 변경하면 데이터 있는 상태

    val projects = remember {
        if (hasData) {
            val today = LocalDate.now()
            listOf(
                ProjectData(
                    id = "project1",
                    title = "사하구 낙동5블럭 낙동강 온도 측정 센터 신축공사",
                    description = "친환경 온도 측정 센터 건립을 위한 신축 공사입니다.",
                    location = "부산 사하구",
                    detailAddress = "부산광역시 사하구 낙동대로 123",
                    distance = 2.5,
                    jobTypes = listOf(),
                    totalWorkers = 15,
                    completedWorkers = 2,
                    dailyWage = 130000,
                    startDate = today.plusDays(3),
                    endDate = today.plusDays(25),
                    startTime = "08:00",
                    endTime = "17:00",
                    status = ProjectStatus.RECRUITING,
                    isUrgent = true,
                    requirements = listOf("안전화 필수", "작업복 착용"),
                    providedItems = listOf("중식 제공", "교통비 지급"),
                    notes = "신축 공사로 깔끔한 작업 환경입니다.",
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now()
                ),
                ProjectData(
                    id = "project2",
                    title = "직공센터 공사",
                    description = "현대적인 업무 시설 구축을 위한 인테리어 공사",
                    location = "인천 연수구",
                    detailAddress = "인천광역시 연수구 컨벤시아대로 456",
                    distance = 12.8,
                    jobTypes = listOf(),
                    totalWorkers = 8,
                    completedWorkers = 8,
                    dailyWage = 140000,
                    startDate = today.minusDays(10),
                    endDate = today.minusDays(1),
                    startTime = "09:00",
                    endTime = "18:00",
                    status = ProjectStatus.COMPLETED,
                    isUrgent = false,
                    requirements = listOf("인테리어 경험 우대"),
                    providedItems = listOf("중식 제공"),
                    notes = "완료된 프로젝트입니다.",
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now()
                )
            )
        } else {
            emptyList()
        }
    }

    var selectedStatus by remember { mutableStateOf<ProjectStatus?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    // 필터링된 프로젝트
    val filteredProjects = remember(projects, selectedStatus) {
        if (selectedStatus == null) {
            projects
        } else {
            projects.filter { it.status == selectedStatus }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            ProjectListTopBar(
                onSearchClick = {
                    // TODO: 검색 기능 구현
                },
                onFilterClick = {
                    // TODO: 필터 기능 구현
                }
            )
        },
        floatingActionButton = {
            ProjectCreateFAB(
                onClick = {
                    navigator.navigate(ProjectRegistrationScreenDestination)
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (isLoading) {
                // 로딩 상태
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (projects.isEmpty()) {
                // 빈 상태
                EmptyProjectRegistrationState(
                    onCreateProjectClick = {
                        navigator.navigate(ProjectRegistrationScreenDestination)
                    },
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                // 프로젝트 목록 표시
                ProjectListContent(
                    projects = filteredProjects,
                    selectedStatus = selectedStatus,
                    onStatusSelected = { status ->
                        selectedStatus = status
                    },
                    onProjectClick = { project ->
                        // TODO: 프로젝트 상세 화면으로 이동
                    },
                    onProjectAction = { project, action ->
                        when (action) {
                            "edit" -> {
                                // TODO: 프로젝트 편집 화면으로 이동
                            }
                            "delete" -> {
                                // TODO: 프로젝트 삭제 처리
                            }
                            "recruit" -> {
                                // TODO: 인력 모집 화면으로 이동
                            }
                        }
                    },
                    showBottomBar = showBottomBar,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProjectListTopBar(
    onSearchClick: () -> Unit,
    onFilterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = "프로젝트 목록",
                style = AppTypography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = appColorScheme.onSurface
            )
        },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "검색",
                    tint = appColorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onFilterClick) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "필터",
                    tint = appColorScheme.onSurfaceVariant
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = appColorScheme.surface,
            titleContentColor = appColorScheme.onSurface
        ),
        modifier = modifier
    )
}

@Composable
private fun ProjectCreateFAB(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        containerColor = appColorScheme.primary,
        contentColor = appColorScheme.onPrimary,
        shape = CircleShape
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "프로젝트 등록",
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun ProjectListContent(
    projects: List<ProjectData>,
    selectedStatus: ProjectStatus?,
    onStatusSelected: (ProjectStatus?) -> Unit,
    onProjectClick: (ProjectData) -> Unit,
    onProjectAction: (ProjectData, String) -> Unit,
    showBottomBar: Boolean,
    modifier: Modifier = Modifier
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
                    top = 8.dp
                ),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(
                bottom = if (showBottomBar) 100.dp else 20.dp
            )
        ) {
            // 필터 탭
            item {
                ProjectFilterTabs(
                    selectedStatus = selectedStatus,
                    onStatusSelected = onStatusSelected,
                    projectCounts = projects.groupingBy { it.status }.eachCount()
                )
            }

            // 헤더 정보
            item {
                ProjectListHeader(
                    totalCount = projects.size,
                    selectedStatus = selectedStatus
                )
            }

            // 프로젝트 카드 목록
            items(
                items = projects,
                key = { it.id }
            ) { project ->
                ProjectCard(
                    project = project,
                    onProjectClick = onProjectClick,
                    onProjectAction = onProjectAction
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
                        top = 20.dp,
                        bottom = if (showBottomBar) 120.dp else 40.dp
                    )
            )
        }
    }
}

@Composable
private fun ProjectListHeader(
    totalCount: Int,
    selectedStatus: ProjectStatus?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = when (selectedStatus) {
                null -> "전체 프로젝트"
                ProjectStatus.RECRUITING -> "모집중인 프로젝트"
                ProjectStatus.IN_PROGRESS -> "진행중인 프로젝트"
                ProjectStatus.COMPLETED -> "완료된 프로젝트"
                ProjectStatus.CANCELLED -> "취소된 프로젝트"
            },
            style = AppTypography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = appColorScheme.onSurface
        )

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = appColorScheme.primaryContainer.copy(alpha = 0.7f)
        ) {
            Text(
                text = "총 ${totalCount}건",
                style = AppTypography.labelMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = appColorScheme.primary,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
    }
}

@Composable
private fun ScrollBar(
    scrollProgress: Float,
    modifier: Modifier = Modifier,
    isVisible: Boolean = true
) {
    if (isVisible) {
        Box(
            modifier = modifier
                .background(
                    color = appColorScheme.outline.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(4.dp)
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.2f)
                    .offset(y = (scrollProgress * 80).dp)
                    .background(
                        color = appColorScheme.primary.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        }
    }
}

// 임시 데이터 모델들 (실제로는 별도 파일에 있어야 함)
data class ProjectData(
    val id: String,
    val title: String,
    val description: String,
    val location: String,
    val detailAddress: String,
    val distance: Double,
    val jobTypes: List<String>,
    val totalWorkers: Int,
    val completedWorkers: Int,
    val dailyWage: Long,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val startTime: String,
    val endTime: String,
    val status: ProjectStatus,
    val isUrgent: Boolean,
    val requirements: List<String>,
    val providedItems: List<String>,
    val notes: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

enum class ProjectStatus {
    RECRUITING,    // 모집중
    IN_PROGRESS,   // 진행중
    COMPLETED,     // 완료
    CANCELLED      // 취소
}

// 임시 컴포넌트들 (실제로는 별도 파일에 있어야 함)
@Composable
fun ProjectCard(
    project: ProjectData,
    onProjectClick: (ProjectData) -> Unit,
    onProjectAction: (ProjectData, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = { onProjectClick(project) }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = project.title,
                style = AppTypography.titleMedium,
                color = appColorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = project.location,
                style = AppTypography.bodyMedium,
                color = appColorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ProjectFilterTabs(
    selectedStatus: ProjectStatus?,
    onStatusSelected: (ProjectStatus?) -> Unit,
    projectCounts: Map<ProjectStatus, Int>,
    modifier: Modifier = Modifier
) {
    // 임시 구현
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilterChip(
            onClick = { onStatusSelected(null) },
            label = { Text("전체") },
            selected = selectedStatus == null
        )
        ProjectStatus.values().forEach { status ->
            FilterChip(
                onClick = { onStatusSelected(status) },
                label = { Text(status.name) },
                selected = selectedStatus == status
            )
        }
    }
}

@Preview(name = "빈 상태", showBackground = true, heightDp = 800)
@Composable
fun CompanyProjectListScreenEmptyPreview() {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme {
        CompanyProjectListScreen(
            navigator = navigator,
            showBottomBar = false
        )
    }
}

@Preview(name = "데이터 있음", showBackground = true, heightDp = 800)
@Composable
fun CompanyProjectListScreenWithDataPreview() {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme {
        // hasData를 true로 설정한 별도 컴포저블이 필요하지만
        // 여기서는 간단히 표시
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("데이터가 있는 상태는 hasData = true로 변경해서 확인")
        }
    }
}