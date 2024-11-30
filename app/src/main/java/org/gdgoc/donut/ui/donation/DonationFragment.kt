package org.gdgoc.donut.ui.donation

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.gdgoc.donut.R
import org.gdgoc.donut.data.DonutSharedPreferences
import org.gdgoc.donut.databinding.FragmentDonationBinding
import org.gdgoc.donut.ui.GiverMainActivity
import org.gdgoc.donut.ui.viewModel.DonationViewModel
import org.gdgoc.donut.util.DonutUtil
import java.io.File
import java.io.IOException

class DonationFragment : Fragment() {
    private lateinit var binding: FragmentDonationBinding
    private val viewModel: DonationViewModel by activityViewModels()

    private var highResolution = false
    private var store = ""
    private var img = ""
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                setImage(uri)
                processImageWithOCR(uri)
            }
        }
    private val recognizer = TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())
    private var recognizedWords = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDonationBinding.inflate(inflater, container, false)

        (activity as GiverMainActivity).disableFloatingButton()
        setStatus()
        checkNameStatus()
        checkAmountStatus()
        checkDueDateStatus()
        setStoreList()
        setSwitch()
        setUploadButton()
        setDonateButton()

        return binding.root
    }

    private fun setStatus(){
        if (viewModel.sharedDirectDonationOption.value == false) {
            binding.tvTitle.text = "Add gifticon to\nwallet!"
            binding.tvDonate.text = "Add to wallet"
        }
    }

    private fun checkNameStatus() {
        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            @SuppressLint("UseCompatLoadingForDrawables")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.etName.background.setTint(resources.getColor(R.color.main_coral))
                binding.etName.setTextColor(resources.getColor(R.color.black_100))
            }

            override fun afterTextChanged(p0: Editable?) {
                if (binding.etName.text.isNullOrBlank()) {
                    binding.etName.background.setTint(resources.getColor(R.color.main_coral))
                    binding.etName.setTextColor(resources.getColor(R.color.gray_300))
                }
                setDonateButton()
            }
        })
    }

    private fun checkAmountStatus() {
        binding.etAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            @SuppressLint("UseCompatLoadingForDrawables")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.etAmount.background.setTint(resources.getColor(R.color.main_coral))
                binding.etAmount.setTextColor(resources.getColor(R.color.black_100))
            }

            override fun afterTextChanged(p0: Editable?) {
                if (binding.etAmount.text.isNullOrBlank()) {
                    binding.etAmount.background.setTint(resources.getColor(R.color.main_coral))
                    binding.etAmount.setTextColor(resources.getColor(R.color.gray_300))
                }
                setDonateButton()
            }
        })
    }

    private fun checkDueDateStatus() {
        binding.etDue.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            @SuppressLint("UseCompatLoadingForDrawables")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.etDue.background.setTint(resources.getColor(R.color.main_coral))
                binding.etDue.setTextColor(resources.getColor(R.color.black_100))
            }

            override fun afterTextChanged(p0: Editable?) {
                if (binding.etDue.text.isNullOrBlank()) {
                    binding.etDue.background.setTint(resources.getColor(R.color.main_coral))
                    binding.etDue.setTextColor(resources.getColor(R.color.gray_300))
                }
                setDonateButton()
            }
        })
    }


    private fun setStoreList() {
        binding.clStore.setOnClickListener {
            binding.clStore.visibility = View.GONE
            binding.clStoreList.visibility = View.VISIBLE
            binding.tvCu.visibility = View.VISIBLE
            binding.tvGs25.visibility = View.VISIBLE
        }
        setStoreClickListener()
    }

    private fun setStoreClickListener() {
        binding.tv7eleven.setOnClickListener {
            binding.tv7eleven.setTextColor(resources.getColor(R.color.main_coral))
            binding.tvCu.setTextColor(resources.getColor(R.color.black_100))
            binding.tvGs25.setTextColor(resources.getColor(R.color.black_100))
            store = "SEVENELEVEN"
            setDonateButton()
        }

        binding.tvCu.setOnClickListener {
            binding.tv7eleven.setTextColor(resources.getColor(R.color.black_100))
            binding.tvCu.setTextColor(resources.getColor(R.color.main_coral))
            binding.tvGs25.setTextColor(resources.getColor(R.color.black_100))
            store = "CU"
            setDonateButton()
        }

        binding.tvGs25.setOnClickListener {
            binding.tv7eleven.setTextColor(resources.getColor(R.color.black_100))
            binding.tvCu.setTextColor(resources.getColor(R.color.black_100))
            binding.tvGs25.setTextColor(resources.getColor(R.color.main_coral))
            store = "GS25"
            setDonateButton()
        }
    }

    private fun setUploadButton() {
        binding.btnUpload.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun uriToFile(uri: Uri): File? {
        val cursor: Cursor? = context?.contentResolver?.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val filePathColumnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                val filePath = it.getString(filePathColumnIndex)
                return File(filePath)
            }
        }
        return null
    }

    private fun setImage(uri: Uri) {
        Glide.with(this)
            .load(uri)
            .centerCrop()
            .into(binding.ivGifticon)
        binding.ivGifticon.visibility = View.VISIBLE
        img = uriToFile(uri)?.path ?: ""

        binding.btnComplete.visibility = View.VISIBLE
        binding.btnUpload.visibility = View.INVISIBLE

        setDonateButton()
    }

    private fun processImageWithOCR(uri: Uri){
        val image: InputImage
        try {
            image = InputImage.fromFilePath(requireContext(), uri)
            val result = recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    for(block in visionText.textBlocks){
                        for(line in block.lines){
                            recognizedWords.add(line.text)
                        }
                    }
                    getGifticonInfoFromImage()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "그림자가 생기지 않도록 다시 촬영해주세요.", Toast.LENGTH_SHORT).show()
                }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun getGifticonInfoFromImage(){
        val dueDate = DonutUtil().formatDate(recognizedWords[recognizedWords.size-3])

        binding.clFillOut.visibility = View.VISIBLE
        binding.etName.setText(recognizedWords[1])
        binding.etDue.setText(dueDate)
        setStoreAutoFill(recognizedWords[0])
    }

    private fun setStoreAutoFill(recognizedStore: String){
        binding.clStore.visibility = View.GONE
        binding.clStoreList.visibility = View.VISIBLE
        binding.tvCu.visibility = View.VISIBLE
        binding.tvGs25.visibility = View.VISIBLE

        when (recognizedStore) {
            "세븐일레븐" -> {
                binding.tv7eleven.setTextColor(resources.getColor(R.color.main_coral))
                binding.tvCu.setTextColor(resources.getColor(R.color.black_100))
                binding.tvGs25.setTextColor(resources.getColor(R.color.black_100))
                store = "SEVENELEVEN"
                setDonateButton()
            }
            "CU" -> {
                binding.tv7eleven.setTextColor(resources.getColor(R.color.black_100))
                binding.tvCu.setTextColor(resources.getColor(R.color.main_coral))
                binding.tvGs25.setTextColor(resources.getColor(R.color.black_100))
                store = "CU"
                setDonateButton()
            }
            "GS25" -> {
                binding.tv7eleven.setTextColor(resources.getColor(R.color.black_100))
                binding.tvCu.setTextColor(resources.getColor(R.color.black_100))
                binding.tvGs25.setTextColor(resources.getColor(R.color.main_coral))
                store = "GS25"
                setDonateButton()
            }
        }
    }

    private fun setSwitch(){
        binding.swResolution.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                highResolution = true
                binding.tvResolution.setTextColor(resources.getColor(R.color.main_coral))
            } else{
                highResolution = false
                binding.tvResolution.setTextColor(resources.getColor(R.color.gray_300))
            }
        }
    }

    private fun setDonateButton() {
        if (!binding.etName.text.isNullOrBlank() && !binding.etAmount.text.isNullOrBlank() && !binding.etDue.text.isNullOrBlank() && store.isNotBlank() && img.isNotBlank()) {
            if(store.isNotBlank() && img.isNotBlank()){
                binding.btnDonate.setBackgroundDrawable(context?.let { AppCompatResources.getDrawable(it, R.drawable.bg_coral_round) })
                binding.tvDonate.setTextColor(resources.getColor(R.color.white))
                binding.btnDonate.setOnClickListener {
                    if (viewModel.sharedDirectDonationOption.value == true) {
                        sendDonationInfo()
                    } else nextScreenWithDonationInfo()
                }
            }
        }
    }

    private fun sendDonationInfo() {
        val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), File(img))
        val giftImage = MultipartBody.Part.createFormData("giftImage", File(img).name, requestFile)
        val product = binding.etName.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val price = binding.etAmount.text.toString().toInt()
        val dueDate = (binding.etDue.text.toString()+"T00:00:00.000000").toRequestBody("text/plain".toMediaTypeOrNull())
        val store = store.toRequestBody("text/plain".toMediaTypeOrNull())
        val isRestored = highResolution.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        DonutSharedPreferences.getAccessToken()?.let { viewModel.requestDonateGiver(it, giftImage, product, price, dueDate, store, isRestored) }
        requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        startActivity(Intent(context, DonationDoneActivity::class.java))
    }

    private fun nextScreenWithDonationInfo() {
        val imgString = img
        val product = binding.etName.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val price = binding.etAmount.text.toString().toInt()
        val store = store.toRequestBody("text/plain".toMediaTypeOrNull())
        val dueDate = (binding.etDue.text.toString()+"T00:00:00.000000").toRequestBody("text/plain".toMediaTypeOrNull())

        viewModel.setGifticonInfo(imgString, product, price, dueDate, store)
        requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
        (activity as GiverMainActivity).changeFragment("donation_check")
    }
}