package com.billcorea.jikgong.presentation.company.main.info.presentation.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.CompanyStats
import com.billcorea.jikgong.api.models.sampleDataFactory.DataFactoryModels.StatItem

@Composable
fun StatsGrid(
  stats: CompanyStats,
  modifier: Modifier = Modifier,
  onStatClick: ((String) -> Unit)? = null
) {
  val statsList = listOf(
    stats.automatedDocs,
    stats.matchedWorkers,
    stats.completedProjects,
    stats.activeConstructionSites
  )

  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 20.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    Row(
      horizontalArrangement = Arrangement.spacedBy(12.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      StatCard(
        stat = statsList[0],
        modifier = Modifier.weight(1f),
        onClick = { onStatClick?.invoke(statsList[0].label) }
      )
      StatCard(
        stat = statsList[1],
        modifier = Modifier.weight(1f),
        onClick = { onStatClick?.invoke(statsList[1].label) }
      )
    }
    Row(
      horizontalArrangement = Arrangement.spacedBy(12.dp),
      modifier = Modifier.fillMaxWidth()
    ) {
      StatCard(
        stat = statsList[2],
        modifier = Modifier.weight(1f),
        onClick = { onStatClick?.invoke(statsList[2].label) }
      )
      StatCard(
        stat = statsList[3],
        isHighlight = true,
        modifier = Modifier.weight(1f),
        onClick = { onStatClick?.invoke(statsList[3].label) }
      )
    }
  }
}

@Composable
private fun StatCard(
  stat: StatItem,
  isHighlight: Boolean = false,
  modifier: Modifier = Modifier,
  onClick: () -> Unit = {}
) {
  var isPressed by remember { mutableStateOf(false) }

  Card(
    modifier = modifier
      .animateContentSize()
      .clickable { 
        onClick()
        isPressed = !isPressed 
      }
      .then(
        if (isHighlight) {
          Modifier.border(
            width = 1.5.dp,
            brush = Brush.linearGradient(
              colors = listOf(
                Color(0x334B7BFF),
                Color(0x335B87FF)
              )
            ),
            shape = RoundedCornerShape(16.dp)
          )
        } else Modifier
      ),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(
      containerColor = if (isHighlight) {
        Color(0xFFF9F5FF)
      } else {
        Color(0xFFF9FAFB)
      }
    ),
    elevation = CardDefaults.cardElevation(
      defaultElevation = if (isPressed) 2.dp else 0.dp
    )
  ) {
    Column(
      modifier = Modifier.padding(16.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
      ) {
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = if (isHighlight) {
            Color.Transparent
          } else {
            Color.White
          },
          shadowElevation = if (!isHighlight) 1.dp else 0.dp,
          modifier = Modifier.size(40.dp)
        ) {
          Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
              .fillMaxSize()
              .then(
                if (isHighlight) {
                  Modifier.background(
                    Brush.linearGradient(
                      colors = listOf(
                        Color(0xFF4B7BFF),
                        Color(0xFF5B87FF)
                      )
                    )
                  )
                } else Modifier
              )
          ) {
            Text(
              text = stat.icon,
              fontSize = 18.sp
            )
          }
        }

        Text(
          text = stat.trendText,
          fontSize = 12.sp,
          fontWeight = FontWeight.SemiBold,
          color = if (stat.isActive) {
            Color(0xFF4B7BFF)
          } else {
            Color(0xFF10B981)
          }
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      Text(
        text = stat.label,
        fontSize = 12.sp,
        color = Color(0xFF6B7280)
      )

      Spacer(modifier = Modifier.height(4.dp))

      Row(
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
      ) {
        Text(
          text = stat.value.toString(),
          fontSize = 22.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF111827)
        )
        Text(
          text = stat.unit,
          fontSize = 22.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFF111827)
        )
      }
    }
  }
}
