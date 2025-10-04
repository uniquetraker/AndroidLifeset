package com.app.lifeset.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import com.app.lifeset.R
import com.app.lifeset.databinding.ActivityChangeLanguageBinding
import com.app.lifeset.util.PrefManager
import com.app.lifeset.util.StaticData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangeLanguageActivity : AppCompatActivity() {

    lateinit var binding: ActivityChangeLanguageBinding
    lateinit var mContext: ChangeLanguageActivity
    private var language: String? = "English"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_language)
        mContext = this
        initUI()
        addListner()
    }

    private fun initUI() {
        language = PrefManager(mContext).getvalue(StaticData.language, "English")
        if (language.equals("English")) {
            binding.rbEnglish.isChecked = true
            binding.rbHindi.isChecked = false
        } else {
            binding.rbEnglish.isChecked = false
            binding.rbHindi.isChecked = true
        }
    }

    private fun addListner() {
        binding.apply {
            ivBack.setOnClickListener {
                finish()
            }
            rbEnglish.setOnCheckedChangeListener { compoundButton, b ->
                if (b){
                    binding.rbEnglish.isChecked=true
                    binding.rbHindi.isChecked=false
                    language = "English"
                }
            }


            rbHindi.setOnCheckedChangeListener { compoundButton, b ->
                if (b){
                    binding.rbEnglish.isChecked=false
                    binding.rbHindi.isChecked=true
                    language = "Hindi"
                }
            }


            llEnglish.setOnClickListener {
                rbEnglish.isChecked = true
                rbHindi.isChecked = false
                language = "English"
            }
            llHindi.setOnClickListener {
                rbEnglish.isChecked = false
                rbHindi.isChecked = true
                language = "Hindi"
            }
            tvSubmit.setOnClickListener {
                PrefManager(mContext).setvalue(StaticData.language, language)
                Toast.makeText(mContext,"Language set successfully",Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}