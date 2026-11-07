package com.apccarvalho.prospera.domain.model

import com.apccarvalho.prospera.ui.theme.CorStatus
import com.apccarvalho.prospera.ui.theme.StatusColors

enum class StatusConsumo {
    OK,
    ATENCAO,
    ESTOURADO;

    val cores: CorStatus
        get() = when (this) {
            OK -> StatusColors.Ok
            ATENCAO -> StatusColors.Atencao
            ESTOURADO -> StatusColors.Estourado
        }

    companion object {
        const val LIMITE_ATENCAO = 0.70
        const val LIMITE_ESTOURADO = 1.0

        fun doPercentual(percentual: Double): StatusConsumo = when {
            percentual >= LIMITE_ESTOURADO -> ESTOURADO
            percentual >= LIMITE_ATENCAO -> ATENCAO
            else -> OK
        }
    }
}
