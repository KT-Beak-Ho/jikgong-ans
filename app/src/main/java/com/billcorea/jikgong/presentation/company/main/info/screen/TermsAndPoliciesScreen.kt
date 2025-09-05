package com.billcorea.jikgong.presentation.company.main.info.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.company.main.info.data.TermsCategory
import com.billcorea.jikgong.presentation.company.main.info.data.TermsAndPoliciesContent
import com.billcorea.jikgong.presentation.company.main.info.popup.TermsCategoryDialog
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun TermsAndPoliciesScreen(
    navigator: DestinationsNavigator,
    navController: NavController
) {
    var selectedCategory by remember { mutableStateOf<TermsCategory?>(null) }
    
    // 선택된 카테고리가 있을 때 다이얼로그 표시
    selectedCategory?.let { category ->
        TermsCategoryDialog(
            category = category,
            onDismiss = { selectedCategory = null },
            onTermsClick = { /* 상세 다이얼로그는 TermsCategoryDialog 내부에서 처리 */ }
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "약관 및 정책",
                        style = AppTypography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFF1A1D29)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navigator.navigateUp() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "뒤로가기",
                            tint = Color(0xFF1A1D29)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 안내 메시지
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF0F9FF)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "약관 및 정책 안내",
                            style = AppTypography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color(0xFF0369A1)
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "직직직 플랫폼의 서비스 이용과 관련된 약관 및 정책을 확인하실 수 있습니다.\n각 항목을 선택하여 자세한 내용을 확인해 주세요.",
                            style = AppTypography.bodyMedium,
                            color = Color(0xFF0C4A6E),
                            lineHeight = AppTypography.bodyMedium.lineHeight * 1.4
                        )
                    }
                }
            }
            
            // 약관 카테고리 목록
            items(
                items = TermsAndPoliciesContent.allCategories,
                key = { it.id }
            ) { category ->
                TermsCategoryItem(
                    category = category,
                    onClick = { selectedCategory = category }
                )
            }
            
            // 하단 여백
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun TermsCategoryItem(
    category: TermsCategory,
    onClick: () -> Unit
) {
    OutlinedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Color(0xFFE5E7EB)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = category.title,
                        style = AppTypography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFF1A1D29)
                    )
                    
                    if (category.isRequired) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFEF4444)
                        ) {
                            Text(
                                text = "필수",
                                style = AppTypography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(6.dp))
                
                Text(
                    text = category.description,
                    style = AppTypography.bodyMedium,
                    color = Color(0xFF6B7280),
                    lineHeight = AppTypography.bodyMedium.lineHeight * 1.4
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "${category.sections.size}개 항목",
                    style = AppTypography.bodySmall.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = Color(0xFF4B7BFF)
                )
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "보기",
                tint = Color(0xFF9CA3AF),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TermsAndPoliciesScreenPreview() {
    Jikgong1111Theme {
        val navController = rememberNavController()
        val navigator = navController.toDestinationsNavigator()
        
        TermsAndPoliciesScreen(
            navigator = navigator,
            navController = navController
        )
    }
}