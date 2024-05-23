package com.example.smsfilteringapplication.services

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.smsfilteringapplication.R
import com.example.smsfilteringapplication.screens.*


class MainMenuAdapter(private val mContext: Context) : BaseAdapter() {
    // define variables for main menu tiles
    private val mainArrayLocal = arrayListOf("Evaluation Mailbox", "Blacklist", "Message Reporting", "Whitelist", "Keywords", "Settings")
    private val descArrayLocal = arrayListOf("Review messages marked as spam in your evaluation mailbox. Decide whether to confirm them as spam, refining the filter, or allow them through if they were mistakenly flagged. Keep your inbox tailored to your preferences with every evaluation.", "Block unwanted texts effortlessly by adding specific numbers to your blacklist. Never receive messages from unwanted contacts again", "Take control of your inbox by reporting unwanted messages as spam. Help improve the effectiveness of the spam filter for yourself and other users by flagging unsolicited or harmful texts.", "Ensure you never miss important messages by allowing specific numbers to bypass your SMS filter. Keep communication open with trusted contacts without any interruption.", "Customize your message filtering by setting up keywords. If any incoming message contains these keywords, you can choose to have it flagged or blocked according to your preferences.","Enjoy the flexibility to tailor your messaging app exactly to your liking, ensuring a seamless and secure communication experience every time you use it.")
    private val colorList = listOf(
        R.color.darkPastelBlue,
        R.color.darkPastelPurple,
        R.color.darkPastelGreen,
        R.color.darkPastelRed,
        R.color.darkPastelOrange,
        R.color.darkPastelPink
    )
    override fun getCount(): Int = mainArrayLocal.size

    override fun getItem(position: Int): Any = mainArrayLocal[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(mContext).inflate(R.layout.main_bl_tile, parent, false)
        val mainTextView = view.findViewById<TextView>(R.id.Bl_label_txt)
        val descTextView = view.findViewById<TextView>(R.id.Bl_label_desc)
        val buttonItem = view.findViewById<Button>(R.id.tile_bl_btn)
        val backgroundTV = view.findViewById<TextView>(R.id.background_tv)
        mainTextView.text = mainArrayLocal[position]
        descTextView.text = descArrayLocal[position]
        val colorId = colorList[position % colorList.size] // Use modulus to cycle through colors


        val colorStateList = ColorStateList.valueOf(ContextCompat.getColor(mContext, colorId))
        backgroundTV.backgroundTintList = colorStateList
        // navigation to other screens
        buttonItem.setOnClickListener {
            val intent = when (position) {
                0 -> Intent(mContext, EvalMailBox::class.java)
                1 -> Intent(mContext, Blacklist::class.java)
                2 -> Intent(mContext, MessageReporting::class.java)
                3 -> Intent(mContext, Whitelist::class.java)
                4 -> Intent(mContext, KeywordManager::class.java)
                5 -> Intent(mContext, Settings::class.java)
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
