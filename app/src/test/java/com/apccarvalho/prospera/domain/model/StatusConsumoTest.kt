package com.apccarvalho.prospera.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class StatusConsumoTest {

    @Test
    fun `sem consumo é OK`() {
        assertEquals(StatusConsumo.OK, StatusConsumo.doPercentual(0.0))
    }

    @Test
    fun `abaixo de 70 por cento é OK`() {
        assertEquals(StatusConsumo.OK, StatusConsumo.doPercentual(0.699))
    }

    @Test
    fun `exatamente 70 por cento é ATENCAO`() {
        assertEquals(StatusConsumo.ATENCAO, StatusConsumo.doPercentual(0.70))
    }

    @Test
    fun `logo abaixo de 100 por cento é ATENCAO`() {
        assertEquals(StatusConsumo.ATENCAO, StatusConsumo.doPercentual(0.999))
    }

    @Test
    fun `exatamente 100 por cento é ESTOURADO`() {
        assertEquals(StatusConsumo.ESTOURADO, StatusConsumo.doPercentual(1.0))
    }

    @Test
    fun `acima de 100 por cento é ESTOURADO`() {
        assertEquals(StatusConsumo.ESTOURADO, StatusConsumo.doPercentual(1.13))
    }
}
