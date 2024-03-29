package com.example.smsfilteringapplication.dataclasses

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject

// I imagine this class is for the database entries, so I'm going to modify it.
// Later, going to streamline these a bit so we don't reuse code, but brute forcing for now.

class WhiteListNumbers : RealmObject {
    var numberList: RealmList<String> = realmListOf<String>()
}


//data class whitelistnumbers(val name : String)  {
//
//}
