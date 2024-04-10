package com.example.smsfilteringapplication.dataclasses

import io.realm.kotlin.types.RealmObject

class EvalItem : RealmObject {
    var sender : String = ""
    var content: String = ""
}