package com.app.lifeset.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.app.lifeset.R
import com.app.lifeset.databinding.ActivitySkillsBinding
import com.app.lifeset.extensions.isNetworkAvailable
import com.app.lifeset.model.SkillsRequest
import com.app.lifeset.util.PrefManager
import com.app.lifeset.util.StaticData
import com.app.lifeset.viewmodel.UpdateProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SkillsActivity : AppCompatActivity() {

    lateinit var binding: ActivitySkillsBinding
    lateinit var mContext: SkillsActivity
    private val viewModel: UpdateProfileViewModel by viewModels()

    private var tech_skills: String? = ""
    private var prof_skills: String? = ""
    private var soft_skills: String? = ""
    private var hobbies: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_skills)
        mContext = this
        initUI()
        addListner()
        addObsereves()
    }

    private fun initUI() {
        if (intent.extras != null) {
            tech_skills = intent.getStringExtra("tech_skills")
            prof_skills = intent.getStringExtra("prof_skills")
            soft_skills = intent.getStringExtra("soft_skills")
            hobbies = intent.getStringExtra("hobbies")

            binding.edtTechnicalSkills.setText(tech_skills)
            binding.edtProffessionalSkills.setText(prof_skills)
            binding.edtSoftSkills.setText(soft_skills)
            binding.edtHobbies.setText(hobbies)
        }
    }

    private fun addListner() {
        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.tvUpdateProfile.setOnClickListener {
            if (isNetworkAvailable(mContext)) {
                if (isValidate()) {
                    viewModel.getUpdateSkills(
                        SkillsRequest(
                            PrefManager(mContext).getvalue(StaticData.id).toString(),
                            action = "skills",
                            tech_skills = binding.edtTechnicalSkills.text.toString().trim(),
                            prof_skills = binding.edtProffessionalSkills.text.toString().trim(),
                            soft_skills = binding.edtSoftSkills.text.toString().trim(),
                            hobbies = binding.edtHobbies.text.toString().trim()
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

    private fun addObsereves() {
        viewModel.isLoading.observe(this, Observer {
            if (it) {
                binding.pbLoadData.visibility = View.VISIBLE
            } else {
                binding.pbLoadData.visibility = View.GONE
            }
        })

        viewModel.updateProfileLiveData.observe(this, Observer {
            if (it.status) {
                Toast.makeText(mContext, it.message, Toast.LENGTH_SHORT).show()
                PrefManager(mContext).setvalue(StaticData.profile_status,it.profile_status)
                val intent = Intent()
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(mContext, it.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun isValidate(): Boolean {
        var isValid = true
        if (binding.edtTechnicalSkills.text.toString().trim().isEmpty()) {
            Toast.makeText(mContext, "Please enter technical skills", Toast.LENGTH_SHORT).show()
            isValid = false
        }/* else if (binding.edtProffessionalSkills.text.toString().trim().isEmpty()) {
            Toast.makeText(mContext, "Please enter proffessional skills", Toast.LENGTH_SHORT).show()
            isValid = false
        } else if (binding.edtSoftSkills.text.toString().trim().isEmpty()) {
            Toast.makeText(mContext, "Please enter soft skills", Toast.LENGTH_SHORT).show()
            isValid = false
        }else if (binding.edtHobbies.text.toString().trim().isEmpty()) {
            Toast.makeText(mContext, "Please enter hobbies", Toast.LENGTH_SHORT).show()
            isValid = false
        }*/
        return isValid
    }
}