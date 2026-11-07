package com.apccarvalho.prospera.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apccarvalho.prospera.domain.model.StatusConsumo
import com.apccarvalho.prospera.ui.theme.LilasClaro
import com.apccarvalho.prospera.ui.theme.ProsperaTheme

@Composable
fun BarraProgresso(
    percentual: Double,
    status: StatusConsumo,
    modifier: Modifier = Modifier,
) {
    val fracao by animateFloatAsState(
        targetValue = percentual.toFloat().coerceIn(0f, 1f),
        label = "progresso",
    )
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(8.dp)
            .clip(CircleShape)
            .background(LilasClaro),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fracao)
                .fillMaxHeight()
                .clip(CircleShape)
                .background(status.cores.barra),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BarraProgressoPreview() {
    ProsperaTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            BarraProgresso(percentual = 0.52, status = StatusConsumo.OK)
            BarraProgresso(percentual = 0.86, status = StatusConsumo.ATENCAO)
            BarraProgresso(percentual = 1.13, status = StatusConsumo.ESTOURADO)
        }
    }
}
