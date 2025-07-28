package com.billcorea.jikgong.presentation.company.main.projectlist.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.presentation.PageIndicator
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectRegistrationTopBar(
    currentPage: Int,
    totalPages: Int,
    hasDraft: Boolean = false,
    lastSavedTime: String? = null,
    isSaving: Boolean = false,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "프로젝트 등록",
                    style = AppTypography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = appColorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(4.dp))

                // 페이지 인디케이터
                PageIndicator(
                    numberOfPages = totalPages,
                    selectedPage = currentPage - 1,
                    defaultRadius = 6.dp,
                    selectedLength = 16.dp,
                    space = 4.dp,
                    animationDurationInMillis = 300
                )

                // 임시저장 상태 표시
                if (hasDraft || lastSavedTime != null) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (isSaving) "저장 중..."
                        else lastSavedTime?.let { "임시저장됨 ($it)" } ?: "임시저장됨",
                        style = AppTypography.labelSmall,
                        color = appColorScheme.primary
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "뒤로가기",
                    tint = appColorScheme.onSurface
                )
            }
        },
        actions = {
            // 임시저장 버튼
            TextButton(
                onClick = onSaveClick,
                enabled = !isSaving
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = appColorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = "임시저장",
                    tint = appColorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (isSaving) "저장중" else "임시저장",
                    style = AppTypography.labelMedium,
                    color = appColorScheme.primary
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

@Preview(showBackground = true)
@Composable
fun ProjectRegistrationTopBarPreview() {
    Jikgong1111Theme {
        Column {
            ProjectRegistrationTopBar(
                currentPage = 1,
                totalPages = 3,
                hasDraft = false,
                lastSavedTime = null,
                isSaving = false,
                onBackClick = {},
                onSaveClick = {}
            )

            ProjectRegistrationTopBar(
                currentPage = 2,
                totalPages = 3,
                hasDraft = true,
                lastSavedTime = "14:25",
                isSaving = false,
                onBackClick = {},
                onSaveClick = {}
            )

            ProjectRegistrationTopBar(
                currentPage = 3,
                totalPages = 3,
                hasDraft = true,
                lastSavedTime = null,
                isSaving = true,
                onBackClick = {},
                onSaveClick = {}
            )
        }
    }
}