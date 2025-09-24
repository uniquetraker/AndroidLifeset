package com.app.lifeset.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.app.lifeset.R
import com.app.lifeset.databinding.ActivityHomeBinding
import com.app.lifeset.extensions.isNetworkAvailable
import com.app.lifeset.model.NotificationModel
import com.app.lifeset.util.PrefManager
import com.app.lifeset.util.StaticData
import com.app.lifeset.viewmodel.JobViewModel
import com.app.lifeset.viewmodel.NotificationViewModel
import com.app.lifeset.viewmodel.ProfileViewModel
import com.bumptech.glide.Glide
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomeBinding
    lateinit var mContext: HomeActivity
    private var callDate: Boolean = false
    private val viewModel: JobViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()
    private val notificationViewModel: NotificationViewModel by viewModels()
    private var profilePic: String = ""
    private var notificationList: ArrayList<NotificationModel> = arrayListOf()
    lateinit var firebaseAnalytics: FirebaseAnalytics
    private var actionBarDrawerToggle: ActionBarDrawerToggle? = null
    private var title: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        mContext = this
        initUI()
        setNavigationDrawer()
        addListner()
        addObserves()
    }

    private fun initUI() {
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
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
    }

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->

            if (isGranted) {
            } else {
            }
        }

    private fun addListner() {
        binding.llProfile.setOnClickListener {
            firebaseAnalytics.logEvent(
                StaticData.myProfileClicked, null
            )
            startActivity(Intent(mContext, MyProfileActivity::class.java))
        }
        binding.llCard.setOnClickListener {
            firebaseAnalytics.logEvent(
                StaticData.profileCardClicked, null
            )
            startActivity(Intent(mContext, ProfileCardActivity::class.java))
        }
        binding.llNetworking.setOnClickListener {
            firebaseAnalytics.logEvent(
                StaticData.networkingClicked, null
            )
            startActivity(Intent(mContext, StudentListActivity::class.java))
        }
        binding.tvUpdateFreelancerProfile.setOnClickListener {
            firebaseAnalytics.logEvent(
                StaticData.createFreelancerProfileClicked, null
            )
            startActivity(
                Intent(mContext, CreateFreelancerProfileActivity::class.java)
                    .putExtra("title", title)
            )
        }

        binding.llPersonality.setOnClickListener {
            firebaseAnalytics.logEvent(
                StaticData.personalityClicked, null
            )
            startActivity(
                Intent(mContext, MainActivity::class.java)
                    .putExtra("mainType", "Personality")
                    .putExtra("selectedPosition", 5)
            )
        }

        binding.llDailyDigest.setOnClickListener {
            firebaseAnalytics.logEvent(
                StaticData.gkClicked, null
            )
            startActivity(
                Intent(mContext, MainActivity::class.java)
                    .putExtra("mainType", "CA & GK")
                    .putExtra("selectedPosition", 0)
            )

        }
        binding.llCurrentAffairs.setOnClickListener {
            firebaseAnalytics.logEvent(
                StaticData.gkClicked, null
            )
            startActivity(
                Intent(mContext, MainActivity::class.java)
                    .putExtra("mainType", "CA & GK")
                    .putExtra("selectedPosition", 0)
            )


        }
        binding.llGeneralStudies.setOnClickListener {
            firebaseAnalytics.logEvent(
                StaticData.gkClicked, null
            )
            startActivity(
                Intent(mContext, MainActivity::class.java)
                    .putExtra("mainType", "generalknowledge")
                    .putExtra("selectedPosition", 6)
            )

        }
        binding.llGovtVacancies.setOnClickListener {
            firebaseAnalytics.logEvent(
                StaticData.govtexamsClicked, null
            )
            startActivity(
                Intent(mContext, MainActivity::class.java)
                    .putExtra("mainType", "Govt Exams")
                    .putExtra("selectedPosition", 1)
            )

        }
        binding.llCollageEvents.setOnClickListener {
            firebaseAnalytics.logEvent(
                StaticData.eventsClicked, null
            )
            startActivity(
                Intent(mContext, MainActivity::class.java)
                    .putExtra("mainType", "Events")
                    .putExtra("selectedPosition", 2)
            )

        }
        binding.llInstitutes.setOnClickListener {
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://lifeset.co.in/institutes")).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            startActivity(browserIntent)

        }
        binding.tvAppliedJobs.setOnClickListener {
            firebaseAnalytics.logEvent(
                StaticData.appliedJobsClicked, null
            )
            startActivity(Intent(mContext, AppliedJobListActivity::class.java))
        }
        binding.tvFreelancerJobs.setOnClickListener {
            firebaseAnalytics.logEvent(
                StaticData.freelancerJobsClicked, null
            )
            startActivity(Intent(mContext, FreelancerJobsActivity::class.java))
        }
        binding.llFreelancer.setOnClickListener {
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://lifeset.co.in/freelancers")).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            startActivity(browserIntent)
        }
        binding.llInternship.setOnClickListener {
            firebaseAnalytics.logEvent(
                StaticData.internshipClicked, null
            )
            startActivity(
                Intent(mContext, MainActivity::class.java)
                    .putExtra("mainType", "Internship")
                    .putExtra("selectedPosition", 3)
            )
        }
        binding.llFresherJobs.setOnClickListener {
            firebaseAnalytics.logEvent(
                StaticData.jobsClicked, null
            )
            startActivity(
                Intent(mContext, MainActivity::class.java)
                    .putExtra("mainType", "Job")
                    .putExtra("selectedPosition", 4)
            )
        }
        binding.ivLinkedin.setOnClickListener {
            openLinkedInCompanyPage(this)
        }
        binding.ivFacebook.setOnClickListener {
            openFacebook(this, "https://www.facebook.com/LifeSet.co.in")
        }
        binding.ivInstagram.setOnClickListener {
            openInstagramProfile(this)

        }
        binding.ivTwitter.setOnClickListener {
            openXProfile(this)
        }

        binding.ivMenu.setOnClickListener {
            binding.drawerlayout.openDrawer(GravityCompat.START)
        }
        binding.rrNotifications.setOnClickListener {
            firebaseAnalytics.logEvent(
                StaticData.notificationClicked, null
            )
            startActivity(Intent(mContext, NotificationActivity::class.java))
        }
        binding.ivReferal.setOnClickListener {
            firebaseAnalytics.logEvent(
                StaticData.referalClicked, null
            )
            startActivity(Intent(mContext, ReferalListActivity::class.java))
        }
    }

    private fun setNavigationDrawer() {
        binding.navigationview.bringToFront()
        binding.navigationview.setNavigationItemSelectedListener { item ->
            val id = item.itemId
            when (id) {
                R.id.menu_home -> {
                    startActivity(Intent(mContext, HomeActivity::class.java))
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

                R.id.menu_changeLanguage -> {
                    startActivity(Intent(mContext, ChangeLanguageActivity::class.java))
                }

            }
            binding.drawerlayout.closeDrawer(GravityCompat.START)
            true
        }
        actionBarDrawerToggle = object : ActionBarDrawerToggle(
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
        actionBarDrawerToggle?.syncState()
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


    private fun addObserves() {

        viewModel.isLoading.observe(this, Observer {
            if (it) {
                binding.pbLoadData.visibility = View.VISIBLE
            } else {
                binding.pbLoadData.visibility = View.GONE
            }
        })

        viewModel.settingsMobileDateResponse.observe(this, Observer {
            if (it.status) {
            }
        })


        notificationViewModel.isNotificationLiveData.observe(this, Observer {
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


        profileViewModel.profileLiveData.observe(this, Observer {
            if (it.status) {
                profilePic = it.profile
            }
        })

        profileViewModel.studentProfileLiveData.observe(this, Observer {
            if (it.status) {
                if (it.datas.is_freelancer == 1) {
                    title = "Update Freelancer Profile"
                    binding.tvUpdateFreelancerProfile.text =
                        getString(R.string.str_update_your_freelancer_profile)
                } else {
                    title = "Create Freelancer Profile"
                    binding.tvUpdateFreelancerProfile.text =
                        getString(R.string.str_create_your_freelancer_profile)
                }

            } else {
                Toast.makeText(mContext, it.msz, Toast.LENGTH_SHORT).show()
            }
        })

    }

    override fun onResume() {
        super.onResume()
        if (isNetworkAvailable(mContext)) {
            profileViewModel.getStudentProfile(
                PrefManager(mContext).getvalue(StaticData.id).toString()
            )
            profileViewModel.getProfile(PrefManager(mContext).getvalue(StaticData.id).toString())
            notificationViewModel.getNotifications(
                PrefManager(mContext).getvalue(StaticData.phoneNumber).toString()
            )

        } else {
            Toast.makeText(
                mContext,
                getString(R.string.str_error_internet_connections),
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    fun openFacebook(context: Context, pageUrl: String = "https://www.facebook.com/LifeSet.co.in") {
        // Try Facebook apps first (main + Lite)
        val fbPackages = listOf("com.facebook.katana", "com.facebook.lite")
        val appDeepLink = Uri.parse("fb://facewebmodal/f?href=$pageUrl")

        for (pkg in fbPackages) {
            val intentApp = Intent(Intent.ACTION_VIEW, appDeepLink).apply {
                `package` = pkg
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            try {
                context.startActivity(intentApp)
                return
            } catch (_: ActivityNotFoundException) {
                // try next
            }
        }

        // Fallback: open in browser
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(pageUrl)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(browserIntent)
    }

    fun openLinkedInCompanyPage(
        context: Context,
        companyPageUrl: String = "https://www.linkedin.com/company/lifeset-a-platform-designed-for-students"
    ) {
        // The URI format that LinkedIn app understands for companies
        val linkedInDeepLink =
            Uri.parse("linkedin://company/lifeset-a-platform-designed-for-students")

        // Attempt to open in LinkedIn app
        val appIntent = Intent(Intent.ACTION_VIEW, linkedInDeepLink).apply {
            `package` = "com.linkedin.android"
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        try {
            context.startActivity(appIntent)
            return
        } catch (_: ActivityNotFoundException) {
            // LinkedIn app not installed, open in browser instead
        }

        // Fallback: open in browser
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(companyPageUrl)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(browserIntent)
    }

    fun openXProfile(
        context: Context,
        username: String = "LifesetIndia"
    ) {
        val deepLink = Uri.parse("twitter://user?screen_name=$username")
        val webUrl = "https://twitter.com/$username"

        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, deepLink).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        } catch (e: ActivityNotFoundException) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(webUrl)).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            })
        }
    }

    fun openInstagramProfile(
        context: Context,
        username: String = "lifeset_of_students_and_youth"
    ) {
        // Deep link for Instagram app
        val uriApp = Uri.parse("http://instagram.com/_u/$username")
        val appIntent = Intent(Intent.ACTION_VIEW, uriApp).apply {
            `package` = "com.instagram.android"
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        try {
            // Attempt to open in Instagram app
            context.startActivity(appIntent)
        } catch (e: ActivityNotFoundException) {
            // Fallback: open in browser
            val uriBrowser = Uri.parse("https://www.instagram.com/$username")
            val browserIntent = Intent(Intent.ACTION_VIEW, uriBrowser).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(browserIntent)
        }
    }

    override fun onBackPressed() {
        // super.onBackPressed()
        exitAppDialog()
    }

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

}