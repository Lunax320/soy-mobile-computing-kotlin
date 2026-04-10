package com.example.soymusicreviewapp.ui.Splash

import androidx.lifecycle.ViewModel
import com.example.soymusicreviewapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _navigateHome = MutableStateFlow(false)
    val navigateHome: StateFlow<Boolean> = _navigateHome

    init {
        checkUser()
    }

    private fun checkUser(){
        // Se fuerza la navegacion al inicio
        _navigateHome.value = true
    }
}