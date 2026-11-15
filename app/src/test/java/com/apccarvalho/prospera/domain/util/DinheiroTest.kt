package com.apccarvalho.prospera.domain.util

import org.junit.Assert.assertEquals
import org.junit.Test

class DinheiroTest {

    @Test
    fun `formatar zero mostra R$ 0,00`() {
        assertEquals("R$ 0,00", Dinheiro.formatar(0))
    }

    @Test
    fun `formatar cinco centavos mostra R$ 0,05`() {
        assertEquals("R$ 0,05", Dinheiro.formatar(5))
    }

    @Test
    fun `formatar valor com milhar separa com ponto e centavos com vírgula`() {
        assertEquals("R$ 1.188,70", Dinheiro.formatar(118_870))
    }

    @Test
    fun `formatar valor máximo mostra R$ 999 mil 999,99`() {
        assertEquals("R$ 999.999,99", Dinheiro.formatar(Dinheiro.VALOR_MAXIMO_CENTAVOS))
    }

    @Test
    fun `formatar valor negativo coloca o sinal antes do símbolo`() {
        assertEquals("-R$ 1,50", Dinheiro.formatar(-150))
    }

    @Test
    fun `formatarSemSimbolo omite o R$`() {
        assertEquals("0,00", Dinheiro.formatarSemSimbolo(0))
        assertEquals("85,90", Dinheiro.formatarSemSimbolo(8_590))
        assertEquals("1.200,00", Dinheiro.formatarSemSimbolo(120_000))
        assertEquals("999.999,99", Dinheiro.formatarSemSimbolo(99_999_999))
    }

    @Test
    fun `digitar 8 5 9 0 preenche o valor da direita para a esquerda`() {
        var centavos = 0L
        val exibidos = mutableListOf<String>()
        for (digito in listOf("8", "5", "9", "0")) {
            centavos = Dinheiro.aplicarDigitacao(centavos, Dinheiro.formatarSemSimbolo(centavos) + digito)
            exibidos += Dinheiro.formatarSemSimbolo(centavos)
        }
        assertEquals(listOf("0,08", "0,85", "8,59", "85,90"), exibidos)
        assertEquals(8_590L, centavos)
    }

    @Test
    fun `apagar o último dígito remove a casa mais à direita`() {
        assertEquals(859L, Dinheiro.aplicarDigitacao(8_590, "85,9"))
    }

    @Test
    fun `apagar todos os dígitos volta para zero`() {
        assertEquals(0L, Dinheiro.aplicarDigitacao(5, ""))
    }

    @Test
    fun `colar texto com letras e símbolos aproveita só os dígitos`() {
        assertEquals(123_456L, Dinheiro.aplicarDigitacao(0, "R$ 1.2a34,5b6!"))
    }

    @Test
    fun `colar texto sem dígitos resulta em zero`() {
        assertEquals(0L, Dinheiro.aplicarDigitacao(1_000, "abc"))
    }

    @Test
    fun `digitar até o limite máximo é aceito`() {
        assertEquals(99_999_999L, Dinheiro.aplicarDigitacao(9_999_999, "99.999,999"))
    }

    @Test
    fun `digitar além do limite máximo mantém o valor atual`() {
        val maximo = Dinheiro.VALOR_MAXIMO_CENTAVOS
        val texto = Dinheiro.formatarSemSimbolo(maximo) + "9"
        assertEquals(maximo, Dinheiro.aplicarDigitacao(maximo, texto))
    }
}
