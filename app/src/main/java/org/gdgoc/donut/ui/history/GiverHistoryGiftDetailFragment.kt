package org.gdgoc.donut.ui.history

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import org.gdgoc.donut.R
import org.gdgoc.donut.data.DonutSharedPreferences
import org.gdgoc.donut.databinding.FragmentGiverHistoryGiftDetailBinding
import org.gdgoc.donut.ui.viewModel.HistoryViewModel
import org.gdgoc.donut.util.DonutUtil

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

    private fun initNetwork(){
        viewModel.sharedGiftId.observe(viewLifecycleOwner, Observer { data ->
            DonutSharedPreferences.getAccessToken()?.let { viewModel.requestGiverHistoryDetailInfo(it, data) }
        })
    }

    @SuppressLint("ResourceAsColor")
    private fun getGiverHistoryDetailInfo(){
        viewModel.giverHistoryDetailInfo.observe(viewLifecycleOwner, Observer { data ->
            val date = data.data!!.dueDate.substring(0, 10)
            val givenDate = data.data.donateDate.substring(0, 10)

            binding.tvTitle.text = data.data.product
            binding.tvAmountNum.text = data.data.amount.toString()
            binding.tvDueTitleNum.text = DonutUtil().setCalendarFormat(date)
            binding.tvDueNum.text = date
            binding.tvGivenDateNum.text = givenDate
            binding.tvStoreText.text = data.data.store
            binding.tvMsgText.text = data.data.message
            binding.tvStatusText.text = data.data.receiver

            if(data.data.status == "USED"){
                binding.clTag.setBackgroundResource(R.drawable.bg_gray100_round20)
                binding.tvTag.setTextColor(resources.getColor(R.color.gray_300))
                binding.tvTag.text = getString(R.string.used)
            }

            if(data.data.status == "UNUSED"){
                binding.clTag.setBackgroundResource(R.drawable.bg_coral_maincoral_round20)
                binding.tvTag.setTextColor(resources.getColor(R.color.main_coral))
                binding.tvTag.text = getString(R.string.unused)
            }
        })
    }
}