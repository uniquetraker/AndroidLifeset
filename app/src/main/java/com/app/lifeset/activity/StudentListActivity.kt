package com.app.lifeset.activity

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.app.lifeset.R
import com.app.lifeset.adapter.StudentListAdapter
import com.app.lifeset.databinding.ActivityStudentListBinding
import com.app.lifeset.extensions.isNetworkAvailable
import com.app.lifeset.model.ChatStudentModel
import com.app.lifeset.util.PrefManager
import com.app.lifeset.util.StaticData
import com.app.lifeset.viewmodel.ChatViewModel
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StudentListActivity : AppCompatActivity(), StudentListAdapter.onItemClick {

    lateinit var binding: ActivityStudentListBinding
    lateinit var mContext: StudentListActivity
    private val viewModel: ChatViewModel by viewModels()
    private var studentList: ArrayList<ChatStudentModel> = arrayListOf()

    private var currentPage = 1
    private var isLoading = false
    private var totalPages = Int.MAX_VALUE // Set this based on your API response
    private lateinit var studentAdapter: StudentListAdapter
    private var currentPosition: Int? = 0
    private var queary: String? = ""
    lateinit var firebaseAnalytics: FirebaseAnalytics


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_student_list)
        mContext = this
        initUI()
        addListner()
    }

    private fun initUI() {
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        setAdapter()
        loadInitialData()
        viewModelObsereves()
    }

    private fun loadInitialData() {
        if (isNetworkAvailable(mContext)) {
            isLoading = true
            currentPage = 1 // Reset to first page
            binding.pbLoadData.visibility = View.VISIBLE
            fetchStudentData()
        } else {
            Toast.makeText(
                mContext,
                getString(R.string.str_error_internet_connections),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun fetchStudentData() {
        viewModel.getChatStudent(
            PrefManager(mContext).getStringValue(StaticData.id).toString(),
            queary.toString(),
            currentPage.toString()
        )
        if (currentPage < totalPages) currentPage++ // Increment only if more pages available
    }

    private fun addListner() {
        binding.apply {
            ivBack.setOnClickListener {
                startActivity(Intent(mContext, HomeActivity::class.java))
                finish()
            }
            tvInvites.setOnClickListener {
                firebaseAnalytics.logEvent(
                    StaticData.invitesClicked, null
                )
                startActivity(Intent(mContext, InviteListActivity::class.java))
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

            binding.ivSearch.setOnClickListener {
                queary = binding.etSearch.text.toString().trim()
                if (queary.toString().isNotEmpty() && queary.toString().length > 3) {
                    hideKeyboard(this@StudentListActivity)
                    currentPage=1
                    viewModel.getChatStudent(
                        PrefManager(mContext).getStringValue(StaticData.id).toString(),
                        queary.toString(),
                        currentPage.toString()
                    )
                } else {
                    Toast.makeText(
                        this@StudentListActivity,
                        "Please enter atleast 3 characters",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            // Show/hide clear button based on EditText content
            etSearch.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    queary=s.toString()
                    if (s.isNullOrEmpty()){
                        hideKeyboard(this@StudentListActivity)
                        ivClear.visibility=View.GONE
                        currentPage=1
                        viewModel.getChatStudent(
                            PrefManager(mContext).getStringValue(StaticData.id).toString(),
                            queary.toString(),
                            currentPage.toString()
                        )
                    }else{
                        ivClear.visibility=View.VISIBLE


                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            ivClear.setOnClickListener {
                etSearch.text.clear()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(mContext, HomeActivity::class.java))
        finish()
    }

    fun hideKeyboard(context: Context) {
        val view = currentFocus
        if (view != null) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
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
        viewModel.chatLiveData.observe(this) { response ->
            isLoading = false
            binding.pbLoadData.visibility = View.GONE

            if (response.status) {
                if (currentPage == 1) {
                    studentList.clear()
                    studentList.addAll(response.datas)
                    studentAdapter?.notifyDataSetChanged()
                    binding.rvStudent.setCurrentItem(0, true) // 'true' for smooth scroll, 'false' for instant scroll

                } else {
                    val start = studentList.size
                    val newItems = response.datas
                    if (newItems.isNotEmpty()) {
                        studentList.addAll(newItems)
                        studentAdapter?.notifyItemRangeInserted(start, newItems.size)
                    }
                }

                response.total_page?.let { totalPages = it }
            } else {
                if (currentPage > 1) currentPage--
            }
        }

        viewModel.inviteStudentLiveData.observe(this, Observer {
            if (it.status) {
                studentAdapter?.notifyItemChanged(currentPosition!!)
                studentList[currentPosition!!].status = "Waiting"
                Toast.makeText(mContext, it.msz, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(mContext, it.msz, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadMoreData() {
        if (isNetworkAvailable(mContext)) {
            isLoading = true
            binding.pbLoadData.visibility = View.VISIBLE
            fetchStudentData()
        }
    }

    private fun setAdapter() {
        /*binding.apply {
            adapter = StudentListAdapter(mContext, studentList, this@StudentListActivity)
            rvStudent.adapter = adapter
            adapter?.notifyDataSetChanged()


        }*/
        studentAdapter = StudentListAdapter(mContext, studentList, this@StudentListActivity)
        binding.rvStudent.adapter = studentAdapter

        binding.rvStudent.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position >= studentList.size - 2 && !isLoading && currentPage < totalPages) {
                    loadMoreData()
                }
            }
        })


    }

    override fun onClick(position: Int, studentModel: ChatStudentModel) {
        if (PrefManager(mContext).getvalue(StaticData.profile_status).equals("pending")) {
            dialogUpdateProfile()
        } else {
            studentInviteDialog(position, studentModel)
        }
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


    private fun studentInviteDialog(position: Int, studentModel: ChatStudentModel) {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Confirmation")
        builder.setMessage("Are you sure you want to Send Invite?")

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            if (isNetworkAvailable(mContext)) {
                dialog.dismiss()
                currentPosition = position
                viewModel.inviteStudent(
                    PrefManager(mContext).getvalue(StaticData.id).toString(),
                    studentModel.id.toString()
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