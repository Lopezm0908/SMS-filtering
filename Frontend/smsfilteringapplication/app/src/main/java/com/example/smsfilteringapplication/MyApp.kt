package com.example.smsfilteringapplication


import android.app.Application
import com.example.smsfilteringapplication.dataclasses.BlackListNumbers
import com.example.smsfilteringapplication.dataclasses.StringItem
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

//MyApp will serve as the database driver basically. All other viewmodels will open this application to interact with the database.


class MyApp: Application() {

    companion object {
        lateinit var realm: Realm
    }

    override fun onCreate() {
        super.onCreate()
        val config = RealmConfiguration.Builder(
            schema = setOf(
                BlackListNumbers::class,
                StringItem::class
            )
        ).deleteRealmIfMigrationNeeded().build()

//        realm = Realm.open(
//            configuration = RealmConfiguration.create(
//                schema = setOf(
//                    BlackListNumbers::class,
//                    StringItem::class
//                )
//            )
//        )
        realm = Realm.open(config)
    }
}