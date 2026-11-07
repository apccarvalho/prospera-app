package com.apccarvalho.prospera.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.apccarvalho.prospera.data.local.entity.TetoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TetoDao {
    @Query("SELECT * FROM tetos WHERE userId = :userId")
    fun observar(userId: String): Flow<List<TetoEntity>>

    @Upsert
    suspend fun salvarTodos(tetos: List<TetoEntity>)
}
