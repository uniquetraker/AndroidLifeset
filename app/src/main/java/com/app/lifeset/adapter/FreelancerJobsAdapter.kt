package com.app.lifeset.adapter

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.app.lifeset.activity.FreelancerJobsActivity
import com.app.lifeset.databinding.AdpFreelancerJobsBinding
import com.app.lifeset.model.FreelancerJobsModel
import com.app.lifeset.model.FreelancerStatusModel
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


class FreelancerJobsAdapter(
    val mContext: FreelancerJobsActivity,
    val freelancerJobsModel: ArrayList<FreelancerJobsModel>,
    val statusModel: ArrayList<FreelancerStatusModel>,
    val onClick: onItemClick
) : RecyclerView.Adapter<FreelancerJobsAdapter.MyViewHolder>() {

    private var statusList: ArrayList<String>? = arrayListOf()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FreelancerJobsAdapter.MyViewHolder {
        return MyViewHolder(
            AdpFreelancerJobsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    override fun getItemCount(): Int {
        return freelancerJobsModel.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FreelancerJobsAdapter.MyViewHolder, position: Int) {
        holder.binding.tvName.text = freelancerJobsModel[position].company_name
        holder.binding.tvEmail.text = "Email : " + freelancerJobsModel[position].company_email
        holder.binding.tvMobile.text = "Mobile : " + freelancerJobsModel[position].company_mobile
        holder.binding.tvDate.text =
            "Date : " + convertDateFormat(freelancerJobsModel[position].updated_at)
        holder.binding.tvDesc.text = freelancerJobsModel[position].request_for

        holder.binding.acStatus.setText(freelancerJobsModel[position].student_status)

        holder.binding.tvEmail.setPaintFlags(holder.binding.tvEmail.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        holder.binding.tvMobile.setPaintFlags(holder.binding.tvMobile.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        holder.binding.tvDesc.setPaintFlags(holder.binding.tvDesc.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)

        holder.binding.acStatus.setOnTouchListener { _, _ ->
            holder.binding.acStatus.showDropDown()
            false
        }

        for (i in 0..<statusModel.size) {
            statusList?.add(statusModel[i].value)
        }

        val arrayAdapter: CustomArrayAdapter =
            CustomArrayAdapter(mContext, statusList)
        holder.binding.acStatus.threshold = 0
        holder.binding.acStatus.dropDownVerticalOffset = 0
        holder.binding.acStatus.setAdapter(arrayAdapter)
        holder.binding.acStatus.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, i, id ->

                onClick.updateJobStatusClick(
                    position, freelancerJobsModel = freelancerJobsModel[position],
                    value = statusModel[i].value
                )

            }


        holder.itemView.setOnClickListener {
            onClick.onClick(position, freelancerJobsModel = freelancerJobsModel[position])
        }

    }

    class MyViewHolder(val binding: AdpFreelancerJobsBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {

    }

    fun convertDateFormat(inputDate: String): String {
        // Parse the input date string
        val zonedDateTime = ZonedDateTime.parse(inputDate)

        // Define the output format as dd/MM/yyyy
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        // Format and return the date
        return zonedDateTime.format(formatter)
    }

    interface onItemClick {
        fun onClick(position: Int, freelancerJobsModel: FreelancerJobsModel)
        fun updateJobStatusClick(
            position: Int, freelancerJobsModel: FreelancerJobsModel,
            value: String
        )
    }

}