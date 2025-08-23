package com.app.lifeset.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.lifeset.activity.ChatHistoryActivity
import com.app.lifeset.databinding.AdpChathistoryBinding
import com.app.lifeset.model.chat.ChatHistoryModel
import com.app.lifeset.util.PrefManager
import com.app.lifeset.util.StaticData
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class ChatHistoryAdapter(
    val mContext: ChatHistoryActivity,
    val chatHistoryList: ArrayList<ChatHistoryModel>
) : RecyclerView.Adapter<ChatHistoryAdapter.MyviewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatHistoryAdapter.MyviewHolder {
        return MyviewHolder(
            AdpChathistoryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    override fun getItemCount(): Int {
        return chatHistoryList.size
    }

    override fun onBindViewHolder(holder: ChatHistoryAdapter.MyviewHolder, position: Int) {

        if (PrefManager(mContext).getvalue(StaticData.id)
                .toString() == chatHistoryList[position].sender_id
        ) {
            holder.binding.rrReceiver.visibility = View.VISIBLE
            holder.binding.rrSender.visibility = View.GONE
            holder.binding.tvReceiverDate.text = convertDateFormat(chatHistoryList[position].date)
            holder.binding.tvReceiverName.text = chatHistoryList[position].comments

        } else {
            holder.binding.rrReceiver.visibility = View.GONE
            holder.binding.rrSender.visibility = View.VISIBLE

            holder.binding.tvSenderDate.text = convertDateFormat(chatHistoryList[position].date)
            holder.binding.tvSenderName.text = chatHistoryList[position].comments
        }

    }

    class MyviewHolder(val binding: AdpChathistoryBinding) : RecyclerView.ViewHolder(
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

}