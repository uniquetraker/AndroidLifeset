package com.app.lifeset.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.lifeset.R
import com.app.lifeset.adapter.ReferalListAdapter
import com.app.lifeset.databinding.ActivityReferalListBinding
import com.app.lifeset.extensions.isNetworkAvailable
import com.app.lifeset.model.ReferalModel
import com.app.lifeset.util.PrefManager
import com.app.lifeset.util.StaticData
import com.app.lifeset.viewmodel.ReferalViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReferalListActivity : AppCompatActivity(), ReferalListAdapter.onItemClick {

    lateinit var binding: ActivityReferalListBinding
    lateinit var mContext: ReferalListActivity
    private val viewModel: ReferalViewModel by viewModels()
    private var deletePosition: Int = 0
    private var referalList: ArrayList<ReferalModel> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_referal_list)
        mContext = this
        initUI()
        addListner()
        addObsereves()
    }

    private fun initUI() {
        if (isNetworkAvailable(mContext)) {
            viewModel.getReferalList(PrefManager(mContext).getvalue(StaticData.id).toString())
        } else {
            Toast.makeText(
                mContext, getString(R.string.str_error_internet_connections),
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    private fun addListner() {
        binding.tvBack.setOnClickListener {
            finish()
        }
        binding.llReferFriend.setOnClickListener {
            startActivityForResult(Intent(mContext, InviteFriendsActivity::class.java), 201)
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

        viewModel.deleteReferalLiveData.observe(this, Observer {
            if (it.status == "success") {
                Toast.makeText(mContext, it.message, Toast.LENGTH_SHORT).show()
                if (isNetworkAvailable(mContext)) {
                    viewModel.getReferalList(PrefManager(mContext).getvalue(StaticData.id).toString())
                } else {
                    Toast.makeText(
                        mContext, getString(R.string.str_error_internet_connections),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(mContext, it.message, Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.referalLiveData.observe(this, Observer {
            if (it.success) {
                referalList.clear()
                referalList.addAll(it.data)
                if (referalList.size > 0) {
                    binding.rvReferal.visibility = View.VISIBLE
                    binding.tvNoDataFound.visibility = View.GONE
                    setAdapter(referalList)
                } else {
                    binding.rvReferal.visibility = View.GONE
                    binding.tvNoDataFound.visibility = View.VISIBLE
                }

            } else {
                binding.rvReferal.visibility = View.GONE
                binding.tvNoDataFound.visibility = View.VISIBLE
            }
        })

    }

    private fun setAdapter(referalList: ArrayList<ReferalModel>) {
        binding.apply {
            rvReferal.layoutManager = LinearLayoutManager(mContext)
            rvReferal.setHasFixedSize(true)
            adapter = ReferalListAdapter(mContext, referalList, this@ReferalListActivity)
            rvReferal.adapter = adapter
            adapter?.notifyDataSetChanged()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 201) {
            if (resultCode == Activity.RESULT_OK) {
                if (isNetworkAvailable(mContext)) {
                    viewModel.getReferalList(
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

    override fun onClick(position: Int, model: ReferalModel) {
        doDeleteDialog(position, model)
    }

    private fun doDeleteDialog(position: Int, model: ReferalModel) {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Delete")
        builder.setMessage("Are you sure you want to Delete this number?")

        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            if (isNetworkAvailable(mContext)) {
                deletePosition=position
                viewModel.deleteReferal(
                    PrefManager(mContext).getvalue(StaticData.id).toString(),
                    model.id.toString()
                )
            } else {
                Toast.makeText(
                    mContext, getString(R.string.str_error_internet_connections),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            dialog.dismiss()
        }

        builder.show()
    }


}