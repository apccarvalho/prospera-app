package com.apccarvalho.prospera.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apccarvalho.prospera.R
import com.apccarvalho.prospera.domain.model.Categoria
import com.apccarvalho.prospera.domain.model.StatusConsumo
import com.apccarvalho.prospera.domain.util.Dinheiro
import com.apccarvalho.prospera.domain.util.percentualDe
import com.apccarvalho.prospera.domain.util.resumirCategorias
import com.apccarvalho.prospera.ui.components.BarraProgresso
import com.apccarvalho.prospera.ui.components.CabecalhoLilas
import com.apccarvalho.prospera.ui.components.CardCategoria
import com.apccarvalho.prospera.ui.components.EspacoBotaoFlutuante
import com.apccarvalho.prospera.ui.components.ExibirMensagemRecebida
import com.apccarvalho.prospera.ui.components.SeloPercentual
import com.apccarvalho.prospera.ui.components.percentualInteiro
import com.apccarvalho.prospera.ui.components.sombraLilas
import com.apccarvalho.prospera.ui.theme.Ameixa
import com.apccarvalho.prospera.ui.theme.Branco
import com.apccarvalho.prospera.ui.theme.Contorno
import com.apccarvalho.prospera.ui.theme.FundoLavanda
import com.apccarvalho.prospera.ui.theme.ProsperaTheme
import com.apccarvalho.prospera.ui.theme.StatusColors
import com.apccarvalho.prospera.ui.theme.TextoSecundario

private val PaddingHorizontal = 20.dp
private val FormatoResumo = RoundedCornerShape(24.dp)

@Composable
fun DashboardScreen(
    uiState: DashboardUiState,
    aoSair: () -> Unit,
    modifier: Modifier = Modifier,
    mensagem: String? = null,
    aoMensagemExibida: () -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }
    ExibirMensagemRecebida(mensagem, snackbarHostState, aoMensagemExibida)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(FundoLavanda),
    ) {
        ConteudoDashboard(uiState = uiState, aoSair = aoSair)
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = EspacoBotaoFlutuante),
        )
    }
}

@Composable
private fun ConteudoDashboard(
    uiState: DashboardUiState,
    aoSair: () -> Unit,
) {
    // O cabeçalho ocupa a largura toda, então o padding horizontal é aplicado item a item
    // em vez de no contentPadding da LazyColumn.
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item(key = "topo") {
            TopoDashboard(uiState = uiState, aoSair = aoSair)
        }
        item(key = "titulo-categorias") {
            TituloSecao(Modifier.padding(horizontal = PaddingHorizontal))
        }
        items(uiState.categorias, key = { it.categoria.name }) { resumo ->
            CardCategoria(
                resumo = resumo,
                modifier = Modifier
                    .padding(horizontal = PaddingHorizontal)
                    .animateItem(),
            )
        }
        item(key = "espaco-fab") {
            Spacer(Modifier.height(EspacoBotaoFlutuante + 16.dp))
        }
    }
}

@Composable
private fun TopoDashboard(
    uiState: DashboardUiState,
    aoSair: () -> Unit,
) {
    val alturaBarraStatus = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    Box(modifier = Modifier.fillMaxWidth()) {
        CabecalhoLilas(altura = 170.dp + alturaBarraStatus)
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(start = PaddingHorizontal, end = PaddingHorizontal, top = 16.dp),
        ) {
            Saudacao(
                primeiroNome = uiState.primeiroNome,
                nomeMes = uiState.nomeMes,
                aoSair = aoSair,
            )
            Spacer(Modifier.height(20.dp))
            CardResumo(uiState)
        }
    }
}

@Composable
private fun Saudacao(
    primeiroNome: String?,
    nomeMes: String,
    aoSair: () -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(R.drawable.ic_girassol),
            contentDescription = null,
            modifier = Modifier.size(40.dp),
        )
        Spacer(Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = if (primeiroNome != null) {
                    stringResource(R.string.dashboard_ola_nome, primeiroNome)
                } else {
                    stringResource(R.string.dashboard_ola)
                },
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Ameixa,
            )
            Text(
                text = nomeMes,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = TextoSecundario,
            )
        }
        IconButton(
            onClick = aoSair,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = Branco,
                contentColor = Ameixa,
            ),
            modifier = Modifier.size(40.dp),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.Logout,
                contentDescription = stringResource(R.string.dashboard_sair),
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

@Composable
private fun CardResumo(uiState: DashboardUiState) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .sombraLilas(FormatoResumo, elevacao = 14.dp)
            .background(Branco, FormatoResumo)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.dashboard_total_gasto),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextoSecundario,
                )
                Text(
                    text = Dinheiro.formatar(uiState.totalGastoCentavos),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Ameixa,
                )
                Text(
                    text = stringResource(
                        R.string.dashboard_percentual_do_teto,
                        percentualInteiro(uiState.percentualGeral),
                        Dinheiro.formatar(uiState.tetoTotalCentavos),
                    ),
                    fontSize = 13.sp,
                    color = TextoSecundario,
                )
            }
            SeloPercentual(percentual = uiState.percentualGeral, status = uiState.statusGeral)
        }

        BarraProgresso(percentual = uiState.percentualGeral, status = uiState.statusGeral)

        HorizontalDivider(thickness = 1.dp, color = Contorno)

        Row {
            IndicadorResumo(
                rotulo = stringResource(R.string.dashboard_saldo_disponivel),
                valor = Dinheiro.formatar(uiState.saldoCentavos),
                corValor = if (uiState.saldoCentavos < 0) {
                    StatusColors.Estourado.texto
                } else {
                    StatusColors.Ok.texto
                },
                modifier = Modifier.weight(1f),
            )
            IndicadorResumo(
                rotulo = stringResource(R.string.dashboard_compras_no_mes),
                valor = uiState.quantidadeCompras.toString(),
                corValor = Ameixa,
                modifier = Modifier.weight(1f),
            )
        }

        if (!uiState.carregando && uiState.quantidadeCompras == 0) {
            Text(
                text = stringResource(R.string.dashboard_sem_compras),
                fontSize = 12.sp,
                color = TextoSecundario,
            )
        }
    }
}

@Composable
private fun IndicadorResumo(
    rotulo: String,
    valor: String,
    corValor: Color,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = rotulo,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = TextoSecundario,
        )
        Text(
            text = valor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = corValor,
        )
    }
}

@Composable
private fun TituloSecao(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(R.string.dashboard_por_categoria),
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = Ameixa,
        )
        Text(
            text = stringResource(R.string.dashboard_maior_consumo),
            fontSize = 12.sp,
            color = TextoSecundario,
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

private fun estadoExemplo(
    totais: Map<Categoria, Long>,
    quantidadeCompras: Int,
): DashboardUiState {
    val totalGasto = totais.values.sum()
    val tetoTotal = TetosExemplo.values.sum()
    val percentual = percentualDe(totalGasto, tetoTotal)
    return DashboardUiState(
        carregando = false,
        primeiroNome = "Andréia",
        nomeMes = "Novembro de 2026",
        totalGastoCentavos = totalGasto,
        tetoTotalCentavos = tetoTotal,
        percentualGeral = percentual,
        statusGeral = StatusConsumo.doPercentual(percentual),
        saldoCentavos = tetoTotal - totalGasto,
        quantidadeCompras = quantidadeCompras,
        categorias = resumirCategorias(TetosExemplo, totais),
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DashboardScreenPreview() {
    ProsperaTheme {
        DashboardScreen(
            uiState = estadoExemplo(
                totais = mapOf(
                    Categoria.FARMACIA to 28_250L,
                    Categoria.SUPERMERCADO to 103_280L,
                    Categoria.LAZER to 21_600L,
                    Categoria.OUTROS to 34_410L,
                    Categoria.COMBUSTIVEL to 26_000L,
                ),
                quantidadeCompras = 23,
            ),
            aoSair = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DashboardScreenSemComprasPreview() {
    ProsperaTheme {
        DashboardScreen(
            uiState = estadoExemplo(totais = emptyMap(), quantidadeCompras = 0),
            aoSair = {},
        )
    }
}
