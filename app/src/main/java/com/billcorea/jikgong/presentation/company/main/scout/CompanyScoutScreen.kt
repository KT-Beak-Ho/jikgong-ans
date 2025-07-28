package com.billcorea.jikgong.presentation.company.main.scout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyScoutScreen(
    navigator: DestinationsNavigator,
    showBottomBar: Boolean = false,
    modifier: Modifier = Modifier
) {
    var searchRadius by remember { mutableStateOf(10) }
    var selectedJobType by remember { mutableStateOf("전체") }
    var experienceFilter by remember { mutableStateOf("상관없음") }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "인력 스카웃",
                        style = AppTypography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = appColorScheme.onSurface
                    )
                },
                actions = {
                    IconButton(onClick = { /* 알림 */ }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "알림",
                            tint = appColorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = appColorScheme.surface
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    // 스카웃 제안서 작성 화면으로 이동
                },
                containerColor = appColorScheme.primary,
                contentColor = appColorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.PersonAdd,
                    contentDescription = "스카웃 제안"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("스카웃 제안")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 검색 필터 섹션
            item {
                SearchFilterSection(
                    searchRadius = searchRadius,
                    onSearchRadiusChange = { searchRadius = it },
                    selectedJobType = selectedJobType,
                    onJobTypeChange = { selectedJobType = it },
                    experienceFilter = experienceFilter,
                    onExperienceFilterChange = { experienceFilter = it }
                )
            }

            // 추천 인력 섹션
            item {
                Text(
                    text = "추천 인력",
                    style = AppTypography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = appColorScheme.onSurface
                )
            }

            // 인력 카드들 (샘플 데이터)
            items(5) { index ->
                WorkerCard(
                    workerName = "김철수 ${index + 1}",
                    experience = "${(index + 1) * 2}년",
                    location = "서울 강남구",
                    distance = "${(index + 1) * 2.5}km",
                    rating = 4.5f + (index * 0.1f),
                    skills = listOf("보통인부", "철근공", "콘크리트공").take(index + 1),
                    isOnline = index % 2 == 0,
                    onScoutClick = {
                        // 스카웃 제안 보내기
                    },
                    onProfileClick = {
                        // 프로필 상세보기
                    }
                )
            }

            // 더보기 버튼
            item {
                OutlinedButton(
                    onClick = { /* 더 많은 인력 보기 */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("더 많은 인력 보기")
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp)) // FAB 공간 확보
            }
        }
    }
}

@Composable
private fun SearchFilterSection(
    searchRadius: Int,
    onSearchRadiusChange: (Int) -> Unit,
    selectedJobType: String,
    onJobTypeChange: (String) -> Unit,
    experienceFilter: String,
    onExperienceFilterChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = appColorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "검색 조건",
                style = AppTypography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = appColorScheme.onSurface
            )

            // 반경 설정
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "검색 반경: ${searchRadius}km",
                    style = AppTypography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium
                    )
                )

                Slider(
                    value = searchRadius.toFloat(),
                    onValueChange = { onSearchRadiusChange(it.toInt()) },
                    valueRange = 1f..50f,
                    steps = 48,
                    colors = SliderDefaults.colors(
                        thumbColor = appColorScheme.primary,
                        activeTrackColor = appColorScheme.primary
                    )
                )
            }

            // 직종 필터
            FilterChipGroup(
                title = "직종",
                options = listOf("전체", "보통인부", "철근공", "콘크리트공", "용접공", "전기공"),
                selectedOption = selectedJobType,
                onOptionSelected = onJobTypeChange
            )

            // 경력 필터
            FilterChipGroup(
                title = "경력",
                options = listOf("상관없음", "신입", "1년 이상", "3년 이상", "5년 이상"),
                selectedOption = experienceFilter,
                onOptionSelected = onExperienceFilterChange
            )
        }
    }
}

@Composable
private fun FilterChipGroup(
    title: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            style = AppTypography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
            )
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            options.take(3).forEach { option ->
                FilterChip(
                    selected = selectedOption == option,
                    onClick = { onOptionSelected(option) },
                    label = { Text(option) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = appColorScheme.primary,
                        selectedLabelColor = appColorScheme.onPrimary
                    )
                )
            }
        }

        if (options.size > 3) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                options.drop(3).forEach { option ->
                    FilterChip(
                        selected = selectedOption == option,
                        onClick = { onOptionSelected(option) },
                        label = { Text(option) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = appColorScheme.primary,
                            selectedLabelColor = appColorScheme.onPrimary
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun WorkerCard(
    workerName: String,
    experience: String,
    location: String,
    distance: String,
    rating: Float,
    skills: List<String>,
    isOnline: Boolean,
    onScoutClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onProfileClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = appColorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // 헤더
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = workerName,
                            style = AppTypography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = appColorScheme.onSurface
                        )

                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = if (isOnline) Color(0xFF4CAF50) else Color(0xFF9E9E9E)
                        ) {
                            Text(
                                text = if (isOnline) "온라인" else "오프라인",
                                style = AppTypography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Work,
                            contentDescription = null,
                            tint = appColorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = experience,
                            style = AppTypography.bodyMedium,
                            color = appColorScheme.onSurfaceVariant
                        )
                    }
                }

                // 평점
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "평점",
                        tint = Color(0xFFFFB300),
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = String.format("%.1f", rating),
                        style = AppTypography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = appColorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 위치 정보
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "위치",
                    tint = appColorScheme.onSurfaceVariant,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "$location • $distance",
                    style = AppTypography.bodyMedium,
                    color = appColorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 스킬 태그들
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                skills.forEach { skill ->
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = appColorScheme.primaryContainer.copy(alpha = 0.3f)
                    ) {
                        Text(
                            text = skill,
                            style = AppTypography.labelMedium,
                            color = appColorScheme.primary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 액션 버튼
            Button(
                onClick = onScoutClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = appColorScheme.primary,
                    contentColor = appColorScheme.onPrimary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.PersonAdd,
                    contentDescription = "스카웃",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "스카웃 제안",
                    style = AppTypography.labelLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CompanyScoutScreenPreview() {
    Jikgong1111Theme {
        // Preview는 navigator 없이 간단하게
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                "Scout Screen Preview",
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}