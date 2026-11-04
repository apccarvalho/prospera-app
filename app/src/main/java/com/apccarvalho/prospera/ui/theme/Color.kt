package com.apccarvalho.prospera.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

val Roxo = Color(0xFF7A5CC9)
val Branco = Color(0xFFFFFFFF)
val LilasMedio = Color(0xFFB9A6F2)
val LilasClaro = Color(0xFFEDE7FF)
val Ameixa = Color(0xFF2E2350)
val FundoLavanda = Color(0xFFFAF8FF)

val TextoSecundario = Color(0xFF3F3566)
val TextoSuave = Color(0xFF6B6380)
val Contorno = Color(0xFFD9D1EE)
val Placeholder = Color(0xFF9A93AD)
val Erro = Color(0xFFD93B3B)

val Dourado = Color(0xFFE8A317)
val DouradoEscuro = Color(0xFFC9820A)

@Immutable
data class CorStatus(
    val barra: Color,
    val fundoSelo: Color,
    val texto: Color,
)

object StatusColors {
    val Ok = CorStatus(
        barra = Color(0xFF2E9E5B),
        fundoSelo = Color(0xFFE3F4EA),
        texto = Color(0xFF1E7A44),
    )
    val Atencao = CorStatus(
        barra = Color(0xFFF2C200),
        fundoSelo = Color(0xFFFFF4C7),
        texto = Color(0xFF7A5E00),
    )
    val Estourado = CorStatus(
        barra = Color(0xFFD93B3B),
        fundoSelo = Color(0xFFFBE3E3),
        texto = Color(0xFFA82626),
    )
}
