package com.humanhorsebell.computer.hansung_calendar_manager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_schedule.*

class ScheduleActivity : AppCompatActivity() {
    lateinit var userNo:String
    lateinit var groupNo:String
    lateinit var day: String
    lateinit var scheduleNo: String
    val comments = ArrayList<Comment>()
    lateinit var scheduleListAdapter: ScheduleListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        groupNo = "0"
        day = "2019-10-20"
        scheduleNo = "0"

        scheduleListAdapter = ScheduleListAdapter(applicationContext, comments)
        listViewComment.adapter = scheduleListAdapter

        val firebaseDatabase = FirebaseDatabase.getInstance().reference
                .child("group").child(groupNo).child("schedule").child(day).child(scheduleNo)

        val singleGroupScheduleListener = object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                //textViewTitle.text = dataSnapshot.child("name").value.toString()
                title = dataSnapshot.child("name").value.toString()
                textViewStartDay.text = dataSnapshot.child("startDate").value.toString()
                textViewStartTime.text = dataSnapshot.child("startTime").value.toString()
                textViewEndDay.text = dataSnapshot.child("endDate").value.toString()
                textViewEndTime.text = dataSnapshot.child("endTime").value.toString()

                val children = dataSnapshot.child("comment").children
                for(child in children){
                    val message = child.child("data").value.toString()
                    val writer = child.child("writer").value.toString()
                    val mDate = child.child("mDate").value.toString()
                    val mTime = child.child("mTime").value.toString()

                    comments.add(Comment(message, mDate, mTime, "", writer))
                    scheduleListAdapter.notifyDataSetChanged()
                    scheduleListAdapter.notifyDataSetInvalidated()
                }
            }
        }
        firebaseDatabase.addListenerForSingleValueEvent(singleGroupScheduleListener)
    }
}
