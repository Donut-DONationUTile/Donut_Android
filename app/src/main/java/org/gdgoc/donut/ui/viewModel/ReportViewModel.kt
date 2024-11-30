package org.gdgoc.donut.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.gdgoc.donut.data.api.RetrofitBuilder
import org.gdgoc.donut.data.remote.request.report.RequestReport
import org.gdgoc.donut.data.remote.response.report.ResponseReport
import org.gdgoc.donut.data.remote.response.report.ResponseReportUnused
import org.gdgoc.donut.data.remote.response.report.ResponseReportUsed

class ReportViewModel(application: Application) : AndroidViewModel(application) {
    private val _cheatedItemInfo = MutableLiveData<ResponseReport>()
    val cheatedItemInfo: LiveData<ResponseReport>
        get() = _cheatedItemInfo

    private val _unusedItemInfo = MutableLiveData<ResponseReportUnused>()
    val unusedItemInfo: LiveData<ResponseReportUnused>
        get() = _unusedItemInfo

    private val _reportUsedInfo = MutableLiveData<ResponseReportUsed>()
    val reportUsedInfo: LiveData<ResponseReportUsed>
        get() = _reportUsedInfo

    fun setCheatedItem(accessToken: String, giftId: Long) = viewModelScope.launch(Dispatchers.IO){
        _cheatedItemInfo.postValue((RetrofitBuilder.reportService.reportCheat("Bearer $accessToken", RequestReport(giftId))))
    }

    fun setUnusedItem(accessToken: String, giftId: Long) = viewModelScope.launch(Dispatchers.IO){
        _unusedItemInfo.postValue((RetrofitBuilder.reportService.reportUnused("Bearer $accessToken", giftId)))
    }

    fun requestReportUsed(accessToken: String, giftId: Long) = viewModelScope.launch(Dispatchers.IO) {
        _reportUsedInfo.postValue(
            RetrofitBuilder.reportService.reportUsed("Bearer $accessToken", giftId= giftId)
        )
    }
}