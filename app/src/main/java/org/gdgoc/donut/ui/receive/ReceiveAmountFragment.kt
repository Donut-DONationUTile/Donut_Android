package org.gdgoc.donut.ui.receive

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import org.gdgoc.donut.R
import org.gdgoc.donut.data.DonutSharedPreferences
import org.gdgoc.donut.databinding.FragmentReceiveAmountBinding
import org.gdgoc.donut.ui.ReceiverMainActivity
import org.gdgoc.donut.ui.viewModel.DonationViewModel

class ReceiveAmountFragment : Fragment() {
    private lateinit var binding: FragmentReceiveAmountBinding
    private val viewModel: DonationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReceiveAmountBinding.inflate(inflater, container, false)

        (activity as ReceiverMainActivity).disableFloatingButton()
        checkAmountStatus()

        return binding.root
    }

    private fun checkAmountStatus() {
        binding.etAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.clAmount.setBackgroundDrawable(context?.let {getDrawable(it, R.drawable.bg_white_maincoral_round8)})
            }

            override fun afterTextChanged(p0: Editable?) {
                if (binding.etAmount.text.isNullOrBlank()) {
                    binding.clAmount.setBackgroundDrawable(context?.let {getDrawable(it, R.drawable.bg_white_black_round8)})
                }
                setContinueButton()
            }
        })
    }

    @SuppressLint("ResourceAsColor")
    private fun setContinueButton() {
        binding.btnDone.setBackgroundDrawable(context?.let {getDrawable(it,R.drawable.bg_coral_round)})
        binding.tvDone.text = getString(R.string.receive_done)
        binding.tvDone.setTextColor(resources.getColor(R.color.white))
        binding.btnDone.setOnClickListener {
            sendReceiveInfo()
        }
    }

    private fun sendReceiveInfo() {
        DonutSharedPreferences.getAccessToken()?.let { viewModel.requestAssignReceiver(it, binding.etAmount.text.toString().toInt()) }

        viewModel.assignReceiverInfo.observe(viewLifecycleOwner, Observer{ data ->
            if(data.code == 201){
                requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
                startActivity(Intent(context, ReceiveDoneActivity::class.java))
            } else {
                Toast.makeText(context, "신청이 승인되지 않았습니다.", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.showErrorToast.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let{
                Toast.makeText(context, "신청이 승인되지 않았습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}