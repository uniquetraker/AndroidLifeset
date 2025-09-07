package com.app.lifeset.activity

import android.app.Activity
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
import com.app.lifeset.databinding.ActivityEducationDetailBinding
import com.app.lifeset.extensions.isNetworkAvailable
import com.app.lifeset.model.CategoryModel
import com.app.lifeset.model.CourseModel
import com.app.lifeset.model.EducationInformationRequest
import com.app.lifeset.util.PrefManager
import com.app.lifeset.util.StaticData
import com.app.lifeset.viewmodel.SignUpViewModel
import com.app.lifeset.viewmodel.UpdateProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONArray

@AndroidEntryPoint
class EducationDetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityEducationDetailBinding
    lateinit var mContext: EducationDetailActivity
    private var gradeList: ArrayList<String> = arrayListOf()
    private var tengradeList: ArrayList<String> = arrayListOf()
    private var grade: String? = ""
    private val viewModel: UpdateProfileViewModel by viewModels()

    private var year: String? = ""
    private var yearList: ArrayList<CourseModel> = arrayListOf()
    private val stringYearList: ArrayList<String> = arrayListOf()


    private var board: String? = ""
    private var location: String? = ""
    private var passingyear: String? = ""
    private var percentage: String? = ""

    private var tenBoard: String? = ""
    private var tenLocation: String? = ""
    private var tenPassingYear: String? = ""
    private var tenPercentage: String? = ""

    private var edu_year: String? = ""
    private var edu_passyear: String? = ""
    private var edu_percentage: String? = ""

    private var yearId: String? = ""

    private var courseName: String? = ""
    private var collageName: String? = ""
    private var course_Id: String? = ""
    private val signUpViewModel: SignUpViewModel by viewModels()

    private var passing1YearList: ArrayList<String> = arrayListOf()
    private var passing2YearList: ArrayList<String> = arrayListOf()
    private var passing3YearList: ArrayList<String> = arrayListOf()
    private var passing4YearList: ArrayList<String> = arrayListOf()
    private var categoryList: ArrayList<CategoryModel> = arrayListOf()
    private val stringCategoryList: ArrayList<String> = arrayListOf()


    private var mainYearList: ArrayList<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_education_detail)
        mContext = this
        initUI()
        addListner()
        addObsereves()
    }

    private fun initUI() {

        if (intent.extras != null) {
            board = intent.getStringExtra("12_board")
            location = intent.getStringExtra("12_sc_location")
            passingyear = intent.getStringExtra("12_passing_year")
            percentage = intent.getStringExtra("12_aggeregate")
            tenBoard = intent.getStringExtra("10_board")
            tenLocation = intent.getStringExtra("10_sc_location")
            tenPassingYear = intent.getStringExtra("10_passing_year")
            tenPercentage = intent.getStringExtra("10_aggeregate")
            courseName = intent.getStringExtra("courseName")
            collageName = intent.getStringExtra("collageName")
            binding.tvCollageName.text = collageName

            if (intent.getStringExtra("edu_year") != null) {
                edu_year = intent.getStringExtra("edu_year")
            }

            if (intent.getStringExtra("year") != null) {
                year = intent.getStringExtra("year")
                binding.acMainYear.setText(year)

                if (year.equals("1st Year")) {
                    yearId = "1"
                    binding.llPassing1Year.visibility = View.VISIBLE
                    binding.llPassing2Year.visibility = View.GONE
                    binding.llPassing3Year.visibility = View.GONE
                    binding.llPassing4Year.visibility = View.GONE
                } else if (year.equals("2nd Year")) {
                    yearId = "2"
                    binding.llPassing1Year.visibility = View.VISIBLE
                    binding.llPassing2Year.visibility = View.VISIBLE
                    binding.llPassing3Year.visibility = View.GONE
                    binding.llPassing4Year.visibility = View.GONE

                } else if (year.equals("3rd Year")) {
                    yearId = "3"
                    binding.llPassing1Year.visibility = View.VISIBLE
                    binding.llPassing2Year.visibility = View.VISIBLE
                    binding.llPassing3Year.visibility = View.VISIBLE
                    binding.llPassing4Year.visibility = View.GONE

                } else {
                    yearId = "4"
                    binding.llPassing1Year.visibility = View.VISIBLE
                    binding.llPassing2Year.visibility = View.VISIBLE
                    binding.llPassing3Year.visibility = View.VISIBLE
                    binding.llPassing4Year.visibility = View.VISIBLE

                }


                if (intent.getStringExtra("edu_passyear") != null) {
                    edu_passyear = intent.getStringExtra("edu_passyear")

                    val separated: List<String> = edu_passyear!!.split(",")

                    if (year.equals("1st Year")) {
                        binding.acPassing1Year.setText(separated[0].trim())
                    } else if (year.equals("2nd Year")) {
                        binding.acPassing1Year.setText(separated[0].trim())
                        binding.acPassing2Year.setText(separated[1].trim())
                    } else if (year.equals("3rd Year")) {
                        binding.acPassing1Year.setText(separated[0].trim())
                        binding.acPassing2Year.setText(separated[1].trim())
                        binding.acPassing3Year.setText(separated[2].trim())

                    } else {
                        binding.acPassing1Year.setText(separated[0].trim())
                        binding.acPassing2Year.setText(separated[1].trim())
                        binding.acPassing3Year.setText(separated[2].trim())
                        binding.acPassing4Year.setText(separated[3].trim())
                    }


                }
                if (intent.getStringExtra("edu_percentage") != null) {
                    edu_percentage = intent.getStringExtra("edu_percentage")


                    val separated: List<String> = edu_percentage!!.split(",")


                    if (year.equals("1st Year")) {

                        binding.edtPassing1YearPercentage.setText(separated[0].trim())
                    } else if (year.equals("2nd Year")) {

                        binding.edtPassing1YearPercentage.setText(separated[0].trim())
                        binding.edtPassing2YearPercentage.setText(separated[1].trim())
                    } else if (year.equals("3rd Year")) {

                        binding.edtPassing1YearPercentage.setText(separated[0].trim())
                        binding.edtPassing2YearPercentage.setText(separated[1].trim())
                        binding.edtPassing3YearPercentage.setText(separated[2].trim())
                    } else if (year.equals("4th Year")) {
                        binding.edtPassing1YearPercentage.setText(separated[0].trim())
                        binding.edtPassing2YearPercentage.setText(separated[1].trim())
                        binding.edtPassing3YearPercentage.setText(separated[2].trim())
                        binding.edtPassing4YearPercentage.setText(separated[3].trim())
                    }


                }

            }





            binding.tvCourseName.text = courseName
            binding.edt12Board.setText(board)
            binding.edt12SchoolCity.setText(location)
            binding.edtPassingYear.setText(passingyear)
            binding.edt12Percentage.setText(percentage)

            binding.edt10Board.setText(tenBoard)
            binding.edt10SchoolCity.setText(tenLocation)
            binding.edt10PassingYear.setText(tenPassingYear)
            binding.edt10Percentage.setText(tenPercentage)
        }
        //   setMainYearData()

        setGradeData()
        set10GradeData()
        setPassing1Year()
        setPassing2Year()
        setPassing3Year()
        setPassing4Year()


        if (isNetworkAvailable(mContext)) {
            if (!PrefManager(mContext).getvalue(StaticData.school_name)
                    .equals("Admin School (Not Registred)")
            ) {
                signUpViewModel.getCategory(
                    PrefManager(mContext).getvalue(StaticData.college_id).toString(), ""
                )
            }


        } else {
            Toast.makeText(
                mContext, getString(R.string.str_error_internet_connections),
                Toast.LENGTH_SHORT
            ).show()
        }


    }

    private fun setMainYearData() {
        mainYearList.clear()
        mainYearList.add("1st Year")
        mainYearList.add("2nd Year")
        mainYearList.add("3rd Year")
        mainYearList.add("4th Year")


        val arrayAdapter: CustomArrayAdapter =
            CustomArrayAdapter(mContext, mainYearList)
        binding.acMainYear.threshold = 0
        binding.acMainYear.dropDownVerticalOffset = 0
        binding.acMainYear.setAdapter(arrayAdapter)
        binding.acMainYear.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->

                if (mainYearList[position] == "1st Year") {
                    yearId = "1"
                    binding.llPassing1Year.visibility = View.VISIBLE
                    binding.llPassing2Year.visibility = View.GONE
                    binding.llPassing3Year.visibility = View.GONE
                    binding.llPassing4Year.visibility = View.GONE


                } else if (mainYearList[position] == "2nd Year") {
                    yearId = "2"
                    binding.llPassing1Year.visibility = View.VISIBLE
                    binding.llPassing2Year.visibility = View.VISIBLE
                    binding.llPassing3Year.visibility = View.GONE
                    binding.llPassing4Year.visibility = View.GONE

                } else if (mainYearList[position] == "3rd Year") {
                    yearId = "3"
                    binding.llPassing1Year.visibility = View.VISIBLE
                    binding.llPassing2Year.visibility = View.VISIBLE
                    binding.llPassing3Year.visibility = View.VISIBLE
                    binding.llPassing4Year.visibility = View.GONE
                } else {
                    yearId = "4"
                    binding.llPassing1Year.visibility = View.VISIBLE
                    binding.llPassing2Year.visibility = View.VISIBLE
                    binding.llPassing3Year.visibility = View.VISIBLE
                    binding.llPassing4Year.visibility = View.VISIBLE
                }

            }


    }

    private fun addListner() {
        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.tvUpdateProfile.setOnClickListener {
            if (isNetworkAvailable(mContext)) {
                if (isValidate()) {
                    val educationYear = JSONArray()
                    educationYear.put("1")
                    educationYear.put("2")
                    educationYear.put("3")
                    educationYear.put("4")

                    val educationPassYear = JSONArray()
                    educationPassYear.put(binding.acPassing1Year.text.toString().trim())
                    educationPassYear.put(binding.acPassing2Year.text.toString().trim())
                    educationPassYear.put(binding.acPassing3Year.text.toString().trim())
                    educationPassYear.put(binding.acPassing4Year.text.toString().trim())


                    val educationPercentage = JSONArray()

                    educationPercentage.put(
                        binding.edtPassing1YearPercentage.text.toString().trim()
                    )
                    educationPercentage.put(
                        binding.edtPassing2YearPercentage.text.toString().trim()
                    )
                    educationPercentage.put(
                        binding.edtPassing3YearPercentage.text.toString().trim()
                    )
                    educationPercentage.put(
                        binding.edtPassing4YearPercentage.text.toString().trim()
                    )


                    val array: Array<String> = jsonArrayToArray(educationYear)
                    array.forEach { println(it) }


                    val array1: Array<String> = jsonArrayToArray(educationPassYear)
                    array1.forEach { println(it) }


                    val array2: Array<String> = jsonArrayToArray(educationPercentage)
                    array2.forEach { println(it) }

                    Log.e("course_Id","="+course_Id.toString())


                    viewModel.getEducationInformation(
                        EducationInformationRequest(
                            PrefManager(mContext).getvalue(StaticData.id).toString(),
                            "education",
                            binding.edt12Board.text.toString().trim(),
                            binding.edt12SchoolCity.text.toString().trim(),
                            binding.edtPassingYear.text.toString().trim(),
                            binding.edt12Percentage.text.toString().trim(),
                            binding.edt10Board.text.toString().trim(),
                            binding.edt10SchoolCity.text.toString().trim(),
                            binding.edt10PassingYear.text.toString().trim(),
                            binding.edt10Percentage.text.toString().trim(),
                            array,
                            array1,
                            array2,
                            yearId.toString(),
                            course_Id.toString()
                        )
                    )
                }

            } else {
                Toast.makeText(
                    mContext, getString(R.string.str_error_internet_connections),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.acGrade.setOnTouchListener { _, _ ->
            binding.acGrade.showDropDown()
            false
        }
        binding.ac10Grade.setOnTouchListener { _, _ ->
            binding.ac10Grade.showDropDown()
            false
        }

        binding.acPassing1Year.setOnTouchListener { _, _ ->
            binding.acPassing1Year.showDropDown()
            false
        }

        binding.acMainYear.setOnTouchListener { _, _ ->
            binding.acMainYear.showDropDown()
            false
        }

        binding.acSelectCourse.setOnTouchListener { _, _ ->
            binding.acSelectCourse.showDropDown()
            false
        }



        binding.acPassing2Year.setOnTouchListener { _, _ ->
            binding.acPassing2Year.showDropDown()
            false
        }

        binding.acPassing3Year.setOnTouchListener { _, _ ->
            binding.acPassing3Year.showDropDown()
            false
        }

        binding.acPassing4Year.setOnTouchListener { _, _ ->
            binding.acPassing4Year.showDropDown()
            false
        }


    }

    fun jsonArrayToArray(jsonArray: JSONArray): Array<String> {
        val list = mutableListOf<String>()

        for (i in 0 until jsonArray.length()) {
            list.add(jsonArray.getString(i))
        }

        return list.toTypedArray()
    }


    private fun setGradeData() {
        gradeList.clear()
        gradeList.add("Percentage")
        gradeList.add("CGPA")

        binding.acGrade.setText(gradeList[0])

        val arrayAdapter: CustomArrayAdapter =
            CustomArrayAdapter(mContext, gradeList)
        binding.acGrade.threshold = 0
        binding.acGrade.dropDownVerticalOffset = 0
        binding.acGrade.setAdapter(arrayAdapter)
        binding.acGrade.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->

                grade = gradeList[position]


            }

    }


    private fun set10GradeData() {
        tengradeList.clear()
        tengradeList.add("Percentage")
        tengradeList.add("CGPA")

        binding.ac10Grade.setText(tengradeList[0])

        val arrayAdapter: CustomArrayAdapter =
            CustomArrayAdapter(mContext, tengradeList)
        binding.ac10Grade.threshold = 0
        binding.ac10Grade.dropDownVerticalOffset = 0
        binding.ac10Grade.setAdapter(arrayAdapter)
        binding.ac10Grade.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->


            }

    }

    private fun setPassing1Year() {
        passing1YearList.clear()
        passing1YearList.add("2024")
        passing1YearList.add("2023")
        passing1YearList.add("2022")
        passing1YearList.add("2021")
        passing1YearList.add("2020")
        passing1YearList.add("2019")
        passing1YearList.add("2018")
        passing1YearList.add("2017")
        passing1YearList.add("2016")
        passing1YearList.add("2015")
        passing1YearList.add("2014")
        passing1YearList.add("2013")
        passing1YearList.add("2012")
        passing1YearList.add("2011")
        passing1YearList.add("2010")
        passing1YearList.add("2009")
        passing1YearList.add("2008")
        passing1YearList.add("2007")
        passing1YearList.add("2006")
        passing1YearList.add("2005")
        passing1YearList.add("2004")
        passing1YearList.add("2003")
        passing1YearList.add("2002")
        passing1YearList.add("2001")


        val arrayAdapter: CustomArrayAdapter =
            CustomArrayAdapter(mContext, passing1YearList)
        binding.acPassing1Year.threshold = 0
        binding.acPassing1Year.dropDownVerticalOffset = 0
        binding.acPassing1Year.setAdapter(arrayAdapter)
        binding.acPassing1Year.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->


            }

    }


    private fun setPassing2Year() {
        passing2YearList.clear()
        passing2YearList.add("2024")
        passing2YearList.add("2023")
        passing2YearList.add("2022")
        passing2YearList.add("2021")
        passing2YearList.add("2020")
        passing2YearList.add("2019")
        passing2YearList.add("2018")
        passing2YearList.add("2017")
        passing2YearList.add("2016")
        passing2YearList.add("2015")
        passing2YearList.add("2014")
        passing2YearList.add("2013")
        passing2YearList.add("2012")
        passing2YearList.add("2011")
        passing2YearList.add("2010")
        passing2YearList.add("2009")
        passing2YearList.add("2008")
        passing2YearList.add("2007")
        passing2YearList.add("2006")
        passing2YearList.add("2005")
        passing2YearList.add("2004")
        passing2YearList.add("2003")
        passing2YearList.add("2002")
        passing2YearList.add("2001")


        val arrayAdapter: CustomArrayAdapter =
            CustomArrayAdapter(mContext, passing2YearList)
        binding.acPassing2Year.threshold = 0
        binding.acPassing2Year.dropDownVerticalOffset = 0
        binding.acPassing2Year.setAdapter(arrayAdapter)
        binding.acPassing2Year.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->


            }

    }


    private fun setPassing3Year() {
        passing3YearList.clear()
        passing3YearList.add("2024")
        passing3YearList.add("2023")
        passing3YearList.add("2022")
        passing3YearList.add("2021")
        passing3YearList.add("2020")
        passing3YearList.add("2019")
        passing3YearList.add("2018")
        passing3YearList.add("2017")
        passing3YearList.add("2016")
        passing3YearList.add("2015")
        passing3YearList.add("2014")
        passing3YearList.add("2013")
        passing3YearList.add("2012")
        passing3YearList.add("2011")
        passing3YearList.add("2010")
        passing3YearList.add("2009")
        passing3YearList.add("2008")
        passing3YearList.add("2007")
        passing3YearList.add("2006")
        passing3YearList.add("2005")
        passing3YearList.add("2004")
        passing3YearList.add("2003")
        passing3YearList.add("2002")
        passing3YearList.add("2001")


        val arrayAdapter: CustomArrayAdapter =
            CustomArrayAdapter(mContext, passing3YearList)
        binding.acPassing3Year.threshold = 0
        binding.acPassing3Year.dropDownVerticalOffset = 0
        binding.acPassing3Year.setAdapter(arrayAdapter)
        binding.acPassing3Year.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->


            }

    }


    private fun setPassing4Year() {
        passing4YearList.clear()
        passing4YearList.add("2024")
        passing4YearList.add("2023")
        passing4YearList.add("2022")
        passing4YearList.add("2021")
        passing4YearList.add("2020")
        passing4YearList.add("2019")
        passing4YearList.add("2018")
        passing4YearList.add("2017")
        passing4YearList.add("2016")
        passing4YearList.add("2015")
        passing4YearList.add("2014")
        passing4YearList.add("2013")
        passing4YearList.add("2012")
        passing4YearList.add("2011")
        passing4YearList.add("2010")
        passing4YearList.add("2009")
        passing4YearList.add("2008")
        passing4YearList.add("2007")
        passing4YearList.add("2006")
        passing4YearList.add("2005")
        passing4YearList.add("2004")
        passing4YearList.add("2003")
        passing4YearList.add("2002")
        passing4YearList.add("2001")


        val arrayAdapter: CustomArrayAdapter =
            CustomArrayAdapter(mContext, passing4YearList)
        binding.acPassing4Year.threshold = 0
        binding.acPassing4Year.dropDownVerticalOffset = 0
        binding.acPassing4Year.setAdapter(arrayAdapter)
        binding.acPassing4Year.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->


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

        signUpViewModel.isLoading.observe(this, Observer {
            if (it) {
                binding.pbLoadData.visibility = View.VISIBLE
            } else {
                binding.pbLoadData.visibility = View.GONE
            }
        })


        signUpViewModel.isCategoryLiveData.observe(this, Observer {
            if (it.status) {
                categoryList.clear()
                categoryList.addAll(it.datas)

                if (!courseName.isNullOrEmpty()) {
                    binding.acSelectCourse.setText(courseName)
                    binding.rrCourse.isClickable = false
                    binding.rrCourse.isFocusable = false
                    binding.rrCourse.isFocusableInTouchMode = false

                    for (i in 0..categoryList.size - 1) {
                        if (courseName?.equals(categoryList[i].name) == true) {
                            course_Id = categoryList[i].id.toString()
                            getYear(course_Id.toString())
                            break
                        }
                    }

                } else {
                    binding.rrCourse.isClickable = true
                    binding.rrCourse.isFocusable = true
                    binding.rrCourse.isFocusableInTouchMode = true
                    setCategoryData(categoryList)


                }


            } else {
            }
        })

        viewModel.educationInformationLiveData.observe(this, Observer {
            if (it.status) {
                Toast.makeText(mContext, it.message, Toast.LENGTH_SHORT).show()
                PrefManager(mContext).setvalue(StaticData.profile_status, it.profile_status)

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
        if (binding.acSelectCourse.text.toString().trim().isEmpty()) {
            Toast.makeText(mContext, "Please select course", Toast.LENGTH_SHORT).show()
            isValid = false
        }
        if (binding.acMainYear.text.toString().trim().isEmpty()) {
            Toast.makeText(mContext, "Please select year", Toast.LENGTH_SHORT).show()
            isValid = false
        }
        if (binding.edt10Board.text.toString().trim().isEmpty()) {
            Toast.makeText(mContext, "Please enter 10th school name", Toast.LENGTH_SHORT).show()
            isValid = false
        } else if (binding.edt10SchoolCity.text.toString().trim().isEmpty()) {
            Toast.makeText(mContext, "Please enter 10th school city", Toast.LENGTH_SHORT).show()
            isValid = false
        } else if (binding.edt10PassingYear.text.toString().trim().isEmpty()) {
            Toast.makeText(mContext, "Please enter 10th passing year", Toast.LENGTH_SHORT).show()
            isValid = false
        } else if (binding.edt10Percentage.text.toString().trim().isEmpty()) {
            Toast.makeText(mContext, "Please enter 10th percentage", Toast.LENGTH_SHORT).show()
            isValid = false
        } else if (binding.edt12Board.text.toString().trim().isEmpty()) {
            Toast.makeText(mContext, "Please enter 12th school name", Toast.LENGTH_SHORT).show()
            isValid = false
        } else if (binding.edt12SchoolCity.text.toString().trim().isEmpty()) {
            Toast.makeText(mContext, "Please enter 12th school city", Toast.LENGTH_SHORT).show()
            isValid = false
        } else if (binding.edtPassingYear.text.toString().trim().isEmpty()) {
            Toast.makeText(mContext, "Please enter 12th passing year", Toast.LENGTH_SHORT).show()
            isValid = false
        } else if (binding.edt12Percentage.text.toString().trim().isEmpty()) {
            Toast.makeText(mContext, "Please enter 12th percentage", Toast.LENGTH_SHORT).show()
            isValid = false
        }


        return isValid
    }


    private fun setCategoryData(categoryModel: ArrayList<CategoryModel>) {
        stringCategoryList.clear()
        for (i in 0 until categoryModel.size) {
            stringCategoryList.add(categoryModel[i].name)
        }

        val arrayAdapter =
            CustomArrayAdapter(mContext, stringCategoryList)
        binding.acSelectCourse.threshold = 0
        binding.acSelectCourse.dropDownVerticalOffset = 0
        binding.acSelectCourse.setAdapter(arrayAdapter)

        binding.acSelectCourse.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                course_Id = categoryModel[position].id.toString()

                yearId = ""
                binding.acMainYear.setText("")

                getYear(course_Id.toString())
            }

    }

    private fun getYear(courseid: String) {
        signUpViewModel.getYear(courseid)

        signUpViewModel.yearLiveData.observe(this, Observer {
            if (it.status) {
                yearList.clear()
                yearList.addAll(it.datas)
                setYearData(yearList)

                if (yearList.size == 1) {
                    yearId = "1"
                    binding.llPassing1Year.visibility = View.VISIBLE
                    binding.llPassing2Year.visibility = View.GONE
                    binding.llPassing3Year.visibility = View.GONE
                    binding.llPassing4Year.visibility = View.GONE


                } else if (yearList.size == 2) {
                    yearId = "2"
                    binding.llPassing1Year.visibility = View.VISIBLE
                    binding.llPassing2Year.visibility = View.VISIBLE
                    binding.llPassing3Year.visibility = View.GONE
                    binding.llPassing4Year.visibility = View.GONE

                } else if (yearList.size == 3) {
                    yearId = "3"
                    binding.llPassing1Year.visibility = View.VISIBLE
                    binding.llPassing2Year.visibility = View.VISIBLE
                    binding.llPassing3Year.visibility = View.VISIBLE
                    binding.llPassing4Year.visibility = View.GONE
                } else {
                    yearId = "4"
                    binding.llPassing1Year.visibility = View.VISIBLE
                    binding.llPassing2Year.visibility = View.VISIBLE
                    binding.llPassing3Year.visibility = View.VISIBLE
                    binding.llPassing4Year.visibility = View.VISIBLE
                }
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
        binding.acMainYear.threshold = 0
        binding.acMainYear.dropDownVerticalOffset = 0
        binding.acMainYear.setAdapter(arrayAdapter)

        binding.acMainYear.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                yearId = yearModel[position].value.toString()


            }
    }

}