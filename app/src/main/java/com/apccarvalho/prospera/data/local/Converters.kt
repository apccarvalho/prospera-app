package com.apccarvalho.prospera.data.local

import androidx.room.TypeConverter
import com.apccarvalho.prospera.domain.model.Categoria
import com.apccarvalho.prospera.domain.model.FormaPagamento
import java.time.LocalDate

class Converters {
    @TypeConverter
    fun categoriaParaTexto(categoria: Categoria): String = categoria.name

    @TypeConverter
    fun textoParaCategoria(texto: String): Categoria = Categoria.valueOf(texto)

    @TypeConverter
    fun formaPagamentoParaTexto(forma: FormaPagamento): String = forma.name

    @TypeConverter
    fun textoParaFormaPagamento(texto: String): FormaPagamento = FormaPagamento.valueOf(texto)

    @TypeConverter
    fun dataParaEpochDay(data: LocalDate): Long = data.toEpochDay()

    @TypeConverter
    fun epochDayParaData(epochDay: Long): LocalDate = LocalDate.ofEpochDay(epochDay)
}
