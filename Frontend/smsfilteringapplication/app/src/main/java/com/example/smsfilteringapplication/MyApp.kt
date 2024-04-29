package com.example.smsfilteringapplication


import android.app.Application
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
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
        //Realm setup
        val config = RealmConfiguration.Builder(
            schema = setOf(
                StringItem::class
            )
        ).deleteRealmIfMigrationNeeded().build()
        realm = Realm.open(config)

        //Python setup
        Python.start(AndroidPlatform(this))
    }
}