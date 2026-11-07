package com.apccarvalho.prospera.ui.tetos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apccarvalho.prospera.data.repository.AuthRepository
import com.apccarvalho.prospera.data.repository.OrcamentoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TetosUiState(
    val salvando: Boolean = false,
    val concluido: Boolean = false,
)

class TetosViewModel(
    private val authRepository: AuthRepository,
    private val orcamentoRepository: OrcamentoRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TetosUiState())
    val uiState: StateFlow<TetosUiState> = _uiState.asStateFlow()

    fun concluir() {
        if (_uiState.value.salvando) return
        val userId = authRepository.usuarioAtualId() ?: return

        _uiState.update { it.copy(salvando = true) }
        viewModelScope.launch {
            orcamentoRepository.salvarRendaEConcluirOnboarding(userId, rendaCentavos = null)
            _uiState.update { it.copy(salvando = false, concluido = true) }
        }
    }
}
