package org.gdgoc.donut.ui.donation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.gdgoc.donut.data.DonutSharedPreferences
import org.gdgoc.donut.databinding.FragmentDonationCheckBinding
import org.gdgoc.donut.ui.GiverMainActivity
import org.gdgoc.donut.ui.viewModel.DonationViewModel
import java.io.File

class DonationCheckFragment : Fragment() {
    private lateinit var binding: FragmentDonationCheckBinding
    private val viewModel: DonationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDonationCheckBinding.inflate(inflater, container, false)

        (activity as GiverMainActivity).disableFloatingButton()
        setCheckBox()

        return binding.root
    }

    private fun setCheckBox() {

        binding.cbAgree.setOnClickListener {
            requestAddToWallet(true)
        }
        binding.cbDisagree.setOnClickListener {
            requestAddToWallet(false)
        }

    }

    private fun requestAddToWallet(agree: Boolean) {
        val img = viewModel.sharedGiftImageString.value
        val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), File(img))
        val giftImage = MultipartBody.Part.createFormData("giftImage", File(img).name, requestFile)
        val product = viewModel.sharedProduct.value
        val price = viewModel.sharedPrice.value
        val dueDate = viewModel.sharedDueDate.value
        val store = viewModel.sharedStore.value

        DonutSharedPreferences.getAccessToken()?.let {
            if (product != null && price != null && dueDate != null && store != null) {
                viewModel.requestAddToWallet(it, giftImage, product, price, dueDate, store, agree)
            }
        }

        requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        startActivity(Intent(context, DonationDoneActivity::class.java))
    }
}