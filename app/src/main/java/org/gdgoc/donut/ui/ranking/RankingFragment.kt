package org.gdgoc.donut.ui.ranking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import org.gdgoc.donut.databinding.FragmentRankingBinding
import org.gdgoc.donut.ui.ranking.adapter.RankingViewPagerAdapter

class RankingFragment : Fragment() {
    private lateinit var binding: FragmentRankingBinding
    private lateinit var itemViewPagerAdapter: RankingViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRankingBinding.inflate(inflater, container, false)

        setViewPager()
        initTabLayout()

        return binding.root
    }

    private fun setViewPager(){
        val fragmentList = listOf(SumFragment(), NumberFragment())

        itemViewPagerAdapter = RankingViewPagerAdapter(this.requireActivity())
        itemViewPagerAdapter.fragments.addAll(fragmentList)

        binding.vpItems.adapter = itemViewPagerAdapter
    }

    private fun initTabLayout(){
        val tabLabel = listOf("sum", "number")
        TabLayoutMediator(binding.tlMenu, binding.vpItems){tab, position -> tab.text = tabLabel[position]}.attach()
    }

    companion object {
    }
}