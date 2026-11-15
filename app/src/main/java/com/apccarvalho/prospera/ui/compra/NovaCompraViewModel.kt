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
    val modoEdicao: Boolean = false,
    val carregando: Boolean = false,
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

    private val formulario = MutableStateFlow(
        NovaCompraUiState(modoEdicao = compraId != null, carregando = compraId != null)
    )

    private val compraOriginal = MutableStateFlow<Compra?>(null)

    private var validarAoAlterar = false

    val uiState: StateFlow<NovaCompraUiState> =
        combine(formulario, observarPrevia()) { estado, previa -> estado.copy(previa = previa) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = formulario.value,
            )

    init {
        if (compraId != null) carregarCompra(compraId)
    }

    private fun carregarCompra(id: Long) {
        val userId = authRepository.usuarioAtualId()
        viewModelScope.launch {
            val compra = userId?.let { compraRepository.buscarPorId(id, it) }
            if (compra == null) {
                formulario.update { it.copy(carregando = false, mensagemConclusao = MENSAGEM_NAO_ENCONTRADA) }
                return@launch
            }
            compraOriginal.value = compra
            formulario.update {
                it.copy(
                    carregando = false,
                    valorCentavos = compra.valorCentavos,
                    categoria = compra.categoria,
                    formaPagamento = compra.formaPagamento,
                    estabelecimento = compra.estabelecimento,
                    data = compra.data,
                )
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observarPrevia(): Flow<PreviaTeto?> {
        val userId = authRepository.usuarioAtualId() ?: return flowOf(null)
        val mesDaCompra = formulario
            .map { YearMonth.from(it.data) }
            .distinctUntilChanged()
        val totaisDoMes = mesDaCompra
            .flatMapLatest { mes -> compraRepository.observarTotaisDoMes(userId, mes).map { mes to it } }
        val categoriaEValor = formulario
            .map { it.categoria to it.valorCentavos }
            .distinctUntilChanged()

        return combine(
            categoriaEValor,
            totaisDoMes,
            orcamentoRepository.observarTetos(userId),
            compraOriginal,
        ) { (categoria, valor), (mes, totais), tetos, original ->
            if (categoria == null || valor <= 0) return@combine null
            // Na edição, o total do banco ainda inclui a versão salva da compra.
            val jaContada = original
                ?.takeIf { it.categoria == categoria && YearMonth.from(it.data) == mes }
                ?.valorCentavos ?: 0L
            val gasto = (totais[categoria] ?: 0L) - jaContada + valor
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
        if (estado.carregando || estado.salvando) return
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
        val original = compraOriginal.value
        viewModelScope.launch {
            val mensagem = if (original == null) {
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
                MENSAGEM_REGISTRADA
            } else {
                val atualizada = compraRepository.atualizar(
                    original.copy(
                        valorCentavos = estado.valorCentavos,
                        categoria = categoria,
                        estabelecimento = estado.estabelecimento.trim(),
                        formaPagamento = formaPagamento,
                        data = estado.data,
                    )
                )
                if (atualizada) MENSAGEM_ATUALIZADA else MENSAGEM_NAO_ENCONTRADA
            }
            formulario.update { it.copy(salvando = false, mensagemConclusao = mensagem) }
        }
    }

    fun excluir() {
        val original = compraOriginal.value ?: return
        if (formulario.value.salvando) return
        formulario.update { it.copy(salvando = true) }
        viewModelScope.launch {
            compraRepository.excluir(original.id, original.userId)
            formulario.update { it.copy(salvando = false, mensagemConclusao = MENSAGEM_EXCLUIDA) }
        }
    }

    private inline fun validarSePreciso(validacao: () -> String?): String? =
        if (validarAoAlterar) validacao() else null

    private fun NovaCompraUiState.temErros(): Boolean =
        listOf(erroValor, erroCategoria, erroFormaPagamento, erroEstabelecimento, erroData).any { it != null }

    companion object {
        const val MENSAGEM_REGISTRADA = "Compra registrada"
        const val MENSAGEM_ATUALIZADA = "Compra atualizada"
        const val MENSAGEM_EXCLUIDA = "Compra excluída"
        const val MENSAGEM_NAO_ENCONTRADA = "Compra não encontrada"
    }
}
