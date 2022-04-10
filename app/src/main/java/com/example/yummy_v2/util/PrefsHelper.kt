package com.example.yummy_v2.util

import android.content.Context
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrefsHelper @Inject constructor(@ApplicationContext context: Context) {
    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    var curLat: String?
        get() = prefs.getString("curLat", null)
        set(value) {
            val editor = prefs.edit()
            editor.putString("curLat", value)
            editor.apply()
        }

    var curLng: String?
        get() = prefs.getString("curLng", null)
        set(value) {
            val editor = prefs.edit()
            editor.putString("curLng", value)
            editor.apply()
        }
}