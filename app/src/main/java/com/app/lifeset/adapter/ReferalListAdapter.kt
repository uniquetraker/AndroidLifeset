package com.app.lifeset.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.lifeset.R
import com.app.lifeset.activity.ReferalListActivity
import com.app.lifeset.databinding.AdpReferalListBinding
import com.app.lifeset.model.ReferalModel

class ReferalListAdapter(
    val mContext: ReferalListActivity,
    val referalList: ArrayList<ReferalModel>,
    val onClick:onItemClick
) : RecyclerView.Adapter<ReferalListAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            AdpReferalListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return referalList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = referalList[position]
        holder.binding.tvMobileNumber.text = model.mobile
        holder.binding.tvDate.text = model.date
        holder.binding.tvStatus.text = model.downloaded
        if (model.downloaded == "Web & Mobile") {
            holder.binding.ivClose.visibility=View.GONE
            holder.binding.tvStatus.setTextColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.greencolor
                )
            )
        } else if (model.downloaded == "Mobile Only" || model.downloaded == "Web Only") {
            holder.binding.ivClose.visibility=View.GONE
            holder.binding.tvStatus.setTextColor(
                ContextCompat.getColor(
                    mContext,
                    R.color.darkyellow
                )
            )
        } else {
            holder.binding.ivClose.visibility=View.VISIBLE
            holder.binding.tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.redcolor))
        }

        holder.binding.ivClose.setOnClickListener {
            onClick.onClick(position,referalList[position])
        }
    }

    class MyViewHolder(val binding: AdpReferalListBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {

    }

    interface onItemClick{
        fun onClick(position: Int,model: ReferalModel)
    }
}