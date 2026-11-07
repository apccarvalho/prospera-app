package com.apccarvalho.prospera.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apccarvalho.prospera.data.repository.AuthRepository
import com.apccarvalho.prospera.domain.util.Validadores
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CadastroUiState(
    val nome: String = "",
    val email: String = "",
    val senha: String = "",
    val confirmacaoSenha: String = "",
    val senhaVisivel: Boolean = false,
    val erroNome: String? = null,
    val erroEmail: String? = null,
    val erroSenha: String? = null,
    val erroConfirmacaoSenha: String? = null,
    val carregando: Boolean = false,
    val mensagemErroGeral: String? = null,
    val cadastroConcluido: Boolean = false,
)

class CadastroViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CadastroUiState())
    val uiState: StateFlow<CadastroUiState> = _uiState.asStateFlow()

    private var validarAoDigitar = false

    fun alterarNome(nome: String) {
        _uiState.update {
            it.copy(
                nome = nome,
                erroNome = if (validarAoDigitar) Validadores.nome(nome) else null,
                mensagemErroGeral = null,
            )
        }
    }

    fun alterarEmail(email: String) {
        _uiState.update {
            it.copy(
                email = email,
                erroEmail = if (validarAoDigitar) Validadores.email(email) else null,
                mensagemErroGeral = null,
            )
        }
    }

    // A confirmação depende da senha, então também é reavaliada quando a senha muda.
    fun alterarSenha(senha: String) {
        _uiState.update {
            it.copy(
                senha = senha,
                erroSenha = if (validarAoDigitar) Validadores.senha(senha) else null,
                erroConfirmacaoSenha = if (validarAoDigitar) {
                    Validadores.confirmacaoSenha(senha, it.confirmacaoSenha)
                } else {
                    null
                },
                mensagemErroGeral = null,
            )
        }
    }

    fun alterarConfirmacaoSenha(confirmacao: String) {
        _uiState.update {
            it.copy(
                confirmacaoSenha = confirmacao,
                erroConfirmacaoSenha = if (validarAoDigitar) {
                    Validadores.confirmacaoSenha(it.senha, confirmacao)
                } else {
                    null
                },
                mensagemErroGeral = null,
            )
        }
    }

    fun alternarSenhaVisivel() {
        _uiState.update { it.copy(senhaVisivel = !it.senhaVisivel) }
    }

    fun cadastrar() {
        val estado = _uiState.value
        if (estado.carregando) return

        validarAoDigitar = true
        val erroNome = Validadores.nome(estado.nome)
        val erroEmail = Validadores.email(estado.email)
        val erroSenha = Validadores.senha(estado.senha)
        val erroConfirmacao = Validadores.confirmacaoSenha(estado.senha, estado.confirmacaoSenha)
        _uiState.update {
            it.copy(
                erroNome = erroNome,
                erroEmail = erroEmail,
                erroSenha = erroSenha,
                erroConfirmacaoSenha = erroConfirmacao,
                mensagemErroGeral = null,
            )
        }
        if (listOf(erroNome, erroEmail, erroSenha, erroConfirmacao).any { it != null }) return

        _uiState.update { it.copy(carregando = true) }
        viewModelScope.launch {
            authRepository.cadastrar(estado.nome.trim(), estado.email.trim(), estado.senha)
                .onSuccess {
                    _uiState.update { it.copy(carregando = false, cadastroConcluido = true) }
                }
                .onFailure { erro ->
                    _uiState.update { it.copy(carregando = false, mensagemErroGeral = erro.message) }
                }
        }
    }
}
