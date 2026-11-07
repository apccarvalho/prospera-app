package com.apccarvalho.prospera.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apccarvalho.prospera.data.repository.AuthRepository
import com.apccarvalho.prospera.data.repository.OrcamentoRepository
import com.apccarvalho.prospera.domain.util.Validadores
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val senha: String = "",
    val senhaVisivel: Boolean = false,
    val erroEmail: String? = null,
    val erroSenha: String? = null,
    val carregando: Boolean = false,
    val mensagemErroGeral: String? = null,
    val mensagemInformativa: String? = null,
    val destino: DestinoAposAuth? = null,
)

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val orcamentoRepository: OrcamentoRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private var validarAoDigitar = false

    fun alterarEmail(email: String) {
        _uiState.update {
            it.copy(
                email = email,
                erroEmail = if (validarAoDigitar) Validadores.email(email) else null,
                mensagemErroGeral = null,
            )
        }
    }

    fun alterarSenha(senha: String) {
        _uiState.update {
            it.copy(
                senha = senha,
                erroSenha = if (validarAoDigitar) Validadores.senha(senha) else null,
                mensagemErroGeral = null,
            )
        }
    }

    fun alternarSenhaVisivel() {
        _uiState.update { it.copy(senhaVisivel = !it.senhaVisivel) }
    }

    fun entrar() {
        val estado = _uiState.value
        if (estado.carregando) return

        validarAoDigitar = true
        val erroEmail = Validadores.email(estado.email)
        val erroSenha = Validadores.senha(estado.senha)
        _uiState.update {
            it.copy(erroEmail = erroEmail, erroSenha = erroSenha, mensagemErroGeral = null)
        }
        if (erroEmail != null || erroSenha != null) return

        _uiState.update { it.copy(carregando = true) }
        viewModelScope.launch {
            authRepository.entrar(estado.email.trim(), estado.senha)
                .onSuccess {
                    val destino = destinoAposEntrar()
                    _uiState.update { it.copy(carregando = false, destino = destino) }
                }
                .onFailure { erro ->
                    _uiState.update { it.copy(carregando = false, mensagemErroGeral = erro.message) }
                }
        }
    }

    fun esqueciSenha() {
        val estado = _uiState.value
        if (estado.carregando) return

        if (Validadores.email(estado.email) != null) {
            _uiState.update {
                it.copy(erroEmail = MENSAGEM_EMAIL_REDEFINICAO, mensagemErroGeral = null)
            }
            return
        }

        _uiState.update { it.copy(erroEmail = null, carregando = true, mensagemErroGeral = null) }
        viewModelScope.launch {
            authRepository.enviarRedefinicaoSenha(estado.email.trim())
                .onSuccess {
                    _uiState.update {
                        it.copy(carregando = false, mensagemInformativa = MENSAGEM_LINK_ENVIADO)
                    }
                }
                .onFailure { erro ->
                    _uiState.update { it.copy(carregando = false, mensagemErroGeral = erro.message) }
                }
        }
    }

    fun mensagemInformativaExibida() {
        _uiState.update { it.copy(mensagemInformativa = null) }
    }

    fun destinoConsumido() {
        _uiState.update { it.copy(destino = null) }
    }

    private suspend fun destinoAposEntrar(): DestinoAposAuth {
        val userId = authRepository.usuarioAtualId() ?: return DestinoAposAuth.TETOS_PRIMEIRO_ACESSO
        val configuracao = orcamentoRepository.observarConfiguracao(userId).first()
        return if (configuracao?.onboardingConcluido == true) {
            DestinoAposAuth.DASHBOARD
        } else {
            DestinoAposAuth.TETOS_PRIMEIRO_ACESSO
        }
    }

    companion object {
        const val MENSAGEM_EMAIL_REDEFINICAO = "Informe seu e-mail para redefinir a senha"
        const val MENSAGEM_LINK_ENVIADO = "Enviamos um link de redefinição para seu e-mail"
    }
}
