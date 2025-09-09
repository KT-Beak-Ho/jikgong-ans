package com.billcorea.jikgong.presentation.company.main.projectlist.projectcreate.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.billcorea.jikgong.ui.theme.AppTypography

data class ProjectCreateData(
    val title: String = "",
    val description: String = "",
    val location: String = "",
    val wage: Int = 0,
    val startDate: String = "",
    val endDate: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectCreateDialog(
    onDismiss: () -> Unit,
    onConfirm: (ProjectCreateData) -> Unit
) {
    var projectData by remember { mutableStateOf(ProjectCreateData()) }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "새 프로젝트 등록",
                    style = AppTypography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                OutlinedTextField(
                    value = projectData.title,
                    onValueChange = { projectData = projectData.copy(title = it) },
                    label = { Text("프로젝트 제목") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                OutlinedTextField(
                    value = projectData.location,
                    onValueChange = { projectData = projectData.copy(location = it) },
                    label = { Text("위치") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                OutlinedTextField(
                    value = projectData.wage.toString(),
                    onValueChange = { 
                        val wage = it.toIntOrNull() ?: 0
                        projectData = projectData.copy(wage = wage) 
                    },
                    label = { Text("일당 (원)") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("취소")
                    }
                    
                    Button(
                        onClick = { onConfirm(projectData) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4B7BFF)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("등록", color = Color.White)
                    }
                }
            }
        }
    }
}