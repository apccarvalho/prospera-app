package com.apccarvalho.prospera.domain.util

import com.apccarvalho.prospera.domain.model.Categoria
import com.apccarvalho.prospera.domain.model.FormaPagamento
import java.time.LocalDate

object Validadores {
    const val ESTABELECIMENTO_MIN = 2
    const val ESTABELECIMENTO_MAX = 50
    const val SENHA_MIN = 6

    private val REGEX_EMAIL = Regex("""^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\.[A-Za-z0-9-]+)*\.[A-Za-z]{2,}$""")

    fun valor(centavos: Long): String? = when {
        centavos <= 0 -> "Informe um valor maior que zero"
        centavos > Dinheiro.VALOR_MAXIMO_CENTAVOS -> "Valor acima do limite permitido"
        else -> null
    }

    fun categoria(categoria: Categoria?): String? =
        if (categoria == null) "Escolha uma categoria" else null

    fun formaPagamento(formaPagamento: FormaPagamento?): String? =
        if (formaPagamento == null) "Escolha a forma de pagamento" else null

    fun estabelecimento(texto: String): String? {
        val limpo = texto.trim()
        return when {
            limpo.length < ESTABELECIMENTO_MIN -> "Informe o estabelecimento"
            limpo.length > ESTABELECIMENTO_MAX -> "Use no máximo 50 caracteres"
            else -> null
        }
    }

    fun data(data: LocalDate, hoje: LocalDate): String? =
        if (data.isAfter(hoje)) "A data não pode ser futura" else null

    fun teto(centavos: Long): String? = valor(centavos)

    fun nome(texto: String): String? =
        if (texto.isBlank()) "Informe seu nome" else null

    fun email(texto: String): String? =
        if (REGEX_EMAIL.matches(texto.trim())) null else "Informe um e-mail válido"

    fun senha(texto: String): String? =
        if (texto.length < SENHA_MIN) "A senha deve ter pelo menos 6 caracteres" else null

    fun confirmacaoSenha(senha: String, confirmacao: String): String? =
        if (senha != confirmacao) "As senhas não conferem" else null
}
