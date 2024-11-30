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
import org.gdgoc.donut.data.remote.response.history.ResponseHistoryGiver
import org.gdgoc.donut.data.remote.response.history.ResponseHistoryGiverDetail
import org.gdgoc.donut.data.remote.response.history.ResponseHistoryGiverDetailData
import org.gdgoc.donut.data.remote.response.history.ResponseHistoryGiverDonationList
import org.gdgoc.donut.databinding.FragmentGiverHistoryGiftDetailBinding
import org.gdgoc.donut.ui.viewModel.HistoryViewModel
import org.gdgoc.donut.util.DonutUtil
import org.gdgoc.donut.util.NetworkState

class GiverHistoryGiftDetailFragment : Fragment() {
    private lateinit var binding: FragmentGiverHistoryGiftDetailBinding
    private val viewModel: HistoryViewModel by activityViewModels()

    override fun onStart() {
        super.onStart()

        initNetwork()
        getGiverHistoryDetailInfo()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGiverHistoryGiftDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    private fun initNetwork() {
        viewModel.sharedGiftId.observe(viewLifecycleOwner) { giftId ->
            DonutSharedPreferences.getAccessToken()?.let { token ->
                viewModel.requestGiverHistoryDetailInfo(token, giftId)
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun getGiverHistoryDetailInfo() {
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

        binding.tvTitle.text = data.product
        binding.tvAmountNum.text = data.amount.toString()
        binding.tvDueTitleNum.text = DonutUtil().setCalendarFormat(date)
        binding.tvDueNum.text = date
        binding.tvGivenDateNum.text = givenDate
        binding.tvStoreText.text = data.store
        binding.tvMsgText.text = data.message
        binding.tvStatusText.text = data.receiver

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
    }
}