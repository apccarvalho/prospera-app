package com.apccarvalho.prospera.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apccarvalho.prospera.R
import com.apccarvalho.prospera.domain.model.Categoria
import com.apccarvalho.prospera.domain.model.ResumoCategoria
import com.apccarvalho.prospera.domain.model.StatusConsumo
import com.apccarvalho.prospera.domain.util.Dinheiro
import com.apccarvalho.prospera.ui.theme.Ameixa
import com.apccarvalho.prospera.ui.theme.Branco
import com.apccarvalho.prospera.ui.theme.LilasClaro
import com.apccarvalho.prospera.ui.theme.ProsperaTheme
import com.apccarvalho.prospera.ui.theme.TextoSecundario

private val FormatoCard = RoundedCornerShape(20.dp)

@Composable
fun CardCategoria(
    resumo: ResumoCategoria,
    modifier: Modifier = Modifier,
) {
    val cores = resumo.status.cores
    Column(
        modifier = modifier
            .fillMaxWidth()
            .sombraLilas(FormatoCard)
            .background(Branco, FormatoCard)
            .then(
                if (resumo.estourado) Modifier.border(1.5.dp, cores.barra, FormatoCard) else Modifier
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(LilasClaro, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = resumo.categoria.icone,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(22.dp),
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
            ) {
                Text(
                    text = resumo.categoria.rotulo,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Ameixa,
                )
                Text(
                    text = stringResource(
                        R.string.valor_de_teto,
                        Dinheiro.formatar(resumo.gastoCentavos),
                        Dinheiro.formatar(resumo.tetoCentavos),
                    ),
                    fontSize = 13.sp,
                    color = TextoSecundario,
                )
            }
            SeloPercentual(percentual = resumo.percentual, status = resumo.status)
        }

        BarraProgresso(percentual = resumo.percentual, status = resumo.status)

        MensagemCategoria(resumo)
    }
}

@Composable
private fun MensagemCategoria(resumo: ResumoCategoria) {
    // Sem teto definido o status é OK com gasto acima de zero; o restante não pode ficar negativo.
    val restante = Dinheiro.formatar((resumo.tetoCentavos - resumo.gastoCentavos).coerceAtLeast(0))
    when (resumo.status) {
        StatusConsumo.OK -> Text(
            text = stringResource(R.string.categoria_restam, restante),
            fontSize = 12.sp,
            color = TextoSecundario,
        )

        StatusConsumo.ATENCAO -> Text(
            text = stringResource(R.string.categoria_atencao_restam, restante),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = resumo.status.cores.texto,
        )

        StatusConsumo.ESTOURADO -> Text(
            text = stringResource(
                R.string.categoria_estourado_em,
                Dinheiro.formatar(resumo.restanteOuExcedenteCentavos),
            ),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = resumo.status.cores.texto,
        )
    }
}

private fun resumoExemplo(categoria: Categoria, gasto: Long, teto: Long): ResumoCategoria {
    val percentual = gasto.toDouble() / teto
    return ResumoCategoria(categoria, gasto, teto, percentual, StatusConsumo.doPercentual(percentual))
}

@Preview(showBackground = true, backgroundColor = 0xFFFAF8FF)
@Composable
private fun CardCategoriaPreview() {
    ProsperaTheme {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            CardCategoria(resumoExemplo(Categoria.COMBUSTIVEL, 26_000, 50_000))
            CardCategoria(resumoExemplo(Categoria.SUPERMERCADO, 103_280, 120_000))
            CardCategoria(resumoExemplo(Categoria.FARMACIA, 28_250, 25_000))
        }
    }
}
