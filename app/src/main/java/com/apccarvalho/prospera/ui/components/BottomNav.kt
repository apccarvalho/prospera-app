package com.apccarvalho.prospera.ui.components

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ReceiptLong
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Savings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.apccarvalho.prospera.R
import com.apccarvalho.prospera.ui.theme.Ameixa
import com.apccarvalho.prospera.ui.theme.Branco
import com.apccarvalho.prospera.ui.theme.LilasClaro
import com.apccarvalho.prospera.ui.theme.ProsperaTheme
import com.apccarvalho.prospera.ui.theme.TextoSecundario

enum class AbaPrincipal(
    @StringRes val rotulo: Int,
    val icone: ImageVector,
) {
    INICIO(R.string.nav_inicio, Icons.Rounded.Home),
    HISTORICO(R.string.nav_historico, Icons.AutoMirrored.Rounded.ReceiptLong),
    TETOS(R.string.nav_tetos, Icons.Rounded.Savings),
}

@Composable
fun BottomNav(
    abaAtiva: AbaPrincipal?,
    aoSelecionar: (AbaPrincipal) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(
        modifier = modifier,
        containerColor = Branco,
    ) {
        AbaPrincipal.entries.forEach { aba ->
            val selecionada = aba == abaAtiva
            NavigationBarItem(
                selected = selecionada,
                onClick = { if (!selecionada) aoSelecionar(aba) },
                icon = { Icon(aba.icone, contentDescription = null) },
                label = {
                    Text(
                        text = stringResource(aba.rotulo),
                        fontWeight = if (selecionada) FontWeight.Bold else FontWeight.Medium,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = Ameixa,
                    indicatorColor = LilasClaro,
                    unselectedIconColor = TextoSecundario,
                    unselectedTextColor = TextoSecundario,
                ),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomNavPreview() {
    ProsperaTheme {
        BottomNav(abaAtiva = AbaPrincipal.INICIO, aoSelecionar = {})
    }
}
