package com.app.lifeset.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Profile
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.lifeset.R
import com.app.lifeset.adapter.ChatHistoryAdapter
import com.app.lifeset.adapter.InviteListAdapter
import com.app.lifeset.databinding.ActivityChatHistoryBinding
import com.app.lifeset.extensions.isNetworkAvailable
import com.app.lifeset.model.chat.ChatHistoryModel
import com.app.lifeset.model.chat.ReceiverModel
import com.app.lifeset.util.PrefManager
import com.app.lifeset.util.StaticData
import com.app.lifeset.viewmodel.ChatViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatHistoryActivity : AppCompatActivity() {

    lateinit var binding: ActivityChatHistoryBinding
    lateinit var mContext: ChatHistoryActivity
    private var id: String? = ""
    private val viewModel: ChatViewModel by viewModels()
    private var chatHistoryList: ArrayList<ChatHistoryModel> = arrayListOf()
    private lateinit var chatHistoryAdapter: ChatHistoryAdapter
    private var currentPage = 1
    private var isLoading = false
    private var totalPages = Int.MAX_VALUE // Update this from API response
    private var isLastPage = false
    private var myChat:Boolean=false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_history)
        mContext = this
        initUI()
        addListner()
    }

    private fun initUI() {
        if (intent.extras != null) {
            id = intent.getStringExtra("Id")
            myChat=intent.getBooleanExtra("myChat",false)
        }

        binding.cvProfile.setOnClickListener {
            startActivity(Intent(mContext,ProfileCardActivity::class.java)
                .putExtra("isChat",true)
                .putExtra("id",id))
        }
        binding.llName.setOnClickListener {
            startActivity(Intent(mContext,ProfileCardActivity::class.java)
                .putExtra("isChat",true)
                .putExtra("id",id))
        }

        if (isNetworkAvailable(mContext)) {
            isLoading = true
            currentPage = 1
            binding.pbLoadData.visibility = View.VISIBLE

            fetchChatHistoryData()

        } else {
            Toast.makeText(
                mContext, getString(R.string.str_error_internet_connections),
                Toast.LENGTH_SHORT
            ).show()
        }
        setupRecyclerView()
        viewModelObsereves()
    }

    private fun fetchChatHistoryData() {

        viewModel.chatStudentHistory(
            uid = PrefManager(mContext).getvalue(StaticData.id).toString(),
            receiverId = id.toString(),
            page = currentPage.toString()
        )
    }

    private fun setupRecyclerView() {
        chatHistoryAdapter = ChatHistoryAdapter(mContext, chatHistoryList)
        binding.rvChatHistory.apply {
            adapter = chatHistoryAdapter
            layoutManager = LinearLayoutManager(mContext)

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
            fetchChatHistoryData()
        }
    }


    private fun addListner() {
        binding.apply {
            ivBack.setOnClickListener {
                if (myChat){
                    startActivity(Intent(mContext,ChatListActivity::class.java))
                    finish()
                }else{
                    finish()
                }
            }
            ivSend.setOnClickListener {
                if (binding.edtMessage.text.toString().trim().isEmpty()) {
                    Toast.makeText(mContext, "Please enter message", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (isNetworkAvailable(mContext)) {
                    viewModel.sendMessage(
                        PrefManager(mContext).getvalue(StaticData.id).toString(),
                        receiverId = id.toString(),
                        comments = binding.edtMessage.text.toString().trim()
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

    override fun onBackPressed() {
        super.onBackPressed()
        if (myChat){
            startActivity(Intent(mContext,ChatListActivity::class.java))
            finish()
        }else{
            finish()
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

        viewModel.chatHistoryLiveData.observe(this) { response ->
            isLoading = false
            binding.pbLoadData.visibility = View.GONE

            if (response.status) {
                if (currentPage == 1) {
                    chatHistoryList.clear()
                }

                val previousSize = chatHistoryList.size
                chatHistoryList.addAll(response.datas)

                if (currentPage == 1) {
                    setChatHistoryData(response.receiver)
                    chatHistoryAdapter.notifyDataSetChanged()
                } else {
                    chatHistoryAdapter.notifyItemRangeInserted(previousSize, response.datas.size)
                }
                binding.rvChatHistory.smoothScrollToPosition(
                    binding.rvChatHistory.adapter?.itemCount ?: 0
                )

                // Update pagination info
                response.total_page?.let {
                    totalPages = it
                    isLastPage = currentPage >= totalPages
                }
            } else {
                setChatHistoryData(response.receiver)
                if (currentPage == 1) {
                }
                if (currentPage > 1) currentPage-- // Revert on failure
            }
        }
        viewModel.sendMessageLiveData.observe(this, Observer {
            if (it.status) {
                binding.edtMessage.setText("")
                Toast.makeText(mContext, it.msz, Toast.LENGTH_SHORT).show()
                if (isNetworkAvailable(mContext)) {
                    binding.rvChatHistory.smoothScrollToPosition(
                        binding.rvChatHistory.adapter?.itemCount ?: 0
                    )
                    viewModel.chatStudentHistory(
                        uid = PrefManager(mContext).getvalue(StaticData.id).toString(),
                        receiverId = id.toString(),
                        page = currentPage.toString()
                    )
                } else {
                    Toast.makeText(
                        mContext, getString(R.string.str_error_internet_connections),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(mContext, it.msz, Toast.LENGTH_SHORT).show()
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setChatHistoryData(receiver: ReceiverModel) {
        Glide.with(mContext).load(receiver.profile)
            .placeholder(R.drawable.ic_profile)
            .error(R.drawable.ic_profile)
            .into(binding.cvProfile)
        binding.tvName.text = receiver.name
        binding.tvSchoolName.text = receiver.school_name + " (" + receiver.school_district + ") "
    }
}