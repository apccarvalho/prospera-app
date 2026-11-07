package com.apccarvalho.prospera.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.apccarvalho.prospera.domain.model.Categoria
import com.apccarvalho.prospera.domain.model.FormaPagamento
import java.time.LocalDate

@Entity(
    tableName = "compras",
    indices = [Index(value = ["userId", "data"])],
)
data class CompraEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String,
    val valorCentavos: Long,
    val categoria: Categoria,
    val estabelecimento: String,
    val formaPagamento: FormaPagamento,
    val data: LocalDate,
    val criadoEm: Long,
)
