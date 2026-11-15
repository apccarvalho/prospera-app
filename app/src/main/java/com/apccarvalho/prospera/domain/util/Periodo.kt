package com.apccarvalho.prospera.domain.util

import java.time.LocalDate
import java.time.YearMonth

private val NOMES_MESES = listOf(
    "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
    "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro",
)

private val NOMES_DIAS_SEMANA = listOf(
    "Segunda-feira", "Terça-feira", "Quarta-feira", "Quinta-feira", "Sexta-feira", "Sábado", "Domingo",
)

fun YearMonth.primeiroDia(): LocalDate = atDay(1)

fun YearMonth.ultimoDia(): LocalDate = atEndOfMonth()

// Nomes fixos em vez de DateTimeFormatter para não depender dos dados de locale da JVM/Android.
fun YearMonth.nomeFormatado(): String = "${NOMES_MESES[monthValue - 1]} de $year"

fun LocalDate.rotuloDoDia(hoje: LocalDate): String {
    val prefixo = when (this) {
        hoje -> "Hoje"
        hoje.minusDays(1) -> "Ontem"
        else -> NOMES_DIAS_SEMANA[dayOfWeek.value - 1]
    }
    val diaMes = "${dayOfMonth.toString().padStart(2, '0')}/${monthValue.toString().padStart(2, '0')}"
    return "$prefixo · $diaMes"
}
