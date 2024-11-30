package org.gdgoc.donut.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import org.gdgoc.donut.data.DonutSharedPreferences
import org.gdgoc.donut.databinding.FragmentGiverHomeBinding
import org.gdgoc.donut.ui.viewModel.HomeViewModel

class GiverHomeFragment : Fragment() {
    private lateinit var binding: FragmentGiverHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    private var donatedAmount = 0.0
    private var requiredAmount = 0.0

    override fun onStart() {
        super.onStart()

        DonutSharedPreferences.getAccessToken()?.let { viewModel.requestGiverHomeInfo(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGiverHomeBinding.inflate(inflater, container, false)

        setProgressBar()
        setSpeechBubble()

        return binding.root
    }


    private fun setProgressBar() {
        viewModel.giverHomeInfo.observe(viewLifecycleOwner, Observer { data ->
            //donatedAmount = data.data!!.donated
            //requiredAmount = data.data.need
            donatedAmount = 30.0
            requiredAmount = 100.0
            binding.tvChildrenNum.text = data.data!!.receivers.toString()

            val percentage = (donatedAmount / requiredAmount * 100)
            binding.progressBar.progress = percentage.toInt()

            if (percentage >= 0 && percentage < 20) {
                setProgressBarPointer(300)
            } else if (percentage >= 20 && percentage < 40) {
                setProgressBarPointer(340)
            } else if (percentage >= 40 && percentage < 80) {
                setProgressBarPointer(350)
            } else if (percentage >= 80) {
                setProgressBarPointer(355)
            }
        })
    }

    private fun setProgressBarPointer(ratio: Int) {
        val percentage = (donatedAmount / requiredAmount * 100)
        val percentage360 = percentage / 100 * ratio
        binding.tvPercentNum.translationX = percentage360.toFloat()
        binding.tvPercent.translationX = percentage360.toFloat()
        binding.ivPointer.translationX = percentage360.toFloat()

        binding.tvPercentNum.text = percentage.toInt().toString()
        Log.d("info", percentage.toInt().toString() + "퍼센트")
    }

    private fun setSpeechBubble() {
        viewModel.giverHomeInfo.observe(viewLifecycleOwner, Observer { data ->
            binding.tvDonatedNum.text = data.data!!.donated.toInt().toString()
            binding.tvNeedNum.text = data.data.need.toInt().toString()
        })

        binding.ivDonut.setOnClickListener {
            if (binding.clSpeechBubble.visibility == View.INVISIBLE) binding.clSpeechBubble.visibility =
                View.VISIBLE
            else binding.clSpeechBubble.visibility = View.INVISIBLE
        }
    }

    companion object {
    }
}