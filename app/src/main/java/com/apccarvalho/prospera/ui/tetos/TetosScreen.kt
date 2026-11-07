package com.apccarvalho.prospera.ui.tetos

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apccarvalho.prospera.R
import com.apccarvalho.prospera.ui.components.BotaoPrimario
import com.apccarvalho.prospera.ui.theme.FundoLavanda
import com.apccarvalho.prospera.ui.theme.ProsperaTheme

@Composable
fun TetosScreen(
    uiState: TetosUiState,
    aoConcluir: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoLavanda)
            .safeDrawingPadding()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.tetos_titulo),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
        BotaoPrimario(
            texto = stringResource(R.string.tetos_concluir),
            aoClicar = aoConcluir,
            carregando = uiState.salvando,
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TetosScreenPreview() {
    ProsperaTheme {
        TetosScreen(uiState = TetosUiState(), aoConcluir = {})
    }
}
