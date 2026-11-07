package com.apccarvalho.prospera.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.apccarvalho.prospera.data.local.dao.CompraDao
import com.apccarvalho.prospera.data.local.dao.ConfiguracaoDao
import com.apccarvalho.prospera.data.local.dao.TetoDao
import com.apccarvalho.prospera.data.local.entity.CompraEntity
import com.apccarvalho.prospera.data.local.entity.ConfiguracaoEntity
import com.apccarvalho.prospera.data.local.entity.TetoEntity

@Database(
    entities = [CompraEntity::class, TetoEntity::class, ConfiguracaoEntity::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class ProsperaDatabase : RoomDatabase() {
    abstract fun compraDao(): CompraDao
    abstract fun tetoDao(): TetoDao
    abstract fun configuracaoDao(): ConfiguracaoDao
}
