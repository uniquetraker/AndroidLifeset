package com.app.lifeset.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.lifeset.R
import com.app.lifeset.activity.ChatListActivity
import com.app.lifeset.databinding.AdpChatlistBinding
import com.app.lifeset.model.chat.ChatModel
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class ChatListAdapter(
    val mContext: ChatListActivity, val chatList: ArrayList<ChatModel>,
    val onClick: onItemClick
) : RecyclerView.Adapter<ChatListAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            AdpChatlistBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(mContext).load(chatList[position].profile).placeholder(R.drawable.ic_profile)
            .error(R.drawable.ic_profile).into(holder.binding.cvProfile)
        holder.binding.tvName.text = chatList[position].name

        holder.binding.tvAcceptDate.text = convertDateFormat(chatList[position].date)
        holder.binding.tvMessage.text = chatList[position].comments
        holder.itemView.setOnClickListener {
            onClick.onClick(position, chatList[position])
        }
    }

    class MyViewHolder(val binding: AdpChatlistBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {

    }

    fun convertDateFormat(inputDate: String): String {
        // Define input and output date formats
        val zonedDateTime = ZonedDateTime.parse(inputDate)

        // If you need to format it differently, use a DateTimeFormatter
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a")
        val formattedDateTime = zonedDateTime.format(formatter)
        return formattedDateTime
    }

    interface onItemClick {
        fun onClick(position: Int, chatModel: ChatModel)
    }
}