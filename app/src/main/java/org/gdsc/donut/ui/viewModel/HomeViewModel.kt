package org.gdsc.donut.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.gdsc.donut.data.api.RetrofitBuilder
import org.gdsc.donut.data.remote.response.home.ResponseHomeGiver
import org.gdsc.donut.data.remote.response.home.ResponseHomeReceiver
import org.gdsc.donut.data.remote.response.home.ResponseHomeReceiverBoxItem
import org.gdsc.donut.data.remote.response.home.ResponseHomeReceiverGiftItem
import org.gdsc.donut.data.remote.response.report.ResponseReportUsed

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val _giverHomeInfo = MutableLiveData<ResponseHomeGiver>()
    val giverHomeInfo: LiveData<ResponseHomeGiver>
        get() = _giverHomeInfo

    private val _receiverHomeInfo = MutableLiveData<ResponseHomeReceiver>()
    val receiverHomeInfo: LiveData<ResponseHomeReceiver>
        get() = _receiverHomeInfo

    private val _receiverHomeBoxInfo = MutableLiveData<ResponseHomeReceiverBoxItem>()
    val receiverHomeBoxInfo: LiveData<ResponseHomeReceiverBoxItem>
        get() = _receiverHomeBoxInfo

    private val _receiverHomeGiftInfo = MutableLiveData<ResponseHomeReceiverGiftItem>()
    val receiverHomeGiftInfo: LiveData<ResponseHomeReceiverGiftItem>
        get() = _receiverHomeGiftInfo

    private val _reportUsedInfo = MutableLiveData<ResponseReportUsed>()
    val reportUsedInfo: LiveData<ResponseReportUsed>
        get() = _reportUsedInfo

    val sharedBoxId = MutableLiveData<Long>()
    fun setBoxId(input: Long) {
        sharedBoxId.value = input
    }

    val sharedGiftId = MutableLiveData<Long>()
    fun setGiftId(input: Long) {
        sharedGiftId.value = input
    }

    fun requestGiverHomeInfo(accessToken: String) = viewModelScope.launch(Dispatchers.IO) {
        _giverHomeInfo.postValue(
            RetrofitBuilder.homeService.getGiverHomeInfo("Bearer $accessToken")
        )
    }

    fun requestReceiverHomeInfo(accessToken: String) = viewModelScope.launch(Dispatchers.IO) {
        _receiverHomeInfo.postValue(
            RetrofitBuilder.homeService.getReceiverHomeInfo("Bearer $accessToken")
        )
    }

    fun requestReceiverHomeBoxInfo(accessToken: String, boxId: Long) = viewModelScope.launch(Dispatchers.IO) {
        _receiverHomeBoxInfo.postValue(
            RetrofitBuilder.homeService.getReceiverHomeBoxInfo("Bearer $accessToken", boxId)
        )
    }

    fun requestReceiverHomeGiftInfo(accessToken: String, giftId: Long) = viewModelScope.launch(Dispatchers.IO) {
        _receiverHomeGiftInfo.postValue(
            RetrofitBuilder.homeService.getReceiverHomeGiftInfo("Bearer $accessToken", giftId)
        )
    }

    fun requestReportUsed(accessToken: String, giftId: Long) = viewModelScope.launch(Dispatchers.IO) {
        _reportUsedInfo.postValue(
            RetrofitBuilder.reportService.reportUsed("Bearer $accessToken", giftId= giftId)
        )
    }
}