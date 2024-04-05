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

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val bundle = intent.extras
            if (bundle != null) {
                val pdus = bundle.get("pdus") as Array<*>
                for (pdu in pdus) {
                    val smsMessage = SmsMessage.createFromPdu(pdu as ByteArray)
                    val sender = smsMessage.originatingAddress
                    val messageBody = smsMessage.messageBody

                    // Check if the message body meets the criteria for blocking
                    if (messageBody.contains("BLOCK_KEYWORD")) {
                        // Log the blocked message
                        Toast.makeText(
                            context,
                            "testing a thing message blocked",
                            Toast.LENGTH_LONG
                        ).show()
                        // Abort broadcast wont work but is a placeholder for now
                        abortBroadcast()
                    } else {
                        // Log the message
                        val values = ContentValues().apply {
                            put("address", sender)
                            put("body", messageBody)
                            put("read", false) // 0 for unread, 1 for read
                            put("date", System.currentTimeMillis())

                            put("type", "1") // 1 for received SMS
                        }

                        try {
                            context.contentResolver.insert(Uri.parse("content://sms/inbox"), values)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        Toast.makeText(
                            context,
                            "testing a thing",
                            Toast.LENGTH_LONG
                        ).show()
                        abortBroadcast()
                    }
                }
            }
        }

    }
    //wont work as a function so i inserted it higher up
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
}