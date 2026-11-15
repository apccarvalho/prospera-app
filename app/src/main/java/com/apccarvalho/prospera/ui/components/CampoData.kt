package com.apccarvalho.prospera.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apccarvalho.prospera.R
import com.apccarvalho.prospera.ui.theme.Branco
import com.apccarvalho.prospera.ui.theme.Contorno
import com.apccarvalho.prospera.ui.theme.ProsperaTheme
import com.apccarvalho.prospera.ui.theme.TextoSecundario
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

private val FormatoCampo = RoundedCornerShape(12.dp)
private val FormatoDataCompleta = DateTimeFormatter.ofPattern("dd/MM/yyyy")
private val LocalePtBr = Locale.forLanguageTag("pt-BR")

@Composable
fun CampoData(
    data: LocalDate,
    aoAlterar: (LocalDate) -> Unit,
    rotulo: String,
    modifier: Modifier = Modifier,
    erro: String? = null,
    habilitado: Boolean = true,
    hoje: LocalDate = LocalDate.now(),
) {
    var seletorAberto by rememberSaveable { mutableStateOf(false) }
    val cores = MaterialTheme.colorScheme
    val dataFormatada = data.format(FormatoDataCompleta)
    val texto = when (data) {
        hoje -> stringResource(R.string.data_hoje, dataFormatada)
        hoje.minusDays(1) -> stringResource(R.string.data_ontem, dataFormatada)
        else -> dataFormatada
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = rotulo,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = TextoSecundario,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(top = 6.dp)
                .fillMaxWidth()
                .heightIn(min = 52.dp)
                .clip(FormatoCampo)
                .background(Branco)
                .border(1.dp, if (erro != null) cores.error else Contorno, FormatoCampo)
                .clickable(
                    enabled = habilitado,
                    role = Role.Button,
                    onClickLabel = stringResource(R.string.data_escolher),
                    onClick = { seletorAberto = true },
                )
                .padding(horizontal = 16.dp),
        ) {
            Text(
                text = texto,
                style = MaterialTheme.typography.bodyLarge,
                color = cores.onSurface,
                modifier = Modifier.weight(1f),
            )
            Icon(
                imageVector = Icons.Rounded.CalendarToday,
                contentDescription = null,
                tint = cores.primary,
                modifier = Modifier.size(20.dp),
            )
        }
        if (erro != null) {
            Text(
                text = erro,
                fontSize = 12.sp,
                color = cores.error,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp),
            )
        }
    }

    if (seletorAberto) {
        SeletorData(
            data = data,
            hoje = hoje,
            aoConfirmar = { escolhida ->
                seletorAberto = false
                aoAlterar(escolhida)
            },
            aoCancelar = { seletorAberto = false },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SeletorData(
    data: LocalDate,
    hoje: LocalDate,
    aoConfirmar: (LocalDate) -> Unit,
    aoCancelar: () -> Unit,
) {
    // O DatePicker trabalha com meia-noite em UTC; converter pelo fuso do aparelho
    // deslocaria a data em um dia em fusos negativos, como o de Brasília.
    val datasPermitidas = remember(hoje) {
        object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean =
                !utcParaData(utcTimeMillis).isAfter(hoje)

            override fun isSelectableYear(year: Int): Boolean = year <= hoje.year
        }
    }
    val estado = rememberDatePickerState(
        initialSelectedDateMillis = dataParaUtc(data),
        yearRange = DatePickerDefaults.YearRange.first..hoje.year,
        selectableDates = datasPermitidas,
    )

    // O DatePicker formata meses e dias da semana pelo locale da Configuration e busca os
    // próprios textos nos recursos do Context; os dois são trocados para o calendário
    // aparecer em português mesmo com o aparelho em outro idioma.
    val configuracao = LocalConfiguration.current
    val contexto = LocalContext.current
    val configuracaoPtBr = remember(configuracao) {
        Configuration(configuracao).apply { setLocale(LocalePtBr) }
    }
    val contextoPtBr = remember(contexto, configuracaoPtBr) {
        contexto.createConfigurationContext(configuracaoPtBr)
    }
    val textoConfirmar = stringResource(R.string.data_confirmar)
    val textoCancelar = stringResource(R.string.cancelar)

    CompositionLocalProvider(
        LocalConfiguration provides configuracaoPtBr,
        LocalContext provides contextoPtBr,
    ) {
        DatePickerDialog(
            onDismissRequest = aoCancelar,
            confirmButton = {
                TextButton(
                    onClick = { estado.selectedDateMillis?.let { aoConfirmar(utcParaData(it)) } },
                    enabled = estado.selectedDateMillis != null,
                ) {
                    Text(textoConfirmar)
                }
            },
            dismissButton = {
                TextButton(onClick = aoCancelar) {
                    Text(textoCancelar)
                }
            },
        ) {
            DatePicker(state = estado)
        }
    }
}

private fun dataParaUtc(data: LocalDate): Long =
    data.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

private fun utcParaData(utcTimeMillis: Long): LocalDate =
    Instant.ofEpochMilli(utcTimeMillis).atZone(ZoneOffset.UTC).toLocalDate()

@Preview(showBackground = true, backgroundColor = 0xFFFAF8FF)
@Composable
private fun CampoDataPreview() {
    val hoje = LocalDate.of(2026, 11, 4)
    ProsperaTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            CampoData(data = hoje, aoAlterar = {}, rotulo = "Data", hoje = hoje)
            CampoData(data = hoje.minusDays(1), aoAlterar = {}, rotulo = "Data", hoje = hoje)
            CampoData(
                data = hoje.minusDays(2),
                aoAlterar = {},
                rotulo = "Data",
                hoje = hoje,
                erro = "A data não pode ser futura",
            )
        }
    }
}
