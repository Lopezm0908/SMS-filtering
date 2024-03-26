package com.example.smsfilteringapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsMessage
import android.util.Log
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
                        Log.d(TAG, "Blocked message from $sender: $messageBody")
                        // Abort broadcast to prevent the message from reaching the inbox
                        abortBroadcast()
                    } else {
                        // Log the message
                        Toast.makeText(
                            context,
                            "testing a thing",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.d(TAG, "Received message from $sender: $messageBody")
                        abortBroadcast()
                    }
                }
            }
        }
    }
}