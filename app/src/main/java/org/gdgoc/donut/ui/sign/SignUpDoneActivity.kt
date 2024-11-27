package org.gdgoc.donut.ui.sign

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import org.gdgoc.donut.databinding.ActivitySignUpDoneBinding

class SignUpDoneActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpDoneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpDoneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        moveSignIn(1)
    }

    private fun moveSignIn(sec: Int) {
        Handler().postDelayed(Runnable {
            finish()
        }, 1000 * sec.toLong()) // sec초 정도 딜레이를 준 후 시작
    }
}