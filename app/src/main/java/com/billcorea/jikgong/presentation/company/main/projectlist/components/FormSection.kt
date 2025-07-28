package com.billcorea.jikgong.presentation.company.main.projectlist.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Work

@Composable
fun FormSection(
    title: String,
    isRequired: Boolean = false,
    icon: ImageVector? = null,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = appColorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 섹션 헤더
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 아이콘
                if (icon != null) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = appColorScheme.primary.copy(alpha = 0.1f)
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = appColorScheme.primary,
                            modifier = Modifier
                                .size(32.dp)
                                .padding(6.dp)
                        )
                    }
                }

                // 제목
                Text(
                    text = title,
                    style = AppTypography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = appColorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )

                // 필수 표시
                if (isRequired) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFFEF5350).copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = "필수",
                            style = AppTypography.labelSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color(0xFFEF5350),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // 콘텐츠
            content()
        }
    }
}

@Composable
fun FormSectionSimple(
    title: String,
    isRequired: Boolean = false,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 제목 행
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = AppTypography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = appColorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )

            if (isRequired) {
                Text(
                    text = "*",
                    style = AppTypography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFFEF5350)
                )
            }
        }

        // 콘텐츠
        content()
    }
}

@Composable
fun InfoCard(
    title: String,
    subtitle: String? = null,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = appColorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    style = AppTypography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = appColorScheme.onSurface
                )

                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = AppTypography.bodySmall,
                        color = appColorScheme.onSurfaceVariant
                    )
                }
            }

            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FormSectionPreview() {
    Jikgong1111Theme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FormSection(
                title = "프로젝트 제목",
                isRequired = true,
                icon = Icons.Default.Work
            ) {
                OutlinedTextField(
                    value = "강남구 아파트 신축 공사",
                    onValueChange = {},
                    placeholder = { Text("프로젝트 제목을 입력해주세요") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            FormSectionSimple(
                title = "선택사항",
                isRequired = false
            ) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("선택사항 입력") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            InfoCard(
                title = "정보",
                subtitle = "추가 정보를 확인하세요"
            ) {
                Text(
                    text = "여기에 상세 정보가 표시됩니다.",
                    style = AppTypography.bodyMedium,
                    color = appColorScheme.onSurfaceVariant
                )
            }
        }
    }
}