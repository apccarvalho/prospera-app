package com.apccarvalho.prospera.domain.model

import kotlin.math.abs

data class ResumoCategoria(
    val categoria: Categoria,
    val gastoCentavos: Long,
    val tetoCentavos: Long,
    val percentual: Double,
    val status: StatusConsumo,
) {
    val estourado: Boolean
        get() = status == StatusConsumo.ESTOURADO

    val restanteOuExcedenteCentavos: Long
        get() = abs(tetoCentavos - gastoCentavos)
}
