package com.example.smsfilteringapplication.services

import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Telephony
import android.telephony.SmsMessage
import android.widget.Toast

class SmsReceiver : BroadcastReceiver() {
    private val TAG = "SmsReceiver"
    var values = ContentValues()
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val bundle = intent.extras
            if (bundle != null) {
                val pdus = bundle.get("pdus") as Array<*>
                for (pdu in pdus) {
                    val smsMessage = SmsMessage.createFromPdu(pdu as ByteArray)
                    val sender = smsMessage.originatingAddress.toString()
                    val messageBody = smsMessage.messageBody.toString()

                writeSmsToInbox(context,sender,messageBody)

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
