package org.gdgoc.donut.ui

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import org.gdgoc.donut.R
import org.gdgoc.donut.data.DonutSharedPreferences
import org.gdgoc.donut.databinding.ActivityReceiverMainBinding
import org.gdgoc.donut.ui.history.HistoryDetailFragment
import org.gdgoc.donut.ui.history.HistoryFragment
import org.gdgoc.donut.ui.home.ReceiverHomeFragment
import org.gdgoc.donut.ui.home.ReceiverHomePackageDetailFragment
import org.gdgoc.donut.ui.home.WalletDetailFragment
import org.gdgoc.donut.ui.mypage.ReceiverMyPageFragment
import org.gdgoc.donut.ui.ranking.RankingFragment
import org.gdgoc.donut.ui.receive.ReceiveAmountFragment
import org.gdgoc.donut.ui.receive.ReceiveStoreFragment
import org.gdgoc.donut.ui.receive.ReceiveStoreStartFragment
import org.gdgoc.donut.ui.viewModel.HomeViewModel
import org.gdgoc.donut.ui.viewModel.SignViewModel

class ReceiverMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReceiverMainBinding
    private val viewModel: SignViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission(),) { isGranted: Boolean ->
        if (isGranted) setFCM()
        else Toast.makeText(baseContext, "Turn off push notification", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReceiverMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setBottomNavigation()
        setFloatingButton()
        askNotificationPermission()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        enableFloatingButton()
    }

    private fun setBottomNavigation() {
        binding.bnvMain.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_wallet -> {
                    changeFragment(getString(R.string.menu_wallet))
                    DonutSharedPreferences.getAccessToken()?.let{homeViewModel.requestReceiverHomeInfo(it)}
                }
                R.id.menu_ranking -> changeFragment(getString(R.string.menu_ranking))
                R.id.menu_my_page -> changeFragment(getString(R.string.menu_my_page))
            }
            true
        }
        changeFragment(getString(R.string.menu_wallet))
    }

    fun changeFragment(tag: String){
        val fragment = supportFragmentManager.findFragmentByTag(tag) ?: when (tag) {
            getString(R.string.menu_wallet) -> ReceiverHomeFragment()
            getString(R.string.menu_history) -> HistoryFragment()
            getString(R.string.menu_ranking) -> RankingFragment()
            getString(R.string.menu_my_page) -> ReceiverMyPageFragment()
            "history_detail" -> HistoryDetailFragment()
            "package_detail" -> ReceiverHomePackageDetailFragment()
            "gift_detail" -> WalletDetailFragment()
            "receive_store_start" -> ReceiveStoreStartFragment()
            "receive_store" -> ReceiveStoreFragment()
            "receive_amount" -> ReceiveAmountFragment()
            else -> null
        } ?: return

        if (fragment.isVisible) return
        val transaction = supportFragmentManager.beginTransaction()

        supportFragmentManager.fragments
            .filter { it.isVisible }
            .forEach {
                transaction.addToBackStack(tag)
                transaction.hide(it)
            }

        if (fragment.isAdded) {
            transaction.show(fragment)
        } else {
            transaction.add(R.id.fcv_main, fragment, tag)
        }

        transaction.commit()
    }

    private fun setFloatingButton(){
        binding.fabReceiveBtn.setOnClickListener {
            changeFragment("receive_store_start")
        }
    }

    fun disableFloatingButton(){
        binding.fabReceiveBtn.visibility = View.GONE
    }

    fun enableFloatingButton(){
        binding.fabReceiveBtn.visibility = View.VISIBLE
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                setFCM()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun setFCM(){
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            DonutSharedPreferences.setFCMToken(task.result)
            DonutSharedPreferences.getAccessToken()
                ?.let { accessToken -> DonutSharedPreferences.getFCMToken()
                    ?.let { fcmToken -> viewModel.sendFCMToken(accessToken, fcmToken) }}
        })
    }
}