package org.gdgoc.donut.ui.history

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.R
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import org.gdgoc.donut.data.DonutSharedPreferences
import org.gdgoc.donut.databinding.FragmentHistoryBinding
import org.gdgoc.donut.ui.GiverMainActivity
import org.gdgoc.donut.ui.ReceiverMainActivity
import org.gdgoc.donut.ui.history.adapter.GiverHistoryAdapter
import org.gdgoc.donut.ui.history.adapter.MonthAdapter
import org.gdgoc.donut.ui.history.adapter.ReceiverHistoryAdapter
import org.gdgoc.donut.ui.viewModel.HistoryViewModel
import java.time.LocalDate
import java.time.LocalDateTime

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var menuAdapter: MonthAdapter
    private lateinit var giverItemAdapter: GiverHistoryAdapter
    private lateinit var receiverItemAdapter: ReceiverHistoryAdapter
    private val viewModel: HistoryViewModel by activityViewModels()
    private var filteredYear: Int = LocalDate.now().year
    private var filteredMonth: Int = LocalDate.now().monthValue

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)

        setDropDownMenu()
        setChipAdapter()
        initNetwork(LocalDateTime.now().withDayOfMonth(1))

        return binding.root
    }

    @SuppressLint("ResourceType")
    private fun setDropDownMenu() {
        val years = arrayOf(2024, 2023, 2022, 2021)
        val yearSpinner = binding.spnYearMenu
        val spinnerAdapter: ArrayAdapter<Int>? = context?.let {
            ArrayAdapter(
                it,
                R.layout.support_simple_spinner_dropdown_item,
                years
            )
        }
        spinnerAdapter?.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        yearSpinner.adapter = spinnerAdapter
        yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                filteredYear = parent?.getItemAtPosition(position) as Int
                initNetwork(
                    LocalDateTime.now().withYear(filteredYear).withMonth(filteredMonth)
                        .withDayOfMonth(1)
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun setChipAdapter() {
        menuAdapter = MonthAdapter()
        binding.rvMonths.adapter = menuAdapter

        menuAdapter.setOnItemClickListener { _, pos ->
            filteredMonth = pos + 1
            initNetwork(
                LocalDateTime.now().withYear(filteredYear).withMonth(filteredMonth)
                    .withDayOfMonth(1)
            )
        }
    }

    private fun initNetwork(date: LocalDateTime) {
        if(DonutSharedPreferences.getUserRole() == "giver") {
            DonutSharedPreferences.getAccessToken()?.let { viewModel.requestGiverHistoryInfo(it, date) }
            setGiverAdapter()
        } else {
            DonutSharedPreferences.getAccessToken()?.let { viewModel.requestReceiverHistoryInfo(it) }
            setReceiverAdapter()
        }
    }

    private fun setGiverAdapter() {
        giverItemAdapter = GiverHistoryAdapter()
        binding.rvGiftItem.adapter = giverItemAdapter
        binding.rvGiftItem.layoutManager = GridLayoutManager(context, 2)

        giverItemAdapter.setOnItemClickListener { _, pos ->
            viewModel.setGiftId(giverItemAdapter.itemList[giverItemAdapter.mPosition].giftId)
            (activity as GiverMainActivity).changeFragment("history_detail")
        }
        setGiverDataList()
    }

    private fun setGiverDataList() {
        viewModel.giverHistoryInfo.observe(viewLifecycleOwner, Observer { data ->
            with(binding.rvGiftItem.adapter as GiverHistoryAdapter) {
                data.data!!.donationList?.let { giverItemAdapter.setGiftItemList(it) }
            }
        })
    }

    private fun setReceiverAdapter() {
        receiverItemAdapter = ReceiverHistoryAdapter()
        binding.rvGiftItem.adapter = receiverItemAdapter
        binding.rvGiftItem.layoutManager = GridLayoutManager(context, 2)

        receiverItemAdapter.setOnItemClickListener { _, pos ->
            viewModel.setGiftId(receiverItemAdapter.itemList[receiverItemAdapter.mPosition].giftId)
            (activity as ReceiverMainActivity).changeFragment("history_detail")
        }
        setReceiverDataList()
    }

    private fun setReceiverDataList() {
        viewModel.receiverHistoryInfo.observe(viewLifecycleOwner, Observer { data ->
            with(binding.rvGiftItem.adapter as ReceiverHistoryAdapter) {
                data.data!!.giftList?.let { receiverItemAdapter.setGiftItemList(it) }
            }
        })
    }
}