package com.app.lifeset.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.app.lifeset.R
import com.app.lifeset.adapter.CustomArrayAdapter
import com.app.lifeset.databinding.ActivityPersonalInformationBinding
import com.app.lifeset.extensions.isNetworkAvailable
import com.app.lifeset.model.PersonalInformationModel
import com.app.lifeset.util.PrefManager
import com.app.lifeset.util.StaticData
import com.app.lifeset.viewmodel.UpdateProfileViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PersonalInformationActivity : AppCompatActivity() {

    lateinit var binding: ActivityPersonalInformationBinding
    lateinit var mContext: PersonalInformationActivity
    private val viewModel: UpdateProfileViewModel by viewModels()
    private var name: String? = ""
    private var email: String? = ""
    private var mobile: String? = ""
    private var gender: String? = ""
    private var categoryList: ArrayList<String> = arrayListOf()
    private var religionList: ArrayList<String> = arrayListOf()
    private var category: String? = ""
    private var religion: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_personal_information)
        mContext = this
        initUI()
        addListner()
        addObsereves()
    }

    private fun initUI() {

        if (intent.extras != null) {
            name = intent.getStringExtra("name")
            email = intent.getStringExtra("email")
            mobile = intent.getStringExtra("mobile")
            gender = intent.getStringExtra("gender")
            category = intent.getStringExtra("category")
            religion = intent.getStringExtra("religion")

            binding.edtName.setText(name)
            binding.edtEmailAddress.setText(email)
            binding.edtMobileNumber.setText(mobile)
            binding.acCategory.setText(category)
            binding.acReligion.setText(religion)

            if (gender.equals("Male")) {
                binding.rbMale.isChecked = true
                binding.rbFemale.isChecked=false
                gender = "Male"
            } else {
                binding.rbMale.isChecked=false
                binding.rbFemale.isChecked = true
                gender = "Female"
            }
        }

    }

    private fun addListner() {
        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.acCategory.setOnTouchListener { _, _ ->
            binding.acCategory.showDropDown()
            false
        }

        binding.acReligion.setOnTouchListener { _, _ ->
            binding.acReligion.showDropDown()
            false
        }

        binding.rbMale.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.rbMale.isChecked = true
                binding.rbFemale.isChecked = false

                gender = "Male"
            }
        }

        binding.rbFemale.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.rbMale.isChecked = false
                binding.rbFemale.isChecked = true

                gender = "Female"
            }
        }

        binding.tvUpdateProfile.setOnClickListener {
            if (isNetworkAvailable(mContext)) {
                viewModel.getPersonalInformation(
                    PersonalInformationModel(
                        PrefManager(mContext).getvalue(StaticData.id).toString(),
                        "personal_info",
                        binding.edtName.text.toString().trim(),
                        binding.edtEmailAddress.text.toString().trim(),
                        binding.edtMobileNumber.text.toString().trim(),
                        gender.toString(),
                        category.toString(),
                        religion.toString()
                    )
                )
            } else {
                Toast.makeText(
                    mContext, getString(R.string.str_error_internet_connections),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        setCategoryData()

        setReligionData()
    }

    private fun setCategoryData() {
        categoryList.clear()
        categoryList.add("General")
        categoryList.add("SC")
        categoryList.add("ST")
        categoryList.add("OBC")

        val arrayAdapter: CustomArrayAdapter =
            CustomArrayAdapter(mContext, categoryList)
        binding.acCategory.threshold = 0
        binding.acCategory.dropDownVerticalOffset = 0
        binding.acCategory.setAdapter(arrayAdapter)
        binding.acCategory.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->

                category = categoryList[position]


            }
    }

    private fun setReligionData() {
        religionList.clear()
        religionList.add("Hinduism")
        religionList.add("Islam")
        religionList.add("Christianity")
        religionList.add("Sikhism")
        religionList.add("Buddhism")
        religionList.add("Jainism")
        religionList.add("Other Religions")

        val arrayAdapter: CustomArrayAdapter =
            CustomArrayAdapter(mContext, religionList)
        binding.acReligion.threshold = 0
        binding.acReligion.dropDownVerticalOffset = 0
        binding.acReligion.setAdapter(arrayAdapter)
        binding.acReligion.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->

                religion = religionList[position]


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

        viewModel.personalInformationLiveData.observe(this, Observer {
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