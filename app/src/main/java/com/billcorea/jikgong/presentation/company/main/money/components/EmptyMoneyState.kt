package com.billcorea.jikgong.presentation.company.main.money.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.TrendingDown
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
            modifier = Modifier.padding(horizontal = 40.dp)
        ) {
            // 빈 상태 아이콘
            Surface(
                modifier = Modifier.size(120.dp),
                shape = RoundedCornerShape(60.dp),
                color = appColorScheme.surfaceVariant.copy(alpha = 0.3f)
            ) {
                Icon(
                    imageVector = Icons.Default.AttachMoney,
                    contentDescription = "임금 관리",
                    modifier = Modifier
                        .size(60.dp)
                        .padding(30.dp),
                    tint = appColorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 메인 메시지
            Text(
                text = "아직 임금 지급 내역이 없습니다",
                style = AppTypography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = appColorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 부제목
            Text(
                text = "프로젝트를 등록하고\n작업자 임금을 관리해보세요",
                style = AppTypography.bodyLarge,
                color = appColorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = AppTypography.bodyLarge.lineHeight * 1.4
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 혜택 안내 카드
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF4CAF50).copy(alpha = 0.05f)
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.TrendingDown,
                            contentDescription = "혜택",
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "직직직 수수료 혜택",
                            style = AppTypography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color(0xFF4CAF50)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "기존 인력사무소 대비 50% 절감",
                        style = AppTypography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = appColorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "기존",
                                style = AppTypography.labelSmall,
                                color = appColorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "10%",
                                style = AppTypography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = appColorScheme.error
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.TrendingDown,
                            contentDescription = "화살표",
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(20.dp)
                        )

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "직직직",
                                style = AppTypography.labelSmall,
                                color = appColorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "5%",
                                style = AppTypography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color(0xFF4CAF50)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 프로젝트 생성 버튼
            Button(
                onClick = onCreateProjectClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = appColorScheme.primary,
                    contentColor = appColorScheme.onPrimary
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp
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
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "첫 프로젝트 등록하기",
                        style = AppTypography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EmptyMoneyStatePreview() {
    Jikgong1111Theme {
        EmptyMoneyState(
            onCreateProjectClick = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}