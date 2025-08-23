package com.app.lifeset.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.app.lifeset.R
import com.app.lifeset.databinding.ActivityOtpBinding
import com.app.lifeset.extensions.isNetworkAvailable
import com.app.lifeset.model.LoginModel
import com.app.lifeset.model.OtpModel
import com.app.lifeset.util.PrefManager
import com.app.lifeset.util.StaticData
import com.app.lifeset.viewmodel.SignUpViewModel
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtpActivity : AppCompatActivity() {

    lateinit var binding: ActivityOtpBinding
    lateinit var mContext: OtpActivity

    private var token: String? = ""
    private var email: String? = ""
    private var name: String? = ""
    private var phoneNumber: String? = ""
    private var collageId: String? = ""
    private var stream: String? = ""
    private var categoryId: String? = ""
    private var yearId: String? = ""

    private val viewModel: SignUpViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_otp)
        mContext = this
        initUI()
        addListner()
        addObsereves()
    }

    private fun initUI() {
        if (intent.extras != null) {
            token = intent.getStringExtra(StaticData.token)
            email = intent.getStringExtra(StaticData.email)
            name = intent.getStringExtra(StaticData.name)
            phoneNumber = intent.getStringExtra(StaticData.phoneNumber)
            collageId = intent.getStringExtra(StaticData.collageId)
            categoryId = intent.getStringExtra(StaticData.categoryId)
            stream = intent.getStringExtra(StaticData.stream)
            yearId = intent.getStringExtra(StaticData.yearId)

            binding.tvMobileNumber.text = "+91 $phoneNumber"
        }
    }

    private fun addListner() {

        binding.tvResendOTP.setOnClickListener {
            if (isNetworkAvailable(mContext)) {
                if (isValidate()) {
                    viewModel.doVerifyRegister(
                        binding.edtNumber1.text.toString()
                            .trim() + "" + binding.edtNumber2.text.toString().trim() + ""
                                + binding.edtNumber3.text.toString()
                            .trim() + "" + binding.edtNumber4.text.toString()
                            .trim() + "" + binding.edtNumber5.text.toString().trim() + ""
                                + binding.edtNumber6.text.toString().trim(),
                        email.toString(),
                        mobile = phoneNumber.toString(),
                        name.toString(),
                        token.toString(),
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

        binding.tvVerify.setOnClickListener {
            if (isNetworkAvailable(mContext)) {
                if (isValidate()) {
                    viewModel.doVerifyRegister(
                        binding.edtNumber1.text.toString()
                            .trim() + "" + binding.edtNumber2.text.toString().trim() + ""
                                + binding.edtNumber3.text.toString()
                            .trim() + "" + binding.edtNumber4.text.toString()
                            .trim() + "" + binding.edtNumber5.text.toString().trim() + ""
                                + binding.edtNumber6.text.toString().trim(),
                        email.toString(),
                        mobile = phoneNumber.toString(),
                        name.toString(),
                        token.toString(),
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

        binding.edtNumber1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()

                if (text.length == 1) {
                    binding.edtNumber2.requestFocus()
                }
            }

        })

        binding.edtNumber2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()

                if (text.length == 1) binding.edtNumber3.requestFocus() else if (text.isEmpty()) binding.edtNumber1.requestFocus()
            }

        })

        binding.edtNumber3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()

                if (text.length == 1) binding.edtNumber4.requestFocus() else if (text.isEmpty()) binding.edtNumber2.requestFocus()
            }

        })

        binding.edtNumber4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()

                if (text.length == 1) binding.edtNumber5.requestFocus() else if (text.isEmpty()) binding.edtNumber3.requestFocus()
            }

        })

        binding.edtNumber5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()

                if (text.length == 1) binding.edtNumber6.requestFocus() else if (text.isEmpty()) binding.edtNumber4.requestFocus()
            }

        })

        binding.edtNumber6.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()

                if (text.isEmpty()) binding.edtNumber5.requestFocus()
            }

        })
    }

    private fun addObsereves() {
        viewModel.isLoading.observe(this, Observer {
            if (it) {
                binding.pbLoadData.visibility = View.VISIBLE
            } else {
                binding.pbLoadData.visibility = View.GONE
            }
        })

        viewModel.verifySignUpLiveData.observe(this, Observer {
            if (it.status) {
                Toast.makeText(mContext, it.msz, Toast.LENGTH_SHORT).show()
                setData(it.datas)
            } else {
                Toast.makeText(mContext, it.msz, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun isValidate(): Boolean {
        var isValid = true
        if (binding.edtNumber1.text.toString().trim().isEmpty()
            || binding.edtNumber2.text.toString().trim().isEmpty()
            || binding.edtNumber3.text.toString().trim().isEmpty()
            || binding.edtNumber4.text.toString().trim().isEmpty()
            || binding.edtNumber5.text.toString().trim().isEmpty()
            || binding.edtNumber6.text.toString().trim().isEmpty()
        ) {
            Toast.makeText(mContext, "Please enter valid otp", Toast.LENGTH_SHORT).show()
            isValid = false
        }
        return isValid
    }

    private fun setData(datas: OtpModel) {
        PrefManager(mContext).setvalue(StaticData.isLogin, true)
        PrefManager(mContext).setvalue(
            StaticData.phoneNumber,
            phoneNumber
        )
        PrefManager(mContext).setvalue(StaticData.id, datas.id.toString())
        PrefManager(mContext).setvalue(StaticData.name, datas.name)
        PrefManager(mContext).setvalue(StaticData.college_id, datas.college_id)
        PrefManager(mContext).setvalue(StaticData.profileType, datas.profileType)
        PrefManager(mContext).setvalue(StaticData.acc_token, datas.acc_token)
        PrefManager(mContext).setvalue(StaticData.fcmToken, datas.fcm_token)

        startActivity(Intent(mContext, ActivateAccountActivity::class.java))
        finishAffinity()
    }
}