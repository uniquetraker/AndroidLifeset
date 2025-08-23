package com.app.lifeset.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.lifeset.activity.MyProfileActivity
import com.app.lifeset.databinding.AdpAppliedJobListBinding
import com.app.lifeset.databinding.AdpCoursedetailBinding
import com.app.lifeset.model.CourseDetail

class CourseDetailAdapter(
    val mContext: MyProfileActivity,
    val coursedetail: ArrayList<CourseDetail>
) : RecyclerView.Adapter<CourseDetailAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            AdpCoursedetailBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    override fun getItemCount(): Int {
        return coursedetail.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = coursedetail[position]
        holder.binding.tvYear.text = model.edu_year
        holder.binding.tvPassingYear.text = model.edu_passyear
        holder.binding.tvPercentage.text = model.edu_percentage

        if (position == itemCount - 1) {
            holder.binding.view.visibility = View.GONE
        } else {
            holder.binding.view.visibility = View.VISIBLE
        }
    }

    class MyViewHolder(val binding: AdpCoursedetailBinding) : RecyclerView.ViewHolder(
        binding.root
    )
}