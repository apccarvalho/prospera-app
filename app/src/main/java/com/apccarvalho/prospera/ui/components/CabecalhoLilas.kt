package com.apccarvalho.prospera.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.apccarvalho.prospera.ui.theme.Branco
import com.apccarvalho.prospera.ui.theme.LilasMedio
import com.apccarvalho.prospera.ui.theme.ProsperaTheme

@Composable
fun CabecalhoLilas(
    altura: Dp,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(altura)
            .clip(RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp))
            .background(LilasMedio),
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 70.dp, y = (-70).dp)
                .size(220.dp)
                .background(Branco.copy(alpha = 0.18f), CircleShape),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFAF8FF)
@Composable
private fun CabecalhoLilasPreview() {
    ProsperaTheme {
        CabecalhoLilas(altura = 220.dp)
    }
}
