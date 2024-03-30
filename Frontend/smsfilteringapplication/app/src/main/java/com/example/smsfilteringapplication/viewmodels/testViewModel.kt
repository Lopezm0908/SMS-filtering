package com.example.smsfilteringapplication.viewmodels

import androidx.lifecycle.ViewModel
import com.example.smsfilteringapplication.MyApp
import com.example.smsfilteringapplication.dataclasses.WhiteListNumbers
import io.realm.kotlin.ext.query

class testViewModel : ViewModel() {
    private val realm = MyApp.realm
    val whiteListNumbers = realm.query<WhiteListNumbers>()

}