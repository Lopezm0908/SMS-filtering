package com.example.smsfilteringapplication.dataclasses
import android.app.DownloadManager.Query
import com.example.smsfilteringapplication.MyApp
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import java.lang.reflect.Field
import kotlin.reflect.KProperty


// In Kotlin, you can have top-level functions (functions that exist outside of classes).
// The functions can be called without having to create an instance of the object the function exists
// inside of.

private val realm = MyApp.realm

enum class QueryField(val value: String) {
//    BOY("boy"),
//    GIRL("girl")
    CONTENT("content"),
    SENDER("sender"),
    ID("id")
}
fun stringItemQueryToArrayList(type : String, data : QueryField) : ArrayList<String> {
//fun stringItemQueryToArrayList(type : String, data : String = "") : ArrayList<String> {
    val arrayListOfItems = arrayListOf<String>()
    arrayListOfItems.clear()
    val initialQuery = realm.query<StringItem>("type = $0", type).find().toList()

    for (i in initialQuery) {
        val field = StringItem::class.java.getDeclaredField(data.value)
        field.isAccessible = true
        val value = field.get(i)
        arrayListOfItems.add(value as String)
    }

    return arrayListOfItems
}
//    if (type == "Whitelist" || type == "KeyWord") {
//        for (i in initialQuery) {
//            arrayListOfItems.add(i.content)
//        }
//    } else if (type == "Eval") {
//        for (i in initialQuery) {
//            arrayListOfItems.add(i.content)
//        }
//    }
//    if (data == "content") {
//        for (i in initialQuery) {
//            arrayListOfItems.add(i.content)
//        }
//    } else if (data == "sender") {
//        for (i in initialQuery) {
//            arrayListOfItems.add(i.sender)
//        }
//    }
//    return arrayListOfItems
//}

suspend fun removeItem (newNumber : String, type : String){
    realm.write{
        val numToDelete : StringItem = realm.query<StringItem>("content = $0", newNumber).query("type = $0", type).find().first()
        val latest = findLatest(numToDelete)
        if (latest != null) {
            delete(latest)
        }
    }
}
suspend fun addItem (newNumber : String, type : String){
    realm.write{
        val testNum = StringItem().apply{
            content = newNumber
            this@apply.type = type
        }
        copyToRealm(testNum, updatePolicy = UpdatePolicy.ALL)
//        when (type) {
//            "Whitelist", "KeyWord" -> {
//                val testNum = StringItem().apply{
//                    content = newNumber
//                    this@apply.type = type
//                }
//                copyToRealm(testNum, updatePolicy = UpdatePolicy.ALL)
//            }
//            "Eval" -> {
//                val testNum = StringItem().apply{
//                    this@apply.sender = sender
//                    content = newNumber
//                    this@apply.type = type
//                }
//                copyToRealm(testNum, updatePolicy = UpdatePolicy.ALL)
//            }
//            else -> {
//                println("Invalid type.")
//            }
//        }
    }
}


class DatabaseDriver {
}