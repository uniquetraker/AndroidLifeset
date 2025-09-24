package com.app.lifeset.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.app.lifeset.R
import com.app.lifeset.adapter.EventAdapter
import com.app.lifeset.adapter.ExamAdapter
import com.app.lifeset.adapter.GKAdapter
import com.app.lifeset.adapter.GeneralKnowledgeAdapter
import com.app.lifeset.adapter.JobCategoryAdapter
import com.app.lifeset.adapter.McqAdapter
import com.app.lifeset.adapter.PersonalityListAdapter
import com.app.lifeset.adapter.WallCategoryAdapter
import com.app.lifeset.databinding.ActivityMainBinding
import com.app.lifeset.extensions.isNetworkAvailable
import com.app.lifeset.model.EventData
import com.app.lifeset.model.ExamModel
import com.app.lifeset.model.GKData
import com.app.lifeset.model.GeneralKnowledgeModel
import com.app.lifeset.model.JobModel
import com.app.lifeset.model.McqModel
import com.app.lifeset.model.NotificationModel
import com.app.lifeset.model.PersonalityModel
import com.app.lifeset.model.PersonalityRequest
import com.app.lifeset.model.WallCategoryModel
import com.app.lifeset.util.PrefManager
import com.app.lifeset.util.StaticData
import com.app.lifeset.viewmodel.EventViewModel
import com.app.lifeset.viewmodel.ExamViewModel
import com.app.lifeset.viewmodel.GkViewModel
import com.app.lifeset.viewmodel.JobViewModel
import com.app.lifeset.viewmodel.McqViewModel
import com.app.lifeset.viewmodel.NotificationViewModel
import com.app.lifeset.viewmodel.PersonalityViewModel
import com.app.lifeset.viewmodel.ProfileViewModel
import com.bumptech.glide.Glide
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), WallCategoryAdapter.onItemClick,
    JobCategoryAdapter.onItemClick, PersonalityListAdapter.onItemClick {

    lateinit var binding: ActivityMainBinding
    lateinit var mContext: MainActivity
    private var actionBarDrawerToggle: ActionBarDrawerToggle? = null
    private val viewModel: JobViewModel by viewModels()
    private val examViewModel: ExamViewModel by viewModels()
    private val eventViewModel: EventViewModel by viewModels()
    private val gkViewModel: GkViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()
    private val personalityViewModel: PersonalityViewModel by viewModels()
    private val mcqViewModel: McqViewModel by viewModels()
    private var wallCategoryList: ArrayList<WallCategoryModel> = arrayListOf()
    private var jobList: ArrayList<JobModel> = arrayListOf()
    private var examList: ArrayList<ExamModel> = arrayListOf()
    private var eventList: ArrayList<EventData> = arrayListOf()
    private var gkList: ArrayList<GKData> = arrayListOf()
    private var mcqList:ArrayList<McqModel> = arrayListOf()
    private var generalKnowlegeList: ArrayList<GeneralKnowledgeModel> = arrayListOf()
    private var personalityList: ArrayList<PersonalityModel> = arrayListOf()
    private var type = "CA & Gk"
    private var model: JobModel? = null
    private var personalityModel: PersonalityModel? = null
    private var exam_id: String? = ""
    private var bookMarkPosition: Int = 0
    private var personalityPosition: Int = 0
    private var currentPosition = 0
    private var selectedPosition = 4

    private var profilePic: String = ""

    var jobId: String? = ""

    private val notificationViewModel: NotificationViewModel by viewModels()
    private var notificationList: ArrayList<NotificationModel> = arrayListOf()
    private var total: Int = 0
    private var post_url: String? = ""
    private var gkPost_url: String? = ""
    private var genralKnowledgeUrl: String? = ""
    private var callDate: Boolean = false
    lateinit var firebaseAnalytics: FirebaseAnalytics
    private var answer: String? = ""
    private var mainType: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mContext = this
        initUI()
        addListner()

    }

    private fun initUI() {
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        addObserves()
        // setNavigationDrawer()
        setData()

        /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
              notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
          }

          if (intent.extras != null) {
              callDate = intent.getBooleanExtra("callDate", false)
              if (callDate) {
                  viewModel.getSettingsMobileDateResponse(
                      PrefManager(mContext).getvalue(StaticData.id).toString()
                  )
              }
          }
  */

    }

    private fun setTypeData(mainType: String) {
        if (mainType == "Job") {
            binding.rvJob.visibility = View.GONE
            binding.rvExam.visibility = View.GONE
            binding.rvEvents.visibility = View.GONE
            binding.rvGk.visibility = View.GONE
            binding.rvMcq.visibility=View.GONE
            binding.rvPersonality.visibility = View.GONE
            binding.tvNoDataFound.visibility = View.GONE
            binding.rrNewBottom.visibility = View.GONE
            currentPosition = 0
            type = mainType.toString()
            if (isNetworkAvailable(mContext)) {
                viewModel.getJobList(
                    mainType.toString(),
                    PrefManager(mContext).getvalue(StaticData.id).toString()
                )
            } else {
                Toast.makeText(
                    mContext, getString(R.string.str_error_internet_connections),
                    Toast.LENGTH_SHORT
                ).show()
            }
            binding.adapter?.notifyDataSetChanged()
        } else if (mainType == "CA & GK") {
            binding.rvJob.visibility = View.GONE
            binding.rvExam.visibility = View.GONE
            binding.rvEvents.visibility = View.GONE
            binding.rvGk.visibility = View.GONE
            binding.rvMcq.visibility=View.GONE
            binding.tvNoDataFound.visibility = View.GONE
            binding.rrNewBottom.visibility = View.GONE
            currentPosition = 0
            type = mainType
            if (isNetworkAvailable(mContext)) {
                gkViewModel.getGKData()
            } else {
                Toast.makeText(
                    mContext, getString(R.string.str_error_internet_connections),
                    Toast.LENGTH_SHORT
                ).show()
            }
            binding.adapter?.notifyDataSetChanged()

        } else if (mainType == "Govt Exams") {
            binding.rvJob.visibility = View.GONE
            binding.rvExam.visibility = View.GONE
            binding.rvEvents.visibility = View.GONE
            binding.rvGk.visibility = View.GONE
            binding.rvMcq.visibility=View.GONE
            binding.rvPersonality.visibility = View.GONE
            binding.tvNoDataFound.visibility = View.GONE
            binding.rrNewBottom.visibility = View.GONE
            currentPosition = 0
            type = mainType
            if (isNetworkAvailable(mContext)) {
                examViewModel.getExamData(PrefManager(mContext).getvalue(StaticData.id).toString())
            } else {
                Toast.makeText(
                    mContext, getString(R.string.str_error_internet_connections),
                    Toast.LENGTH_SHORT
                ).show()
            }
            binding.adapter?.notifyDataSetChanged()
        } else if (mainType == "Events") {
            binding.rvJob.visibility = View.GONE
            binding.rvExam.visibility = View.GONE
            binding.rvEvents.visibility = View.GONE
            binding.rvGk.visibility = View.GONE
            binding.rvMcq.visibility=View.GONE
            binding.rvPersonality.visibility = View.GONE
            binding.tvNoDataFound.visibility = View.GONE
            binding.rrNewBottom.visibility = View.GONE
            currentPosition = 0
            type = mainType
            if (isNetworkAvailable(mContext)) {
                eventViewModel.getEventData()
            } else {
                Toast.makeText(
                    mContext, getString(R.string.str_error_internet_connections),
                    Toast.LENGTH_SHORT
                ).show()
            }
            binding.adapter?.notifyDataSetChanged()
        } else if (mainType == "Internship") {
            binding.rvJob.visibility = View.GONE
            binding.rvExam.visibility = View.GONE
            binding.rvEvents.visibility = View.GONE
            binding.rvGk.visibility = View.GONE
            binding.rvMcq.visibility=View.GONE
            binding.rvPersonality.visibility = View.GONE
            binding.tvNoDataFound.visibility = View.GONE
            binding.rrNewBottom.visibility = View.GONE
            currentPosition = 0
            type = mainType
            if (isNetworkAvailable(mContext)) {
                viewModel.getJobList(
                    mainType.toString(),
                    PrefManager(mContext).getvalue(StaticData.id).toString()
                )
            } else {
                Toast.makeText(
                    mContext, getString(R.string.str_error_internet_connections),
                    Toast.LENGTH_SHORT
                ).show()
            }
            binding.adapter?.notifyDataSetChanged()
        } else if (mainType == "Personality") {
            binding.rvJob.visibility = View.GONE
            binding.rvExam.visibility = View.GONE
            binding.rvEvents.visibility = View.GONE
            binding.rvGk.visibility = View.GONE
            binding.rvMcq.visibility=View.GONE
            binding.rvPersonality.visibility = View.GONE
            binding.tvNoDataFound.visibility = View.GONE
            binding.rrNewBottom.visibility = View.GONE
            currentPosition = 0
            type = mainType
            if (isNetworkAvailable(mContext)) {
                personalityViewModel.getPersonalityData(
                    PrefManager(mContext).getvalue(StaticData.id).toString()
                )
            } else {
                Toast.makeText(
                    mContext, getString(R.string.str_error_internet_connections),
                    Toast.LENGTH_SHORT
                ).show()
            }
            binding.adapter?.notifyDataSetChanged()

        } else if (mainType == "generalknowledge") {
            binding.rvJob.visibility = View.GONE
            binding.rvExam.visibility = View.GONE
            binding.rvEvents.visibility = View.GONE
            binding.rvGk.visibility = View.GONE
            binding.rvMcq.visibility=View.GONE
            binding.rvGeneralKnowlege.visibility = View.GONE
            binding.rvPersonality.visibility = View.GONE
            binding.tvNoDataFound.visibility = View.GONE
            binding.rrNewBottom.visibility = View.GONE
            currentPosition = 0
            type = mainType
            if (isNetworkAvailable(mContext)) {
                gkViewModel.getGeneralKnowledgeData(
                    PrefManager(mContext).getvalue(StaticData.language,"English")
                )
            } else {
                Toast.makeText(
                    mContext, getString(R.string.str_error_internet_connections),
                    Toast.LENGTH_SHORT
                ).show()
            }
            binding.adapter?.notifyDataSetChanged()

        }else if (mainType == "mcq") {
            binding.rvJob.visibility = View.GONE
            binding.rvExam.visibility = View.GONE
            binding.rvEvents.visibility = View.GONE
            binding.rvGk.visibility = View.GONE
            binding.rvMcq.visibility=View.GONE
            binding.rvGeneralKnowlege.visibility = View.GONE
            binding.rvPersonality.visibility = View.GONE
            binding.tvNoDataFound.visibility = View.GONE
            binding.rrNewBottom.visibility = View.GONE
            currentPosition = 0
            type = mainType
            if (isNetworkAvailable(mContext)) {
                mcqViewModel.getMcqData(
                    PrefManager(mContext).getvalue(StaticData.language,"English")
                )
            } else {
                Toast.makeText(
                    mContext, getString(R.string.str_error_internet_connections),
                    Toast.LENGTH_SHORT
                ).show()
            }
            binding.adapter?.notifyDataSetChanged()

        }
    }


    private fun addListner() {
        binding.ivMenu.setOnClickListener {
            finish()
            //binding.drawerlayout.openDrawer(GravityCompat.START)
        }
        binding.llMyNetwork.setOnClickListener {
            firebaseAnalytics.logEvent(
                StaticData.networkingClicked, null
            )
            startActivity(Intent(mContext, StudentListActivity::class.java))
        }

        binding.tvPrevious.setOnClickListener {
            binding.rvJob.setCurrentItem(currentPosition - 1, true)

        }
        binding.tvNext.setOnClickListener {
            binding.rvJob.setCurrentItem(currentPosition + 1, true)

        }

        binding.ivPrevious.setOnClickListener {
            when (type) {
                "Job" -> {
                    binding.rvJob.setCurrentItem(currentPosition - 1, true)
                }

                "Internship" -> {
                    binding.rvJob.setCurrentItem(currentPosition - 1, true)
                }

                "Govt Exams" -> {
                    binding.rvExam.setCurrentItem(currentPosition - 1, true)
                }

                "Events" -> {
                    binding.rvEvents.setCurrentItem(currentPosition - 1, true)
                    post_url = eventList[currentPosition].post_url
                }

                "CA & GK" -> {
                    binding.rvGk.setCurrentItem(currentPosition - 1, true)
                    gkPost_url = gkList[currentPosition].post_url
                }

                "Personality" -> {
                    binding.rvPersonality.setCurrentItem(currentPosition - 1, true)
                }

                "generalknowledge" -> {
                    binding.rvGeneralKnowlege.setCurrentItem(currentPosition - 1, true)
                    genralKnowledgeUrl = generalKnowlegeList[currentPosition].full_document_link
                }
                "mcq"->{
                    binding.rvMcq.setCurrentItem(currentPosition - 1, true)
                }
            }

        }
        binding.ivNext.setOnClickListener {
            when (type) {
                "Job" -> {
                    binding.rvJob.setCurrentItem(currentPosition + 1, true)
                }

                "Internship" -> {
                    binding.rvJob.setCurrentItem(currentPosition + 1, true)
                }

                "Govt Exams" -> {
                    binding.rvExam.setCurrentItem(currentPosition + 1, true)
                }

                "Events" -> {
                    binding.rvEvents.setCurrentItem(currentPosition + 1, true)
                    post_url = eventList[currentPosition].post_url
                }

                "CA & GK" -> {
                    binding.rvGk.setCurrentItem(currentPosition + 1, true)
                    gkPost_url = gkList[currentPosition].post_url
                }

                "Personality" -> {
                    binding.rvPersonality.setCurrentItem(currentPosition + 1, true)

                }

                "generalknowledge" -> {
                    binding.rvGeneralKnowlege.setCurrentItem(currentPosition + 1, true)
                    genralKnowledgeUrl = generalKnowlegeList[currentPosition].full_document_link
                }
                "mcq"->{
                    binding.rvMcq.setCurrentItem(currentPosition + 1, true)
                }
            }
        }

        binding.rrNotifications.setOnClickListener {
            firebaseAnalytics.logEvent(
                StaticData.notificationClicked, null
            )
            startActivity(Intent(mContext, NotificationActivity::class.java))
        }


        binding.tvReadMore.setOnClickListener {
            if (type == "Job") {
                firebaseAnalytics.logEvent(
                    StaticData.jobDetailsClicked, null
                )
                startActivity(
                    Intent(mContext, JobDetailActivity::class.java)
                        .putExtra("id", jobId.toString())

                )


            } else if (type == "Internship") {
                firebaseAnalytics.logEvent(
                    StaticData.jobDetailsClicked, null
                )
                startActivity(
                    Intent(mContext, JobDetailActivity::class.java)
                        .putExtra("id", jobId.toString())

                )
            } else if (type == "Govt Exams") {
                firebaseAnalytics.logEvent(
                    StaticData.examDetailsClicked, null
                )
                startActivity(
                    Intent(mContext, ExamDetailsActivity::class.java)
                        .putExtra("examId", exam_id)
                )

            } else if (type == "Events") {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(post_url)
                startActivity(intent)

            } else if (type == "CA & GK") {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(gkPost_url)
                startActivity(intent)

            }else if (type == "generalknowledge") {
                try {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(genralKnowledgeUrl)
                    startActivity(intent)
                }catch (e:Exception){

                }


            }
        }
    }

    /*
        private val notificationPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->

                if (isGranted) {
                } else {
                }
            }
    */


    private fun setData() {
        if (intent.extras != null) {
            selectedPosition = intent.getIntExtra("selectedPosition", 0)
        }


        wallCategoryList.clear()
        wallCategoryList.add(WallCategoryModel("CA & GK", getString(R.string.str_gk_ca)))
        wallCategoryList.add(WallCategoryModel("Govt Exams", getString(R.string.str_exams)))
        wallCategoryList.add(WallCategoryModel("Events", getString(R.string.str_events)))
        wallCategoryList.add(WallCategoryModel("Internship", getString(R.string.str_internship)))
        wallCategoryList.add(WallCategoryModel("Job", getString(R.string.str_jobs)))
        wallCategoryList.add(WallCategoryModel("Personality", getString(R.string.str_personality)))
        wallCategoryList.add(
            WallCategoryModel(
                "generalknowledge",
                getString(R.string.str_general_knowledge)
            )
        )
        wallCategoryList.add(
            WallCategoryModel(
                "mcq",
                getString(R.string.str_test_preparation)
            )
        )

        binding.apply {
            rvWall.layoutManager =
                LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
            rvWall.setHasFixedSize(true)

            adapter = WallCategoryAdapter(
                mContext,
                wallCategoryList,
                this@MainActivity,
                selectedPosition
            )
            rvWall.adapter = adapter

            // ✅ Smooth scroll AFTER layout is ready
            rvWall.post {
                rvWall.smoothScrollToPosition(selectedPosition)
            }
        }


        if (intent.extras != null) {
            mainType = intent.getStringExtra("mainType").toString()
            setTypeData(mainType.toString())
        }
        /*
                type = "CA & GK"

                if (isNetworkAvailable(mContext)) {
                    gkViewModel.getGKData()
                } else {
                    Toast.makeText(
                        mContext, getString(R.string.str_error_internet_connections),
                        Toast.LENGTH_SHORT
                    ).show()
                }
        */


    }

    private fun addObserves() {

        viewModel.isLoading.observe(this, Observer {
            if (it) {
                binding.pbLoadData.visibility = View.VISIBLE
            } else {
                binding.pbLoadData.visibility = View.GONE
            }
        })

        examViewModel.isLoading.observe(this, Observer {
            if (it) {
                binding.pbLoadData.visibility = View.VISIBLE
            } else {
                binding.pbLoadData.visibility = View.GONE
            }
        })

        eventViewModel.isLoading.observe(this, Observer {
            if (it) {
                binding.pbLoadData.visibility = View.VISIBLE
            } else {
                binding.pbLoadData.visibility = View.GONE
            }
        })


        gkViewModel.isLoading.observe(this, Observer {
            if (it) {
                binding.pbLoadData.visibility = View.VISIBLE
            } else {
                binding.pbLoadData.visibility = View.GONE
            }
        })

        personalityViewModel.isLoading.observe(this, Observer {
            if (it) {
                binding.pbLoadData.visibility = View.VISIBLE
            } else {
                binding.pbLoadData.visibility = View.GONE
            }
        })

        mcqViewModel.isLoading.observe(this, Observer {
            if (it) {
                binding.pbLoadData.visibility = View.VISIBLE
            } else {
                binding.pbLoadData.visibility = View.GONE
            }
        })
        /*viewModel.settingsMobileDateResponse.observe(this, Observer {
            if (it.status) {
            }
        })*/

        personalityViewModel.personalityAnswerLiveData.observe(this, Observer {
            if (it.status) {
                personalityModel?.personality_answer = answer
                Toast.makeText(mContext, it.message, Toast.LENGTH_SHORT).show()
                binding?.personalityAdapter?.notifyItemChanged(personalityPosition)
            } else {
                Toast.makeText(mContext, it.message, Toast.LENGTH_SHORT).show()
            }
        })


        viewModel.bookMarkLiveData.observe(this, Observer {
            if (it.status) {
                Toast.makeText(mContext, it.msz, Toast.LENGTH_SHORT).show()
                model?.is_bookmarked = "1"
                binding.jobAdapter?.notifyItemChanged(bookMarkPosition)
            } else {
                Toast.makeText(mContext, it.msz, Toast.LENGTH_SHORT).show()
            }
        })

        /*profileViewModel.profileLiveData.observe(this, Observer {
            if (it.status) {
                profilePic = it.profile
            }
        })*/


        /*notificationViewModel.isNotificationLiveData.observe(this, Observer {
            if (it.status == "success") {
                notificationList.clear()
                notificationList.addAll(it.data)
                if (notificationList.size > 0) {
                    binding.tvCount.visibility = View.VISIBLE
                    binding.tvCount.text = notificationList.size.toString()

                } else {
                    binding.tvCount.visibility = View.GONE
                }
            } else {
                binding.tvCount.visibility = View.GONE
            }
        })
*/

        gkViewModel.gkLiveData.observe(this, Observer {
            if (it.success) {
                gkList.clear()
                gkList = it.data
                if (gkList.size > 0) {
                    binding.rvExam.visibility = View.GONE
                    binding.rvJob.visibility = View.GONE
                    binding.rvEvents.visibility = View.GONE
                    binding.rvGk.visibility = View.VISIBLE
                    binding.rvGeneralKnowlege.visibility = View.GONE
                    binding.rvMcq.visibility=View.GONE
                    binding.rvPersonality.visibility = View.GONE
                    binding.tvNoDataFound.visibility = View.GONE
                    binding.rrNewBottom.visibility = View.VISIBLE
                    setGkAdapter(gkList)
                } else {
                    binding.rvExam.visibility = View.GONE
                    binding.rvJob.visibility = View.GONE
                    binding.rvEvents.visibility = View.GONE
                    binding.rvGk.visibility = View.GONE
                    binding.rvMcq.visibility=View.GONE
                    binding.rvGeneralKnowlege.visibility = View.GONE
                    binding.rvPersonality.visibility = View.GONE
                    binding.tvNoDataFound.visibility = View.VISIBLE
                    binding.rrNewBottom.visibility = View.GONE

                }

            } else {
                binding.rvJob.visibility = View.GONE
                binding.rvExam.visibility = View.GONE
                binding.rvEvents.visibility = View.GONE
                binding.rvGk.visibility = View.GONE
                binding.rvMcq.visibility=View.GONE
                binding.rvGeneralKnowlege.visibility = View.GONE
                binding.rvPersonality.visibility = View.GONE
                binding.tvNoDataFound.visibility = View.VISIBLE
                binding.rrNewBottom.visibility = View.GONE
            }
        })




        gkViewModel.generalKnowledgeLiveData.observe(this, Observer {
            if (it.status) {
                generalKnowlegeList.clear()
                generalKnowlegeList = it.data
                if (generalKnowlegeList.size > 0) {
                    binding.rvExam.visibility = View.GONE
                    binding.rvJob.visibility = View.GONE
                    binding.rvEvents.visibility = View.GONE
                    binding.rvGk.visibility = View.GONE
                    binding.rvMcq.visibility=View.GONE
                    binding.rvGeneralKnowlege.visibility = View.VISIBLE
                    binding.rvPersonality.visibility = View.GONE
                    binding.tvNoDataFound.visibility = View.GONE
                    binding.rrNewBottom.visibility = View.VISIBLE
                    setGeneralKnowledgeAdapter(generalKnowlegeList)
                } else {
                    binding.rvExam.visibility = View.GONE
                    binding.rvJob.visibility = View.GONE
                    binding.rvEvents.visibility = View.GONE
                    binding.rvGk.visibility = View.GONE
                    binding.rvMcq.visibility=View.GONE
                    binding.rvGeneralKnowlege.visibility = View.GONE
                    binding.rvPersonality.visibility = View.GONE
                    binding.tvNoDataFound.visibility = View.VISIBLE
                    binding.rrNewBottom.visibility = View.GONE

                }

            } else {
                binding.rvJob.visibility = View.GONE
                binding.rvExam.visibility = View.GONE
                binding.rvEvents.visibility = View.GONE
                binding.rvGk.visibility = View.GONE
                binding.rvMcq.visibility=View.GONE
                binding.rvGeneralKnowlege.visibility = View.GONE
                binding.rvPersonality.visibility = View.GONE
                binding.tvNoDataFound.visibility = View.VISIBLE
                binding.rrNewBottom.visibility = View.GONE
            }
        })





        personalityViewModel.personalityLiveData.observe(this, Observer {
            if (it.success) {
                personalityList.clear()
                personalityList = it.data
                if (personalityList.size > 0) {
                    binding.rvExam.visibility = View.GONE
                    binding.rvJob.visibility = View.GONE
                    binding.rvEvents.visibility = View.GONE
                    binding.rvGk.visibility = View.GONE
                    binding.rvMcq.visibility=View.GONE
                    binding.rvGeneralKnowlege.visibility = View.GONE
                    binding.rvPersonality.visibility = View.VISIBLE
                    binding.tvNoDataFound.visibility = View.GONE
                    binding.rrNewBottom.visibility = View.VISIBLE
                    setPersonalityAdapter(personalityList)
                } else {
                    binding.rvExam.visibility = View.GONE
                    binding.rvJob.visibility = View.GONE
                    binding.rvEvents.visibility = View.GONE
                    binding.rvGk.visibility = View.GONE
                    binding.rvMcq.visibility=View.GONE
                    binding.rvGeneralKnowlege.visibility = View.GONE
                    binding.rvPersonality.visibility = View.GONE
                    binding.tvNoDataFound.visibility = View.VISIBLE
                    binding.rrNewBottom.visibility = View.GONE
                }

            } else {
                binding.rvJob.visibility = View.GONE
                binding.rvExam.visibility = View.GONE
                binding.rvEvents.visibility = View.GONE
                binding.rvGk.visibility = View.GONE
                binding.rvMcq.visibility=View.GONE
                binding.rvGeneralKnowlege.visibility = View.GONE
                binding.rvPersonality.visibility = View.GONE
                binding.tvNoDataFound.visibility = View.VISIBLE
                binding.rrNewBottom.visibility = View.GONE
            }

        })




        mcqViewModel.mcqLiveData.observe(this, Observer {
            if (it.status) {
                mcqList.clear()
                mcqList = it.data
                if (mcqList.size > 0) {
                    binding.rvExam.visibility = View.GONE
                    binding.rvJob.visibility = View.GONE
                    binding.rvEvents.visibility = View.GONE
                    binding.rvGk.visibility = View.GONE
                    binding.rvGeneralKnowlege.visibility = View.GONE
                    binding.rvPersonality.visibility = View.GONE
                    binding.rvMcq.visibility=View.VISIBLE
                    binding.tvNoDataFound.visibility = View.GONE
                    binding.rrNewBottom.visibility = View.VISIBLE
                    setMcqAdapter(mcqList)
                } else {
                    binding.rvExam.visibility = View.GONE
                    binding.rvJob.visibility = View.GONE
                    binding.rvEvents.visibility = View.GONE
                    binding.rvGk.visibility = View.GONE
                    binding.rvGeneralKnowlege.visibility = View.GONE
                    binding.rvMcq.visibility=View.GONE
                    binding.rvPersonality.visibility = View.GONE
                    binding.tvNoDataFound.visibility = View.VISIBLE
                    binding.rrNewBottom.visibility = View.GONE
                }

            } else {
                binding.rvJob.visibility = View.GONE
                binding.rvExam.visibility = View.GONE
                binding.rvEvents.visibility = View.GONE
                binding.rvGk.visibility = View.GONE
                binding.rvMcq.visibility=View.GONE
                binding.rvGeneralKnowlege.visibility = View.GONE
                binding.rvPersonality.visibility = View.GONE
                binding.tvNoDataFound.visibility = View.VISIBLE
                binding.rrNewBottom.visibility = View.GONE
            }

        })


        examViewModel.examLiveData.observe(this, Observer
        {
            if (it.success) {
                examList.clear()
                examList = it.data
                if (examList.size > 0) {
                    binding.rvExam.visibility = View.VISIBLE
                    binding.rvJob.visibility = View.GONE
                    binding.rvEvents.visibility = View.GONE
                    binding.rvGk.visibility = View.GONE
                    binding.rvMcq.visibility=View.GONE
                    binding.rvGeneralKnowlege.visibility = View.GONE
                    binding.rvPersonality.visibility = View.GONE
                    binding.tvNoDataFound.visibility = View.GONE
                    binding.rrNewBottom.visibility = View.VISIBLE
                    setExamAdapter(examList)
                } else {
                    binding.rvExam.visibility = View.GONE
                    binding.rvJob.visibility = View.GONE
                    binding.rvEvents.visibility = View.GONE
                    binding.rvGk.visibility = View.GONE
                    binding.rvMcq.visibility=View.GONE
                    binding.rvGeneralKnowlege.visibility = View.GONE
                    binding.rvPersonality.visibility = View.GONE
                    binding.tvNoDataFound.visibility = View.VISIBLE
                    binding.rrNewBottom.visibility = View.GONE
                }

            } else {
                binding.rvJob.visibility = View.GONE
                binding.rvExam.visibility = View.GONE
                binding.rvEvents.visibility = View.GONE
                binding.rvGk.visibility = View.GONE
                binding.rvMcq.visibility=View.GONE
                binding.rvGeneralKnowlege.visibility = View.GONE
                binding.rvPersonality.visibility = View.GONE
                binding.tvNoDataFound.visibility = View.VISIBLE
                binding.rrNewBottom.visibility = View.GONE
            }
        })




        eventViewModel.eventLiveData.observe(this, Observer
        {
            if (it.success) {
                eventList.clear()
                eventList = it.data
                if (eventList.size > 0) {
                    binding.rvJob.visibility = View.GONE
                    binding.rvExam.visibility = View.GONE
                    binding.rvGk.visibility = View.GONE
                    binding.rvGeneralKnowlege.visibility = View.GONE
                    binding.rvPersonality.visibility = View.GONE
                    binding.rvMcq.visibility=View.GONE
                    binding.rvEvents.visibility = View.VISIBLE
                    binding.tvNoDataFound.visibility = View.GONE
                    binding.rrNewBottom.visibility = View.VISIBLE
                    setEventAdapter(eventList)
                } else {
                    binding.rvJob.visibility = View.GONE
                    binding.rvExam.visibility = View.GONE
                    binding.rvEvents.visibility = View.GONE
                    binding.rvGk.visibility = View.GONE
                    binding.rvMcq.visibility=View.GONE
                    binding.rvGeneralKnowlege.visibility = View.GONE
                    binding.rvPersonality.visibility = View.GONE
                    binding.tvNoDataFound.visibility = View.VISIBLE
                    binding.rrNewBottom.visibility = View.GONE
                }

            } else {
                binding.rvJob.visibility = View.GONE
                binding.rvExam.visibility = View.GONE
                binding.rvEvents.visibility = View.GONE
                binding.rvGk.visibility = View.GONE
                binding.rvMcq.visibility=View.GONE
                binding.rvGeneralKnowlege.visibility = View.GONE
                binding.rvPersonality.visibility = View.GONE
                binding.tvNoDataFound.visibility = View.VISIBLE
                binding.rrNewBottom.visibility = View.GONE
            }
        })




        viewModel.jobLiveData.observe(this, Observer
        {
            if (it.status) {
                jobList.clear()
                jobList = it.datas


                if (jobList.size > 0) {
                    binding.rvJob.visibility = View.VISIBLE
                    binding.rvExam.visibility = View.GONE
                    binding.rvEvents.visibility = View.GONE
                    binding.rvGk.visibility = View.GONE
                    binding.rvGeneralKnowlege.visibility = View.GONE
                    binding.rvMcq.visibility=View.GONE
                    binding.rvPersonality.visibility = View.GONE
                    binding.tvNoDataFound.visibility = View.GONE
                    binding.rrNewBottom.visibility = View.VISIBLE
                    setAdapter(jobList)
                } else {
                    binding.rvJob.visibility = View.GONE
                    binding.rvExam.visibility = View.GONE
                    binding.rvEvents.visibility = View.GONE
                    binding.rvGk.visibility = View.GONE
                    binding.rvGeneralKnowlege.visibility = View.GONE
                    binding.rvMcq.visibility=View.GONE
                    binding.rvPersonality.visibility = View.GONE
                    binding.tvNoDataFound.visibility = View.VISIBLE
                    binding.rrNewBottom.visibility = View.GONE
                }

            } else {
                binding.rvJob.visibility = View.GONE
                binding.rvExam.visibility = View.GONE
                binding.rvEvents.visibility = View.GONE
                binding.rvGk.visibility = View.GONE
                binding.rvMcq.visibility=View.GONE
                binding.rvGeneralKnowlege.visibility = View.GONE
                binding.rvPersonality.visibility = View.GONE
                binding.tvNoDataFound.visibility = View.VISIBLE
                binding.rrNewBottom.visibility = View.GONE
            }
        })
    }

    private fun setAdapter(jobList: ArrayList<JobModel>) {
        binding.apply {
            jobAdapter = JobCategoryAdapter(mContext, jobList, this@MainActivity)
            rvJob.adapter = jobAdapter
            jobAdapter?.notifyDataSetChanged()
        }




        binding.rvJob.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                /*
                                if (currentPage <= total && position == binding.jobAdapter?.itemCount!! - 1) {
                                    currentPage++
                                    viewModel.getJobList(
                                        type.toString(),
                                        PrefManager(mContext).getvalue(StaticData.id).toString()
                                    )
                                }
                */


                currentPosition = position

                if (jobList.size > 0) {
                    if (jobList[position].type.equals("Job") || jobList[position].type.equals("Internship")) {

                        jobId = jobList[position].id.toString()
                        binding.tvReadMore.visibility = View.VISIBLE
                    } else {
                        binding.tvReadMore.visibility = View.INVISIBLE
                    }
                }

                if (currentPosition == 0) {
                    binding.ivPrevious.visibility = View.GONE
                } else {
                    binding.ivPrevious.visibility = View.VISIBLE

                }

                if (currentPosition == jobList.size - 1) {
                    binding.ivNext.visibility = View.GONE
                } else {
                    binding.ivNext.visibility = View.VISIBLE
                }


            }
        })

    }


    private fun setEventAdapter(eventList: ArrayList<EventData>) {
        binding.apply {
            //  val reversedList: ArrayList<EventData> = eventList.reversed() as ArrayList<EventData>
            eventAdapter = EventAdapter(mContext, eventList, this@MainActivity)
            rvEvents.adapter = eventAdapter
            eventAdapter?.notifyDataSetChanged()
        }




        binding.rvEvents.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                /*
                                if (currentPage <= total && position == binding.jobAdapter?.itemCount!! - 1) {
                                    currentPage++
                                    viewModel.getJobList(
                                        type.toString(),
                                        PrefManager(mContext).getvalue(StaticData.id).toString()
                                    )
                                }
                */


                currentPosition = position


                if (eventList.size > 0) {
                    if (eventList[currentPosition].type.equals("event")) {
                        post_url = eventList[currentPosition].post_url
                        if (!eventList[currentPosition].post_url.isNullOrEmpty()) {
                            binding.tvReadMore.visibility = View.VISIBLE
                        } else {
                            binding.tvReadMore.visibility = View.INVISIBLE
                        }
                    } else {
                        binding.tvReadMore.visibility = View.INVISIBLE
                    }

                }

                if (currentPosition == 0) {
                    binding.ivPrevious.visibility = View.GONE
                } else {
                    binding.ivPrevious.visibility = View.VISIBLE

                }

                if (currentPosition == eventList.size - 1) {
                    binding.ivNext.visibility = View.GONE
                } else {
                    binding.ivNext.visibility = View.VISIBLE
                }


            }
        })

    }


    private fun setExamAdapter(examList: ArrayList<ExamModel>) {
        binding.apply {
            //  val reversedList: ArrayList<ExamModel> = examList.reversed() as ArrayList<ExamModel>

            examAdapter = ExamAdapter(mContext, examList, this@MainActivity)
            rvExam.adapter = examAdapter
            examAdapter?.notifyDataSetChanged()
        }




        binding.rvExam.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                /*
                                if (currentPage <= total && position == binding.jobAdapter?.itemCount!! - 1) {
                                    currentPage++
                                    viewModel.getJobList(
                                        type.toString(),
                                        PrefManager(mContext).getvalue(StaticData.id).toString()
                                    )
                                }
                */


                currentPosition = position

                if (examList.size > 0) {
                    if (examList[currentPosition].type.equals("Government Exam")) {
                        exam_id = examList[currentPosition].id.toString()
                        binding.tvReadMore.visibility = View.VISIBLE
                    } else {
                        binding.tvReadMore.visibility = View.INVISIBLE
                    }
                    //  jobId = jobList[position].id.toString()
                }

                if (currentPosition == 0) {
                    binding.ivPrevious.visibility = View.GONE
                } else {
                    binding.ivPrevious.visibility = View.VISIBLE
                }
                if (currentPosition == examList.size - 1) {
                    binding.ivNext.visibility = View.GONE
                } else {
                    binding.ivNext.visibility = View.VISIBLE
                }
            }
        })

    }


    private fun setGkAdapter(gkList: ArrayList<GKData>) {
        binding.apply {
            //  val reversedList: ArrayList<GKData> = gkList.reversed() as ArrayList<GKData>

            gkAdapter = GKAdapter(mContext, gkList, this@MainActivity)
            rvGk.adapter = gkAdapter
            gkAdapter?.notifyDataSetChanged()
        }




        binding.rvGk.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                /*
                                if (currentPage <= total && position == binding.jobAdapter?.itemCount!! - 1) {
                                    currentPage++
                                    viewModel.getJobList(
                                        type.toString(),
                                        PrefManager(mContext).getvalue(StaticData.id).toString()
                                    )
                                }
                */


                currentPosition = position



                if (gkList.size > 0) {
                    if (gkList[currentPosition].type.equals("GK")) {
                        gkPost_url = gkList[currentPosition].post_url
                        if (!gkList[currentPosition].post_url.isNullOrEmpty()) {
                            binding.tvReadMore.visibility = View.VISIBLE
                        } else {
                            binding.tvReadMore.visibility = View.INVISIBLE
                        }
                    } else {
                        binding.tvReadMore.visibility = View.INVISIBLE
                    }
                }

                if (currentPosition == 0) {
                    binding.ivPrevious.visibility = View.GONE
                } else {
                    binding.ivPrevious.visibility = View.VISIBLE

                }

                if (currentPosition == gkList.size - 1) {
                    binding.ivNext.visibility = View.GONE
                } else {
                    binding.ivNext.visibility = View.VISIBLE
                }


            }
        })

    }


    private fun setGeneralKnowledgeAdapter(generalKnowledgeList: ArrayList<GeneralKnowledgeModel>) {
        binding.apply {
            generalKnowledgeAdapter =
                GeneralKnowledgeAdapter(mContext, generalKnowledgeList, this@MainActivity)
            rvGeneralKnowlege.adapter = generalKnowledgeAdapter
            generalKnowledgeAdapter?.notifyDataSetChanged()
        }




        binding.rvGeneralKnowlege.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                /*
                                if (currentPage <= total && position == binding.jobAdapter?.itemCount!! - 1) {
                                    currentPage++
                                    viewModel.getJobList(
                                        type.toString(),
                                        PrefManager(mContext).getvalue(StaticData.id).toString()
                                    )
                                }
                */


                currentPosition = position



                if (generalKnowledgeList.size > 0) {
                    if (generalKnowledgeList[currentPosition].type.equals("GK")) {
                        genralKnowledgeUrl = generalKnowledgeList[currentPosition].full_document_link
                        if (!generalKnowledgeList[currentPosition].documnet_link.isNullOrEmpty()) {
                            binding.tvReadMore.visibility = View.VISIBLE
                        } else {
                            binding.tvReadMore.visibility = View.INVISIBLE
                        }
                    } else {
                        binding.tvReadMore.visibility = View.INVISIBLE
                    }
                }

                if (currentPosition == 0) {
                    binding.ivPrevious.visibility = View.GONE
                } else {
                    binding.ivPrevious.visibility = View.VISIBLE

                }

                if (currentPosition == generalKnowledgeList.size - 1) {
                    binding.ivNext.visibility = View.GONE
                } else {
                    binding.ivNext.visibility = View.VISIBLE
                }


            }
        })

    }


    private fun setPersonalityAdapter(personalityList: ArrayList<PersonalityModel>) {
        binding.apply {

            personalityAdapter =
                PersonalityListAdapter(mContext, personalityList, this@MainActivity)
            rvPersonality.adapter = personalityAdapter
            personalityAdapter?.notifyDataSetChanged()
        }




        binding.rvPersonality.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)



                currentPosition = position



                if (personalityList.size > 0) {
                    binding.tvPrevious.visibility = View.GONE
                    binding.tvNext.visibility = View.GONE
                    binding.tvReadMore.visibility = View.GONE

                    if (currentPosition == 0) {
                        binding.ivPrevious.visibility = View.GONE
                    } else {
                        binding.ivPrevious.visibility = View.VISIBLE

                    }

                    if (currentPosition == personalityList.size - 1) {
                        binding.ivNext.visibility = View.GONE
                    } else {
                        binding.ivNext.visibility = View.VISIBLE
                    }

                }
            }
        })

    }


    private fun setMcqAdapter(mcqList: ArrayList<McqModel>) {
        binding.apply {

            mcqAdapter =
                McqAdapter(mContext, mcqList, this@MainActivity)
            rvMcq.adapter = mcqAdapter
            mcqAdapter?.notifyDataSetChanged()
        }




        binding.rvMcq.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)



                currentPosition = position



                if (mcqList.size > 0) {
                    binding.tvPrevious.visibility = View.GONE
                    binding.tvNext.visibility = View.GONE
                    binding.tvReadMore.visibility = View.GONE

                    if (currentPosition == 0) {
                        binding.ivPrevious.visibility = View.GONE
                    } else {
                        binding.ivPrevious.visibility = View.VISIBLE

                    }

                    if (currentPosition == mcqList.size - 1) {
                        binding.ivNext.visibility = View.GONE
                    } else {
                        binding.ivNext.visibility = View.VISIBLE
                    }

                }
            }
        })

    }


    private fun setNavigationDrawer() {
        binding.navigationview.bringToFront()
        binding.navigationview.setNavigationItemSelectedListener { item ->
            val id = item.itemId
            when (id) {
                R.id.menu_home -> {
                    startActivity(Intent(mContext, MainActivity::class.java))
                }

                R.id.menu_profile -> {
                    /* if (PrefManager(mContext).getvalue(StaticData.college_id)
                             ?.isEmpty() == true || PrefManager(mContext).getvalue(StaticData.college_id)
                             .equals("0")
                     ) {
                         startActivity(Intent(mContext, ActivateAccountActivity::class.java))
                     } else {
                         startActivity(Intent(mContext, MyProfileActivity::class.java))
                     }
    */
                    firebaseAnalytics.logEvent(
                        StaticData.myProfileClicked, null
                    )
                    startActivity(Intent(mContext, MyProfileActivity::class.java))


                }

                R.id.menu_changepassword -> {

                    firebaseAnalytics.logEvent(
                        StaticData.changePasswordClicked, null
                    )
                    startActivity(Intent(mContext, ChangePasswordActivity::class.java))
                }

                R.id.menu_applyjobs -> {
                    firebaseAnalytics.logEvent(
                        StaticData.appliedJobsClicked, null
                    )
                    startActivity(Intent(mContext, AppliedJobListActivity::class.java))
                }

                R.id.menu_studentList -> {
                    firebaseAnalytics.logEvent(
                        StaticData.freelancerJobsClicked, null
                    )
                    startActivity(Intent(mContext, FreelancerJobsActivity::class.java))
                }

                R.id.menu_inviteList -> {

                    startActivity(Intent(mContext, InviteListActivity::class.java))
                }

                R.id.menu_network -> {
                    firebaseAnalytics.logEvent(
                        StaticData.networkingClicked, null
                    )
                    startActivity(Intent(mContext, StudentListActivity::class.java))
                }

                R.id.menu_chat -> {
                    startActivity(Intent(mContext, ChatListActivity::class.java))
                }

                R.id.menu_aboutus -> {
                    firebaseAnalytics.logEvent(
                        StaticData.aboutUsClicked, null
                    )
                    startActivity(
                        Intent(mContext, CMSPageActivity::class.java)
                            .putExtra("type", "About Us")
                            .putExtra("pageType", "about-us")
                    )
                }

                R.id.menu_termsconditions -> {
                    firebaseAnalytics.logEvent(
                        StaticData.termsConditionClicked, null
                    )
                    startActivity(
                        Intent(mContext, CMSPageActivity::class.java)
                            .putExtra("type", "Terms & Conditions")
                            .putExtra("pageType", "terms-and-condition")
                    )

                }

                R.id.menu_privacypolicy -> {
                    firebaseAnalytics.logEvent(
                        StaticData.privacyPolicyClicked, null
                    )

                    startActivity(
                        Intent(mContext, CMSPageActivity::class.java)
                            .putExtra("type", "Privacy Policy")
                            .putExtra("pageType", "privacy-policy")
                    )

                }

                R.id.menu_share -> {
                    try {
                        val shareIntent = Intent(Intent.ACTION_SEND)
                        shareIntent.type = "text/plain"
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "")
                        var shareMessage =
                            "\uD83D\uDE80 Attention Students & Recent Graduates! \uD83C\uDF93✨\n" +
                                    "\n" +
                                    "Looking for the ultimate student platform? LifeSet is here to help you! \uD83C\uDF1F\n" +
                                    "\n" +
                                    "\uD83D\uDD25 What you get?\n" +
                                    "✅ Find the best Colleges & Courses \uD83C\uDF93\n" +
                                    "✅ Network & Chat with fellow students \uD83E\uDD1D\n" +
                                    "✅ Discover Freelancing & Job Opportunities \uD83D\uDCBC\n" +
                                    "✅ Stay updated on Govt Jobs & Exams \uD83D\uDCE2\n" +
                                    "✅ Get the latest News & Current Affairs \uD83D\uDCF0\n" +
                                    "✅ Explore College Events & Fests \uD83C\uDF89\n" +
                                    "✅ Connect with Alumni for Career Guidance \uD83D\uDC69\u200D\uD83D\uDCBC\uD83D\uDC68\u200D\uD83D\uDCBC\n" +
                                    "✅ Find Internships & Placements \uD83D\uDE80\n" +
                                    "\n" +
                                    "\uD83D\uDCF2 Download Now & Build Your Future!"
                        shareMessage =
                            """
                            ${shareMessage + "https://play.google.com/store/apps/details?id=" + packageName}
                            
                            
                            """.trimIndent()
                        shareIntent.putExtra(
                            Intent.EXTRA_TEXT,
                            shareMessage + "\n" + "\uD83D\uDC49 Share this with your friends and help them grow too! \uD83D\uDE80"
                        )
                        startActivity(Intent.createChooser(shareIntent, "choose one"))
                    } catch (e: Exception) {
                        //e.toString();
                    }
                }

                R.id.menu_logout -> {
                    logoutDialog()
                }

            }
            //   binding.drawerlayout.closeDrawer(GravityCompat.START)
            true
        }
        /*actionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            binding.drawerlayout,
            null,
            R.string.open_drawer,
            R.string.close_drawer
        ) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                binding.drawerlayout.openDrawer(GravityCompat.START)
                setHeaderData()
            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                binding.drawerlayout.closeDrawer(GravityCompat.START)
            }
        }
        binding.drawerlayout.setDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle?.syncState()*/
    }


    @SuppressLint("SetTextI18n")
    private fun setHeaderData() {
        val hView = binding.navigationview.getHeaderView(0)
        val cvProfile = hView.findViewById<CircleImageView>(R.id.ivImage)
        val name = hView.findViewById<TextView>(R.id.tvName)


        name.text = PrefManager(mContext).getvalue(StaticData.name)
        Glide.with(mContext).load(profilePic)
            .placeholder(R.drawable.ic_profile).error(R.drawable.ic_profile).into(cvProfile)

        val tvVersion = hView.findViewById<TextView>(R.id.tvAppVersion)
        try {
            val pInfo: PackageInfo =
                getPackageManager().getPackageInfo(getPackageName(), 0)
            val version = pInfo.versionName
            tvVersion.text = "Version:- " + version

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

    }

    private fun logoutDialog() {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to Logout?")

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            val fcmToken = PrefManager(mContext).getvalue(StaticData.fcmToken)
            PrefManager(mContext).clearValue()
            PrefManager(mContext).setvalue(StaticData.fcmToken, fcmToken)
            startActivity(Intent(mContext, LoginActivity::class.java))
            finishAffinity()

        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            dialog.dismiss()
        }

        builder.show()
    }

    override fun onClick(pos: Int, model: WallCategoryModel) {
        type = model.id
        when (type) {
            "Job" -> {
                firebaseAnalytics.logEvent(
                    StaticData.jobsClicked, null
                )
                setJobData(pos, model)
            }

            "Internship" -> {
                firebaseAnalytics.logEvent(
                    StaticData.internshipClicked, null
                )
                setInternShipData(pos, model)
            }

            "Govt Exams" -> {
                firebaseAnalytics.logEvent(
                    StaticData.govtexamsClicked, null
                )
                setExamData(pos, model)
            }

            "Events" -> {
                firebaseAnalytics.logEvent(
                    StaticData.eventsClicked, null
                )
                setEventData(pos, model)
            }

            "CA & GK" -> {
                firebaseAnalytics.logEvent(
                    StaticData.gkClicked, null
                )
                setGKData(pos, model)
            }

            "Personality" -> {
                firebaseAnalytics.logEvent(
                    StaticData.personalityClicked, null
                )
                setPersonalityData(pos, model)
            }
            "generalknowledge"->{
                firebaseAnalytics.logEvent(
                    StaticData.generalknowledgeClicked, null
                )
                setGeneralKnowledgeData(pos, model)
            }
            "mcq"->{
                firebaseAnalytics.logEvent(
                    StaticData.mcqClicked, null
                )
                setMcqData(pos, model)
            }

        }
    }

    override fun onClick(model: JobModel, pos: Int) {
        firebaseAnalytics.logEvent(
            StaticData.jobDetailsClicked, null
        )
        startActivity(
            Intent(mContext, JobDetailActivity::class.java)
                .putExtra("id", model.id.toString())
                .putExtra("position", pos)
                .putExtra("model", Gson().toJson(model))
        )


    }

    override fun onBookMarkClick(position: Int, jobmodel: JobModel) {
        if (isNetworkAvailable(mContext)) {
            if (jobmodel?.is_bookmarked?.isEmpty() == true) {
                model = jobmodel
                bookMarkPosition = position
                viewModel.addBookMark(
                    PrefManager(mContext).getvalue(StaticData.college_id).toString(),
                    PrefManager(mContext).getvalue(StaticData.id).toString(),
                    jobmodel.id.toString()
                )
            } else {
                Toast.makeText(mContext, "Exam is already saved", Toast.LENGTH_SHORT).show()
            }

        } else {
            Toast.makeText(
                mContext, getString(R.string.str_error_internet_connections),
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 201) {
            if (resultCode == Activity.RESULT_OK) {
                if (isNetworkAvailable(mContext)) {
                    viewModel.getJobList(
                        type,
                        PrefManager(mContext).getvalue(StaticData.id).toString()
                    )
                } else {
                    Toast.makeText(
                        mContext, getString(R.string.str_error_internet_connections),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    /*override fun onBackPressed() {
        // super.onBackPressed()
        exitAppDialog()

    }*/

    private fun exitAppDialog() {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Are you sure you want to exit app?")

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->

            val a = Intent(Intent.ACTION_MAIN)
            a.addCategory(Intent.CATEGORY_HOME)
            a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(a)
            finishAffinity()
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            dialog.dismiss()
        }

        builder.show()
    }

    private fun setGeneralKnowledgeData(pos: Int, model: WallCategoryModel) {
        binding.rvJob.visibility = View.GONE
        binding.rvExam.visibility = View.GONE
        binding.rvEvents.visibility = View.GONE
        binding.rvGk.visibility = View.GONE
        binding.rvGeneralKnowlege.visibility = View.GONE
        binding.rvGeneralKnowlege.visibility = View.GONE
        binding.rvMcq.visibility=View.GONE
        binding.rvPersonality.visibility = View.GONE
        binding.tvNoDataFound.visibility = View.GONE
        binding.rrNewBottom.visibility = View.GONE
        currentPosition = 0
        type = model.id
        if (isNetworkAvailable(mContext)) {
            gkViewModel.getGeneralKnowledgeData(
                PrefManager(mContext).getvalue(StaticData.language,"English")
            )
        } else {
            Toast.makeText(
                mContext, getString(R.string.str_error_internet_connections),
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.adapter?.notifyDataSetChanged()

    }


    private fun setMcqData(pos: Int, model: WallCategoryModel) {
        binding.rvJob.visibility = View.GONE
        binding.rvExam.visibility = View.GONE
        binding.rvEvents.visibility = View.GONE
        binding.rvGk.visibility = View.GONE
        binding.rvMcq.visibility=View.GONE
        binding.rvGeneralKnowlege.visibility = View.GONE
        binding.rvPersonality.visibility = View.GONE
        binding.tvNoDataFound.visibility = View.GONE
        binding.rrNewBottom.visibility = View.GONE
        currentPosition = 0
        type = model.id
        if (isNetworkAvailable(mContext)) {
            mcqViewModel.getMcqData(
                PrefManager(mContext).getvalue(StaticData.language,"English")
            )
        } else {
            Toast.makeText(
                mContext, getString(R.string.str_error_internet_connections),
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.adapter?.notifyDataSetChanged()

    }


    private fun setJobData(pos: Int, model: WallCategoryModel) {
        binding.rvJob.visibility = View.GONE
        binding.rvExam.visibility = View.GONE
        binding.rvEvents.visibility = View.GONE
        binding.rvGk.visibility = View.GONE
        binding.rvMcq.visibility=View.GONE
        binding.rvGeneralKnowlege.visibility = View.GONE
        binding.rvPersonality.visibility = View.GONE
        binding.tvNoDataFound.visibility = View.GONE
        binding.rrNewBottom.visibility = View.GONE
        currentPosition = 0
        type = model.id.toString()
        if (isNetworkAvailable(mContext)) {
            viewModel.getJobList(
                model.id.toString(),
                PrefManager(mContext).getvalue(StaticData.id).toString()
            )
        } else {
            Toast.makeText(
                mContext, getString(R.string.str_error_internet_connections),
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.adapter?.notifyDataSetChanged()
    }

    private fun setInternShipData(pos: Int, model: WallCategoryModel) {
        binding.rvJob.visibility = View.GONE
        binding.rvExam.visibility = View.GONE
        binding.rvEvents.visibility = View.GONE
        binding.rvGk.visibility = View.GONE
        binding.rvMcq.visibility=View.GONE
        binding.rvGeneralKnowlege.visibility = View.GONE
        binding.rvPersonality.visibility = View.GONE
        binding.tvNoDataFound.visibility = View.GONE
        binding.rrNewBottom.visibility = View.GONE
        currentPosition = 0
        type = model.id
        if (isNetworkAvailable(mContext)) {
            viewModel.getJobList(
                model.id.toString(),
                PrefManager(mContext).getvalue(StaticData.id).toString()
            )
        } else {
            Toast.makeText(
                mContext, getString(R.string.str_error_internet_connections),
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.adapter?.notifyDataSetChanged()
    }

    private fun setExamData(pos: Int, model: WallCategoryModel) {
        binding.rvJob.visibility = View.GONE
        binding.rvExam.visibility = View.GONE
        binding.rvEvents.visibility = View.GONE
        binding.rvGk.visibility = View.GONE
        binding.rvMcq.visibility=View.GONE
        binding.rvGeneralKnowlege.visibility = View.GONE
        binding.rvPersonality.visibility = View.GONE
        binding.tvNoDataFound.visibility = View.GONE
        binding.rrNewBottom.visibility = View.GONE
        currentPosition = 0
        type = model.id
        if (isNetworkAvailable(mContext)) {
            examViewModel.getExamData(PrefManager(mContext).getvalue(StaticData.id).toString())
        } else {
            Toast.makeText(
                mContext, getString(R.string.str_error_internet_connections),
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.adapter?.notifyDataSetChanged()
    }

    private fun setEventData(pos: Int, model: WallCategoryModel) {
        binding.rvJob.visibility = View.GONE
        binding.rvExam.visibility = View.GONE
        binding.rvEvents.visibility = View.GONE
        binding.rvGk.visibility = View.GONE
        binding.rvMcq.visibility=View.GONE
        binding.rvGeneralKnowlege.visibility = View.GONE
        binding.rvPersonality.visibility = View.GONE
        binding.tvNoDataFound.visibility = View.GONE
        binding.rrNewBottom.visibility = View.GONE
        currentPosition = 0
        type = model.id
        if (isNetworkAvailable(mContext)) {
            eventViewModel.getEventData()
        } else {
            Toast.makeText(
                mContext, getString(R.string.str_error_internet_connections),
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.adapter?.notifyDataSetChanged()
    }

    private fun setGKData(pos: Int, model: WallCategoryModel) {
        binding.rvJob.visibility = View.GONE
        binding.rvExam.visibility = View.GONE
        binding.rvEvents.visibility = View.GONE
        binding.rvGk.visibility = View.GONE
        binding.rvMcq.visibility=View.GONE
        binding.rvGeneralKnowlege.visibility = View.GONE
        binding.tvNoDataFound.visibility = View.GONE
        binding.rrNewBottom.visibility = View.GONE
        currentPosition = 0
        type = model.id
        if (isNetworkAvailable(mContext)) {
            gkViewModel.getGKData()
        } else {
            Toast.makeText(
                mContext, getString(R.string.str_error_internet_connections),
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.adapter?.notifyDataSetChanged()

    }

    private fun setPersonalityData(pos: Int, model: WallCategoryModel) {
        binding.rvJob.visibility = View.GONE
        binding.rvExam.visibility = View.GONE
        binding.rvEvents.visibility = View.GONE
        binding.rvGk.visibility = View.GONE
        binding.rvMcq.visibility=View.GONE
        binding.rvGeneralKnowlege.visibility = View.GONE
        binding.rvPersonality.visibility = View.GONE
        binding.tvNoDataFound.visibility = View.GONE
        binding.rrNewBottom.visibility = View.GONE
        currentPosition = 0
        type = model.id
        if (isNetworkAvailable(mContext)) {
            personalityViewModel.getPersonalityData(
                PrefManager(mContext).getvalue(StaticData.id).toString()
            )
        } else {
            Toast.makeText(
                mContext, getString(R.string.str_error_internet_connections),
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.adapter?.notifyDataSetChanged()

    }

    override fun onAnswer1Click(
        position: Int, model: PersonalityModel, llAnswer1: RadioButton, llAnswer2: RadioButton,
        llAnswer3: RadioButton, llAnswer4: RadioButton, llAnswer5: RadioButton
    ) {
        personalityModel = model
        llAnswer1.isChecked = true
        llAnswer2.isChecked = false
        llAnswer3.isChecked = false
        llAnswer4.isChecked = false
        llAnswer5.isChecked = false

        answer = "1"
        personalityPosition = position
        Log.e("answerselected", "1")

        //     binding.personalityAdapter?.notifyDataSetChanged()
        if (isNetworkAvailable(mContext)) {
            personalityViewModel.doPersonalityAnswerData(
                PersonalityRequest(
                    PrefManager(mContext).getvalue(StaticData.id).toString(),
                    model.id.toString(),
                    answer.toString()
                )
            )
        } else {
            Toast.makeText(
                mContext, getString(R.string.str_error_internet_connections),
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    override fun onAnswer2Click(
        position: Int, model: PersonalityModel, llAnswer1: RadioButton, llAnswer2: RadioButton,
        llAnswer3: RadioButton, llAnswer4: RadioButton, llAnswer5: RadioButton
    ) {
        personalityModel = model
        llAnswer1.isChecked = false
        llAnswer2.isChecked = true
        llAnswer3.isChecked = false
        llAnswer4.isChecked = false
        llAnswer5.isChecked = false

        answer = "2"
        personalityPosition = position
        Log.e("answerselected", "2")

        //      binding.personalityAdapter?.notifyDataSetChanged()

        if (isNetworkAvailable(mContext)) {
            personalityViewModel.doPersonalityAnswerData(
                PersonalityRequest(
                    PrefManager(mContext).getvalue(StaticData.id).toString(),
                    model.id.toString(),
                    answer.toString()
                )
            )
        } else {
            Toast.makeText(
                mContext, getString(R.string.str_error_internet_connections),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onAnswer3Click(
        position: Int, model: PersonalityModel, llAnswer1: RadioButton, llAnswer2: RadioButton,
        llAnswer3: RadioButton, llAnswer4: RadioButton, llAnswer5: RadioButton
    ) {
        personalityModel = model
        llAnswer1.isChecked = false
        llAnswer2.isChecked = false
        llAnswer3.isChecked = true
        llAnswer4.isChecked = false
        llAnswer5.isChecked = false

        answer = "3"
        personalityPosition = position
        Log.e("answerselected", "3")

//        binding.personalityAdapter?.notifyDataSetChanged()

        if (isNetworkAvailable(mContext)) {
            personalityViewModel.doPersonalityAnswerData(
                PersonalityRequest(
                    PrefManager(mContext).getvalue(StaticData.id).toString(),
                    model.id.toString(),
                    answer.toString()
                )
            )
        } else {
            Toast.makeText(
                mContext, getString(R.string.str_error_internet_connections),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onAnswer4Click(
        position: Int, model: PersonalityModel, llAnswer1: RadioButton, llAnswer2: RadioButton,
        llAnswer3: RadioButton, llAnswer4: RadioButton, llAnswer5: RadioButton
    ) {
        personalityModel = model
        llAnswer1.isChecked = false
        llAnswer2.isChecked = false
        llAnswer3.isChecked = false
        llAnswer4.isChecked = true
        llAnswer5.isChecked = false

        answer = "4"
        personalityPosition = position
        Log.e("answerselected", "4")

        // binding.personalityAdapter?.notifyDataSetChanged()

        if (isNetworkAvailable(mContext)) {
            personalityViewModel.doPersonalityAnswerData(
                PersonalityRequest(
                    PrefManager(mContext).getvalue(StaticData.id).toString(),
                    model.id.toString(),
                    answer.toString()
                )
            )
        } else {
            Toast.makeText(
                mContext, getString(R.string.str_error_internet_connections),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onAnswer5Click(
        position: Int, model: PersonalityModel, llAnswer1: RadioButton, llAnswer2: RadioButton,
        llAnswer3: RadioButton, llAnswer4: RadioButton, llAnswer5: RadioButton
    ) {
        personalityModel = model
        llAnswer1.isChecked = false
        llAnswer2.isChecked = false
        llAnswer3.isChecked = false
        llAnswer4.isChecked = false
        llAnswer5.isChecked = true

        answer = "5"
        personalityPosition = position
        Log.e("answerselected", "5")
        //      binding.personalityAdapter?.notifyDataSetChanged()

        if (isNetworkAvailable(mContext)) {
            personalityViewModel.doPersonalityAnswerData(
                PersonalityRequest(
                    PrefManager(mContext).getvalue(StaticData.id).toString(),
                    model.id.toString(),
                    answer.toString()
                )
            )
        } else {
            Toast.makeText(
                mContext, getString(R.string.str_error_internet_connections),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}