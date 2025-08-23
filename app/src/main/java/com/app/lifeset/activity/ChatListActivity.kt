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
import com.app.lifeset.adapter.ChatListAdapter
import com.app.lifeset.adapter.InviteListAdapter
import com.app.lifeset.adapter.MyNetworkAdapter
import com.app.lifeset.databinding.ActivityChatListBinding
import com.app.lifeset.extensions.isNetworkAvailable
import com.app.lifeset.model.InviteStudentModel
import com.app.lifeset.model.chat.ChatModel
import com.app.lifeset.util.PrefManager
import com.app.lifeset.util.StaticData
import com.app.lifeset.viewmodel.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ChatListActivity : AppCompatActivity(), ChatListAdapter.onItemClick {

    lateinit var binding: ActivityChatListBinding
    lateinit var mContext: ChatListActivity
    private val viewModel: ChatViewModel by viewModels()
    private var chatList: ArrayList<ChatModel> = arrayListOf()
    private lateinit var chatListAdapter: ChatListAdapter
    private var currentPage = 1
    private var isLoading = false
    private var totalPages = Int.MAX_VALUE // Update this from API response
    private var isLastPage = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_list)
        mContext = this
        initUI()
        addListner()
    }

    private fun initUI() {
        if (isNetworkAvailable(mContext)) {
            isLoading = true
            currentPage = 1
            binding.pbLoadData.visibility = View.VISIBLE
            fetchChatData()


        } else {
            Toast.makeText(
                mContext, getString(R.string.str_error_internet_connections),
                Toast.LENGTH_SHORT
            ).show()
        }

        setupRecyclerView()

        viewModelObsereves()

    }

    private fun fetchChatData() {

        viewModel.getChatList(
            PrefManager(mContext).getvalue(StaticData.id).toString(),
            currentPage.toString()
        )
    }

    private fun setupRecyclerView() {
        chatListAdapter = ChatListAdapter(mContext, chatList, this@ChatListActivity)
        binding.rvChats.apply {
            adapter = chatListAdapter
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
            fetchChatData()
        }
    }



    private fun addListner() {
        binding.apply {
            ivBack.setOnClickListener {
             startActivity(Intent(mContext,HomeActivity::class.java))
                finish()
            }
            tvStudent.setOnClickListener {
                startActivity(Intent(mContext, StudentListActivity::class.java))
            }
            tvNetworks.setOnClickListener {
                startActivity(Intent(mContext, MyNetworkActivity::class.java))
            }
            tvInvites.setOnClickListener {
                startActivity(Intent(mContext, InviteListActivity::class.java))
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

        viewModel.getChatLiveData.observe(this) { response ->
            isLoading = false
            binding.pbLoadData.visibility = View.GONE

            if (response.status) {
                if (currentPage == 1) {
                    chatList.clear()
                }

                val previousSize = chatList.size
                chatList.addAll(response.datas)

                if (currentPage == 1) {
                    chatListAdapter.notifyDataSetChanged()
                } else {
                    chatListAdapter.notifyItemRangeInserted(previousSize, response.datas.size)
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

    private fun updateVisibility() {
        if (chatList.isEmpty()) {
            binding.tvNoDataFound.visibility = View.VISIBLE
            binding.rvChats.visibility = View.GONE
        } else {
            binding.rvChats.visibility = View.VISIBLE
            binding.tvNoDataFound.visibility = View.GONE
        }
    }


    override fun onClick(position: Int, chatModel: ChatModel) {
        startActivity(
            Intent(mContext, ChatHistoryActivity::class.java)
                .putExtra("Id", chatModel.receiver_id.toString())
                .putExtra("myChat",true)
        )
    }

}