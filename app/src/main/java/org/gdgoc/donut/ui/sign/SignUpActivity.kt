package org.gdgoc.donut.ui.sign

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import org.gdgoc.donut.R
import org.gdgoc.donut.data.DonutSharedPreferences
import org.gdgoc.donut.databinding.ActivitySignUpBinding
import org.gdgoc.donut.ui.GiverMainActivity
import org.gdgoc.donut.ui.viewModel.NetworkState
import org.gdgoc.donut.ui.viewModel.SignViewModel

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private val viewModel: SignViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkNameStatus()
        checkPasswordStatus()
        checkConfirmStatus()
    }

    private fun checkNameStatus() {
        binding.etUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.clUsername.setBackgroundDrawable(getDrawable(R.drawable.bg_white_maincoral_round8))
                binding.etUsername.setTextColor(getColor(R.color.black_100))
            }

            override fun afterTextChanged(p0: Editable?) {
                if (binding.etUsername.text.isNullOrBlank()) {
                    binding.clUsername.setBackgroundDrawable(getDrawable(R.drawable.bg_white_black_round8))
                    binding.etUsername.setTextColor(getColor(R.color.gray_300))
                }
                setCreateBtn()
            }
        })
    }

    private fun checkPasswordStatus() {
        binding.etPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.clPassword.setBackgroundDrawable(getDrawable(R.drawable.bg_white_maincoral_round8))
                binding.etPassword.setTextColor(getColor(R.color.black_100))
            }

            override fun afterTextChanged(p0: Editable?) {
                if (binding.etPassword.text.isNullOrBlank()) {
                    binding.clPassword.setBackgroundDrawable(getDrawable(R.drawable.bg_white_black_round8))
                    binding.etPassword.setTextColor(getColor(R.color.gray_300))
                }
                setCreateBtn()
            }
        })
    }

    private fun checkConfirmStatus() {
        binding.etConfirm.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                binding.clPasswordCheck.setBackgroundDrawable(getDrawable(R.drawable.bg_white_maincoral_round8))
                binding.etConfirm.setTextColor(getColor(R.color.black_100))
            }

            override fun afterTextChanged(p0: Editable?) {
                if (binding.etConfirm.text.isNullOrBlank()) {
                    binding.clPasswordCheck.setBackgroundDrawable(getDrawable(R.drawable.bg_white_black_round8))
                    binding.etConfirm.setTextColor(getColor(R.color.gray_300))
                }
                setCreateBtn()
            }
        })
    }

    private fun setCreateBtn() {
        if (!binding.etUsername.text.isNullOrBlank() && !binding.etPassword.text.isNullOrBlank() && !binding.etConfirm.text.isNullOrBlank()) {
            if(binding.etPassword.text.toString() == binding.etConfirm.text.toString()){
                binding.btnCreate.setBackgroundDrawable(getDrawable(R.drawable.bg_coral_round))
                binding.tvContinue.setTextColor(getColor(R.color.white))
                binding.btnCreate.setOnClickListener {
                    requestSignUp()
                }
            }else {
                binding.btnCreate.setBackgroundDrawable(getDrawable(R.drawable.bg_gray200_round))
                binding.tvContinue.setTextColor(getColor(R.color.gray_300))
            }
        }
    }

    private fun requestSignUp(){
        val id = binding.etUsername.text.toString()
        val password = binding.etPassword.text.toString()
        viewModel.requestReceiverSignUp(id, password)
        setReceiverUserInfo()
    }

    private fun setReceiverUserInfo() {
        viewModel.receiverSignUpInfo.observe(this, Observer { state ->
            when (state) {
                is NetworkState.Loading -> {
                    Toast.makeText(this, "잠시만 기다려주세요.", Toast.LENGTH_SHORT).show()
                }
                is NetworkState.Success -> {
                    startActivity(Intent(this, SignUpDoneActivity::class.java))
                    finish()
                }
                is NetworkState.Error -> {
                    binding.ivCancel.visibility = View.VISIBLE
                    binding.tvCheck.visibility = View.VISIBLE
                    Toast.makeText(this, "서버 오류입니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

}