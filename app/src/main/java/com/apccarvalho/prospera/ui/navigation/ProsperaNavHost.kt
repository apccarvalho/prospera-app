package com.apccarvalho.prospera.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.apccarvalho.prospera.ui.ProsperaViewModelFactory
import com.apccarvalho.prospera.ui.auth.CadastroScreen
import com.apccarvalho.prospera.ui.auth.CadastroViewModel
import com.apccarvalho.prospera.ui.auth.DestinoAposAuth
import com.apccarvalho.prospera.ui.auth.LoginScreen
import com.apccarvalho.prospera.ui.auth.LoginViewModel
import com.apccarvalho.prospera.ui.dashboard.DashboardScreen
import com.apccarvalho.prospera.ui.dashboard.DashboardViewModel
import com.apccarvalho.prospera.ui.tetos.TetosScreen
import com.apccarvalho.prospera.ui.tetos.TetosViewModel
import com.apccarvalho.prospera.ui.theme.FundoLavanda

@Composable
fun ProsperaNavHost(
    navController: NavHostController = rememberNavController(),
) {
    // A rota "inicio" só mostra o fundo do app enquanto o estado de login é verificado;
    // assim o Login não aparece por um instante para quem já está logado.
    NavHost(navController = navController, startDestination = Rotas.INICIO) {
        composable(Rotas.INICIO) {
            val viewModel: InicioViewModel = viewModel(factory = ProsperaViewModelFactory)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(uiState.rotaInicial) {
                uiState.rotaInicial?.let { rota ->
                    navController.navigate(rota) {
                        popUpTo(Rotas.INICIO) { inclusive = true }
                    }
                }
            }
            Box(
                Modifier
                    .fillMaxSize()
                    .background(FundoLavanda)
            )
        }

        composable(Rotas.LOGIN) {
            val viewModel: LoginViewModel = viewModel(factory = ProsperaViewModelFactory)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(uiState.destino) {
                uiState.destino?.let { destino ->
                    navController.navegarAposAutenticacao(destino)
                    viewModel.destinoConsumido()
                }
            }
            LoginScreen(
                uiState = uiState,
                aoAlterarEmail = viewModel::alterarEmail,
                aoAlterarSenha = viewModel::alterarSenha,
                aoAlternarSenhaVisivel = viewModel::alternarSenhaVisivel,
                aoEntrar = viewModel::entrar,
                aoEsqueciSenha = viewModel::esqueciSenha,
                aoIrParaCadastro = dropUnlessResumed { navController.navigate(Rotas.CADASTRO) },
                aoMensagemInformativaExibida = viewModel::mensagemInformativaExibida,
            )
        }

        composable(Rotas.CADASTRO) {
            val viewModel: CadastroViewModel = viewModel(factory = ProsperaViewModelFactory)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(uiState.cadastroConcluido) {
                if (uiState.cadastroConcluido) {
                    navController.navegarAposAutenticacao(DestinoAposAuth.TETOS_PRIMEIRO_ACESSO)
                }
            }
            CadastroScreen(
                uiState = uiState,
                aoAlterarNome = viewModel::alterarNome,
                aoAlterarEmail = viewModel::alterarEmail,
                aoAlterarSenha = viewModel::alterarSenha,
                aoAlterarConfirmacaoSenha = viewModel::alterarConfirmacaoSenha,
                aoAlternarSenhaVisivel = viewModel::alternarSenhaVisivel,
                aoCadastrar = viewModel::cadastrar,
                aoVoltar = dropUnlessResumed { navController.popBackStack() },
            )
        }

        composable(
            route = Rotas.TETOS,
            arguments = listOf(
                navArgument(Rotas.ARG_PRIMEIRO_ACESSO) {
                    type = NavType.BoolType
                    defaultValue = false
                },
            ),
        ) { entrada ->
            val primeiroAcesso = entrada.arguments?.getBoolean(Rotas.ARG_PRIMEIRO_ACESSO) ?: false
            val viewModel: TetosViewModel = viewModel(factory = ProsperaViewModelFactory)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(uiState.concluido) {
                if (!uiState.concluido) return@LaunchedEffect
                if (primeiroAcesso) {
                    navController.navigate(Rotas.DASHBOARD) {
                        popUpTo(Rotas.TETOS) { inclusive = true }
                    }
                } else {
                    navController.popBackStack()
                }
            }
            TetosScreen(
                uiState = uiState,
                aoConcluir = viewModel::concluir,
            )
        }

        composable(Rotas.DASHBOARD) {
            val viewModel: DashboardViewModel = viewModel(factory = ProsperaViewModelFactory)

            DashboardScreen(
                aoSair = dropUnlessResumed {
                    viewModel.sair()
                    navController.navigate(Rotas.LOGIN) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                },
            )
        }
    }
}

private fun NavHostController.navegarAposAutenticacao(destino: DestinoAposAuth) {
    val rota = when (destino) {
        DestinoAposAuth.DASHBOARD -> Rotas.DASHBOARD
        DestinoAposAuth.TETOS_PRIMEIRO_ACESSO -> Rotas.tetos(primeiroAcesso = true)
    }
    navigate(rota) {
        popUpTo(Rotas.LOGIN) { inclusive = true }
    }
}
