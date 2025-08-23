package com.app.lifeset.adapter

import android.content.Intent
import android.net.Uri
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.lifeset.activity.MainActivity
import com.app.lifeset.databinding.AdpEventBinding
import com.app.lifeset.extensions.createdDateFormat
import com.app.lifeset.model.EventData
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class EventAdapter(
    val mContext: MainActivity,
    val eventList: ArrayList<EventData>,
    val mainActivity: MainActivity
) : RecyclerView.Adapter<EventAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            AdpEventBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.binding.model = eventList[position]

        if (eventList[position].type.equals("event")) {
            holder.binding.ivAdvertisement.visibility = View.GONE
            holder.binding.llMain.visibility = View.VISIBLE


            if (!eventList[position].pincode.isNullOrEmpty()) {
                holder.binding.llPinCode.visibility = View.VISIBLE
            } else {
                holder.binding.llPinCode.visibility = View.GONE
            }
            holder.binding.tvDate.text = createdDateFormat(eventList[position].updated.toString())

            if (!eventList[position].description.isNullOrEmpty()) {
                holder.binding.tvDescription.visibility = View.VISIBLE
                holder.binding.tvDescriptionTitle.visibility = View.GONE
                holder.binding.tvDescription.text = Html.fromHtml(eventList[position].description)
            } else {
                holder.binding.tvDescription.visibility = View.GONE
                holder.binding.tvDescriptionTitle.visibility = View.GONE
            }

            if (!eventList[position].img.isNullOrEmpty()) {
                holder.binding.ivImage.visibility = View.VISIBLE
                Glide.with(mContext)
                    .load("https://lifeset.co.in/img/Post/" + eventList[position].img)
                    .into(holder.binding.ivImage)
            } else {
                holder.binding.ivImage.visibility = View.GONE
            }
        } else {
            holder.binding.ivAdvertisement.visibility = View.VISIBLE
            holder.binding.llMain.visibility = View.GONE
            Glide.with(mContext)
                .load("https://lifeset.co.in/admin/img/client/" + eventList[position].img)
                .into(holder.binding.ivAdvertisement)
            holder.binding.ivAdvertisement.setOnClickListener {
                val url = eventList[position].advertise_link // Replace with your desired URL
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                mContext.startActivity(intent)
            }
        }


    }

    class MyViewHolder(val binding: AdpEventBinding) : RecyclerView.ViewHolder(
        binding.root
    )

    fun getCurrentFormattedDate(): String {
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }

}