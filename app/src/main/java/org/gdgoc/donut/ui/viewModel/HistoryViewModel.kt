package org.gdgoc.donut.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.gdgoc.donut.data.api.RetrofitBuilder
import org.gdgoc.donut.data.remote.response.history.ResponseHistoryGiver
import org.gdgoc.donut.data.remote.response.history.ResponseHistoryGiverDetail
import org.gdgoc.donut.data.remote.response.history.ResponseHistoryReceiver
import java.time.LocalDateTime

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val _receiverHistoryInfo = MutableLiveData<ResponseHistoryReceiver>()
    val receiverHistoryInfo: LiveData<ResponseHistoryReceiver>
        get() = _receiverHistoryInfo

    private val _giverHistoryInfo = MutableLiveData<ResponseHistoryGiver>()
    val giverHistoryInfo: LiveData<ResponseHistoryGiver>
        get() = _giverHistoryInfo

    private val _giverHistoryDetailInfo = MutableLiveData<ResponseHistoryGiverDetail>()
    val giverHistoryDetailInfo: LiveData<ResponseHistoryGiverDetail>
        get() = _giverHistoryDetailInfo

    val sharedGiftId = MutableLiveData<Long>()
    fun setGiftId(input: Long) {
        sharedGiftId.value = input
    }

    fun requestReceiverHistoryInfo(accessToken: String) = viewModelScope.launch(Dispatchers.IO) {
        _receiverHistoryInfo.postValue(
            RetrofitBuilder.historyService.getReceiverHistoryInfo("Bearer $accessToken")
        )
    }

    fun requestGiverHistoryInfo(accessToken: String, date: LocalDateTime) = viewModelScope.launch(Dispatchers.IO) {
        _giverHistoryInfo.postValue(
            RetrofitBuilder.historyService.getGiverHistoryInfo("Bearer $accessToken", date)
        )
    }

    fun requestGiverHistoryDetailInfo(accessToken: String, giftId: Long) = viewModelScope.launch(Dispatchers.IO) {
        _giverHistoryDetailInfo.postValue(
            RetrofitBuilder.historyService.getGiverHistoryDetailInfo("Bearer $accessToken", giftId)
        )
    }
}