package com.app.lifeset.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.app.lifeset.activity.NotificationActivity
import com.app.lifeset.databinding.AdpNotificationsBinding
import com.app.lifeset.model.NotificationModel
import com.app.lifeset.util.Utils

class NotificationAdapter(
    val mContext: NotificationActivity,
    val notificationList: ArrayList<NotificationModel>,
    val onclick: onItemClick
) : RecyclerView.Adapter<NotificationAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            AdpNotificationsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    override fun getItemCount(): Int {
        return notificationList.size

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = notificationList[position]
        holder.binding.tvType.text = model.type
        holder.binding.tvMessage.text = model.message
        holder.binding.tvDate.text = Utils.convertDateFormat(model.created)

        holder.binding.llClose.setOnClickListener {
            onclick.onDeleteNotifications(position, notificationList[position])
        }

        holder.itemView.setOnClickListener {
            onclick.onItemClick(position, notificationList[position])
        }


    }

    class MyViewHolder(val binding: AdpNotificationsBinding) : RecyclerView.ViewHolder(
        binding.root
    )


    interface onItemClick {
        fun onDeleteNotifications(position: Int, model: NotificationModel)

        fun onItemClick(position: Int, model: NotificationModel)
    }
}