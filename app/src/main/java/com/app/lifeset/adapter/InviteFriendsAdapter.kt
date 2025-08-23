package com.app.lifeset.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.lifeset.R
import com.app.lifeset.model.ContactModel

class InviteFriendsAdapter(
    val contacts: List<ContactModel>,
    private val onContactSelectionChanged: () -> Unit
) : RecyclerView.Adapter<InviteFriendsAdapter.ContactViewHolder>() {

    var filteredContacts: MutableList<ContactModel> = contacts.toMutableList()

    inner class ContactViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.tvContactName)
        val phoneTextView: TextView = view.findViewById(R.id.tvPhoneNumber)
        val checkBox: CheckBox = view.findViewById(R.id.checkboxSelect)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adp_invite_friends, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = filteredContacts[position]
        holder.nameTextView.text = contact.name
        holder.phoneTextView.text = contact.phoneNumber

        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = contact.selected

        // Checkbox change listener
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            contact.selected = isChecked
            contacts.find {
                it.name == contact.name && it.phoneNumber == contact.phoneNumber
            }?.selected = isChecked
            onContactSelectionChanged()
        }

        // Item click listener
        holder.itemView.setOnClickListener {
            // Toggle the checkbox state
            holder.checkBox.isChecked = !holder.checkBox.isChecked
        }
    }



    override fun getItemCount(): Int = filteredContacts.size

    fun selectAll(isSelectAll: Boolean) {
        contacts.forEach { it.selected = isSelectAll }
        filter("") // refresh the filtered list with selection
    }

    fun getSelectedNumbers(): String {
        return contacts.filter { it.selected }
            .joinToString(", ") { it.phoneNumber.replace("+91","").replace(" ","")
                .replace("-","")}
    }

    fun filter(query: String) {
        val lowerCaseQuery = query.lowercase()
        filteredContacts = if (lowerCaseQuery.isEmpty()) {
            contacts.toMutableList()
        } else {
            contacts.filter {
                it.name.lowercase().contains(lowerCaseQuery) ||
                        it.phoneNumber.contains(lowerCaseQuery)
            }.toMutableList()
        }
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int = position
}
