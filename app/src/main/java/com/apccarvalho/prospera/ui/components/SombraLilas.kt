package com.apccarvalho.prospera.ui.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.apccarvalho.prospera.ui.theme.Roxo

fun Modifier.sombraLilas(forma: Shape, elevacao: Dp = 10.dp): Modifier = shadow(
    elevation = elevacao,
    shape = forma,
    ambientColor = Roxo.copy(alpha = 0.22f),
    spotColor = Roxo.copy(alpha = 0.22f),
)
