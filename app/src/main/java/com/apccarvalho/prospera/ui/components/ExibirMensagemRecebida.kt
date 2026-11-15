package com.apccarvalho.prospera.ui.components

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

/**
 * Mostra no Snackbar uma mensagem entregue por outra tela e a consome na hora. O Snackbar roda
 * num escopo próprio porque consumir a mensagem muda a chave do efeito e o cancelaria.
 */
@Composable
fun ExibirMensagemRecebida(
    mensagem: String?,
    snackbarHostState: SnackbarHostState,
    aoMensagemExibida: () -> Unit,
) {
    val escopo = rememberCoroutineScope()
    LaunchedEffect(mensagem) {
        if (mensagem != null) {
            aoMensagemExibida()
            escopo.launch { snackbarHostState.showSnackbar(mensagem) }
        }
    }
}
