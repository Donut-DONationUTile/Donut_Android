package org.gdgoc.donut.ui.sign

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import org.gdgoc.donut.databinding.ActivitySignUpConfirmBinding
import java.io.IOException

class SignUpConfirmActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpConfirmBinding
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                setImage(uri)
                processImageWithOCR(uri)
            }
        }
    private val recognizer = TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpConfirmBinding.inflate(layoutInflater)

        setUploadButton()

        setContentView(binding.root)
    }

    override fun onResume() {
        runPhotoPicker()
        super.onResume()
    }

    override fun onRestart() {
        runPhotoPicker()
        super.onRestart()
    }

    private fun setUploadButton() {
        binding.btnUpload.setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
        }
    }

    private fun runPhotoPicker(){
        if(imageCaptured){
            if(binding.ivCard.visibility != View.VISIBLE){
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        }
    }

    private fun setImage(uri: Uri) {
        Glide.with(this)
            .load(uri)
            .centerCrop()
            .into(binding.ivCard)
        binding.ivCard.visibility = View.VISIBLE
    }

    private fun processImageWithOCR(uri: Uri){
        val image: InputImage
        try {
            image = InputImage.fromFilePath(this, uri)
            val result = recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    Log.d("visionText", visionText.text)
                    verifyUserStatusFromOCRText(visionText.text)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "그림자가 생기지 않도록 다시 촬영해주세요.", Toast.LENGTH_SHORT).show()
                    binding.ivCard.visibility = View.INVISIBLE
                }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun verifyUserStatusFromOCRText(text: String){
        if(text.contains("가상계좌") && text.contains("NH")){
            setContinueBtn()
        } else {
            Toast.makeText(this, "인증에 실패하였습니다. 다시 촬영해주세요.", Toast.LENGTH_SHORT).show()
            binding.ivCard.visibility = View.INVISIBLE
        }
    }

    private fun setContinueBtn() {
        binding.btnContinue.visibility = View.VISIBLE
        binding.btnContinue.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
    }

    companion object{
        var imageCaptured = false
    }

}