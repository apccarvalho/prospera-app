package com.apccarvalho.prospera.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apccarvalho.prospera.R
import com.apccarvalho.prospera.ui.components.BotaoPrimario
import com.apccarvalho.prospera.ui.components.CabecalhoLilas
import com.apccarvalho.prospera.ui.components.CampoTexto
import com.apccarvalho.prospera.ui.theme.Ameixa
import com.apccarvalho.prospera.ui.theme.Branco
import com.apccarvalho.prospera.ui.theme.FundoLavanda
import com.apccarvalho.prospera.ui.theme.ProsperaTheme
import com.apccarvalho.prospera.ui.theme.TextoSecundario

@Composable
fun CadastroScreen(
    uiState: CadastroUiState,
    aoAlterarNome: (String) -> Unit,
    aoAlterarEmail: (String) -> Unit,
    aoAlterarSenha: (String) -> Unit,
    aoAlterarConfirmacaoSenha: (String) -> Unit,
    aoAlternarSenhaVisivel: () -> Unit,
    aoCadastrar: () -> Unit,
    aoVoltar: () -> Unit,
) {
    val habilitado = !uiState.carregando

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FundoLavanda),
    ) {
        CabecalhoLilas(altura = 220.dp)
        ColunaAuth {
            Row(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(top = 12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    onClick = aoVoltar,
                    enabled = habilitado,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Branco,
                        contentColor = Ameixa,
                        disabledContainerColor = Branco,
                    ),
                    modifier = Modifier.size(40.dp),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = stringResource(R.string.voltar),
                    )
                }
                Spacer(Modifier.width(12.dp))
                Text(
                    text = stringResource(R.string.cadastro_barra_titulo),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Ameixa,
                    modifier = Modifier.weight(1f),
                )
                Image(
                    painter = painterResource(R.drawable.ic_girassol),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp),
                )
            }

            Column(
                modifier = Modifier.padding(top = 20.dp, bottom = 20.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = stringResource(R.string.cadastro_titulo),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Ameixa,
                )
                Text(
                    text = stringResource(R.string.cadastro_subtitulo),
                    fontSize = 14.sp,
                    color = TextoSecundario,
                )
            }

            CartaoAuth {
                CampoTexto(
                    valor = uiState.nome,
                    aoAlterar = aoAlterarNome,
                    rotulo = stringResource(R.string.rotulo_nome),
                    placeholder = stringResource(R.string.placeholder_nome),
                    erro = uiState.erroNome,
                    habilitado = habilitado,
                    capitalizacao = KeyboardCapitalization.Words,
                )
                CampoTexto(
                    valor = uiState.email,
                    aoAlterar = aoAlterarEmail,
                    rotulo = stringResource(R.string.rotulo_email),
                    placeholder = stringResource(R.string.placeholder_email),
                    erro = uiState.erroEmail,
                    habilitado = habilitado,
                    tipoTeclado = KeyboardType.Email,
                )
                CampoTexto(
                    valor = uiState.senha,
                    aoAlterar = aoAlterarSenha,
                    rotulo = stringResource(R.string.rotulo_senha),
                    placeholder = stringResource(R.string.cadastro_placeholder_senha),
                    erro = uiState.erroSenha,
                    textoApoio = stringResource(R.string.cadastro_apoio_senha),
                    habilitado = habilitado,
                    tipoTeclado = KeyboardType.Password,
                    modoSenha = true,
                    senhaVisivel = uiState.senhaVisivel,
                    aoAlternarSenhaVisivel = aoAlternarSenhaVisivel,
                )
                CampoTexto(
                    valor = uiState.confirmacaoSenha,
                    aoAlterar = aoAlterarConfirmacaoSenha,
                    rotulo = stringResource(R.string.rotulo_confirmar_senha),
                    placeholder = stringResource(R.string.cadastro_placeholder_confirmar_senha),
                    erro = uiState.erroConfirmacaoSenha,
                    habilitado = habilitado,
                    tipoTeclado = KeyboardType.Password,
                    acaoTeclado = ImeAction.Done,
                    aoConfirmar = aoCadastrar,
                    modoSenha = true,
                    senhaVisivel = uiState.senhaVisivel,
                    aoAlternarSenhaVisivel = aoAlternarSenhaVisivel,
                )
                MensagemErroGeral(uiState.mensagemErroGeral)
                BotaoPrimario(
                    texto = stringResource(R.string.cadastro_botao),
                    aoClicar = aoCadastrar,
                    carregando = uiState.carregando,
                )
            }

            Spacer(Modifier.weight(1f))
            LinkRodape(
                pergunta = stringResource(R.string.cadastro_ja_tem_conta),
                acao = stringResource(R.string.cadastro_entrar),
                aoClicar = aoVoltar,
                habilitado = habilitado,
                modifier = Modifier.navigationBarsPadding(),
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CadastroScreenPreview() {
    ProsperaTheme {
        CadastroScreen(
            uiState = CadastroUiState(
                nome = "Maria",
                email = "maria@email.com",
                senha = "123",
                erroSenha = "A senha deve ter pelo menos 6 caracteres",
                erroConfirmacaoSenha = "As senhas não conferem",
            ),
            aoAlterarNome = {},
            aoAlterarEmail = {},
            aoAlterarSenha = {},
            aoAlterarConfirmacaoSenha = {},
            aoAlternarSenhaVisivel = {},
            aoCadastrar = {},
            aoVoltar = {},
        )
    }
}
