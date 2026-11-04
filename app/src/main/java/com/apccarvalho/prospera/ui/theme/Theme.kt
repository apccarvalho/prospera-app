package com.apccarvalho.prospera.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

// Os surfaceContainer* do Material 3 têm tons próprios (cinza-lilás) que seriam usados
// por Card, TextField e menus; aqui ficam brancos para seguir a superfície da marca.
private val EsquemaClaro = lightColorScheme(
    primary = Roxo,
    onPrimary = Branco,
    primaryContainer = LilasClaro,
    onPrimaryContainer = Ameixa,
    secondary = LilasMedio,
    onSecondary = Ameixa,
    secondaryContainer = LilasClaro,
    onSecondaryContainer = Ameixa,
    tertiary = Dourado,
    onTertiary = Ameixa,
    background = FundoLavanda,
    onBackground = Ameixa,
    surface = Branco,
    onSurface = Ameixa,
    surfaceVariant = LilasClaro,
    onSurfaceVariant = TextoSuave,
    surfaceContainerLowest = Branco,
    surfaceContainerLow = Branco,
    surfaceContainer = Branco,
    surfaceContainerHigh = Branco,
    surfaceContainerHighest = Branco,
    outline = Contorno,
    outlineVariant = Contorno,
    error = Erro,
    onError = Branco,
    errorContainer = StatusColors.Estourado.fundoSelo,
    onErrorContainer = StatusColors.Estourado.texto,
)

val ProsperaShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(24.dp),
)

@Composable
fun ProsperaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = EsquemaClaro,
        typography = Typography,
        shapes = ProsperaShapes,
        content = content,
    )
}
