
package com.billcorea.jikgong.presentation.company.main.scout

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator

@Composable
fun CompanyScoutScreen(
    navigator: DestinationsNavigator,
    viewModel: CompanyScoutViewModel = viewModel(),
    modifier: Modifier = Modifier,
    showBottomBar: Boolean = true
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showScoutDialog by remember { mutableStateOf(false) }
    var selectedWorker by remember { mutableStateOf<Worker?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // 헤더 - 토스 스타일
        TossStyleScoutHeader()

        // 추천 인력 섹션
        RecommendedWorkersSection(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
        )

        // 필터 칩들
        WorkTypeFilterSection(
            selectedWorkType = uiState.selectedWorkType,
            onWorkTypeSelected = { workType ->
                viewModel.onEvent(CompanyScoutEvent.FilterByWorkType(workType))
            },
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        // 정렬 옵션
        SortSection(
            selectedSort = uiState.selectedSortType,
            onSortSelected = { sortType ->
                viewModel.onEvent(CompanyScoutEvent.SortWorkers(sortType))
            },
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        // 작업자 목록
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.filteredWorkers) { worker ->
                    TossStyleWorkerCard(
                        worker = worker,
                        onSaveClick = {
                            viewModel.onEvent(CompanyScoutEvent.SaveWorker(worker.id))
                        },
                        onScoutClick = {
                            selectedWorker = worker
                            showScoutDialog = true
                        },
                        onClick = {
                            viewModel.onEvent(CompanyScoutEvent.ShowWorkerDetail(worker.id))
                        }
                    )
                }
            }
        }
    }

    // 스카웃 메시지 다이얼로그
    if (showScoutDialog && selectedWorker != null) {
        TossStyleScoutDialog(
            worker = selectedWorker!!,
            onConfirm = { message ->
                viewModel.onEvent(
                    CompanyScoutEvent.SendScoutMessage(selectedWorker!!.id, message)
                )
                showScoutDialog = false
                selectedWorker = null
            },
            onDismiss = {
                showScoutDialog = false
                selectedWorker = null
            }
        )
    }
}

@Composable
private fun TossStyleScoutHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "필요한 인력을",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = "빠르게 찾아보세요",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // 검색 버튼
            Surface(
                onClick = { /* 검색 */ },
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(40.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "검색",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun RecommendedWorkersSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "추천 인력",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            TextButton(
                onClick = { /* 전체보기 */ }
            ) {
                Text(
                    text = "전체보기",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(3) { index ->
                RecommendedWorkerCard(
                    name = when(index) {
                        0 -> "김철수"
                        1 -> "박영희"
                        else -> "이민수"
                    },
                    workType = when(index) {
                        0 -> "철근공"
                        1 -> "타일공"
                        else -> "도배공"
                    },
                    rating = when(index) {
                        0 -> 4.8f
                        1 -> 4.9f
                        else -> 4.6f
                    },
                    distance = when(index) {
                        0 -> "2.5km"
                        1 -> "3.2km"
                        else -> "5.8km"
                    }
                )
            }
        }
    }
}

@Composable
private fun RecommendedWorkerCard(
    name: String,
    workType: String,
    rating: Float,
    distance: String
) {
    Card(
        modifier = Modifier.width(140.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 프로필 아바타
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "프로필",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = workType,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "평점",
                    modifier = Modifier.size(12.dp),
                    tint = Color(0xFFFFD700)
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "$rating",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = distance,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
private fun WorkTypeFilterSection(
    selectedWorkType: WorkType?,
    onWorkTypeSelected: (WorkType?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 전체 필터
        item {
            FilterChip(
                selected = selectedWorkType == null,
                onClick = { onWorkTypeSelected(null) },
                label = {
                    Text(
                        "전체",
                        fontWeight = if (selectedWorkType == null) FontWeight.SemiBold else FontWeight.Normal
                    )
                },
                shape = RoundedCornerShape(20.dp)
            )
        }

        // 작업 유형별 필터
        items(WorkType.values()) { workType ->
            FilterChip(
                selected = selectedWorkType == workType,
                onClick = { onWorkTypeSelected(workType) },
                label = {
                    Text(
                        workType.displayName,
                        fontWeight = if (selectedWorkType == workType) FontWeight.SemiBold else FontWeight.Normal
                    )
                },
                shape = RoundedCornerShape(20.dp)
            )
        }
    }
}

@Composable
private fun SortSection(
    selectedSort: WorkerSortType,
    onSortSelected: (WorkerSortType) -> Unit,
    modifier: Modifier = Modifier
) {
    var showSortMenu by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Box {
            OutlinedButton(
                onClick = { showSortMenu = true },
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = selectedSort.displayName,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "정렬",
                    modifier = Modifier.size(16.dp)
                )
            }

            DropdownMenu(
                expanded = showSortMenu,
                onDismissRequest = { showSortMenu = false }
            ) {
                WorkerSortType.values().forEach { sortType ->
                    DropdownMenuItem(
                        text = { Text(sortType.displayName) },
                        onClick = {
                            onSortSelected(sortType)
                            showSortMenu = false
                        },
                        leadingIcon = {
                            if (selectedSort == sortType) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TossStyleWorkerCard(
    worker: Worker,
    onSaveClick: () -> Unit,
    onScoutClick: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            // 상단: 프로필 정보와 저장 버튼
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 프로필 아바타
                    Surface(
                        modifier = Modifier.size(56.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "프로필",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = worker.name,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = "${worker.age}세",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "평점",
                                modifier = Modifier.size(16.dp),
                                tint = Color(0xFFFFD700)
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            Text(
                                text = "${worker.rating}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = "경력 ${worker.experienceYears}년",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                }

                // 저장 버튼
                IconButton(
                    onClick = onSaveClick
                ) {
                    Icon(
                        imageVector = if (worker.isSaved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = if (worker.isSaved) "저장됨" else "저장",
                        tint = if (worker.isSaved) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 작업 유형 태그들
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(worker.workTypes) { workType ->
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                    ) {
                        Text(
                            text = workType.displayName,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 위치와 거리
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "위치",
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "${worker.location} • ${worker.distanceText}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 소개
            Text(
                text = worker.introduction,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 하단: 상태와 스카웃 버튼
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (worker.isAvailable) Color(0xFF00C73C) else Color(0xFF8E8E93),
                            modifier = Modifier.size(8.dp)
                        ) {}

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = if (worker.isAvailable) "작업 가능" else "작업 중",
                            style = MaterialTheme.typography.labelMedium,
                            color = if (worker.isAvailable) Color(0xFF00C73C) else Color(0xFF8E8E93),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Text(
                        text = "완료 프로젝트 ${worker.completedProjects}개",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }

                // 스카웃 버튼
                Button(
                    onClick = onScoutClick,
                    enabled = worker.isAvailable,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "스카웃",
                        modifier = Modifier.size(16.dp)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = "스카웃하기",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun TossStyleScoutDialog(
    worker: Worker,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var message by remember {
        mutableStateOf("안녕하세요. 저희 프로젝트에 참여하실 의향이 있으시다면 연락 부탁드립니다.")
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = "${worker.name}님께",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "스카웃 제안을 보내시겠어요?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        },
        text = {
            Column {
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = message,
                    onValueChange = { message = it },
                    label = { Text("메시지") },
                    placeholder = { Text("메시지를 입력해주세요") },
                    maxLines = 4,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(message) },
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("전송하기", fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("취소")
            }
        },
        shape = RoundedCornerShape(20.dp)
    )
}

// Preview 컴포저블들
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

@Preview(name = "작업자 카드", showBackground = true)
@Composable
fun TossStyleWorkerCardPreview() {
    Jikgong1111Theme {
        TossStyleWorkerCard(
            worker = Worker(
                id = "1",
                name = "김철수",
                age = 35,
                location = "서울특별시 강남구",
                workTypes = listOf(WorkType.REINFORCEMENT, WorkType.FORMWORK),
                experienceYears = 8,
                rating = 4.8f,
                completedProjects = 142,
                profileImageUrl = null,
                introduction = "8년차 철근공입니다. 성실하고 안전하게 작업합니다.",
                skills = listOf("철근배근", "용접", "안전관리"),
                distance = 2.5f,
                isAvailable = true,
                isSaved = false,
                lastActiveDate = "2일 전"
            ),
            onSaveClick = {},
            onScoutClick = {},
            onClick = {}
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

// 데이터 모델들 (CompanyScoutViewModel에서 가져온 것과 동일)
enum class WorkType(val displayName: String) {
    REINFORCEMENT("철근공"),
    FORMWORK("거푸집공"),
    TILE("타일공"),
    PAINTING("도색공"),
    WALLPAPER("도배공"),
    INTERIOR("인테리어"),
    ELECTRICAL("전기공"),
    PLUMBING("배관공")
}

enum class WorkerSortType(val displayName: String) {
    RATING_HIGH("평점 높은 순"),
    RATING_LOW("평점 낮은 순"),
    EXPERIENCE_HIGH("경력 많은 순"),
    EXPERIENCE_LOW("경력 적은 순"),
    DISTANCE("거리 가까운 순")
}

data class Worker(
    val id: String,
    val name: String,
    val age: Int,
    val location: String,
    val workTypes: List<WorkType>,
    val experienceYears: Int,
    val rating: Float,
    val completedProjects: Int,
    val profileImageUrl: String?,
    val introduction: String,
    val skills: List<String>,
    val distance: Float, // km
    val isAvailable: Boolean,
    val isSaved: Boolean,
    val lastActiveDate: String
) {
    val workTypesText: String
        get() = workTypes.joinToString(", ") { it.displayName }

    val distanceText: String
        get() = "${String.format("%.1f", distance)}km"
}