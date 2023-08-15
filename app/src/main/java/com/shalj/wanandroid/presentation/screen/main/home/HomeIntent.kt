package com.shalj.wanandroid.presentation.screen.main.home

import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.shalj.wanandroid.base.BaseViewModel
import com.shalj.wanandroid.model.BannerData
import com.shalj.wanandroid.net.Api
import com.shalj.wanandroid.net.RequestResult
import com.shalj.wanandroid.presentation.components.multistatewidget.MultiStateWidgetState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : BaseViewModel() {

    private val _multiState = MutableStateFlow<MultiStateWidgetState>(MultiStateWidgetState.Loading)
    private val _errorMsg = MutableStateFlow("")

    private val _banner = MutableStateFlow(listOf<BannerData>())

    val articles = Pager(
        config = PagingConfig(pageSize = 20, prefetchDistance = 1),
        pagingSourceFactory = { HomePagingSource(_multiState, _errorMsg) }
    ).flow.cachedIn(viewModelScope)

    val uiState = combine(
        _multiState,
        _banner,
        _errorMsg
    ) { refreshing, banner, errorMsg ->
        HomeState(refreshing, banner, errorMsg)
    }.shareIn(viewModelScope, started = SharingStarted.WhileSubscribed())

    init {
        getBannerData()
    }

    private fun getBannerData() {
        viewModelScope.launch {
            val result = Api.getBannerData()
            withContext(Dispatchers.Main) {
                when (result) {
                    is RequestResult.Error -> result.msg
                    is RequestResult.Success -> _banner.value = result.data
                }
            }
        }
    }
}