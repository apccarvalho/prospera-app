package com.apccarvalho.prospera.data.repository

import com.apccarvalho.prospera.data.local.dao.ConfiguracaoDao
import com.apccarvalho.prospera.data.local.dao.TetoDao
import com.apccarvalho.prospera.data.local.entity.ConfiguracaoEntity
import com.apccarvalho.prospera.data.local.entity.TetoEntity
import com.apccarvalho.prospera.domain.model.Categoria
import com.apccarvalho.prospera.domain.model.Configuracao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class OrcamentoRepository(
    private val tetoDao: TetoDao,
    private val configuracaoDao: ConfiguracaoDao,
) {

    fun observarTetos(userId: String): Flow<Map<Categoria, Long>> =
        tetoDao.observar(userId).map { tetos -> tetos.associate { it.categoria to it.valorCentavos } }

    suspend fun salvarTetos(userId: String, tetos: Map<Categoria, Long>) {
        tetoDao.salvarTodos(
            tetos.map { (categoria, valor) -> TetoEntity(userId, categoria, valor) }
        )
    }

    fun observarConfiguracao(userId: String): Flow<Configuracao?> =
        configuracaoDao.observar(userId).map { it?.paraDominio() }

    suspend fun salvarRendaEConcluirOnboarding(userId: String, rendaCentavos: Long?) {
        val atual = configuracaoDao.observar(userId).first() ?: ConfiguracaoEntity(userId)
        configuracaoDao.salvar(
            atual.copy(rendaMensalCentavos = rendaCentavos, onboardingConcluido = true)
        )
    }
}

private fun ConfiguracaoEntity.paraDominio() = Configuracao(
    userId = userId,
    rendaMensalCentavos = rendaMensalCentavos,
    onboardingConcluido = onboardingConcluido,
)
