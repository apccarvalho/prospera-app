package com.apccarvalho.prospera.ui.compra

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
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
import com.apccarvalho.prospera.ui.theme.Ameixa
import com.apccarvalho.prospera.ui.theme.Branco
import com.apccarvalho.prospera.ui.theme.FundoLavanda
import com.apccarvalho.prospera.ui.theme.ProsperaTheme

@Composable
fun NovaCompraScreen(
    aoVoltar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(FundoLavanda)
            .safeDrawingPadding(),
    ) {
        IconButton(
            onClick = aoVoltar,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Branco,
                contentColor = Ameixa,
            ),
            modifier = Modifier
                .padding(start = 20.dp, top = 12.dp)
                .size(40.dp),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = stringResource(R.string.voltar),
            )
        }
        Text(
            text = stringResource(R.string.nova_compra),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Ameixa,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun NovaCompraScreenPreview() {
    ProsperaTheme {
        NovaCompraScreen(aoVoltar = {})
    }
}
