package org.gdgoc.donut.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import org.gdgoc.donut.data.DonutSharedPreferences
import org.gdgoc.donut.data.remote.response.home.ResponseHomeReceiverGift
import org.gdgoc.donut.data.remote.response.home.ResponseHomeReceiverGiftItem
import org.gdgoc.donut.data.remote.response.home.ResponseHomeReceiverGiftItemData
import org.gdgoc.donut.databinding.FragmentReceiverHomeGiftDetailBinding
import org.gdgoc.donut.ui.ReceiverMainActivity
import org.gdgoc.donut.ui.viewModel.HomeViewModel
import org.gdgoc.donut.ui.viewModel.ReportViewModel
import org.gdgoc.donut.util.DonutUtil
import org.gdgoc.donut.util.NetworkState

class ReceiverHomeGiftDetailFragment : Fragment() {
    private lateinit var binding: FragmentReceiverHomeGiftDetailBinding
    private val viewModel: HomeViewModel by activityViewModels()
    private val reportViewModel: ReportViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReceiverHomeGiftDetailBinding.inflate(inflater, container, false)

        initNetwork()
        getReceiverHomeGiftInfo()
        setReportBtn()

        return binding.root
    }

    private fun initNetwork(){
        DonutSharedPreferences.getAccessToken()?.let { viewModel.requestReceiverHomeInfo(it) }
    }

    private fun getReceiverHomeGiftInfo() {
        viewModel.receiverHomeGiftInfo.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkState.Loading -> {}
                is NetworkState.Success -> {
                    state.data.data?.let { updateGiftInfoUI(it) }
                }
                is NetworkState.Error -> {
                    Toast.makeText(context, "서버 오류입니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setReportBtn() {
        binding.btnReport.setOnClickListener {
            val giftId = viewModel.sharedGiftId.value
            if (giftId != null) {
                DonutSharedPreferences.getAccessToken()?.let { token ->
                    reportViewModel.requestReportUsed(token, giftId)
                }
                requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
                startActivity(Intent(context, ReceiverMainActivity::class.java))
            } else {
                Toast.makeText(context, "선택된 아이템이 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateGiftInfoUI(data: ResponseHomeReceiverGiftItemData) {
        val date = data.dueDate.substring(0, 10)
        binding.tvTitle.text = data.product
        binding.tvAmountNum.text = data.price.toString()
        binding.tvDueTitleNum.text = DonutUtil().setCalendarFormat(date)
        binding.tvDueNum.text = date
        binding.tvStoreText.text = data.store
        binding.tvStatusText.text = if (data.status == "USED") "can use" else "not used"

        Glide.with(this)
            .load(data.imgUrl)
            .fitCenter()
            .into(binding.ivImage)
    }
}