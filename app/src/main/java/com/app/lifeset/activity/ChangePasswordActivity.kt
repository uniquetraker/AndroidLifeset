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
import com.app.lifeset.databinding.ActivityChangePasswordBinding
import com.app.lifeset.extensions.isNetworkAvailable
import com.app.lifeset.util.PrefManager
import com.app.lifeset.util.StaticData
import com.app.lifeset.viewmodel.ChangePasswordViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ChangePasswordActivity : AppCompatActivity() {

    lateinit var binding: ActivityChangePasswordBinding
    lateinit var mContext: ChangePasswordActivity
    private val viewModel: ChangePasswordViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password)
        mContext = this
        initUI()
        addListner()
    }

    private fun initUI() {
        addObsereves()
    }

    private fun addListner() {
        binding.apply {
            ivBack.setOnClickListener {
                finish()
            }
            tvSubmit.setOnClickListener {
                if (isNetworkAvailable(mContext)) {
                    if (isValidate()) {
                        viewModel.doChangePassword(
                            PrefManager(mContext).getvalue(StaticData.id).toString(),
                            binding.edtNewPassword.text.toString().trim()
                        )
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

        viewModel.changePasswordLiveData.observe(this, Observer {
            if (it.status) {
                Toast.makeText(mContext, it.msz, Toast.LENGTH_SHORT).show()
                PrefManager(mContext).clearValue()
                startActivity(Intent(mContext, LoginActivity::class.java))
                finishAffinity()

            } else {
                Toast.makeText(mContext, it.msz, Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun isValidate(): Boolean {
        var isValid = true
        if (binding.edtNewPassword.text.toString().trim().isEmpty()) {
            Toast.makeText(mContext, "Please enter new password", Toast.LENGTH_SHORT).show()
            isValid = false
        } else if (binding.edtConfirmNewPassword.text.toString().trim().isEmpty()) {
            Toast.makeText(mContext, "Please enter confirm new password", Toast.LENGTH_SHORT).show()
            isValid = false
        } else if (binding.edtNewPassword.text.toString().trim() != binding.edtConfirmNewPassword.text.toString().trim()
        ) {
            Toast.makeText(
                mContext,
                "Confirm password and new password does not same",
                Toast.LENGTH_SHORT
            ).show()
            isValid = false
        }
        return isValid
    }
}