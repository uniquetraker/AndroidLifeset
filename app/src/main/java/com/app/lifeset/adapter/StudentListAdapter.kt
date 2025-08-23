package com.app.lifeset.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.lifeset.R
import com.app.lifeset.activity.StudentListActivity
import com.app.lifeset.databinding.AdpStudentlistBinding
import com.app.lifeset.model.ChatStudentModel
import com.bumptech.glide.Glide

class StudentListAdapter(
    val mContext: StudentListActivity,
    val studentList: ArrayList<ChatStudentModel>,
    val onClick: onItemClick
) : RecyclerView.Adapter<StudentListAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            AdpStudentlistBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (!studentList[position].type.equals("advertisement")) {
            holder.binding.llMain.visibility = View.VISIBLE
            holder.binding.cvProfile.visibility = View.VISIBLE
            holder.binding.tvSendInvite.visibility = View.VISIBLE
            holder.binding.ivAdvertisement.visibility = View.GONE
            Glide.with(mContext).load(studentList[position].profile)
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_profile)
                .into(holder.binding.cvProfile)
            holder.binding.tvStudyAt.text = "Native of " + studentList[position].location
            holder.binding.tvName.text =
                studentList[position].name + " " + "(" + studentList[position].study_year + ")"
            holder.binding.tvCollageName.text =
                studentList[position].course_name + " from " + studentList[position].school_name
            holder.binding.tvInterest.text = studentList[position].interest
            holder.binding.tvSkills.text = studentList[position].skills

            if (studentList[position].interest?.isEmpty() == true && studentList[position].skills?.isEmpty() == true) {
                holder.binding.llInterest.visibility = View.GONE
            } else {
                holder.binding.llInterest.visibility = View.VISIBLE
            }

            if (studentList[position].status == "New") {
                holder.binding.tvSendInvite.text = "Send Invite"
            } else if (studentList[position].status == "Waiting") {
                holder.binding.tvSendInvite.text = "Invitation Sent"
            } else if (studentList[position].status == "Accepted") {
                holder.binding.tvSendInvite.text = "My Network"
            } else if (studentList[position].status == "Cancelled") {
                holder.binding.tvSendInvite.text = "Invitation Cancelled"
            }

            holder.binding.tvSendInvite.setOnClickListener {
                if (studentList[position].status == "New") {
                    onClick.onClick(position, studentList[position])
                }
            }
        } else {
            holder.binding.llMain.visibility = View.GONE
            holder.binding.cvProfile.visibility = View.GONE
            holder.binding.tvSendInvite.visibility = View.GONE
            holder.binding.ivAdvertisement.visibility = View.VISIBLE
            Glide.with(mContext)
                .load("https://lifeset.co.in/admin/img/client/" + studentList[position].img)
                .into(holder.binding.ivAdvertisement)
            holder.binding.ivAdvertisement.setOnClickListener {
                val url = studentList[position].advertise_link // Replace with your desired URL
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                mContext.startActivity(intent)
            }
        }

    }

    class MyViewHolder(val binding: AdpStudentlistBinding) : RecyclerView.ViewHolder(
        binding.root
    )

    interface onItemClick {
        fun onClick(position: Int, studentModel: ChatStudentModel)
    }
}