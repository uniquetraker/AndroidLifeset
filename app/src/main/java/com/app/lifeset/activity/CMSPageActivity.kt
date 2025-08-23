package com.app.lifeset.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.KeyEvent
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.app.lifeset.R
import com.app.lifeset.databinding.ActivityCmspageBinding
import com.app.lifeset.extensions.isNetworkAvailable
import com.app.lifeset.util.StaticData
import com.app.lifeset.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CMSPageActivity : AppCompatActivity() {

    lateinit var binding: ActivityCmspageBinding
    lateinit var mContext: CMSPageActivity
    private var type: String? = ""
    private var pageType: String? = ""
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cmspage)
        mContext = this
        initUI()
        addListner()
        addObsereves()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initUI() {
        if (intent.extras != null) {
            type = intent.getStringExtra("type")
            pageType = intent.getStringExtra("pageType")

            binding.tvTitle.text = type
        }


        if (isNetworkAvailable(mContext)) {
            viewModel.getCMSPage(pageType.toString())
        } else {
            Toast.makeText(
                mContext, getString(R.string.str_error_internet_connections),
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.webview.setWebViewClient(object : WebViewClient() {

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                binding.pbLoadData.visibility = View.VISIBLE
            }

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                var overrideUrlLoading = false

                if (url != null && url.startsWith("whatsapp://")) {
                    view.context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                    overrideUrlLoading = true
                } else {
                    view.loadUrl(url)
                }

                return overrideUrlLoading
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.pbLoadData.visibility = View.GONE
            }
        })
        binding.webview.settings.javaScriptEnabled = true

        when (type) {
            "About Us" -> {
                binding.webview.loadUrl(StaticData.ABOUTUS)

            }

            "Terms & Conditions" -> {
                binding.webview.loadUrl(StaticData.TERMSCONDITION)

            }

            "Privacy Policy" -> {
                binding.webview.loadUrl(StaticData.PRIVARCYPOLICY)
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            when (keyCode) {
                KeyEvent.KEYCODE_BACK -> {
                    if (binding.webview.canGoBack()) {
                        binding.webview.goBack()
                    } else {
                        finish()
                    }
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun addListner() {
        binding.apply {
            ivBack.setOnClickListener {
                finish()
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

        viewModel.cmsPageLiveData.observe(this, Observer {
            if (it.status) {
                binding.tvName.text = Html.fromHtml(it.datas.description)
            }
        })
    }
}