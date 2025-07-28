package com.billcorea.jikgong.presentation.company.main.projectlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.billcorea.jikgong.presentation.company.main.projectlist.components.EmptyProjectRegistrationState
import com.billcorea.jikgong.presentation.company.main.projectlist.components.ProjectCard
import com.billcorea.jikgong.presentation.company.main.projectlist.components.ProjectData
import com.billcorea.jikgong.presentation.company.main.projectlist.components.ProjectStatus
import com.billcorea.jikgong.presentation.company.main.projectlist.shared.ProjectRegistrationSharedViewModel
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectListScreen(
    navigator: DestinationsNavigator,
    showBottomBar: Boolean = false,
    projectRegistrationViewModel: ProjectRegistrationSharedViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf(ProjectFilter.ALL) }
    var showFilterSheet by remember { mutableStateOf(false) }

    // 샘플 데이터
    val sampleProjects = remember { getSampleProjectList() }

    // 필터링된 프로젝트 목록
    val filteredProjects = remember(sampleProjects, selectedFilter, searchQuery) {
        sampleProjects.filter { project ->
            val matchesFilter = when (selectedFilter) {
                ProjectFilter.ALL -> true
                ProjectFilter.RECRUITING -> project.status == ProjectStatus.RECRUITING
                ProjectFilter.IN_PROGRESS -> project.status == ProjectStatus.IN_PROGRESS
                ProjectFilter.COMPLETED -> project.status == ProjectStatus.COMPLETED
            }
            val matchesSearch = if (searchQuery.isBlank()) {
                true
            } else {
                project.title.contains(searchQuery, ignoreCase = true) ||
                        project.location.contains(searchQuery, ignoreCase = true)
            }
            matchesFilter && matchesSearch
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            ProjectListTopBar(
                searchQuery = searchQuery,
                onSearchQueryChange = { searchQuery = it },
                onFilterClick = { showFilterSheet = true }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // 프로젝트 등록 화면으로 이동
                    // navigator.navigate("project_registration")
                },
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
        },
        containerColor = appColorScheme.surface
    ) { paddingValues ->
        if (filteredProjects.isEmpty() && searchQuery.isBlank() && selectedFilter == ProjectFilter.ALL) {
            // 빈 상태 - 처음 사용자
            EmptyProjectRegistrationState(
                onCreateProjectClick = {
                    // 프로젝트 등록 화면으로 이동
                    // navigator.navigate("project_registration")
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // 필터 칩들
                FilterChips(
                    selectedFilter = selectedFilter,
                    onFilterSelected = { selectedFilter = it },
                    projectCounts = getProjectCounts(sampleProjects),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

                if (filteredProjects.isEmpty()) {
                    // 필터링 결과가 없음
                    EmptyFilterResultState(
                        searchQuery = searchQuery,
                        selectedFilter = selectedFilter,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    )
                } else {
                    // 프로젝트 목록
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            ProjectListHeader(
                                totalCount = filteredProjects.size,
                                selectedFilter = selectedFilter
                            )
                        }

                        items(filteredProjects) { project ->
                            ProjectCard(
                                project = project,
                                onProjectClick = { clickedProject ->
                                    // 프로젝트 상세 화면으로 이동
                                },
                                onProjectAction = { actionProject, action ->
                                    when (action) {
                                        "edit" -> {
                                            // 프로젝트 수정 화면으로 이동
                                        }
                                        "recruit" -> {
                                            // 인력 모집 화면으로 이동
                                        }
                                    }
                                }
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(80.dp)) // FAB 공간 확보
                        }
                    }
                }
            }
        }
    }

    // 필터 바텀 시트
    if (showFilterSheet) {
        FilterBottomSheet(
            selectedFilter = selectedFilter,
            onFilterSelected = {
                selectedFilter = it
                showFilterSheet = false
            },
            onDismiss = { showFilterSheet = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProjectListTopBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onFilterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = "프로젝트 목록",
                style = AppTypography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = appColorScheme.onSurface
            )
        },
        actions = {
            // 검색 아이콘 버튼
            IconButton(onClick = { /* 검색 기능 확장 */ }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "검색",
                    tint = appColorScheme.onSurface
                )
            }
            // 필터 아이콘 버튼
            IconButton(onClick = onFilterClick) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "필터",
                    tint = appColorScheme.onSurface
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
private fun FilterChips(
    selectedFilter: ProjectFilter,
    onFilterSelected: (ProjectFilter) -> Unit,
    projectCounts: Map<ProjectFilter, Int>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ProjectFilter.values().forEach { filter ->
            val isSelected = selectedFilter == filter
            val count = projectCounts[filter] ?: 0

            FilterChip(
                selected = isSelected,
                onClick = { onFilterSelected(filter) },
                label = {
                    Text(
                        text = "${filter.displayName} ($count)",
                        style = AppTypography.labelMedium.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = appColorScheme.primary,
                    selectedLabelColor = appColorScheme.onPrimary,
                    containerColor = appColorScheme.surfaceVariant,
                    labelColor = appColorScheme.onSurfaceVariant
                )
            )
        }
    }
}

@Composable
private fun ProjectListHeader(
    totalCount: Int,
    selectedFilter: ProjectFilter,
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
                    text = "${selectedFilter.displayName} 프로젝트",
                    style = AppTypography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = appColorScheme.onSurface
                )
                Text(
                    text = "총 ${totalCount}개",
                    style = AppTypography.bodyMedium,
                    color = appColorScheme.onSurfaceVariant
                )
            }

            Surface(
                shape = CircleShape,
                color = appColorScheme.primary.copy(alpha = 0.1f)
            ) {
                Text(
                    text = totalCount.toString(),
                    style = AppTypography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = appColorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun EmptyFilterResultState(
    searchQuery: String,
    selectedFilter: ProjectFilter,
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
            Surface(
                modifier = Modifier.size(80.dp),
                shape = CircleShape,
                color = appColorScheme.surfaceVariant.copy(alpha = 0.5f)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(20.dp),
                    tint = appColorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (searchQuery.isNotBlank()) {
                    "'$searchQuery'에 대한 검색 결과가 없습니다"
                } else {
                    "${selectedFilter.displayName} 프로젝트가 없습니다"
                },
                style = AppTypography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = appColorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "다른 조건으로 검색해보세요",
                style = AppTypography.bodyMedium,
                color = appColorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterBottomSheet(
    selectedFilter: ProjectFilter,
    onFilterSelected: (ProjectFilter) -> Unit,
    onDismiss: () -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = bottomSheetState,
        containerColor = appColorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "필터 선택",
                style = AppTypography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            ProjectFilter.values().forEach { filter ->
                val isSelected = selectedFilter == filter

                Card(
                    onClick = { onFilterSelected(filter) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) {
                            appColorScheme.primaryContainer
                        } else {
                            appColorScheme.surface
                        }
                    ),
                    border = if (isSelected) {
                        null
                    } else {
                        androidx.compose.foundation.BorderStroke(
                            1.dp,
                            appColorScheme.outline.copy(alpha = 0.3f)
                        )
                    }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = filter.displayName,
                            style = AppTypography.bodyLarge.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            ),
                            color = if (isSelected) {
                                appColorScheme.onPrimaryContainer
                            } else {
                                appColorScheme.onSurface
                            }
                        )

                        if (isSelected) {
                            Surface(
                                shape = CircleShape,
                                color = appColorScheme.primary
                            ) {
                                Box(
                                    modifier = Modifier.size(20.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add, // 체크 아이콘 대신 사용
                                        contentDescription = "선택됨",
                                        tint = appColorScheme.onPrimary,
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// 필터 열거형
enum class ProjectFilter(val displayName: String) {
    ALL("전체"),
    RECRUITING("모집중"),
    IN_PROGRESS("진행중"),
    COMPLETED("완료")
}

// 샘플 데이터 생성 함수
private fun getSampleProjectList(): List<ProjectData> {
    return listOf(
        ProjectData(
            id = "1",
            title = "강남구 아파트 신축 공사",
            description = "고급 아파트 신축을 위한 건설 프로젝트입니다.",
            location = "서울 강남구",
            detailAddress = "테헤란로 123",
            distance = 2.5,
            jobTypes = listOf("보통인부", "철근공"),
            totalWorkers = 15,
            completedWorkers = 3,
            dailyWage = 150000L,
            startDate = LocalDate.now().plusDays(3),
            endDate = LocalDate.now().plusDays(30),
            startTime = "08:00",
            endTime = "17:00",
            status = ProjectStatus.RECRUITING,
            isUrgent = true,
            requirements = listOf("안전화 필수"),
            providedItems = listOf("중식 제공"),
            notes = "깔끔한 작업 환경",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        ),
        ProjectData(
            id = "2",
            title = "부산 교량 보수 공사",
            description = "교량 안전성 강화를 위한 보수 작업",
            location = "부산 해운대구",
            detailAddress = "해운대로 456",
            distance = 15.2,
            jobTypes = listOf("용접공"),
            totalWorkers = 8,
            completedWorkers = 8,
            dailyWage = 180000L,
            startDate = LocalDate.now().minusDays(20),
            endDate = LocalDate.now().minusDays(1),
            startTime = "08:00",
            endTime = "17:00",
            status = ProjectStatus.COMPLETED,
            isUrgent = false,
            requirements = listOf(),
            providedItems = listOf(),
            notes = "",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        ),
        ProjectData(
            id = "3",
            title = "인천 물류센터 건설",
            description = "대형 물류센터 신축 공사",
            location = "인천 연수구",
            detailAddress = "컨벤시아대로 123",
            distance = 25.0,
            jobTypes = listOf("보통인부", "콘크리트공"),
            totalWorkers = 30,
            completedWorkers = 12,
            dailyWage = 140000L,
            startDate = LocalDate.now().minusDays(5),
            endDate = LocalDate.now().plusDays(45),
            startTime = "07:30",
            endTime = "17:30",
            status = ProjectStatus.IN_PROGRESS,
            isUrgent = false,
            requirements = listOf("경력 1년 이상"),
            providedItems = listOf("중식 제공", "교통비 지급"),
            notes = "장기 근무 가능자 우대",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        ),
        ProjectData(
            id = "4",
            title = "대전 공장 리모델링",
            description = "기존 공장 시설 개보수 작업",
            location = "대전 유성구",
            detailAddress = "대덕대로 789",
            distance = 120.0,
            jobTypes = listOf("전기공", "배관공"),
            totalWorkers = 12,
            completedWorkers = 0,
            dailyWage = 160000L,
            startDate = LocalDate.now().plusDays(10),
            endDate = LocalDate.now().plusDays(40),
            startTime = "08:30",
            endTime = "17:00",
            status = ProjectStatus.RECRUITING,
            isUrgent = false,
            requirements = listOf("관련 자격증 필수"),
            providedItems = listOf("중식 제공", "숙박 제공"),
            notes = "지방 근무 가능자만",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
    )
}

// 프로젝트 개수 계산 함수
private fun getProjectCounts(projects: List<ProjectData>): Map<ProjectFilter, Int> {
    return mapOf(
        ProjectFilter.ALL to projects.size,
        ProjectFilter.RECRUITING to projects.count { it.status == ProjectStatus.RECRUITING },
        ProjectFilter.IN_PROGRESS to projects.count { it.status == ProjectStatus.IN_PROGRESS },
        ProjectFilter.COMPLETED to projects.count { it.status == ProjectStatus.COMPLETED }
    )
}

@Preview(showBackground = true)
@Composable
fun ProjectListScreenPreview() {
    Jikgong1111Theme {
        // Preview는 navigator 없이 간단하게
        Box(modifier = Modifier.fillMaxSize()) {
            Text("Project List Screen Preview")
        }
    }
}