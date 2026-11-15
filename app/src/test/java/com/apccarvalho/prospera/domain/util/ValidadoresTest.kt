package com.apccarvalho.prospera.domain.util

import com.apccarvalho.prospera.domain.model.Categoria
import com.apccarvalho.prospera.domain.model.FormaPagamento
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate

class ValidadoresTest {

    private val hoje = LocalDate.of(2026, 11, 4)

    @Test
    fun `valor zero retorna mensagem de erro`() {
        assertEquals("Informe um valor maior que zero", Validadores.valor(0))
    }

    @Test
    fun `valor negativo retorna mensagem de erro`() {
        assertEquals("Informe um valor maior que zero", Validadores.valor(-1))
    }

    @Test
    fun `valor de um centavo é válido`() {
        assertNull(Validadores.valor(1))
    }

    @Test
    fun `valor igual ao máximo é válido`() {
        assertNull(Validadores.valor(Dinheiro.VALOR_MAXIMO_CENTAVOS))
    }

    @Test
    fun `valor acima do máximo retorna mensagem de erro`() {
        assertEquals(
            "Valor acima do limite permitido",
            Validadores.valor(Dinheiro.VALOR_MAXIMO_CENTAVOS + 1),
        )
    }

    @Test
    fun `teto segue as mesmas regras do valor`() {
        assertNotNull(Validadores.teto(0))
        assertNull(Validadores.teto(1))
        assertNotNull(Validadores.teto(Dinheiro.VALOR_MAXIMO_CENTAVOS + 1))
    }

    @Test
    fun `categoria nula retorna mensagem de erro`() {
        assertEquals("Escolha uma categoria", Validadores.categoria(null))
    }

    @Test
    fun `categoria escolhida é válida`() {
        assertNull(Validadores.categoria(Categoria.SUPERMERCADO))
    }

    @Test
    fun `forma de pagamento nula retorna mensagem de erro`() {
        assertEquals("Escolha a forma de pagamento", Validadores.formaPagamento(null))
    }

    @Test
    fun `forma de pagamento escolhida é válida`() {
        assertNull(Validadores.formaPagamento(FormaPagamento.PIX))
    }

    @Test
    fun `estabelecimento com 1 caractere retorna mensagem de erro`() {
        assertEquals("Informe o estabelecimento", Validadores.estabelecimento("A"))
    }

    @Test
    fun `estabelecimento com 2 caracteres é válido`() {
        assertNull(Validadores.estabelecimento("Ab"))
    }

    @Test
    fun `estabelecimento com 50 caracteres é válido`() {
        assertNull(Validadores.estabelecimento("a".repeat(50)))
    }

    @Test
    fun `estabelecimento com 51 caracteres retorna mensagem de erro`() {
        assertEquals("Use no máximo 50 caracteres", Validadores.estabelecimento("a".repeat(51)))
    }

    @Test
    fun `estabelecimento só com espaços retorna mensagem de erro`() {
        assertEquals("Informe o estabelecimento", Validadores.estabelecimento("     "))
    }

    @Test
    fun `espaços nas pontas do estabelecimento não contam no tamanho`() {
        assertEquals("Informe o estabelecimento", Validadores.estabelecimento("  A  "))
        assertNull(Validadores.estabelecimento("  " + "a".repeat(50) + "  "))
    }

    @Test
    fun `data de hoje é válida`() {
        assertNull(Validadores.data(hoje, hoje))
    }

    @Test
    fun `data de ontem é válida`() {
        assertNull(Validadores.data(hoje.minusDays(1), hoje))
    }

    @Test
    fun `data de amanhã retorna mensagem de erro`() {
        assertEquals("A data não pode ser futura", Validadores.data(hoje.plusDays(1), hoje))
    }

    @Test
    fun `nome em branco retorna mensagem de erro`() {
        assertEquals("Informe seu nome", Validadores.nome("   "))
    }

    @Test
    fun `nome preenchido é válido`() {
        assertNull(Validadores.nome("Andréia"))
    }

    @Test
    fun `e-mails válidos são aceitos`() {
        listOf(
            "maria@email.com",
            "maria.silva+compras@sub.dominio.com.br",
            "  maria@email.com  ",
        ).forEach { email ->
            assertNull(email, Validadores.email(email))
        }
    }

    @Test
    fun `e-mails inválidos retornam mensagem de erro`() {
        listOf(
            "",
            "maria",
            "maria@",
            "maria.com",
            "@email.com",
            "maria@email",
            "maria@email.c",
            "ma ria@email.com",
        ).forEach { email ->
            assertEquals(email, "Informe um e-mail válido", Validadores.email(email))
        }
    }

    @Test
    fun `senha com 5 caracteres retorna mensagem de erro`() {
        assertEquals("A senha deve ter pelo menos 6 caracteres", Validadores.senha("12345"))
    }

    @Test
    fun `senha com 6 caracteres é válida`() {
        assertNull(Validadores.senha("123456"))
    }

    @Test
    fun `confirmação igual a senha é válida`() {
        assertNull(Validadores.confirmacaoSenha("segredo1", "segredo1"))
    }

    @Test
    fun `confirmação diferente da senha retorna mensagem de erro`() {
        assertEquals("As senhas não conferem", Validadores.confirmacaoSenha("segredo1", "segredo2"))
    }
}
