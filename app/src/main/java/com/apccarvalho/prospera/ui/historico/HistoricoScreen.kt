package com.apccarvalho.prospera.ui.historico

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apccarvalho.prospera.R
import com.apccarvalho.prospera.domain.model.Categoria
import com.apccarvalho.prospera.domain.model.Compra
import com.apccarvalho.prospera.domain.model.FormaPagamento
import com.apccarvalho.prospera.domain.util.Dinheiro
import com.apccarvalho.prospera.ui.components.ChipOpcao
import com.apccarvalho.prospera.ui.components.EspacoBotaoFlutuante
import com.apccarvalho.prospera.ui.components.ExibirMensagemRecebida
import com.apccarvalho.prospera.ui.components.sombraLilas
import com.apccarvalho.prospera.ui.theme.Ameixa
import com.apccarvalho.prospera.ui.theme.Branco
import com.apccarvalho.prospera.ui.theme.Contorno
import com.apccarvalho.prospera.ui.theme.FundoLavanda
import com.apccarvalho.prospera.ui.theme.LilasClaro
import com.apccarvalho.prospera.ui.theme.ProsperaTheme
import com.apccarvalho.prospera.ui.theme.TextoSecundario
import java.time.LocalDate

private val PaddingHorizontal = 20.dp
private val FormatoCard = RoundedCornerShape(20.dp)

@Composable
fun HistoricoScreen(
    uiState: HistoricoUiState,
    aoSelecionarFiltro: (Categoria?) -> Unit,
    aoAbrirCompra: (Long) -> Unit,
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
        Column(modifier = Modifier.fillMaxSize()) {
            Cabecalho(uiState)
            Filtros(filtro = uiState.filtro, aoSelecionar = aoSelecionarFiltro)
            Spacer(Modifier.height(16.dp))
            if (!uiState.carregando && uiState.grupos.isEmpty()) {
                EstadoVazio(
                    filtro = uiState.filtro,
                    modifier = Modifier.weight(1f),
                )
            } else {
                ListaGrupos(
                    grupos = uiState.grupos,
                    aoAbrirCompra = aoAbrirCompra,
                    modifier = Modifier.weight(1f),
                )
            }
        }
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = EspacoBotaoFlutuante),
        )
    }
}

@Composable
private fun Cabecalho(uiState: HistoricoUiState) {
    Column(
        modifier = Modifier
            .statusBarsPadding()
            .padding(start = PaddingHorizontal, end = PaddingHorizontal, top = 24.dp, bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = stringResource(R.string.historico_titulo),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Ameixa,
        )
        Text(
            text = stringResource(
                R.string.historico_subtitulo,
                uiState.nomeMes,
                pluralStringResource(
                    R.plurals.historico_quantidade_compras,
                    uiState.quantidadeCompras,
                    uiState.quantidadeCompras,
                ),
                Dinheiro.formatar(uiState.totalCentavos),
            ),
            fontSize = 14.sp,
            color = TextoSecundario,
        )
    }
}

@Composable
private fun Filtros(
    filtro: Categoria?,
    aoSelecionar: (Categoria?) -> Unit,
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = PaddingHorizontal),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item(key = "todas") {
            ChipOpcao(
                texto = stringResource(R.string.historico_filtro_todas),
                selecionado = filtro == null,
                aoClicar = { aoSelecionar(null) },
            )
        }
        items(Categoria.entries, key = { it.name }) { categoria ->
            ChipOpcao(
                texto = categoria.rotuloCurto,
                selecionado = filtro == categoria,
                aoClicar = { aoSelecionar(categoria) },
            )
        }
    }
}

@Composable
private fun ListaGrupos(
    grupos: List<GrupoDia>,
    aoAbrirCompra: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = PaddingHorizontal,
            end = PaddingHorizontal,
            top = 4.dp,
            bottom = EspacoBotaoFlutuante + 16.dp,
        ),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        items(grupos, key = { it.data.toEpochDay() }) { grupo ->
            Column(
                modifier = Modifier.animateItem(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                CabecalhoDia(grupo)
                CardDia(compras = grupo.compras, aoAbrirCompra = aoAbrirCompra)
            }
        }
    }
}

@Composable
private fun CabecalhoDia(grupo: GrupoDia) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = grupo.rotulo,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Ameixa,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = Dinheiro.formatar(grupo.totalCentavos),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = TextoSecundario,
        )
    }
}

@Composable
private fun CardDia(
    compras: List<Compra>,
    aoAbrirCompra: (Long) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .sombraLilas(FormatoCard)
            .clip(FormatoCard)
            .background(Branco),
    ) {
        compras.forEachIndexed { indice, compra ->
            if (indice > 0) {
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Contorno,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )
            }
            LinhaCompra(compra = compra, aoClicar = { aoAbrirCompra(compra.id) })
        }
    }
}

@Composable
private fun LinhaCompra(
    compra: Compra,
    aoClicar: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = aoClicar)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(LilasClaro, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = compra.categoria.icone,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(22.dp),
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 12.dp),
        ) {
            Text(
                text = compra.estabelecimento,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Ameixa,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = stringResource(
                    R.string.historico_detalhe_compra,
                    compra.categoria.rotuloCurto,
                    compra.formaPagamento.rotulo,
                ),
                fontSize = 12.sp,
                color = TextoSecundario,
            )
        }
        Text(
            text = Dinheiro.formatar(compra.valorCentavos),
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Ameixa,
        )
    }
}

@Composable
private fun EstadoVazio(
    filtro: Categoria?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp)
            .padding(bottom = EspacoBotaoFlutuante),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = if (filtro == null) {
                stringResource(R.string.historico_vazio)
            } else {
                stringResource(R.string.historico_vazio_categoria, filtro.rotuloCurto)
            },
            fontSize = 14.sp,
            color = TextoSecundario,
            textAlign = TextAlign.Center,
        )
    }
}

private val HojeExemplo = LocalDate.of(2026, 11, 4)

private fun compraExemplo(
    id: Long,
    estabelecimento: String,
    categoria: Categoria,
    formaPagamento: FormaPagamento,
    valorCentavos: Long,
    diasAtras: Long,
) = Compra(
    id = id,
    userId = "preview",
    valorCentavos = valorCentavos,
    categoria = categoria,
    estabelecimento = estabelecimento,
    formaPagamento = formaPagamento,
    data = HojeExemplo.minusDays(diasAtras),
    criadoEm = -id,
)

private fun estadoExemplo(compras: List<Compra>, filtro: Categoria? = null) = HistoricoUiState(
    carregando = false,
    nomeMes = "Novembro de 2026",
    quantidadeCompras = compras.size,
    totalCentavos = compras.sumOf { it.valorCentavos },
    filtro = filtro,
    grupos = agruparPorDia(compras, HojeExemplo),
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HistoricoScreenPreview() {
    val compras = listOf(
        compraExemplo(1, "Drogaria Minas", Categoria.FARMACIA, FormaPagamento.DEBITO, 4_790, 0),
        compraExemplo(2, "Posto Petrobras", Categoria.COMBUSTIVEL, FormaPagamento.CREDITO, 15_000, 0),
        compraExemplo(3, "Supermercado Central", Categoria.SUPERMERCADO, FormaPagamento.PIX, 23_465, 1),
        compraExemplo(4, "Cinema do Shopping", Categoria.LAZER, FormaPagamento.CREDITO, 6_400, 1),
        compraExemplo(5, "Padaria Pão Quente", Categoria.SUPERMERCADO, FormaPagamento.DINHEIRO, 1_850, 2),
        compraExemplo(6, "Conta de internet", Categoria.OUTROS, FormaPagamento.BOLETO, 9_990, 2),
        compraExemplo(7, "Feira do Bairro", Categoria.SUPERMERCADO, FormaPagamento.PIX, 3_720, 3),
    )
    ProsperaTheme {
        HistoricoScreen(
            uiState = estadoExemplo(compras),
            aoSelecionarFiltro = {},
            aoAbrirCompra = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HistoricoScreenVazioPreview() {
    ProsperaTheme {
        HistoricoScreen(
            uiState = estadoExemplo(emptyList()),
            aoSelecionarFiltro = {},
            aoAbrirCompra = {},
        )
    }
}
