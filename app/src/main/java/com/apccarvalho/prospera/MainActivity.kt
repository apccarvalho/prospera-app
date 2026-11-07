package com.apccarvalho.prospera

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.apccarvalho.prospera.ui.navigation.ProsperaNavHost
import com.apccarvalho.prospera.ui.theme.ProsperaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // O app só tem tema claro; sem isso, com o sistema em modo escuro os ícones
        // das barras ficariam claros sobre o fundo claro.
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT),
        )
        setContent {
            ProsperaTheme {
                ProsperaNavHost()
            }
        }
    }
}
