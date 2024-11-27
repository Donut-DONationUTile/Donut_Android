package org.gdgoc.donut

import android.app.Application
import org.gdgoc.donut.data.DonutSharedPreferences

class DonutApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initSharedPreferences()
    }

    private fun initSharedPreferences() {
        DonutSharedPreferences.init(applicationContext)
    }
}