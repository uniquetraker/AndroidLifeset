package com.app.lifeset.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.app.lifeset.R
import com.app.lifeset.databinding.ActivityHomeTownBinding
import com.app.lifeset.model.HomeTownRequest
import com.app.lifeset.util.PrefManager
import com.app.lifeset.util.StaticData
import com.app.lifeset.viewmodel.UpdateProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeTownActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomeTownBinding
    lateinit var mContext: HomeTownActivity
    private var state: String? = ""
    private var district: String? = ""
    private var city: String? = ""
    private var village: String? = ""
    private var address: String? = ""
    private var pincode: String? = ""
    private val viewModel: UpdateProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home_town)
        mContext = this
        initUI()
        addListner()
        addObsereves()
    }

    private fun initUI() {
        if (intent.extras != null) {
            state = intent.getStringExtra("state")
            district = intent.getStringExtra("district")
            city = intent.getStringExtra("city")
            village = intent.getStringExtra("village")
            address = intent.getStringExtra("address")
            pincode = intent.getStringExtra("pincode")

            binding.apply {
                edtState.setText(state)
                edtDistrict.setText(district)
                edtCity.setText(city)
                edtVillage.setText(village)
                edtAddress.setText(address)
                edtPinCode.setText(pincode)
            }
        }
    }

    private fun addListner() {
        binding.apply {
            ivBack.setOnClickListener {
                finish()
            }
            tvUpdateProfile.setOnClickListener {
                viewModel.updateHomeTownRequest(
                    HomeTownRequest(
                        id = PrefManager(this@HomeTownActivity).getvalue(StaticData.id).toString(),
                        action = "profile",
                        binding.edtState.text.toString().trim(),
                        binding.edtDistrict.text.toString().trim(),
                        binding.edtCity.text.toString().trim(),
                        binding.edtPinCode.text.toString().trim(),
                        binding.edtAddress.text.toString().trim()


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

        viewModel.updateHomeTownLiveData.observe(this, Observer {
            if (it.status) {
                Toast.makeText(mContext, it.message, Toast.LENGTH_SHORT).show()
                val intent = Intent()
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(mContext, it.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}