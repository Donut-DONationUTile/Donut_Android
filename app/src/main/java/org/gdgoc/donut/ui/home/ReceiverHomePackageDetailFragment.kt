package org.gdgoc.donut.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import org.gdgoc.donut.data.DonutSharedPreferences
import org.gdgoc.donut.data.remote.response.home.ResponseHomeReceiverBox
import org.gdgoc.donut.data.remote.response.home.ResponseHomeReceiverBoxItemData
import org.gdgoc.donut.data.remote.response.home.ResponseHomeReceiverGift
import org.gdgoc.donut.databinding.FragmentReceiverHomePackageDetailBinding
import org.gdgoc.donut.ui.ReceiverMainActivity
import org.gdgoc.donut.ui.home.adpater.DetailItemAdapter
import org.gdgoc.donut.ui.viewModel.HomeViewModel
import org.gdgoc.donut.util.DonutUtil
import org.gdgoc.donut.util.NetworkState

class ReceiverHomePackageDetailFragment : Fragment() {
    private lateinit var binding: FragmentReceiverHomePackageDetailBinding
    private lateinit var itemAdapter: DetailItemAdapter
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReceiverHomePackageDetailBinding.inflate(inflater, container, false)

        initNetwork()
        getReceiverHomeBoxInfo()
        setAdapter()

        return binding.root
    }

    private fun initNetwork(){
        DonutSharedPreferences.getAccessToken()?.let { viewModel.requestReceiverHomeInfo(it) }
    }

    private fun getReceiverHomeBoxInfo() {
        viewModel.receiverHomeBoxInfo.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkState.Loading -> {}
                is NetworkState.Success -> {
                    val data = state.data.data
                    if (data != null) updateBoxInfoUI(data)
                    updateGiftItemList(data?.giftList)
                }
                is NetworkState.Error -> {
                    Toast.makeText(context, "서버 오류입니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateBoxInfoUI(data: ResponseHomeReceiverBoxItemData) {
        val date = data.dueDate.substring(0, 10)
        binding.tvTitle.text = data.store
        binding.tvAmountNum.text = data.amount.toString()
        binding.tvDueNum.text = DonutUtil().setCalendarFormat(date)
    }

    private fun updateGiftItemList(giftList: List<ResponseHomeReceiverGift>?) {
        giftList?.let {
            itemAdapter.setGiftItemList(it)
        }
    }

    private fun setAdapter() {
        itemAdapter = DetailItemAdapter()
        binding.rvDetailItem.adapter = itemAdapter
        binding.rvDetailItem.layoutManager = GridLayoutManager(context, 2)

        itemAdapter.setOnItemClickListener { _, _ ->
            val selectedGiftId = itemAdapter.itemList[itemAdapter.mPosition].giftId
            viewModel.setGiftId(selectedGiftId)
            (activity as ReceiverMainActivity).changeFragment("gift_detail")
        }
    }
}