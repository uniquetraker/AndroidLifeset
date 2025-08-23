package com.app.lifeset.activity

import android.app.Activity
import android.app.ComponentCaller
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.lifeset.R
import com.app.lifeset.adapter.InviteListAdapter
import com.app.lifeset.databinding.ActivityInviteListBinding
import com.app.lifeset.extensions.isNetworkAvailable
import com.app.lifeset.model.InviteStudentModel
import com.app.lifeset.util.PrefManager
import com.app.lifeset.util.StaticData
import com.app.lifeset.viewmodel.ChatViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InviteListActivity : AppCompatActivity(), InviteListAdapter.onItemClick {

    lateinit var binding: ActivityInviteListBinding
    lateinit var mContext: InviteListActivity
    private val viewModel: ChatViewModel by viewModels()
    private var inviteList: ArrayList<InviteStudentModel> = arrayListOf()
    private lateinit var inviteAdapter: InviteListAdapter
    private var currentPage = 1
    private var isLoading = false
    private var totalPages = Int.MAX_VALUE // Update this from API response
    private var isLastPage = false
    lateinit var firebaseAnalytics: FirebaseAnalytics


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_invite_list)
        mContext = this
        initUI()
        addListner()

    }

    private fun initUI() {
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        if (isNetworkAvailable(mContext)) {
            isLoading = true
            currentPage = 1
            binding.pbLoadData.visibility = View.VISIBLE
            fetchInviteData()

        } else {
            Toast.makeText(
                mContext, getString(R.string.str_error_internet_connections),
                Toast.LENGTH_SHORT
            ).show()
        }

        setupRecyclerView()

        viewModelObsereves()
    }

    private fun addListner() {
        binding.apply {
            ivBack.setOnClickListener {
                startActivity(Intent(mContext,HomeActivity::class.java))
                finish()
            }
            tvStudent.setOnClickListener {
                firebaseAnalytics.logEvent(
                    StaticData.networkingClicked, null
                )
                startActivity(Intent(mContext, StudentListActivity::class.java))
            }
            tvNetworks.setOnClickListener {
                firebaseAnalytics.logEvent(
                    StaticData.myNetworksClicked, null
                )
                startActivity(Intent(mContext, MyNetworkActivity::class.java))
            }
            tvChats.setOnClickListener {
                startActivity(Intent(mContext, ChatListActivity::class.java))
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(mContext,HomeActivity::class.java))
        finish()
    }

    private fun fetchInviteData() {
        viewModel.getInviteList(
            PrefManager(mContext).getvalue(StaticData.id).toString(),
            currentPage.toString()
        )
    }

    private fun setupRecyclerView() {
        inviteAdapter = InviteListAdapter(mContext, inviteList, this@InviteListActivity)
        binding.rvInvite.apply {
            adapter = inviteAdapter
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
            fetchInviteData()
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

        viewModel.inviteListLiveData.observe(this) { response ->
            isLoading = false
            binding.pbLoadData.visibility = View.GONE

            if (response.status) {
                if (currentPage == 1) {
                    inviteList.clear()
                }

                val previousSize = inviteList.size
                inviteList.addAll(response.datas)

                if (currentPage == 1) {
                    inviteAdapter.notifyDataSetChanged()
                } else {
                    inviteAdapter.notifyItemRangeInserted(previousSize, response.datas.size)
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
                if (currentPage > 1) currentPage-- // Revert on failure
            }
        }
    }


    /* private fun setAdapter(inviteList: ArrayList<InviteStudentModel>) {
         binding.apply {
             rvInvite.layoutManager = LinearLayoutManager(mContext)
             rvInvite.setHasFixedSize(true)
             adapter = InviteListAdapter(mContext, inviteList, this@InviteListActivity)
             rvInvite.adapter = adapter
             adapter?.notifyDataSetChanged()
         }
     }*/

    private fun updateVisibility() {
        if (inviteList.isEmpty()) {
            binding.tvNoDataFound.visibility = View.VISIBLE
            binding.rvInvite.visibility = View.GONE
        } else {
            binding.rvInvite.visibility = View.VISIBLE
            binding.tvNoDataFound.visibility = View.GONE
        }
    }

    override fun onClick(position: Int, inviteStudentModel: InviteStudentModel) {
        if (inviteStudentModel.status == "Waiting" || inviteStudentModel.status == "Invited") {
            val intent = Intent(mContext, InviteDetailsActivity::class.java)
                .putExtra("model", Gson().toJson(inviteStudentModel))
            inviteResultLauncher.launch(intent)
        } else {
            startActivity(
                Intent(mContext, ChatHistoryActivity::class.java)
                    .putExtra(
                        "Id", inviteStudentModel.sid.toString()
                    )
                    .putExtra("myChat",false)
            )
        }

    }


    // Activity Result Launcher
    private val inviteResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            currentPage = 1
            viewModel.getInviteList(
                PrefManager(mContext).getvalue(StaticData.id).toString(),
                currentPage.toString()
            )
        }
    }
}
