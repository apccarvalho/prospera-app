package com.apccarvalho.prospera.ui.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apccarvalho.prospera.R
import com.apccarvalho.prospera.ui.components.BotaoPrimario
import com.apccarvalho.prospera.ui.components.CabecalhoLilas
import com.apccarvalho.prospera.ui.components.CampoTexto
import com.apccarvalho.prospera.ui.components.LogoHorizontal
import com.apccarvalho.prospera.ui.theme.FundoLavanda
import com.apccarvalho.prospera.ui.theme.ProsperaTheme
import com.apccarvalho.prospera.ui.theme.TextoSecundario

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    aoAlterarEmail: (String) -> Unit,
    aoAlterarSenha: (String) -> Unit,
    aoAlternarSenhaVisivel: () -> Unit,
    aoEntrar: () -> Unit,
    aoEsqueciSenha: () -> Unit,
    aoIrParaCadastro: () -> Unit,
    aoMensagemInformativaExibida: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(uiState.mensagemInformativa) {
        uiState.mensagemInformativa?.let { mensagem ->
            snackbarHostState.showSnackbar(mensagem)
            aoMensagemInformativaExibida()
        }
    }

    val habilitado = !uiState.carregando

    Scaffold(
        containerColor = FundoLavanda,
        contentWindowInsets = WindowInsets(0),
        snackbarHost = { SnackbarHost(snackbarHostState, Modifier.navigationBarsPadding()) },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            CabecalhoLilas(altura = 330.dp)
            ColunaAuth {
                Box(
                    modifier = Modifier
                        .statusBarsPadding()
                        .fillMaxWidth()
                        .height(220.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        LogoHorizontal()
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = stringResource(R.string.slogan),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextoSecundario,
                            textAlign = TextAlign.Center,
                        )
                    }
                }

                CartaoAuth {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = stringResource(R.string.login_titulo),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = stringResource(R.string.login_subtitulo),
                            fontSize = 14.sp,
                            color = TextoSecundario,
                        )
                    }
                    CampoTexto(
                        valor = uiState.email,
                        aoAlterar = aoAlterarEmail,
                        rotulo = stringResource(R.string.rotulo_email),
                        placeholder = stringResource(R.string.placeholder_email),
                        erro = uiState.erroEmail,
                        habilitado = habilitado,
                        tipoTeclado = KeyboardType.Email,
                        acaoTeclado = ImeAction.Next,
                    )
                    Column {
                        CampoTexto(
                            valor = uiState.senha,
                            aoAlterar = aoAlterarSenha,
                            rotulo = stringResource(R.string.rotulo_senha),
                            placeholder = stringResource(R.string.login_placeholder_senha),
                            erro = uiState.erroSenha,
                            habilitado = habilitado,
                            tipoTeclado = KeyboardType.Password,
                            acaoTeclado = ImeAction.Done,
                            aoConfirmar = aoEntrar,
                            modoSenha = true,
                            senhaVisivel = uiState.senhaVisivel,
                            aoAlternarSenhaVisivel = aoAlternarSenhaVisivel,
                        )
                        Text(
                            text = stringResource(R.string.login_esqueci_senha),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .align(Alignment.End)
                                .clickable(enabled = habilitado, onClick = aoEsqueciSenha)
                                .padding(vertical = 8.dp, horizontal = 4.dp),
                        )
                    }
                    MensagemErroGeral(uiState.mensagemErroGeral)
                    BotaoPrimario(
                        texto = stringResource(R.string.login_botao),
                        aoClicar = aoEntrar,
                        carregando = uiState.carregando,
                    )
                }

                Spacer(Modifier.weight(1f))
                LinkRodape(
                    pergunta = stringResource(R.string.login_sem_conta),
                    acao = stringResource(R.string.login_cadastre_se),
                    aoClicar = aoIrParaCadastro,
                    habilitado = habilitado,
                    modifier = Modifier.navigationBarsPadding(),
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginScreenPreview() {
    ProsperaTheme {
        LoginScreen(
            uiState = LoginUiState(email = "maria@email.com"),
            aoAlterarEmail = {},
            aoAlterarSenha = {},
            aoAlternarSenhaVisivel = {},
            aoEntrar = {},
            aoEsqueciSenha = {},
            aoIrParaCadastro = {},
            aoMensagemInformativaExibida = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoginScreenComErrosPreview() {
    ProsperaTheme {
        LoginScreen(
            uiState = LoginUiState(
                email = "maria@",
                erroEmail = "Informe um e-mail válido",
                erroSenha = "A senha deve ter pelo menos 6 caracteres",
                mensagemErroGeral = "E-mail ou senha incorretos",
            ),
            aoAlterarEmail = {},
            aoAlterarSenha = {},
            aoAlternarSenhaVisivel = {},
            aoEntrar = {},
            aoEsqueciSenha = {},
            aoIrParaCadastro = {},
            aoMensagemInformativaExibida = {},
        )
    }
}
