package com.apccarvalho.prospera.data.repository

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class AuthRepository(private val auth: FirebaseAuth) {

    fun usuarioAtualId(): String? = auth.currentUser?.uid

    fun nomeUsuario(): String? = auth.currentUser?.displayName

    suspend fun entrar(email: String, senha: String): Result<Unit> = executar {
        auth.signInWithEmailAndPassword(email.trim(), senha).await()
    }

    suspend fun cadastrar(nome: String, email: String, senha: String): Result<Unit> = executar {
        val resultado = auth.createUserWithEmailAndPassword(email.trim(), senha).await()
        val perfil = userProfileChangeRequest { displayName = nome.trim() }
        resultado.user?.updateProfile(perfil)?.await()
    }

    fun sair() {
        auth.signOut()
    }

    private suspend fun executar(bloco: suspend () -> Unit): Result<Unit> =
        try {
            bloco()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(Exception(mensagemDeErro(e), e))
        }

    // A ordem importa: FirebaseAuthWeakPasswordException é subclasse de FirebaseAuthInvalidCredentialsException.
    private fun mensagemDeErro(e: Exception): String = when (e) {
        is FirebaseAuthWeakPasswordException -> "A senha deve ter pelo menos 6 caracteres"
        is FirebaseAuthUserCollisionException -> "Este e-mail já está cadastrado"
        is FirebaseAuthInvalidCredentialsException,
        is FirebaseAuthInvalidUserException -> "E-mail ou senha incorretos"
        is FirebaseNetworkException -> "Sem conexão com a internet"
        else -> "Não foi possível concluir. Tente novamente"
    }
}
