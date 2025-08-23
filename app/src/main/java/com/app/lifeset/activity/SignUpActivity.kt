package com.app.lifeset.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.app.lifeset.R
import com.app.lifeset.adapter.CustomArrayAdapter
import com.app.lifeset.databinding.ActivitySignUpBinding
import com.app.lifeset.extensions.isNetworkAvailable
import com.app.lifeset.model.CategoryModel
import com.app.lifeset.model.ChangeCollageModel
import com.app.lifeset.model.CollageModel
import com.app.lifeset.model.CourseModel
import com.app.lifeset.util.StaticData
import com.app.lifeset.viewmodel.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignUpBinding
    lateinit var mContext: SignUpActivity
    private val viewModel: SignUpViewModel by viewModels()
    private var collageList: ArrayList<CollageModel> = arrayListOf()
    private val stringCollageList: ArrayList<String> = arrayListOf()

    private var changeCollageList: ArrayList<ChangeCollageModel> = arrayListOf()
    private var stringChangeCollageList: ArrayList<String> = arrayListOf()

    private var categoryList: ArrayList<CategoryModel> = arrayListOf()
    private val stringCategoryList: ArrayList<String> = arrayListOf()

    private var yearList: ArrayList<CourseModel> = arrayListOf()
    private val stringYearList: ArrayList<String> = arrayListOf()

    private var collageId: String = ""
    private var stream = ""
    private var categoryId: String = ""
    private var yearId: String = ""
    private var emailAddress: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        mContext = this
        initUI()
        addListner()
        addObsereves()
    }

    private fun initUI() {
        if (intent.extras != null) {
            emailAddress = intent.getStringExtra(StaticData.email)
        }


/*
        if (isNetworkAvailable(mContext)) {
            viewModel.getCollage()
        } else {
            Toast.makeText(
                mContext,
                getString(R.string.str_error_internet_connections),
                Toast.LENGTH_SHORT
            ).show()
        }
*/
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun addListner() {
        binding.apply {
            tvLogin.setOnClickListener {
                startActivity(Intent(mContext, LoginActivity::class.java))
                finishAffinity()
            }

            rrAdults.setOnClickListener {
                binding.acInstitute.showDropDown()
            }

            /*
                        acInstitute.setOnTouchListener { _, _ ->
                            binding.acInstitute.showDropDown()
                            false
                        }
            */
            acCategory.setOnTouchListener { _, _ ->
                binding.acCategory.showDropDown()
                false
            }


            acSelectCourse.setOnTouchListener { _, _ ->
                binding.acSelectCourse.showDropDown()
                false
            }

            acSelectYear.setOnTouchListener { _, _ ->
                binding.acSelectYear.showDropDown()
                false
            }

            tvSubmit.setOnClickListener {
                if (isNetworkAvailable(mContext)) {
                    if (isValidate()) {
                        viewModel.doRegister(
                            email = emailAddress.toString(),
                            binding.edtPhoneNumber.text.toString().trim(),
                            binding.edtName.text.toString().trim(),
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

    private fun isValidate(): Boolean {
        var isValid = true
     /*   if (binding.acInstitute.text.toString().trim().isEmpty()) {
            Toast.makeText(mContext, "Please select institute", Toast.LENGTH_SHORT).show()
            isValid = false
        } else if (binding.acCategory.text.toString().trim().isEmpty()) {
            Toast.makeText(mContext, "Please select category", Toast.LENGTH_SHORT).show()
            isValid = false
        } else if (binding.acSelectCourse.text.toString().trim().isEmpty()) {
            Toast.makeText(mContext, "Please select course", Toast.LENGTH_SHORT).show()
            isValid = false
        } else if (binding.acInstitute.text.toString().trim().isEmpty()) {
            Toast.makeText(mContext, "Please select year", Toast.LENGTH_SHORT).show()
            isValid = false
        } else*/ if (binding.edtName.text.toString().trim().isEmpty()) {
            Toast.makeText(mContext, "Please enter name", Toast.LENGTH_SHORT).show()
            isValid = false
        } else if (binding.edtPhoneNumber.text.toString().trim().isEmpty()) {
            Toast.makeText(mContext, "Please enter mobile number", Toast.LENGTH_SHORT).show()
            isValid = false
        }
        return isValid
    }

    private fun addObsereves() {
        viewModel.isLoading.observe(this, Observer {
            if (it) {
                binding.pbLoadData.visibility = View.VISIBLE
            } else {
                binding.pbLoadData.visibility = View.GONE
            }
        })

        viewModel.collageLiveData.observe(this, Observer {
            if (it.status) {
                collageList.clear()
                collageList.addAll(it.datas)
                setCollageData(collageList)
            } else {
            }
        })

        viewModel.signUpLiveData.observe(this, Observer {
            if (it.status) {
                Toast.makeText(mContext, it.msz, Toast.LENGTH_SHORT).show()
                startActivity(
                    Intent(mContext, OtpActivity::class.java)
                        .putExtra(StaticData.token, it.datas)
                        .putExtra(StaticData.email, emailAddress)
                        .putExtra(StaticData.name, binding.edtName.text.toString().trim())
                        .putExtra(
                            StaticData.phoneNumber,
                            binding.edtPhoneNumber.text.toString().trim()
                        )
                        .putExtra(StaticData.collageId, collageId)
                        .putExtra(StaticData.categoryId, categoryId)
                        .putExtra(StaticData.yearId, yearId)
                        .putExtra(StaticData.stream, stream)
                )
            } else {
                Toast.makeText(mContext, it.msz, Toast.LENGTH_SHORT).show()
            }
        })


    }

    private fun setCollageData(collageResponse: ArrayList<CollageModel>) {
        stringCollageList.clear()
        for (i in 0 until collageResponse.size) {
            stringCollageList.add(collageResponse[i].name)
        }

        val arrayAdapter: CustomArrayAdapter =
            CustomArrayAdapter(mContext, stringCollageList)
        binding.acInstitute.threshold = 0
        binding.acInstitute.dropDownVerticalOffset = 0
        binding.acInstitute.setAdapter(arrayAdapter)

        binding.acInstitute.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->

                for (i in 0..collageResponse.size - 1) {
                    if (collageResponse.get(i).name.equals(
                            binding.acInstitute.text.toString().trim()
                        )
                    ) {
                        collageId = collageResponse.get(i).id.toString()
                        break
                    }
                }


                // collageId = collageResponse[position].id.toString()
                stream = ""
                categoryId = ""
                yearId = ""

                binding.acCategory.setText("")
                binding.acSelectYear.setText("")
                binding.acSelectCourse.setText("")

                Log.e("collageId", "=" + collageId)

                getChangeCollage(collageId)
            }

    }


    private fun getChangeCollage(collageId: String) {
        viewModel.getChangeCollage(collageId)

        viewModel.changeCollageLiveData.observe(this, Observer {
            if (it.status) {
                changeCollageList.clear()
                changeCollageList.addAll(it.datas)
                setChangeCollage(changeCollageList, collageId)
            } else {
            }
        })
    }


    private fun setChangeCollage(
        changeCollageModel: ArrayList<ChangeCollageModel>,
        collageId: String
    ) {
        stringChangeCollageList.clear()
        for (i in 0 until changeCollageModel.size) {
            stringChangeCollageList.add(changeCollageModel[i].name)
        }

        val arrayAdapter: CustomArrayAdapter =
            CustomArrayAdapter(mContext, stringChangeCollageList)
        binding.acCategory.threshold = 0
        binding.acCategory.dropDownVerticalOffset = 0
        binding.acCategory.setAdapter(arrayAdapter)

        binding.acCategory.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                stream = changeCollageModel[position].name

                categoryId = ""
                yearId = ""

                binding.acSelectCourse.setText("")
                binding.acSelectYear.setText("")

                getCategory(collageId, stream)
            }

    }

    private fun getCategory(collageId: String, stream: String) {
        viewModel.getCategory(collageId, stream)

        viewModel.isCategoryLiveData.observe(this, Observer {
            if (it.status) {
                categoryList.clear()
                categoryList.addAll(it.datas)
                setCategoryData(categoryList)
            } else {
            }
        })
    }


    private fun setCategoryData(categoryModel: ArrayList<CategoryModel>) {
        stringCategoryList.clear()
        for (i in 0 until categoryModel.size) {
            stringCategoryList.add(categoryModel[i].name)
        }

        val arrayAdapter: CustomArrayAdapter =
            CustomArrayAdapter(mContext, stringCategoryList)
        binding.acSelectCourse.threshold = 0
        binding.acSelectCourse.dropDownVerticalOffset = 0
        binding.acSelectCourse.setAdapter(arrayAdapter)

        binding.acSelectCourse.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                categoryId = categoryModel[position].id.toString()

                yearId = ""
                binding.acSelectYear.setText("")

                getYear(categoryId)
            }

    }


    private fun getYear(courseid: String) {
        viewModel.getYear(courseid)

        viewModel.yearLiveData.observe(this, Observer {
            if (it.status) {
                yearList.clear()
                yearList.addAll(it.datas)
                setYearData(yearList)
            } else {
            }
        })
    }


    private fun setYearData(yearModel: ArrayList<CourseModel>) {
        stringYearList.clear()
        for (i in 0 until yearModel.size) {
            stringYearList.add(yearModel[i].name)
        }

        val arrayAdapter: CustomArrayAdapter =
            CustomArrayAdapter(mContext, stringYearList)
        binding.acSelectYear.threshold = 0
        binding.acSelectYear.dropDownVerticalOffset = 0
        binding.acSelectYear.setAdapter(arrayAdapter)

        binding.acSelectYear.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                yearId = yearModel[position].value.toString()
            }

    }

}