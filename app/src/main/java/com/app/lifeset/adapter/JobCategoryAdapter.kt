package com.app.lifeset.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.lifeset.R
import com.app.lifeset.activity.MainActivity
import com.app.lifeset.databinding.AdpJobListBinding
import com.app.lifeset.model.JobModel
import com.bumptech.glide.Glide

class JobCategoryAdapter(
    val mContext: MainActivity, val jobList: ArrayList<JobModel>,
    val onClick: onItemClick
) :
    RecyclerView.Adapter<JobCategoryAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            AdpJobListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    override fun getItemCount(): Int {
        return jobList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = jobList[position]
        holder.binding.model = model

        if (jobList[position].type.equals("Job") || jobList[position].type.equals("Internship")) {

            holder.binding.llMain.visibility = View.VISIBLE
            holder.binding.ivAdvertisement.visibility = View.GONE

            holder.binding.tvType.text = model.type

            holder.binding.tvTitle.text = "${model.title}"


            if (model.post_for?.isEmpty()==true){
                holder.binding.tvCompanyNameTitle.visibility = View.GONE
                holder.binding.tvCompanyTitle.visibility = View.GONE
            }else{
                holder.binding.tvCompanyNameTitle.visibility = View.VISIBLE
                holder.binding.tvCompanyTitle.visibility = View.VISIBLE
                holder.binding.tvCompanyTitle.text = model.post_for
            }

            if (model.industry?.isEmpty()==true){
                holder.binding.tvIndustryTitle.visibility = View.GONE
                holder.binding.tvIndustry.visibility = View.GONE
            }else{
                holder.binding.tvIndustryTitle.visibility = View.VISIBLE
                holder.binding.tvIndustry.visibility = View.VISIBLE
                holder.binding.tvIndustry.text = model.industry
            }

            if (model.functions?.isEmpty()==true){
                holder.binding.tvFunctionTitle.visibility = View.GONE
                holder.binding.tvFunctions.visibility = View.GONE
            }else{
                holder.binding.tvFunctionTitle.visibility = View.VISIBLE
                holder.binding.tvFunctions.visibility = View.VISIBLE
                holder.binding.tvFunctions.text = model.functions
            }

            if (model.role?.isEmpty()==true){
                holder.binding.tvRoleTitle.visibility = View.GONE
                holder.binding.tvRole.visibility = View.GONE
            }else{
                holder.binding.tvRoleTitle.visibility = View.VISIBLE
                holder.binding.tvRole.visibility = View.VISIBLE
                holder.binding.tvRole.text = model.role
            }


            if (model.experience?.isEmpty()==true){
                holder.binding.tvPastExperienceTitle.visibility = View.GONE
                holder.binding.tvPastExperience.visibility = View.GONE
            }else{
                holder.binding.tvPastExperienceTitle.visibility = View.VISIBLE
                holder.binding.tvPastExperience.visibility = View.VISIBLE
                holder.binding.tvPastExperience.text = model.experience
            }



            if (model.job_type?.isEmpty()==true){
                holder.binding.tvJobTypeTitle.visibility = View.GONE
                holder.binding.tvJobType.visibility = View.GONE
            }else{
                holder.binding.tvJobTypeTitle.visibility = View.VISIBLE
                holder.binding.tvJobType.visibility = View.VISIBLE
                holder.binding.tvJobType.text = model.job_type
            }

            if (model.client_to_manage?.isEmpty()==true){
                holder.binding.tvClientManageTitle.visibility = View.GONE
                holder.binding.tvClientManage.visibility = View.GONE
            }else{
                holder.binding.tvClientManageTitle.visibility = View.VISIBLE
                holder.binding.tvClientManage.visibility = View.VISIBLE
                holder.binding.tvClientManage.text = model.client_to_manage
            }


            if (model.capacity?.isEmpty()==true){
                holder.binding.tvCapacityTitle.visibility = View.GONE
                holder.binding.tvCapacity.visibility = View.GONE
            }else{
                holder.binding.tvCapacityTitle.visibility = View.VISIBLE
                holder.binding.tvCapacity.visibility = View.VISIBLE
                holder.binding.tvCapacity.text = model.capacity
            }


            if (model.fixed_salary?.isEmpty() == true) {
                holder.binding.tvFixedSalary.visibility = View.GONE
                holder.binding.tvYearlySalary.visibility = View.GONE
            } else {
                holder.binding.tvFixedSalary.visibility = View.VISIBLE
                holder.binding.tvYearlySalary.visibility = View.VISIBLE
                holder.binding.tvFixedSalary.text = model.fixed_salary
            }


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
                holder.binding.tvDescription.text = Html.fromHtml(model.description)
            }


            if (model.working_days?.isEmpty() == true){
                holder.binding.tvWorkingDaysTitle.visibility = View.GONE
                holder.binding.tvWorkingDays.visibility = View.GONE

            }else{
                holder.binding.tvWorkingDaysTitle.visibility = View.VISIBLE
                holder.binding.tvWorkingDays.visibility = View.VISIBLE
                holder.binding.tvWorkingDays.text = model.working_days
            }


            if (model.work_time?.isEmpty() == true){
                holder.binding.tvWorkTimeTitle.visibility = View.GONE
                holder.binding.tvWorkTime.visibility = View.GONE

            }else{
                holder.binding.tvWorkTimeTitle.visibility = View.VISIBLE
                holder.binding.tvWorkTime.visibility = View.VISIBLE
                holder.binding.tvWorkTime.text = model.work_time
            }


            holder.binding.ivShare.setOnClickListener {

            }

            holder.binding.ivBookMark.setOnClickListener {
                onClick.onBookMarkClick(position, model)
            }

            holder.binding.tvReadMore.setOnClickListener {
                onClick.onClick(model, position)
            }
        } else {
            holder.binding.llMain.visibility = View.GONE
            holder.binding.ivAdvertisement.visibility = View.VISIBLE
            Glide.with(mContext)
                .load("https://lifeset.co.in/admin/img/client/" + jobList[position].img)
                .into(holder.binding.ivAdvertisement)
            holder.binding.ivAdvertisement.setOnClickListener {
                val url = jobList[position].advertise_link // Replace with your desired URL
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                mContext.startActivity(intent)
            }
        }
    }

    class MyViewHolder(val binding: AdpJobListBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {

    }

    interface onItemClick {
        fun onClick(model: JobModel, position: Int)
        fun onBookMarkClick(position: Int, model: JobModel)
    }
}