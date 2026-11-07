package com.apccarvalho.prospera.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.apccarvalho.prospera.data.local.entity.CompraEntity
import com.apccarvalho.prospera.domain.model.Categoria
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

data class TotalPorCategoria(
    val categoria: Categoria,
    val totalCentavos: Long,
)

@Dao
interface CompraDao {
    @Insert
    suspend fun inserir(compra: CompraEntity): Long

    @Query("SELECT * FROM compras WHERE id = :id AND userId = :userId")
    suspend fun buscarPorId(id: Long, userId: String): CompraEntity?

    @Update
    suspend fun atualizar(compra: CompraEntity)

    @Query("DELETE FROM compras WHERE id = :id AND userId = :userId")
    suspend fun excluir(id: Long, userId: String)

    @Query(
        """
        SELECT * FROM compras
        WHERE userId = :userId AND data BETWEEN :inicio AND :fim
        ORDER BY data DESC, criadoEm DESC
        """
    )
    fun observarDoPeriodo(userId: String, inicio: LocalDate, fim: LocalDate): Flow<List<CompraEntity>>

    @Query(
        """
        SELECT categoria, SUM(valorCentavos) AS totalCentavos FROM compras
        WHERE userId = :userId AND data BETWEEN :inicio AND :fim
        GROUP BY categoria
        """
    )
    fun observarTotaisPorCategoria(
        userId: String,
        inicio: LocalDate,
        fim: LocalDate,
    ): Flow<List<TotalPorCategoria>>
}
