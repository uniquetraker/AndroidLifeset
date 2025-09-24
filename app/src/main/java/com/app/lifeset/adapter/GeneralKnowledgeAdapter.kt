package com.app.lifeset.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.lifeset.activity.MainActivity
import com.app.lifeset.databinding.AdpGeneralknowledgeBinding
import com.app.lifeset.extensions.convertIsoToReadableDate
import com.app.lifeset.extensions.createdDateFormat
import com.app.lifeset.model.GeneralKnowledgeModel
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class GeneralKnowledgeAdapter(
    val mContext: MainActivity,
    val generalKnowledgeList: ArrayList<GeneralKnowledgeModel>,
    val mainActivity: MainActivity
) : RecyclerView.Adapter<GeneralKnowledgeAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            AdpGeneralknowledgeBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    override fun getItemCount(): Int {
        return generalKnowledgeList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.model = generalKnowledgeList[position]

        if (generalKnowledgeList[position].type.equals("GK")) {
            holder.binding.ivAdvertisement.visibility = View.GONE
            holder.binding.llMain.visibility = View.VISIBLE
            if (!generalKnowledgeList[position].updated.isNullOrEmpty()) {
                holder.binding.tvDate.text =
                    convertDateFormat(generalKnowledgeList[position].updated.toString())
            }
            holder.binding.tvCategory.text = generalKnowledgeList[position].category_name + " | " + generalKnowledgeList[position]
                .sub_category_name + " | " + generalKnowledgeList[position].section_name


            if (generalKnowledgeList[position].image.isNotEmpty()) {
                holder.binding.ivImage.visibility = View.VISIBLE
                Glide.with(mContext)
                    .load("https://lifeset.co.in/img/Post/" + generalKnowledgeList[position].image)
                    .into(holder.binding.ivImage)
            } else {
                holder.binding.ivImage.visibility = View.VISIBLE
            }



            if (!generalKnowledgeList[position].description.isNullOrEmpty()) {
                holder.binding.tvDescription.visibility = View.VISIBLE
                holder.binding.tvDescriptionTitle.visibility = View.GONE
                holder.binding.tvDescription.text =
                    Html.fromHtml(
                        generalKnowledgeList[position].description,
                        Html.FROM_HTML_MODE_COMPACT
                    )
                holder.binding.tvDescription.movementMethod = LinkMovementMethod.getInstance()
            } else {
                holder.binding.tvDescription.visibility = View.GONE
                holder.binding.tvDescriptionTitle.visibility = View.GONE
            }
        } else {
            holder.binding.ivAdvertisement.visibility = View.VISIBLE
            holder.binding.llMain.visibility = View.GONE
            Glide.with(mContext)
                .load("https://lifeset.co.in/admin/img/client/" + generalKnowledgeList[position].img)
                .into(holder.binding.ivAdvertisement)
            holder.binding.ivAdvertisement.setOnClickListener {
                val url =
                    generalKnowledgeList[position].advertise_link // Replace with your desired URL
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                mContext.startActivity(intent)
            }
        }


    }

    class MyViewHolder(val binding: AdpGeneralknowledgeBinding) : RecyclerView.ViewHolder(
        binding.root
    )

    fun convertDateFormat(inputDate: String): String {
        // Parse ISO datetime
        val parsedDate = OffsetDateTime.parse(inputDate)

        // Format to "22 September 2025"
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH)
        return parsedDate.format(formatter)
    }


}