package com.apccarvalho.prospera.ui.compra

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.TrendingUp
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apccarvalho.prospera.R
import com.apccarvalho.prospera.domain.model.Categoria
import com.apccarvalho.prospera.domain.model.FormaPagamento
import com.apccarvalho.prospera.domain.model.StatusConsumo
import com.apccarvalho.prospera.domain.util.Dinheiro
import com.apccarvalho.prospera.ui.components.BarraProgresso
import com.apccarvalho.prospera.ui.components.BotaoPrimario
import com.apccarvalho.prospera.ui.components.CampoData
import com.apccarvalho.prospera.ui.components.CampoTexto
import com.apccarvalho.prospera.ui.components.ChipOpcao
import com.apccarvalho.prospera.ui.components.OpcaoCategoria
import com.apccarvalho.prospera.ui.components.percentualInteiro
import com.apccarvalho.prospera.ui.components.sombraLilas
import com.apccarvalho.prospera.ui.theme.Ameixa
import com.apccarvalho.prospera.ui.theme.Branco
import com.apccarvalho.prospera.ui.theme.Erro
import com.apccarvalho.prospera.ui.theme.FundoLavanda
import com.apccarvalho.prospera.ui.theme.LilasClaro
import com.apccarvalho.prospera.ui.theme.Placeholder
import com.apccarvalho.prospera.ui.theme.ProsperaTheme
import com.apccarvalho.prospera.ui.theme.TextoSecundario
import java.time.LocalDate

private val PaddingHorizontal = 20.dp
private val FormatoCardValor = RoundedCornerShape(24.dp)
private val FormatoPrevia = RoundedCornerShape(14.dp)
private const val COLUNAS_CATEGORIA = 3

@Composable
fun NovaCompraScreen(
    uiState: NovaCompraUiState,
    aoAlterarValor: (Long) -> Unit,
    aoSelecionarCategoria: (Categoria) -> Unit,
    aoSelecionarFormaPagamento: (FormaPagamento) -> Unit,
    aoAlterarEstabelecimento: (String) -> Unit,
    aoAlterarData: (LocalDate) -> Unit,
    aoSalvar: () -> Unit,
    aoExcluir: () -> Unit,
    aoVoltar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focoValor = remember { FocusRequester() }
    val teclado = LocalSoftwareKeyboardController.current
    LaunchedEffect(Unit) {
        if (!uiState.modoEdicao) {
            focoValor.requestFocus()
            teclado?.show()
        }
    }
    var confirmandoExclusao by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FundoLavanda)
            .imePadding(),
    ) {
        BarraSuperior(
            titulo = stringResource(
                if (uiState.modoEdicao) R.string.compra_titulo_edicao else R.string.nova_compra
            ),
            aoVoltar = aoVoltar,
            aoExcluir = if (uiState.modoEdicao) {
                { confirmandoExclusao = true }
            } else {
                null
            },
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = PaddingHorizontal, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Column {
                CardValor(
                    centavos = uiState.valorCentavos,
                    aoAlterar = aoAlterarValor,
                    erro = uiState.erroValor != null,
                    modifier = Modifier.focusRequester(focoValor),
                )
                MensagemErro(uiState.erroValor)
            }

            SecaoCategoria(
                selecionada = uiState.categoria,
                erro = uiState.erroCategoria,
                previa = uiState.previa,
                aoSelecionar = aoSelecionarCategoria,
            )

            SecaoFormaPagamento(
                selecionada = uiState.formaPagamento,
                erro = uiState.erroFormaPagamento,
                aoSelecionar = aoSelecionarFormaPagamento,
            )

            CampoTexto(
                valor = uiState.estabelecimento,
                aoAlterar = aoAlterarEstabelecimento,
                rotulo = stringResource(R.string.compra_estabelecimento),
                placeholder = stringResource(R.string.compra_estabelecimento_placeholder),
                erro = uiState.erroEstabelecimento,
                capitalizacao = KeyboardCapitalization.Words,
                acaoTeclado = ImeAction.Done,
            )

            CampoData(
                data = uiState.data,
                aoAlterar = aoAlterarData,
                rotulo = stringResource(R.string.compra_data),
                erro = uiState.erroData,
            )

            Spacer(Modifier.height(4.dp))
        }

        Rodape(
            textoBotao = stringResource(
                if (uiState.modoEdicao) R.string.compra_salvar_alteracoes else R.string.compra_salvar
            ),
            salvando = uiState.salvando,
            aoSalvar = aoSalvar,
        )
    }

    if (confirmandoExclusao) {
        DialogoExclusao(
            aoConfirmar = {
                confirmandoExclusao = false
                aoExcluir()
            },
            aoCancelar = { confirmandoExclusao = false },
        )
    }
}

@Composable
private fun DialogoExclusao(
    aoConfirmar: () -> Unit,
    aoCancelar: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = aoCancelar,
        title = { Text(stringResource(R.string.compra_excluir_titulo)) },
        text = { Text(stringResource(R.string.compra_excluir_texto)) },
        confirmButton = {
            TextButton(onClick = aoConfirmar) {
                Text(stringResource(R.string.compra_excluir_confirmar), color = Erro)
            }
        },
        dismissButton = {
            TextButton(onClick = aoCancelar) {
                Text(stringResource(R.string.cancelar))
            }
        },
    )
}

@Composable
private fun BarraSuperior(
    titulo: String,
    aoVoltar: () -> Unit,
    aoExcluir: (() -> Unit)?,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = PaddingHorizontal, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = aoVoltar,
            colors = IconButtonDefaults.iconButtonColors(
                containerColor = LilasClaro,
                contentColor = Ameixa,
            ),
            modifier = Modifier.size(40.dp),
        ) {
            Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = stringResource(R.string.voltar),
            )
        }
        Spacer(Modifier.width(12.dp))
        Text(
            text = titulo,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Ameixa,
            modifier = Modifier.weight(1f),
        )
        if (aoExcluir != null) {
            IconButton(onClick = aoExcluir, modifier = Modifier.size(40.dp)) {
                Icon(
                    imageVector = Icons.Rounded.Delete,
                    contentDescription = stringResource(R.string.compra_excluir),
                    tint = Erro,
                )
            }
        }
    }
}

@Composable
private fun CardValor(
    centavos: Long,
    aoAlterar: (Long) -> Unit,
    erro: Boolean,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val texto = Dinheiro.formatarSemSimbolo(centavos)
    // Mesma máscara do CampoValor: o texto é sempre reescrito com o cursor no fim.
    var valorCampo by remember(texto) {
        mutableStateOf(TextFieldValue(texto, TextRange(texto.length)))
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .sombraLilas(FormatoCardValor)
            .background(Branco, FormatoCardValor)
            .padding(vertical = 20.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.compra_valor),
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = TextoSecundario,
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "R$",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
            Spacer(Modifier.width(8.dp))
            BasicTextField(
                value = valorCampo,
                onValueChange = { novo ->
                    val novoCentavos = Dinheiro.aplicarDigitacao(centavos, novo.text)
                    if (novoCentavos != centavos) aoAlterar(novoCentavos)
                    val novoTexto = Dinheiro.formatarSemSimbolo(novoCentavos)
                    valorCampo = TextFieldValue(novoTexto, TextRange(novoTexto.length))
                },
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = when {
                        erro -> MaterialTheme.colorScheme.error
                        centavos == 0L -> Placeholder
                        else -> Ameixa
                    },
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                    autoCorrectEnabled = false,
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) },
                ),
                modifier = modifier.width(IntrinsicSize.Min),
            )
        }
    }
}

@Composable
private fun SecaoCategoria(
    selecionada: Categoria?,
    erro: String?,
    previa: PreviaTeto?,
    aoSelecionar: (Categoria) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TituloSecao(stringResource(R.string.compra_categoria), Modifier.weight(1f))
            Text(
                text = stringResource(R.string.compra_obrigatoria),
                fontSize = 12.sp,
                color = TextoSecundario,
            )
        }
        Categoria.entries.chunked(COLUNAS_CATEGORIA).forEach { linha ->
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                linha.forEach { categoria ->
                    OpcaoCategoria(
                        categoria = categoria,
                        selecionada = categoria == selecionada,
                        aoClicar = { aoSelecionar(categoria) },
                        modifier = Modifier.weight(1f),
                    )
                }
                repeat(COLUNAS_CATEGORIA - linha.size) {
                    Spacer(Modifier.weight(1f))
                }
            }
        }
        MensagemErro(erro, comEspacoSuperior = false)
        if (previa != null) {
            CaixaPrevia(previa)
        }
    }
}

@Composable
private fun CaixaPrevia(previa: PreviaTeto) {
    val cores = previa.status.cores
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(cores.fundoSelo, FormatoPrevia)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.TrendingUp,
                contentDescription = null,
                tint = cores.texto,
                modifier = Modifier.size(18.dp),
            )
            Text(
                text = stringResource(
                    R.string.compra_previa_teto,
                    Dinheiro.formatar(previa.gastoCentavos),
                    Dinheiro.formatar(previa.tetoCentavos),
                    percentualInteiro(previa.percentual),
                ),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = cores.texto,
            )
        }
        BarraProgresso(percentual = previa.percentual, status = previa.status)
    }
}

@Composable
private fun SecaoFormaPagamento(
    selecionada: FormaPagamento?,
    erro: String?,
    aoSelecionar: (FormaPagamento) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        TituloSecao(stringResource(R.string.compra_forma_pagamento))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            FormaPagamento.entries.forEach { forma ->
                ChipOpcao(
                    texto = forma.rotulo,
                    selecionado = forma == selecionada,
                    aoClicar = { aoSelecionar(forma) },
                )
            }
        }
        MensagemErro(erro, comEspacoSuperior = false)
    }
}

@Composable
private fun TituloSecao(texto: String, modifier: Modifier = Modifier) {
    Text(
        text = texto,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = Ameixa,
        modifier = modifier,
    )
}

@Composable
private fun MensagemErro(erro: String?, comEspacoSuperior: Boolean = true) {
    if (erro != null) {
        Text(
            text = erro,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(top = if (comEspacoSuperior) 6.dp else 0.dp, start = 4.dp),
        )
    }
}

@Composable
private fun Rodape(
    textoBotao: String,
    salvando: Boolean,
    aoSalvar: () -> Unit,
) {
    Surface(
        color = Branco,
        shadowElevation = 12.dp,
        modifier = Modifier.fillMaxWidth(),
    ) {
        BotaoPrimario(
            texto = textoBotao,
            aoClicar = aoSalvar,
            carregando = salvando,
            modifier = Modifier
                .navigationBarsPadding()
                .padding(horizontal = PaddingHorizontal, vertical = 14.dp),
        )
    }
}

@Composable
private fun NovaCompraScreenExemplo(uiState: NovaCompraUiState) {
    ProsperaTheme {
        NovaCompraScreen(
            uiState = uiState,
            aoAlterarValor = {},
            aoSelecionarCategoria = {},
            aoSelecionarFormaPagamento = {},
            aoAlterarEstabelecimento = {},
            aoAlterarData = {},
            aoSalvar = {},
            aoExcluir = {},
            aoVoltar = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun NovaCompraScreenVaziaPreview() {
    NovaCompraScreenExemplo(NovaCompraUiState())
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun NovaCompraScreenPreenchidaPreview() {
    NovaCompraScreenExemplo(
        NovaCompraUiState(
            valorCentavos = 8_590,
            categoria = Categoria.SUPERMERCADO,
            formaPagamento = FormaPagamento.PIX,
            estabelecimento = "Sacolão do Bairro",
            data = LocalDate.now(),
            previa = PreviaTeto(
                gastoCentavos = 111_870,
                tetoCentavos = 120_000,
                percentual = 111_870.0 / 120_000,
                status = StatusConsumo.ATENCAO,
            ),
        )
    )
}
