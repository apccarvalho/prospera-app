package com.apccarvalho.prospera.ui.tetos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apccarvalho.prospera.data.repository.AuthRepository
import com.apccarvalho.prospera.data.repository.OrcamentoRepository
import com.apccarvalho.prospera.domain.model.Categoria
import com.apccarvalho.prospera.domain.util.Validadores
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TetosUiState(
    val primeiroAcesso: Boolean = false,
    val rendaCentavos: Long = 0,
    val erroRenda: String? = null,
    val tetos: Map<Categoria, Long> = Categoria.entries.associateWith { 0L },
    val errosTetos: Map<Categoria, String?> = emptyMap(),
    val carregando: Boolean = true,
    val salvando: Boolean = false,
    val concluido: Boolean = false,
    val mensagemInformativa: String? = null,
) {
    val tetoTotalCentavos: Long
        get() = tetos.values.sum()

    val percentualDaRenda: Double?
        get() = if (rendaCentavos > 0) tetoTotalCentavos.toDouble() / rendaCentavos else null
}

class TetosViewModel(
    private val orcamentoRepository: OrcamentoRepository,
    private val authRepository: AuthRepository,
    primeiroAcesso: Boolean,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TetosUiState(primeiroAcesso = primeiroAcesso))
    val uiState: StateFlow<TetosUiState> = _uiState.asStateFlow()

    private var validarAoDigitar = false

    init {
        carregar()
    }

    private fun carregar() {
        val userId = authRepository.usuarioAtualId()
        if (userId == null) {
            _uiState.update { it.copy(carregando = false) }
            return
        }
        viewModelScope.launch {
            val tetosSalvos = orcamentoRepository.observarTetos(userId).first()
            val configuracao = orcamentoRepository.observarConfiguracao(userId).first()
            _uiState.update { estado ->
                estado.copy(
                    rendaCentavos = configuracao?.rendaMensalCentavos ?: 0L,
                    tetos = Categoria.entries.associateWith { tetosSalvos[it] ?: 0L },
                    carregando = false,
                )
            }
        }
    }

    fun alterarRenda(centavos: Long) {
        _uiState.update {
            it.copy(
                rendaCentavos = centavos,
                erroRenda = if (validarAoDigitar) Validadores.valor(centavos) else null,
            )
        }
    }

    fun alterarTeto(categoria: Categoria, centavos: Long) {
        _uiState.update {
            it.copy(
                tetos = it.tetos + (categoria to centavos),
                errosTetos = it.errosTetos +
                    (categoria to if (validarAoDigitar) Validadores.teto(centavos) else null),
            )
        }
    }

    fun salvar() {
        val estado = _uiState.value
        if (estado.carregando || estado.salvando) return
        val userId = authRepository.usuarioAtualId() ?: return

        validarAoDigitar = true
        val erroRenda = Validadores.valor(estado.rendaCentavos)
        val errosTetos = estado.tetos.mapValues { (_, valor) -> Validadores.teto(valor) }
        _uiState.update { it.copy(erroRenda = erroRenda, errosTetos = errosTetos) }
        if (erroRenda != null || errosTetos.values.any { it != null }) return

        _uiState.update { it.copy(salvando = true) }
        viewModelScope.launch {
            orcamentoRepository.salvarTetos(userId, estado.tetos)
            orcamentoRepository.salvarRendaEConcluirOnboarding(userId, estado.rendaCentavos)
            _uiState.update {
                if (it.primeiroAcesso) {
                    it.copy(salvando = false, concluido = true)
                } else {
                    it.copy(salvando = false, mensagemInformativa = MENSAGEM_TETOS_ATUALIZADOS)
                }
            }
        }
    }

    fun mensagemInformativaExibida() {
        _uiState.update { it.copy(mensagemInformativa = null) }
    }

    companion object {
        const val MENSAGEM_TETOS_ATUALIZADOS = "Tetos atualizados"
    }
}
