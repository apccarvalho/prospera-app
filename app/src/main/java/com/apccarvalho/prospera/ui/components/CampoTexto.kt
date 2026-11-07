package com.apccarvalho.prospera.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apccarvalho.prospera.R
import com.apccarvalho.prospera.ui.theme.Branco
import com.apccarvalho.prospera.ui.theme.Contorno
import com.apccarvalho.prospera.ui.theme.Placeholder
import com.apccarvalho.prospera.ui.theme.ProsperaTheme
import com.apccarvalho.prospera.ui.theme.TextoSecundario

private val FormatoCampo = RoundedCornerShape(12.dp)

@Composable
fun CampoTexto(
    valor: String,
    aoAlterar: (String) -> Unit,
    rotulo: String,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    erro: String? = null,
    textoApoio: String? = null,
    habilitado: Boolean = true,
    tipoTeclado: KeyboardType = KeyboardType.Text,
    capitalizacao: KeyboardCapitalization = KeyboardCapitalization.None,
    acaoTeclado: ImeAction = ImeAction.Next,
    aoConfirmar: () -> Unit = {},
    modoSenha: Boolean = false,
    senhaVisivel: Boolean = false,
    aoAlternarSenhaVisivel: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val focado by interactionSource.collectIsFocusedAsState()
    val cores = MaterialTheme.colorScheme

    val corBorda = when {
        erro != null -> cores.error
        focado -> cores.primary
        else -> Contorno
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = rotulo,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = TextoSecundario,
        )
        BasicTextField(
            value = valor,
            onValueChange = aoAlterar,
            enabled = habilitado,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = cores.onSurface),
            cursorBrush = SolidColor(cores.primary),
            keyboardOptions = KeyboardOptions(
                keyboardType = tipoTeclado,
                imeAction = acaoTeclado,
                capitalization = capitalizacao,
                autoCorrectEnabled = !modoSenha && tipoTeclado == KeyboardType.Text,
            ),
            // Down em vez de Next: Next pararia no ícone de mostrar senha ao lado do campo.
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) },
                onDone = {
                    focusManager.clearFocus()
                    aoConfirmar()
                },
            ),
            visualTransformation = if (modoSenha && !senhaVisivel) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            interactionSource = interactionSource,
            modifier = Modifier
                .padding(top = 6.dp)
                .fillMaxWidth()
                .alpha(if (habilitado) 1f else 0.6f),
            decorationBox = { campo ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .heightIn(min = 52.dp)
                        .background(Branco, FormatoCampo)
                        .border(1.dp, corBorda, FormatoCampo)
                        .padding(start = 16.dp, end = if (modoSenha) 4.dp else 16.dp),
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        if (valor.isEmpty() && placeholder != null) {
                            Text(
                                text = placeholder,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Placeholder,
                            )
                        }
                        campo()
                    }
                    if (modoSenha) {
                        IconButton(onClick = aoAlternarSenhaVisivel, enabled = habilitado) {
                            Icon(
                                imageVector = if (senhaVisivel) {
                                    Icons.Rounded.VisibilityOff
                                } else {
                                    Icons.Rounded.Visibility
                                },
                                contentDescription = stringResource(
                                    if (senhaVisivel) R.string.ocultar_senha else R.string.mostrar_senha
                                ),
                                tint = cores.onSurfaceVariant,
                            )
                        }
                    }
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
private fun CampoTextoPreview() {
    ProsperaTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            CampoTexto(
                valor = "",
                aoAlterar = {},
                rotulo = "E-mail",
                placeholder = "seu@email.com",
            )
            CampoTexto(
                valor = "maria@",
                aoAlterar = {},
                rotulo = "E-mail",
                erro = "Informe um e-mail válido",
            )
            CampoTexto(
                valor = "segredo",
                aoAlterar = {},
                rotulo = "Senha",
                textoApoio = "Mínimo de 6 caracteres",
                modoSenha = true,
            )
        }
    }
}
