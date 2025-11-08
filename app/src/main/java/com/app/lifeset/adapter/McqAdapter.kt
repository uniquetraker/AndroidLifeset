package com.app.lifeset.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.app.lifeset.activity.MainActivity
import com.app.lifeset.databinding.AdpMcqBinding
import com.app.lifeset.extensions.convertIsoToReadableDate
import com.app.lifeset.model.McqModel
import com.bumptech.glide.Glide

class McqAdapter(
    val mContext: MainActivity, val mcqList: ArrayList<McqModel>, val mainActivity: MainActivity,
    val onClick: onItemClick
) : RecyclerView.Adapter<McqAdapter.MyViewHolder>() {
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
            if (!mcqList[position].updated.isNullOrEmpty()) {
                holder.binding.tvDate.text =
                    convertIsoToReadableDate(mcqList[position].updated.toString())
            }
            // Set answers safely
            if (answers.isNotEmpty()) holder.binding.tvAnswer1.text = answers.getOrNull(0) ?: ""
            if (answers.size > 1) holder.binding.tvAnswer2.text = answers.getOrNull(1) ?: ""
            if (answers.size > 2) holder.binding.tvAnswer3.text = answers.getOrNull(2) ?: ""
            if (answers.size > 3) holder.binding.tvAnswer4.text = answers.getOrNull(3) ?: ""
            holder.binding.tvCategory.text =
                mcqList[position].category_name + " | " + mcqList[position]
                    .sub_category_name + " | " + mcqList[position].section_name

            if (!mcqList[position].response.isNullOrEmpty()) {
                if (answers.getOrNull(0).equals(mcqList[position].response)) {
                    holder.binding.rbAnswer1.isChecked = true
                    holder.binding.rbAnswer2.isChecked = false
                    holder.binding.rbAnswer3.isChecked = false
                    holder.binding.rbAnswer4.isChecked = false
                } else if (answers.getOrNull(1).equals(mcqList[position].response)) {
                    holder.binding.rbAnswer1.isChecked = false
                    holder.binding.rbAnswer2.isChecked = true
                    holder.binding.rbAnswer3.isChecked = false
                    holder.binding.rbAnswer4.isChecked = false
                } else if (answers.getOrNull(2).equals(mcqList[position].response)) {
                    holder.binding.rbAnswer1.isChecked = false
                    holder.binding.rbAnswer2.isChecked = false
                    holder.binding.rbAnswer3.isChecked = true
                    holder.binding.rbAnswer4.isChecked = false
                } else if (answers.getOrNull(3).equals(mcqList[position].response)) {
                    holder.binding.rbAnswer1.isChecked = false
                    holder.binding.rbAnswer2.isChecked = false
                    holder.binding.rbAnswer3.isChecked = false
                    holder.binding.rbAnswer4.isChecked = true
                }
            }

            // Remove any existing listener before changing the state
            holder.binding.rbAnswer1.setOnCheckedChangeListener(null)
            holder.binding.rbAnswer2.setOnCheckedChangeListener(null)
            holder.binding.rbAnswer3.setOnCheckedChangeListener(null)
            holder.binding.rbAnswer4.setOnCheckedChangeListener(null)


            holder.binding.rbAnswer1.setOnCheckedChangeListener { compoundButton, b ->
                if (b) {
                    onClick.onAnswer1Click(
                        position,
                        mcqList[position],
                        answers.getOrNull(0) ?: "",
                        holder.binding.rbAnswer1,
                        holder.binding.rbAnswer2,
                        holder.binding.rbAnswer3,
                        holder.binding.rbAnswer4
                    )
                }
            }
            holder.binding.llAnswer1.setOnClickListener {
                onClick.onAnswer1Click(
                    position,
                    mcqList[position],
                    answers.getOrNull(0) ?: "",
                    holder.binding.rbAnswer1,
                    holder.binding.rbAnswer2,
                    holder.binding.rbAnswer3,
                    holder.binding.rbAnswer4
                )
            }
            holder.binding.rbAnswer2.setOnCheckedChangeListener { compoundButton, b ->
                if (b) {
                    onClick.onAnswer2Click(
                        position,
                        mcqList[position],
                        answers.getOrNull(1) ?: "",
                        holder.binding.rbAnswer1,
                        holder.binding.rbAnswer2,
                        holder.binding.rbAnswer3,
                        holder.binding.rbAnswer4
                    )
                }
            }
            holder.binding.llAnswer2.setOnClickListener {
                onClick.onAnswer2Click(
                    position,
                    mcqList[position],
                    answers.getOrNull(1) ?: "",
                    holder.binding.rbAnswer1,
                    holder.binding.rbAnswer2,
                    holder.binding.rbAnswer3,
                    holder.binding.rbAnswer4
                )
            }
            holder.binding.rbAnswer3.setOnCheckedChangeListener { compoundButton, b ->
                if (b) {
                    onClick.onAnswer3Click(
                        position,
                        mcqList[position],
                        answers.getOrNull(2) ?: "",
                        holder.binding.rbAnswer1,
                        holder.binding.rbAnswer2,
                        holder.binding.rbAnswer3,
                        holder.binding.rbAnswer4
                    )
                }
            }
            holder.binding.llAnswer3.setOnClickListener {
                onClick.onAnswer3Click(
                    position,
                    mcqList[position],
                    answers.getOrNull(2) ?: "",
                    holder.binding.rbAnswer1,
                    holder.binding.rbAnswer2,
                    holder.binding.rbAnswer3,
                    holder.binding.rbAnswer4
                )
            }
            holder.binding.rbAnswer4.setOnCheckedChangeListener { compoundButton, b ->
                if (b) {
                    onClick.onAnswer4Click(
                        position,
                        mcqList[position],
                        answers.getOrNull(3) ?: "",
                        holder.binding.rbAnswer1,
                        holder.binding.rbAnswer2,
                        holder.binding.rbAnswer3,
                        holder.binding.rbAnswer4
                    )
                }
            }
            holder.binding.llAnswer4.setOnClickListener {
                onClick.onAnswer4Click(
                    position,
                    mcqList[position],
                    answers.getOrNull(3) ?: "",
                    holder.binding.rbAnswer1,
                    holder.binding.rbAnswer2,
                    holder.binding.rbAnswer3,
                    holder.binding.rbAnswer4
                )
            }
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

    class MyViewHolder(val binding: AdpMcqBinding) : RecyclerView.ViewHolder(
        binding.root
    )

    interface onItemClick {
        fun onAnswer1Click(
            position: Int,
            model: McqModel,
            answer: String,
            llAnswer1: RadioButton,
            llAnswer2: RadioButton,
            llAnswer3: RadioButton,
            llAnswer4: RadioButton
        )

        fun onAnswer2Click(
            position: Int,
            model: McqModel,
            answer: String,
            llAnswer1: RadioButton,
            llAnswer2: RadioButton,
            llAnswer3: RadioButton,
            llAnswer4: RadioButton
        )

        fun onAnswer3Click(
            position: Int,
            model: McqModel,
            answer: String,
            llAnswer1: RadioButton,
            llAnswer2: RadioButton,
            llAnswer3: RadioButton,
            llAnswer4: RadioButton
        )

        fun onAnswer4Click(
            position: Int,
            model: McqModel,
            answer: String,
            llAnswer1: RadioButton,
            llAnswer2: RadioButton,
            llAnswer3: RadioButton,
            llAnswer4: RadioButton
        )
    }
}