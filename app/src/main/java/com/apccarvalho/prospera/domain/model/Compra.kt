package com.apccarvalho.prospera.domain.model

import java.time.LocalDate

data class Compra(
    val id: Long = 0,
    val userId: String,
    val valorCentavos: Long,
    val categoria: Categoria,
    val estabelecimento: String,
    val formaPagamento: FormaPagamento,
    val data: LocalDate,
    val criadoEm: Long = 0,
)
