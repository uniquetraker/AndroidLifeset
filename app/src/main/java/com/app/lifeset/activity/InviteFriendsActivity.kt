package com.app.lifeset.activity

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.lifeset.R
import com.app.lifeset.adapter.InviteFriendsAdapter
import com.app.lifeset.databinding.ActivityInviteFriendsBinding
import com.app.lifeset.extensions.isNetworkAvailable
import com.app.lifeset.model.AddReferalRequest
import com.app.lifeset.model.ContactModel
import com.app.lifeset.util.PrefManager
import com.app.lifeset.util.StaticData
import com.app.lifeset.viewmodel.ReferalViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InviteFriendsActivity : AppCompatActivity() {

    lateinit var binding: ActivityInviteFriendsBinding
    lateinit var mContext: InviteFriendsActivity
    private val REQUEST_CONTACTS_PERMISSION = 100
    private lateinit var inviteFriendsAdapter: InviteFriendsAdapter
    private var isSelectAll = false
    private val viewModel: ReferalViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_invite_friends)
        mContext = this
        initUI()
        addListner()
        addObsereves()
    }

    private fun initUI() {
        binding.rvContacts.layoutManager = LinearLayoutManager(mContext)
        binding.noDataFound.visibility = View.GONE // Initially hide noDataFound

        if (ContextCompat.checkSelfPermission(
                mContext,
                android.Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            loadContacts()
        } else {
            requestPermissions(
                arrayOf(android.Manifest.permission.READ_CONTACTS),
                REQUEST_CONTACTS_PERMISSION
            )
        }
    }

    private fun addListner() {
        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.btnSelectAll.setOnClickListener { toggleSelectAll() }
        binding.btnSendSMS.setOnClickListener {
            if (isNetworkAvailable(mContext)) {
                displaySelectedNumbers()
            } else {
                Toast.makeText(
                    mContext, getString(R.string.str_error_internet_connections),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                if (query.isEmpty()) {
                    inviteFriendsAdapter.filter("")
                    binding.searchViewLayout.setEndIconDrawable(0)
                    binding.searchViewLayout.isEndIconVisible = false
                } else {
                    inviteFriendsAdapter.filter(query)
                    binding.searchViewLayout.setEndIconDrawable(R.drawable.ic_baseline_close_24)
                    binding.searchViewLayout.isEndIconVisible = true
                }
                updateNoDataVisibility() // Update visibility after filtering
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.searchViewLayout.setEndIconOnClickListener {
            binding.searchView.text?.clear()
            inviteFriendsAdapter.filter("")
            updateNoDataVisibility() // Update visibility after clearing search
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

        viewModel.addReferalLiveData.observe(this, Observer {
            if (it.status == "success") {
                Toast.makeText(mContext, it.message, Toast.LENGTH_SHORT).show()
                if (it.alreadyAdded.equals("1")) {
                    Toast.makeText(mContext, it.skippedMessage, Toast.LENGTH_SHORT).show()
                }
                val intent = Intent()
                setResult(Activity.RESULT_OK, intent)
                finish()
            } else {
                Toast.makeText(mContext, it.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadContacts() {
        val contacts = fetchContacts(contentResolver)
        val sortedContacts = sortContactsAlphabetically(contacts)
        inviteFriendsAdapter = InviteFriendsAdapter(sortedContacts) {
            updateSelectAllButtonText()
            updateNoDataVisibility() // Update visibility after selection changes
        }
        binding.rvContacts.adapter = inviteFriendsAdapter
        updateNoDataVisibility() // Check initial data
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CONTACTS_PERMISSION &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            loadContacts()
        } else {
            Toast.makeText(mContext, "Permission Denied", Toast.LENGTH_SHORT).show()
            binding.noDataFound.visibility = View.VISIBLE // Show noDataFound if permission denied
            binding.btnSelectAll.visibility = View.GONE
            binding.searchViewLayout.visibility = View.GONE
            binding.btnSendSMS.visibility = View.GONE

        }
    }

    private fun fetchContacts(contentResolver: ContentResolver): List<ContactModel> {
        val contacts = mutableListOf<ContactModel>()
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )

        val cursor = contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            while (it.moveToNext()) {
                val name = it.getString(nameIndex) ?: "Unknown"
                val phoneNumber = it.getString(numberIndex) ?: "No Number"
                contacts.add(ContactModel(name, phoneNumber))
            }
        }
        return contacts
    }

    private fun sortContactsAlphabetically(contacts: List<ContactModel>): List<ContactModel> {
        return contacts.sortedWith(compareBy({ it.name.lowercase() }, { it.phoneNumber }))
    }

    private fun updateSelectAllButtonText() {
        val contacts = inviteFriendsAdapter.contacts
        isSelectAll = contacts.all { it.selected }
        binding.btnSelectAll.text =
            if (isSelectAll) "DeSelect All" else "Select All"
    }

    private fun toggleSelectAll() {
        isSelectAll = !isSelectAll
        inviteFriendsAdapter.selectAll(isSelectAll)
        updateSelectAllButtonText()
        updateNoDataVisibility()
    }


    private fun displaySelectedNumbers() {
        val selectedNumbers = inviteFriendsAdapter.getSelectedNumbers()
        Log.e("selectedNumber","="+selectedNumbers)
        if (selectedNumbers.isEmpty()) {
            Toast.makeText(mContext, "No Contact Selected", Toast.LENGTH_SHORT).show()
        } else {
            if (isNetworkAvailable(mContext)) {
                viewModel.addReferal(
                    AddReferalRequest(
                        std_id = PrefManager(mContext).getvalue(StaticData.id).toString(),
                        mobile = selectedNumbers
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


    private fun sendSmsToMultipleContacts(selectedNumbers: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse("sms:$selectedNumbers")
        intent.putExtra(
            "sms_body",
            "Hey I'm using TirthYatra Seva, and i think you'd love it. Download it here: https://play.google.com/store/search?q=tirthyatra+seva&c=apps&hl=en_IN"
        )
        startActivity(intent)
    }

    private fun updateNoDataVisibility() {
        binding.noDataFound.visibility = if (inviteFriendsAdapter.itemCount == 0) {
            View.VISIBLE
        } else {
            View.GONE
        }

        binding.btnSelectAll.visibility = if (inviteFriendsAdapter.itemCount == 0) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
}