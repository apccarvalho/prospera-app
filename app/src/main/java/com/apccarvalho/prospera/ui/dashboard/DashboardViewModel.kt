package com.apccarvalho.prospera.ui.dashboard

import androidx.lifecycle.ViewModel
import com.apccarvalho.prospera.data.repository.AuthRepository

class DashboardViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {

    fun sair() {
        authRepository.sair()
    }
}
