package com.app.lifeset.activity

import android.app.Activity
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
import com.app.lifeset.R
import com.app.lifeset.databinding.ActivityCreateFreelancerProfileBinding
import com.app.lifeset.extensions.isNetworkAvailable
import com.app.lifeset.model.SaveFreelancerRequest
import com.app.lifeset.model.StudentProfileModel
import com.app.lifeset.util.PrefManager
import com.app.lifeset.util.StaticData
import com.app.lifeset.viewmodel.ChatViewModel
import com.app.lifeset.viewmodel.ProfileViewModel
import com.bumptech.glide.Glide
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateFreelancerProfileActivity : AppCompatActivity() {

    lateinit var binding: ActivityCreateFreelancerProfileBinding
    lateinit var mContext: CreateFreelancerProfileActivity
    private val viewModel: ProfileViewModel by viewModels()
    private val chatViewModel: ChatViewModel by viewModels()
    lateinit var firebaseAnalytics: FirebaseAnalytics
    private var title: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_freelancer_profile)
        mContext = this
        initUI()
        addListner()
        addObsereves()
    }

    private fun initUI() {
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        if (intent.extras!=null){
            title=intent.getStringExtra("title")
            binding.tvTitle.setText(title)
        }

        if (isNetworkAvailable(mContext)) {
            viewModel.getStudentProfile(PrefManager(mContext).getvalue(StaticData.id).toString())
        } else {
            Toast.makeText(
                mContext, getString(R.string.str_error_internet_connections),
                Toast.LENGTH_SHORT
            ).show()
        }

    }


    private fun addListner() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.tvUpdateProfile.setOnClickListener {
            if (isValidate()) {
                chatViewModel.saveFreelancerDetails(
                    SaveFreelancerRequest(
                        user_id = PrefManager(mContext).getvalue(StaticData.id).toString(),
                        freelancer_detail = binding.edtFreelancerProfile.text.toString().trim()
                    )
                )
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

        chatViewModel.isLoading.observe(this, Observer {
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


        chatViewModel.saveFreelancerLiveData.observe(this, Observer {
            if (it.status) {
                firebaseAnalytics.logEvent(
                    StaticData.updateFreelancerProfileClicked, null
                )
                Toast.makeText(mContext, it.msg, Toast.LENGTH_SHORT).show()
                val intent = Intent()
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(mContext, it.msg, Toast.LENGTH_SHORT).show()
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
            edtFreelancerProfile.setText(data.freelancer_detail)

        }
    }

    private fun isValidate(): Boolean {
        var isValid = true
        if (binding.edtFreelancerProfile.text.toString().trim().isEmpty()) {
            Toast.makeText(mContext, "Please add freelancer skills", Toast.LENGTH_SHORT).show()
            isValid = false
        }
        return isValid
    }
}