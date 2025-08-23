package com.app.lifeset.adapter

import android.content.Context
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.lifeset.R
import com.app.lifeset.activity.AppliedJobListActivity
import com.app.lifeset.activity.MainActivity
import com.app.lifeset.databinding.AdpAppliedJobListBinding
import com.app.lifeset.databinding.AdpJobListBinding
import com.app.lifeset.model.JobModel

class AppliedJobAdapter(
    val mContext: Context, val jobList: ArrayList<JobModel>,
    val onClick: onItemClick
) :
    RecyclerView.Adapter<AppliedJobAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            AdpAppliedJobListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    override fun getItemCount(): Int {
        return jobList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = jobList[position]
        holder.binding.model = model

        holder.binding.tvType.text = model.type

        holder.binding.tvTitle.text = "${model.title}"

        if (model.fixed_salary?.isEmpty() == true) {
            holder.binding.tvYearlySalary.visibility = View.GONE
            holder.binding.tvSalary.visibility = View.GONE
        } else {
            holder.binding.tvYearlySalary.visibility = View.VISIBLE
            holder.binding.tvSalary.visibility = View.VISIBLE
            holder.binding.tvSalary.text = model.fixed_salary
        }

        holder.binding.tvDescription.text = Html.fromHtml(model.description)

        if (model.is_bookmarked == "1") {
            holder.binding.ivBookMark.setImageResource(R.drawable.ic_bookmarkfill)
        } else {
            holder.binding.ivBookMark.setImageResource(R.drawable.ic_bookmark)
        }

        if (model.description?.isEmpty() == true) {
            holder.binding.tvDescription.visibility = View.GONE
            holder.binding.tvDescriptionTitle.visibility = View.GONE
        } else {
            holder.binding.tvDescription.visibility = View.VISIBLE
            holder.binding.tvDescriptionTitle.visibility = View.VISIBLE

        }



        holder.binding.ivShare.setOnClickListener {

        }

        holder.binding.ivBookMark.setOnClickListener {
            onClick.onBookMarkClick(position, model)
        }

        holder.binding.tvReadMore.setOnClickListener {
            onClick.onClick(model)
        }
    }

    class MyViewHolder(val binding: AdpAppliedJobListBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {

    }

    interface onItemClick {
        fun onClick(model: JobModel)
        fun onBookMarkClick(position: Int, model: JobModel)
    }
}