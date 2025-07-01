package com.billcorea.jikgong.presentation.worker.auth.join.page3.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.billcorea.jikgong.R
import com.billcorea.jikgong.ui.theme.AppTypography
import com.billcorea.jikgong.ui.theme.appColorScheme

@Composable
fun BankSelectList(
  doBankSelect:(bankCode : String) -> Unit,
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
        text = stringResource(R.string.selectBank),
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
        .padding(start = 8.dp, end = 8.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = {
        doBankSelect("006")
      }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Image(
            painter = painterResource(R.drawable.ic_kb_v2),
            contentDescription = "KB",
            modifier = Modifier.fillMaxWidth()
          )
          Text(text= stringResource(R.string.kbbank), style = AppTypography.bodyMedium)
        }

      }
      IconButton(onClick = {
        doBankSelect("021")
      }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Image(
            painter = painterResource(R.drawable.ic_sinhan_v2),
            contentDescription = "SinHan",
            modifier = Modifier.fillMaxWidth()
          )
          Text(text= stringResource(R.string.shinhan), style = AppTypography.bodyMedium)
        }
      }
      IconButton(onClick = {
        doBankSelect("089")
      }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Image(
            painter = painterResource(R.drawable.ic_kbank_v2),
            contentDescription = "K Bank",
            modifier = Modifier.fillMaxWidth()
          )
          Text(text= stringResource(R.string.kbank), style = AppTypography.bodyMedium)
        }
      }
    }
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(start = 8.dp, end = 8.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = {
        doBankSelect("020")
      }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Image(
            painter = painterResource(R.drawable.ic_woori_v2),
            contentDescription = "WooRi",
            modifier = Modifier.fillMaxWidth()
          )
          Text(text= stringResource(R.string.wooriBank), style = AppTypography.bodyMedium)
        }
      }
      IconButton(onClick = {
        doBankSelect("011")
      }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Image(
            painter = painterResource(R.drawable.ic_nhbank_v2),
            contentDescription = "NongHyub",
            modifier = Modifier.fillMaxWidth()
          )
          Text(text= stringResource(R.string.nhBank), style = AppTypography.bodyMedium)
        }

      }
      IconButton(onClick = {
        doBankSelect("005")
      }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Image(
            painter = painterResource(R.drawable.ic_hana_v2),
            contentDescription = "Hana Bank",
            modifier = Modifier.fillMaxWidth()
          )
          Text(text= stringResource(R.string.hanaBank), style = AppTypography.bodyMedium)
        }
      }
    }
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(start = 8.dp, end = 8.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = {
        doBankSelect("003")
      }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Image(
            painter = painterResource(R.drawable.ic_ibk_v2),
            contentDescription = "ibk",
            modifier = Modifier.fillMaxWidth()
          )
          Text(text= stringResource(R.string.ibkBank), style = AppTypography.bodyMedium)
        }
      }
      IconButton(onClick = {
        doBankSelect("090")
      }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Image(
            painter = painterResource(R.drawable.ic_kakao_v2),
            contentDescription = "kakao bank",
            modifier = Modifier.fillMaxWidth()
          )
          Text(text= stringResource(R.string.kakaoBank), style = AppTypography.bodyMedium)
        }
      }
      IconButton(onClick = {
        doBankSelect("031")
      }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Image(
            painter = painterResource(R.drawable.ic_imbank_v2),
            contentDescription = "IM Bank",
            modifier = Modifier.fillMaxWidth()
          )
          Text(text= stringResource(R.string.imBank), style = AppTypography.bodyMedium)
        }
      }
    }
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(start = 8.dp, end = 8.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = {
        doBankSelect("092")
      }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Image(
            painter = painterResource(R.drawable.ic_toss_v2),
            contentDescription = "toss",
            modifier = Modifier.fillMaxWidth()
          )
          Text(text= stringResource(R.string.tossBank), style = AppTypography.bodyMedium)
        }
      }
      IconButton(onClick = {
        doBankSelect("032")
      }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Image(
            painter = painterResource(R.drawable.ic_bnk_v2),
            contentDescription = "bnk busan bank",
            modifier = Modifier.fillMaxWidth()
          )
          Text(text= stringResource(R.string.bankBusan), style = AppTypography.bodyMedium)
        }

      }
      IconButton(onClick = {
        doBankSelect("023")
      }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Image(
            painter = painterResource(R.drawable.ic_sc_jeil_v2),
            contentDescription = "SC Jeil Bank",
            modifier = Modifier.fillMaxWidth()
          )
          Text(text= stringResource(R.string.scJeil), style = AppTypography.bodyMedium)
        }
      }
    }
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(start = 8.dp, end = 8.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = {
        doBankSelect("045")
      }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Image(
            painter = painterResource(R.drawable.ic_mg_saemaeul_v2),
            contentDescription = "MG Sae ma eul",
            modifier = Modifier.fillMaxWidth()
          )
          Text(text= stringResource(R.string.mgSaemaeul), style = AppTypography.bodyMedium)
        }
      }
      IconButton(onClick = {
        doBankSelect("071")
      }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Image(
            painter = painterResource(R.drawable.ic_post_bank_v2),
            contentDescription = "post bank",
            modifier = Modifier.fillMaxWidth()
          )
          Text(text= stringResource(R.string.postBank), style = AppTypography.bodyMedium)
        }
      }
      IconButton(onClick = {
        doBankSelect("039")
      }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Image(
            painter = painterResource(R.drawable.ic_bnk_v2),
            contentDescription = "BNK GN Bank",
            modifier = Modifier.fillMaxWidth()
          )
          Text(text= stringResource(R.string.gyungNamBank), style = AppTypography.bodyMedium)
        }
      }
    }
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(start = 8.dp, end = 8.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = {
        doBankSelect("034")
      }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Image(
            painter = painterResource(R.drawable.ic_gwangju_v2),
            contentDescription = "GwangJu bank",
            modifier = Modifier.fillMaxWidth()
          )
          Text(text= stringResource(R.string.gwangJuBank), style = AppTypography.bodyMedium)
        }
      }
      IconButton(onClick = {
        doBankSelect("002")
      }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Image(
            painter = painterResource(R.drawable.ic_kdb_sanup_v2),
            contentDescription = "KDB Sanup bank",
            modifier = Modifier.fillMaxWidth()
          )
          Text(text= stringResource(R.string.kdbBank), style = AppTypography.bodyMedium)
        }
      }
      IconButton(onClick = {
        doBankSelect("048")
      }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Image(
            painter = painterResource(R.drawable.ic_cu_v2),
            contentDescription = "CU",
            modifier = Modifier.fillMaxWidth()
          )
          Text(text= stringResource(R.string.cu), style = AppTypography.bodyMedium)
        }
      }
    }
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(start = 8.dp, end = 8.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = {
        doBankSelect("037")
      }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Image(
            painter = painterResource(R.drawable.ic_gwangju_v2),
            contentDescription = "JB bank",
            modifier = Modifier.fillMaxWidth()
          )
          Text(text= stringResource(R.string.jeunBukBank), style = AppTypography.bodyMedium)
        }

      }
      IconButton(onClick = {
        doBankSelect("027")
      }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Image(
            painter = painterResource(R.drawable.ic_citibank_v2),
            contentDescription = "citi bank",
            modifier = Modifier.fillMaxWidth()
          )
          Text(text= stringResource(R.string.citiBank), style = AppTypography.bodyMedium)
        }
      }
      IconButton(onClick = {
        doBankSelect("007")
      }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Image(
            painter = painterResource(R.drawable.ic_suhyub_v2),
            contentDescription = "SH",
            modifier = Modifier.fillMaxWidth()
          )
          Text(text= stringResource(R.string.shBank), style = AppTypography.bodyMedium)
        }
      }
    }
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(start = 8.dp, end = 8.dp),
      horizontalArrangement = Arrangement.Start,
      verticalAlignment = Alignment.CenterVertically
    ) {
      IconButton(onClick = {
        doBankSelect("035")
      }, modifier = Modifier.width((screenWeight * .28).dp).height(60.dp)) {
        Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
        ) {
          Image(
            painter = painterResource(R.drawable.ic_sinhan_v2),
            contentDescription = "Jeju bank",
            modifier = Modifier.fillMaxWidth()
          )
          Text(text= stringResource(R.string.jejuBank), style = AppTypography.bodyMedium)
        }
      }
    }
  }
}