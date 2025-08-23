package com.app.lifeset.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.app.lifeset.R


class CustomArrayAdapter(context: Context?, objects: List<String?>?) :
    ArrayAdapter<String?>(context!!, 0, objects!!) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.dropdown, parent, false)
        }
        val textView = convertView!!.findViewById<TextView>(R.id.text1)
        val item = getItem(position)
        textView.text = item
        return convertView
    }
}