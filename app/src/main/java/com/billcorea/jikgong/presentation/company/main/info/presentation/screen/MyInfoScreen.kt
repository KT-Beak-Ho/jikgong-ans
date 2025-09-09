package com.billcorea.jikgong.presentation.company.main.info.presentation.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
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
import com.billcorea.jikgong.presentation.company.main.info.feature.dialog.screen.ProfileEditDialog
import com.billcorea.jikgong.presentation.destinations.CompanyScoutMainScreenDestination
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
    var showComingSoonDialog by remember { mutableStateOf(false) }
    var comingSoonFeature by remember { mutableStateOf("") }
    
    // Coming Soon 다이얼로그
    if (showComingSoonDialog) {
        AlertDialog(
            onDismissRequest = { showComingSoonDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Construction,
                    contentDescription = null,
                    tint = Color(0xFF4B7BFF),
                    modifier = Modifier.size(48.dp)
                )
            },
            title = {
                Text(
                    text = "준비 중인 기능",
                    style = AppTypography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF1A1D29)
                )
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "'$comingSoonFeature' 기능은\n현재 준비 중입니다.",
                        style = AppTypography.bodyLarge,
                        color = Color(0xFF6B7280),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Text(
                        text = "곧 더 나은 서비스로 찾아뵙겠습니다!",
                        style = AppTypography.bodyMedium,
                        color = Color(0xFF9CA3AF),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showComingSoonDialog = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFF4B7BFF)
                    )
                ) {
                    Text(
                        text = "확인",
                        style = AppTypography.labelLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }
    
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
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.Transparent
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF4B7BFF).copy(alpha = 0.05f),
                                    Color(0xFF6B8BFF).copy(alpha = 0.03f)
                                )
                            )
                        )
                ) {
                    TopAppBar(
                        title = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = Color(0xFF4B7BFF).copy(alpha = 0.15f),
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "👤",
                                            style = AppTypography.titleMedium
                                        )
                                    }
                                }
                                Text(
                                    text = "내 정보",
                                    style = AppTypography.titleLarge.copy(
                                        fontWeight = FontWeight.ExtraBold
                                    ),
                                    color = Color(0xFF1A1D29)
                                )
                            }
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
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFF4B7BFF).copy(alpha = 0.1f),
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "편집",
                                            tint = Color(0xFF4B7BFF),
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Transparent
                        )
                    )
                }
            }
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF8FAFC),
                            Color(0xFFEFF3FF)
                        )
                    )
                )
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 빠른 메뉴
            item {
                QuickMenuCard(
                    onDocumentAutomationClick = {
                        comingSoonFeature = "서류 자동화"
                        showComingSoonDialog = true
                    },
                    onMatchingWorkersClick = {
                        comingSoonFeature = "매칭 인력"
                        showComingSoonDialog = true
                    },
                    onCompletedProjectsClick = {
                        comingSoonFeature = "완료 프로젝트"
                        showComingSoonDialog = true
                    },
                    onConstructionSitesClick = {
                        comingSoonFeature = "시공 현장"
                        showComingSoonDialog = true
                    },
                    onScoutProposalClick = {
                        navigator.navigate(CompanyScoutMainScreenDestination)
                    },
                    showComingSoonDialog = showComingSoonDialog,
                    comingSoonFeature = comingSoonFeature,
                    onComingSoonFeatureChange = { comingSoonFeature = it },
                    onShowComingSoonDialogChange = { showComingSoonDialog = it }
                )
            }
            
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
}

@Composable
private fun QuickMenuCard(
    onDocumentAutomationClick: () -> Unit,
    onMatchingWorkersClick: () -> Unit,
    onCompletedProjectsClick: () -> Unit,
    onConstructionSitesClick: () -> Unit,
    onScoutProposalClick: () -> Unit,
    showComingSoonDialog: Boolean,
    comingSoonFeature: String,
    onComingSoonFeatureChange: (String) -> Unit,
    onShowComingSoonDialogChange: (Boolean) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    val animatedElevation by animateDpAsState(
        targetValue = if (isExpanded) 12.dp else 6.dp,
        animationSpec = tween(300),
        label = "elevation"
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = animatedElevation
        ),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(
            width = 2.dp,
            color = androidx.compose.ui.graphics.Brush.horizontalGradient(
                colors = listOf(
                    Color(0xFF4B7BFF).copy(alpha = 0.3f),
                    Color(0xFF6B8BFF).copy(alpha = 0.1f)
                )
            ).let { brush ->
                Color(0xFF4B7BFF).copy(alpha = 0.2f)
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF4B7BFF).copy(alpha = 0.15f),
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "⚡",
                                style = AppTypography.titleLarge
                            )
                        }
                    }
                    
                    Column {
                        Text(
                            text = "빠른 메뉴",
                            style = AppTypography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color(0xFF1A1D29)
                        )
                        Text(
                            text = "자주 사용하는 기능",
                            style = AppTypography.labelSmall,
                            color = Color(0xFF9CA3AF)
                        )
                    }
                }
                
                IconButton(
                    onClick = { isExpanded = !isExpanded }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = if (isExpanded) "접기" else "펼치기",
                        tint = Color(0xFF4B7BFF),
                        modifier = Modifier.rotate(if (isExpanded) 90f else -90f)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 첫 번째 줄
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickMenuItem(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Description,
                    iconEmoji = "📄",
                    label = "서류 자동화",
                    backgroundColor = Color(0xFF4B7BFF).copy(alpha = 0.1f),
                    iconColor = Color(0xFF4B7BFF),
                    onClick = onDocumentAutomationClick
                )
                
                QuickMenuItem(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Group,
                    iconEmoji = "👥",
                    label = "매칭 인력",
                    backgroundColor = Color(0xFF10B981).copy(alpha = 0.1f),
                    iconColor = Color(0xFF10B981),
                    onClick = onMatchingWorkersClick
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // 두 번째 줄
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickMenuItem(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.CheckCircle,
                    iconEmoji = "✅",
                    label = "완료 프로젝트",
                    backgroundColor = Color(0xFF8B5CF6).copy(alpha = 0.1f),
                    iconColor = Color(0xFF8B5CF6),
                    onClick = onCompletedProjectsClick
                )
                
                QuickMenuItem(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Construction,
                    iconEmoji = "🏗️",
                    label = "시공 현장",
                    backgroundColor = Color(0xFFF59E0B).copy(alpha = 0.1f),
                    iconColor = Color(0xFFF59E0B),
                    onClick = onConstructionSitesClick
                )
            }
            
            // 세 번째 줄 - 스크랩한 인력 추가
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        QuickMenuItem(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.Bookmark,
                            iconEmoji = "⭐",
                            label = "스크랩한 인력",
                            backgroundColor = Color(0xFFEC407A).copy(alpha = 0.1f),
                            iconColor = Color(0xFFEC407A),
                            onClick = {
                                onComingSoonFeatureChange("스크랩한 인력")
                                onShowComingSoonDialogChange(true)
                            }
                        )
                        
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // 스카우트 제안 버튼 (전체 너비)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onScoutProposalClick() },
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                                colors = listOf(
                                    Color(0xFF4B7BFF),
                                    Color(0xFF6B8BFF)
                                )
                            )
                        )
                        .padding(18.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.PersonSearch,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        Text(
                            text = "스카우트 제안 보기",
                            style = AppTypography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.White
                        )
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color.White.copy(alpha = 0.25f)
                        ) {
                            Text(
                                text = "NEW",
                                style = AppTypography.labelSmall.copy(
                                    fontWeight = FontWeight.ExtraBold
                                ),
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickMenuItem(
    icon: ImageVector,
    iconEmoji: String,
    label: String,
    backgroundColor: Color,
    iconColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )
    val elevation by animateDpAsState(
        targetValue = if (isPressed) 8.dp else 3.dp,
        animationSpec = tween(100),
        label = "elevation"
    )
    
    Card(
        modifier = modifier
            .height(90.dp)
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = elevation
        ),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(
            width = 1.5.dp,
            brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                colors = listOf(
                    iconColor.copy(alpha = 0.3f),
                    iconColor.copy(alpha = 0.1f)
                )
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = iconEmoji,
                style = AppTypography.headlineSmall
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = label,
                style = AppTypography.labelMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = iconColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
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
            containerColor = androidx.compose.ui.graphics.Brush.horizontalGradient(
                colors = listOf(
                    Color(0xFFF8FAFC),
                    Color(0xFFF0F4FF)
                )
            ).let { Color(0xFFF8FAFC) }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (license.isValid) Color(0xFF10B981).copy(alpha = 0.2f) else Color(0xFFEF4444).copy(alpha = 0.2f)
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
            containerColor = androidx.compose.ui.graphics.Brush.horizontalGradient(
                colors = listOf(
                    Color(0xFFF8FAFC),
                    Color(0xFFF0F4FF)
                )
            ).let { Color(0xFFF8FAFC) }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (insurance.isActive) Color(0xFF10B981).copy(alpha = 0.2f) else Color(0xFFEF4444).copy(alpha = 0.2f)
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
            
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = profile.safetyRating.ratingColor.copy(alpha = 0.1f)
                ),
                border = BorderStroke(
                    width = 2.dp,
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(
                            profile.safetyRating.ratingColor.copy(alpha = 0.5f),
                            profile.safetyRating.ratingColor.copy(alpha = 0.2f)
                        )
                    )
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .background(
                            brush = androidx.compose.ui.graphics.Brush.radialGradient(
                                colors = listOf(
                                    profile.safetyRating.ratingColor.copy(alpha = 0.2f),
                                    profile.safetyRating.ratingColor.copy(alpha = 0.05f)
                                )
                            )
                        )
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = profile.safetyRating.ratingGrade,
                            style = AppTypography.headlineLarge.copy(
                                fontWeight = FontWeight.ExtraBold
                            ),
                            color = profile.safetyRating.ratingColor
                        )
                        Text(
                            text = "안전등급",
                            style = AppTypography.labelMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = profile.safetyRating.ratingColor.copy(alpha = 0.9f)
                        )
                    }
                }
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
            defaultElevation = 4.dp,
            pressedElevation = 8.dp,
            hoveredElevation = 6.dp
        ),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(
            width = 1.dp,
            color = Color(0xFFE5E7EB)
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF4B7BFF).copy(alpha = 0.1f),
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = Color(0xFF4B7BFF),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                
                Text(
                    text = title,
                    style = AppTypography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF1A1D29)
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = AppTypography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            color = Color(0xFF6B7280),
            modifier = Modifier.weight(0.4f)
        )
        
        Text(
            text = value,
            style = AppTypography.bodyMedium.copy(
                fontWeight = if (isLink) FontWeight.Bold else FontWeight.Normal
            ),
            color = if (isLink) Color(0xFF4B7BFF) else Color(0xFF1A1D29),
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(0.6f),
            textAlign = androidx.compose.ui.text.style.TextAlign.End
        )
    }
    
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 4.dp),
        thickness = 0.5.dp,
        color = Color(0xFFE5E7EB).copy(alpha = 0.5f)
    )
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