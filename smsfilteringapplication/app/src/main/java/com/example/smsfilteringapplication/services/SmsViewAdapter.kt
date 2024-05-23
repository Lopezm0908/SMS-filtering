package com.example.smsfilteringapplication.services

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.smsfilteringapplication.R


class SmsViewAdapter(context: Context, fromarray: ArrayList<String>, bodyarray: ArrayList<String>): BaseAdapter() {
        private val mContext: Context

        val from_array = fromarray
        val body_array = bodyarray
        init{
            mContext = context
        }
        override fun getItemId(position: Int): Long {
            return position.toLong()
        }
        // responsible for how many rows
        override fun getCount(): Int {
            return body_array.size
        }
        override fun getItem(position: Int): Any {
            return "Test String"
        }

        //responsible for rendering each row
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(mContext)
            val blacklistRow = layoutInflater.inflate(R.layout.message_selectable_item, parent, false)


            val fromTextView = blacklistRow.findViewById<TextView>(R.id.from_data)
            val bodyTextView = blacklistRow.findViewById<TextView>(R.id.body_data)


            fromTextView.text = from_array.get(position)
            bodyTextView.text = body_array.get(position)

            return blacklistRow
        }

    }
