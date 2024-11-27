package org.gdgoc.donut.ui.donation

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import org.gdgoc.donut.databinding.ActivityDonationDoneBinding
import org.gdgoc.donut.ui.GiverMainActivity

class DonationDoneActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDonationDoneBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonationDoneBinding.inflate(layoutInflater)
        setContentView(binding.root)

        moveMain(2)
    }

    private fun moveMain(sec: Int) {
        Handler().postDelayed(Runnable {
            startActivity(Intent(this, GiverMainActivity::class.java))
            finish()
        }, 1000 * sec.toLong()) // sec초 정도 딜레이를 준 후 시작
    }

}