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
import org.gdgoc.donut.databinding.FragmentUsedBinding
import org.gdgoc.donut.ui.history.adapter.UsedItemAdapter
import org.gdgoc.donut.ui.viewModel.HistoryViewModel


class UsedFragment : Fragment() {
    private lateinit var binding: FragmentUsedBinding
    private lateinit var itemAdapter: UsedItemAdapter
    private val viewModel: HistoryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUsedBinding.inflate(inflater, container, false)

        initNetwork()
        setAdapter()

        return binding.root
    }

    private fun initNetwork(){
        DonutSharedPreferences.getAccessToken()?.let { viewModel.requestReceiverHistoryInfo(it) }
    }

    private fun setAdapter(){
        itemAdapter = UsedItemAdapter()
        binding.rvUsedItem.adapter = itemAdapter
        binding.rvUsedItem.layoutManager = GridLayoutManager(context, 2)
        setDataList()
    }

    private fun setDataList() {
        viewModel.receiverHistoryInfo.observe(viewLifecycleOwner, Observer { data ->
            with(binding.rvUsedItem.adapter as UsedItemAdapter) {
                data.data!!.giftList?.let { itemAdapter.setGiftItemList(it) }
            }
        })
    }

    companion object {
    }
}