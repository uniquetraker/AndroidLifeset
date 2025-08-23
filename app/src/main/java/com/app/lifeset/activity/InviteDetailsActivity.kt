package com.app.lifeset.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.app.lifeset.R
import com.app.lifeset.databinding.ActivityInviteDetailsBinding
import com.app.lifeset.extensions.isNetworkAvailable
import com.app.lifeset.model.InviteStudentModel
import com.app.lifeset.util.PrefManager
import com.app.lifeset.util.StaticData
import com.app.lifeset.viewmodel.ChatViewModel
import com.bumptech.glide.Glide
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InviteDetailsActivity : AppCompatActivity() {

    lateinit var binding: ActivityInviteDetailsBinding
    lateinit var mContext: InviteDetailsActivity
    lateinit var model: InviteStudentModel
    private val viewModel: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_invite_details)
        mContext = this
        initUI()
        addListner()
    }

    private fun initUI() {
        if (intent.extras != null) {
            model = Gson().fromJson(intent.getStringExtra("model"), InviteStudentModel::class.java)
            setData(model)
        }
        viewModelObsereves()
    }

    private fun addListner() {
        binding.apply {
            ivBack.setOnClickListener {
                finish()
            }

            tvAccept.setOnClickListener {
                acceptDialog(model.invite_id)
            }
            tvCancel.setOnClickListener {
                cancelDialog(model.invite_id)
            }
        }
    }

    private fun setData(model: InviteStudentModel) {
        Glide.with(mContext).load(model.profile)
            .placeholder(R.drawable.ic_profile).error(R.drawable.ic_profile)
            .into(binding.cvProfile)

        binding.tvStudyAt.text = "Native of " + model.study_at
        binding.tvName.text =
            model.name + " " + "(" + model.study_year + ")"
        binding.tvCollageName.text =
            model.course_name + " from " + model.school_name
        binding.tvInterest.text = model.interest
        binding.tvSkills.text = model.skills

        if (model.interest.isEmpty() && model.skills.isEmpty()) {
            binding.llInterest.visibility = View.GONE
        } else {
            binding.llInterest.visibility = View.VISIBLE
        }


    }

    private fun viewModelObsereves() {
        viewModel.isLoading.observe(this, Observer {
            if (it) {
                binding.pbLoadData.visibility = View.VISIBLE
            } else {
                binding.pbLoadData.visibility = View.GONE
            }
        })
        viewModel.inviteStatusLiveData.observe(this, Observer {
            if (it.status) {
                Toast.makeText(mContext, it.msz, Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK)
                finish()
            } else {
                Toast.makeText(mContext, it.msz, Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun acceptDialog(inviteId: Int) {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Confirmation")
        builder.setMessage("Are you sure you want to Accept Invite?")

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            if (isNetworkAvailable(mContext)) {
                dialog.dismiss()
                viewModel.inviteStatus(
                    PrefManager(mContext).getvalue(StaticData.id).toString(),
                    inviteId.toString(), "Accept"
                )
            } else {
                Toast.makeText(
                    mContext, getString(R.string.str_error_internet_connections),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            dialog.dismiss()
        }

        builder.show()
    }

    private fun cancelDialog(inviteId: Int) {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Confirmation")
        builder.setMessage("Are you sure you want to Cancel Invite?")

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            if (isNetworkAvailable(mContext)) {
                dialog.dismiss()
                viewModel.inviteStatus(
                    PrefManager(mContext).getvalue(StaticData.id).toString(),
                    inviteId.toString(), "Cancel"
                )
            } else {
                Toast.makeText(
                    mContext, getString(R.string.str_error_internet_connections),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            dialog.dismiss()
        }

        builder.show()
    }
}