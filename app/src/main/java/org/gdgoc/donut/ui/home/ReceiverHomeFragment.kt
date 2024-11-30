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
import org.gdgoc.donut.data.remote.response.home.ResponseHomeReceiver
import org.gdgoc.donut.data.remote.response.home.ResponseHomeReceiverBox
import org.gdgoc.donut.data.remote.response.home.ResponseHomeReceiverData
import org.gdgoc.donut.databinding.FragmentReceiverHomeBinding
import org.gdgoc.donut.ui.ReceiverMainActivity
import org.gdgoc.donut.ui.home.adpater.PackageItemAdapter
import org.gdgoc.donut.ui.viewModel.HomeViewModel
import org.gdgoc.donut.util.NetworkState

class ReceiverHomeFragment : Fragment() {
    private lateinit var binding: FragmentReceiverHomeBinding
    private lateinit var itemAdapter: PackageItemAdapter
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReceiverHomeBinding.inflate(inflater, container, false)

        initNetwork()
        getReceiverHomeInfo()
        setAdapter()

        return binding.root
    }

    private fun initNetwork(){
        DonutSharedPreferences.getAccessToken()?.let { viewModel.requestReceiverHomeInfo(it) }
    }

    private fun getReceiverHomeInfo() {
        viewModel.receiverHomeInfo.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkState.Loading -> {}
                is NetworkState.Success -> {
                    val data = state.data.data
                    if (data != null)  updateReceiverHomeUI(data)
                    updatePackageItemList(data?.boxList)
                }
                is NetworkState.Error -> {
                    Toast.makeText(context, "서버 오류입니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateReceiverHomeUI(data: ResponseHomeReceiverData) {
        if (data.availability) {
            binding.tvTitle.visibility = View.VISIBLE
            binding.imageView4.visibility = View.VISIBLE
            binding.imageView5.visibility = View.INVISIBLE
        } else {
            binding.tvTitle.visibility = View.GONE
            binding.imageView5.visibility = View.VISIBLE
            binding.imageView4.visibility = View.INVISIBLE
        }

        binding.tvDollarNum.text = data.amount.toString()
        binding.tv7Num.text = data.sevenEleven.toString()
        binding.tvCuNum.text = data.cu.toString()
        binding.tvGsNum.text = data.gs25.toString()
    }

    private fun updatePackageItemList(boxList: List<ResponseHomeReceiverBox>?) {
        boxList?.let {
            itemAdapter.setBoxItemList(it)
        }
    }

    private fun setAdapter() {
        itemAdapter = PackageItemAdapter()
        binding.rvPackageItem.adapter = itemAdapter
        binding.rvPackageItem.layoutManager = GridLayoutManager(context, 2)

        itemAdapter.setOnItemClickListener { _, _ ->
            val selectedBoxId = itemAdapter.itemList[itemAdapter.mPosition].boxId
            viewModel.setBoxId(selectedBoxId)
            (activity as ReceiverMainActivity).changeFragment("package_detail")
        }
    }
}