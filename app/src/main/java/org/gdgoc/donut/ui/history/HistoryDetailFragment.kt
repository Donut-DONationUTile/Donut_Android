package org.gdgoc.donut.ui.history

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import org.gdgoc.donut.R
import org.gdgoc.donut.data.DonutSharedPreferences
import org.gdgoc.donut.data.remote.response.history.ResponseHistoryGiverDetailData
import org.gdgoc.donut.databinding.FragmentHistoryDetailBinding
import org.gdgoc.donut.ui.viewModel.HistoryViewModel
import org.gdgoc.donut.util.DonutUtil
import org.gdgoc.donut.util.NetworkState

class HistoryDetailFragment : Fragment() {
    private lateinit var binding: FragmentHistoryDetailBinding
    private val viewModel: HistoryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryDetailBinding.inflate(inflater, container, false)

        initNetwork()
        getHistoryDetailInfo()

        return binding.root
    }

    private fun initNetwork(){
        viewModel.sharedGiftId.observe(viewLifecycleOwner, Observer { data ->
            DonutSharedPreferences.getAccessToken()?.let { viewModel.requestGiverHistoryDetailInfo(it, data) }
        })
    }

    @SuppressLint("ResourceAsColor")
    private fun getHistoryDetailInfo() {
        viewModel.giverHistoryDetailInfo.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkState.Loading -> {}
                is NetworkState.Success -> {
                    state.data.data?.let { updateHistoryDetailUI(it) }
                }
                is NetworkState.Error -> {
                    Toast.makeText(context, "서버 오류입니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun updateHistoryDetailUI(data: ResponseHistoryGiverDetailData) {
        val date = data.dueDate.substring(0, 10)
        val givenDate = data.donateDate.substring(0, 10)
        val receivedDate = data.receivedDate?.substring(0, 10) ?: ""

        // 상태별 태그 UI 업데이트
        when (data.status) {
            "USED" -> {
                binding.clTag.setBackgroundResource(R.drawable.bg_gray100_round20)
                binding.tvTag.setTextColor(resources.getColor(R.color.gray_300))
                binding.tvTag.text = getString(R.string.used)
            }
            "UNUSED" -> {
                binding.clTag.setBackgroundResource(R.drawable.bg_coral_maincoral_round20)
                binding.tvTag.setTextColor(resources.getColor(R.color.main_coral))
                binding.tvTag.text = getString(R.string.unused)
            }
        }

        // 공통 UI 업데이트
        binding.tvTitle.text = data.product
        binding.tvAmountNum.text = data.amount.toString()
        binding.tvDueTitleNum.text = DonutUtil().setCalendarFormat(date)
        binding.tvDueNum.text = date
        binding.tvStoreText.text = data.store

        // 역할별 UI 업데이트
        if (DonutSharedPreferences.getUserRole() == "giver") {
            updateGiverDetailUI(data, givenDate)
        } else {
            updateReceiverDetailUI(data, receivedDate)
        }
    }

    private fun updateGiverDetailUI(data: ResponseHistoryGiverDetailData, givenDate: String) {
        binding.tvGivenDateNum.text = givenDate
        binding.tvStatusText.text = data.receiver
        binding.tvMsgText.text = data.message
    }

    private fun updateReceiverDetailUI(data: ResponseHistoryGiverDetailData, receivedDate: String) {
        binding.tvGivenDate.text = getString(R.string.giverHistory_receivedDay)
        binding.tvGivenDateNum.text = receivedDate
        binding.tvStatus.text = "from"
        binding.tvStatusText.text = data.giver
        binding.tvMsg.text = getString(R.string.giverHistory_sentMsg)
        binding.tvMsgText.text = data.message
    }
}