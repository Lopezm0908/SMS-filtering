package com.example.smsfilteringapplication.dataclasses

import io.realm.kotlin.types.RealmObject

class StringItem : RealmObject {
    var content: String = ""
    var type : String = ""
    var sender = ""
    var ID = ""
}