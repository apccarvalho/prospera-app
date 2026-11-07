package com.apccarvalho.prospera.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apccarvalho.prospera.R
import com.apccarvalho.prospera.ui.theme.Ameixa
import com.apccarvalho.prospera.ui.theme.ProsperaTheme
import com.apccarvalho.prospera.ui.theme.marca

@Composable
fun LogoHorizontal(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Image(
            painter = painterResource(R.drawable.ic_girassol),
            contentDescription = null,
            modifier = Modifier.size(80.dp),
        )
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.marca.copy(fontSize = 42.sp, lineHeight = 48.sp),
            color = Ameixa,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFB9A6F2)
@Composable
private fun LogoHorizontalPreview() {
    ProsperaTheme {
        LogoHorizontal()
    }
}
