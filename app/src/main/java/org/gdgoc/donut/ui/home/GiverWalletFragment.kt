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
import org.gdgoc.donut.data.remote.response.home.ResponseWalletGiftList
import org.gdgoc.donut.data.remote.response.home.ResponseWalletGiver
import org.gdgoc.donut.data.remote.response.home.ResponseWalletGiverData
import org.gdgoc.donut.data.remote.response.home.ResponseWalletImpendingList
import org.gdgoc.donut.databinding.FragmentGiverWalletBinding
import org.gdgoc.donut.ui.GiverMainActivity
import org.gdgoc.donut.ui.home.adpater.WalletGiftItemAdapter
import org.gdgoc.donut.ui.home.adpater.WalletImpedingItemAdapter
import org.gdgoc.donut.ui.viewModel.DonationViewModel
import org.gdgoc.donut.ui.viewModel.HomeViewModel
import org.gdgoc.donut.util.NetworkState


class GiverWalletFragment : Fragment() {
    private lateinit var binding: FragmentGiverWalletBinding
    private lateinit var giftItemAdapter: WalletGiftItemAdapter
    private lateinit var impendingItemAdapter: WalletImpedingItemAdapter
    private val viewModel: HomeViewModel by activityViewModels()
    private val donationViewModel: DonationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGiverWalletBinding.inflate(inflater, container, false)

        initNetwork()
        getGiverWalletInfo()
        setAdapter()

        return binding.root
    }

    private fun initNetwork() {
        DonutSharedPreferences.getAccessToken()?.let { viewModel.requestGiverWalletInfo(it) }
    }

    private fun getGiverWalletInfo() {
        viewModel.giverWalletInfo.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkState.Loading -> {}
                is NetworkState.Success -> {
                    state.data.data?.let { updateWalletInfo(it) }
                    updateGiftItemList(state.data.data?.giftList)
                    updateImpendingItemList(state.data.data?.impendingList)
                }
                is NetworkState.Error -> {
                    Toast.makeText(context, "서버 오류입니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateWalletInfo(data: ResponseWalletGiverData) {
        binding.tvChildrenNum.text = data.receiver.toString()
        binding.tvDollarNum.text = data.amount.toInt().toString()
        binding.tv7Num.text = data.seveneleven.toString()
        binding.tvCuNum.text = data.cu.toString()
        binding.tvGsNum.text = data.gs25.toString()
    }

    private fun updateGiftItemList(giftList: List<ResponseWalletGiftList>?) {
        giftList?.let {
            giftItemAdapter.setGiftItemList(it)
        }
    }

    private fun updateImpendingItemList(impendingList: List<ResponseWalletImpendingList>?) {
        if (!impendingList.isNullOrEmpty()) {
            impendingItemAdapter.setImpendingItemList(impendingList)
            binding.tvDonateTitle.visibility = View.VISIBLE
        } else {
            binding.tvDonateTitle.visibility = View.GONE
        }
    }

    private fun setAdapter() {
        impendingItemAdapter = WalletImpedingItemAdapter(donationViewModel)
        binding.rvImminentGiftItem.adapter = impendingItemAdapter
        giftItemAdapter = WalletGiftItemAdapter()
        binding.rvGiftItem.adapter = giftItemAdapter
        binding.rvGiftItem.layoutManager = GridLayoutManager(context, 2)

        giftItemAdapter.setOnItemClickListener { _, pos ->
            viewModel.setGiftId(giftItemAdapter.itemList[giftItemAdapter.mPosition].giftId)
            (activity as GiverMainActivity).changeFragment("wallet_detail")
        }
    }
}