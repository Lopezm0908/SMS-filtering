package com.example.smsfilteringapplication


import android.app.Application
import com.example.smsfilteringapplication.dataclasses.BlackListNumbers
import com.example.smsfilteringapplication.dataclasses.WhiteListNumbers
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.UpdatePolicy

//MyApp will serve as the database driver basically. All other viewmodels will open this application to interact with the database.


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

    suspend fun numbDBWrite(numberDB : Realm){
        numberDB.write{
            val testNum = WhiteListNumbers().apply{
                number = "12345"
            }
            copyToRealm(testNum, updatePolicy = UpdatePolicy.ALL)
        }
    }
}