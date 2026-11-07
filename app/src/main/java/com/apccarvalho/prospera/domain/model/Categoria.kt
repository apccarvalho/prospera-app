package com.apccarvalho.prospera.domain.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Category
import androidx.compose.material.icons.rounded.Celebration
import androidx.compose.material.icons.rounded.LocalGasStation
import androidx.compose.material.icons.rounded.Medication
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

enum class Categoria(
    val rotulo: String,
    val rotuloCurto: String,
) {
    SUPERMERCADO("Supermercado/Feira", "Supermercado"),
    COMBUSTIVEL("Combustível/Transporte", "Combustível"),
    FARMACIA("Farmácia/Saúde", "Farmácia"),
    LAZER("Lazer/Diversão", "Lazer"),
    OUTROS("Outros", "Outros");

    val icone: ImageVector
        get() = when (this) {
            SUPERMERCADO -> Icons.Rounded.ShoppingCart
            COMBUSTIVEL -> Icons.Rounded.LocalGasStation
            FARMACIA -> Icons.Rounded.Medication
            LAZER -> Icons.Rounded.Celebration
            OUTROS -> Icons.Rounded.Category
        }
}
