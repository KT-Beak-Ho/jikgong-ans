// app/src/main/java/com/billcorea/jikgong/presentation/company/main/info/components/InfoCard.kt
package com.billcorea.jikgong.presentation.company.main.info.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.billcorea.jikgong.presentation.company.main.info.data.InfoMenuItem
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoCard(
    menuItem: InfoMenuItem,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = menuItem.onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = appColorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = menuItem.icon,
                contentDescription = menuItem.title,
                tint = appColorScheme.primary,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = menuItem.title,
                        style = AppTypography.titleMedium.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = appColorScheme.onSurface
                    )

                    menuItem.badge?.let { badge ->
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = appColorScheme.error
                        ) {
                            Text(
                                text = badge,
                                style = AppTypography.labelSmall,
                                color = appColorScheme.onError,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Text(
                    text = menuItem.subtitle,
                    style = AppTypography.bodySmall,
                    color = appColorScheme.onSurfaceVariant
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "이동",
                tint = appColorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InfoCardPreview() {
    Jikgong1111Theme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            InfoCard(
                menuItem = InfoMenuItem(
                    id = "test",
                    icon = Icons.Filled.Notifications,
                    title = "알림 설정",
                    subtitle = "푸시 알림, 이메일 알림 설정"
                )
            )

            InfoCard(
                menuItem = InfoMenuItem(
                    id = "test_badge",
                    icon = Icons.Filled.CalendarToday,
                    title = "이벤트",
                    subtitle = "진행 중인 이벤트 확인",
                    badge = "2"
                )
            )
        }
    }
}