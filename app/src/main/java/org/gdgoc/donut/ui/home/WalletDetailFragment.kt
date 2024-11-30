package org.gdgoc.donut.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import org.gdgoc.donut.data.DonutSharedPreferences
import org.gdgoc.donut.databinding.FragmentWalletDetailBinding
import org.gdgoc.donut.ui.viewModel.DonationViewModel
import org.gdgoc.donut.ui.viewModel.HomeViewModel
import org.gdgoc.donut.ui.viewModel.ReportViewModel
import org.gdgoc.donut.util.DonutUtil
import org.gdgoc.donut.util.NetworkState

class WalletDetailFragment : Fragment(), MessageDialogInterface {
    private lateinit var binding: FragmentWalletDetailBinding
    private val viewModel: HomeViewModel by activityViewModels()
    private val reportViewModel: ReportViewModel by activityViewModels()
    private val donationViewModel: DonationViewModel by activityViewModels()

    var store = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWalletDetailBinding.inflate(inflater, container, false)

        initNetwork()
        getReceiverHomeGiftInfo()
        setReportButton()
        setUsedButton()
        setUnusedButton()
        setDonateButton()

        return binding.root
    }

    private fun initNetwork() {
        viewModel.sharedGiftId.observe(viewLifecycleOwner) { giftId ->
            giftId?.let {
                DonutSharedPreferences.getAccessToken()?.let { token ->
                    viewModel.requestWalletDetailInfo(token, it)
                }
            }
        }
    }

    private fun getReceiverHomeGiftInfo() {
        viewModel.walletDetailInfo.observe(viewLifecycleOwner) { state ->
            when (state) {
                is NetworkState.Loading -> {}
                is NetworkState.Success -> {
                    val data = state.data.data
                    val date = data?.dueDate?.substring(0, 10)
                    binding.tvTitle.text = data?.product
                    binding.tvAmountNum.text = data?.price.toString()
                    binding.tvDueTitleNum.text = date?.let { DonutUtil().setCalendarFormat(it) }
                    binding.tvDueNum.text = date
                    binding.tvStoreText.text = data?.store
                    store = data?.store.toString()
                    setGoogleMapIcon()

                    binding.tvStatusText.text = if (data?.status == "USED") "can use" else "unused"
                    Glide.with(this)
                        .load(data?.imgUrl)
                        .fitCenter()
                        .into(binding.ivImage)
                }
                is NetworkState.Error -> {
                    Toast.makeText(context, "서버 오류입니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setReportButton() {
        binding.ivDots.setOnClickListener {
            toggleVisibility(binding.clReport)
        }

        binding.clReport.setOnClickListener {
            viewModel.sharedGiftId.value?.let { giftId ->
                DonutSharedPreferences.getAccessToken()?.let { token ->
                    reportViewModel.setCheatedItem(token, giftId)
                }
            }
        }
    }

    override fun onSendMsgButtonClicked() {
        val content = viewModel.sharedContent.value
        val giftId = viewModel.sharedGiftId.value
        if (content != null && giftId != null) {
            DonutSharedPreferences.getAccessToken()?.let { token ->
                viewModel.requestSendMsg(token, giftId, content)
            }
        }
    }

    private fun setUsedButton() {
        binding.btnUsed.setOnClickListener {
            if (DonutSharedPreferences.getUserRole() == "receiver") {
                context?.let { MessageDialog(it, this, viewModel).show() }
                onSendMsgButtonClicked()
                viewModel.sharedGiftId.value?.let { giftId ->
                    DonutSharedPreferences.getAccessToken()?.let { token ->
                        reportViewModel.requestReportUsed(token, giftId)
                    }
                }
            } else {
                requireActivity().supportFragmentManager.popBackStack()
            }
        }
    }

    private fun setUnusedButton() {
        binding.btnUnused.setOnClickListener {
            viewModel.sharedGiftId.value?.let { giftId ->
                DonutSharedPreferences.getAccessToken()?.let { token ->
                    reportViewModel.setUnusedItem(token, giftId)
                }
            }
        }
    }

    private fun setDonateButton() {
        if (DonutSharedPreferences.getUserRole() == "giver") {
            binding.btnDonate.setOnClickListener {
                viewModel.sharedGiftId.value?.let { giftId ->
                    DonutSharedPreferences.getAccessToken()?.let { token ->
                        donationViewModel.requestDirectDonation(token, giftId)
                    }
                }
            }
        } else {
            binding.btnDonate.visibility = View.INVISIBLE
        }
    }

    private fun toggleVisibility(view: View) {
        view.visibility = if (view.visibility == View.VISIBLE) View.INVISIBLE else View.VISIBLE
    }


    private fun setGoogleMapIcon() {
        val gmmIntentUri = Uri.parse("geo:0,0?q=${store}")
        binding.ivPin.setOnClickListener {
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        }
    }
}