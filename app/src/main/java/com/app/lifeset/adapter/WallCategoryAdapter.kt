package com.app.lifeset.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.lifeset.R
import com.app.lifeset.activity.MainActivity
import com.app.lifeset.databinding.AdpWallCategoryBinding
import com.app.lifeset.model.WallCategoryModel

class WallCategoryAdapter(
    val mContext: MainActivity,
    val wallCategoryList: ArrayList<WallCategoryModel>,
    val onClick: onItemClick,
    val selectedPosition: Int
) : RecyclerView.Adapter<WallCategoryAdapter.MyViewHolder>() {

    var checkedPosition: Int = selectedPosition

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            AdpWallCategoryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    override fun getItemCount(): Int {
        return wallCategoryList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = wallCategoryList[position]

        holder.binding.tvName.text = model.name

        if (position == itemCount - 1) {
            holder.binding.view.visibility = View.GONE
        } else {
            holder.binding.view.visibility = View.VISIBLE
        }

        if (position == checkedPosition) {
            holder.binding.tvName.setTextColor(ContextCompat.getColor(mContext, R.color.black))
        } else {
            holder.binding.tvName.setTextColor(ContextCompat.getColor(mContext, R.color.darkyellow))
        }

        holder.itemView.setOnClickListener {
            checkedPosition = position
            onClick.onClick(position, model)
        }

    }

    class MyViewHolder(val binding: AdpWallCategoryBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {

    }

    interface onItemClick {
        fun onClick(pos: Int, model: WallCategoryModel)
    }
}