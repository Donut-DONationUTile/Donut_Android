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
import org.gdgoc.donut.util.NetworkState
import java.time.LocalDateTime

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val _receiverHistoryInfo = MutableLiveData<NetworkState<ResponseHistoryReceiver>>()
    val receiverHistoryInfo: LiveData<NetworkState<ResponseHistoryReceiver>>
        get() = _receiverHistoryInfo

    private val _giverHistoryInfo = MutableLiveData<NetworkState<ResponseHistoryGiver>>()
    val giverHistoryInfo: LiveData<NetworkState<ResponseHistoryGiver>>
        get() = _giverHistoryInfo

    private val _giverHistoryDetailInfo = MutableLiveData<NetworkState<ResponseHistoryGiverDetail>>()
    val giverHistoryDetailInfo: LiveData<NetworkState<ResponseHistoryGiverDetail>>
        get() = _giverHistoryDetailInfo

    val sharedGiftId = MutableLiveData<Long>()
    fun setGiftId(input: Long) {
        sharedGiftId.value = input
    }

    private fun <T> handleRequest(
        liveData: MutableLiveData<NetworkState<T>>,
        requestBlock: suspend () -> T
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            liveData.postValue(NetworkState.Loading)
            try {
                val response = requestBlock()
                liveData.postValue(NetworkState.Success(response))
            } catch (e: Exception) {
                liveData.postValue(NetworkState.Error("Error: ${e.message}"))
            }
        }
    }

    fun requestReceiverHistoryInfo(accessToken: String) {
        handleRequest(_receiverHistoryInfo) {
            RetrofitBuilder.historyService.getReceiverHistoryInfo("Bearer $accessToken")
        }
    }

    fun requestGiverHistoryInfo(accessToken: String, date: LocalDateTime) {
        handleRequest(_giverHistoryInfo) {
            RetrofitBuilder.historyService.getGiverHistoryInfo("Bearer $accessToken", date)
        }
    }

    fun requestGiverHistoryDetailInfo(accessToken: String, giftId: Long) {
        handleRequest(_giverHistoryDetailInfo) {
            RetrofitBuilder.historyService.getGiverHistoryDetailInfo("Bearer $accessToken", giftId)
        }
    }
}