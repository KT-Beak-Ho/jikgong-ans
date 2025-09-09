package com.billcorea.jikgong.presentation.company.main.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.offset
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompanyTopBar(
    title: String,
    modifier: Modifier = Modifier,
    showBackButton: Boolean = false,
    showSearchButton: Boolean = false,
    onBackClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = AppTypography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = appColorScheme.onSurface
            )
        },
        modifier = modifier.fillMaxWidth(),
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "뒤로가기",
                        tint = appColorScheme.onSurface
                    )
                }
            }
        },
        actions = {
            if (showSearchButton) {
                IconButton(onClick = onSearchClick) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "검색",
                        tint = appColorScheme.onSurface
                    )
                }
            }
            actions()
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            titleContentColor = appColorScheme.onSurface,
            navigationIconContentColor = appColorScheme.onSurface,
            actionIconContentColor = appColorScheme.onSurface
        )
    )
}

// 기본적인 상단바 (제목만)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleCompanyTopBar(
    title: String,
    modifier: Modifier = Modifier
) {
    CompanyTopBar(
        title = title,
        modifier = modifier
    )
}

// 뒤로가기 버튼이 있는 상단바
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackNavigationTopBar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CompanyTopBar(
        title = title,
        showBackButton = true,
        onBackClick = onBackClick,
        modifier = modifier
    )
}

// 검색 버튼이 있는 상단바
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchableTopBar(
    title: String,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CompanyTopBar(
        title = title,
        showSearchButton = true,
        onSearchClick = onSearchClick,
        modifier = modifier
    )
}

// 스카우트 전용 상단바
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScoutTopBar(
    title: String,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = AppTypography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = appColorScheme.onSurface
            )
        },
        modifier = modifier.fillMaxWidth(),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            titleContentColor = appColorScheme.onSurface,
            actionIconContentColor = appColorScheme.onSurface
        )
    )
}

// 내정보 전용 상단바 (알림 포함)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoTopBar(
    title: String,
    modifier: Modifier = Modifier,
    notificationCount: Int = 0,
    onNotificationClick: () -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = AppTypography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = appColorScheme.onSurface
            )
        },
        modifier = modifier.fillMaxWidth(),
        actions = {
            // 알림 버튼 (알림 개수 뱃지 포함)
            Box {
                IconButton(onClick = onNotificationClick) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "알림",
                        tint = appColorScheme.onSurface
                    )
                }
                if (notificationCount > 0) {
                    Badge(
                        modifier = Modifier
                            .offset(x = (-8).dp, y = 8.dp)
                            .size(18.dp),
                        containerColor = Color(0xFFFF5252),
                        contentColor = Color.White
                    ) {
                        Text(
                            text = if (notificationCount > 99) "99+" else notificationCount.toString(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            titleContentColor = appColorScheme.onSurface,
            actionIconContentColor = appColorScheme.onSurface
        )
    )
}

@Preview(showBackground = true)
@Composable
fun CompanyTopBarPreview() {
    Jikgong1111Theme {
        Column {
            SimpleCompanyTopBar(title = "프로젝트 목록")
            Spacer(modifier = Modifier.height(16.dp))
            BackNavigationTopBar(
                title = "출근확정 근로자 정보",
                onBackClick = {}
            )
            Spacer(modifier = Modifier.height(16.dp))
            SearchableTopBar(
                title = "임금 관리",
                onSearchClick = {}
            )
            Spacer(modifier = Modifier.height(16.dp))
            ScoutTopBar(
                title = "스카우트"
            )
            Spacer(modifier = Modifier.height(16.dp))
            InfoTopBar(
                title = "내 정보",
                notificationCount = 3,
                onNotificationClick = {}
            )
        }
    }
}