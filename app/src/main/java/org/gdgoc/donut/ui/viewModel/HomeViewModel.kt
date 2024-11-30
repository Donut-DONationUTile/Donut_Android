package org.gdgoc.donut.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.gdgoc.donut.data.api.RetrofitBuilder
import org.gdgoc.donut.data.remote.request.message.RequestSendMsg
import org.gdgoc.donut.data.remote.response.home.*
import org.gdgoc.donut.data.remote.response.message.ResponseSendMsg
import org.gdgoc.donut.util.Event
import org.gdgoc.donut.util.NetworkState

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _giverHomeInfo = MutableLiveData<NetworkState<ResponseHomeGiver>>()
    val giverHomeInfo: LiveData<NetworkState<ResponseHomeGiver>> get() = _giverHomeInfo

    private val _giverWalletInfo = MutableLiveData<NetworkState<ResponseWalletGiver>>()
    val giverWalletInfo: LiveData<NetworkState<ResponseWalletGiver>> get() = _giverWalletInfo

    private val _giverWalletImpendingInfo = MutableLiveData<NetworkState<ResponseWalletImpendingList>>()
    val giverWalletImpendingInfo: LiveData<NetworkState<ResponseWalletImpendingList>> get() = _giverWalletImpendingInfo

    private val _giverWalletGiftInfo = MutableLiveData<NetworkState<ResponseWalletGiftList>>()
    val giverWalletGiftInfo: LiveData<NetworkState<ResponseWalletGiftList>> get() = _giverWalletGiftInfo

    private val _walletDetailInfo = MutableLiveData<NetworkState<ResponseWalletDetailItem>>()
    val walletDetailInfo: LiveData<NetworkState<ResponseWalletDetailItem>> get() = _walletDetailInfo

    private val _receiverHomeInfo = MutableLiveData<NetworkState<ResponseHomeReceiver>>()
    val receiverHomeInfo: LiveData<NetworkState<ResponseHomeReceiver>> get() = _receiverHomeInfo

    private val _receiverHomeBoxInfo = MutableLiveData<NetworkState<ResponseHomeReceiverBoxItem>>()
    val receiverHomeBoxInfo: LiveData<NetworkState<ResponseHomeReceiverBoxItem>> get() = _receiverHomeBoxInfo

    private val _receiverHomeGiftInfo = MutableLiveData<NetworkState<ResponseHomeReceiverGiftItem>>()
    val receiverHomeGiftInfo: LiveData<NetworkState<ResponseHomeReceiverGiftItem>> get() = _receiverHomeGiftInfo

    private val _sendMsgInfo = MutableLiveData<NetworkState<ResponseSendMsg>>()
    val sendMsgInfo: LiveData<NetworkState<ResponseSendMsg>> get() = _sendMsgInfo

    private val _showErrorToast = MutableLiveData<Event<String>>()
    val showErrorToast: LiveData<Event<String>> get() = _showErrorToast

    val sharedBoxId = MutableLiveData<Long>()
    fun setBoxId(input: Long) {
        sharedBoxId.value = input
    }

    val sharedGiftId = MutableLiveData<Long>()
    fun setGiftId(input: Long) {
        sharedGiftId.value = input
    }

    val sharedContent = MutableLiveData<String>()
    fun setContent(input: String) {
        sharedContent.value = input
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
                _showErrorToast.postValue(Event("An error occurred: ${e.message}"))
            }
        }
    }

    fun requestGiverHomeInfo(accessToken: String) {
        handleRequest(_giverHomeInfo) {
            RetrofitBuilder.homeService.getGiverHomeInfo("Bearer $accessToken")
        }
    }

    fun requestGiverWalletInfo(accessToken: String) {
        handleRequest(_giverWalletInfo) {
            RetrofitBuilder.homeService.getGiverWalletInfo("Bearer $accessToken")
        }
    }

    fun requestWalletDetailInfo(accessToken: String, giftId: Long) {
        handleRequest(_walletDetailInfo) {
            RetrofitBuilder.homeService.getWalletGiftInfo("Bearer $accessToken", giftId)
        }
    }

    fun requestReceiverHomeInfo(accessToken: String) {
        handleRequest(_receiverHomeInfo) {
            RetrofitBuilder.homeService.getReceiverHomeInfo("Bearer $accessToken")
        }
    }

    fun requestReceiverHomeBoxInfo(accessToken: String, boxId: Long) {
        handleRequest(_receiverHomeBoxInfo) {
            RetrofitBuilder.homeService.getReceiverHomeBoxInfo("Bearer $accessToken", boxId)
        }
    }

    fun requestReceiverHomeGiftInfo(accessToken: String, giftId: Long) {
        handleRequest(_receiverHomeGiftInfo) {
            RetrofitBuilder.homeService.getReceiverHomeGiftInfo("Bearer $accessToken", giftId)
        }
    }

    fun requestSendMsg(accessToken: String, giftId: Long, content: String) {
        handleRequest(_sendMsgInfo) {
            RetrofitBuilder.messageService.sendMessage(
                "Bearer $accessToken",
                RequestSendMsg(giftId, content)
            )
        }
    }
}
