package com.apccarvalho.prospera.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apccarvalho.prospera.ui.theme.Branco
import com.apccarvalho.prospera.ui.theme.Roxo
import com.apccarvalho.prospera.ui.theme.TextoSecundario

private val FormatoCartao = RoundedCornerShape(24.dp)

/**
 * Coluna rolável com altura mínima igual à da tela: permite usar weight para empurrar o
 * rodapé para baixo e, com o teclado aberto, faz o conteúdo rolar em vez de ser cortado.
 */
@Composable
internal fun ColunaAuth(
    modifier: Modifier = Modifier,
    conteudo: @Composable ColumnScope.() -> Unit,
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val alturaMinima = maxHeight
        Box(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .verticalScroll(rememberScrollState()),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = alturaMinima)
                    .padding(horizontal = 24.dp),
                content = conteudo,
            )
        }
    }
}

@Composable
internal fun CartaoAuth(
    modifier: Modifier = Modifier,
    conteudo: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 16.dp,
                shape = FormatoCartao,
                ambientColor = Roxo.copy(alpha = 0.25f),
                spotColor = Roxo.copy(alpha = 0.25f),
            )
            .background(Branco, FormatoCartao)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        content = conteudo,
    )
}

@Composable
internal fun MensagemErroGeral(mensagem: String?) {
    if (mensagem != null) {
        Text(
            text = mensagem,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
internal fun LinkRodape(
    pergunta: String,
    acao: String,
    aoClicar: () -> Unit,
    habilitado: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = pergunta, fontSize = 14.sp, color = TextoSecundario)
        Text(
            text = acao,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .clickable(enabled = habilitado, onClick = aoClicar)
                .padding(vertical = 8.dp),
        )
    }
}
