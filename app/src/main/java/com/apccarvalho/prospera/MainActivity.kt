package com.apccarvalho.prospera

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.Savings
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apccarvalho.prospera.ui.theme.Contorno
import com.apccarvalho.prospera.ui.theme.CorStatus
import com.apccarvalho.prospera.ui.theme.Dourado
import com.apccarvalho.prospera.ui.theme.ProsperaTheme
import com.apccarvalho.prospera.ui.theme.StatusColors
import com.apccarvalho.prospera.ui.theme.marca
import com.apccarvalho.prospera.ui.theme.valorDestaque

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
                TelaTesteTema()
            }
        }
    }
}

@Composable
private fun TelaTesteTema() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.Savings,
                    contentDescription = null,
                    tint = Dourado,
                    modifier = Modifier.size(36.dp),
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "PROSPERA",
                    style = MaterialTheme.typography.marca,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            Column {
                Text(
                    text = "Total do mês",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = "R$ 1.234,56",
                    style = MaterialTheme.typography.valorDestaque,
                )
            }

            Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Rounded.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Botão primário")
            }

            Card(
                shape = MaterialTheme.shapes.large,
                border = BorderStroke(1.dp, Contorno),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Text("Status", style = MaterialTheme.typography.titleMedium)
                    LinhaStatus("OK", StatusColors.Ok, Icons.Rounded.CheckCircle, 0.45f)
                    LinhaStatus("ATENÇÃO", StatusColors.Atencao, Icons.Rounded.Warning, 0.85f)
                    LinhaStatus("ESTOURADO", StatusColors.Estourado, Icons.Rounded.Error, 1f)
                }
            }
        }
    }
}

@Composable
private fun LinhaStatus(rotulo: String, cor: CorStatus, icone: ImageVector, progresso: Float) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Surface(
            shape = MaterialTheme.shapes.small,
            color = cor.fundoSelo,
            contentColor = cor.texto,
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(icone, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text(
                    text = rotulo,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
        LinearProgressIndicator(
            progress = { progresso },
            color = cor.barra,
            trackColor = cor.fundoSelo,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFAF8FF)
@Composable
private fun TelaTesteTemaPreview() {
    ProsperaTheme {
        TelaTesteTema()
    }
}
