package com.app.lifeset.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.lifeset.R
import com.app.lifeset.adapter.InviteListAdapter
import com.app.lifeset.adapter.MyNetworkAdapter
import com.app.lifeset.databinding.ActivityMyNetworkBinding
import com.app.lifeset.extensions.isNetworkAvailable
import com.app.lifeset.model.InviteStudentModel
import com.app.lifeset.util.PrefManager
import com.app.lifeset.util.StaticData
import com.app.lifeset.viewmodel.ChatViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyNetworkActivity : AppCompatActivity(), MyNetworkAdapter.onItemClick {

    lateinit var binding: ActivityMyNetworkBinding
    lateinit var mContext: MyNetworkActivity
    private val viewModel: ChatViewModel by viewModels()
    private var myNetworkList: ArrayList<InviteStudentModel> = arrayListOf()
    lateinit var myNetworkAdapter: MyNetworkAdapter
    private var currentPage = 1
    private var isLoading = false
    private var totalPages = Int.MAX_VALUE // Update this from API response
    private var isLastPage = false
    lateinit var firebaseAnalytics: FirebaseAnalytics



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_network)
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
            fetchNetworkData()

        } else {
            Toast.makeText(
                mContext, getString(R.string.str_error_internet_connections),
                Toast.LENGTH_SHORT
            ).show()
        }

        setupRecyclerView()

        viewModelObsereves()
    }


    private fun fetchNetworkData() {
        viewModel.getMyNetworkList(
            PrefManager(mContext).getvalue(StaticData.id).toString(),
            currentPage.toString()
        )
    }

    private fun setupRecyclerView() {
        myNetworkAdapter = MyNetworkAdapter(mContext, myNetworkList, this@MyNetworkActivity)
        binding.rvInvite.apply {
            adapter = myNetworkAdapter
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
            fetchNetworkData()
        }
    }


    private fun addListner() {
        binding.apply {
            ivBack.setOnClickListener {
                startActivity(Intent(mContext,HomeActivity::class.java))
                finish()
            }
            tvInvites.setOnClickListener {
                firebaseAnalytics.logEvent(
                    StaticData.invitesClicked, null
                )
                startActivity(Intent(mContext, InviteListActivity::class.java))
            }
            tvStudent.setOnClickListener {
                firebaseAnalytics.logEvent(
                    StaticData.networkingClicked, null
                )
                startActivity(Intent(mContext, StudentListActivity::class.java))
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

    private fun viewModelObsereves() {
        viewModel.isLoading.observe(this, Observer {
            if (it) {
                binding.pbLoadData.visibility = View.VISIBLE
            } else {
                binding.pbLoadData.visibility = View.GONE
            }
        })

        viewModel.myNetworkLiveData.observe(this) { response ->
            isLoading = false
            binding.pbLoadData.visibility = View.GONE

            if (response.status) {
                if (currentPage == 1) {
                    myNetworkList.clear()
                }

                val previousSize = myNetworkList.size
                myNetworkList.addAll(response.datas)

                if (currentPage == 1) {
                    myNetworkAdapter.notifyDataSetChanged()
                } else {
                    myNetworkAdapter.notifyItemRangeInserted(previousSize, response.datas.size)
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
        if (myNetworkList.isEmpty()) {
            binding.tvNoDataFound.visibility = View.VISIBLE
            binding.rvInvite.visibility = View.GONE
        } else {
            binding.rvInvite.visibility = View.VISIBLE
            binding.tvNoDataFound.visibility = View.GONE
        }
    }



    override fun onClick(position: Int, inviteStudentModel: InviteStudentModel) {
        startActivity(
            Intent(mContext, ChatHistoryActivity::class.java)
                .putExtra("Id", inviteStudentModel.sid.toString())
                .putExtra("myChat",false)
        )
    }
}