package com.example.soymusicreviewapp.ui.screens.settings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsState())
    val uiState: StateFlow<SettingsState> = _uiState.asStateFlow()

    fun onLogoutClicked() {
        // Here you would handle the logout logic
        _uiState.update { it.copy(showLogoutDialog = true) }
    }

    fun onDeleteAccountClicked() {
        // Here you would handle the delete account logic
        _uiState.update { it.copy(showDeleteAccountDialog = true) }
    }

    fun dismissLogoutDialog() {
        _uiState.update { it.copy(showLogoutDialog = false) }
    }

    fun dismissDeleteAccountDialog() {
        _uiState.update { it.copy(showDeleteAccountDialog = false) }
    }

    fun confirmLogout() {
        // Actual logout logic here
        _uiState.update { it.copy(showLogoutDialog = false) }
    }

    fun confirmDeleteAccount() {
        // Actual delete account logic here
        _uiState.update { it.copy(showDeleteAccountDialog = false) }
    }
}
