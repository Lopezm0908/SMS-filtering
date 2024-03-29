package com.example.smsfilteringapplication


import android.app.Application
import com.example.smsfilteringapplication.dataclasses.BlackListNumbers
import com.example.smsfilteringapplication.dataclasses.WhiteListNumbers
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class MyApp: Application() {

    companion object {
        lateinit var realm: Realm
    }

    override fun onCreate() {
        super.onCreate()
        realm = Realm.open(
            configuration = RealmConfiguration.create(
                schema = setOf(
                    BlackListNumbers::class,
                    WhiteListNumbers::class,
                )
            )
        )
    }
}