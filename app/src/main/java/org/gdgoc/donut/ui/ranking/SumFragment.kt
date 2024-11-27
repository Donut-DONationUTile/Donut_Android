package org.gdgoc.donut.ui.ranking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import org.gdgoc.donut.data.DonutSharedPreferences
import org.gdgoc.donut.databinding.FragmentSumBinding
import org.gdgoc.donut.ui.ranking.adapter.SumRankingAdapter
import org.gdgoc.donut.ui.viewModel.RankingViewModel

class SumFragment : Fragment() {
    private lateinit var binding: FragmentSumBinding
    private lateinit var itemAdapter: SumRankingAdapter
    private val viewModel: RankingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSumBinding.inflate(inflater, container, false)

        initNetwork()
        setAdapter()

        return binding.root
    }

    private fun initNetwork(){
        DonutSharedPreferences.getAccessToken()?.let { viewModel.requestPriceRankingInfo(it) }
    }

    private fun setAdapter() {
        itemAdapter = SumRankingAdapter()
        binding.rvRanking.adapter = itemAdapter
        binding.rvRanking.layoutManager = LinearLayoutManager(context)
        setDataList()
    }

    private fun setDataList() {
        viewModel.priceRankingInfo.observe(viewLifecycleOwner, Observer { data ->
            with(binding.rvRanking.adapter as SumRankingAdapter) {
                data.data!!.let { itemAdapter.setPriceRankingItemList(it) }
            }
            binding.tvName.text = data.data?.get(0)!!.name
            binding.tvDollar.text = data.data[0].price.toString()
        })
    }


    companion object {
    }
}