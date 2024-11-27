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
import org.gdgoc.donut.databinding.FragmentHistoryDetailBinding
import org.gdgoc.donut.ui.viewModel.HistoryViewModel
import org.gdgoc.donut.util.DonutUtil

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
    private fun getHistoryDetailInfo(){
        viewModel.giverHistoryDetailInfo.observe(viewLifecycleOwner, Observer { data ->
            val date = data.data!!.dueDate.substring(0, 10)
            val givenDate = data.data.donateDate.substring(0, 10)
            var receivedDate = ""
            if(data.data.receivedDate!=null) receivedDate = data.data.receivedDate.substring(0, 10)

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

            binding.tvTitle.text = data.data.product
            binding.tvAmountNum.text = data.data.amount.toString()
            binding.tvDueTitleNum.text = DonutUtil().setCalendarFormat(date)
            binding.tvDueNum.text = date
            binding.tvStoreText.text = data.data.store

            if(DonutSharedPreferences.getUserRole() == "giver") {
                binding.tvGivenDateNum.text = givenDate
                binding.tvStatusText.text = data.data.receiver
                binding.tvMsgText.text = data.data.message
            } else {
                if(DonutSharedPreferences.getUserRole() == "receiver") {
                    binding.tvGivenDate.text = "the day you received"
                    binding.tvGivenDateNum.text = receivedDate

                    binding.tvStatus.text = "from"
                    binding.tvStatusText.text = data.data.giver

                    binding.tvMsg.text = "the message\nyou sent"
                    binding.tvMsgText.text = data.data.message
                }
            }
        })
    }
}