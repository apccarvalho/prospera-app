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
import com.apccarvalho.prospera.ui.dashboard.DashboardViewModel
import com.apccarvalho.prospera.ui.navigation.InicioViewModel
import com.apccarvalho.prospera.ui.tetos.TetosViewModel

val ProsperaViewModelFactory: ViewModelProvider.Factory = viewModelFactory {
    initializer {
        val container = appContainer()
        LoginViewModel(container.authRepository, container.orcamentoRepository)
    }
    initializer {
        CadastroViewModel(appContainer().authRepository)
    }
    initializer {
        val container = appContainer()
        InicioViewModel(container.authRepository, container.orcamentoRepository)
    }
    initializer {
        val container = appContainer()
        TetosViewModel(container.authRepository, container.orcamentoRepository)
    }
    initializer {
        DashboardViewModel(appContainer().authRepository)
    }
}

private fun CreationExtras.appContainer(): AppContainer =
    (checkNotNull(this[APPLICATION_KEY]) as ProsperaApp).container
