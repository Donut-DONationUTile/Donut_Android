package org.gdgoc.donut.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import org.gdgoc.donut.data.DonutSharedPreferences
import org.gdgoc.donut.databinding.FragmentUnusedBinding
import org.gdgoc.donut.ui.ReceiverMainActivity
import org.gdgoc.donut.ui.history.adapter.UnusedItemAdapter
import org.gdgoc.donut.ui.viewModel.HistoryViewModel
import org.gdgoc.donut.ui.viewModel.HomeViewModel

class UnusedFragment : Fragment() {
    private lateinit var binding: FragmentUnusedBinding
    private lateinit var itemAdapter: UnusedItemAdapter
    private val viewModel: HistoryViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUnusedBinding.inflate(inflater, container, false)

        initNetwork()
        setAdapter()

        return binding.root
    }

    private fun initNetwork() {
        DonutSharedPreferences.getAccessToken()?.let { viewModel.requestReceiverHistoryInfo(it) }
    }

    private fun setAdapter() {
        itemAdapter = UnusedItemAdapter()
        binding.rvUnusedItem.adapter = itemAdapter
        binding.rvUnusedItem.layoutManager = GridLayoutManager(context, 2)

        itemAdapter.setOnItemClickListener { _, pos ->
            homeViewModel.setGiftId(itemAdapter.itemList[itemAdapter.mPosition].giftId)
            (activity as ReceiverMainActivity).changeFragment("gift_detail")

        }
        setDataList()
    }

    private fun setDataList() {
        viewModel.receiverHistoryInfo.observe(viewLifecycleOwner, Observer { data ->
            with(binding.rvUnusedItem.adapter as UnusedItemAdapter) {
                data.data!!.giftList?.let { itemAdapter.setGiftItemList(it) }
            }
        })
    }
}