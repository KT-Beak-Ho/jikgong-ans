package com.billcorea.jikgong.presentation.company.main.info.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun MyInfoScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            BackNavigationTopBar(
                title = "내 정보",
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
            // 프로필 카드
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = appColorScheme.primaryContainer.copy(alpha = 0.1f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // 프로필 이미지
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Surface(
                                modifier = Modifier.fillMaxSize(),
                                shape = CircleShape,
                                color = appColorScheme.primary.copy(alpha = 0.2f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Business,
                                    contentDescription = null,
                                    modifier = Modifier.size(40.dp),
                                    tint = appColorScheme.primary
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = "김직공건설",
                            style = AppTypography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Text(
                            text = "기업회원 • 프리미엄",
                            style = AppTypography.bodyMedium,
                            color = appColorScheme.primary
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        OutlinedButton(
                            onClick = { /* TODO: Edit profile */ },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = appColorScheme.primary
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("프로필 수정")
                        }
                    }
                }
            }
            
            // 기본 정보
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
                            text = "기본 정보",
                            style = AppTypography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        InfoMenuItem(
                            icon = Icons.Default.Business,
                            title = "회사명",
                            value = "김직공건설",
                            onClick = { /* TODO: Edit company name */ }
                        )
                        
                        InfoMenuItem(
                            icon = Icons.Default.Person,
                            title = "대표자명",
                            value = "김직공",
                            onClick = { /* TODO: Edit representative name */ }
                        )
                        
                        InfoMenuItem(
                            icon = Icons.Default.Phone,
                            title = "연락처",
                            value = "010-1234-5678",
                            onClick = { /* TODO: Edit phone */ }
                        )
                        
                        InfoMenuItem(
                            icon = Icons.Default.Email,
                            title = "이메일",
                            value = "info@jikgong.com",
                            onClick = { /* TODO: Edit email */ }
                        )
                        
                        InfoMenuItem(
                            icon = Icons.Default.LocationOn,
                            title = "주소",
                            value = "서울시 강남구 테헤란로 123",
                            onClick = { /* TODO: Edit address */ }
                        )
                    }
                }
            }
            
            // 사업자 정보
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
                            text = "사업자 정보",
                            style = AppTypography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        InfoMenuItem(
                            icon = Icons.Default.Numbers,
                            title = "사업자등록번호",
                            value = "123-45-67890",
                            isEditable = false
                        )
                        
                        InfoMenuItem(
                            icon = Icons.Default.Category,
                            title = "업종",
                            value = "건설업",
                            onClick = { /* TODO: Edit business type */ }
                        )
                        
                        InfoMenuItem(
                            icon = Icons.Default.CalendarMonth,
                            title = "가입일",
                            value = "2024.03.15",
                            isEditable = false
                        )
                        
                        InfoMenuItem(
                            icon = Icons.Default.Verified,
                            title = "인증상태",
                            value = "인증완료",
                            valueColor = Color(0xFF4CAF50),
                            isEditable = false
                        )
                    }
                }
            }
            
            // 계정 관리
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
                            text = "계정 관리",
                            style = AppTypography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        AccountMenuItem(
                            icon = Icons.Default.Lock,
                            title = "비밀번호 변경",
                            onClick = { /* TODO: Change password */ }
                        )
                        
                        AccountMenuItem(
                            icon = Icons.Default.Logout,
                            title = "로그아웃",
                            onClick = { /* TODO: Logout */ }
                        )
                        
                        AccountMenuItem(
                            icon = Icons.Default.DeleteForever,
                            title = "회원탈퇴",
                            textColor = Color(0xFFFF5722),
                            onClick = { /* TODO: Delete account */ }
                        )
                    }
                }
            }
            
            // 앱 설정
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
                            text = "앱 설정",
                            style = AppTypography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "데이터 절약 모드",
                                    style = AppTypography.bodyMedium
                                )
                                Text(
                                    text = "이미지 화질을 낮춰 데이터를 절약합니다",
                                    style = AppTypography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                            
                            var dataSaveMode by remember { mutableStateOf(false) }
                            Switch(
                                checked = dataSaveMode,
                                onCheckedChange = { dataSaveMode = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color.White,
                                    checkedTrackColor = appColorScheme.primary
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoMenuItem(
    icon: ImageVector,
    title: String,
    value: String,
    valueColor: Color = Color.Black,
    isEditable: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    val modifier = if (isEditable && onClick != null) {
        Modifier.fillMaxWidth()
    } else {
        Modifier.fillMaxWidth()
    }
    
    if (isEditable && onClick != null) {
        Surface(
            modifier = modifier,
            onClick = onClick,
            color = Color.Transparent
        ) {
            InfoMenuItemContent(icon, title, value, valueColor, isEditable)
        }
    } else {
        InfoMenuItemContent(icon, title, value, valueColor, isEditable)
    }
}

@Composable
private fun InfoMenuItemContent(
    icon: ImageVector,
    title: String,
    value: String,
    valueColor: Color,
    isEditable: Boolean
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
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = AppTypography.bodyMedium,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = AppTypography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = valueColor
            )
        }
        
        if (isEditable) {
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
private fun AccountMenuItem(
    icon: ImageVector,
    title: String,
    textColor: Color = Color.Black,
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
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Text(
                text = title,
                style = AppTypography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = textColor,
                modifier = Modifier.weight(1f)
            )
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyInfoScreenPreview() {
    Jikgong1111Theme {
        MyInfoScreen(
            navController = rememberNavController()
        )
    }
}