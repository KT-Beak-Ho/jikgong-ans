package com.billcorea.jikgong.presentation.company.main.info.feature.dialog.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.billcorea.jikgong.presentation.company.main.info.data.CompanyProfile
import com.billcorea.jikgong.presentation.company.main.info.data.CompanySize
import com.billcorea.jikgong.presentation.company.main.info.data.BusinessType
import com.billcorea.jikgong.presentation.company.main.info.data.Address
import com.billcorea.jikgong.presentation.company.main.info.data.ProfileContent
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.Jikgong1111Theme

@Composable
fun ProfileEditDialog(
    profile: CompanyProfile,
    onDismiss: () -> Unit,
    onSave: (CompanyProfile) -> Unit
) {
    var editedProfile by remember { mutableStateOf(profile) }
    var showCompanySizeDropdown by remember { mutableStateOf(false) }
    var showBusinessTypeDropdown by remember { mutableStateOf(false) }
    
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.9f),
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
                        Text(
                            text = "프로필 편집",
                            style = AppTypography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color(0xFF1A1D29)
                        )
                        Text(
                            text = "회사 정보를 수정할 수 있습니다.",
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

                // 편집 폼
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    contentPadding = PaddingValues(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 기본 정보
                    item {
                        SectionHeader(title = "기본 정보")
                    }
                    
                    item {
                        OutlinedTextField(
                            value = editedProfile.companyName,
                            onValueChange = { editedProfile = editedProfile.copy(companyName = it) },
                            label = { Text("회사명") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                    
                    item {
                        OutlinedTextField(
                            value = editedProfile.businessRegistrationNumber,
                            onValueChange = { editedProfile = editedProfile.copy(businessRegistrationNumber = it) },
                            label = { Text("사업자등록번호") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                    
                    item {
                        OutlinedTextField(
                            value = editedProfile.ceoName,
                            onValueChange = { editedProfile = editedProfile.copy(ceoName = it) },
                            label = { Text("대표자명") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                    
                    // 회사 규모 드롭다운
                    item {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = editedProfile.companySizeDisplayName,
                                onValueChange = { },
                                label = { Text("회사 규모") },
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = true,
                                trailingIcon = {
                                    IconButton(onClick = { showCompanySizeDropdown = true }) {
                                        Icon(
                                            imageVector = Icons.Default.KeyboardArrowDown,
                                            contentDescription = "선택"
                                        )
                                    }
                                }
                            )
                            
                            DropdownMenu(
                                expanded = showCompanySizeDropdown,
                                onDismissRequest = { showCompanySizeDropdown = false }
                            ) {
                                CompanySize.entries.forEach { size ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                when (size) {
                                                    CompanySize.STARTUP -> "스타트업 (1-10명)"
                                                    CompanySize.SMALL -> "중소기업 (11-50명)"
                                                    CompanySize.MEDIUM -> "중견기업 (51-300명)"
                                                    CompanySize.LARGE -> "대기업 (300명+)"
                                                    CompanySize.INDIVIDUAL -> "개인사업자"
                                                }
                                            )
                                        },
                                        onClick = {
                                            editedProfile = editedProfile.copy(companySize = size)
                                            showCompanySizeDropdown = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    
                    // 업종 드롭다운
                    item {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = editedProfile.businessTypeDisplayName,
                                onValueChange = { },
                                label = { Text("업종") },
                                modifier = Modifier.fillMaxWidth(),
                                readOnly = true,
                                trailingIcon = {
                                    IconButton(onClick = { showBusinessTypeDropdown = true }) {
                                        Icon(
                                            imageVector = Icons.Default.KeyboardArrowDown,
                                            contentDescription = "선택"
                                        )
                                    }
                                }
                            )
                            
                            DropdownMenu(
                                expanded = showBusinessTypeDropdown,
                                onDismissRequest = { showBusinessTypeDropdown = false }
                            ) {
                                BusinessType.entries.forEach { type ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                when (type) {
                                                    BusinessType.CONSTRUCTION -> "건설업"
                                                    BusinessType.ARCHITECTURE -> "건축업"
                                                    BusinessType.CIVIL_ENGINEERING -> "토목공사업"
                                                    BusinessType.INTERIOR -> "실내건축업"
                                                    BusinessType.ELECTRICAL -> "전기공사업"
                                                    BusinessType.PLUMBING -> "배관공사업"
                                                    BusinessType.PAINTING -> "도장공사업"
                                                    BusinessType.LANDSCAPING -> "조경공사업"
                                                    BusinessType.DEMOLITION -> "철거공사업"
                                                    BusinessType.OTHER -> "기타"
                                                }
                                            )
                                        },
                                        onClick = {
                                            editedProfile = editedProfile.copy(businessType = type)
                                            showBusinessTypeDropdown = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    
                    item {
                        OutlinedTextField(
                            value = editedProfile.description,
                            onValueChange = { editedProfile = editedProfile.copy(description = it) },
                            label = { Text("회사 소개") },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3,
                            maxLines = 5
                        )
                    }
                    
                    item {
                        OutlinedTextField(
                            value = editedProfile.website ?: "",
                            onValueChange = { editedProfile = editedProfile.copy(website = it.ifBlank { null }) },
                            label = { Text("홈페이지 (선택사항)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri)
                        )
                    }
                    
                    // 연락처 정보
                    item {
                        SectionHeader(title = "연락처 정보")
                    }
                    
                    item {
                        OutlinedTextField(
                            value = editedProfile.phoneNumber,
                            onValueChange = { editedProfile = editedProfile.copy(phoneNumber = it) },
                            label = { Text("전화번호") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                        )
                    }
                    
                    item {
                        OutlinedTextField(
                            value = editedProfile.faxNumber ?: "",
                            onValueChange = { editedProfile = editedProfile.copy(faxNumber = it.ifBlank { null }) },
                            label = { Text("팩스번호 (선택사항)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                        )
                    }
                    
                    item {
                        OutlinedTextField(
                            value = editedProfile.email,
                            onValueChange = { editedProfile = editedProfile.copy(email = it) },
                            label = { Text("이메일") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                        )
                    }
                    
                    item {
                        OutlinedTextField(
                            value = editedProfile.emergencyContact ?: "",
                            onValueChange = { editedProfile = editedProfile.copy(emergencyContact = it.ifBlank { null }) },
                            label = { Text("비상연락처 (선택사항)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                        )
                    }
                    
                    // 주소 정보
                    item {
                        SectionHeader(title = "주소 정보")
                    }
                    
                    item {
                        OutlinedTextField(
                            value = editedProfile.address.zipCode,
                            onValueChange = { 
                                editedProfile = editedProfile.copy(
                                    address = editedProfile.address.copy(zipCode = it)
                                )
                            },
                            label = { Text("우편번호") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                    
                    item {
                        OutlinedTextField(
                            value = editedProfile.address.fullAddress,
                            onValueChange = { 
                                editedProfile = editedProfile.copy(
                                    address = editedProfile.address.copy(fullAddress = it)
                                )
                            },
                            label = { Text("주소") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                    
                    item {
                        OutlinedTextField(
                            value = editedProfile.address.detailAddress ?: "",
                            onValueChange = { 
                                editedProfile = editedProfile.copy(
                                    address = editedProfile.address.copy(
                                        detailAddress = it.ifBlank { null }
                                    )
                                )
                            },
                            label = { Text("상세주소 (선택사항)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                }

                // 하단 버튼
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color(0xFFE5E7EB)
                )
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF6B7280)
                        ),
                        border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "취소",
                            style = AppTypography.titleMedium.copy(
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }

                    Button(
                        onClick = { onSave(editedProfile) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4B7BFF)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "저장",
                            style = AppTypography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = AppTypography.titleMedium.copy(
            fontWeight = FontWeight.Bold
        ),
        color = Color(0xFF1A1D29)
    )
}

@Preview(showBackground = true)
@Composable
fun ProfileEditDialogPreview() {
    Jikgong1111Theme {
        ProfileEditDialog(
            profile = ProfileContent.createMockCompanyProfile(),
            onDismiss = {},
            onSave = {}
        )
    }
}