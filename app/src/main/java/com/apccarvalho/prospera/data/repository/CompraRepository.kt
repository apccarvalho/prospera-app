package com.apccarvalho.prospera.data.repository

import com.apccarvalho.prospera.data.local.dao.CompraDao
import com.apccarvalho.prospera.data.local.entity.CompraEntity
import com.apccarvalho.prospera.domain.model.Categoria
import com.apccarvalho.prospera.domain.model.Compra
import com.apccarvalho.prospera.domain.util.primeiroDia
import com.apccarvalho.prospera.domain.util.ultimoDia
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.YearMonth

class CompraRepository(
    private val compraDao: CompraDao,
    private val relogio: () -> Long = System::currentTimeMillis,
) {

    suspend fun salvar(compra: Compra): Long =
        compraDao.inserir(compra.paraEntity().copy(id = 0, criadoEm = relogio()))

    suspend fun buscarPorId(id: Long, userId: String): Compra? =
        compraDao.buscarPorId(id, userId)?.paraDominio()

    /** Devolve false quando a compra não existe ou pertence a outro usuário. */
    suspend fun atualizar(compra: Compra): Boolean {
        val original = compraDao.buscarPorId(compra.id, compra.userId) ?: return false
        compraDao.atualizar(
            original.copy(
                valorCentavos = compra.valorCentavos,
                categoria = compra.categoria,
                estabelecimento = compra.estabelecimento.trim(),
                formaPagamento = compra.formaPagamento,
                data = compra.data,
            )
        )
        return true
    }

    suspend fun excluir(id: Long, userId: String) {
        compraDao.excluir(id, userId)
    }

    fun observarComprasDoMes(userId: String, mes: YearMonth): Flow<List<Compra>> =
        compraDao.observarDoPeriodo(userId, mes.primeiroDia(), mes.ultimoDia())
            .map { lista -> lista.map { it.paraDominio() } }

    fun observarTotaisDoMes(userId: String, mes: YearMonth): Flow<Map<Categoria, Long>> =
        compraDao.observarTotaisPorCategoria(userId, mes.primeiroDia(), mes.ultimoDia())
            .map { totais -> totais.associate { it.categoria to it.totalCentavos } }
}

private fun Compra.paraEntity() = CompraEntity(
    id = id,
    userId = userId,
    valorCentavos = valorCentavos,
    categoria = categoria,
    estabelecimento = estabelecimento.trim(),
    formaPagamento = formaPagamento,
    data = data,
    criadoEm = criadoEm,
)

private fun CompraEntity.paraDominio() = Compra(
    id = id,
    userId = userId,
    valorCentavos = valorCentavos,
    categoria = categoria,
    estabelecimento = estabelecimento,
    formaPagamento = formaPagamento,
    data = data,
    criadoEm = criadoEm,
)
