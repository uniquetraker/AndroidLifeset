package com.app.lifeset.activity

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.app.lifeset.R
import com.app.lifeset.databinding.ActivityExamDetailsBinding
import com.app.lifeset.extensions.isNetworkAvailable
import com.app.lifeset.model.ExamDetailModel
import com.app.lifeset.util.PrefManager
import com.app.lifeset.util.StaticData
import com.app.lifeset.viewmodel.ExamViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class ExamDetailsActivity : AppCompatActivity() {

    lateinit var binding: ActivityExamDetailsBinding
    lateinit var mContext: ExamDetailsActivity
    private var interested: Int = 0
    private var id: String? = ""
    private val viewModel: ExamViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_exam_details)
        mContext = this
        initUI()
        addListner()
        viewModelObsereves()
    }

    private fun initUI() {
        if (intent.extras != null) {
            id = intent.getStringExtra("examId")
        }

        if (isNetworkAvailable(mContext)){
            viewModel.getExamDetails(PrefManager(mContext).getvalue(StaticData.id).toString(),
                id.toString())
        }else{
            Toast.makeText(mContext,getString(R.string.str_error_internet_connections),
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun addListner() {
        binding.apply {
            ivBack.setOnClickListener {
                finish()
            }
            tvInterested.setOnClickListener {
                if (interested == 0) {
                    if (isNetworkAvailable(mContext)) {
                        doInterestedExam()
                    } else {
                        Toast.makeText(
                            mContext, getString(R.string.str_error_internet_connections),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {

                }
            }
        }
    }

    private fun setData(examModel: ExamDetailModel) {
        binding.apply {
            tvTitle.text = examModel.exam_name
            tvExamLevel.text = examModel.exam_level
            tvNameOfPost.text = examModel.name_of_post

            binding.tvDate.text = getCurrentFormattedDate()

            if (!examModel.announcement_date.isNullOrEmpty()) {
                binding.tvAnnouncementDate.text = convertDateFormat(examModel.announcement_date)
            }

            if (!examModel.last_date_form_filling.isNullOrEmpty()) {
                binding.tvFormFillingDate.text = convertDateFormat(examModel.last_date_form_filling)
            }

            if (!examModel.fees.isNullOrEmpty()) {
                binding.llFees.visibility = View.VISIBLE
                binding.tvFees.setText(examModel.fees)
            } else {
                binding.llFees.visibility = View.GONE
            }

            if (!examModel.vacancy.isNullOrEmpty()) {
                binding.llVacancySeats.visibility = View.VISIBLE
                binding.tvVacancy.setText(examModel.vacancy)
            } else {
                binding.llVacancySeats.visibility = View.GONE
            }

            if (!examModel.age_limit.isNullOrEmpty()) {
                binding.llAgeLimit.visibility = View.VISIBLE
                binding.tvAgeLimit.setText(examModel.age_limit)
            } else {
                binding.llAgeLimit.visibility = View.GONE
            }

            if (!examModel.description.isNullOrEmpty()) {
                binding.tvDescription.visibility = View.VISIBLE
                binding.tvDescriptionTitle.visibility = View.GONE
                binding.tvDescription.text =
                    Html.fromHtml(examModel.description, Html.FROM_HTML_MODE_COMPACT)
                binding.tvDescription.movementMethod = LinkMovementMethod.getInstance()
            } else {
                binding.tvDescription.visibility = View.GONE
                binding.tvDescriptionTitle.visibility = View.GONE
            }

            if (examModel.interest_status == 0) {
                binding.tvInterested.text = "Interested"
                interested = examModel.interest_status
            } else {
                binding.tvInterested.text = "I am Interested"
                interested = examModel.interest_status
            }


        }
    }

    fun convertDateFormat(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date!!)
    }

    fun getCurrentFormattedDate(): String {
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
        val currentDate = Date()
        return dateFormat.format(currentDate)
    }

    private fun viewModelObsereves() {
        viewModel.isLoading.observe(this, Observer {
            if (it) {
                binding.pbLoadData.visibility = View.VISIBLE
            } else {
                binding.pbLoadData.visibility = View.GONE
            }
        })

        viewModel.examDetailLiveData.observe(this, Observer {
            if (it.success){
                setData(it.data)
            }else{
            }
        })


        viewModel.postInterestedLiveData.observe(this, Observer {
            if (it.status) {
                Toast.makeText(mContext, it.msz, Toast.LENGTH_SHORT).show()
                finish()

            } else {
                Toast.makeText(mContext, it.msz, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun doInterestedExam() {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Are you sure you want to interested exam?")

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            dialog.dismiss()
            viewModel.postInterested(
                id.toString(),
                PrefManager(mContext).getvalue(StaticData.id).toString()
            )

        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            dialog.dismiss()
        }

        builder.show()
    }

}