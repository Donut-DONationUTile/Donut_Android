package org.gdgoc.donut.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.gdgoc.donut.data.api.RetrofitBuilder
import org.gdgoc.donut.data.remote.response.ranking.ResponseNumberRanking
import org.gdgoc.donut.data.remote.response.ranking.ResponsePriceRanking

class RankingViewModel(application: Application) : AndroidViewModel(application) {
    private val _priceRankingInfo = MutableLiveData<ResponsePriceRanking>()
    val priceRankingInfo: LiveData<ResponsePriceRanking>
        get() = _priceRankingInfo

    private val _numberRankingInfo = MutableLiveData<ResponseNumberRanking>()
    val numberRankingInfo: LiveData<ResponseNumberRanking>
        get() = _numberRankingInfo

    fun requestPriceRankingInfo(accessToken: String) = viewModelScope.launch(Dispatchers.IO) {
        _priceRankingInfo.postValue(
            RetrofitBuilder.rankingService.getPriceRankingInfo("Bearer $accessToken")
        )
    }

    fun requestNumberRankingInfo(accessToken: String) = viewModelScope.launch(Dispatchers.IO) {
        _numberRankingInfo.postValue(
            RetrofitBuilder.rankingService.getNumberRankingInfo("Bearer $accessToken")
        )
    }
}