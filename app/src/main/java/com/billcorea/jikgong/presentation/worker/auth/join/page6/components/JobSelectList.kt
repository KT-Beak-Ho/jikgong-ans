package com.billcorea.jikgong.presentation.worker.auth.join.page6.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.billcorea.jikgong.R
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.appColorScheme

@Composable
fun JobSelectList(
  doJobSelect:(jobCode : String) -> Unit,
  doClose:() -> Unit
) {
  val config = LocalConfiguration.current
  val screenWeight = config.screenWidthDp
  val screenHeight = config.screenHeightDp

  Column (modifier = Modifier.fillMaxWidth()) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = stringResource(R.string.selectJob),
        color = appColorScheme.primary,
        lineHeight = 1.25.em,
        style = AppTypography.bodyMedium,
        modifier = Modifier.padding(3.dp)
      )
      IconButton(onClick = {
        doClose()
      }) {
        Icon(imageVector = Icons.Default.Close, contentDescription = "close")
      }
    }

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp, end = 16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = {
        doJobSelect("NORMAL")
      }, modifier = Modifier
        .width((screenWeight * .28).dp)
        .height(40.dp)
        .border(
          width = 1.dp,
          color = Color.Gray,
          shape = RoundedCornerShape(18.dp)
        )
        .clip(RoundedCornerShape(18.dp))
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(text= "보통인부", style = AppTypography.bodyMedium)
        }

      }
      IconButton(onClick = {
        doJobSelect("FOREMAN")
      }, modifier = Modifier
        .width((screenWeight * .28).dp)
        .height(40.dp)
        .border(
          width = 1.dp,
          color = Color.Gray,
          shape = RoundedCornerShape(18.dp)
        )
        .clip(RoundedCornerShape(18.dp))
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(text= "작업반장", style = AppTypography.bodyMedium)
        }
      }
      IconButton(onClick = {
        doJobSelect("SKILLED_LABORER")
      }, modifier = Modifier
        .width((screenWeight * .28).dp)
        .height(40.dp)
        .border(
          width = 1.dp,
          color = Color.Gray,
          shape = RoundedCornerShape(18.dp)
        )
        .clip(RoundedCornerShape(18.dp))
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(text= "특별인부", style = AppTypography.bodyMedium)
        }
      }
    }

    Spacer(modifier = Modifier.padding(8.dp))

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp, end = 16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = {
        doJobSelect("HELPER")
      }, modifier = Modifier
        .width((screenWeight * .28).dp)
        .height(40.dp)
        .border(
          width = 1.dp,
          color = Color.Gray,
          shape = RoundedCornerShape(18.dp)
        )
        .clip(RoundedCornerShape(18.dp))
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(text= "조력공", style = AppTypography.bodyMedium)
        }
      }
      IconButton(onClick = {
        doJobSelect("SCAFFOLDER")
      }, modifier = Modifier
        .width((screenWeight * .28).dp)
        .height(40.dp)
        .border(
          width = 1.dp,
          color = Color.Gray,
          shape = RoundedCornerShape(18.dp)
        )
        .clip(RoundedCornerShape(18.dp))
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(text= "비계공", style = AppTypography.bodyMedium)
        }

      }
      IconButton(onClick = {
        doJobSelect("FORMWORK_CARPENTER")
      }, modifier = Modifier
        .width((screenWeight * .28).dp)
        .height(40.dp)
        .border(
          width = 1.dp,
          color = Color.Gray,
          shape = RoundedCornerShape(18.dp)
        )
        .clip(RoundedCornerShape(18.dp))
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(text= "형틀목공", style = AppTypography.bodyMedium)
        }
      }
    }

    Spacer(modifier = Modifier.padding(8.dp))

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp, end = 16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = {
        doJobSelect("REBAR_WORKER")
      }, modifier = Modifier
        .width((screenWeight * .28).dp)
        .height(40.dp)
        .border(
          width = 1.dp,
          color = Color.Gray,
          shape = RoundedCornerShape(18.dp)
        )
        .clip(RoundedCornerShape(18.dp))
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(text= "철근공", style = AppTypography.bodyMedium)
        }
      }
      IconButton(onClick = {
        doJobSelect("STEEL_STRUCTURE")
      }, modifier = Modifier
        .width((screenWeight * .28).dp)
        .height(40.dp)
        .border(
          width = 1.dp,
          color = Color.Gray,
          shape = RoundedCornerShape(18.dp)
        )
        .clip(RoundedCornerShape(18.dp))
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(text= "철골공", style = AppTypography.bodyMedium)
        }
      }
      IconButton(onClick = {
        doJobSelect("WELDER")
      }, modifier = Modifier
        .width((screenWeight * .28).dp)
        .height(40.dp)
        .border(
          width = 1.dp,
          color = Color.Gray,
          shape = RoundedCornerShape(18.dp)
        )
        .clip(RoundedCornerShape(18.dp))
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(text= "용접공", style = AppTypography.bodyMedium)
        }
      }
    }

    Spacer(modifier = Modifier.padding(8.dp))

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp, end = 16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = {
        doJobSelect("CONCRETE_WORKER")
      }, modifier = Modifier
        .width((screenWeight * .28).dp)
        .height(40.dp)
        .border(
          width = 1.dp,
          color = Color.Gray,
          shape = RoundedCornerShape(18.dp)
        )
        .clip(RoundedCornerShape(18.dp))
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(text= "콘트리트공", style = AppTypography.bodyMedium)
        }
      }
      IconButton(onClick = {
        doJobSelect("BRICKLAYER")
      }, modifier = Modifier
        .width((screenWeight * .28).dp)
        .height(40.dp)
        .border(
          width = 1.dp,
          color = Color.Gray,
          shape = RoundedCornerShape(18.dp)
        )
        .clip(RoundedCornerShape(18.dp))
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(text= "조적공", style = AppTypography.bodyMedium)
        }

      }
      IconButton(onClick = {
        doJobSelect("DRYWALL_FINISHER")
      }, modifier = Modifier
        .width((screenWeight * .28).dp)
        .height(40.dp)
        .border(
          width = 1.dp,
          color = Color.Gray,
          shape = RoundedCornerShape(18.dp)
        )
        .clip(RoundedCornerShape(18.dp))
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(text= "견출공", style = AppTypography.bodyMedium)
        }
      }
    }

    Spacer(modifier = Modifier.padding(8.dp))

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp, end = 16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = {
        doJobSelect("CONSTRUCTION_CARPENTER")
      }, modifier = Modifier
        .width((screenWeight * .28).dp)
        .height(40.dp)
        .border(
          width = 1.dp,
          color = Color.Gray,
          shape = RoundedCornerShape(18.dp)
        )
        .clip(RoundedCornerShape(18.dp))
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(text= "건축목공", style = AppTypography.bodyMedium)
        }
      }
      IconButton(onClick = {
        doJobSelect("WINDOW_DOOR_INSTALLER")
      }, modifier = Modifier
        .width((screenWeight * .28).dp)
        .height(40.dp)
        .border(
          width = 1.dp,
          color = Color.Gray,
          shape = RoundedCornerShape(18.dp)
        )
        .clip(RoundedCornerShape(18.dp))
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(text= "창호공", style = AppTypography.bodyMedium)
        }
      }
      IconButton(onClick = {
        doJobSelect("GLAZIER")
      }, modifier = Modifier
        .width((screenWeight * .28).dp)
        .height(40.dp)
        .border(
          width = 1.dp,
          color = Color.Gray,
          shape = RoundedCornerShape(18.dp)
        )
        .clip(RoundedCornerShape(18.dp))
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(text= "유리공", style = AppTypography.bodyMedium)
        }
      }
    }

    Spacer(modifier = Modifier.padding(8.dp))

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp, end = 16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = {
        doJobSelect("WATERPROOFING_WORKER")
      }, modifier = Modifier
        .width((screenWeight * .28).dp)
        .height(40.dp)
        .border(
          width = 1.dp,
          color = Color.Gray,
          shape = RoundedCornerShape(18.dp)
        )
        .clip(RoundedCornerShape(18.dp))
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(text= "방수공", style = AppTypography.bodyMedium)
        }
      }
      IconButton(onClick = {
        doJobSelect("PLASTERER")
      }, modifier = Modifier
        .width((screenWeight * .28).dp)
        .height(40.dp)
        .border(
          width = 1.dp,
          color = Color.Gray,
          shape = RoundedCornerShape(18.dp)
        )
        .clip(RoundedCornerShape(18.dp))
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(text= "미장공", style = AppTypography.bodyMedium)
        }
      }
      IconButton(onClick = {
        doJobSelect("TILE")
      }, modifier = Modifier
        .width((screenWeight * .28).dp)
        .height(40.dp)
        .border(
          width = 1.dp,
          color = Color.Gray,
          shape = RoundedCornerShape(18.dp)
        )
        .clip(RoundedCornerShape(18.dp))
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(text= "타일공", style = AppTypography.bodyMedium)
        }
      }
    }

    Spacer(modifier = Modifier.padding(8.dp))

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp, end = 16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = {
        doJobSelect("PAINTER")
      }, modifier = Modifier
        .width((screenWeight * .28).dp)
        .height(40.dp)
        .border(
          width = 1.dp,
          color = Color.Gray,
          shape = RoundedCornerShape(18.dp)
        )
        .clip(RoundedCornerShape(18.dp))
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(text= "도장공", style = AppTypography.bodyMedium)
        }

      }
      IconButton(onClick = {
        doJobSelect("INTERIOR_FINISHER")
      }, modifier = Modifier
        .width((screenWeight * .28).dp)
        .height(40.dp)
        .border(
          width = 1.dp,
          color = Color.Gray,
          shape = RoundedCornerShape(18.dp)
        )
        .clip(RoundedCornerShape(18.dp))
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(text= "내장공", style = AppTypography.bodyMedium)
        }
      }
      IconButton(onClick = {
        doJobSelect("WALLPAPER_INSTALLER")
      }, modifier = Modifier
        .width((screenWeight * .28).dp)
        .height(40.dp)
        .border(
          width = 1.dp,
          color = Color.Gray,
          shape = RoundedCornerShape(18.dp)
        )
        .clip(RoundedCornerShape(18.dp))
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(text= "도배공", style = AppTypography.bodyMedium)
        }
      }
    }

    Spacer(modifier = Modifier.padding(8.dp))

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp, end = 16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = {
        doJobSelect("POLISHER")
      }, modifier = Modifier
        .width((screenWeight * .28).dp)
        .height(40.dp)
        .border(
          width = 1.dp,
          color = Color.Gray,
          shape = RoundedCornerShape(18.dp)
        )
        .clip(RoundedCornerShape(18.dp))
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(text= "연마공", style = AppTypography.bodyMedium)
        }
      }
      IconButton(onClick = {
        doJobSelect("STONEMASON")
      }, modifier = Modifier
        .width((screenWeight * .28).dp)
        .height(40.dp)
        .border(
          width = 1.dp,
          color = Color.Gray,
          shape = RoundedCornerShape(18.dp)
        )
        .clip(RoundedCornerShape(18.dp))
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(text= "석공", style = AppTypography.bodyMedium)
        }
      }
      IconButton(onClick = {
        doJobSelect("GROUT_WORKER")
      }, modifier = Modifier
        .width((screenWeight * .28).dp)
        .height(40.dp)
        .border(
          width = 1.dp,
          color = Color.Gray,
          shape = RoundedCornerShape(18.dp)
        )
        .clip(RoundedCornerShape(18.dp))
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(text= "줄눈공", style = AppTypography.bodyMedium)
        }
      }
    }

    Spacer(modifier = Modifier.padding(8.dp))

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp, end = 16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = {
        doJobSelect("PANEL_ASSEMBLER")
      }, modifier = Modifier
        .width((screenWeight * .28).dp)
        .height(40.dp)
        .border(
          width = 1.dp,
          color = Color.Gray,
          shape = RoundedCornerShape(18.dp)
        )
        .clip(RoundedCornerShape(18.dp))
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(text= "판넬조립공", style = AppTypography.bodyMedium)
        }
      }
      IconButton(onClick = {
        doJobSelect("ROOFER")
      }, modifier = Modifier
        .width((screenWeight * .28).dp)
        .height(40.dp)
        .border(
          width = 1.dp,
          color = Color.Gray,
          shape = RoundedCornerShape(18.dp)
        )
        .clip(RoundedCornerShape(18.dp))
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(text= "지붕잇기공", style = AppTypography.bodyMedium)
        }
      }
      IconButton(onClick = {
        doJobSelect("LANDSCAPER")
      }, modifier = Modifier
        .width((screenWeight * .28).dp)
        .height(40.dp)
        .border(
          width = 1.dp,
          color = Color.Gray,
          shape = RoundedCornerShape(18.dp)
        )
        .clip(RoundedCornerShape(18.dp))
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(text= "조경공", style = AppTypography.bodyMedium)
        }
      }
    }

    Spacer(modifier = Modifier.padding(8.dp))

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp, end = 16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = {
        doJobSelect("CAULKER")
      }, modifier = Modifier
        .width((screenWeight * .28).dp)
        .height(40.dp)
        .border(
          width = 1.dp,
          color = Color.Gray,
          shape = RoundedCornerShape(18.dp)
        )
        .clip(RoundedCornerShape(18.dp))
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(text= "코킹공", style = AppTypography.bodyMedium)
        }
      }
      IconButton(onClick = {
        doJobSelect("PLUMBER")
      }, modifier = Modifier
        .width((screenWeight * .28).dp)
        .height(40.dp)
        .border(
          width = 1.dp,
          color = Color.Gray,
          shape = RoundedCornerShape(18.dp)
        )
        .clip(RoundedCornerShape(18.dp))
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(text= "배관공", style = AppTypography.bodyMedium)
        }
      }
      IconButton(onClick = {
        doJobSelect("BOILER_TECHNICIAN")
      }, modifier = Modifier
        .width((screenWeight * .28).dp)
        .height(40.dp)
        .border(
          width = 1.dp,
          color = Color.Gray,
          shape = RoundedCornerShape(18.dp)
        )
        .clip(RoundedCornerShape(18.dp))
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(text= "보일러공", style = AppTypography.bodyMedium)
        }
      }
    }

    Spacer(modifier = Modifier.padding(8.dp))

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp, end = 16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = {
        doJobSelect("SANITARY_TECHNICIAN")
      }, modifier = Modifier
        .width((screenWeight * .28).dp)
        .height(40.dp)
        .border(
          width = 1.dp,
          color = Color.Gray,
          shape = RoundedCornerShape(18.dp)
        )
        .clip(RoundedCornerShape(18.dp))
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(text= "위생공", style = AppTypography.bodyMedium)
        }
      }
      IconButton(onClick = {
        doJobSelect("DUCT_INSTALLER")
      }, modifier = Modifier
        .width((screenWeight * .28).dp)
        .height(40.dp)
        .border(
          width = 1.dp,
          color = Color.Gray,
          shape = RoundedCornerShape(18.dp)
        )
        .clip(RoundedCornerShape(18.dp))
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(text= "덕트공", style = AppTypography.bodyMedium)
        }
      }
      IconButton(onClick = {
        doJobSelect("INSULATION_WORKER")
      }, modifier = Modifier
        .width((screenWeight * .28).dp)
        .height(40.dp)
        .border(
          width = 1.dp,
          color = Color.Gray,
          shape = RoundedCornerShape(18.dp)
        )
        .clip(RoundedCornerShape(18.dp))
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(text= "보온공", style = AppTypography.bodyMedium)
        }
      }
    }

    Spacer(modifier = Modifier.padding(8.dp))

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp, end = 16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = {
        doJobSelect("MECHANICAL_EQUIPMENT_TECHNICIAN")
      }, modifier = Modifier
        .width((screenWeight * .28).dp)
        .height(40.dp)
        .border(
          width = 1.dp,
          color = Color.Gray,
          shape = RoundedCornerShape(18.dp)
        )
        .clip(RoundedCornerShape(18.dp))
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(text= "기계설비공", style = AppTypography.bodyMedium)
        }
      }
      IconButton(onClick = {
        doJobSelect("ELECTRICIAN")
      }, modifier = Modifier
        .width((screenWeight * .28).dp)
        .height(40.dp)
        .border(
          width = 1.dp,
          color = Color.Gray,
          shape = RoundedCornerShape(18.dp)
        )
        .clip(RoundedCornerShape(18.dp))
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(text= "내선진공", style = AppTypography.bodyMedium)
        }
      }
      IconButton(onClick = {
        doJobSelect("TELECOMMUNICATIONS_INSTALLER")
      }, modifier = Modifier
        .width((screenWeight * .28).dp)
        .height(40.dp)
        .border(
          width = 1.dp,
          color = Color.Gray,
          shape = RoundedCornerShape(18.dp)
        )
        .clip(RoundedCornerShape(18.dp))
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(text= "통신내선공", style = AppTypography.bodyMedium)
        }
      }
    }

    Spacer(modifier = Modifier.padding(8.dp))

    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp, end = 16.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = {
        doJobSelect("TELECOMMUNICATIONS_EQUIPMENT_INSTALLER")
      }, modifier = Modifier
        .width((screenWeight * .28).dp)
        .height(40.dp)
        .border(
          width = 1.dp,
          color = Color.Gray,
          shape = RoundedCornerShape(18.dp)
        )
        .clip(RoundedCornerShape(18.dp))
      ) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Text(text= "통신설비공", style = AppTypography.bodyMedium)
        }
      }
    }
  }
}
