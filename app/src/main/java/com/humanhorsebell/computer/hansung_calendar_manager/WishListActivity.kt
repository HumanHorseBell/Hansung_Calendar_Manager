package com.humanhorsebell.computer.hansung_calendar_manager

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_wish_list.*
import java.util.*
import kotlin.collections.ArrayList

class WishListActivity : AppCompatActivity() {
    var wishlist = ArrayList<String?>()
    val database = FirebaseDatabase.getInstance().reference
    var curGroup = "0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wish_list)

        var adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, this.wishlist)

        val listView = findViewById<ListView>(R.id.wishListView)
        insertMode()
        listView.adapter = adapter


    }

    private fun insertMode() {//삽입모드 초기화
        fab.setOnClickListener {
            insertTodo()
        }
    }

    private fun insertTodo() {
        var cancelBtn: Button? = null
        var ok: Button? = null
        val view = layoutInflater.inflate(R.layout.activity_calendar_dialog_add, null, false).apply {
            var btnDatePicker_start: Button? = null
            var btnDatePicker_end: Button? = null
            var txtTitle: EditText? = null
            var txt_startDate: EditText? = null
            var txt_startTime: EditText? = null
            var txt_endDate: EditText? = null
            var txt_endTime: EditText? = null
            var txt_start: TextView? = null
            var txt_end: TextView? = null
            var switch1: Switch? = null
            btnDatePicker_start = findViewById<View>(R.id.btn_start_date) as Button
            btnDatePicker_end = findViewById<View>(R.id.btn_end_date) as Button

            txt_startDate = findViewById<View>(R.id.start_date) as EditText
            txt_startTime = findViewById<View>(R.id.start_time) as EditText
            txt_endDate = findViewById<View>(R.id.end_date) as EditText
            txt_endTime = findViewById<View>(R.id.end_time) as EditText

            txt_start = findViewById<View>(R.id.txt_startdate) as TextView
            txt_end = findViewById<View>(R.id.txt_enddate) as TextView
            var mYear = 0
            var mMonth = 0
            var mDay = 0
            var mHour = 0
            var mMinute = 0
            btnDatePicker_start!!.setOnClickListener {
                val c = Calendar.getInstance()
                mHour = c[Calendar.HOUR_OF_DAY]
                mMinute = c[Calendar.MINUTE]
                // Launch Time Picker Dialog
                val timePickerDialog = TimePickerDialog(this.context,
                        TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute -> txt_startTime!!.setText("$hourOfDay:$minute") }, mHour, mMinute, false)
                timePickerDialog.show()

                mYear = c[Calendar.YEAR]
                mMonth = c[Calendar.MONTH]
                mDay = c[Calendar.DAY_OF_MONTH]
                val datePickerDialog = DatePickerDialog(this.context,
                        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                            txt_startDate!!
                                    .setText(year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth)
                        }, mYear, mMonth, mDay)
                datePickerDialog.show()

            }
            btnDatePicker_end!!.setOnClickListener {
                val c = Calendar.getInstance()
                mHour = c[Calendar.HOUR_OF_DAY]
                mMinute = c[Calendar.MINUTE]
                // Launch Time Picker Dialog
                val timePickerDialog = TimePickerDialog(this.context,
                        TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute -> txt_endTime!!.setText("$hourOfDay:$minute") }, mHour, mMinute, false)
                timePickerDialog.show()

                mYear = c[Calendar.YEAR]
                mMonth = c[Calendar.MONTH]
                mDay = c[Calendar.DAY_OF_MONTH]
                val datePickerDialog = DatePickerDialog(this.context,
                        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                            txt_endDate!!
                                    .setText(year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth)
                        }, mYear, mMonth, mDay)
                datePickerDialog.show()


            }
            cancelBtn = findViewById<View>(R.id.btn_cancel) as Button
            ok = findViewById<View>(R.id.btn_ok) as Button
            switch1 = findViewById(R.id.switch1)


                    switch1!!.visibility=View.GONE
                    btnDatePicker_end!!.visibility = View.GONE
                    btnDatePicker_start!!.visibility = View.GONE
                    txt_startDate!!.visibility = View.GONE
                    txt_startTime!!.visibility = View.GONE
                    txt_endDate!!.visibility = View.GONE
                    txt_endTime!!.visibility = View.GONE
                    txt_start!!.visibility = View.GONE
                    txt_end!!.visibility = View.GONE




        }
        val dialog = AlertDialog.Builder(this)
                .setView(view)
                .create()
        dialog.show()
        cancelBtn!!.setOnClickListener {
            dialog.dismiss()
        }
        ok!!.setOnClickListener {
            var i = 1
            var j = 0

            var txtTitle = findViewById<View>(R.id.calendar_title) as EditText
                var wishRef = database.child("group").child(curGroup).child("wishlist")

                /*wishRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(dataSnapshot: DatabaseError) {}
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (data in dataSnapshot.children) {
                            var wishListNum = data.key
                            i++
                        }


                        wishRef.child("wishlist" + i).setValue(txtTitle!!.text.toString()!!)

                    }
                })*/
                //위시리스트


            dialog.dismiss()
        }
    }

}
