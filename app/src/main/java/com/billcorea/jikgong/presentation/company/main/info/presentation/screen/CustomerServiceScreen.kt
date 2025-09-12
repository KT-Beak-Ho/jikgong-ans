package com.billcorea.jikgong.presentation.company.main.info.presentation.screen

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
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import android.net.Uri
import com.billcorea.jikgong.presentation.company.main.common.BackNavigationTopBar
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.billcorea.jikgong.ui.theme.appColorScheme
import com.ramcosta.composedestinations.annotation.Destination

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun CustomerServiceScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            BackNavigationTopBar(
                title = "고객센터",
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
            // 연락처 정보 카드
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF4B7BFF).copy(alpha = 0.1f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "📞 고객센터 연락처",
                            style = AppTypography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "전화번호",
                                    style = AppTypography.bodyMedium,
                                    color = Color.Gray
                                )
                                Text(
                                    text = "1588-1234",
                                    style = AppTypography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF4B7BFF)
                                )
                            }
                            
                            val context = LocalContext.current
                            Button(
                                onClick = { 
                                    val phoneNumber = "tel:15881234"
                                    val intent = Intent(Intent.ACTION_DIAL).apply {
                                        data = Uri.parse(phoneNumber)
                                    }
                                    context.startActivity(intent)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF4B7BFF)
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Phone,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("전화하기")
                            }
                        }
                        
                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 16.dp),
                            thickness = 1.dp,
                            color = Color.Gray.copy(alpha = 0.2f)
                        )
                        
                        Text(
                            text = "운영시간: 평일 09:00 ~ 18:00 (주말/공휴일 휴무)",
                            style = AppTypography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }
            
            // 자주 묻는 질문
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
                            text = "❓ 자주 묻는 질문",
                            style = AppTypography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        ExpandableFAQSection(
                            title = "스카우트 관련 문의",
                            icon = Icons.Default.QuestionAnswer,
                            faqs = listOf(
                                FAQItem("Q1. AI 스카우트는 어떻게 작동하나요?", "AI가 귀사의 현장 조건, 필요 기술, 위치 등을 분석하여 가장 적합한 인력을 자동으로 추천합니다. 경력, 자격증, 출근율, 과거 평가 등을 종합적으로 고려하여 최적의 인재를 매칭해드립니다."),
                                FAQItem("Q2. 스카우트 제안을 보냈는데 응답이 없어요.", "구직자가 48시간 내 응답하지 않으면 자동 거절 처리됩니다. 프로필이 우수한 구직자일수록 여러 제안을 받기 때문에 경쟁력 있는 임금과 근무조건을 제시하시길 권장합니다."),
                                FAQItem("Q3. 한 번에 몇 명까지 스카우트할 수 있나요?", "베이직 플랜은 일일 10명, 프로 플랜은 30명, 엔터프라이즈 플랜은 무제한 스카우트가 가능합니다."),
                                FAQItem("Q4. 스카우트한 인력의 노쇼(무단결근)가 발생했어요.", "노쇼 발생 시 해당 구직자의 신뢰도 점수가 자동 차감되며, 3회 이상 누적 시 플랫폼 이용이 제한됩니다. 피해 보상은 고객센터로 문의해주세요.")
                            )
                        )
                        
                        ExpandableFAQSection(
                            title = "급여 및 정산문의",
                            icon = Icons.Default.Payment,
                            faqs = listOf(
                                FAQItem("Q1. 임금 정산은 언제 이루어지나요?", "구직자가 작업 완료 확인을 하면 익일 자동 정산됩니다. 기업은 월말 일괄 정산 또는 즉시 정산 중 선택 가능합니다."),
                                FAQItem("Q2. 중개수수료는 정말 없나요?", "네, 직직직은 중개수수료가 전혀 없습니다. 대신 월 구독료(베이직 30만원/프로 70만원/엔터프라이즈 150만원)로 운영됩니다."),
                                FAQItem("Q3. 세금계산서 발행이 자동으로 되나요?", "네, 모든 거래에 대해 전자세금계산서가 자동 발행되며, 국세청에 자동 전송됩니다. 관리 페이지에서 언제든 다운로드 가능합니다."),
                                FAQItem("Q4. 임금 지급 내역을 엑셀로 받을 수 있나요?", "대시보드에서 기간별, 현장별, 구직자별로 정렬하여 엑셀 파일로 다운로드 가능합니다."),
                                FAQItem("Q5. 구직자가 임금을 받지 못했다고 하는데요?", "정산 내역 페이지에서 송금 완료 증빙을 확인하실 수 있습니다. 계좌번호 오류인 경우 구직자에게 정확한 계좌 재등록을 요청해주세요.")
                            )
                        )
                        
                        ExpandableFAQSection(
                            title = "계정 및 회원 정보",
                            icon = Icons.Default.AccountBox,
                            faqs = listOf(
                                FAQItem("Q1. 사업자등록증을 변경하고 싶어요.", "[내 정보] → [기업 정보 수정]에서 새로운 사업자등록증을 업로드하시면 영업일 기준 1일 내 확인 후 변경됩니다."),
                                FAQItem("Q2. 담당자를 여러 명 등록할 수 있나요?", "프로 플랜부터 최대 5명, 엔터프라이즈 플랜은 무제한 담당자 계정 생성이 가능합니다. 각 담당자별 권한 설정도 가능합니다."),
                                FAQItem("Q3. 구독 플랜을 변경하고 싶어요.", "상위 플랜으로는 즉시 변경 가능하며, 하위 플랜으로는 다음 결제일부터 적용됩니다. 잔여 기간은 일할 계산하여 환불 또는 차감됩니다."),
                                FAQItem("Q4. 회원 탈퇴 시 데이터는 어떻게 되나요?", "근로기준법에 따라 임금대장 등 법정 보존 서류는 3년간 보관되며, 이후 완전 삭제됩니다. 탈퇴 전 필요한 데이터는 반드시 백업하시기 바랍니다."),
                                FAQItem("Q5. 기업 인증이 거절되었어요.", "사업자등록증이 불명확하거나 건설업 등록증이 없는 경우 거절될 수 있습니다. 건설업 면허가 없는 경우 하도급 계약서 등 증빙 서류를 추가 제출해주세요.")
                            )
                        )
                    }
                }
            }
            
            // 1:1 문의
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
                            text = "🤖 AI 1:1 문의하기",
                            style = AppTypography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        Text(
                            text = "AI가 24시간 궁금한 점을 즉시 해결해드립니다. 복잡한 문의사항도 AI가 이해하고 정확한 답변을 제공합니다.",
                            style = AppTypography.bodyMedium,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        var showInquiryDialog by remember { mutableStateOf(false) }
                        Button(
                            onClick = { showInquiryDialog = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4B7BFF)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Create,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "AI 문의하기",
                                style = AppTypography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                        }
                        
                        // AI 문의 다이얼로그
                        if (showInquiryDialog) {
                            AIInquiryDialog(
                                onDismiss = { showInquiryDialog = false },
                                onSend = { inquiry ->
                                    // TODO: 실제 AI API 연동
                                    showInquiryDialog = false
                                }
                            )
                        }
                    }
                }
            }
            
            // 앱 정보
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
                            text = "앱 정보",
                            style = AppTypography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        
                        InfoRow("앱 버전", "1.2.0")
                        InfoRow("최신 업데이트", "2025.08.29")
                        InfoRow("문의 응답 시간", "평균 2시간 이내")
                    }
                }
            }
        }
    }
}

@Composable
private fun ServiceMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
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
                tint = Color(0xFF4B7BFF),
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = AppTypography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = subtitle,
                    style = AppTypography.bodySmall,
                    color = Color.Gray
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
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = AppTypography.bodyMedium,
            color = Color.Gray
        )
        Text(
            text = value,
            style = AppTypography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

data class FAQItem(
    val question: String,
    val answer: String
)

@Composable
private fun ExpandableFAQSection(
    title: String,
    icon: ImageVector,
    faqs: List<FAQItem>
) {
    var isExpanded by remember { mutableStateOf(false) }
    
    Column {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            onClick = { isExpanded = !isExpanded },
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
                    tint = Color(0xFF4B7BFF),
                    modifier = Modifier.size(24.dp)
                )
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Text(
                    text = title,
                    style = AppTypography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )
                
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        
        if (isExpanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 40.dp, end = 16.dp, bottom = 16.dp)
            ) {
                faqs.forEach { faq ->
                    FAQItemContent(faq)
                    if (faq != faqs.last()) {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
        
        HorizontalDivider(
            thickness = 1.dp,
            color = Color.Gray.copy(alpha = 0.1f)
        )
    }
}

@Composable
private fun FAQItemContent(faq: FAQItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF8F9FA)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = faq.question,
                style = AppTypography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4B7BFF),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                text = faq.answer,
                style = AppTypography.bodySmall,
                color = Color(0xFF374151),
                lineHeight = 18.sp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIInquiryDialog(
    onDismiss: () -> Unit,
    onSend: (String) -> Unit
) {
    var inquiryText by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("일반문의") }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.SmartToy,
                    contentDescription = null,
                    tint = Color(0xFF4B7BFF),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "AI 문의하기",
                    style = AppTypography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 카테고리 선택
                Text(
                    "문의 유형",
                    style = AppTypography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("일반문의", "기술지원", "결제문의", "기타").forEach { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(cat, fontSize = 12.sp) },
                            modifier = Modifier.height(32.dp)
                        )
                    }
                }
                
                // 문의 내용
                OutlinedTextField(
                    value = inquiryText,
                    onValueChange = { inquiryText = it },
                    label = { Text("문의 내용") },
                    placeholder = { Text("궁금한 점을 자세히 작성해주세요") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    maxLines = 5
                )
                
                Text(
                    "* AI가 24시간 즉시 답변해드립니다",
                    style = AppTypography.bodySmall,
                    color = Color(0xFF4B7BFF)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { 
                    if (inquiryText.isNotBlank()) {
                        onSend("[$category] $inquiryText")
                    }
                },
                enabled = inquiryText.isNotBlank()
            ) {
                Text("전송")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun CustomerServiceScreenPreview() {
    Jikgong1111Theme {
        CustomerServiceScreen(
            navController = rememberNavController()
        )
    }
}