package com.apccarvalho.prospera.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apccarvalho.prospera.R
import com.apccarvalho.prospera.domain.model.StatusConsumo
import kotlin.math.roundToInt

@Composable
fun SeloPercentual(
    percentual: Double,
    status: StatusConsumo,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(R.string.percentual_inteiro, percentualInteiro(percentual)),
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = status.cores.texto,
        modifier = modifier
            .background(status.cores.fundoSelo, CircleShape)
            .padding(horizontal = 10.dp, vertical = 4.dp),
    )
}

fun percentualInteiro(percentual: Double): Int = (percentual * 100).roundToInt()
