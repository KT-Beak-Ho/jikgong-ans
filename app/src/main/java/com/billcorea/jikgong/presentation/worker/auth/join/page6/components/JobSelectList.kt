package com.billcorea.jikgong.presentation.worker.auth.join.page6.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.billcorea.jikgong.R
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.appColorScheme

// Job data class
data class JobType(
  val code: String,
  val name: String
)

@Composable
fun JobSelectList(
  selectedJobs: Set<String>,
  doJobSelect:(jobCode : String) -> Unit,
  doClose:() -> Unit
) {
  // Define all job types
  val allJobTypes = remember {
    listOf(
      JobType("NORMAL", "보통인부"),
      JobType("FOREMAN", "작업반장"),
      JobType("SKILLED_LABORER", "특별인부"),
      JobType("HELPER", "조력공"),
      JobType("SCAFFOLDER", "비계공"),
      JobType("FORMWORK_CARPENTER", "형틀목공"),
      JobType("REBAR_WORKER", "철근공"),
      JobType("STEEL_STRUCTURE", "철골공"),
      JobType("WELDER", "용접공"),
      JobType("CONCRETE_WORKER", "콘크리트공"),
      JobType("BRICKLAYER", "조적공"),
      JobType("DRYWALL_FINISHER", "견출공"),
      JobType("CONSTRUCTION_CARPENTER", "건축목공"),
      JobType("WINDOW_DOOR_INSTALLER", "창호공"),
      JobType("GLAZIER", "유리공"),
      JobType("WATERPROOFING_WORKER", "방수공"),
      JobType("PLASTERER", "미장공"),
      JobType("TILE", "타일공"),
      JobType("PAINTER", "도장공"),
      JobType("INTERIOR_FINISHER", "내장공"),
      JobType("WALLPAPER_INSTALLER", "도배공"),
      JobType("POLISHER", "연마공"),
      JobType("STONEMASON", "석공"),
      JobType("GROUT_WORKER", "줄눈공"),
      JobType("PANEL_ASSEMBLER", "판넬조립공"),
      JobType("ROOFER", "지붕잇기공"),
      JobType("LANDSCAPER", "조경공"),
      JobType("CAULKER", "코킹공"),
      JobType("PLUMBER", "배관공"),
      JobType("BOILER_TECHNICIAN", "보일러공"),
      JobType("SANITARY_TECHNICIAN", "위생공"),
      JobType("DUCT_INSTALLER", "덕트공"),
      JobType("INSULATION_WORKER", "보온공"),
      JobType("MECHANICAL_EQUIPMENT_TECHNICIAN", "기계설비공"),
      JobType("ELECTRICIAN", "내선전공"),
      JobType("TELECOMMUNICATIONS_INSTALLER", "통신내선공"),
      JobType("TELECOMMUNICATIONS_EQUIPMENT_INSTALLER", "통신설비공"),
      JobType("OTHER", "기타")
    )
  }
  
  var searchQuery by remember { mutableStateOf("") }
  
  // Filter job types based on search query
  val filteredJobTypes = remember(searchQuery) {
    if (searchQuery.isBlank()) {
      allJobTypes
    } else {
      allJobTypes.filter { jobType ->
        jobType.name.contains(searchQuery, ignoreCase = true) ||
        jobType.code.contains(searchQuery, ignoreCase = true)
      }
    }
  }

  Column(modifier = Modifier.fillMaxWidth()) {
    // Header
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = stringResource(R.string.selectJob),
        color = appColorScheme.primary,
        style = AppTypography.titleMedium,
        fontWeight = FontWeight.Bold
      )
      IconButton(onClick = doClose) {
        Icon(
          imageVector = Icons.Default.Close, 
          contentDescription = "close",
          tint = Color.Gray
        )
      }
    }
    
    // Selected jobs summary
    if (selectedJobs.isNotEmpty()) {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
          containerColor = appColorScheme.primaryContainer.copy(alpha = 0.3f)
        )
      ) {
        Column(
          modifier = Modifier.padding(12.dp)
        ) {
          Text(
            text = "선택된 직종 (${selectedJobs.size}개)",
            style = AppTypography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = appColorScheme.primary
          )
          Spacer(modifier = Modifier.height(8.dp))
          val selectedJobNames = selectedJobs.mapNotNull { code ->
            allJobTypes.find { it.code == code }?.name
          }
          Text(
            text = selectedJobNames.joinToString(", "),
            style = AppTypography.bodySmall,
            color = appColorScheme.onPrimaryContainer
          )
        }
      }
      Spacer(modifier = Modifier.height(8.dp))
    }
    
    // Search field
    OutlinedTextField(
      value = searchQuery,
      onValueChange = { searchQuery = it },
      label = { Text("직종 검색") },
      leadingIcon = {
        Icon(
          Icons.Outlined.Search,
          contentDescription = "검색"
        )
      },
      trailingIcon = {
        if (searchQuery.isNotEmpty()) {
          IconButton(onClick = { searchQuery = "" }) {
            Icon(
              Icons.Filled.Clear,
              contentDescription = "검색어 지우기"
            )
          }
        }
      },
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
      keyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Search
      ),
      singleLine = true
    )
    
    Spacer(modifier = Modifier.height(16.dp))
    
    // Job type list
    LazyColumn(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      items(filteredJobTypes) { jobType ->
        JobTypeItem(
          jobType = jobType,
          isSelected = selectedJobs.contains(jobType.code),
          onJobSelect = { doJobSelect(jobType.code) }
        )
      }
    }
  }
}

@Composable
private fun JobTypeItem(
  jobType: JobType,
  isSelected: Boolean,
  onJobSelect: () -> Unit
) {
  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onJobSelect() },
    colors = CardDefaults.cardColors(
      containerColor = if (isSelected) {
        appColorScheme.primaryContainer
      } else {
        Color.White
      }
    ),
    border = if (isSelected) {
      androidx.compose.foundation.BorderStroke(
        width = 2.dp,
        color = appColorScheme.primary
      )
    } else {
      androidx.compose.foundation.BorderStroke(
        width = 1.dp,
        color = Color.Gray.copy(alpha = 0.3f)
      )
    },
    shape = RoundedCornerShape(12.dp)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(
        text = jobType.name,
        style = AppTypography.bodyMedium,
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
        color = if (isSelected) {
          appColorScheme.onPrimaryContainer
        } else {
          Color.Black
        }
      )
      
      if (isSelected) {
        Icon(
          Icons.Default.CheckCircle,
          contentDescription = "선택됨",
          tint = appColorScheme.primary,
          modifier = Modifier.size(20.dp)
        )
      } else {
        Icon(
          Icons.Outlined.RadioButtonUnchecked,
          contentDescription = "선택 안됨",
          tint = Color.Gray,
          modifier = Modifier.size(20.dp)
        )
      }
    }
  }
}