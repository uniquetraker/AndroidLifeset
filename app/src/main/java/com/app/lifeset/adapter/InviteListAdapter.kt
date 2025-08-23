package com.app.lifeset.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.lifeset.R
import com.app.lifeset.activity.InviteListActivity
import com.app.lifeset.databinding.AdpInviteListBinding
import com.app.lifeset.model.InviteStudentModel
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class InviteListAdapter(
    val mContext: InviteListActivity, val inviteList: ArrayList<InviteStudentModel>,
    val onClick: onItemClick
) : RecyclerView.Adapter<InviteListAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            AdpInviteListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    override fun getItemCount(): Int {
        return inviteList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(mContext).load(inviteList[position].profile).placeholder(R.drawable.ic_profile)
            .error(R.drawable.ic_profile).into(holder.binding.cvProfile)
        holder.binding.tvName.text = inviteList[position].name
        holder.binding.tvSchoolName.text =
            inviteList[position].school_name + " " + "(" + inviteList[position].study_year + ")"

        if (inviteList[position].status == "Waiting") {
            holder.binding.rrStatus.visibility = View.VISIBLE
            holder.binding.tvAcceptDate.visibility = View.GONE
            holder.binding.tvDate.text = convertDateFormat(inviteList[position].date)
            holder.binding.rrMain.setBackgroundResource(R.drawable.selected_yellow)

        } else {
            holder.binding.rrStatus.visibility = View.GONE
            holder.binding.tvAcceptDate.visibility = View.VISIBLE
            holder.binding.tvAcceptDate.text = convertDateFormat(inviteList[position].date)
            holder.binding.rrMain.setBackgroundResource(R.drawable.selected_grey)
            holder.binding.cvProfile.borderColor = Color.GRAY
        }

        holder.itemView.setOnClickListener {
            onClick.onClick(position, inviteList[position])
        }
    }

    class MyViewHolder(val binding: AdpInviteListBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {

    }

    fun convertDateFormat(inputDate: String): String {
        // Define input and output date formats
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.getDefault())

        // Parse input date and format it to the desired format
        val date: Date? = inputFormat.parse(inputDate)
        val formattedDate = outputFormat.format(date!!)
        return formattedDate
    }

    interface onItemClick {
        fun onClick(position: Int, inviteStudentModel: InviteStudentModel)
    }
}