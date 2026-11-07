package com.apccarvalho.prospera.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apccarvalho.prospera.ui.theme.Branco
import com.apccarvalho.prospera.ui.theme.ProsperaTheme

@Composable
fun BotaoPrimario(
    texto: String,
    aoClicar: () -> Unit,
    modifier: Modifier = Modifier,
    habilitado: Boolean = true,
    carregando: Boolean = false,
) {
    val primary = MaterialTheme.colorScheme.primary
    // O clique é bloqueado pelo próprio onClick, e não por enabled, para o botão
    // manter a cor primary enquanto carrega.
    Button(
        onClick = { if (!carregando) aoClicar() },
        enabled = habilitado,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = primary,
            contentColor = Branco,
            disabledContainerColor = primary.copy(alpha = 0.4f),
            disabledContentColor = Branco,
        ),
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
    ) {
        if (carregando) {
            CircularProgressIndicator(
                color = Branco,
                strokeWidth = 2.dp,
                modifier = Modifier.size(22.dp),
            )
        } else {
            Text(text = texto, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFAF8FF)
@Composable
private fun BotaoPrimarioPreview() {
    ProsperaTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            BotaoPrimario(texto = "Entrar", aoClicar = {})
            BotaoPrimario(texto = "Entrar", aoClicar = {}, carregando = true)
            BotaoPrimario(texto = "Entrar", aoClicar = {}, habilitado = false)
        }
    }
}
