package com.app.lifeset.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.app.lifeset.R
import com.app.lifeset.databinding.ActivitySplashBinding
import com.app.lifeset.util.PrefManager
import com.app.lifeset.util.StaticData
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val splashDelayMillis = 2000L // or StaticData.SPLASH_TIME_OUT.toLong()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    private fun checkForUpdate() {
        val appUpdateManager = AppUpdateManagerFactory.create(this)
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
            ) {
                showUpdateDialog(appUpdateManager, appUpdateInfo)
            } else {
                proceedToNextScreen()
            }
        }.addOnFailureListener {
            proceedToNextScreen() // fallback
        }
    }

    override fun onResume() {
        super.onResume()
        checkForUpdate()

    }

    private fun showUpdateDialog(
        appUpdateManager: AppUpdateManager,
        appUpdateInfo: com.google.android.play.core.appupdate.AppUpdateInfo
    ) {
        AlertDialog.Builder(this)
            .setTitle("Update Available")
            .setMessage("A new version of this app is available. Please update to continue.")
            .setCancelable(false)
            .setPositiveButton("Update Now") { _, _ ->
                try {
                    val viewIntent = Intent(
                        "android.intent.action.VIEW",
                        Uri.parse("https://play.google.com/store/apps/details?id=${packageName}")
                    )
                    startActivity(viewIntent)
                } catch (e: Exception) {
                    Toast.makeText(
                        applicationContext, "Unable to Connect Try Again...", Toast.LENGTH_LONG
                    ).show()
                    e.printStackTrace()
                }
            }
            /*.setNegativeButton("Later") { _, _ ->
                proceedToNextScreen()
            }*/
            .show()
    }

    private fun proceedToNextScreen() {
        lifecycleScope.launch {
            delay(splashDelayMillis)

            val isLoggedIn = PrefManager(this@SplashActivity).getvalue(StaticData.isLogin, false)
            val intent = if (isLoggedIn) {
                Intent(this@SplashActivity, HomeActivity::class.java)
                    .putExtra("callDate",true)
            } else {
                Intent(this@SplashActivity, LoginActivity::class.java)
            }

            startActivity(intent)
            finish()
        }
    }
}