package com.example.smsfilteringapplication.services

import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Telephony
import android.telephony.SmsMessage
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.example.smsfilteringapplication.dataclasses.DetermineSpam
import com.example.smsfilteringapplication.dataclasses.GlobalValues
import com.example.smsfilteringapplication.dataclasses.QueryField
import com.example.smsfilteringapplication.dataclasses.addItem
import com.example.smsfilteringapplication.dataclasses.stringItemQueryToArrayList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class SmsReceiver : BroadcastReceiver()
{
    object SmsApplicationScope {
        val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }
    companion object {
        private var lastProcessedTime: Long = 0
    }
    private val TAG = "SmsReceiver"
    private val type = "KeyWord"
    private var keyWordList = stringItemQueryToArrayList(type, QueryField.CONTENT)
    private var whitelist = stringItemQueryToArrayList("Whitelist", QueryField.CONTENT)
    private var sendergl = String()
    private var bodygl = String()
    private val checkMsg = DetermineSpam()
    private val py: Python = Python.getInstance()
    private val module: PyObject = py.getModule("BERT")


    override fun onReceive(context: Context, intent: Intent) {
        // buffers lists for evaluation
        keyWordList = stringItemQueryToArrayList(type, QueryField.CONTENT)
        whitelist = stringItemQueryToArrayList("Whitelist", QueryField.CONTENT)

        Log.d(TAG, "onReceive triggered")
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) { //packages received message
            val bundle = intent.extras
            if (bundle != null) {
                Log.d(TAG, "SMS bundle is not null")
                val pdus = bundle.get("pdus") as Array<*>
                val format = bundle.getString("format")
                var fullMessage = ""
                for (pdu in pdus) {
                    val smsMessage = SmsMessage.createFromPdu(pdu as ByteArray, format ?: "")
                    fullMessage += smsMessage.messageBody
                }
                sendergl = pdus[0]?.let {
                    val smsMessage = SmsMessage.createFromPdu(it as ByteArray)
                    smsMessage.originatingAddress.toString()
                } ?: ""
                bodygl = fullMessage
                //stops duplicates from occurring
                val currentTime = System.currentTimeMillis()
                if (currentTime - lastProcessedTime < 500) { // 1 second threshold
                    Log.d(TAG, "Ignoring duplicate SMS received within a short interval.")
                    return
                }
                lastProcessedTime = currentTime
                //runs through spam filters, if statements check different filters based on what is selected.
                run {
                    Log.d(TAG, "Checking spam filters")
                    val pyFunc: PyObject? = module["BertApiRequest"]
                    if (isStringInSmsBody(whitelist, sendergl) == true) {
                        Log.d(TAG, "Writing SMS to inbox")
                        writeSmsToInbox(context, sendergl, bodygl)
                        return
                    } else {
                        if (GlobalValues.getCheckboxState(
                                context,
                                "bertcheck"
                            ) == false && GlobalValues.getCheckboxState(
                                context,
                                "evalcheck"
                            ) == false && GlobalValues.getCheckboxState(
                                context,
                                "keycheck"
                            ) == false && GlobalValues.getCheckboxState(context, "logic") == false
                        ) {
                            Log.d(TAG, "Writing SMS to inbox")
                            writeSmsToInbox(context, sendergl, bodygl)
                            return
                        } else if (GlobalValues.getCheckboxState(
                                context,
                                "bertcheck"
                            ) == false && GlobalValues.getCheckboxState(
                                context,
                                "evalcheck"
                            ) == false && GlobalValues.getCheckboxState(
                                context,
                                "keycheck"
                            ) == false && GlobalValues.getCheckboxState(context, "logic") == true
                        ) {
                            if (!checkMsg.Determine(bodygl) && sendergl.isNotEmpty()) {
                                Log.d(TAG, "Writing SMS to inbox")
                                writeSmsToInbox(context, sendergl, bodygl)
                                return
                            } else {
                                Log.d(TAG, "Blocking SMS as spam")
                                Toast.makeText(
                                    context,
                                    "message from $sendergl has been blocked by spam filter",
                                    Toast.LENGTH_LONG
                                ).show()
                                return
                            }
                        } else if (GlobalValues.getCheckboxState(
                                context,
                                "bertcheck"
                            ) == false && GlobalValues.getCheckboxState(
                                context,
                                "evalcheck"
                            ) == false && GlobalValues.getCheckboxState(
                                context,
                                "keycheck"
                            ) == true && GlobalValues.getCheckboxState(context, "logic") == false
                        ) {
                            if (!isStringInSmsBody(keyWordList, bodygl) && sendergl.isNotEmpty()) {
                                Log.d(TAG, "Writing SMS to inbox")
                                writeSmsToInbox(context, sendergl, bodygl)
                                return
                            } else {
                                Log.d(TAG, "Blocking SMS as spam")
                                Toast.makeText(
                                    context,
                                    "message from $sendergl has been blocked by spam filter",
                                    Toast.LENGTH_LONG
                                ).show()
                                return


                            }
                        } else if (GlobalValues.getCheckboxState(
                                context,
                                "bertcheck"
                            ) == false && GlobalValues.getCheckboxState(
                                context,
                                "evalcheck"
                            ) == false && GlobalValues.getCheckboxState(
                                context,
                                "keycheck"
                            ) == true && GlobalValues.getCheckboxState(context, "logic") == true
                        ) {
                            if (!isStringInSmsBody(
                                    keyWordList,
                                    bodygl
                                ) && !checkMsg.Determine(bodygl) && sendergl.isNotEmpty()
                            ) {
                                Log.d(TAG, "Writing SMS to inbox")
                                writeSmsToInbox(context, sendergl, bodygl)
                                return
                            } else {
                                Log.d(TAG, "Blocking SMS as spam")
                                Toast.makeText(
                                    context,
                                    "message from $sendergl has been blocked by spam filter",
                                    Toast.LENGTH_LONG
                                ).show()
                                return
                            }
                        } else if (GlobalValues.getCheckboxState(
                                context,
                                "bertcheck"
                            ) == false && GlobalValues.getCheckboxState(
                                context,
                                "evalcheck"
                            ) == true && GlobalValues.getCheckboxState(
                                context,
                                "keycheck"
                            ) == false && GlobalValues.getCheckboxState(context, "logic") == false
                        ) {

                            Log.d(TAG, "Writing SMS to inbox")
                            writeSmsToInbox(context, sendergl, bodygl)
                            return


                        } else if (GlobalValues.getCheckboxState(
                                context,
                                "bertcheck"
                            ) == false && GlobalValues.getCheckboxState(
                                context,
                                "evalcheck"
                            ) == true && GlobalValues.getCheckboxState(
                                context,
                                "keycheck"
                            ) == false && GlobalValues.getCheckboxState(context, "logic") == true
                        ) {
                            if (!checkMsg.Determine(bodygl) && sendergl.isNotEmpty()) {
                                Log.d(TAG, "Writing SMS to inbox")
                                writeSmsToInbox(context, sendergl, bodygl)
                                return
                            } else {
                                Log.d(TAG, "Blocking SMS as spam")
                                Toast.makeText(
                                    context,
                                    "message from $sendergl has been blocked by spam filter",
                                    Toast.LENGTH_LONG
                                ).show()
                                SmsApplicationScope.applicationScope.launch {
                                    addItem(bodygl, "Eval", "1", sendergl)
                                }
                                Log.d(TAG, "Sending blocked message to evaluation")
                                return
                            }
                        } else if (GlobalValues.getCheckboxState(
                                context,
                                "bertcheck"
                            ) == false && GlobalValues.getCheckboxState(
                                context,
                                "evalcheck"
                            ) == true && GlobalValues.getCheckboxState(
                                context,
                                "keycheck"
                            ) == true && GlobalValues.getCheckboxState(context, "logic") == false
                        ) {
                            if (!isStringInSmsBody(keyWordList, bodygl) && sendergl.isNotEmpty()) {
                                Log.d(TAG, "Writing SMS to inbox")
                                writeSmsToInbox(context, sendergl, bodygl)
                                return
                            } else {
                                Log.d(TAG, "Blocking SMS as spam")
                                Toast.makeText(
                                    context,
                                    "message from $sendergl has been blocked by spam filter",
                                    Toast.LENGTH_LONG
                                ).show()
                                SmsApplicationScope.applicationScope.launch {
                                    addItem(bodygl, "Eval", "1", sendergl)
                                }
                                Log.d(TAG, "Sending blocked message to evaluation")
                                return
                            }
                        } else if (GlobalValues.getCheckboxState(
                                context,
                                "bertcheck"
                            ) == false && GlobalValues.getCheckboxState(
                                context,
                                "evalcheck"
                            ) == true && GlobalValues.getCheckboxState(
                                context,
                                "keycheck"
                            ) == true && GlobalValues.getCheckboxState(context, "logic") == true
                        ) {
                            if (!isStringInSmsBody(
                                    keyWordList,
                                    bodygl
                                ) && !checkMsg.Determine(bodygl) && sendergl.isNotEmpty()
                            ) {
                                Log.d(TAG, "Writing SMS to inbox")
                                writeSmsToInbox(context, sendergl, bodygl)
                                return
                            } else {
                                Log.d(TAG, "Blocking SMS as spam")
                                Toast.makeText(
                                    context,
                                    "message from $sendergl has been blocked by spam filter",
                                    Toast.LENGTH_LONG
                                ).show()
                                SmsApplicationScope.applicationScope.launch {
                                    addItem(bodygl, "Eval", "1", sendergl)
                                }
                                Log.d(TAG, "Sending blocked message to evaluation")
                                return
                            }
                        } else if (GlobalValues.getCheckboxState(
                                context,
                                "bertcheck"
                            ) == true && GlobalValues.getCheckboxState(
                                context,
                                "evalcheck"
                            ) == false && GlobalValues.getCheckboxState(
                                context,
                                "keycheck"
                            ) == false && GlobalValues.getCheckboxState(context, "logic") == false
                        ) {
                            val newItem: String = pyFunc?.call(bodygl).toString()
                            while (newItem != null) {
                                if (newItem == "False" && sendergl.isNotEmpty()) {
                                    Log.d(TAG, "Writing SMS to inbox")
                                    writeSmsToInbox(context, sendergl, bodygl)
                                    return
                                } else {
                                    Log.d(TAG, "Blocking SMS as spam")
                                    Toast.makeText(
                                        context,
                                        "message from $sendergl has been blocked by spam filter",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    return
                                }
                            }
                        } else if (GlobalValues.getCheckboxState(
                                context,
                                "bertcheck"
                            ) == true && GlobalValues.getCheckboxState(
                                context,
                                "evalcheck"
                            ) == false && GlobalValues.getCheckboxState(
                                context,
                                "keycheck"
                            ) == false && GlobalValues.getCheckboxState(context, "logic") == true
                        ) {
                            val newItem: String = pyFunc?.call(bodygl).toString()
                            while (newItem != null) {
                                if (newItem == "False" && !checkMsg.Determine(bodygl) && sendergl.isNotEmpty()) {
                                    Log.d(TAG, "Writing SMS to inbox")
                                    writeSmsToInbox(context, sendergl, bodygl)
                                    return
                                } else {
                                    Log.d(TAG, "Blocking SMS as spam")
                                    Toast.makeText(
                                        context,
                                        "message from $sendergl has been blocked by spam filter",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    return

                                }
                            }
                        } else if (GlobalValues.getCheckboxState(
                                context,
                                "bertcheck"
                            ) == true && GlobalValues.getCheckboxState(
                                context,
                                "evalcheck"
                            ) == false && GlobalValues.getCheckboxState(
                                context,
                                "keycheck"
                            ) == true && GlobalValues.getCheckboxState(context, "logic") == false
                        ) {
                            val newItem: String = pyFunc?.call(bodygl).toString()
                            while (newItem != null) {
                                if (!isStringInSmsBody(
                                        keyWordList,
                                        bodygl
                                    ) && newItem == "False" && sendergl.isNotEmpty()
                                ) {
                                    Log.d(TAG, "Writing SMS to inbox")
                                    writeSmsToInbox(context, sendergl, bodygl)
                                    return
                                } else {
                                    Log.d(TAG, "Blocking SMS as spam")
                                    Toast.makeText(
                                        context,
                                        "message from $sendergl has been blocked by spam filter",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    return

                                }
                            }
                        } else if (GlobalValues.getCheckboxState(
                                context,
                                "bertcheck"
                            ) == true && GlobalValues.getCheckboxState(
                                context,
                                "evalcheck"
                            ) == false && GlobalValues.getCheckboxState(
                                context,
                                "keycheck"
                            ) == true && GlobalValues.getCheckboxState(context, "logic") == true
                        ) {
                            val newItem: String = pyFunc?.call(bodygl).toString()
                            while (newItem != null) {
                                if (newItem == "False" && !isStringInSmsBody(
                                        keyWordList,
                                        bodygl
                                    ) && !checkMsg.Determine(bodygl) && sendergl.isNotEmpty()
                                ) {
                                    Log.d(TAG, "Writing SMS to inbox")
                                    writeSmsToInbox(context, sendergl, bodygl)
                                    return
                                } else {
                                    Log.d(TAG, "Blocking SMS as spam")
                                    Toast.makeText(
                                        context,
                                        "message from $sendergl has been blocked by spam filter",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    return

                                }
                            }
                        } else if (GlobalValues.getCheckboxState(
                                context,
                                "bertcheck"
                            ) == true && GlobalValues.getCheckboxState(
                                context,
                                "evalcheck"
                            ) == true && GlobalValues.getCheckboxState(
                                context,
                                "keycheck"
                            ) == false && GlobalValues.getCheckboxState(context, "logic") == false
                        ) {
                            val newItem: String = pyFunc?.call(bodygl).toString()
                            while (newItem != null) {
                                if (newItem == "False" && sendergl.isNotEmpty()) {
                                    Log.d(TAG, "Writing SMS to inbox")
                                    writeSmsToInbox(context, sendergl, bodygl)
                                    return
                                } else {
                                    Log.d(TAG, "Blocking SMS as spam")
                                    Toast.makeText(
                                        context,
                                        "message from $sendergl has been blocked by spam filter",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    SmsApplicationScope.applicationScope.launch {
                                        addItem(bodygl, "Eval", "1", sendergl)
                                    }
                                    Log.d(TAG, "Sending blocked message to evaluation")
                                    return
                                }
                            }
                        } else if (GlobalValues.getCheckboxState(
                                context,
                                "bertcheck"
                            ) == true && GlobalValues.getCheckboxState(
                                context,
                                "evalcheck"
                            ) == true && GlobalValues.getCheckboxState(
                                context,
                                "keycheck"
                            ) == false && GlobalValues.getCheckboxState(context, "logic") == true
                        ) {
                            val newItem: String = pyFunc?.call(bodygl).toString()
                            while (newItem != null) {
                                if (newItem == "False" && !checkMsg.Determine(bodygl) && sendergl.isNotEmpty()) {
                                    Log.d(TAG, "Writing SMS to inbox")
                                    writeSmsToInbox(context, sendergl, bodygl)
                                    return
                                } else {
                                    Log.d(TAG, "Blocking SMS as spam")
                                    Toast.makeText(
                                        context,
                                        "message from $sendergl has been blocked by spam filter",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    SmsApplicationScope.applicationScope.launch {
                                        addItem(bodygl, "Eval", "1", sendergl)
                                    }
                                    Log.d(TAG, "Sending blocked message to evaluation")
                                    return
                                }
                            }
                        } else if (GlobalValues.getCheckboxState(
                                context,
                                "bertcheck"
                            ) == true && GlobalValues.getCheckboxState(
                                context,
                                "evalcheck"
                            ) == true && GlobalValues.getCheckboxState(
                                context,
                                "keycheck"
                            ) == true && GlobalValues.getCheckboxState(context, "logic") == false
                        ) {
                            val newItem: String = pyFunc?.call(bodygl).toString()
                            while (newItem != null) {
                                if (!isStringInSmsBody(
                                        keyWordList,
                                        bodygl
                                    ) && newItem == "False" && sendergl.isNotEmpty()
                                ) {
                                    Log.d(TAG, "Writing SMS to inbox")
                                    writeSmsToInbox(context, sendergl, bodygl)
                                    return
                                } else {
                                    Log.d(TAG, "Blocking SMS as spam")
                                    Toast.makeText(
                                        context,
                                        "message from $sendergl has been blocked by spam filter",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    SmsApplicationScope.applicationScope.launch {
                                        addItem(bodygl, "Eval", "1", sendergl)
                                    }
                                    Log.d(TAG, "Sending blocked message to evaluation")
                                    return
                                }
                            }
                        } else if (GlobalValues.getCheckboxState(
                                context,
                                "bertcheck"
                            ) == true && GlobalValues.getCheckboxState(
                                context,
                                "evalcheck"
                            ) == true && GlobalValues.getCheckboxState(
                                context,
                                "keycheck"
                            ) == true && GlobalValues.getCheckboxState(context, "logic") == true
                        ) {

                            val newItem: String = pyFunc?.call(bodygl).toString()
                            if (newItem != null) {
                                if (newItem == "False" && !isStringInSmsBody(
                                        keyWordList,
                                        bodygl
                                    ) && !checkMsg.Determine(bodygl) && sendergl.isNotEmpty()
                                ) {
                                    Log.d(TAG, "Writing SMS to inbox")
                                    writeSmsToInbox(context, sendergl, bodygl)
                                    return
                                } else {
                                    Log.d(TAG, "Blocking SMS as spam")
                                    Toast.makeText(
                                        context,
                                        "message from $sendergl has been blocked by spam filter",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    SmsApplicationScope.applicationScope.launch {
                                        addItem(bodygl, "Eval", "1", sendergl)
                                    }
                                    Log.d(TAG, "Sending blocked message to evaluation")
                                    return
                                }
                            }
                        }
                    }
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
