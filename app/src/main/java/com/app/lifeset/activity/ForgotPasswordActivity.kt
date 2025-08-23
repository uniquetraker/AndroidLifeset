package com.app.lifeset.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.app.lifeset.R
import com.app.lifeset.databinding.ActivityForgotPasswordBinding
import com.app.lifeset.extensions.isNetworkAvailable
import com.app.lifeset.viewmodel.ForgotPasswordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordActivity : AppCompatActivity() {


    lateinit var binding: ActivityForgotPasswordBinding
    lateinit var mContext: ForgotPasswordActivity

    private val viewModel: ForgotPasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password)
        mContext = this
        initUI()
        addListner()
        addObsereves()
    }

    private fun initUI() {

    }

    private fun addListner() {
        binding.apply {
            ivBack.setOnClickListener {
                finish()
            }
            tvSubmit.setOnClickListener {
                if (isNetworkAvailable(mContext)) {
                    if (isValidate()) {
                        viewModel.doForgotPassword(binding.edtPhoneNumber.text.toString().trim())
                    }
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

        viewModel.forgotPasswordLiveData.observe(this, Observer {
            if (it.status) {
                Toast.makeText(mContext, it.msz, Toast.LENGTH_SHORT).show()
                startActivity(Intent(mContext, LoginActivity::class.java))
                finishAffinity()
            } else {
                Toast.makeText(mContext, it.msz, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun isValidate(): Boolean {
        var isValid = true
        if (binding.edtPhoneNumber.text.toString().trim().isEmpty()) {
            Toast.makeText(mContext, "Please enter mobile number", Toast.LENGTH_SHORT).show()
            isValid = false
        }
        return isValid
    }
}