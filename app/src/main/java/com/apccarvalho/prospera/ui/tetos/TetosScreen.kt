package com.apccarvalho.prospera.ui.tetos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apccarvalho.prospera.R
import com.apccarvalho.prospera.domain.model.Categoria
import com.apccarvalho.prospera.domain.util.Dinheiro
import com.apccarvalho.prospera.ui.components.BotaoPrimario
import com.apccarvalho.prospera.ui.components.CampoValor
import com.apccarvalho.prospera.ui.theme.Ameixa
import com.apccarvalho.prospera.ui.theme.Branco
import com.apccarvalho.prospera.ui.theme.Contorno
import com.apccarvalho.prospera.ui.theme.FundoLavanda
import com.apccarvalho.prospera.ui.theme.ProsperaTheme
import com.apccarvalho.prospera.ui.theme.StatusColors
import com.apccarvalho.prospera.ui.theme.TextoSecundario
import kotlin.math.roundToInt

@Composable
fun TetosScreen(
    uiState: TetosUiState,
    aoAlterarRenda: (Long) -> Unit,
    aoAlterarTeto: (Categoria, Long) -> Unit,
    aoSalvar: () -> Unit,
    aoMensagemInformativaExibida: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(uiState.mensagemInformativa) {
        uiState.mensagemInformativa?.let { mensagem ->
            snackbarHostState.showSnackbar(mensagem)
            aoMensagemInformativaExibida()
        }
    }

    val camposHabilitados = !uiState.carregando && !uiState.salvando

    Scaffold(
        containerColor = FundoLavanda,
        contentWindowInsets = WindowInsets(0),
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding(),
        ) {
            Column(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = stringResource(
                        if (uiState.primeiroAcesso) R.string.tetos_titulo_primeiro_acesso else R.string.tetos_titulo_edicao
                    ),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Ameixa,
                )
                Text(
                    text = stringResource(R.string.tetos_subtitulo),
                    fontSize = 14.sp,
                    color = TextoSecundario,
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                CampoValor(
                    centavos = uiState.rendaCentavos,
                    aoAlterar = aoAlterarRenda,
                    rotulo = stringResource(R.string.tetos_renda_rotulo),
                    erro = uiState.erroRenda,
                    textoApoio = stringResource(R.string.tetos_renda_apoio),
                    habilitado = camposHabilitados,
                )
                HorizontalDivider(thickness = 1.dp, color = Contorno)
                Text(
                    text = stringResource(R.string.tetos_por_categoria),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Ameixa,
                )
                val categorias = Categoria.entries
                categorias.forEach { categoria ->
                    CampoValor(
                        centavos = uiState.tetos[categoria] ?: 0L,
                        aoAlterar = { aoAlterarTeto(categoria, it) },
                        rotulo = categoria.rotulo,
                        erro = uiState.errosTetos[categoria],
                        habilitado = camposHabilitados,
                        acaoTeclado = if (categoria == categorias.last()) ImeAction.Done else ImeAction.Next,
                    )
                }
                Spacer(Modifier.height(8.dp))
            }

            RodapeTetos(
                tetoTotalCentavos = uiState.tetoTotalCentavos,
                percentualDaRenda = uiState.percentualDaRenda,
                salvando = uiState.salvando,
                habilitado = !uiState.carregando,
                aoSalvar = aoSalvar,
            )
        }
    }
}

@Composable
private fun RodapeTetos(
    tetoTotalCentavos: Long,
    percentualDaRenda: Double?,
    salvando: Boolean,
    habilitado: Boolean,
    aoSalvar: () -> Unit,
) {
    Surface(
        color = Branco,
        shadowElevation = 12.dp,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(R.string.tetos_total),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextoSecundario,
                )
                Text(
                    text = Dinheiro.formatar(tetoTotalCentavos),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Ameixa,
                )
            }
            if (percentualDaRenda != null) {
                AvisoRenda(percentualDaRenda)
            }
            BotaoPrimario(
                texto = stringResource(R.string.tetos_salvar),
                aoClicar = aoSalvar,
                habilitado = habilitado,
                carregando = salvando,
            )
        }
    }
}

@Composable
private fun AvisoRenda(percentualDaRenda: Double) {
    val acimaDaRenda = percentualDaRenda > 1.0
    val cor = if (acimaDaRenda) StatusColors.Estourado.texto else StatusColors.Ok.texto
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Icon(
            imageVector = if (acimaDaRenda) Icons.Rounded.Warning else Icons.Rounded.CheckCircle,
            contentDescription = null,
            tint = cor,
            modifier = Modifier.size(16.dp),
        )
        Text(
            text = stringResource(R.string.tetos_aviso_renda, (percentualDaRenda * 100).roundToInt()),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = cor,
        )
    }
}

private val TetosExemplo = mapOf(
    Categoria.SUPERMERCADO to 120_000L,
    Categoria.COMBUSTIVEL to 50_000L,
    Categoria.FARMACIA to 25_000L,
    Categoria.LAZER to 30_000L,
    Categoria.OUTROS to 65_000L,
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TetosScreenPrimeiroAcessoPreview() {
    ProsperaTheme {
        TetosScreen(
            uiState = TetosUiState(
                primeiroAcesso = true,
                rendaCentavos = 340_000,
                tetos = TetosExemplo,
                carregando = false,
            ),
            aoAlterarRenda = {},
            aoAlterarTeto = { _, _ -> },
            aoSalvar = {},
            aoMensagemInformativaExibida = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TetosScreenEdicaoPreview() {
    ProsperaTheme {
        TetosScreen(
            uiState = TetosUiState(
                primeiroAcesso = false,
                rendaCentavos = 340_000,
                tetos = TetosExemplo,
                carregando = false,
            ),
            aoAlterarRenda = {},
            aoAlterarTeto = { _, _ -> },
            aoSalvar = {},
            aoMensagemInformativaExibida = {},
        )
    }
}
