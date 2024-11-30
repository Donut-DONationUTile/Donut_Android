package org.gdgoc.donut.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.gdgoc.donut.data.DonutSharedPreferences
import org.gdgoc.donut.data.api.RetrofitBuilder
import org.gdgoc.donut.data.remote.request.auth.*
import org.gdgoc.donut.data.remote.response.auth.*
import org.gdgoc.donut.util.Event
import org.gdgoc.donut.util.NetworkState

class SignViewModel(application: Application) : AndroidViewModel(application) {

    private val _receiverSignUpInfo = MutableLiveData<NetworkState<ResponseSignUpReceiver>>()
    val receiverSignUpInfo: LiveData<NetworkState<ResponseSignUpReceiver>> get() = _receiverSignUpInfo

    private val _receiverSignInInfo = MutableLiveData<NetworkState<ResponseSignInReceiver>>()
    val receiverSignInInfo: LiveData<NetworkState<ResponseSignInReceiver>> get() = _receiverSignInInfo

    private val _giverSignInInfo = MutableLiveData<NetworkState<ResponseSignInGiver>>()
    val giverSignInInfo: LiveData<NetworkState<ResponseSignInGiver>> get() = _giverSignInInfo

    private val _googleLoginInfo = MutableLiveData<NetworkState<ResponseGoogleLogin>>()
    val googleLoginInfo: LiveData<NetworkState<ResponseGoogleLogin>> get() = _googleLoginInfo

    private val _fcmInfo = MutableLiveData<NetworkState<ResponseSendFCMToken>>()
    val fcmInfo: LiveData<NetworkState<ResponseSendFCMToken>> get() = _fcmInfo

    private val _showErrorToast = MutableLiveData<Event<String>>()
    val showErrorToast: LiveData<Event<String>> get() = _showErrorToast

    fun saveUserId(id: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            DonutSharedPreferences.setUserId(id)
        }
    }

    fun saveAccessToken(token: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            DonutSharedPreferences.setAccessToken(token)
        }
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

    fun requestReceiverSignUp(id: String, password: String) {
        handleRequest(_receiverSignUpInfo) {
            RetrofitBuilder.authService.signUpReceiver(RequestSignUpReceiver(id, password))
        }
    }

    fun requestReceiverSignIn(id: String, password: String) {
        handleRequest(_receiverSignInInfo) {
            RetrofitBuilder.authService.signInReceiver(RequestSignInReceiver(id, password))
        }
    }

    fun requestGiverSignIn(idToken: String) {
        handleRequest(_giverSignInInfo) {
            RetrofitBuilder.authService.signInGiver(RequestSignInGiver(idToken))
        }
    }

    fun requestGoogleLogin(clientId: String, clientSecret: String, code: String, grantType: String, redirectUri: String) {
        handleRequest(_googleLoginInfo) {
            RetrofitBuilder.googleService.signInWithGoogle(
                RequestGoogleLogin(clientId, clientSecret, code, grantType, redirectUri)
            )
        }
    }

    fun sendFCMToken(accessToken: String, token: String) {
        handleRequest(_fcmInfo) {
            RetrofitBuilder.authService.sendFCMToken(
                "Bearer $accessToken",
                RequestSendFCMToken(token)
            )
        }
    }
}
