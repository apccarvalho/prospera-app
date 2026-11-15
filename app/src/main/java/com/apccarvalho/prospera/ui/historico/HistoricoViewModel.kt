package com.apccarvalho.prospera.ui.historico

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apccarvalho.prospera.data.repository.AuthRepository
import com.apccarvalho.prospera.data.repository.CompraRepository
import com.apccarvalho.prospera.domain.model.Categoria
import com.apccarvalho.prospera.domain.model.Compra
import com.apccarvalho.prospera.domain.util.nomeFormatado
import com.apccarvalho.prospera.domain.util.rotuloDoDia
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.YearMonth

data class GrupoDia(
    val data: LocalDate,
    val rotulo: String,
    val totalCentavos: Long,
    val compras: List<Compra>,
)

data class HistoricoUiState(
    val carregando: Boolean = true,
    val nomeMes: String = "",
    val quantidadeCompras: Int = 0,
    val totalCentavos: Long = 0,
    val filtro: Categoria? = null,
    val grupos: List<GrupoDia> = emptyList(),
)

class HistoricoViewModel(
    compraRepository: CompraRepository,
    authRepository: AuthRepository,
) : ViewModel() {

    private val mes = YearMonth.now()
    private val filtro = MutableStateFlow<Categoria?>(null)
    private val estadoInicial = HistoricoUiState(nomeMes = mes.nomeFormatado())

    val uiState: StateFlow<HistoricoUiState> = run {
        val userId = authRepository.usuarioAtualId()
        val compras = if (userId == null) {
            flowOf(emptyList())
        } else {
            compraRepository.observarComprasDoMes(userId, mes)
        }
        combine(compras, filtro) { lista, categoria ->
            val visiveis = lista
                .filter { categoria == null || it.categoria == categoria }
                .sortedWith(compareByDescending<Compra> { it.data }.thenByDescending { it.criadoEm })
            estadoInicial.copy(
                carregando = false,
                quantidadeCompras = visiveis.size,
                totalCentavos = visiveis.sumOf { it.valorCentavos },
                filtro = categoria,
                grupos = agruparPorDia(visiveis, LocalDate.now()),
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = estadoInicial,
    )

    fun selecionarFiltro(categoria: Categoria?) {
        filtro.value = categoria
    }
}

internal fun agruparPorDia(compras: List<Compra>, hoje: LocalDate): List<GrupoDia> =
    compras
        .groupBy { it.data }
        .map { (data, doDia) ->
            GrupoDia(
                data = data,
                rotulo = data.rotuloDoDia(hoje),
                totalCentavos = doDia.sumOf { it.valorCentavos },
                compras = doDia,
            )
        }
