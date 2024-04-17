package com.example.smsfilteringapplication.services

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import com.example.smsfilteringapplication.R
import com.example.smsfilteringapplication.screens.*

class mainmenuadapter(private val mContext: Context) : BaseAdapter() {
    private val mainarraylocal = arrayListOf("Evaluation Mailbox", "Blacklist", "Message Reporting", "Whitelist", "Keywords")
    private val descarraylocal = arrayListOf("eval description", "blacklist description", "message reporting description", "whitelist description", "keyword description")

    override fun getCount(): Int = mainarraylocal.size

    override fun getItem(position: Int): Any = mainarraylocal[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(mContext).inflate(R.layout.main_bl_tile, parent, false)

        val mainTextView = view.findViewById<TextView>(R.id.Bl_label_txt)
        val descTextView = view.findViewById<TextView>(R.id.Bl_label_desc)
        val buttonItem = view.findViewById<Button>(R.id.tile_bl_btn)
        val backgroundtv = view.findViewById<TextView>(R.id.background_tv)
        val colorlist = arrayListOf<Int>()
        mainTextView.text = mainarraylocal[position]
        descTextView.text = descarraylocal[position]
        //backgroundtv.setBackgroundColor(colorlist.get(position))
        buttonItem.setOnClickListener {
            val intent = when (position) {
                0 -> Intent(mContext, Evalmailbox::class.java)
                1 -> Intent(mContext, Blacklist::class.java)
                2 -> Intent(mContext, Messagereporting::class.java)
                3 -> Intent(mContext, Whitelist::class.java)
                4 -> Intent(mContext, KeywordManager::class.java)
                else -> return@setOnClickListener
            }
            mContext.startActivity(intent)
        }

        val layoutParams = view.layoutParams ?: ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        layoutParams.height = parent?.height ?: ViewGroup.LayoutParams.MATCH_PARENT
        view.layoutParams = layoutParams

        return view
    }

}
