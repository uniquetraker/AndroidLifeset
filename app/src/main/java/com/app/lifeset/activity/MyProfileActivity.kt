package com.app.lifeset.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.lifeset.R
import com.app.lifeset.adapter.CourseDetailAdapter
import com.app.lifeset.databinding.ActivityMyProfileBinding
import com.app.lifeset.extensions.isNetworkAvailable
import com.app.lifeset.model.CastsModel
import com.app.lifeset.model.RelisionsModel
import com.app.lifeset.model.StudentProfileModel
import com.app.lifeset.model.StudentProfileResponse
import com.app.lifeset.util.PrefManager
import com.app.lifeset.util.StaticData
import com.app.lifeset.viewmodel.ProfileViewModel
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.analytics.FirebaseAnalytics
import com.yalantis.ucrop.UCrop
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@AndroidEntryPoint
class MyProfileActivity : AppCompatActivity() {

    lateinit var binding: ActivityMyProfileBinding
    lateinit var mContext: MyProfileActivity
    private val viewModel: ProfileViewModel by viewModels()

    private var isCamera = false
    private var isGallery = false
    private var profilePath = ""
    private var genderList: ArrayList<String> = arrayListOf()

    private var casteList: ArrayList<CastsModel> = arrayListOf()
    private var stringCastList: ArrayList<String> = arrayListOf()

    private var religionList: ArrayList<RelisionsModel> = arrayListOf()
    private var stringReligionList: ArrayList<String> = arrayListOf()

    private var encodedString = ""

    private var isUpdate = false

    lateinit var studentProfileModel: StudentProfileModel
    lateinit var firebaseAnalytics: FirebaseAnalytics
    private var title: String? = ""


    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 1001
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_profile)
        mContext = this
        initUI()
        addListner()
        setGenderData()
        addObsereves()
    }

    private fun initUI() {
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        if (isNetworkAvailable(mContext)) {
//            viewModel.getCastes(PrefManager(mContext).getvalue(StaticData.id).toString())
            viewModel.getStudentProfile(PrefManager(mContext).getvalue(StaticData.id).toString())
            viewModel.getCollageName(
                PrefManager(mContext).getvalue(StaticData.college_id).toString()
            )
            //viewModel.getProfile(PrefManager(mContext).getvalue(StaticData.id).toString())
        } else {
            Toast.makeText(
                mContext, getString(R.string.str_error_internet_connections),
                Toast.LENGTH_SHORT
            ).show()
        }

    }


    @SuppressLint("ClickableViewAccessibility")
    private fun addListner() {
        binding.apply {

            acSelectGender.setOnTouchListener { v, event ->
                acSelectGender.showDropDown()
                false
            }
            acCategory.setOnTouchListener { v, event ->
                acCategory.showDropDown()
                false
            }
            acReligions.setOnTouchListener { v, event ->
                acReligions.showDropDown()
                false
            }
            tvCreateFreelancerProfile.setOnClickListener {
                firebaseAnalytics.logEvent(
                    StaticData.createFreelancerProfileClicked, null
                )
                startActivityForResult(
                    Intent(
                        mContext,
                        CreateFreelancerProfileActivity::class.java
                    ).putExtra("title", title), 201
                )
            }
            tvViewProfileCard.setOnClickListener {
                firebaseAnalytics.logEvent(
                    StaticData.viewYourProfileClicked, null
                )
                startActivity(Intent(mContext, ProfileCardActivity::class.java))
            }

            ivGeneralDetailEdit.setOnClickListener {
                val intent = Intent(mContext, PersonalInformationActivity::class.java)
                intent.putExtra("name", binding.edtName.text.toString().trim())
                intent.putExtra("email", binding.edtEmail.text.toString().trim())
                intent.putExtra("mobile", binding.edtPhoneNumber.text.toString().trim())
                intent.putExtra("gender", binding.tvGender.text.toString().trim())
                intent.putExtra("category", binding.tvCaste.text.toString().trim())
                intent.putExtra("religion", binding.tvReligion.text.toString().trim())
                startActivityForResult(intent, 201)


                /*val httpIntent = Intent(Intent.ACTION_VIEW)
                httpIntent.data = Uri.parse("https://lifeset.co.in/student-profile-view")

                startActivity(httpIntent)*/
            }
            ivEducationDetailEdit.setOnClickListener {
                val intent = Intent(mContext, EducationDetailActivity::class.java)
                intent.putExtra("12_board", binding.tvBoard.text.toString().trim())
                intent.putExtra("12_sc_location", binding.tv12City.text.toString().trim())
                intent.putExtra("12_passing_year", binding.tvPassingYear.text.toString().trim())
                intent.putExtra("12_aggeregate", binding.tvPercentage.text.toString().trim())
                intent.putExtra("10_board", binding.tv10Board.text.toString().trim())
                intent.putExtra("10_sc_location", binding.tv10City.text.toString().trim())
                intent.putExtra("10_passing_year", binding.tv10PassingYear.text.toString().trim())
                intent.putExtra("10_aggeregate", binding.tv10Percentage.text.toString().trim())
                intent.putExtra("edu_year", studentProfileModel.edu_year)
                intent.putExtra("edu_passyear", studentProfileModel.edu_passyear)
                intent.putExtra("edu_percentage", studentProfileModel.edu_percentage)
                intent.putExtra("year", studentProfileModel.year)
                intent.putExtra("collageName", binding.tvCollageName.text.toString().trim())
                intent.putExtra("courseName", studentProfileModel.course)
                startActivityForResult(intent, 201)


                /*val httpIntent = Intent(Intent.ACTION_VIEW)
                httpIntent.data = Uri.parse("https://lifeset.co.in/student-profile-view")

                startActivity(httpIntent)*/
            }

            ivEditHomeTownAddress.setOnClickListener {
                val intent = Intent(mContext, HomeTownActivity::class.java)
                intent.putExtra("state", binding.tvStateValue.text.toString().trim())
                intent.putExtra("district", binding.tvDistrictValue.text.toString().trim())
                intent.putExtra("city", binding.tvCityValue.text.toString().trim())
                intent.putExtra("village", binding.tvVillageValue.text.toString().trim())
                intent.putExtra("address", binding.tvAddressValue.text.toString().trim())
                intent.putExtra("pincode", binding.tvPinCodeValue.text.toString().trim())
                startActivityForResult(intent, 201)

            }

            ivOtherDetailsEdit.setOnClickListener {
                val intent = Intent(mContext, SkillsActivity::class.java)
                intent.putExtra("tech_skills", binding.tvTechnicalSkills.text.toString().trim())
                intent.putExtra("prof_skills", binding.tvProffessionalSkill.text.toString().trim())
                intent.putExtra("soft_skills", binding.tvSoftSkills.text.toString().trim())
                intent.putExtra("hobbies", binding.tvInterestHobbies.text.toString().trim())
                startActivityForResult(intent, 201)

                /*  val httpIntent = Intent(Intent.ACTION_VIEW)
                  httpIntent.data = Uri.parse("https://lifeset.co.in/student-profile-view")

                  startActivity(httpIntent)*/
            }


            tvBack.setOnClickListener {
                finish()
            }

            tvUpdateProfile.setOnClickListener {
                if (isNetworkAvailable(mContext)) {
                    viewModel.doUpdateProfile(
                        PrefManager(mContext).getvalue(StaticData.id).toString(),
                        binding.edtName.text.toString().trim(),
                        binding.edtPhoneNumber.text.toString().trim(),
                        binding.edtEmail.text.toString().trim(),
                        binding.edtState.text.toString().trim(),
                        binding.edtCity.text.toString().trim(),
                        binding.edtPinCode.text.toString().trim(),
                        binding.edtAddress.text.toString().trim(),
                        binding.acSelectGender.text.toString().trim(),
                        binding.acReligions.text.toString().trim(),
                        binding.acCategory.text.toString().trim(),
                        binding.edtYear.text.toString().trim(),
                        encodedString,
                        binding.edtLanguage.text.toString().trim()
                    )
                } else {
                    Toast.makeText(
                        mContext, getString(R.string.str_error_internet_connections),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            rrProfile.setOnClickListener {
                /* if (checkPermission()) {
                     alertDialogForImagePicker()
                 } else {
                     checkPermission()
                 }*/
                showImagePickerDialog()

            }


        }

    }

    private fun showImagePickerDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.image_picker)
        dialog.setCancelable(true)

        val txtCamera = dialog.findViewById<TextView>(R.id.txtcamera)
        val txtGallery = dialog.findViewById<TextView>(R.id.txtGallery)
        val txtCancel = dialog.findViewById<TextView>(R.id.txtCancel)

        txtCamera.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CAMERA),
                    REQUEST_CAMERA_PERMISSION
                )
            } else {
                launchCamera()
            }
            dialog.dismiss()
        }

        txtGallery.setOnClickListener {
            launchGallery()
            dialog.dismiss()
        }

        txtCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun launchCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startForProfileImageResult.launch(intent)
        isCamera = true
        isGallery = false
    }

    private fun launchGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startForProfileImageResult.launch(intent)
        isGallery = true
        isCamera = false
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 201) {
            if (resultCode == Activity.RESULT_OK) {
                viewModel.getStudentProfile(
                    PrefManager(mContext).getvalue(StaticData.id).toString()
                )
            }
        } else if (resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            val resultUri = UCrop.getOutput(data!!)
            profilePath = resultUri?.path ?: return
            binding.cvProfile.setImageURI(resultUri)

            if (isNetworkAvailable(mContext)) {
                val uid: RequestBody = PrefManager(mContext).getvalue(StaticData.id).toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull())

                val file = File(profilePath)
                val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                val profileBody =
                    MultipartBody.Part.createFormData("image_path", file.name, requestFile)

                viewModel.updateProfileApi(uid, profileBody)
            } else {
                Toast.makeText(
                    mContext,
                    getString(R.string.str_error_internet_connections),
                    Toast.LENGTH_SHORT
                ).show()
            }

        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
            Toast.makeText(this, "Crop error: ${cropError?.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setGenderData() {
        genderList.clear()
        genderList.add("Male")
        genderList.add("Female")


        val arrayAdapter =
            NonFilterArrayAdapter(mContext, R.layout.newdropdown, genderList)
        binding.acSelectGender.threshold = 0
        binding.acSelectGender.dropDownVerticalOffset = 0
        binding.acSelectGender.setAdapter(arrayAdapter)

        binding.acSelectGender.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                {

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

        viewModel.collageNameLiveData.observe(this, Observer {
            if (it.status) {
                binding.tvCollageName.text = it.datas.name
            }
        })

        viewModel.updateProfile.observe(this, Observer {
            if (it.status) {
                Toast.makeText(mContext, it.msz, Toast.LENGTH_SHORT).show()
                PrefManager(mContext).setvalue(StaticData.profile_status, it.profile_status)
                finish()
            } else {
                Toast.makeText(mContext, it.msz, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.casteLiveData.observe(this, Observer {
            if (it.status) {
                casteList.clear()
                casteList.addAll(it.casts)
                religionList.clear()
                religionList.addAll(it.relisions)
                setCastAdapter(casteList)
                setReligionAdapter(religionList)

            }
        })

        viewModel.updateProfileLiveData.observe(this, Observer {
            if (it.status) {
                PrefManager(mContext).setvalue(
                    StaticData.name,
                    binding.edtName.text.toString().trim()
                )
                firebaseAnalytics.logEvent(
                    StaticData.updateProfileSuccessfullyClicked, null
                )
                Toast.makeText(mContext, it.msz, Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(mContext, it.msz, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.studentProfileLiveData.observe(this, Observer {
            if (it.status) {
                studentProfileModel = it.datas
                setData(it.datas, it)
            } else {
                Toast.makeText(mContext, it.msz, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setCastAdapter(casteList: ArrayList<CastsModel>) {
        stringCastList.clear()
        for (i in 0 until casteList.size) {
            stringCastList.add(casteList[i].name)
        }

        val arrayAdapter =
            NonFilterArrayAdapter(mContext, R.layout.newdropdown, stringCastList)
        binding.acCategory.threshold = 0
        binding.acCategory.dropDownVerticalOffset = 0
        binding.acCategory.setAdapter(arrayAdapter)

        binding.acCategory.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                {

                }
            }
    }

    private fun setReligionAdapter(religionList: ArrayList<RelisionsModel>) {
        stringReligionList.clear()
        for (i in 0 until religionList.size) {
            stringReligionList.add(religionList[i].name)
        }

        val arrayAdapter =
            NonFilterArrayAdapter(mContext, R.layout.newdropdown, stringReligionList)
        binding.acReligions.threshold = 0
        binding.acReligions.dropDownVerticalOffset = 0
        binding.acReligions.setAdapter(arrayAdapter)

        binding.acReligions.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                {

                }
            }
    }


    private fun setData(
        datas: StudentProfileModel,
        studentProfileResponse: StudentProfileResponse
    ) {
        if (datas.is_freelancer == 1) {
            title = "Update Freelancer Profile"
            binding.tvCreateFreelancerProfile.text =
                getString(R.string.str_update_your_freelancer_profile)
        } else {
            title = "Create Freelancer Profile"
            binding.tvCreateFreelancerProfile.text =
                getString(R.string.str_create_your_freelancer_profile)
        }


        binding.apply {
            Glide.with(mContext).load(datas.profile)
                .placeholder(R.drawable.ic_profile)
                .error(R.drawable.ic_profile)
                .into(cvProfile)


            tvName.text = datas.name

            PrefManager(mContext).setvalue(StaticData.name, datas.name)


            if (!datas.name.isNullOrEmpty()) {
                tvUserName.text = datas.name
            }

            if (!datas.email.isNullOrEmpty()) {
                tvEmailId.text = datas.email
            }

            if (!datas.mobile.isNullOrEmpty()) {
                tvMobileNumber.text = datas.mobile
            }


            if (!datas.gender.isNullOrEmpty()) {
                tvGender.text = datas.gender
            }

            if (!datas.religion.isNullOrEmpty()) {
                tvReligion.text = datas.religion
            }

            if (!datas.caste.isNullOrEmpty()) {
                tvCaste.text = datas.caste
            }

            if (!datas.state.isNullOrEmpty()) {
                tvStateValue.text = datas.state
            }

            if (!datas.district.isNullOrEmpty()) {
                tvDistrictValue.text = datas.district
            }



            if (!datas.city.isNullOrEmpty()) {
                tvCityValue.text = datas.city
            }



            if (!datas.village.isNullOrEmpty()) {
                tvVillageValue.text = datas.village
            }



            if (!datas.pincode.isNullOrEmpty()) {
                tvPinCodeValue.text = datas.pincode
            }


            if (!datas.address.isNullOrEmpty()) {
                tvAddressValue.text = datas.address
            }

            if (!datas.course.isNullOrEmpty()) {
                tvCourseValue.text = datas.course
            }

            if (!datas.year.isNullOrEmpty()) {
                tvYearValue.text = datas.year
            }

            if (!datas.semester.isNullOrEmpty()) {
                tvSemesterValue.text = datas.semester
            }

            if (!datas.twelveBoard.isNullOrEmpty()) {
                tvBoard.text = datas.twelveBoard
            }


            if (!datas.twelveLocation.isNullOrEmpty()) {
                tv12City.text = datas.twelveLocation
            }

            if (!datas.twelvePercentage.isNullOrEmpty()) {
                tvPercentage.text = datas.twelvePercentage
            }


            if (!datas.twelvePassingYear.isNullOrEmpty()) {
                tvPassingYear.text = datas.twelvePassingYear
            }




            if (!datas.tenBoard.isNullOrEmpty()) {
                tv10Board.text = datas.tenBoard
            }


            if (!datas.tenLocation.isNullOrEmpty()) {
                tv10City.text = datas.tenLocation
            }

            if (!datas.tenPercentage.isNullOrEmpty()) {
                tv10Percentage.text = datas.tenPercentage
            }


            if (!datas.tenPassingYear.isNullOrEmpty()) {
                tv10PassingYear.text = datas.tenPassingYear
            }


            if (!datas.tech_skills.isNullOrEmpty()) {
                tvTechnicalSkills.text = datas.tech_skills
            }



            if (!datas.prof_skills.isNullOrEmpty()) {
                tvProffessionalSkill.text = datas.prof_skills
            }



            if (!datas.soft_skills.isNullOrEmpty()) {
                tvSoftSkills.text = datas.soft_skills
            }


            if (!datas.hobbies.isNullOrEmpty()) {
                tvInterestHobbies.text = datas.hobbies
            }

            if (!datas.lang_know.isNullOrEmpty()) {
                tvLanguageValue.text = datas.lang_know
            }

            if (studentProfileResponse.coursedetail.size > 0) {
                llCourseDetails.visibility = View.VISIBLE
                rvCourseDetails.layoutManager = LinearLayoutManager(mContext)
                rvCourseDetails.setHasFixedSize(true)
                adapter = CourseDetailAdapter(mContext, studentProfileResponse.coursedetail)
                rvCourseDetails.adapter = adapter

            } else {
                llCourseDetails.visibility = View.GONE
            }



            edtName.setText(datas.name)
            edtEmail.setText(datas.email)
            edtPhoneNumber.setText(datas.mobile)
            edtCourse.setText(datas.course)
            edtYear.setText(datas.year)

            if (!datas.gender.isNullOrEmpty()) {
                acSelectGender.setText(datas.gender)
            }

            if (!datas.lang_know.isNullOrEmpty()) {
                edtLanguage.setText(datas.lang_know)
            }

            if (!datas.city.isNullOrEmpty()) {
                edtCity.setText(datas.city)
            }


            if (!datas.caste.isNullOrEmpty()) {
                acCategory.setText(datas.caste)
            }


            if (!datas.religion.isNullOrEmpty()) {
                acReligions.setText(datas.religion)
            }


            if (!datas.state.isNullOrEmpty()) {
                edtState.setText(datas.state)
            }


            if (!datas.city.isNullOrEmpty()) {
                edtCity.setText(datas.city)
            }


            if (!datas.address.isNullOrEmpty()) {
                edtAddress.setText(datas.address)
            }


            if (!datas.pincode.isNullOrEmpty()) {
                edtPinCode.setText(datas.pincode)
            }


        }


    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CAMERA_PERMISSION &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            launchCamera()
        } else {
            Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show()
        }
    }

    private fun alertDialogForImagePicker() {
        val dialogView = Dialog(this)
        dialogView.setContentView(R.layout.image_picker)
        dialogView.setCancelable(false)
        val txtcamera = dialogView.findViewById<TextView>(R.id.txtcamera)
        val txtGallery = dialogView.findViewById<TextView>(R.id.txtGallery)
        val txtCancel = dialogView.findViewById<TextView>(R.id.txtCancel)
        txtcamera.setOnClickListener { v: View? ->
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startForProfileImageResult.launch(intent)


            isCamera = true
            isGallery = false
            dialogView.dismiss()
        }
        txtGallery.setOnClickListener { v: View? ->
            ImagePicker.with(mContext).compress(1024).maxResultSize(1080, 1080).galleryOnly()
                .createIntent {
                    startForProfileImageResult.launch(it)
                }
            isCamera = false
            isGallery = true
            dialogView.dismiss()
        }
        txtCancel.setOnClickListener { v: View? -> dialogView.dismiss() }
        dialogView.show()
    }


    private fun startCrop(uri: Uri) {
        val destinationFileName = "Cropped_${System.currentTimeMillis()}.jpg"
        val destinationUri = Uri.fromFile(File(cacheDir, destinationFileName))

        val options = UCrop.Options()
        options.setCompressionQuality(80)
        options.setToolbarTitle("Crop Image")
        options.setFreeStyleCropEnabled(true)

        UCrop.of(uri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(800, 800)
            .withOptions(options)
            .start(this) // If you're in a Fragment, use requireContext() and requireActivity()
    }


    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                if (isCamera) {
                    val imageBitmap = data?.extras?.get("data") as? Bitmap
                    imageBitmap?.let {
                        val filePath = saveImageToStorage(it)
                        startCrop(Uri.fromFile(File(filePath)))
                    }
                } else if (isGallery) {
                    val uri = data?.data
                    uri?.let {
                        startCrop(it)
                    }
                }
            } else {
                Toast.makeText(this, "Cancelled or Failed", Toast.LENGTH_SHORT).show()
            }
        }

    private fun saveImageToStorage(bitmap: Bitmap): String? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_$timeStamp.jpg"
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        return try {
            val imageFile = File(storageDir, imageFileName)
            val fos = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)
            fos.close()
            imageFile.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }


    class NonFilterArrayAdapter<T>(context: Context, @LayoutRes resource: Int, objects: List<T>) :
        ArrayAdapter<T>(context, resource, objects) {

        override fun getFilter() = NonFilter()

        class NonFilter : Filter() {
            override fun performFiltering(constraint: CharSequence?) = FilterResults()

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) = Unit
        }
    }


}