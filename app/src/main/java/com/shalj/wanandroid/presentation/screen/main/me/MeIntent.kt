package com.shalj.wanandroid.presentation.screen.main.me

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.shalj.wanandroid.base.BaseViewModel
import com.shalj.wanandroid.di.userInfoKey
import com.shalj.wanandroid.model.LoginResp
import com.shalj.wanandroid.net.toModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MeIntent @Inject constructor(
    dataStore: DataStore<Preferences>
) : BaseViewModel() {

    val userInfo =
        dataStore.data.map { it[userInfoKey].orEmpty().ifEmpty { "{}" }.toModel<LoginResp>() }
}