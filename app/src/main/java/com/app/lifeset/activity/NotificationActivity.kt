package com.app.lifeset.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.lifeset.R
import com.app.lifeset.adapter.NotificationAdapter
import com.app.lifeset.databinding.ActivityNotificationBinding
import com.app.lifeset.extensions.isNetworkAvailable
import com.app.lifeset.model.NotificationModel
import com.app.lifeset.util.PrefManager
import com.app.lifeset.util.StaticData
import com.app.lifeset.viewmodel.NotificationViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NotificationActivity : AppCompatActivity(), NotificationAdapter.onItemClick {

    lateinit var binding: ActivityNotificationBinding
    lateinit var mContext: NotificationActivity
    private val viewModel: NotificationViewModel by viewModels()
    private var notificationList: ArrayList<NotificationModel> = arrayListOf()
    private var currentPosition: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification)
        mContext = this
        initUI()
        addListner()
        addObsereves()
    }

    private fun initUI() {

        if (isNetworkAvailable(mContext)) {
            viewModel.getNotifications(
                PrefManager(mContext).getvalue(StaticData.phoneNumber).toString()
            )
        } else {
            Toast.makeText(
                mContext, getString(R.string.str_error_internet_connections),
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun addListner() {
        binding.ivBack.setOnClickListener {
            finish()
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

        viewModel.isNotificationLiveData.observe(this, Observer {
            if (it.status == "success") {
                notificationList.clear()
                notificationList.addAll(it.data)
                if (notificationList.size > 0) {
                    binding.rvNotifications.visibility = View.VISIBLE
                    binding.tvNoDataFound.visibility = View.GONE

                    setData(notificationList)


                } else {
                    binding.rvNotifications.visibility = View.GONE
                    binding.tvNoDataFound.visibility = View.VISIBLE
                }
            } else {
                binding.tvNoDataFound.visibility = View.VISIBLE
                binding.rvNotifications.visibility = View.GONE
            }
        })


        viewModel.isDeleteNotificationLiveData.observe(this, Observer {
            if (it.status == "success") {
                Toast.makeText(mContext, it.message, Toast.LENGTH_SHORT).show()
                if (notificationList.size > 0) {
                    notificationList.removeAt(currentPosition)
                    viewModel.getNotifications(
                        PrefManager(mContext).getvalue(StaticData.phoneNumber).toString()
                    )
                } else {
                    binding.rvNotifications.visibility = View.GONE
                    binding.tvNoDataFound.visibility = View.VISIBLE
                }
            } else {
                Toast.makeText(mContext, it.message, Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun setData(notificationList: ArrayList<NotificationModel>) {
        binding.apply {
            rvNotifications.layoutManager = LinearLayoutManager(mContext)
            rvNotifications.setHasFixedSize(true)
            adapter = NotificationAdapter(mContext, notificationList, this@NotificationActivity)
            rvNotifications.adapter = adapter
            adapter?.notifyDataSetChanged()
        }
    }

    override fun onDeleteNotifications(position: Int, model: NotificationModel) {
        notificationDialog(position, model)
    }

    override fun onItemClick(position: Int, model: NotificationModel) {
        if (!model.url.isEmpty()){
            val httpIntent = Intent(Intent.ACTION_VIEW)
            httpIntent.data = Uri.parse(model.url)

            startActivity(httpIntent)
        }

    }

    private fun notificationDialog(position: Int, model: NotificationModel) {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to Delete Notifications?")

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            if (isNetworkAvailable(mContext)) {
                currentPosition = position
                viewModel.deleteNotifications(
                    model.id.toString(),
                    PrefManager(mContext).getvalue(StaticData.phoneNumber).toString()
                )
            } else {
                Toast.makeText(
                    mContext,
                    getString(R.string.str_error_internet_connections),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            dialog.dismiss()
        }

        builder.show()
    }

}