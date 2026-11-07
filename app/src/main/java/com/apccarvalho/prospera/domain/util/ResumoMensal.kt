package com.apccarvalho.prospera.domain.util

import com.apccarvalho.prospera.domain.model.Categoria
import com.apccarvalho.prospera.domain.model.ResumoCategoria
import com.apccarvalho.prospera.domain.model.StatusConsumo

fun percentualDe(gastoCentavos: Long, tetoCentavos: Long): Double =
    if (tetoCentavos > 0) gastoCentavos.toDouble() / tetoCentavos else 0.0

/** Uma entrada por categoria, mesmo sem gasto, do maior para o menor percentual; empates seguem a ordem do enum. */
fun resumirCategorias(
    tetos: Map<Categoria, Long>,
    totais: Map<Categoria, Long>,
): List<ResumoCategoria> =
    Categoria.entries
        .map { categoria ->
            val gasto = totais[categoria] ?: 0L
            val teto = tetos[categoria] ?: 0L
            val percentual = percentualDe(gasto, teto)
            ResumoCategoria(
                categoria = categoria,
                gastoCentavos = gasto,
                tetoCentavos = teto,
                percentual = percentual,
                status = StatusConsumo.doPercentual(percentual),
            )
        }
        .sortedWith(
            compareByDescending<ResumoCategoria> { it.percentual }.thenBy { it.categoria.ordinal }
        )
