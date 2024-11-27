package org.gdgoc.donut.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object DonutSharedPreferences {
    private lateinit var preferences: SharedPreferences
    private const val PREFERENCES_NAME = "DONUT_PREFERENCES"
    private const val KEY_ACCESS_TOKEN = "KEY_ACCESS_TOKEN"
    private const val USER_ID = "USER_ID"
    private const val USER_ROLE = "USER_ROLE"
    private const val ACCESS_TOKEN = "ACCESS_TOKEN"
    private const val FCM_TOKEN = "FCM_TOKEN"

    fun init(context: Context) {
        preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    fun set(token: String?){
        preferences.edit {
            if (token == null) remove(KEY_ACCESS_TOKEN)
            else putString(KEY_ACCESS_TOKEN, token)
        }
    }

    fun setUserId(id: String?){
        preferences.edit {
            if (id == null) {
                remove(USER_ID)
            } else {
                putString(USER_ID, id)
            }
        }
    }

    fun getUserId(): String? = preferences.getString(USER_ID, null)

    fun setAccessToken(token: String?){
        preferences.edit {
            if (token == null) {
                remove(ACCESS_TOKEN)
            } else {
                putString(ACCESS_TOKEN, token)
            }
        }
    }

    fun getAccessToken(): String? = preferences.getString(ACCESS_TOKEN, null)

    fun setFCMToken(token: String?){
        preferences.edit {
            if (token == null) {
                remove(FCM_TOKEN)
            } else {
                putString(FCM_TOKEN, token)
            }
        }
    }

    fun getFCMToken(): String? = preferences.getString(FCM_TOKEN, null)

    fun setUserRole(role: String?){
        preferences.edit {
            if (role == null) {
                remove(USER_ROLE)
            } else {
                putString(USER_ROLE, role)
            }
        }
    }

    fun getUserRole(): String? = preferences.getString(USER_ROLE, null)
}