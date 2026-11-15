package com.apccarvalho.prospera.ui.navigation

object Rotas {
    const val INICIO = "inicio"
    const val LOGIN = "login"
    const val CADASTRO = "cadastro"
    const val DASHBOARD = "dashboard"
    const val HISTORICO = "historico"

    const val ARG_PRIMEIRO_ACESSO = "primeiroAcesso"
    const val TETOS = "tetos?$ARG_PRIMEIRO_ACESSO={$ARG_PRIMEIRO_ACESSO}"

    const val ARG_COMPRA_ID = "compraId"
    const val NOVA_COMPRA = "nova-compra?$ARG_COMPRA_ID={$ARG_COMPRA_ID}"
    const val COMPRA_NOVA = -1L

    const val CHAVE_MENSAGEM_COMPRA = "mensagemCompra"

    fun tetos(primeiroAcesso: Boolean): String = "tetos?$ARG_PRIMEIRO_ACESSO=$primeiroAcesso"

    fun novaCompra(compraId: Long? = null): String =
        if (compraId == null) "nova-compra" else "nova-compra?$ARG_COMPRA_ID=$compraId"
}
