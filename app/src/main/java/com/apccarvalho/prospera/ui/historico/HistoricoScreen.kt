package com.apccarvalho.prospera.ui.historico

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.apccarvalho.prospera.R
import com.apccarvalho.prospera.ui.theme.Ameixa
import com.apccarvalho.prospera.ui.theme.FundoLavanda
import com.apccarvalho.prospera.ui.theme.ProsperaTheme

@Composable
fun HistoricoScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(FundoLavanda)
            .safeDrawingPadding(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.historico_titulo),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Ameixa,
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HistoricoScreenPreview() {
    ProsperaTheme {
        HistoricoScreen()
    }
}
