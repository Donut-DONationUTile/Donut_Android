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
import org.gdgoc.donut.databinding.FragmentNumberBinding
import org.gdgoc.donut.ui.ranking.adapter.NumberRankingAdapter
import org.gdgoc.donut.ui.viewModel.RankingViewModel

class NumberFragment : Fragment() {
    private lateinit var binding: FragmentNumberBinding
    private lateinit var itemAdapter: NumberRankingAdapter
    private val viewModel: RankingViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNumberBinding.inflate(inflater, container, false)

        initNetwork()
        setAdapter()

        return binding.root
    }

    private fun initNetwork(){
        DonutSharedPreferences.getAccessToken()?.let { viewModel.requestNumberRankingInfo(it) }
    }

    private fun setAdapter() {
        itemAdapter = NumberRankingAdapter()
        binding.rvRanking.adapter = itemAdapter
        binding.rvRanking.layoutManager = LinearLayoutManager(context)
        setDataList()
    }

    private fun setDataList() {
        viewModel.numberRankingInfo.observe(viewLifecycleOwner, Observer { data ->
            with(binding.rvRanking.adapter as NumberRankingAdapter) {
                data.data!!.let { itemAdapter.setNumberRankingItemList(it) }
            }
            binding.tvName.text = data.data?.get(0)!!.name
            binding.tvDollar.text = data.data[0].number.toString()
        })
    }


    companion object {
    }
}