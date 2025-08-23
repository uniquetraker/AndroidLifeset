package com.app.lifeset.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.lifeset.activity.MainActivity
import com.app.lifeset.databinding.AdpExamBinding
import com.app.lifeset.model.ExamModel
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class ExamAdapter(
    val mContext: MainActivity,
    val examList: ArrayList<ExamModel>,
    val context: Context
) : RecyclerView.Adapter<ExamAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            AdpExamBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    override fun getItemCount(): Int {
        return examList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = examList[position]
        holder.binding.model = model
        if (examList[position].type.equals("Government Exam")) {
            holder.binding.llMain.visibility = View.VISIBLE
            holder.binding.ivAdvertisement.visibility = View.GONE
            if (!model.announcement_date.isNullOrEmpty()) {
                holder.binding.tvAnnouncementDate.text = convertDateFormat(model.announcement_date)
            }

            if (!model.last_date_form_filling.isNullOrEmpty()) {
                holder.binding.tvFormFillingDate.text =
                    convertDateFormat(model.last_date_form_filling)
            }

            holder.binding.tvDate.text = createdDateFormat(model.updated_at.toString())

            if (!model.fees.isNullOrEmpty()) {
                holder.binding.llFees.visibility = View.VISIBLE
            } else {
                holder.binding.llFees.visibility = View.GONE
            }

            if (!model.vacancy.isNullOrEmpty()) {
                holder.binding.llVacancySeats.visibility = View.VISIBLE
            } else {
                holder.binding.llVacancySeats.visibility = View.GONE
            }

            if (!model.age_limit.isNullOrEmpty()) {
                holder.binding.llAgeLimit.visibility = View.VISIBLE
            } else {
                holder.binding.llAgeLimit.visibility = View.GONE
            }

            if (!model.description.isNullOrEmpty()) {
                holder.binding.tvDescription.visibility = View.VISIBLE
                holder.binding.tvDescriptionTitle.visibility = View.GONE
                holder.binding.tvDescription.text =
                    Html.fromHtml(model.description, Html.FROM_HTML_MODE_COMPACT)
                holder.binding.tvDescription.movementMethod = LinkMovementMethod.getInstance()
            } else {
                holder.binding.tvDescription.visibility = View.GONE
                holder.binding.tvDescriptionTitle.visibility = View.GONE
            }

        } else {
            holder.binding.llMain.visibility = View.GONE
            holder.binding.ivAdvertisement.visibility = View.VISIBLE

            Glide.with(mContext)
                .load("https://lifeset.co.in/admin/img/client/" + examList[position].img)
                .into(holder.binding.ivAdvertisement)
            holder.binding.ivAdvertisement.setOnClickListener {
                val url = examList[position].advertise_link // Replace with your desired URL
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                mContext.startActivity(intent)
            }
        }

    }

    class MyViewHolder(val binding: AdpExamBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {

    }

    fun convertDateFormat(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date!!)
    }

    fun createdDateFormat(inputDate: String): String {
        val input = inputDate
        val correctedInput = input.uppercase(Locale.ENGLISH) // converts "am" to "AM"

        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a", Locale.ENGLISH)
        val outputFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH)

        val date = LocalDateTime.parse(correctedInput, inputFormatter)
        val formattedDate = date.format(outputFormatter)
        return formattedDate

    }

    fun getCurrentFormattedDate(): String {
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }
}