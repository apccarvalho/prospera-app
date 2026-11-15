package com.apccarvalho.prospera.ui.compra

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apccarvalho.prospera.data.repository.AuthRepository
import com.apccarvalho.prospera.data.repository.CompraRepository
import com.apccarvalho.prospera.data.repository.OrcamentoRepository
import com.apccarvalho.prospera.domain.model.Categoria
import com.apccarvalho.prospera.domain.model.Compra
import com.apccarvalho.prospera.domain.model.FormaPagamento
import com.apccarvalho.prospera.domain.model.StatusConsumo
import com.apccarvalho.prospera.domain.util.Validadores
import com.apccarvalho.prospera.domain.util.percentualDe
import com.apccarvalho.prospera.ui.navigation.Rotas
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

data class PreviaTeto(
    val gastoCentavos: Long,
    val tetoCentavos: Long,
    val percentual: Double,
    val status: StatusConsumo,
)

data class NovaCompraUiState(
    val valorCentavos: Long = 0,
    val categoria: Categoria? = null,
    val formaPagamento: FormaPagamento? = null,
    val estabelecimento: String = "",
    val data: LocalDate = LocalDate.now(),
    val erroValor: String? = null,
    val erroCategoria: String? = null,
    val erroFormaPagamento: String? = null,
    val erroEstabelecimento: String? = null,
    val erroData: String? = null,
    val previa: PreviaTeto? = null,
    val salvando: Boolean = false,
    val mensagemConclusao: String? = null,
)

class NovaCompraViewModel(
    private val compraRepository: CompraRepository,
    private val orcamentoRepository: OrcamentoRepository,
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val compraId: Long? = savedStateHandle.get<Long>(Rotas.ARG_COMPRA_ID)
        ?.takeIf { it != Rotas.COMPRA_NOVA }

    private val formulario = MutableStateFlow(NovaCompraUiState())

    private var validarAoAlterar = false

    val uiState: StateFlow<NovaCompraUiState> =
        combine(formulario, observarPrevia()) { estado, previa -> estado.copy(previa = previa) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = formulario.value,
            )

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observarPrevia(): Flow<PreviaTeto?> {
        val userId = authRepository.usuarioAtualId() ?: return flowOf(null)
        val totaisDoMes = formulario
            .map { YearMonth.from(it.data) }
            .distinctUntilChanged()
            .flatMapLatest { mes -> compraRepository.observarTotaisDoMes(userId, mes) }
        val categoriaEValor = formulario
            .map { it.categoria to it.valorCentavos }
            .distinctUntilChanged()

        return combine(
            categoriaEValor,
            totaisDoMes,
            orcamentoRepository.observarTetos(userId),
        ) { (categoria, valor), totais, tetos ->
            if (categoria == null || valor <= 0) return@combine null
            val gasto = (totais[categoria] ?: 0L) + valor
            val teto = tetos[categoria] ?: 0L
            val percentual = percentualDe(gasto, teto)
            PreviaTeto(
                gastoCentavos = gasto,
                tetoCentavos = teto,
                percentual = percentual,
                status = StatusConsumo.doPercentual(percentual),
            )
        }
    }

    fun alterarValor(centavos: Long) {
        formulario.update {
            it.copy(valorCentavos = centavos, erroValor = validarSePreciso { Validadores.valor(centavos) })
        }
    }

    fun selecionarCategoria(categoria: Categoria) {
        formulario.update {
            it.copy(categoria = categoria, erroCategoria = validarSePreciso { Validadores.categoria(categoria) })
        }
    }

    fun selecionarFormaPagamento(formaPagamento: FormaPagamento) {
        formulario.update {
            it.copy(
                formaPagamento = formaPagamento,
                erroFormaPagamento = validarSePreciso { Validadores.formaPagamento(formaPagamento) },
            )
        }
    }

    fun alterarEstabelecimento(texto: String) {
        val limitado = texto.take(Validadores.ESTABELECIMENTO_MAX)
        formulario.update {
            it.copy(
                estabelecimento = limitado,
                erroEstabelecimento = validarSePreciso { Validadores.estabelecimento(limitado) },
            )
        }
    }

    fun alterarData(data: LocalDate) {
        formulario.update {
            it.copy(data = data, erroData = validarSePreciso { Validadores.data(data, LocalDate.now()) })
        }
    }

    fun salvar() {
        val estado = formulario.value
        if (estado.salvando) return
        val userId = authRepository.usuarioAtualId() ?: return

        validarAoAlterar = true
        val comErros = estado.copy(
            erroValor = Validadores.valor(estado.valorCentavos),
            erroCategoria = Validadores.categoria(estado.categoria),
            erroFormaPagamento = Validadores.formaPagamento(estado.formaPagamento),
            erroEstabelecimento = Validadores.estabelecimento(estado.estabelecimento),
            erroData = Validadores.data(estado.data, LocalDate.now()),
        )
        formulario.value = comErros
        val categoria = comErros.categoria
        val formaPagamento = comErros.formaPagamento
        if (comErros.temErros() || categoria == null || formaPagamento == null) return

        formulario.update { it.copy(salvando = true) }
        viewModelScope.launch {
            compraRepository.salvar(
                Compra(
                    userId = userId,
                    valorCentavos = estado.valorCentavos,
                    categoria = categoria,
                    estabelecimento = estado.estabelecimento.trim(),
                    formaPagamento = formaPagamento,
                    data = estado.data,
                    criadoEm = System.currentTimeMillis(),
                )
            )
            formulario.update { it.copy(salvando = false, mensagemConclusao = MENSAGEM_REGISTRADA) }
        }
    }

    private inline fun validarSePreciso(validacao: () -> String?): String? =
        if (validarAoAlterar) validacao() else null

    private fun NovaCompraUiState.temErros(): Boolean =
        listOf(erroValor, erroCategoria, erroFormaPagamento, erroEstabelecimento, erroData).any { it != null }

    companion object {
        const val MENSAGEM_REGISTRADA = "Compra registrada"
    }
}
