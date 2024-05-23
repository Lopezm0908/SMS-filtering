package com.example.smsfilteringapplication.dataclasses

import android.content.Context

object GlobalValues {
    private const val PREF_NAME = "MyPrefs"

    fun saveCheckboxState(context: Context, key: String, isChecked: Boolean) {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean(key, isChecked).apply()
    }

    fun getCheckboxState(context: Context, key: String): Boolean {
        val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(key, false)
    }
}