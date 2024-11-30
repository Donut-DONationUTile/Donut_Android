package org.gdgoc.donut.ui.mypage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import org.gdgoc.donut.data.DonutSharedPreferences
import org.gdgoc.donut.databinding.FragmentReceiverMyPageBinding
import org.gdgoc.donut.ui.ReceiverMainActivity
import org.gdgoc.donut.ui.sign.SignActivity
import org.gdgoc.donut.ui.viewModel.MyPageViewModel

class ReceiverMyPageFragment : Fragment() {
    private lateinit var binding: FragmentReceiverMyPageBinding
    private val viewModel: MyPageViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReceiverMyPageBinding.inflate(inflater, container, false)

        initNetwork()
        setInfo()
        setClickListener()

        return binding.root
    }

    private fun initNetwork(){
        DonutSharedPreferences.getAccessToken()?.let { viewModel.requestReceiverMyPageInfo(it) }
    }

    private fun setInfo(){
        viewModel.receiverMyPageInfo.observe(viewLifecycleOwner, Observer { data ->
            binding.tvDollarNum.text = data.data!!.total.toInt().toString()
        })
    }

    private fun setClickListener(){
        binding.ivDonut.setOnClickListener {
            DonutSharedPreferences.setAccessToken("")
            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
            startActivity(Intent(context, SignActivity::class.java))
        }

        binding.clHistory.setOnClickListener {
            (activity as ReceiverMainActivity).changeFragment("History")
        }
        binding.clService.setOnClickListener {
            Toast.makeText(activity, "준비중입니다.", Toast.LENGTH_SHORT).show()
        }
        binding.clOpenSource.setOnClickListener {
            Toast.makeText(activity, "준비중입니다.", Toast.LENGTH_SHORT).show()
        }
        binding.clAbout.setOnClickListener {
            Toast.makeText(activity, "준비중입니다.", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
    }
}