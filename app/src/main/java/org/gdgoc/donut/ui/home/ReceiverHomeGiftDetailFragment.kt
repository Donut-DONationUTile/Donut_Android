package org.gdgoc.donut.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import org.gdgoc.donut.data.DonutSharedPreferences
import org.gdgoc.donut.databinding.FragmentReceiverHomeGiftDetailBinding
import org.gdgoc.donut.ui.ReceiverMainActivity
import org.gdgoc.donut.ui.viewModel.HomeViewModel
import org.gdgoc.donut.ui.viewModel.ReportViewModel
import org.gdgoc.donut.util.DonutUtil

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
        viewModel.sharedGiftId.observe(viewLifecycleOwner, Observer { data ->
            DonutSharedPreferences.getAccessToken()?.let { viewModel.requestReceiverHomeGiftInfo(it, data) }
        })
    }

    private fun getReceiverHomeGiftInfo(){
        viewModel.receiverHomeGiftInfo.observe(viewLifecycleOwner, Observer { data ->
            val date = data.data!!.dueDate.substring(0, 10)
            binding.tvTitle.text = data.data.product
            binding.tvAmountNum.text = data.data.price.toString()
            binding.tvDueTitleNum.text = DonutUtil().setCalendarFormat(date)
            binding.tvDueNum.text = date
            binding.tvStoreText.text = data.data.store
            if(data.data.status == "USED") binding.tvStatusText.text = "can use"
            Glide.with(this)
                .load(data.data.imgUrl)
                .fitCenter()
                .into(binding.ivImage)
        })
    }

    private fun setReportBtn(){
        binding.btnReport.setOnClickListener {
            viewModel.sharedGiftId.observe(viewLifecycleOwner, Observer { data ->
                DonutSharedPreferences.getAccessToken()?.let { reportViewModel.requestReportUsed(it, data) }
            })
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
            startActivity(Intent(context, ReceiverMainActivity::class.java))
            // 나중에 viewModel로 response 옵저빙해서 마지막 아이템인지 받고 메세지 띄우기
        }
    }
}