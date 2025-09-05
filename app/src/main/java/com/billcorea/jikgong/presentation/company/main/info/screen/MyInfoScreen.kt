package com.billcorea.jikgong.presentation.company.main.info.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.billcorea.jikgong.presentation.company.main.info.data.CompanyProfile
import com.billcorea.jikgong.presentation.company.main.info.data.ProfileContent
import com.billcorea.jikgong.presentation.company.main.info.data.BusinessLicense
import com.billcorea.jikgong.presentation.company.main.info.data.Insurance
import com.billcorea.jikgong.presentation.company.main.info.popup.ProfileEditDialog
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.utils.toDestinationsNavigator
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun MyInfoScreen(
    navigator: DestinationsNavigator,
    navController: NavController
) {
    var profile by remember { mutableStateOf(ProfileContent.createMockCompanyProfile()) }
    var showEditDialog by remember { mutableStateOf(false) }
    
    // 프로필 편집 다이얼로그
    if (showEditDialog) {
        ProfileEditDialog(
            profile = profile,
            onDismiss = { showEditDialog = false },
            onSave = { updatedProfile ->
                profile = updatedProfile
                showEditDialog = false
            }
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "내 정보",
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
                actions = {
                    IconButton(
                        onClick = { showEditDialog = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "편집",
                            tint = Color(0xFF4B7BFF)
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 회사 기본 정보
            item {
                CompanyBasicInfoCard(profile = profile)
            }
            
            // 연락처 정보
            item {
                ContactInfoCard(profile = profile)
            }
            
            // 주소 정보
            item {
                AddressInfoCard(profile = profile)
            }
            
            // 사업 정보
            item {
                BusinessInfoCard(profile = profile)
            }
            
            // 자격 및 인증
            item {
                LicensesCard(profile = profile)
            }
            
            // 보험 정보
            item {
                InsuranceCard(profile = profile)
            }
            
            // 안전 등급
            item {
                SafetyRatingCard(profile = profile)
            }
            
            // 운영 정보
            item {
                OperationInfoCard(profile = profile)
            }
            
            // 하단 여백
            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun CompanyBasicInfoCard(profile: CompanyProfile) {
    InfoCard(
        title = "기본 정보",
        icon = Icons.Default.Business
    ) {
        InfoItem(
            label = "회사명",
            value = profile.companyName
        )
        InfoItem(
            label = "사업자등록번호",
            value = profile.businessRegistrationNumber
        )
        InfoItem(
            label = "대표자명",
            value = profile.ceoName
        )
        InfoItem(
            label = "설립일",
            value = profile.establishedDate.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"))
        )
        InfoItem(
            label = "회사 규모",
            value = profile.companySizeDisplayName
        )
        InfoItem(
            label = "업종",
            value = profile.businessTypeDisplayName
        )
        InfoItem(
            label = "회사 소개",
            value = profile.description,
            maxLines = 3
        )
        profile.website?.let { website ->
            InfoItem(
                label = "홈페이지",
                value = website,
                isLink = true
            )
        }
    }
}

@Composable
private fun ContactInfoCard(profile: CompanyProfile) {
    InfoCard(
        title = "연락처 정보",
        icon = Icons.Default.Phone
    ) {
        InfoItem(
            label = "대표 전화",
            value = profile.phoneNumber
        )
        profile.faxNumber?.let { fax ->
            InfoItem(
                label = "팩스",
                value = fax
            )
        }
        InfoItem(
            label = "이메일",
            value = profile.email,
            isLink = true
        )
        profile.emergencyContact?.let { emergency ->
            InfoItem(
                label = "비상연락처",
                value = emergency
            )
        }
    }
}

@Composable
private fun AddressInfoCard(profile: CompanyProfile) {
    InfoCard(
        title = "주소 정보",
        icon = Icons.Default.LocationOn
    ) {
        InfoItem(
            label = "우편번호",
            value = profile.address.zipCode
        )
        InfoItem(
            label = "주소",
            value = profile.address.displayAddress
        )
        InfoItem(
            label = "시/도",
            value = profile.address.city
        )
        InfoItem(
            label = "구/군",
            value = profile.address.district
        )
        if (profile.workingAreas.isNotEmpty()) {
            InfoItem(
                label = "서비스 지역",
                value = profile.workingAreas.joinToString(", ")
            )
        }
    }
}

@Composable
private fun BusinessInfoCard(profile: CompanyProfile) {
    InfoCard(
        title = "사업 정보",
        icon = Icons.Default.Business
    ) {
        InfoItem(
            label = "업종",
            value = profile.businessTypeDisplayName
        )
        InfoItem(
            label = "설립일",
            value = profile.establishedDate.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"))
        )
        InfoItem(
            label = "기본 급여 지급일",
            value = profile.paymentTerms.paymentDaysDisplayName
        )
        InfoItem(
            label = "근무시간 (평일)",
            value = profile.workingHours.weekdayHours
        )
        profile.workingHours.weekendHours?.let { weekend ->
            InfoItem(
                label = "근무시간 (토요일)",
                value = weekend
            )
        }
    }
}

@Composable
private fun LicensesCard(profile: CompanyProfile) {
    InfoCard(
        title = "자격 및 면허",
        icon = Icons.Default.VerifiedUser
    ) {
        if (profile.licenses.isNotEmpty()) {
            profile.licenses.forEach { license ->
                LicenseItem(license = license)
            }
        } else {
            Text(
                text = "등록된 면허가 없습니다.",
                style = AppTypography.bodyMedium,
                color = Color(0xFF9CA3AF)
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        if (profile.certifications.isNotEmpty()) {
            Text(
                text = "인증서",
                style = AppTypography.titleSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color(0xFF374151)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            profile.certifications.forEach { cert ->
                InfoItem(
                    label = cert.name,
                    value = "${cert.issuingOrganization} • ${cert.certificationDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))}"
                )
            }
        }
    }
}

@Composable
private fun LicenseItem(license: BusinessLicense) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF8FAFC)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = license.name,
                    style = AppTypography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF1A1D29)
                )
                
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = if (license.isValid) Color(0xFF10B981) else Color(0xFFEF4444)
                ) {
                    Text(
                        text = license.statusDisplayName,
                        style = AppTypography.labelSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = "면허번호: ${license.licenseNumber}",
                style = AppTypography.bodySmall,
                color = Color(0xFF6B7280)
            )
            
            Text(
                text = "발급기관: ${license.issuedBy}",
                style = AppTypography.bodySmall,
                color = Color(0xFF6B7280)
            )
            
            license.expiryDate?.let { expiry ->
                Text(
                    text = "유효기간: ~${expiry.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))}",
                    style = AppTypography.bodySmall,
                    color = Color(0xFF6B7280)
                )
            }
        }
    }
    
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun InsuranceCard(profile: CompanyProfile) {
    InfoCard(
        title = "보험 정보",
        icon = Icons.Default.Shield
    ) {
        if (profile.insurances.isNotEmpty()) {
            profile.insurances.forEach { insurance ->
                InsuranceItem(insurance = insurance)
            }
        } else {
            Text(
                text = "등록된 보험이 없습니다.",
                style = AppTypography.bodyMedium,
                color = Color(0xFF9CA3AF)
            )
        }
    }
}

@Composable
private fun InsuranceItem(insurance: Insurance) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF8FAFC)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = insurance.typeDisplayName,
                    style = AppTypography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF1A1D29)
                )
                
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = if (insurance.isActive) Color(0xFF10B981) else Color(0xFFEF4444)
                ) {
                    Text(
                        text = if (insurance.isActive) "활성" else "만료",
                        style = AppTypography.labelSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = "보험사: ${insurance.provider}",
                style = AppTypography.bodySmall,
                color = Color(0xFF6B7280)
            )
            
            Text(
                text = "보장금액: ${String.format("%,d", insurance.coverageAmount)}원",
                style = AppTypography.bodySmall,
                color = Color(0xFF6B7280)
            )
            
            Text(
                text = "기간: ${insurance.startDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))} ~ ${insurance.endDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"))}",
                style = AppTypography.bodySmall,
                color = Color(0xFF6B7280)
            )
        }
    }
    
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun SafetyRatingCard(profile: CompanyProfile) {
    InfoCard(
        title = "안전 등급",
        icon = Icons.Default.Shield
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "종합 안전등급",
                    style = AppTypography.bodyMedium,
                    color = Color(0xFF6B7280)
                )
                
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = profile.safetyRating.ratingGrade,
                        style = AppTypography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = profile.safetyRating.ratingColor
                    )
                    
                    Text(
                        text = "(${profile.safetyRating.overallRating}/5.0)",
                        style = AppTypography.bodyMedium,
                        color = Color(0xFF6B7280)
                    )
                }
            }
            
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = profile.safetyRating.ratingColor.copy(alpha = 0.1f)
            ) {
                Text(
                    text = profile.safetyRating.ratingGrade,
                    style = AppTypography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = profile.safetyRating.ratingColor,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        InfoItem(
            label = "연간 사고 발생",
            value = "${profile.safetyRating.accidentCount}건"
        )
        InfoItem(
            label = "연간 안전교육",
            value = "${profile.safetyRating.safetyTrainingHours}시간"
        )
        InfoItem(
            label = "안전장비 점수",
            value = "${profile.safetyRating.safetyEquipmentScore}/5.0"
        )
        profile.safetyRating.lastInspectionDate?.let { lastInspection ->
            InfoItem(
                label = "최근 점검일",
                value = lastInspection.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"))
            )
        }
    }
}

@Composable
private fun OperationInfoCard(profile: CompanyProfile) {
    InfoCard(
        title = "운영 정보",
        icon = Icons.Default.Schedule
    ) {
        InfoItem(
            label = "근무시간 (평일)",
            value = profile.workingHours.weekdayHours
        )
        profile.workingHours.weekendHours?.let { weekend ->
            InfoItem(
                label = "근무시간 (토요일)",
                value = weekend
            )
        }
        InfoItem(
            label = "기본 급여 지급일",
            value = profile.paymentTerms.paymentDaysDisplayName
        )
        InfoItem(
            label = "자동 급여 지급",
            value = if (profile.paymentTerms.autoPaymentEnabled) "사용" else "미사용"
        )
        InfoItem(
            label = "최근 활동",
            value = profile.lastActiveAt.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm"))
        )
    }
}

@Composable
private fun InfoCard(
    title: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        border = BorderStroke(1.dp, Color(0xFFE5E7EB))
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF4B7BFF),
                    modifier = Modifier.size(20.dp)
                )
                
                Text(
                    text = title,
                    style = AppTypography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF1A1D29)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            content()
        }
    }
}

@Composable
private fun InfoItem(
    label: String,
    value: String,
    maxLines: Int = 1,
    isLink: Boolean = false
) {
    Column(
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            style = AppTypography.bodySmall,
            color = Color(0xFF6B7280)
        )
        
        Spacer(modifier = Modifier.height(2.dp))
        
        Text(
            text = value,
            style = AppTypography.bodyMedium,
            color = if (isLink) Color(0xFF4B7BFF) else Color(0xFF1A1D29),
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MyInfoScreenPreview() {
    Jikgong1111Theme {
        val navController = rememberNavController()
        val navigator = navController.toDestinationsNavigator()
        
        MyInfoScreen(
            navigator = navigator,
            navController = navController
        )
    }
}