package com.example.smsfilteringapplication.services

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.smsfilteringapplication.R

class BlacklistAdapter(context: Context, numberarray: ArrayList<String>): BaseAdapter() {
    private val mContext: Context
    private val numberArrayLocal = numberarray

    init{
        mContext = context
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    // responsible for how many rows
    override fun getCount(): Int {
        return numberArrayLocal.size
    }
    override fun getItem(position: Int): Any {
        return "Test String"
    }

    //responsible for rendering each row
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(mContext)
       val blacklistRow = layoutInflater.inflate(R.layout.blacklist_whitelist_item_layout, parent, false)

        val numbersTextView = blacklistRow.findViewById<TextView>(R.id.item_phone_number)

        numbersTextView.text = numberArrayLocal.get(position)
        return blacklistRow
    }

}