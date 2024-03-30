package com.example.smsfilteringapplication.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smsfilteringapplication.MyApp
import com.example.smsfilteringapplication.dataclasses.WhiteListNumbers
import io.realm.kotlin.UpdatePolicy
import kotlinx.coroutines.flow.SharingStarted
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

//Viewmodels are essentially for maintaining state and configuration on a particular screen.

class WhitelistViewModel : ViewModel(){
    private val realm = MyApp.realm

    val whitelistedNumbers = realm
        .query<WhiteListNumbers>()
        .asFlow()
        .map { results ->
            results.list.toList()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            emptyList()
        )




    //The following is for initial testing (creating sample entries to start
    init {
        createSampleEntries()
    }



    private fun createSampleEntries(){
        viewModelScope.launch {
            realm.write {
                val whiteList1 = WhiteListNumbers().apply {
                    number = "Test Number 1!"
                }
                val whiteList2 = WhiteListNumbers().apply {
                    number = "Test Number 2!"
                }
                val whiteList3 = WhiteListNumbers().apply {
                    number = "Test Number 3!"
                }
                copyToRealm(whiteList1, updatePolicy = UpdatePolicy.ALL) //updatePolicy = When something already exists, it is UPDATED
                copyToRealm(whiteList2, updatePolicy = UpdatePolicy.ALL)
                copyToRealm(whiteList3, updatePolicy = UpdatePolicy.ALL)
            }
        }
    }
}