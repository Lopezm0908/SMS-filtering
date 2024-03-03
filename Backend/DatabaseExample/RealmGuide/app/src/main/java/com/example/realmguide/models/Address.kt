package com.example.realmguide.models
import io.realm.kotlin.types.EmbeddedRealmObject
import io.realm.kotlin.types.RealmObject
// address
// Teacher 1-1 address
// Teacher 1-many courses
// Students many-many courses


// we use embedded realm object since the address will ALWAYS be associated with a teacher in 1-1
class Address : EmbeddedRealmObject {
    var fullName: String = ""
    var street: String = ""
    var houseNumber: Int = 0
    var zip: Int = 0
    var city: String = ""
    var teacher: Teacher? = null
}