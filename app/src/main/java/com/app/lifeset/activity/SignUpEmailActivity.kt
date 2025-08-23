package com.app.lifeset.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.app.lifeset.R
import com.app.lifeset.databinding.ActivitySignUpBinding
import com.app.lifeset.databinding.ActivitySignUpEmailBinding
import com.app.lifeset.extensions.isNetworkAvailable
import com.app.lifeset.extensions.isValidateEmail
import com.app.lifeset.util.StaticData
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SignUpEmailActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignUpEmailBinding
    lateinit var mContext: SignUpEmailActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up_email)
        mContext = this
        initUI()
        addListner()
    }

    private fun initUI() {

    }

    private fun addListner() {
        binding.apply {
            tvLogin.setOnClickListener {
                startActivity(Intent(mContext, LoginActivity::class.java))
                finishAffinity()
            }
            tvSubmit.setOnClickListener {
                if (isNetworkAvailable(mContext)) {
                    if (isValidateEmail(mContext, binding.edtEmailAddress.text.toString().trim())) {
                        startActivity(
                            Intent(mContext, SignUpActivity::class.java)
                                .putExtra(
                                    StaticData.email,
                                    binding.edtEmailAddress.text.toString().trim()
                                )
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
        }
    }

}