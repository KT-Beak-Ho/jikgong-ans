package com.billcorea.jikgong.presentation.company.main.money.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme

@Composable
fun EmptyMoneyState(
    onCreateProjectClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            // 메인 아이콘
            Surface(
                modifier = Modifier.size(120.dp),
                shape = RoundedCornerShape(60.dp),
                color = appColorScheme.primaryContainer.copy(alpha = 0.3f)
            ) {
                Icon(
                    imageVector = Icons.Default.AttachMoney,
                    contentDescription = "임금 관리",
                    modifier = Modifier
                        .size(60.dp)
                        .padding(30.dp),
                    tint = appColorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 메인 메시지
            Text(
                text = "아직 임금 지급 내역이 없습니다",
                style = AppTypography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = appColorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 부제목
            Text(
                text = "프로젝트를 등록하고\n스마트한 임금 관리를 시작해보세요",
                style = AppTypography.bodyLarge,
                color = appColorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = AppTypography.bodyLarge.lineHeight * 1.4
            )

            Spacer(modifier = Modifier.height(40.dp))

            // 직직직 혜택 카드들
            JikgongBenefitCardsSection()

            Spacer(modifier = Modifier.height(40.dp))

            // 프로젝트 등록 버튼
            Button(
                onClick = onCreateProjectClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = appColorScheme.primary,
                    contentColor = appColorScheme.onPrimary
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 6.dp,
                    pressedElevation = 12.dp
                )
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "프로젝트 추가",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "첫 프로젝트 등록하기",
                        style = AppTypography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 도움말 텍스트
            Text(
                text = "프로젝트 등록 후 바로 인력 모집을 시작할 수 있습니다",
                style = AppTypography.bodySmall,
                color = appColorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun JikgongBenefitCardsSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 혜택 제목
        Text(
            text = "직직직 플랫폼만의 특별 혜택",
            style = AppTypography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = appColorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 수수료 절감 혜택 카드 (메인)
        MainBenefitCard(
            title = "수수료 50% 절감",
            subtitle = "기존 인력사무소 대비",
            description = "월 평균 30만원 절약 효과",
            highlight = "기업 부담 5%로 감소",
            color = Color(0xFF4CAF50),
            isMain = true
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 신속 지급 혜택 카드
            BenefitCard(
                icon = Icons.Default.Schedule,
                title = "신속 임금 지급",
                description = "업무일 기준 2일 이내",
                highlight = "당일 지급 시스템",
                color = Color(0xFF2196F3),
                modifier = Modifier.weight(1f)
            )

            // 스마트 관리 혜택 카드
            BenefitCard(
                icon = Icons.Default.Security,
                title = "스마트 관리",
                description = "AI 기반 자동 계산",
                highlight = "효율성 300% 향상",
                color = Color(0xFF9C27B0),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun MainBenefitCard(
    title: String,
    subtitle: String,
    description: String,
    highlight: String,
    color: Color,
    isMain: Boolean = false,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.08f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isMain) 4.dp else 2.dp
        )
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 아이콘
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = color
            ) {
                Icon(
                    imageVector = Icons.Default.TrendingUp,
                    contentDescription = title,
                    tint = Color.White,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(8.dp)
                )
            }

            // 텍스트 정보
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = AppTypography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = color
                )

                Text(
                    text = subtitle,
                    style = AppTypography.bodySmall,
                    color = appColorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = description,
                    style = AppTypography.bodyMedium,
                    color = appColorScheme.onSurface
                )

                Text(
                    text = highlight,
                    style = AppTypography.labelMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = color
                )
            }

            // 50% 절감 태그
            if (isMain) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = color
                ) {
                    Text(
                        text = "50% 절감",
                        style = AppTypography.labelMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun BenefitCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    highlight: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.06f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 아이콘
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = color
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = Color.White,
                    modifier = Modifier
                        .size(32.dp)
                        .padding(6.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 텍스트 정보
            Text(
                text = title,
                style = AppTypography.titleSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = color,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = description,
                style = AppTypography.bodySmall,
                color = appColorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = highlight,
                style = AppTypography.labelSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = color,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
fun EmptyMoneyStatePreview() {
    Jikgong1111Theme {
        EmptyMoneyState(
            onCreateProjectClick = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}