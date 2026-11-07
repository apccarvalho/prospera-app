package com.apccarvalho.prospera.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.apccarvalho.prospera.data.local.entity.ConfiguracaoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ConfiguracaoDao {
    @Query("SELECT * FROM configuracao WHERE userId = :userId")
    fun observar(userId: String): Flow<ConfiguracaoEntity?>

    @Upsert
    suspend fun salvar(configuracao: ConfiguracaoEntity)
}
