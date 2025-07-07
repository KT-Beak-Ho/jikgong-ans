package com.billcorea.jikgong.presentation.company.auth.main.projectlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.company.auth.main.common.components.CompanyBottomNavTabs
import com.billcorea.jikgong.presentation.company.auth.main.common.components.CompanyBottomNavigation
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

// 프로젝트 데이터 클래스
data class ProjectItem(
    val id: String,
    val title: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val status: String = "진행중"
)

// 색상 정의
private val DarkBlue = Color(0xFF2E3A59)
private val LightGray = Color(0xFFF8F9FA)
private val MediumGray = Color(0xFF8B95A1)
private val DarkGray = Color(0xFF343A46)
private val BlueHighlight = Color(0xFF5BA7F7)

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun CompanyProjectListScreen(
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier
) {
    // 프로젝트 상태에 따라 데이터 설정 (테스트용)
    CompanyProjectListScreenContent(
        navigator = navigator,
        hasProjects = false, // true로 변경하면 프로젝트가 있는 상태
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CompanyProjectListScreenContent(
    navigator: DestinationsNavigator,
    hasProjects: Boolean,
    modifier: Modifier = Modifier
) {
    // 프로젝트 데이터
    val projects = if (hasProjects) {
        listOf(
            ProjectItem(
                id = "1",
                title = "사하구 낙동5블럭 낙동강 온도 측정 센터 신축공사",
                startDate = LocalDate.of(2024, 1, 25),
                endDate = LocalDate.of(2026, 1, 25)
            ),
            ProjectItem(
                id = "2",
                title = "직공센터 공사",
                startDate = LocalDate.of(2024, 1, 25),
                endDate = LocalDate.of(2026, 1, 25)
            )
        )
    } else {
        emptyList()
    }

    var currentRoute by remember { mutableStateOf(CompanyBottomNavTabs.PROJECT_LIST.route) }

    // 통계 계산
    val inProgressCount = projects.size
    val scheduledCount = 0
    val completedCount = 0

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkBlue)
            ) {
                // 상단 타이틀 바
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "직직직!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // 직공 TIP 버튼
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color.Gray.copy(alpha = 0.3f)
                        ) {
                            Text(
                                text = "직공 TIP",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                            )
                        }

                        // 알림 버튼
                        IconButton(
                            onClick = { /* TODO: 알림 화면으로 이동 */ },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "알림",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }

                // 프로젝트 목록 타이틀
                Text(
                    text = "프로젝트 목록",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

                // 통계 탭들
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatTab(
                        title = "진행중",
                        count = inProgressCount,
                        isSelected = true
                    )
                    StatTab(
                        title = "예정",
                        count = scheduledCount,
                        isSelected = false
                    )
                    StatTab(
                        title = "완료",
                        count = completedCount,
                        isSelected = false
                    )
                }
            }
        },
        bottomBar = {
            CompanyBottomNavigation(
                currentRoute = currentRoute,
                onTabSelected = { route ->
                    currentRoute = route
                }
            )
        },
        floatingActionButton = {
            if (hasProjects) {
                ExtendedFloatingActionButton(
                    onClick = {
                        // TODO: 프로젝트 생성 화면으로 이동
                    },
                    containerColor = DarkBlue,
                    contentColor = Color.White,
                    modifier = Modifier.padding(bottom = 80.dp, end = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "추가",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "프로젝트 등록",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(LightGray)
        ) {
            // 메인 컨텐츠 영역
            if (projects.isEmpty()) {
                // 빈 상태
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(40.dp)
                    ) {
                        Text(
                            text = "프로젝트 등록 후",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = MediumGray,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "일자리를 관리해보세요.",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = MediumGray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(bottom = 40.dp)
                        )

                        // 프로젝트 등록 버튼
                        Button(
                            onClick = {
                                // TODO: 프로젝트 생성 화면으로 이동
                            },
                            modifier = Modifier
                                .width(200.dp)
                                .height(48.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DarkBlue
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "추가",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "프로젝트 등록",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                            )
                        }
                    }
                }
            } else {
                // 프로젝트 리스트
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(projects) { project ->
                        ProjectCard(
                            project = project,
                            onProjectClick = {
                                // TODO: 프로젝트 상세 화면으로 이동
                            }
                        )
                    }

                    // 하단 여백 추가
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun StatTab(
    title: String,
    count: Int,
    isSelected: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal,
            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.6f)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = count.toString(),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) BlueHighlight else Color.White.copy(alpha = 0.4f)
        )
        if (isSelected) {
            Spacer(modifier = Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .width(28.dp)
                    .height(2.dp)
                    .background(
                        color = BlueHighlight,
                        shape = RoundedCornerShape(1.dp)
                    )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProjectCard(
    project: ProjectItem,
    onProjectClick: (ProjectItem) -> Unit
) {
    Card(
        onClick = { onProjectClick(project) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.5.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // 프로젝트 제목과 더보기 버튼
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = project.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    modifier = Modifier.weight(1f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 20.sp
                )

                IconButton(
                    onClick = { /* TODO: 프로젝트 옵션 메뉴 */ },
                    modifier = Modifier
                        .size(20.dp)
                        .padding(0.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "더보기",
                        tint = Color.Gray,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 날짜 정보
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "착공일",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray
                    )
                    Text(
                        text = formatDate(project.startDate),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "준공일",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Gray
                    )
                    Text(
                        text = formatDate(project.endDate),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 관리 버튼
            Button(
                onClick = { onProjectClick(project) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(6.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF5F5F5),
                    contentColor = Color.Black
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 0.dp
                )
            ) {
                Text(
                    text = "관리",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }
}

// 날짜 포맷 함수
private fun formatDate(date: LocalDate): String {
    return try {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd (E)", Locale.KOREAN)
        date.format(formatter)
    } catch (e: Exception) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        "${date.format(formatter)} (${getDayOfWeek(date.dayOfWeek.value)})"
    }
}

private fun getDayOfWeek(dayOfWeek: Int): String {
    return when (dayOfWeek) {
        1 -> "월"
        2 -> "화"
        3 -> "수"
        4 -> "목"
        5 -> "금"
        6 -> "토"
        7 -> "일"
        else -> ""
    }
}

@Preview(name = "프로젝트 있음", showBackground = true, heightDp = 800)
@Composable
fun CompanyProjectListScreenWithProjectsPreview() {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme {
        CompanyProjectListScreenContent(
            navigator = navigator,
            hasProjects = true
        )
    }
}

@Preview(name = "프로젝트 없음", showBackground = true, heightDp = 800)
@Composable
fun CompanyProjectListScreenEmptyPreview() {
    val navController = rememberNavController()
    val navigator = navController.toDestinationsNavigator()

    Jikgong1111Theme {
        CompanyProjectListScreenContent(
            navigator = navigator,
            hasProjects = false
        )
    }
}