package com.app.lifeset.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.lifeset.R
import com.app.lifeset.adapter.FreelancerJobsAdapter
import com.app.lifeset.adapter.MyNetworkAdapter
import com.app.lifeset.databinding.ActivityFreelancerJobsBinding
import com.app.lifeset.extensions.isNetworkAvailable
import com.app.lifeset.model.FreelancerJobsModel
import com.app.lifeset.model.FreelancerStatusModel
import com.app.lifeset.util.PrefManager
import com.app.lifeset.util.StaticData
import com.app.lifeset.viewmodel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FreelancerJobsActivity : AppCompatActivity(), FreelancerJobsAdapter.onItemClick {

    lateinit var binding: ActivityFreelancerJobsBinding
    lateinit var mContext: FreelancerJobsActivity
    private var freelancerJobsModel: ArrayList<FreelancerJobsModel> = arrayListOf()
    private var freelancerStatusModel: ArrayList<FreelancerStatusModel> = arrayListOf()
    private val viewModel: ChatViewModel by viewModels()
    private var currentPage = 1
    private var isLoading = false
    private var totalPages = Int.MAX_VALUE // Update this from API response
    private var isLastPage = false
    lateinit var freelancerJobsAdapter: FreelancerJobsAdapter
    private var currentPosition: Int = 0
    private var newValue: String? =
        ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_freelancer_jobs)
        mContext = this
        initUI()
        addListner()
        viewModelObsereves()
    }

    private fun initUI() {

        if (isNetworkAvailable(mContext)) {
            viewModel.getFreelancerStatus()

        } else {
            Toast.makeText(
                mContext, getString(R.string.str_error_internet_connections),
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun fetchFreelancerJobsData() {
         viewModel.getFreelancerJobs(
             PrefManager(mContext).getvalue(StaticData.id).toString(),
             "",
             currentPage.toString()
         )

       /* viewModel.getFreelancerJobs(
            "83",
            "",
            currentPage.toString()
        )*/
    }

    private fun setupRecyclerView() {
        freelancerJobsAdapter =
            FreelancerJobsAdapter(
                mContext,
                freelancerJobsModel,
                freelancerStatusModel,
                this@FreelancerJobsActivity
            )
        binding.rvFreelancerJobs.apply {
            adapter = freelancerJobsAdapter
            layoutManager = LinearLayoutManager(context)

            // Add scroll listener for pagination
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    if (!isLoading && !isLastPage) {
                        if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount - 2
                            && firstVisibleItemPosition >= 0
                        ) {
                            currentPage++
                            loadMoreData()
                        }
                    }
                }
            })
        }
    }

    private fun loadMoreData() {
        if (isNetworkAvailable(mContext)) {
            isLoading = true
            binding.pbLoadData.visibility = View.VISIBLE
            fetchFreelancerJobsData()
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

        viewModel.freelancerUpdateStatusLiveData.observe(this, Observer {
            if (it.status) {
                Toast.makeText(mContext, it.msz, Toast.LENGTH_SHORT).show()
                freelancerJobsModel[currentPosition].student_status = newValue.toString()
                freelancerJobsAdapter?.notifyItemChanged(currentPosition)
            } else {
                Toast.makeText(mContext, it.msz, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.freelancerStatusLiveData.observe(this, Observer {
            if (!it.status) {
                freelancerStatusModel.clear()
                freelancerStatusModel.addAll(it.datas)
                isLoading = true
                currentPage = 1
                binding.pbLoadData.visibility = View.VISIBLE
                fetchFreelancerJobsData()
                setupRecyclerView()

            } else {
                Toast.makeText(mContext, it.msz, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.freelancerJobsLiveData.observe(this) { response ->
            isLoading = false
            binding.pbLoadData.visibility = View.GONE

            if (response.status) {
                if (currentPage == 1) {
                    freelancerJobsModel.clear()
                }

                val previousSize = freelancerJobsModel.size
                freelancerJobsModel.addAll(response.datas)

                if (currentPage == 1) {
                    freelancerJobsAdapter.notifyDataSetChanged()
                } else {
                    freelancerJobsAdapter.notifyItemRangeInserted(previousSize, response.datas.size)
                }

                updateVisibility()

                // Update pagination info
                response.total_page?.let {
                    totalPages = it
                    isLastPage = currentPage >= totalPages
                }
            } else {
                if (currentPage == 1) {
                    updateVisibility()
                }
            }
        }
    }

    private fun updateVisibility() {
        if (freelancerJobsModel.isEmpty()) {
            binding.tvNoDataFound.visibility = View.VISIBLE
            binding.rvFreelancerJobs.visibility = View.GONE
        } else {
            binding.rvFreelancerJobs.visibility = View.VISIBLE
            binding.tvNoDataFound.visibility = View.GONE
        }
    }


    private fun addListner() {
        binding.ivBack.setOnClickListener {
            finish()
        }
    }

    override fun onClick(position: Int, freelancerJobsModel: FreelancerJobsModel) {

        startActivity(
            Intent(mContext, JobDetailActivity::class.java)
                .putExtra("id", freelancerJobsModel.job_id)
        )

    }

    override fun updateJobStatusClick(
        position: Int,
        freelancerJobsModel: FreelancerJobsModel,
        value: String
    ) {
        if (isNetworkAvailable(mContext)) {
            currentPosition = position
            newValue = value
            viewModel.updateFreelancerJobStatus(
                PrefManager(mContext).getvalue(StaticData.id).toString(), freelancerJobsModel.id.toString(),
                value
            )
        } else {
            Toast.makeText(
                mContext, getString(R.string.str_error_internet_connections),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}