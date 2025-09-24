package com.app.lifeset.adapter

import android.content.Intent
import android.net.Uri
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.lifeset.activity.MainActivity
import com.app.lifeset.databinding.AdpMcqBinding
import com.app.lifeset.extensions.convertIsoToReadableDate
import com.app.lifeset.model.McqModel
import com.bumptech.glide.Glide

class McqAdapter(val mContext: MainActivity, val mcqList: ArrayList<McqModel>, val mainActivity: MainActivity)
    :RecyclerView.Adapter<McqAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            AdpMcqBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return mcqList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.model = mcqList[position]

        if (mcqList[position].type == "MCQ") {
            holder.binding.ivAdvertisement.visibility = View.GONE
            holder.binding.clMain.visibility = View.VISIBLE
            holder.binding.tvName.text = mcqList[position].objquestions
            val answers = mcqList[position].answer.split(";;")
            if(!mcqList[position].updated.isNullOrEmpty()){
                holder.binding.tvDate.text = convertIsoToReadableDate(mcqList[position].updated.toString())
            }
            // Set answers safely
            if (answers.isNotEmpty()) holder.binding.tvAnswer1.text = answers.getOrNull(0) ?: ""
            if (answers.size > 1) holder.binding.tvAnswer2.text = answers.getOrNull(1) ?: ""
            if (answers.size > 2) holder.binding.tvAnswer3.text = answers.getOrNull(2) ?: ""
            if (answers.size > 3) holder.binding.tvAnswer4.text = answers.getOrNull(3) ?: ""
            holder.binding.tvCategory.text = mcqList[position].category_name + " | " + mcqList[position]
                .sub_category_name + " | " + mcqList[position].section_name



        } else {
            holder.binding.ivAdvertisement.visibility = View.VISIBLE
            holder.binding.clMain.visibility = View.GONE
            Glide.with(mContext)
                .load("https://lifeset.co.in/admin/img/client/" + mcqList[position].img)
                .into(holder.binding.ivAdvertisement)
            holder.binding.ivAdvertisement.setOnClickListener {
                val url = mcqList[position].advertise_link // Replace with your desired URL
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                mContext.startActivity(intent)
            }
        }



    }

    class MyViewHolder(val binding:AdpMcqBinding):RecyclerView.ViewHolder(
        binding.root
    ){

    }

}