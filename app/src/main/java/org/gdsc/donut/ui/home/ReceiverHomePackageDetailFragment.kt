package org.gdsc.donut.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import org.gdsc.donut.data.local.UnusedItemData
import org.gdsc.donut.databinding.FragmentReceiverHomePackageDetailBinding
import org.gdsc.donut.ui.ReceiverMainActivity
import org.gdsc.donut.ui.home.adpater.DetailItemAdapter

class ReceiverHomePackageDetailFragment : Fragment() {
    private lateinit var binding: FragmentReceiverHomePackageDetailBinding
    private lateinit var itemAdapter: DetailItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReceiverHomePackageDetailBinding.inflate(inflater, container, false)

        setAdapter()

        return binding.root
    }

    private fun setAdapter() {
        itemAdapter = DetailItemAdapter()
        binding.rvDetailItem.adapter = itemAdapter
        binding.rvDetailItem.layoutManager = GridLayoutManager(context, 2)
        itemAdapter.itemList.addAll(
            listOf(
                UnusedItemData("34", "chupa chups", "19.94"),
                UnusedItemData("34", "chupa chups", "19.94"),
                UnusedItemData("34", "chupa chups", "19.94"),
                UnusedItemData("34", "chupa chups", "19.94"),
                UnusedItemData("34", "chupa chups", "19.94"),
                UnusedItemData("34", "chupa chups", "19.94"),
                UnusedItemData("34", "chupa chups", "19.94"),
                UnusedItemData("34", "chupa chups", "19.94")
            )
        )

        itemAdapter.setOnItemClickListener { _, pos ->
            for (changePos in itemAdapter.itemList.indices) {
                (activity as ReceiverMainActivity).changeFragment("gift_detail")
            }
        }
    }

    companion object {
    }
}