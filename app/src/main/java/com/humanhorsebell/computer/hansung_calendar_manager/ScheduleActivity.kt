package com.humanhorsebell.computer.hansung_calendar_manager

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.bumptech.glide.load.engine.Resource
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_schedule.*
import org.jetbrains.anko.Android
import org.jetbrains.anko.selectorResource
import java.nio.channels.Selector
import java.text.SimpleDateFormat
import java.util.Date

class ScheduleActivity : AppCompatActivity() {
    lateinit var userNo:String
    lateinit var groupNo:String
    lateinit var day: String
    lateinit var scheduleNo: String
    val comments = ArrayList<Comment>()
    lateinit var scheduleListAdapter: ScheduleListAdapter
    lateinit var count: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        userNo = "3"
        groupNo = "0"
        day = "2019-10-20"
        scheduleNo = "0"

        scheduleListAdapter = ScheduleListAdapter(applicationContext, comments, userNo)
        listViewComment.adapter = scheduleListAdapter
        listViewComment.divider = null
        //listViewComment.isEnabled = false

        val firebaseDatabase = FirebaseDatabase.getInstance().reference
                .child("group").child(groupNo).child("schedule").child(day).child(scheduleNo)

        val commentListener = object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                comments.clear()
                val children = dataSnapshot.children
                for (child in children) {
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

                firebaseDatabase.child("comment").addValueEventListener(commentListener)
            }
        }
        firebaseDatabase.addListenerForSingleValueEvent(singleGroupScheduleListener)

        val singleCommentNumberListener = object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                count = dataSnapshot.children.count().toString()
            }
        }
        firebaseDatabase.child("comment").addListenerForSingleValueEvent(singleCommentNumberListener)

        val editListener = object : TextView.OnEditorActionListener{
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                when(actionId){
                    EditorInfo.IME_ACTION_SEND -> buttonSubmit.performClick()
                    else -> return false
                }
                return true
            }
        }
        editTextComment.setOnEditorActionListener(editListener)

        val buttonClickListener = View.OnClickListener{v ->
            if(!editTextComment.text.isEmpty()){
                var comment = firebaseDatabase.child("comment").child(count)

                comment.child("data").setValue(editTextComment.text.toString())
                comment.child("writer").setValue(userNo)

                val now = System.currentTimeMillis()
                val date = Date(now)
                val formatterDate = SimpleDateFormat("yyyy-MM-dd")
                val formatterTime = SimpleDateFormat("HH:mm")
                val formattedDate = formatterDate.format(date)
                val formattedTime = formatterTime.format(date)

                comment.child("mDate").setValue(formattedDate)
                comment.child("mTime").setValue(formattedTime)
                editTextComment.setText("")
            }
        }
        buttonSubmit.setOnClickListener(buttonClickListener)
    }
}
