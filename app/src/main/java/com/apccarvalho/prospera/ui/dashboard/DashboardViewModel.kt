package com.apccarvalho.prospera.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apccarvalho.prospera.data.repository.AuthRepository
import com.apccarvalho.prospera.data.repository.CompraRepository
import com.apccarvalho.prospera.data.repository.OrcamentoRepository
import com.apccarvalho.prospera.domain.model.ResumoCategoria
import com.apccarvalho.prospera.domain.model.StatusConsumo
import com.apccarvalho.prospera.domain.util.nomeFormatado
import com.apccarvalho.prospera.domain.util.percentualDe
import com.apccarvalho.prospera.domain.util.resumirCategorias
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.YearMonth

data class DashboardUiState(
    val carregando: Boolean = true,
    val primeiroNome: String? = null,
    val nomeMes: String = "",
    val totalGastoCentavos: Long = 0,
    val tetoTotalCentavos: Long = 0,
    val percentualGeral: Double = 0.0,
    val statusGeral: StatusConsumo = StatusConsumo.OK,
    val saldoCentavos: Long = 0,
    val quantidadeCompras: Int = 0,
    val categorias: List<ResumoCategoria> = emptyList(),
    val saiu: Boolean = false,
)

class DashboardViewModel(
    compraRepository: CompraRepository,
    orcamentoRepository: OrcamentoRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val mes = YearMonth.now()
    private val primeiroNome = authRepository.nomeUsuario()
        ?.trim()
        ?.split(Regex("\\s+"))
        ?.firstOrNull()
        ?.takeIf { it.isNotEmpty() }

    private val estadoInicial = DashboardUiState(
        primeiroNome = primeiroNome,
        nomeMes = mes.nomeFormatado(),
    )

    private val saiu = MutableStateFlow(false)

    val uiState: StateFlow<DashboardUiState> = run {
        val userId = authRepository.usuarioAtualId()
        if (userId == null) {
            flowOf(estadoInicial.copy(carregando = false, saiu = true))
        } else {
            combine(
                orcamentoRepository.observarTetos(userId),
                compraRepository.observarTotaisDoMes(userId, mes),
                compraRepository.observarComprasDoMes(userId, mes),
                saiu,
            ) { tetos, totais, compras, saiu ->
                val totalGasto = totais.values.sum()
                val tetoTotal = tetos.values.sum()
                val percentualGeral = percentualDe(totalGasto, tetoTotal)
                estadoInicial.copy(
                    carregando = false,
                    totalGastoCentavos = totalGasto,
                    tetoTotalCentavos = tetoTotal,
                    percentualGeral = percentualGeral,
                    statusGeral = StatusConsumo.doPercentual(percentualGeral),
                    saldoCentavos = tetoTotal - totalGasto,
                    quantidadeCompras = compras.size,
                    categorias = resumirCategorias(tetos, totais),
                    saiu = saiu,
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = estadoInicial,
    )

    fun sair() {
        authRepository.sair()
        saiu.update { true }
    }
}
