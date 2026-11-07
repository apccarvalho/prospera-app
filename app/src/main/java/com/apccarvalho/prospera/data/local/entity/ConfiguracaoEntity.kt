package com.apccarvalho.prospera.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "configuracao")
data class ConfiguracaoEntity(
    @PrimaryKey val userId: String,
    val rendaMensalCentavos: Long? = null,
    val onboardingConcluido: Boolean = false,
)
