package com.app.lifeset.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.app.lifeset.R
import com.app.lifeset.databinding.ActivityJobDetailBinding
import com.app.lifeset.extensions.isNetworkAvailable
import com.app.lifeset.model.JobDetailModel
import com.app.lifeset.util.PrefManager
import com.app.lifeset.util.StaticData
import com.app.lifeset.viewmodel.JobViewModel
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class JobDetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityJobDetailBinding
    lateinit var mContext: JobDetailActivity
    private val viewModel: JobViewModel by viewModels()

    private var id: String = ""
    lateinit var firebaseAnalytics: FirebaseAnalytics


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_job_detail)
        mContext = this
        initUI()
        addListner()
        addObsereves()
    }

    private fun initUI() {
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        if (intent.extras != null) {
            id = intent.getStringExtra("id").toString()
        }



        if (isNetworkAvailable(mContext)) {
            viewModel.getJobDetail(
                PrefManager(mContext).getvalue(StaticData.id).toString(), id.toString()
            )
        } else {
            Toast.makeText(
                mContext, getString(R.string.str_error_internet_connections), Toast.LENGTH_SHORT
            ).show()
        }

    }


    private fun addListner() {
        binding.apply {
            ivBack.setOnClickListener {
                finish()
            }
            binding.ivBookMark.setOnClickListener {

                if (isNetworkAvailable(mContext)) {
                    if (PrefManager(mContext).getvalue(StaticData.profile_status).equals("pending")){
                        dialogUpdateProfile()
                    } else {
                        viewModel.addBookMark(
                            PrefManager(mContext).getvalue(StaticData.college_id).toString(),
                            PrefManager(mContext).getvalue(StaticData.id).toString(),
                            id.toString()
                        )
                    }
                } else {
                    Toast.makeText(
                        mContext,
                        getString(R.string.str_error_internet_connections),
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
            binding.tvApplyNow.setOnClickListener {
                if (binding.tvApplyNow.text.equals("Apply Now")) {
                    if (isNetworkAvailable(mContext)) {
                        if (PrefManager(mContext).getvalue(StaticData.profile_status).equals("pending")){
                            dialogUpdateProfile()
                        }else{
                            doApplyJob()
                        }

                    } else {
                        Toast.makeText(
                            mContext,
                            getString(R.string.str_error_internet_connections),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun doApplyJob() {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Are you sure you want to apply job?")

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            dialog.dismiss()
            firebaseAnalytics.logEvent(
                StaticData.jobApplyClicked, null
            )
            viewModel.applyJob(
                PrefManager(mContext).getvalue(StaticData.college_id).toString(),
                PrefManager(mContext).getvalue(StaticData.id).toString(),
                id.toString()
            )

        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            dialog.dismiss()
        }

        builder.show()
    }

    private fun dialogUpdateProfile() {
        val dialog = Dialog(mContext)
        dialog.setCancelable(true)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_updateprofile)
        val tvUpdateProfile = dialog.findViewById<MaterialTextView>(R.id.tvUpdateProfile)
        val tvWhatsappSupport = dialog.findViewById<MaterialTextView>(R.id.tvWhatsapp)
        tvUpdateProfile.setOnClickListener {
            startActivity(Intent(mContext, MyProfileActivity::class.java))
            dialog.dismiss()
        }
        tvWhatsappSupport.setOnClickListener {
            dialog.dismiss()
            val phoneNumber = "918448861216" // Add country code, no "+" sign
            val url = "https://wa.me/$phoneNumber"

            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                intent.setPackage("com.whatsapp") // Optional: ensures it opens in WhatsApp only
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(this, "WhatsApp is not installed.", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }


    private fun addObsereves() {
        viewModel.isLoading.observe(this, Observer {
            if (it) {
                binding.pbLoadData.visibility = View.VISIBLE
            } else {
                binding.pbLoadData.visibility = View.GONE
            }
        })

        viewModel.jobDetailLiveData.observe(this, Observer {
            if (it.status) {
                setData(it.datas)
            } else {
                Toast.makeText(mContext, it.msz, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.applyJobLiveData.observe(this, Observer {
            if (it.status) {
                Toast.makeText(mContext, it.msz, Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(mContext, it.msz, Toast.LENGTH_SHORT).show()
            }
        })


        viewModel.bookMarkLiveData.observe(this, Observer {
            if (it.status) {
                Toast.makeText(mContext, it.msz, Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(mContext, it.msz, Toast.LENGTH_SHORT).show()
            }
        })

    }

    @SuppressLint("SetTextI18n")
    private fun setData(jobDetailModel: JobDetailModel) {
        binding.apply {
            tvDate.text = jobDetailModel.dob
            if (jobDetailModel.is_bookmarked == "1") {
                binding.ivBookMark.setImageResource(R.drawable.ic_bookmarkfill)
            } else {
                binding.ivBookMark.setImageResource(R.drawable.ic_bookmark)
            }

            if (jobDetailModel.is_applied == "1") {
                binding.tvApplyNow.text = "Applied"
            } else {
                binding.tvApplyNow.text = "Apply Now"
            }

            binding.tvTitle.text = jobDetailModel.title
            binding.tvCompanyTitle.text = jobDetailModel.post_for
            binding.tvIndustryTitle.text = jobDetailModel.industry
            binding.tvFunctions.text = jobDetailModel.functions
            binding.tvRole.text = jobDetailModel.role
            binding.tvPastExperience.text = jobDetailModel.experience
            binding.tvJobType.text = jobDetailModel.jb_type
            binding.tvClientManage.text = jobDetailModel.client_manage
            binding.tvCapacity.text = jobDetailModel.capacity

            if (jobDetailModel.fixed_salary.isEmpty()) {
                binding.tvYearlySalary.visibility = View.GONE
                binding.tvSalary.visibility = View.GONE
            } else {
                binding.tvYearlySalary.visibility = View.VISIBLE
                binding.tvSalary.visibility = View.VISIBLE
                binding.tvSalary.text = jobDetailModel.fixed_salary

            }
            binding.tvWorkingDays.text = jobDetailModel.working_day
            binding.tvWorkTime.text = jobDetailModel.work_time
            if (jobDetailModel.description.isEmpty()) {
                binding.tvDescription.visibility = View.GONE
                binding.tvDescriptionTitle.visibility = View.GONE
            } else {
                binding.tvDescription.visibility = View.VISIBLE
                binding.tvDescriptionTitle.visibility = View.VISIBLE
                binding.tvDescription.text = Html.fromHtml(jobDetailModel.description)
            }


        }
    }
}