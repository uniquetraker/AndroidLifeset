package com.app.lifeset.adapter

import android.content.Intent
import android.net.Uri
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.app.lifeset.activity.MainActivity
import com.app.lifeset.adapter.GKAdapter.MyViewHolder
import com.app.lifeset.databinding.AdpGkBinding
import com.app.lifeset.databinding.AdpPersonalityBinding
import com.app.lifeset.extensions.createdDateFormat
import com.app.lifeset.model.PersonalityModel
import com.bumptech.glide.Glide

class PersonalityListAdapter(
    val mContext: MainActivity,
    val personalityList: ArrayList<PersonalityModel>,
    val onClick: onItemClick
) : RecyclerView.Adapter<PersonalityListAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            AdpPersonalityBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    override fun getItemCount(): Int {
        return personalityList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.model = personalityList[position]

        if (personalityList[position].post_type == "Personality") {
            holder.binding.ivAdvertisement.visibility = View.GONE
            holder.binding.clMain.visibility = View.VISIBLE


            if (!personalityList[position].image.isNullOrEmpty()) {
                holder.binding.ivImage.visibility = View.VISIBLE
                Glide.with(mContext)
                    .load("https://lifeset.co.in/img/Post/" + personalityList[position].image)
                    .into(holder.binding.ivImage)

            } else {
                holder.binding.ivImage.visibility = View.GONE
            }

            holder.binding.tvName.text = Html.fromHtml(personalityList[position].name)

            if (personalityList[position].personality_answer.equals("1")) {
                holder.binding.rbAnswer1.isChecked = true
                holder.binding.rbAnswer2.isChecked = false
                holder.binding.rbAnswer3.isChecked = false
                holder.binding.rbAnswer4.isChecked = false
                holder.binding.rbAnswer5.isChecked = false
            } else if (personalityList[position].personality_answer.equals("2")) {
                holder.binding.rbAnswer1.isChecked = false
                holder.binding.rbAnswer2.isChecked = true
                holder.binding.rbAnswer3.isChecked = false
                holder.binding.rbAnswer4.isChecked = false
                holder.binding.rbAnswer5.isChecked = false
            } else if (personalityList[position].personality_answer.equals("3")) {
                holder.binding.rbAnswer1.isChecked = false
                holder.binding.rbAnswer2.isChecked = false
                holder.binding.rbAnswer3.isChecked = true
                holder.binding.rbAnswer4.isChecked = false
                holder.binding.rbAnswer5.isChecked = false
            } else if (personalityList[position].personality_answer.equals("4")) {
                holder.binding.rbAnswer1.isChecked = false
                holder.binding.rbAnswer2.isChecked = false
                holder.binding.rbAnswer3.isChecked = false
                holder.binding.rbAnswer4.isChecked = true
                holder.binding.rbAnswer5.isChecked = false
            } else if (personalityList[position].personality_answer.equals("5")) {
                holder.binding.rbAnswer1.isChecked = false
                holder.binding.rbAnswer2.isChecked = false
                holder.binding.rbAnswer3.isChecked = false
                holder.binding.rbAnswer4.isChecked = false
                holder.binding.rbAnswer5.isChecked = true
            }

       /*     holder.binding.rbAnswer1.setOnCheckedChangeListener { compoundButton, b ->
                if (b) {
                    onClick.onAnswer1Click(
                        position,
                        personalityList[position],
                        holder.binding.rbAnswer1,
                        holder.binding.rbAnswer2,
                        holder.binding.rbAnswer3,
                        holder.binding.rbAnswer4,
                        holder.binding.rbAnswer5
                    )
                }
            }


            holder.binding.rbAnswer2.setOnCheckedChangeListener { compoundButton, b ->
                if (b) {
                    onClick.onAnswer2Click(
                        position,
                        personalityList[position],
                        holder.binding.rbAnswer1,
                        holder.binding.rbAnswer2,
                        holder.binding.rbAnswer3,
                        holder.binding.rbAnswer4,
                        holder.binding.rbAnswer5
                    )
                }
            }



            holder.binding.rbAnswer3.setOnCheckedChangeListener { compoundButton, b ->
                if (b) {
                    onClick.onAnswer3Click(
                        position,
                        personalityList[position],
                        holder.binding.rbAnswer1,
                        holder.binding.rbAnswer2,
                        holder.binding.rbAnswer3,
                        holder.binding.rbAnswer4,
                        holder.binding.rbAnswer5
                    )
                }
            }

            holder.binding.rbAnswer4.setOnCheckedChangeListener { compoundButton, b ->
                if (b) {
                    onClick.onAnswer4Click(
                        position,
                        personalityList[position],
                        holder.binding.rbAnswer1,
                        holder.binding.rbAnswer2,
                        holder.binding.rbAnswer3,
                        holder.binding.rbAnswer4,
                        holder.binding.rbAnswer5
                    )
                }
            }


            holder.binding.rbAnswer5.setOnCheckedChangeListener { compoundButton, b ->
                if (b) {
                    onClick.onAnswer5Click(
                        position,
                        personalityList[position],
                        holder.binding.rbAnswer1,
                        holder.binding.rbAnswer2,
                        holder.binding.rbAnswer3,
                        holder.binding.rbAnswer4,
                        holder.binding.rbAnswer5
                    )
                }
            }

*/




            holder.binding.llAnswer1.setOnClickListener {
                onClick.onAnswer1Click(
                    position,
                    personalityList[position],
                    holder.binding.rbAnswer1,
                    holder.binding.rbAnswer2,
                    holder.binding.rbAnswer3,
                    holder.binding.rbAnswer4,
                    holder.binding.rbAnswer5
                )
            }

            holder.binding.llAnswer2.setOnClickListener {
                onClick.onAnswer2Click(
                    position,
                    personalityList[position],
                    holder.binding.rbAnswer1,
                    holder.binding.rbAnswer2,
                    holder.binding.rbAnswer3,
                    holder.binding.rbAnswer4,
                    holder.binding.rbAnswer5
                )
            }
            holder.binding.llAnswer3.setOnClickListener {
                onClick.onAnswer3Click(
                    position,
                    personalityList[position],
                    holder.binding.rbAnswer1,
                    holder.binding.rbAnswer2,
                    holder.binding.rbAnswer3,
                    holder.binding.rbAnswer4,
                    holder.binding.rbAnswer5
                )
            }

            holder.binding.llAnswer4.setOnClickListener {
                onClick.onAnswer4Click(
                    position,
                    personalityList[position],
                    holder.binding.rbAnswer1,
                    holder.binding.rbAnswer2,
                    holder.binding.rbAnswer3,
                    holder.binding.rbAnswer4,
                    holder.binding.rbAnswer5
                )
            }

            holder.binding.llAnswer5.setOnClickListener {
                onClick.onAnswer5Click(
                    position,
                    personalityList[position],
                    holder.binding.rbAnswer1,
                    holder.binding.rbAnswer2,
                    holder.binding.rbAnswer3,
                    holder.binding.rbAnswer4,
                    holder.binding.rbAnswer5
                )
            }


        } else {
            holder.binding.ivAdvertisement.visibility = View.VISIBLE
            holder.binding.clMain.visibility = View.GONE
            Glide.with(mContext)
                .load("https://lifeset.co.in/admin/img/client/" + personalityList[position].img)
                .into(holder.binding.ivAdvertisement)
            holder.binding.ivAdvertisement.setOnClickListener {
                val url = personalityList[position].advertise_link // Replace with your desired URL
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                mContext.startActivity(intent)
            }
        }


    }

    class MyViewHolder(val binding: AdpPersonalityBinding) : RecyclerView.ViewHolder(
        binding.root
    ) {

    }

    interface onItemClick {
        fun onAnswer1Click(
            position: Int,
            model: PersonalityModel,
            llAnswer1: RadioButton,
            llAnswer2: RadioButton,
            llAnswer3: RadioButton,
            llAnswer4: RadioButton,
            llAnswer5: RadioButton
        )

        fun onAnswer2Click(
            position: Int,
            model: PersonalityModel,
            llAnswer1: RadioButton,
            llAnswer2: RadioButton,
            llAnswer3: RadioButton,
            llAnswer4: RadioButton,
            llAnswer5: RadioButton
        )

        fun onAnswer3Click(
            position: Int,
            model: PersonalityModel,
            llAnswer1: RadioButton,
            llAnswer2: RadioButton,
            llAnswer3: RadioButton,
            llAnswer4: RadioButton,
            llAnswer5: RadioButton
        )

        fun onAnswer4Click(
            position: Int,
            model: PersonalityModel,
            llAnswer1: RadioButton,
            llAnswer2: RadioButton,
            llAnswer3: RadioButton,
            llAnswer4: RadioButton,
            llAnswer5: RadioButton
        )

        fun onAnswer5Click(
            position: Int,
            model: PersonalityModel,
            llAnswer1: RadioButton,
            llAnswer2: RadioButton,
            llAnswer3: RadioButton,
            llAnswer4: RadioButton,
            llAnswer5: RadioButton
        )
    }
}