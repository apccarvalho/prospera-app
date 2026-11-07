package com.apccarvalho.prospera.domain.model

enum class FormaPagamento(val rotulo: String) {
    PIX("Pix"),
    DINHEIRO("Dinheiro"),
    CREDITO("Crédito"),
    DEBITO("Débito"),
    BOLETO("Boleto"),
}
