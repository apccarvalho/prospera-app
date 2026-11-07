package com.apccarvalho.prospera.domain.util

import kotlin.math.abs

object Dinheiro {
    const val VALOR_MAXIMO_CENTAVOS = 99_999_999L

    private val MAXIMO_DIGITOS = VALOR_MAXIMO_CENTAVOS.toString().length

    fun formatar(centavos: Long): String {
        val sinal = if (centavos < 0) "-" else ""
        return "${sinal}R$ ${formatarSemSimbolo(abs(centavos))}"
    }

    fun formatarSemSimbolo(centavos: Long): String {
        val sinal = if (centavos < 0) "-" else ""
        val positivo = abs(centavos)
        val reais = (positivo / 100).toString()
        val resto = (positivo % 100).toString().padStart(2, '0')
        val reaisComMilhar = reais.reversed().chunked(3).joinToString(".").reversed()
        return "$sinal$reaisComMilhar,$resto"
    }

    /**
     * Máscara de entrada em que cada dígito entra pela direita: o texto digitado é reduzido
     * aos seus dígitos e lido como centavos. Se o resultado passar do máximo, a digitação é
     * ignorada e o valor atual é mantido.
     */
    fun aplicarDigitacao(centavosAtuais: Long, novoTexto: String): Long {
        val digitos = novoTexto.filter { it.isDigit() }.trimStart('0')
        if (digitos.isEmpty()) return 0L
        if (digitos.length > MAXIMO_DIGITOS) return centavosAtuais
        val novoValor = digitos.toLong()
        return if (novoValor > VALOR_MAXIMO_CENTAVOS) centavosAtuais else novoValor
    }
}
