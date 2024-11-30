package org.gdgoc.donut.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayoutMediator
import org.gdgoc.donut.data.DonutSharedPreferences
import org.gdgoc.donut.databinding.FragmentReceiverHistoryBinding
import org.gdgoc.donut.ui.history.adapter.HistoryViewPagerAdapter
import org.gdgoc.donut.ui.viewModel.HistoryViewModel

class ReceiverHistoryFragment : Fragment() {
    private lateinit var binding: FragmentReceiverHistoryBinding
    private lateinit var itemViewPagerAdapter: HistoryViewPagerAdapter
    private val viewModel: HistoryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReceiverHistoryBinding.inflate(inflater, container, false)

        initNetwork()
        getReceiverHistoryInfo()
        setViewPager()
        initTabLayout()

        return binding.root
    }

    private fun initNetwork(){
        DonutSharedPreferences.getAccessToken()?.let { viewModel.requestReceiverHistoryInfo(it) }
    }

    private fun getReceiverHistoryInfo(){
        viewModel.receiverHistoryInfo.observe(viewLifecycleOwner, Observer { data ->
            binding.tvDollarNum.text = data.data!!.amount.toString()
        })
    }

    private fun setViewPager(){
        val fragmentList = listOf(UnusedFragment(), UsedFragment())

        itemViewPagerAdapter = HistoryViewPagerAdapter(this.requireActivity())
        itemViewPagerAdapter.fragments.addAll(fragmentList)

        binding.vpItems.adapter = itemViewPagerAdapter
    }

    private fun initTabLayout(){
        val tabLabel = listOf("unused", "used")
        TabLayoutMediator(binding.tlMenu, binding.vpItems){tab, position -> tab.text = tabLabel[position]}.attach()
    }

    companion object {
    }
}