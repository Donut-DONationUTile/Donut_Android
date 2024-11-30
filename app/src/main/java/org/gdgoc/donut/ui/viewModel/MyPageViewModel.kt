package org.gdgoc.donut.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.gdgoc.donut.data.api.RetrofitBuilder
import org.gdgoc.donut.data.remote.response.mypage.ResponseGiverMyPage
import org.gdgoc.donut.data.remote.response.mypage.ResponseReceiverMyPage

class MyPageViewModel(application: Application) : AndroidViewModel(application) {
    private val _giverMyPageInfo = MutableLiveData<ResponseGiverMyPage>()
    val giverMyPageInfo: LiveData<ResponseGiverMyPage>
        get() = _giverMyPageInfo

    private val _receiverMyPageInfo = MutableLiveData<ResponseReceiverMyPage>()
    val receiverMyPageInfo: LiveData<ResponseReceiverMyPage>
        get() = _receiverMyPageInfo

    fun requestGiverMyPageInfo(accessToken: String) = viewModelScope.launch(Dispatchers.IO) {
        _giverMyPageInfo.postValue(
            RetrofitBuilder.myPageService.getGiverMyPageInfo("Bearer $accessToken")
        )
    }

    fun requestReceiverMyPageInfo(accessToken: String) = viewModelScope.launch(Dispatchers.IO) {
        _receiverMyPageInfo.postValue(
            RetrofitBuilder.myPageService.getReceiverMyPageInfo("Bearer $accessToken")
        )
    }
}