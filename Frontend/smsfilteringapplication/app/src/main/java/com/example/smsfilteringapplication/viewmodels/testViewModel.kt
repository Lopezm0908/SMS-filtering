package com.example.smsfilteringapplication.viewmodels

import androidx.lifecycle.ViewModel
import com.example.smsfilteringapplication.MyApp
import com.example.smsfilteringapplication.dataclasses.StringItem
import io.realm.kotlin.ext.query

class testViewModel : ViewModel() {
    private val realm = MyApp.realm
    val stringItem = realm.query<StringItem>()

}