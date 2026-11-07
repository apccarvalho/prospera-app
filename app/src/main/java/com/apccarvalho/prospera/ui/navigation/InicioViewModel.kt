package com.apccarvalho.prospera.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apccarvalho.prospera.data.repository.AuthRepository
import com.apccarvalho.prospera.data.repository.OrcamentoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class InicioUiState(
    val rotaInicial: String? = null,
)

class InicioViewModel(
    private val authRepository: AuthRepository,
    private val orcamentoRepository: OrcamentoRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(InicioUiState())
    val uiState: StateFlow<InicioUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.value = InicioUiState(rotaInicial = calcularRotaInicial())
        }
    }

    private suspend fun calcularRotaInicial(): String {
        val userId = authRepository.usuarioAtualId() ?: return Rotas.LOGIN
        val configuracao = orcamentoRepository.observarConfiguracao(userId).first()
        return if (configuracao?.onboardingConcluido == true) {
            Rotas.DASHBOARD
        } else {
            Rotas.tetos(primeiroAcesso = true)
        }
    }
}
