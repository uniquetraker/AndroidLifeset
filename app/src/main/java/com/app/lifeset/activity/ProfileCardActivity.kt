package com.app.lifeset.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.app.lifeset.R
import com.app.lifeset.databinding.ActivityProfileCardBinding
import com.app.lifeset.extensions.isNetworkAvailable
import com.app.lifeset.model.StudentProfileModel
import com.app.lifeset.model.StudentProfileResponse
import com.app.lifeset.util.PrefManager
import com.app.lifeset.util.StaticData
import com.app.lifeset.viewmodel.ProfileViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileCardActivity : AppCompatActivity() {

    lateinit var binding: ActivityProfileCardBinding
    lateinit var mContext: ProfileCardActivity
    private val viewModel: ProfileViewModel by viewModels()
    private var isChat: Boolean = false

    private val handler = Handler(Looper.getMainLooper())
    private var scrollX = 0
    private var isUserScrolling = false
    private val scrollSpeed = 3
    private val scrollInterval: Long = 30
    private var maxScroll = 0
    private var technicalSkills: String? = ""

    private val scrollRunnable = object : Runnable {
        override fun run() {
            if (!isUserScrolling) {
                scrollX -= scrollSpeed
                if (scrollX <= 0) {
                    scrollX = maxScroll
                    binding.horizontalScrollView.scrollTo(scrollX, 0)
                } else {
                    binding.horizontalScrollView.smoothScrollTo(scrollX, 0)
                }
            }
            handler.postDelayed(this, scrollInterval)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile_card)
        mContext = this
        initUI()
        addListner()
        addObsereves()

        binding.horizontalScrollView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> isUserScrolling = true
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> isUserScrolling = false
            }
            false
        }

        // Set up click toasts
        for (i in 0 until binding.buttonContainer.childCount) {
            val view = binding.buttonContainer.getChildAt(i)
            if (view is Button) {
                view.setOnClickListener {
                    Toast.makeText(this, "Clicked: ${view.text}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Wait for layout to draw before initializing
        binding.horizontalScrollView.viewTreeObserver.addOnGlobalLayoutListener {
            maxScroll = binding.buttonContainer.width / 2
            scrollX = maxScroll
            binding.horizontalScrollView.scrollTo(scrollX, 0)
            handler.postDelayed(scrollRunnable, 1000)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(scrollRunnable)
    }


    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(scrollRunnable)
    }

    /*override fun onResume() {
        super.onResume()
        binding.horizontalScrollView.post {
            handler.post(scrollRunnable)
        }
    }*/

    private fun initUI() {
        if (isNetworkAvailable(mContext)) {
            if (intent.extras != null) {
                isChat = intent.getBooleanExtra("isChat", false)
                if (isChat) {
                    val id = intent.getStringExtra("id")
                    viewModel.getStudentProfile(id.toString())
                } else {
                    viewModel.getStudentProfile(
                        PrefManager(mContext).getvalue(StaticData.id).toString()
                    )
                }
            } else {
                viewModel.getStudentProfile(
                    PrefManager(mContext).getvalue(StaticData.id).toString()
                )
            }

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
            tvEditProfileDetails.setOnClickListener {
                val intent = Intent(mContext, SkillsActivity::class.java)
                intent.putExtra("tech_skills",technicalSkills)
                startActivityForResult(intent, 201)

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==201){
            if (resultCode==Activity.RESULT_OK){
                if (isNetworkAvailable(mContext)){
                    viewModel.getStudentProfile(
                        PrefManager(mContext).getvalue(StaticData.id).toString()
                    )
                }else{
                    Toast.makeText(mContext,getString(R.string.str_error_internet_connections),
                        Toast.LENGTH_SHORT).show()
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


        viewModel.studentProfileLiveData.observe(this, Observer {
            if (it.status) {
                setData(it.datas)
            } else {
                Toast.makeText(mContext, it.msz, Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun setData(
        data: StudentProfileModel
    ) {
        binding.apply {
            Glide.with(mContext).load(data.profile)
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_profile)
                .into(cvProfile)
            binding.tvStudyAt.text =
                "Native of " + data.city + " ," + data.district + " , " + data.state
            binding.tvName.text =
                data.name + " " + "(" + data.year + ")"
            binding.tvCollageName.text =
                data.course + " from " + data.school_name
            binding.tvInterest.text = data.interest
            binding.tvSkills.text = data.skills
            technicalSkills=data.skills
            if (data.is_freelancer == 1) {
                binding.tvFreelancer.visibility = View.VISIBLE
                binding.tvFreelancingTitle.visibility = View.VISIBLE
                binding.tvFreelancing.visibility = View.VISIBLE
                binding.tvFreelancing.text = data.freelancer_detail
            } else {
                binding.tvFreelancer.visibility = View.GONE
                binding.tvFreelancingTitle.visibility = View.GONE
                binding.tvFreelancing.visibility = View.GONE
            }
        }
    }
}