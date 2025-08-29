package com.billcorea.jikgong.presentation.company.main.info.screen

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.company.main.common.BackNavigationTopBar
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.ramcosta.composedestinations.annotation.Destination

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun TermsAndPoliciesScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            BackNavigationTopBar(
                title = "약관 및 정책",
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 필수 약관
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "📋 필수 약관",
                            style = AppTypography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        TermsMenuItem(
                            icon = Icons.Default.Gavel,
                            title = "서비스 이용약관",
                            subtitle = "직공 서비스 이용에 관한 약관",
                            lastUpdated = "2025.08.15",
                            onClick = { /* TODO: Navigate to terms detail */ }
                        )
                        
                        TermsMenuItem(
                            icon = Icons.Default.Security,
                            title = "개인정보처리방침",
                            subtitle = "개인정보 수집, 이용, 보관에 관한 정책",
                            lastUpdated = "2025.08.15",
                            isImportant = true,
                            onClick = { /* TODO: Navigate to privacy policy */ }
                        )
                        
                        TermsMenuItem(
                            icon = Icons.Default.Work,
                            title = "근로계약 약관",
                            subtitle = "근로자와 기업 간의 계약에 관한 약관",
                            lastUpdated = "2025.07.01",
                            onClick = { /* TODO: Navigate to employment terms */ }
                        )
                    }
                }
            }
            
            // 추가 정책
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "📝 추가 정책",
                            style = AppTypography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        TermsMenuItem(
                            icon = Icons.Default.Payment,
                            title = "결제 및 환불 정책",
                            subtitle = "결제 수단, 환불 절차에 관한 정책",
                            lastUpdated = "2025.06.01",
                            onClick = { /* TODO: Navigate to payment policy */ }
                        )
                        
                        TermsMenuItem(
                            icon = Icons.Default.Campaign,
                            title = "마케팅 정보 수신 동의",
                            subtitle = "이벤트, 프로모션 정보 제공 동의",
                            lastUpdated = "2025.05.15",
                            onClick = { /* TODO: Navigate to marketing consent */ }
                        )
                        
                        TermsMenuItem(
                            icon = Icons.Default.LocationOn,
                            title = "위치정보 이용약관",
                            subtitle = "위치기반 서비스 이용에 관한 약관",
                            lastUpdated = "2025.04.01",
                            onClick = { /* TODO: Navigate to location terms */ }
                        )
                    }
                }
            }
            
            // 제3자 서비스
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "🔗 제3자 서비스",
                            style = AppTypography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        TermsMenuItem(
                            icon = Icons.Default.Analytics,
                            title = "데이터 분석 서비스",
                            subtitle = "Google Analytics, Firebase 등",
                            lastUpdated = "2025.03.01",
                            onClick = { /* TODO: Navigate to analytics terms */ }
                        )
                        
                        TermsMenuItem(
                            icon = Icons.Default.Notifications,
                            title = "푸시 알림 서비스",
                            subtitle = "FCM(Firebase Cloud Messaging)",
                            lastUpdated = "2025.03.01",
                            onClick = { /* TODO: Navigate to push terms */ }
                        )
                    }
                }
            }
            
            // 약관 동의 현황
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF8F9FA)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "✅ 내 동의 현황",
                            style = AppTypography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        ConsentStatusItem("서비스 이용약관", true, true)
                        ConsentStatusItem("개인정보처리방침", true, true)
                        ConsentStatusItem("근로계약 약관", true, true)
                        ConsentStatusItem("마케팅 정보 수신", false, false)
                        ConsentStatusItem("위치정보 이용", true, false)
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        OutlinedButton(
                            onClick = { /* TODO: Navigate to consent management */ },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = appColorScheme.primary
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ManageAccounts,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("동의 관리하기")
                        }
                    }
                }
            }
            
            // 법적 고지
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFF3E0)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "⚖️ 법적 고지",
                            style = AppTypography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        Text(
                            text = "• 본 약관은 대한민국 법령에 따라 해석됩니다\n• 약관 변경 시 7일 전 사전 고지됩니다\n• 중요 약관 변경 시 별도 동의를 받습니다\n• 분쟁 발생 시 서울중앙지방법원이 관할법원입니다",
                            style = AppTypography.bodySmall,
                            color = Color(0xFF8D6E63),
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TermsMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    lastUpdated: String,
    isImportant: Boolean = false,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isImportant) Color(0xFFFF5722) else appColorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        style = AppTypography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                    
                    if (isImportant) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFFF5722)
                        ) {
                            Text(
                                text = "중요",
                                color = Color.White,
                                style = AppTypography.bodySmall,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                
                Text(
                    text = subtitle,
                    style = AppTypography.bodySmall,
                    color = Color.Gray
                )
                
                Text(
                    text = "최종 업데이트: $lastUpdated",
                    style = AppTypography.bodySmall,
                    color = Color.Gray,
                    fontSize = 10.sp
                )
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun ConsentStatusItem(
    title: String,
    isAgreed: Boolean,
    isRequired: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = AppTypography.bodyMedium
            )
            
            if (isRequired) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "(필수)",
                    style = AppTypography.bodySmall,
                    color = Color(0xFFFF5722),
                    fontSize = 10.sp
                )
            }
        }
        
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (isAgreed) Color(0xFF4CAF50) else Color(0xFF9E9E9E)
        ) {
            Text(
                text = if (isAgreed) "동의" else "미동의",
                color = Color.White,
                style = AppTypography.bodySmall,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TermsAndPoliciesScreenPreview() {
    Jikgong1111Theme {
        TermsAndPoliciesScreen(
            navController = rememberNavController()
        )
    }
}