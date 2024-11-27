package org.gdgoc.donut.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        viewModel.sharedGiftId.observe(viewLifecycleOwner, Observer { data ->
            DonutSharedPreferences.getAccessToken()
                ?.let { viewModel.requestWalletDetailInfo(it, data) }
        })
    }

    private fun getReceiverHomeGiftInfo() {
        viewModel.walletDetailInfo.observe(viewLifecycleOwner, Observer { data ->
            val date = data.data!!.dueDate.substring(0, 10)
            binding.tvTitle.text = data.data.product
            binding.tvAmountNum.text = data.data.price.toString()
            binding.tvDueTitleNum.text = DonutUtil().setCalendarFormat(date)
            binding.tvDueNum.text = date
            binding.tvStoreText.text = data.data.store
            store = data.data.store
            setGoogleMapIcon()

            if (data.data.status == "USED") binding.tvStatusText.text = "can use"
            Glide.with(this)
                .load(data.data.imgUrl)
                .fitCenter()
                .into(binding.ivImage)
        })
    }

    private fun setReportButton() {
        binding.ivDots.setOnClickListener {
            if (binding.clReport.visibility == View.VISIBLE) binding.clReport.visibility =
                View.INVISIBLE
            else binding.clReport.visibility = View.VISIBLE
        }

        binding.clReport.setOnClickListener {
            viewModel.sharedGiftId.observe(viewLifecycleOwner, Observer { data ->
                DonutSharedPreferences.getAccessToken()
                    ?.let { reportViewModel.setCheatedItem(it, data) }
            })
        }
    }

    override fun onSendMsgButtonClicked() {
        val content = viewModel.sharedContent.value
        viewModel.sharedGiftId.observe(viewLifecycleOwner, Observer { data ->
            DonutSharedPreferences.getAccessToken()?.let {
                if (content != null) {
                    viewModel.requestSendMsg(it, data, content)
                }
            }
        })
    }

    private fun setUsedButton() {
        binding.btnUsed.setOnClickListener {
            if (DonutSharedPreferences.getUserRole() == "receiver") {
                context?.let { MessageDialog(it, this, viewModel).show() }
                onSendMsgButtonClicked()
                viewModel.sharedGiftId.observe(viewLifecycleOwner, Observer { data ->
                    DonutSharedPreferences.getAccessToken()
                        ?.let { reportViewModel.requestReportUsed(it, data) }
                })
            } else requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun setUnusedButton() {
        binding.btnUnused.setOnClickListener {
            viewModel.sharedGiftId.observe(viewLifecycleOwner, Observer { data ->
                DonutSharedPreferences.getAccessToken()
                    ?.let { reportViewModel.setUnusedItem(it, data) }
            })
        }
    }

    private fun setDonateButton() {
        if (DonutSharedPreferences.getUserRole() == "giver") {
            binding.btnDonate.setOnClickListener {
                viewModel.sharedGiftId.observe(viewLifecycleOwner, Observer { data ->
                    DonutSharedPreferences.getAccessToken()
                        ?.let { donationViewModel.requestDirectDonation(it, data) }
                })
            }
        } else {
            binding.btnDonate.visibility = View.INVISIBLE
        }
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