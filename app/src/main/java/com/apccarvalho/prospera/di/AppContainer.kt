package com.apccarvalho.prospera.di

import android.content.Context
import androidx.room.Room
import com.apccarvalho.prospera.data.local.ProsperaDatabase
import com.apccarvalho.prospera.data.repository.AuthRepository
import com.apccarvalho.prospera.data.repository.CompraRepository
import com.apccarvalho.prospera.data.repository.OrcamentoRepository
import com.google.firebase.auth.FirebaseAuth

class AppContainer(context: Context) {

    private val contextoApp = context.applicationContext

    private val database: ProsperaDatabase by lazy {
        Room.databaseBuilder(contextoApp, ProsperaDatabase::class.java, "prospera.db").build()
    }

    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    val authRepository: AuthRepository by lazy { AuthRepository(firebaseAuth) }

    val compraRepository: CompraRepository by lazy { CompraRepository(database.compraDao()) }

    val orcamentoRepository: OrcamentoRepository by lazy {
        OrcamentoRepository(database.tetoDao(), database.configuracaoDao())
    }
}
