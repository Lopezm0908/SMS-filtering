package com.example.smsfilteringapplication.dataclasses

import io.realm.kotlin.types.RealmObject

class StringItem : RealmObject {
    var type : String = ""
    var content: String = ""
    var sender = ""
    var ID = ""
}