package com.apccarvalho.prospera.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.apccarvalho.prospera.ProsperaApp
import com.apccarvalho.prospera.di.AppContainer
import com.apccarvalho.prospera.ui.auth.CadastroViewModel
import com.apccarvalho.prospera.ui.auth.LoginViewModel

val ProsperaViewModelFactory: ViewModelProvider.Factory = viewModelFactory {
    initializer {
        val container = appContainer()
        LoginViewModel(container.authRepository, container.orcamentoRepository)
    }
    initializer {
        CadastroViewModel(appContainer().authRepository)
    }
}

private fun CreationExtras.appContainer(): AppContainer =
    (checkNotNull(this[APPLICATION_KEY]) as ProsperaApp).container
