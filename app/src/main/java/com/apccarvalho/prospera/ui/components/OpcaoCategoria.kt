package com.apccarvalho.prospera.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apccarvalho.prospera.domain.model.Categoria
import com.apccarvalho.prospera.ui.theme.Ameixa
import com.apccarvalho.prospera.ui.theme.Branco
import com.apccarvalho.prospera.ui.theme.Contorno
import com.apccarvalho.prospera.ui.theme.LilasClaro
import com.apccarvalho.prospera.ui.theme.ProsperaTheme
import com.apccarvalho.prospera.ui.theme.TextoSecundario

private val FormatoOpcao = RoundedCornerShape(16.dp)

@Composable
fun OpcaoCategoria(
    categoria: Categoria,
    selecionada: Boolean,
    aoClicar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val primary = MaterialTheme.colorScheme.primary
    Box(
        modifier = modifier
            .height(84.dp)
            .clip(FormatoOpcao)
            .background(if (selecionada) LilasClaro else Branco)
            .border(
                width = if (selecionada) 1.5.dp else 1.dp,
                color = if (selecionada) primary else Contorno,
                shape = FormatoOpcao,
            )
            .selectable(selected = selecionada, role = Role.RadioButton, onClick = aoClicar),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(if (selecionada) Branco else LilasClaro, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = categoria.icone,
                    contentDescription = null,
                    tint = primary,
                    modifier = Modifier.size(20.dp),
                )
            }
            Spacer(Modifier.height(6.dp))
            Text(
                text = categoria.rotuloCurto,
                fontSize = 12.sp,
                fontWeight = if (selecionada) FontWeight.Bold else FontWeight.Medium,
                color = if (selecionada) Ameixa else TextoSecundario,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        if (selecionada) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .size(18.dp)
                    .background(primary, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = null,
                    tint = Branco,
                    modifier = Modifier.size(12.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFAF8FF)
@Composable
private fun OpcaoCategoriaPreview() {
    ProsperaTheme {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            OpcaoCategoria(
                categoria = Categoria.SUPERMERCADO,
                selecionada = true,
                aoClicar = {},
                modifier = Modifier.weight(1f),
            )
            OpcaoCategoria(
                categoria = Categoria.COMBUSTIVEL,
                selecionada = false,
                aoClicar = {},
                modifier = Modifier.weight(1f),
            )
            OpcaoCategoria(
                categoria = Categoria.FARMACIA,
                selecionada = false,
                aoClicar = {},
                modifier = Modifier.weight(1f),
            )
        }
    }
}
