package com.example.smsfilteringapplication.services

import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Telephony
import android.telephony.SmsMessage
import android.widget.Toast
import com.example.smsfilteringapplication.dataclasses.DetermineSpam
import com.example.smsfilteringapplication.dataclasses.QueryField
import com.example.smsfilteringapplication.dataclasses.stringItemQueryToArrayList

class SmsReceiver : BroadcastReceiver() {
    private val TAG = "SmsReceiver"
    private val type = "KeyWord"
    var values = ContentValues()
    //var keywordlist = mutableListOf<String>("key1", "key2")
    var keyWordList = stringItemQueryToArrayList(type, QueryField.CONTENT)
    var sendergl = String()
    var bodygl = String()
    val checkMsg = DetermineSpam()
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val bundle = intent.extras
            if (bundle != null) {
                val pdus = bundle.get("pdus") as Array<*>
                for (pdu in pdus) {
                    val smsMessage = SmsMessage.createFromPdu(pdu as ByteArray)
                     sendergl = smsMessage.originatingAddress.toString()
                     bodygl = smsMessage.messageBody.toString()

                    }
                if (isStringInSmsBody(keyWordList, bodygl) == false && checkMsg.Determine(bodygl) == false) {
                    writeSmsToInbox(context, sendergl, bodygl)
                }
                else
                {
                    Toast.makeText(
                        context,
                        "message from $sendergl has been blocked by spam value ${checkMsg.Determine(bodygl)}",
                        Toast.LENGTH_LONG
                    ).show()

                }
                }
            }
        }
    }
    fun writeSmsToInbox(context: Context, sender: String, messageBody: String) {
        val values = ContentValues().apply {
            put("address", sender)
            put("body", messageBody)
            put("read", false) // 0 for unread, 1 for read
            put("date", System.currentTimeMillis())

            put("type", "1") //1 for received
        }

        try {
            context.contentResolver.insert(Uri.parse("content://sms/inbox"), values)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
fun isStringInSmsBody(listOfStrings: List<String>, smsBody: String): Boolean {
    // Iterate through each string in the list
    for (string in listOfStrings) {
        // Check if the current string is contained in the SMS message body
        if (smsBody.contains(string, ignoreCase = true)) {
            // Return true if a match is found
            return true
        }
    }
    // Return false if no matches are found
    return false
}
