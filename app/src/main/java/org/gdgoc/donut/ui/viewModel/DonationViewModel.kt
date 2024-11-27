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
import org.gdgoc.donut.data.remote.response.donation.ResponseAddToWallet
import org.gdgoc.donut.data.remote.response.donation.ResponseAssignReceiver
import org.gdgoc.donut.data.remote.response.donation.ResponseDirectDonation
import org.gdgoc.donut.data.remote.response.donation.ResponseDonateGiver
import org.gdgoc.donut.util.Event
import kotlin.Exception

class DonationViewModel(application: Application) : AndroidViewModel(application) {
    private val _assignReceiverInfo = MutableLiveData<ResponseAssignReceiver>()
    val assignReceiverInfo: LiveData<ResponseAssignReceiver>
        get() = _assignReceiverInfo

    private val _donateGiverInfo = MutableLiveData<ResponseDonateGiver>()
    val donateGiverInfo: LiveData<ResponseDonateGiver>
        get() = _donateGiverInfo

    private val _addToWalletInfo = MutableLiveData<ResponseAddToWallet>()
    val addToWalletInfo: LiveData<ResponseAddToWallet>
        get() = _addToWalletInfo

    private val _donateDirectInfo = MutableLiveData<ResponseDirectDonation>()
    val donateDirectInfo: LiveData<ResponseDirectDonation>
        get() = _donateDirectInfo

    private val _showErrorToast = MutableLiveData<Event<Boolean>>()
    val showErrorToast: LiveData<Event<Boolean>> = _showErrorToast

    val sharedDirectDonationOption = MutableLiveData<Boolean>()
    fun setDirectDonationOption(input: Boolean) {
        sharedDirectDonationOption.value = input
    }

    private val sharedStoreName = MutableLiveData<String>()
    val sharedGiftImageString = MutableLiveData<String>()
    val sharedProduct = MutableLiveData<RequestBody>()
    val sharedPrice = MutableLiveData<Int>()
    val sharedDueDate = MutableLiveData<RequestBody>()
    val sharedStore = MutableLiveData<RequestBody>()

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

    fun requestAssignReceiver(accessToken: String, price: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            try {
            _assignReceiverInfo.postValue(sharedStoreName.value?.let { RequestAssignReceiver(it, price) }?.let {
                    RetrofitBuilder.donationService.assignReceiver("Bearer $accessToken", it)
                })
            } catch (e: Exception){
                _showErrorToast.postValue(Event(true))
            }
        }

    fun requestDonateGiver(accessToken: String, giftImage: MultipartBody.Part?, product: RequestBody, price: Int, dueDate: RequestBody, store: RequestBody, isRestored: RequestBody) =
        viewModelScope.launch(Dispatchers.IO) {
            _donateGiverInfo.postValue(
                RetrofitBuilder.donationService.donateGiver("Bearer $accessToken", giftImage, product, price, dueDate, store, isRestored)
            )
        }

    fun requestAddToWallet(accessToken: String, giftImage: MultipartBody.Part?, product: RequestBody, price: Int, dueDate: RequestBody, store: RequestBody, autoDonation: Boolean) =
        viewModelScope.launch(Dispatchers.IO) {
            _addToWalletInfo.postValue(
                RetrofitBuilder.donationService.requestAddToWallet("Bearer $accessToken", giftImage, product, price, dueDate, store, autoDonation)
            )
        }

    fun requestDirectDonation(accessToken: String, giftId: Long) = viewModelScope.launch(Dispatchers.IO){
        _donateDirectInfo.postValue((RetrofitBuilder.donationService.requestDirectDonation("Bearer $accessToken", giftId)))
    }
}