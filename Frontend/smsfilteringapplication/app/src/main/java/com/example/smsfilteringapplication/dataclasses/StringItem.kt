package com.example.smsfilteringapplication.dataclasses

import io.realm.kotlin.types.RealmObject

class StringItem : RealmObject {
    //var numberList: RealmList<String> = realmListOf<String>()
    var content: String = ""
    var type : String = ""
}