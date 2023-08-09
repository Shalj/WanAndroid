package com.shalj.wanandroid.ui.screen.start

import androidx.lifecycle.viewModelScope
import com.shalj.wanandroid.base.BaseViewModel
import com.shalj.wanandroid.base.IUiIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor() : BaseViewModel() {

    private val _startUiState = MutableStateFlow(StartUIState())
    val startUIState = _startUiState.asStateFlow()

    override fun onStart() {
        viewModelScope.launch {
            delay(500)
            _startUiState.update { it.copy(iconVisible = true) }
            delay(1000)
            _startUiState.update { it.copy(textVisible = true) }
        }
    }

    override fun handleIntent(uiIntent: IUiIntent) {

    }
}