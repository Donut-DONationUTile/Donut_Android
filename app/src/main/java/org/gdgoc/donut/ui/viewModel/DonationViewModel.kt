package org.gdgoc.donut.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.gdgoc.donut.data.api.RetrofitBuilder
import org.gdgoc.donut.data.remote.request.donation.RequestAssignReceiver
import org.gdgoc.donut.data.remote.response.donation.*
import org.gdgoc.donut.util.Event
import org.gdgoc.donut.util.NetworkState

class DonationViewModel(application: Application) : AndroidViewModel(application) {

    private val _assignReceiverInfo = MutableLiveData<NetworkState<ResponseAssignReceiver>>()
    val assignReceiverInfo: LiveData<NetworkState<ResponseAssignReceiver>> get() = _assignReceiverInfo

    private val _donateGiverInfo = MutableLiveData<NetworkState<ResponseDonateGiver>>()
    val donateGiverInfo: LiveData<NetworkState<ResponseDonateGiver>> get() = _donateGiverInfo

    private val _addToWalletInfo = MutableLiveData<NetworkState<ResponseAddToWallet>>()
    val addToWalletInfo: LiveData<NetworkState<ResponseAddToWallet>> get() = _addToWalletInfo

    private val _donateDirectInfo = MutableLiveData<NetworkState<ResponseDirectDonation>>()
    val donateDirectInfo: LiveData<NetworkState<ResponseDirectDonation>> get() = _donateDirectInfo

    val sharedDirectDonationOption = MutableLiveData<Boolean>()
    val sharedStoreName = MutableLiveData<String>()
    val sharedGiftImageString = MutableLiveData<String>()
    val sharedProduct = MutableLiveData<RequestBody>()
    val sharedPrice = MutableLiveData<Int>()
    val sharedDueDate = MutableLiveData<RequestBody>()
    val sharedStore = MutableLiveData<RequestBody>()

    fun setDirectDonationOption(input: Boolean) {
        sharedDirectDonationOption.value = input
    }

    fun setStoreName(input: String) {
        sharedStoreName.value = input
    }

    fun setGifticonInfo(img: String, product: RequestBody, price: Int, dueDate: RequestBody, store: RequestBody) {
        sharedGiftImageString.value = img
        sharedProduct.value = product
        sharedPrice.value = price
        sharedDueDate.value = dueDate
        sharedStore.value = store
    }

    private fun <T> handleRequest(liveData: MutableLiveData<NetworkState<T>>, requestBlock: suspend () -> T) {
        viewModelScope.launch(Dispatchers.IO) {
            liveData.postValue(NetworkState.Loading)
            try {
                val response = requestBlock()
                liveData.postValue(NetworkState.Success(response))
            } catch (e: Exception) {
                liveData.postValue(NetworkState.Error(e.message ?: "Unknown error"))
            }
        }
    }

    fun requestAssignReceiver(accessToken: String, price: Int) {
        handleRequest(_assignReceiverInfo) {
            sharedStoreName.value?.let { RequestAssignReceiver(it, price) }?.let {
                RetrofitBuilder.donationService.assignReceiver("Bearer $accessToken", it)
            } ?: throw IllegalArgumentException("Store name is null")
        }
    }

    fun requestDonateGiver(accessToken: String, giftImage: MultipartBody.Part?, product: RequestBody, price: Int, dueDate: RequestBody, store: RequestBody, isRestored: RequestBody) {
        handleRequest(_donateGiverInfo) {
            RetrofitBuilder.donationService.donateGiver("Bearer $accessToken", giftImage, product, price, dueDate, store, isRestored)
        }
    }

    fun requestAddToWallet(accessToken: String, giftImage: MultipartBody.Part?, product: RequestBody, price: Int, dueDate: RequestBody, store: RequestBody, autoDonation: Boolean) {
        handleRequest(_addToWalletInfo) {
            RetrofitBuilder.donationService.requestAddToWallet("Bearer $accessToken", giftImage, product, price, dueDate, store, autoDonation)
        }
    }

    fun requestDirectDonation(accessToken: String, giftId: Long) {
        handleRequest(_donateDirectInfo) {
            RetrofitBuilder.donationService.requestDirectDonation("Bearer $accessToken", giftId)
        }
    }
}
