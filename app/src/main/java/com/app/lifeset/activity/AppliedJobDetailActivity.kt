package com.app.lifeset.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.app.lifeset.R
import com.app.lifeset.databinding.ActivityAppliedJobDetailBinding
import com.app.lifeset.databinding.ActivityAppliedJobListBinding
import com.app.lifeset.extensions.isNetworkAvailable
import com.app.lifeset.model.JobDetailModel
import com.app.lifeset.model.JobModel
import com.app.lifeset.util.PrefManager
import com.app.lifeset.util.StaticData
import com.app.lifeset.viewmodel.JobViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppliedJobDetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityAppliedJobDetailBinding
    lateinit var mContext: AppliedJobDetailActivity
    private val viewModel: JobViewModel by viewModels()
    private var id: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_applied_job_detail)
        mContext = this
        initUI()
        addListner()
        addObsereves()
    }

    private fun initUI() {
        if (intent.extras != null) {
            id = intent.getStringExtra("id").toString()
        }

        if (isNetworkAvailable(mContext)) {
            viewModel.getJobDetail(
                PrefManager(mContext).getvalue(StaticData.id).toString(),
                id.toString()
            )
        } else {
            Toast.makeText(
                mContext,
                getString(R.string.str_error_internet_connections),
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun addListner() {
        binding.apply {
            ivBack.setOnClickListener {
                finish()
            }
            ivBookMark.setOnClickListener {
                if (isNetworkAvailable(mContext)) {
                    viewModel.addBookMark(
                        PrefManager(mContext).getvalue(StaticData.college_id).toString(),
                        PrefManager(mContext).getvalue(StaticData.id).toString(),
                        id.toString()
                    )
                } else {
                    Toast.makeText(
                        mContext, getString(R.string.str_error_internet_connections),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }


    private fun addObsereves() {
        viewModel.isLoading.observe(this, Observer {
            if (it) {
                binding.pbLoadData.visibility = View.VISIBLE
            } else {
                binding.pbLoadData.visibility = View.GONE
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

        viewModel.jobDetailLiveData.observe(this, Observer {
            if (it.status) {
                setData(it.datas)
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