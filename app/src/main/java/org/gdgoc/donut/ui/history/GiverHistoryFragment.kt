package org.gdgoc.donut.ui.history

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import org.gdgoc.donut.R
import org.gdgoc.donut.data.DonutSharedPreferences
import org.gdgoc.donut.data.remote.response.history.ResponseHistoryGiverData
import org.gdgoc.donut.data.remote.response.history.ResponseHistoryGiverDonationList
import org.gdgoc.donut.databinding.FragmentGiverHistoryBinding
import org.gdgoc.donut.ui.GiverMainActivity
import org.gdgoc.donut.ui.history.adapter.GiverHistoryAdapter
import org.gdgoc.donut.ui.history.adapter.MonthAdapter
import org.gdgoc.donut.ui.viewModel.HistoryViewModel
import org.gdgoc.donut.util.NetworkState
import java.time.LocalDate
import java.time.LocalDateTime

class GiverHistoryFragment : Fragment() {
    private lateinit var binding: FragmentGiverHistoryBinding
    private lateinit var menuAdapter: MonthAdapter
    private lateinit var itemAdapter: GiverHistoryAdapter
    private val viewModel: HistoryViewModel by activityViewModels()
    private var filteredYear: Int = LocalDate.now().year
    private var filteredMonth: Int = LocalDate.now().monthValue

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGiverHistoryBinding.inflate(inflater, container, false)

        setDropDownMenu()
        setChipAdapter()
        initNetwork(LocalDateTime.now().withDayOfMonth(1))
        getGiverHistoryInfo()
        setAdapter()

        return binding.root
    }

    private fun initNetwork(date: LocalDateTime) {
        DonutSharedPreferences.getAccessToken()?.let { token ->
            viewModel.requestGiverHistoryInfo(token, date)
        }
    }

    @SuppressLint("ResourceType")
    private fun setDropDownMenu() {
        val years = arrayOf(2024, 2023, 2022, 2021)
        val yearSpinner = binding.spnYearMenu
        val spinnerAdapter: ArrayAdapter<Int>? = context?.let {
            ArrayAdapter(
                it,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                years
            )
        }
        spinnerAdapter?.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        yearSpinner.adapter = spinnerAdapter
        yearSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filteredYear = parent?.getItemAtPosition(position) as Int
                initNetwork(LocalDateTime.now().withYear(filteredYear).withMonth(filteredMonth).withDayOfMonth(1))
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

    private fun getGiverHistoryInfo() {
        viewModel.giverHistoryInfo.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkState.Loading -> {}
                is NetworkState.Success -> {
                    val data = state.data.data
                    if (data != null) updateHistoryInfoUI(data)
                    if (data != null) data.donationList?.let { updateGiftItemList(it) }
                }
                is NetworkState.Error -> {
                    Toast.makeText(context, "서버 오류입니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateHistoryInfoUI(data: ResponseHistoryGiverData) {
        binding.tvDollarNum.text = data.totalAmount.toString()
        binding.tvUnreceivedNum.text = data.unreceived.toString()
        binding.tvReceivedNum.text = data.received.toString()
        binding.tvMsgNum.text = data.msg.toString()

        if (data.period >= 2) {
            binding.tvTitleYearNum.text = data.period.toString()
            binding.tvTitleYear.text = getString(R.string.giverHistory_years)
        } else {
            binding.tvTitleYearNum.text = "a"
        }
    }

    private fun updateGiftItemList(donationList: List<ResponseHistoryGiverDonationList>) {
        donationList.let {
            itemAdapter.setGiftItemList(it)
        }
    }

    private fun setAdapter() {
        itemAdapter = GiverHistoryAdapter()
        binding.rvGiftItem.adapter = itemAdapter
        binding.rvGiftItem.layoutManager = GridLayoutManager(context, 2)

        itemAdapter.setOnItemClickListener { _, _ ->
            val selectedGiftId = itemAdapter.itemList[itemAdapter.mPosition].giftId
            viewModel.setGiftId(selectedGiftId)
            (activity as GiverMainActivity).changeFragment("history_detail")
        }
    }
}