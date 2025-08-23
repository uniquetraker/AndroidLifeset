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
import com.app.lifeset.databinding.AdpGkBinding
import com.app.lifeset.extensions.createdDateFormat
import com.app.lifeset.model.GKData
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GKAdapter(
    val mContext: MainActivity,
    val gkList: ArrayList<GKData>,
    val mainActivity: MainActivity
) : RecyclerView.Adapter<GKAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GKAdapter.MyViewHolder {
        return MyViewHolder(
            AdpGkBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    override fun getItemCount(): Int {
        return gkList.size
    }

    override fun onBindViewHolder(holder: GKAdapter.MyViewHolder, position: Int) {
        holder.binding.model = gkList[position]

        if (gkList[position].type.equals("GK")) {
            holder.binding.ivAdvertisement.visibility = View.GONE
            holder.binding.llMain.visibility = View.VISIBLE
            holder.binding.tvDate.text = createdDateFormat(gkList[position].updated_at.toString())

            if (!gkList[position].image.isNullOrEmpty()) {
                holder.binding.ivImage.visibility = View.VISIBLE
                Glide.with(mContext).load("https://lifeset.co.in/img/Post/" + gkList[position].image)
                    .into(holder.binding.ivImage)
            } else {
                holder.binding.ivImage.visibility = View.GONE
            }


            if (!gkList[position].pincode.isNullOrEmpty()) {
                holder.binding.llPinCode.visibility = View.GONE
            } else {
                holder.binding.llPinCode.visibility = View.GONE
            }



            if (!gkList[position].state.isNullOrEmpty()) {
                holder.binding.llState.visibility = View.GONE
            } else {
                holder.binding.llState.visibility = View.GONE
            }


            if (!gkList[position].district.isNullOrEmpty()) {
                holder.binding.llDistrict.visibility = View.GONE
            } else {
                holder.binding.llDistrict.visibility = View.GONE
            }

            if (!gkList[position].description.isNullOrEmpty()) {
                holder.binding.tvDescription.visibility = View.VISIBLE
                holder.binding.tvDescriptionTitle.visibility = View.GONE
                holder.binding.tvDescription.text =
                    Html.fromHtml(gkList[position].description, Html.FROM_HTML_MODE_COMPACT)
                holder.binding.tvDescription.movementMethod = LinkMovementMethod.getInstance()
            } else {
                holder.binding.tvDescription.visibility = View.GONE
                holder.binding.tvDescriptionTitle.visibility = View.GONE
            }
        }else{
            holder.binding.ivAdvertisement.visibility = View.VISIBLE
            holder.binding.llMain.visibility = View.GONE
            Glide.with(mContext)
                .load("https://lifeset.co.in/admin/img/client/" + gkList[position].img)
                .into(holder.binding.ivAdvertisement)
            holder.binding.ivAdvertisement.setOnClickListener {
                val url = gkList[position].advertise_link // Replace with your desired URL
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                mContext.startActivity(intent)
            }
        }


    }

    class MyViewHolder(val binding: AdpGkBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {

    }

    fun getCurrentFormattedDate(): String {
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }

}