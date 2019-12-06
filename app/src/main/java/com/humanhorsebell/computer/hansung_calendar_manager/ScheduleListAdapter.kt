package com.humanhorsebell.computer.hansung_calendar_manager

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.schedule_list_view_item.view.*

class ScheduleListAdapter(val context: Context, val comments: ArrayList<Comment>, val userNo: String) : BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.schedule_list_view_item, null) as View

        view.isEnabled = false

        val writer = view.findViewById<TextView>(R.id.textViewWriter)
        val message = view.findViewById<TextView>(R.id.textViewMessage)
        val dayAndTime = view.findViewById<TextView>(R.id.textViewTime)

        val singleUserListener = object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                writer.text = dataSnapshot.child("name").value.toString()
            }
        }

        if(userNo == comments[position].writer){
            view.linearLayoutMessage.removeView(writer)
            val linearLayoutItem = view.findViewById<LinearLayout>(R.id.linearLayoutItem)
            val layoutParams = linearLayoutItem.layoutParams as LinearLayout.LayoutParams
            layoutParams.gravity = Gravity.RIGHT

            view.linearLayoutMessage.gravity = Gravity.RIGHT
            view.linearLayoutInfo.gravity = Gravity.LEFT
        }else {
            writer.setBackgroundResource(R.drawable.writer_layout)
            //writer.text = comments[position].writer
            FirebaseDatabase.getInstance().reference.child("user").child(comments[position].writer).addListenerForSingleValueEvent(singleUserListener)
        }
        message.text = comments[position].data
        dayAndTime.text = comments[position].mDate + "  " + comments[position].mTime

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