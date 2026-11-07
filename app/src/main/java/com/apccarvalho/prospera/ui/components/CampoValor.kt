package com.apccarvalho.prospera.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apccarvalho.prospera.domain.util.Dinheiro
import com.apccarvalho.prospera.ui.theme.Ameixa
import com.apccarvalho.prospera.ui.theme.Branco
import com.apccarvalho.prospera.ui.theme.Contorno
import com.apccarvalho.prospera.ui.theme.Placeholder
import com.apccarvalho.prospera.ui.theme.ProsperaTheme
import com.apccarvalho.prospera.ui.theme.TextoSecundario

private val FormatoCampo = RoundedCornerShape(12.dp)

@Composable
fun CampoValor(
    centavos: Long,
    aoAlterar: (Long) -> Unit,
    rotulo: String,
    modifier: Modifier = Modifier,
    erro: String? = null,
    textoApoio: String? = null,
    habilitado: Boolean = true,
    acaoTeclado: ImeAction = ImeAction.Next,
    aoConfirmar: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val focado by interactionSource.collectIsFocusedAsState()
    val cores = MaterialTheme.colorScheme

    val texto = Dinheiro.formatarSemSimbolo(centavos)
    // O estado local é recriado sempre que o valor muda e também é reescrito em toda edição,
    // mesmo quando o valor não muda; assim o cursor volta para o fim se a pessoa tocar no meio do texto.
    var valorCampo by remember(texto) {
        mutableStateOf(TextFieldValue(texto, TextRange(texto.length)))
    }

    val (larguraBorda, corBorda) = when {
        erro != null -> 1.dp to cores.error
        focado -> 2.dp to cores.primary
        else -> 1.dp to Contorno
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = rotulo,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = TextoSecundario,
        )
        BasicTextField(
            value = valorCampo,
            onValueChange = { novo ->
                val novoCentavos = Dinheiro.aplicarDigitacao(centavos, novo.text)
                if (novoCentavos != centavos) aoAlterar(novoCentavos)
                val novoTexto = Dinheiro.formatarSemSimbolo(novoCentavos)
                valorCampo = TextFieldValue(novoTexto, TextRange(novoTexto.length))
            },
            enabled = habilitado,
            singleLine = true,
            textStyle = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = if (centavos == 0L) Placeholder else Ameixa,
            ),
            cursorBrush = SolidColor(cores.primary),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = acaoTeclado,
                autoCorrectEnabled = false,
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) },
                onDone = {
                    focusManager.clearFocus()
                    aoConfirmar()
                },
            ),
            interactionSource = interactionSource,
            modifier = Modifier
                .padding(top = 6.dp)
                .fillMaxWidth()
                .alpha(if (habilitado) 1f else 0.6f),
            decorationBox = { campo ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .height(56.dp)
                        .background(Branco, FormatoCampo)
                        .border(larguraBorda, corBorda, FormatoCampo)
                        .padding(horizontal = 16.dp),
                ) {
                    Text(
                        text = "R$",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = cores.primary,
                    )
                    Spacer(Modifier.width(8.dp))
                    campo()
                }
            },
        )
        when {
            erro != null -> Text(
                text = erro,
                fontSize = 12.sp,
                color = cores.error,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp),
            )

            textoApoio != null -> Text(
                text = textoApoio,
                fontSize = 12.sp,
                color = TextoSecundario,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp),
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFAF8FF)
@Composable
private fun CampoValorPreview() {
    ProsperaTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            CampoValor(
                centavos = 0,
                aoAlterar = {},
                rotulo = "Renda mensal",
                textoApoio = "Usada como referência para planejar seus tetos.",
            )
            CampoValor(
                centavos = 120_000,
                aoAlterar = {},
                rotulo = "Supermercado/Feira",
            )
            CampoValor(
                centavos = 0,
                aoAlterar = {},
                rotulo = "Lazer/Diversão",
                erro = "Informe um valor maior que zero",
            )
        }
    }
}
