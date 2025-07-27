package com.billcorea.jikgong.presentation.company.main.projectlist.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.appColorScheme

@Composable
fun EmptyProjectState(
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
                    imageVector = Icons.Default.Assignment,
                    contentDescription = "프로젝트",
                    modifier = Modifier
                        .size(60.dp)
                        .padding(30.dp),
                    tint = appColorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 메인 메시지
            Text(
                text = "등록된 프로젝트가 없습니다",
                style = AppTypography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = appColorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 부제목
            Text(
                text = "첫 프로젝트를 등록하고\n효율적인 인력 관리를 시작해보세요",
                style = AppTypography.bodyLarge,
                color = appColorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            // 혜택 카드들
            BenefitCardsSection()

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
                    defaultElevation = 6.dp
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
private fun BenefitCardsSection(
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

        // 빠른 인력 모집 혜택 카드 (메인)
        MainBenefitCard(
            title = "24시간 빠른 모집",
            subtitle = "실시간 매칭 시스템",
            description = "평균 6시간 내 모집 완료",
            highlight = "모집 속도 300% 향상",
            color = appColorScheme.primary
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 스마트 관리 혜택 카드
            BenefitCard(
                icon = Icons.Default.Speed,
                title = "스마트 관리",
                description = "AI 기반 자동 매칭",
                highlight = "관리 효율성 증대",
                color = Color(0xFF9C27B0),
                modifier = Modifier.weight(1f)
            )

            // 안전한 결제 혜택 카드
            BenefitCard(
                icon = Icons.Default.Security,
                title = "안전한 결제",
                description = "에스크로 결제 시스템",
                highlight = "100% 안전 보장",
                color = Color(0xFF4CAF50),
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
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.08f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
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
                    imageVector = Icons.Default.Bolt,
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

            // 24시간 태그
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = color
            ) {
                Text(
                    text = "24H",
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