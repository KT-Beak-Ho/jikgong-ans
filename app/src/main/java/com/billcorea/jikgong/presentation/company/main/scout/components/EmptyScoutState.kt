// app/src/main/java/com/billcorea/jikgong/presentation/company/main/scout/components/EmptyScoutState.kt
package com.billcorea.jikgong.presentation.company.main.scout.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme

@Composable
fun EmptyScoutState(
    onSearchClick: () -> Unit,
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
                    imageVector = Icons.Default.Search,
                    contentDescription = "인력 검색",
                    modifier = Modifier
                        .size(60.dp)
                        .padding(30.dp),
                    tint = appColorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 메인 메시지
            Text(
                text = "우수한 인력을 찾아보세요",
                style = AppTypography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = appColorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 부제목
            Text(
                text = "조건에 맞는 전문 인력을\n빠르고 쉽게 찾아드립니다",
                style = AppTypography.bodyLarge,
                color = appColorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = AppTypography.bodyLarge.lineHeight * 1.4
            )

            Spacer(modifier = Modifier.height(40.dp))

            // 직직직 스카웃 혜택 카드들
            ScoutBenefitCards()

            Spacer(modifier = Modifier.height(40.dp))

            // 검색 시작 버튼
            Button(
                onClick = onSearchClick,
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
                        imageVector = Icons.Default.Search,
                        contentDescription = "검색",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "인력 검색 시작",
                        style = AppTypography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 도움말 텍스트
            Text(
                text = "검색 조건을 설정하여 최적의 인력을 찾아보세요",
                style = AppTypography.bodySmall,
                color = appColorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ScoutBenefitCards(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 혜택 제목
        Text(
            text = "직직직 스카웃의 특별한 장점",
            style = AppTypography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = appColorScheme.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 빠른 매칭 혜택 카드 (메인)
        MainBenefitCard(
            title = "3배 빠른 인력 매칭",
            subtitle = "기존 방식 대비",
            description = "평균 30분 이내 응답",
            highlight = "즉시 연결 가능",
            color = Color(0xFF4CAF50),
            isMain = true
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 검증된 인력 혜택 카드
            BenefitCard(
                icon = Icons.Default.VerifiedUser,
                title = "검증된 인력",
                description = "평점 및 경력 확인",
                highlight = "신뢰성 보장",
                color = Color(0xFF2196F3),
                modifier = Modifier.weight(1f)
            )

            // 실시간 소통 혜택 카드
            BenefitCard(
                icon = Icons.Default.Chat,
                title = "실시간 소통",
                description = "즉시 메시지 교환",
                highlight = "빠른 협의",
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
                    imageVector = Icons.Default.Speed,
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

            // 3배 빠름 태그
            if (isMain) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = color
                ) {
                    Text(
                        text = "3배 빠름",
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
    icon: ImageVector,
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

@Preview(name = "기본 상태", showBackground = true, heightDp = 800)
@Composable
fun EmptyScoutStatePreview() {
    Jikgong1111Theme {
        EmptyScoutState(
            onSearchClick = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(name = "다크 테마", showBackground = true, heightDp = 800)
@Composable
fun EmptyScoutStateDarkPreview() {
    Jikgong1111Theme(darkTheme = true) {
        EmptyScoutState(
            onSearchClick = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(name = "컴팩트 화면", showBackground = true, heightDp = 600, widthDp = 300)
@Composable
fun EmptyScoutStateCompactPreview() {
    Jikgong1111Theme {
        EmptyScoutState(
            onSearchClick = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}