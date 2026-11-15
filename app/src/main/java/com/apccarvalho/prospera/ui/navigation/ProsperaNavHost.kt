package com.apccarvalho.prospera.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.apccarvalho.prospera.ui.ProsperaViewModelFactory
import com.apccarvalho.prospera.ui.auth.CadastroScreen
import com.apccarvalho.prospera.ui.auth.CadastroViewModel
import com.apccarvalho.prospera.ui.auth.DestinoAposAuth
import com.apccarvalho.prospera.ui.auth.LoginScreen
import com.apccarvalho.prospera.ui.auth.LoginViewModel
import com.apccarvalho.prospera.ui.components.AbaPrincipal
import com.apccarvalho.prospera.ui.components.BotaoNovaCompra
import com.apccarvalho.prospera.ui.components.BottomNav
import com.apccarvalho.prospera.ui.compra.NovaCompraScreen
import com.apccarvalho.prospera.ui.compra.NovaCompraViewModel
import com.apccarvalho.prospera.ui.dashboard.DashboardScreen
import com.apccarvalho.prospera.ui.dashboard.DashboardViewModel
import com.apccarvalho.prospera.ui.historico.HistoricoScreen
import com.apccarvalho.prospera.ui.tetos.TetosScreen
import com.apccarvalho.prospera.ui.tetos.TetosViewModel
import com.apccarvalho.prospera.ui.theme.FundoLavanda

@Composable
fun ProsperaNavHost(
    navController: NavHostController = rememberNavController(),
) {
    val entradaAtual by navController.currentBackStackEntryAsState()
    val abaAtiva = entradaAtual?.abaPrincipal()

    // O Scaffold externo é o único que conhece a BottomNav e o FAB. O padding dele é consumido
    // como insets para que as telas não somem de novo a barra de navegação do sistema.
    Scaffold(
        containerColor = FundoLavanda,
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            if (abaAtiva != null) {
                BottomNav(
                    abaAtiva = abaAtiva,
                    aoSelecionar = navController::navegarParaAba,
                )
            }
        },
        floatingActionButton = {
            if (abaAtiva == AbaPrincipal.INICIO || abaAtiva == AbaPrincipal.HISTORICO) {
                BotaoNovaCompra(
                    aoClicar = {
                        navController.navigate(Rotas.novaCompra()) { launchSingleTop = true }
                    },
                )
            }
        },
    ) { innerPadding ->
        NavHostProspera(
            navController = navController,
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding),
        )
    }
}

@Composable
private fun NavHostProspera(
    navController: NavHostController,
    modifier: Modifier,
) {
    // A rota "inicio" só mostra o fundo do app enquanto o estado de login é verificado;
    // assim o Login não aparece por um instante para quem já está logado.
    NavHost(navController = navController, startDestination = Rotas.INICIO, modifier = modifier) {
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
        ) {
            val viewModel: TetosViewModel = viewModel(factory = ProsperaViewModelFactory)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(uiState.concluido) {
                if (uiState.concluido) {
                    navController.navigate(Rotas.DASHBOARD) {
                        popUpTo(Rotas.TETOS) { inclusive = true }
                    }
                }
            }
            TetosScreen(
                uiState = uiState,
                aoAlterarRenda = viewModel::alterarRenda,
                aoAlterarTeto = viewModel::alterarTeto,
                aoSalvar = viewModel::salvar,
                aoMensagemInformativaExibida = viewModel::mensagemInformativaExibida,
            )
        }

        composable(Rotas.DASHBOARD) { entrada ->
            val viewModel: DashboardViewModel = viewModel(factory = ProsperaViewModelFactory)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val mensagem by entrada.mensagemDeCompra()

            LaunchedEffect(uiState.saiu) {
                if (uiState.saiu) {
                    navController.navigate(Rotas.LOGIN) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            }
            DashboardScreen(
                uiState = uiState,
                aoSair = viewModel::sair,
                mensagem = mensagem,
                aoMensagemExibida = entrada::consumirMensagemDeCompra,
            )
        }

        composable(Rotas.HISTORICO) {
            HistoricoScreen()
        }

        composable(
            route = Rotas.NOVA_COMPRA,
            arguments = listOf(
                navArgument(Rotas.ARG_COMPRA_ID) {
                    type = NavType.LongType
                    defaultValue = Rotas.COMPRA_NOVA
                },
            ),
        ) { entrada ->
            val viewModel: NovaCompraViewModel = viewModel(factory = ProsperaViewModelFactory)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            LaunchedEffect(uiState.mensagemConclusao) {
                uiState.mensagemConclusao?.let { mensagem ->
                    navController.voltarComMensagem(entrada, mensagem)
                }
            }
            NovaCompraScreen(
                uiState = uiState,
                aoAlterarValor = viewModel::alterarValor,
                aoSelecionarCategoria = viewModel::selecionarCategoria,
                aoSelecionarFormaPagamento = viewModel::selecionarFormaPagamento,
                aoAlterarEstabelecimento = viewModel::alterarEstabelecimento,
                aoAlterarData = viewModel::alterarData,
                aoSalvar = viewModel::salvar,
                aoVoltar = dropUnlessResumed { navController.popBackStack() },
            )
        }
    }
}

@Composable
private fun NavBackStackEntry.mensagemDeCompra(): State<String?> =
    savedStateHandle.getStateFlow<String?>(Rotas.CHAVE_MENSAGEM_COMPRA, null)
        .collectAsStateWithLifecycle()

private fun NavBackStackEntry.consumirMensagemDeCompra() {
    savedStateHandle[Rotas.CHAVE_MENSAGEM_COMPRA] = null
}

private fun NavHostController.voltarComMensagem(entrada: NavBackStackEntry, mensagem: String) {
    if (currentBackStackEntry != entrada) return
    previousBackStackEntry?.savedStateHandle?.set(Rotas.CHAVE_MENSAGEM_COMPRA, mensagem)
    popBackStack()
}

private fun NavBackStackEntry.abaPrincipal(): AbaPrincipal? = when (destination.route) {
    Rotas.DASHBOARD -> AbaPrincipal.INICIO
    Rotas.HISTORICO -> AbaPrincipal.HISTORICO
    Rotas.TETOS -> {
        val primeiroAcesso = arguments?.getBoolean(Rotas.ARG_PRIMEIRO_ACESSO) ?: false
        if (primeiroAcesso) null else AbaPrincipal.TETOS
    }

    else -> null
}

private fun NavHostController.navegarParaAba(aba: AbaPrincipal) {
    val rota = when (aba) {
        AbaPrincipal.INICIO -> Rotas.DASHBOARD
        AbaPrincipal.HISTORICO -> Rotas.HISTORICO
        AbaPrincipal.TETOS -> Rotas.tetos(primeiroAcesso = false)
    }
    navigate(rota) {
        popUpTo(Rotas.DASHBOARD) { saveState = true }
        launchSingleTop = true
        restoreState = true
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
