package com.example.smsfilteringapplication.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smsfilteringapplication.dataclasses.WhiteListNumbers
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class testViewModel : ViewModel() {
    private val realm = MyApp.realm
    val whiteListNumbers = realm.query<WhiteListNumbers>()

}