package com.apccarvalho.prospera.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apccarvalho.prospera.R
import com.apccarvalho.prospera.ui.theme.Branco
import com.apccarvalho.prospera.ui.theme.ProsperaTheme

@Composable
fun BotaoNovaCompra(
    aoClicar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ExtendedFloatingActionButton(
        onClick = aoClicar,
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = Branco,
        icon = { Icon(Icons.Rounded.Add, contentDescription = null) },
        text = {
            Text(
                text = stringResource(R.string.nova_compra),
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
            )
        },
    )
}

@Preview
@Composable
private fun BotaoNovaCompraPreview() {
    ProsperaTheme {
        BotaoNovaCompra(aoClicar = {}, modifier = Modifier.padding(16.dp))
    }
}
