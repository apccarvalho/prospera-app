package com.apccarvalho.prospera.data.local.entity

import androidx.room.Entity
import com.apccarvalho.prospera.domain.model.Categoria

@Entity(
    tableName = "tetos",
    primaryKeys = ["userId", "categoria"],
)
data class TetoEntity(
    val userId: String,
    val categoria: Categoria,
    val valorCentavos: Long,
)
