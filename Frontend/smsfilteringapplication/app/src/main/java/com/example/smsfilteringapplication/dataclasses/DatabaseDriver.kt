package com.example.smsfilteringapplication.dataclasses
import com.example.smsfilteringapplication.MyApp
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject


// In Kotlin, you can have top-level functions (functions that exist outside of classes).
// The functions can be called without having to create an instance of the object the function exists
// inside of.

private val realm = MyApp.realm
fun realmQueryToArrayList(type : String) : ArrayList<String> {
    val arrayListOfItems = arrayListOf<String>()
    arrayListOfItems.clear()
    val whiteListedNumbers = realm.query<StringItem>("type = $0", type).find().toList()

    for(i in whiteListedNumbers){
        arrayListOfItems.add(i.content)
    }
    return arrayListOfItems
}

suspend fun removeNumber (newNumber : String, type : String){
    realm.write{
        //val numToDelete : StringItem = realm.query<StringItem>("content = $0", newNumber).find().first()
        val numToDelete : StringItem = realm.query<StringItem>("content = $0", newNumber).query("type = $0", type).find().first()
        val latest = findLatest(numToDelete)
        if (latest != null) {
            delete(latest)
        }
    }
}
suspend fun addNumber (newNumber : String, type : String){
    realm.write{
        val testNum = StringItem().apply{
            content = newNumber
            this@apply.type = type
        }
        copyToRealm(testNum, updatePolicy = UpdatePolicy.ALL)
    }
}

class DatabaseDriver {
}