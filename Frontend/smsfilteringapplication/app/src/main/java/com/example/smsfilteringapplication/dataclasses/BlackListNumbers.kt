package com.example.smsfilteringapplication.dataclasses

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject

class BlackListNumbers : RealmObject {
    var numberList: RealmList<String> = realmListOf<String>()
}