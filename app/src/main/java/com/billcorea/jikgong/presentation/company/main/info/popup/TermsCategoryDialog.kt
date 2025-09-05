package com.billcorea.jikgong.presentation.company.main.info.popup

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.billcorea.jikgong.presentation.company.main.info.data.TermsCategory
import com.billcorea.jikgong.presentation.company.main.info.data.TermsSection
import com.billcorea.jikgong.presentation.company.main.info.data.TermsAndPoliciesContent
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import java.time.format.DateTimeFormatter

@Composable
fun TermsCategoryDialog(
    category: TermsCategory,
    onDismiss: () -> Unit,
    onTermsClick: (TermsSection) -> Unit
) {
    var selectedTerms by remember { mutableStateOf<TermsSection?>(null) }
    
    // 상세 약관이 선택되었을 때 상세 다이얼로그 표시
    selectedTerms?.let { terms ->
        TermsDetailDialog(
            termsSection = terms,
            onDismiss = { selectedTerms = null }
        )
    }
    
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.8f),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // 헤더
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = category.title,
                                style = AppTypography.headlineSmall.copy(
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
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Text(
                            text = category.description,
                            style = AppTypography.bodyMedium,
                            color = Color(0xFF6B7280)
                        )
                    }
                    
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "닫기",
                            tint = Color(0xFF6B7280)
                        )
                    }
                }

                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color(0xFFE5E7EB)
                )

                // 약관 목록
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = category.sections,
                        key = { it.id }
                    ) { termsSection ->
                        TermsItem(
                            termsSection = termsSection,
                            onClick = { 
                                selectedTerms = termsSection
                                onTermsClick(termsSection)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TermsItem(
    termsSection: TermsSection,
    onClick: () -> Unit
) {
    OutlinedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = Color(0xFFFBFCFF)
        ),
        border = BorderStroke(
            width = 1.dp,
            color = Color(0xFFE5E7EB)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = termsSection.title,
                    style = AppTypography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF1A1D29)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "버전 ${termsSection.version}",
                        style = AppTypography.bodySmall,
                        color = Color(0xFF6B7280)
                    )
                    
                    Text(
                        text = "•",
                        style = AppTypography.bodySmall,
                        color = Color(0xFF6B7280)
                    )
                    
                    Text(
                        text = termsSection.lastUpdated.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")),
                        style = AppTypography.bodySmall,
                        color = Color(0xFF6B7280)
                    )
                }
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "보기",
                tint = Color(0xFF9CA3AF),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TermsCategoryDialogPreview() {
    Jikgong1111Theme {
        TermsCategoryDialog(
            category = TermsAndPoliciesContent.essentialTerms,
            onDismiss = {},
            onTermsClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TermsCategoryDialogAdditionalPreview() {
    Jikgong1111Theme {
        TermsCategoryDialog(
            category = TermsAndPoliciesContent.additionalPolicies,
            onDismiss = {},
            onTermsClick = {}
        )
    }
}