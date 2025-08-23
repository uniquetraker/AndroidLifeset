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
import com.app.lifeset.databinding.ActivityLoginBinding
import com.app.lifeset.extensions.isNetworkAvailable
import com.app.lifeset.extensions.isValidPassword
import com.app.lifeset.extensions.isValidPhoneNumber
import com.app.lifeset.model.LoginModel
import com.app.lifeset.util.PrefManager
import com.app.lifeset.util.StaticData
import com.app.lifeset.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    lateinit var mContext: LoginActivity
    private val viewModel: LoginViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        mContext = this
        initUI()
        addListner()
    }

    private fun initUI() {
        addObsereves()
    }

    private fun addListner() {
        binding.tvSubmit.setOnClickListener {
            if (isNetworkAvailable(mContext)) {
                if (isValidPhoneNumber(mContext, binding.edtPhoneNumber.text.toString().trim())
                    && isValidPassword(mContext, binding.edtPassword.text.toString().trim())
                ) {
                    viewModel.doLogin(
                        binding.edtPhoneNumber.text.toString().trim(),
                        binding.edtPassword.text.toString().trim(),
                        PrefManager(mContext).getvalue(StaticData.fcmToken).toString()
                    )
                }
            } else {
                Toast.makeText(
                    mContext,
                    getString(R.string.str_error_internet_connections),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        binding.tvForgotPassword.setOnClickListener {
            startActivity(Intent(mContext, ForgotPasswordActivity::class.java))
        }
        binding.tvNewAccount.setOnClickListener {
            startActivity(Intent(mContext, SignUpEmailActivity::class.java))
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

        viewModel.loginLiveData.observe(this, Observer {
            if (it.response_status) {
                Toast.makeText(mContext, it.msz, Toast.LENGTH_SHORT).show()
                setData(it.datas)
            } else {
                Toast.makeText(mContext, it.msz, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setData(datas: LoginModel) {
        PrefManager(mContext).setvalue(StaticData.isLogin, true)
        PrefManager(mContext).setvalue(
            StaticData.phoneNumber,
            binding.edtPhoneNumber.text.toString().trim()
        )
        PrefManager(mContext).setvalue(StaticData.id, datas.id.toString())
        PrefManager(mContext).setvalue(StaticData.name, datas.name)
        PrefManager(mContext).setvalue(StaticData.college_id, datas.college_id)
        PrefManager(mContext).setvalue(StaticData.profileType, datas.profileType)
        PrefManager(mContext).setvalue(StaticData.acc_token, datas.acc_token)
        PrefManager(mContext).setvalue(StaticData.fcmToken, datas.fcm_token)
        PrefManager(mContext).setvalue(StaticData.profile_status,datas.profile_status)

        startActivity(Intent(mContext, HomeActivity::class.java))
        finishAffinity()
    }
}