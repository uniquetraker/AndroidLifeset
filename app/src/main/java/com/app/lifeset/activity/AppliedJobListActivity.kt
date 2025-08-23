package com.app.lifeset.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.app.lifeset.R
import com.app.lifeset.adapter.AppliedJobAdapter
import com.app.lifeset.databinding.ActivityAppliedJobListBinding
import com.app.lifeset.extensions.isNetworkAvailable
import com.app.lifeset.model.JobModel
import com.app.lifeset.util.PrefManager
import com.app.lifeset.util.StaticData
import com.app.lifeset.viewmodel.JobViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AppliedJobListActivity : AppCompatActivity(), AppliedJobAdapter.onItemClick {

    lateinit var binding: ActivityAppliedJobListBinding
    lateinit var mContext: AppliedJobListActivity
    private val viewModel: JobViewModel by viewModels()
    private var jobList: ArrayList<JobModel> = arrayListOf()

    private var currentPosition = 0

    private var model: JobModel? = null
    private var bookMarkPosition: Int = 0
    var jobId: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_applied_job_list)
        mContext = this
        initUI()
        addListner()
        addObserves()
    }

    private fun initUI() {

        if (isNetworkAvailable(mContext)) {
            viewModel.getApplyJob(
                PrefManager(mContext).getvalue(StaticData.college_id).toString(),
                PrefManager(mContext).getvalue(StaticData.id).toString()
            )
        } else {
            Toast.makeText(
                mContext, getString(R.string.str_error_internet_connections),
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun addListner() {
        binding.apply {
            ivBack.setOnClickListener {
                finish()
            }

            tvPrevious.setOnClickListener {
                binding.rvJob.setCurrentItem(currentPosition - 1, true)
            }
            tvNext.setOnClickListener {
                binding.rvJob.setCurrentItem(currentPosition + 1, true)

            }
            binding.ivPrevious.setOnClickListener {
                binding.rvJob.setCurrentItem(currentPosition - 1, true)
            }
            binding.ivNext.setOnClickListener {
                binding.rvJob.setCurrentItem(currentPosition + 1, true)

            }

            binding.tvReadMore.setOnClickListener {
                startActivity(
                    Intent(mContext, JobDetailActivity::class.java)
                        .putExtra("id", jobId.toString())

                )

            }

        }
    }


    private fun addObserves() {

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
                model?.is_bookmarked = "1"
                binding.adapter?.notifyItemChanged(bookMarkPosition)
            } else {
                Toast.makeText(mContext, it.msz, Toast.LENGTH_SHORT).show()
            }
        })


        viewModel.applyjobListLiveData.observe(this, Observer {
            if (it.status) {
                jobList.clear()
                jobList = it.datas

                if (jobList.size > 0) {
                    binding.rvJob.visibility = View.VISIBLE
                    binding.tvNoDataFound.visibility = View.GONE
                    binding.rrNewBottom.visibility = View.VISIBLE
                    setAdapter(jobList)
                } else {
                    binding.rvJob.visibility = View.GONE
                    binding.tvNoDataFound.visibility = View.VISIBLE
                    binding.rrNewBottom.visibility = View.GONE
                }

            } else {
                binding.rvJob.visibility = View.GONE
                binding.tvNoDataFound.visibility = View.VISIBLE
                binding.rrNewBottom.visibility = View.GONE
            }
        })
    }

    private fun setAdapter(jobList: ArrayList<JobModel>) {
        binding.apply {
            adapter = AppliedJobAdapter(mContext, jobList, this@AppliedJobListActivity)
            rvJob.adapter = adapter
            adapter?.notifyDataSetChanged()
        }

        binding.rvJob.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPosition = position

                if (jobList.size > 0) {
                    jobId = jobList[position].id.toString()
                }

                if (currentPosition == 0) {
                    binding.ivPrevious.visibility = View.GONE
                } else {
                    binding.ivPrevious.visibility = View.VISIBLE

                }

                if (currentPosition == jobList.size - 1) {
                    binding.ivNext.visibility = View.GONE
                } else {
                    binding.ivNext.visibility = View.VISIBLE
                }

            }
        })
    }

    override fun onClick(model: JobModel) {

        startActivity(
            Intent(mContext, AppliedJobDetailActivity::class.java)
                .putExtra("id", model.id.toString())
        )

/*
        startActivityForResult(
            Intent(mContext, AppliedJobDetailActivity::class.java)
                .putExtra(StaticData.jobModel, Gson().toJson(model)), 201
        )
*/

    }

    override fun onBookMarkClick(position: Int, jobModel: JobModel) {
        if (isNetworkAvailable(mContext)) {
            if (jobModel?.is_bookmarked?.isEmpty() == true) {
                model = jobModel
                bookMarkPosition = position
                viewModel.addBookMark(
                    PrefManager(mContext).getvalue(StaticData.college_id).toString(),
                    PrefManager(mContext).getvalue(StaticData.id).toString(),
                    jobModel.id.toString()
                )
            } else {
                Toast.makeText(mContext, "Exam is already saved", Toast.LENGTH_SHORT).show()
            }

        } else {
            Toast.makeText(
                mContext, getString(R.string.str_error_internet_connections),
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 201) {
            if (resultCode == Activity.RESULT_OK) {
                if (isNetworkAvailable(mContext)) {
                    viewModel.getApplyJob(
                        PrefManager(mContext).getvalue(StaticData.college_id).toString(),
                        PrefManager(mContext).getvalue(StaticData.id).toString()
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

}