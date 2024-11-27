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
import org.gdgoc.donut.data.remote.response.home.ResponseHomeGiver
import org.gdgoc.donut.data.remote.response.home.ResponseHomeReceiver
import org.gdgoc.donut.data.remote.response.home.ResponseHomeReceiverBoxItem
import org.gdgoc.donut.data.remote.response.home.ResponseHomeReceiverGiftItem
import org.gdgoc.donut.data.remote.response.home.ResponseWalletDetailItem
import org.gdgoc.donut.data.remote.response.home.ResponseWalletGiftList
import org.gdgoc.donut.data.remote.response.home.ResponseWalletGiver
import org.gdgoc.donut.data.remote.response.home.ResponseWalletImpendingList
import org.gdgoc.donut.data.remote.response.message.ResponseSendMsg

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val _giverHomeInfo = MutableLiveData<ResponseHomeGiver>()
    val giverHomeInfo: LiveData<ResponseHomeGiver>
        get() = _giverHomeInfo

    private val _giverWalletInfo = MutableLiveData<ResponseWalletGiver>()
    val giverWalletInfo: LiveData<ResponseWalletGiver>
        get() = _giverWalletInfo

    private val _giverWalletImpendingInfo = MutableLiveData<ResponseWalletImpendingList>()
    val giverWalletImpendingInfo: LiveData<ResponseWalletImpendingList>
        get() = _giverWalletImpendingInfo

    private val _giverWalletGiftInfo = MutableLiveData<ResponseWalletGiftList>()
    val giverWalletGiftInfo: LiveData<ResponseWalletGiftList>
        get() = _giverWalletGiftInfo

    private val _walletDetailInfo = MutableLiveData<ResponseWalletDetailItem>()
    val walletDetailInfo: LiveData<ResponseWalletDetailItem>
        get() = _walletDetailInfo

    private val _receiverHomeInfo = MutableLiveData<ResponseHomeReceiver>()
    val receiverHomeInfo: LiveData<ResponseHomeReceiver>
        get() = _receiverHomeInfo

    private val _receiverHomeBoxInfo = MutableLiveData<ResponseHomeReceiverBoxItem>()
    val receiverHomeBoxInfo: LiveData<ResponseHomeReceiverBoxItem>
        get() = _receiverHomeBoxInfo

    private val _receiverHomeGiftInfo = MutableLiveData<ResponseHomeReceiverGiftItem>()
    val receiverHomeGiftInfo: LiveData<ResponseHomeReceiverGiftItem>
        get() = _receiverHomeGiftInfo

    private val _sendMsgInfo = MutableLiveData<ResponseSendMsg>()
    val sendMsgInfo: LiveData<ResponseSendMsg>
        get() = _sendMsgInfo

    val sharedBoxId = MutableLiveData<Long>()
    fun setBoxId(input: Long) {
        sharedBoxId.value = input
    }

    val sharedGiftId = MutableLiveData<Long>()
    fun setGiftId(input: Long) {
        sharedGiftId.value = input
    }

    val sharedContent = MutableLiveData<String>()
    fun setContent(input: String){
        sharedContent.value = input
    }

    fun requestGiverHomeInfo(accessToken: String) = viewModelScope.launch(Dispatchers.IO) {
        _giverHomeInfo.postValue(
            RetrofitBuilder.homeService.getGiverHomeInfo("Bearer $accessToken")
        )
    }

    fun requestGiverWalletInfo(accessToken: String) = viewModelScope.launch(Dispatchers.IO) {
        _giverWalletInfo.postValue(
            RetrofitBuilder.homeService.getGiverWalletInfo("Bearer $accessToken")
        )
    }

    fun requestWalletDetailInfo(accessToken: String, giftId: Long) = viewModelScope.launch(Dispatchers.IO) {
        _walletDetailInfo.postValue(
            RetrofitBuilder.homeService.getWalletGiftInfo("Bearer $accessToken", giftId)
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

    fun requestSendMsg(accessToken: String, giftID: Long, content: String) = viewModelScope.launch(Dispatchers.IO) {
        _sendMsgInfo.postValue(
            RetrofitBuilder.messageService.sendMessage("Bearer $accessToken", RequestSendMsg(giftID, content))
        )
    }

}