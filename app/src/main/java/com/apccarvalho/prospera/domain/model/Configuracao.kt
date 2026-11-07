package com.apccarvalho.prospera.domain.model

data class Configuracao(
    val userId: String,
    val rendaMensalCentavos: Long?,
    val onboardingConcluido: Boolean,
)
