package com.humanhorsebell.computer.hansung_calendar_manager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import kotlinx.android.synthetic.main.schedule_list_view_item.view.*

class ScheduleListAdapter(val context: Context, val comments: ArrayList<Comment>) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.schedule_list_view_item, null) as View

        val writer = view.findViewById<TextView>(R.id.textViewWriter)
        val message = view.findViewById<TextView>(R.id.textViewMessage)
        val dayAndTime = view.findViewById<TextView>(R.id.textViewTime)

        writer.text = comments[position].writer
        message.text = comments[position].data
        dayAndTime.text = comments[position].mDate + " " + comments[position].mTime

        return view
    }

    override fun getItem(position: Int): Any {
        return comments[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return comments.size
    }
}