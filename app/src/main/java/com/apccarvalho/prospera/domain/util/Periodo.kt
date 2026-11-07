package com.apccarvalho.prospera.domain.util

import java.time.LocalDate
import java.time.YearMonth

private val NOMES_MESES = listOf(
    "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
    "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro",
)

fun YearMonth.primeiroDia(): LocalDate = atDay(1)

fun YearMonth.ultimoDia(): LocalDate = atEndOfMonth()

// Nomes fixos em vez de DateTimeFormatter para não depender dos dados de locale da JVM/Android.
fun YearMonth.nomeFormatado(): String = "${NOMES_MESES[monthValue - 1]} de $year"
