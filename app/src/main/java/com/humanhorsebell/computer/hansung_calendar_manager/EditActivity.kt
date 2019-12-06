package com.humanhorsebell.computer.hansung_calendar_manager

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_edit.*
import java.util.*
import kotlin.collections.ArrayList


class EditActivity : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance().reference
    val groupRef = database.child("group")
    val userRef = database.child("user")

    var group: Group? = null;

    //임의로 user결정 및 group 결정 나중에 intent로 받아올 것. curUser를
    val curUser = "0"
    val curGroup = "0"
    lateinit var curDate: String
    val curGrpRef = groupRef.child(curGroup)
    val todoName = ArrayList<String>()

    var groupkeys = ArrayList<String>()
    var groups: Group? = null
    lateinit var adapter: BaseAdapter
    lateinit var itemList: ListView
    var astarty = "1"

    //일정의 제목DB만 가져오는 -
    val todoNameValueEventListener: ValueEventListener = object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) { }
        override fun onDataChange(p0: DataSnapshot) {
            todoName.clear()
            for (data in p0.children) {
                val groupNum: String? = data.key
                val title = data.child("name")
                if (groupNum != null) {
                    todoName.add(title.value.toString())
                    adapter.notifyDataSetChanged()
                    adapter.notifyDataSetInvalidated()
                }
            }
        }

    }

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        if (astarty.equals("0")) {
            fab.visibility = View.GONE
        } else {
            fab.visibility = View.VISIBLE
        }

        insertMode()

        val calendar: Calendar = Calendar.getInstance()
        curDate = calendar.get(Calendar.YEAR).toString() + "-" + (calendar.get(Calendar.MONTH).toString().toInt()+1).toString() + "-" + calendar.get(Calendar.DAY_OF_MONTH).toString()


        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, this.todoName)
        itemList = findViewById<View>(R.id.todoListView) as ListView
        itemList.adapter = adapter

        //유저의 속한 그룹 찾기
        userRef.child(curUser).child("group").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) { }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                groupkeys.clear()
                for (data in dataSnapshot.children) {
                    var groupKey = data.key
                    if(groupKey != null) {
                        groupkeys.add(groupKey.toString())
                    }
                }
            }

        })

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            todoName.clear()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month+1)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            curDate = year.toString() + "-" + ((month.toString()).toInt()+1).toString() + "-" + dayOfMonth
            adapter.notifyDataSetChanged()
            adapter.notifyDataSetInvalidated()
            totalTodo(groupkeys)    //이걸로 전체 일정 받아오니까 그룹버튼 눌렀을 때 따로 조건 처리
        }
        totalTodo(groupkeys)    //이걸로 전체 일정 받아오니까 그룹버튼 눌렀을 때 따로 조건 처리
    }

    //전체 일정 보여주기(그룹이 하나라면 하나만 보여지겠쥬?)
    private fun totalTodo(grpKey: ArrayList<String>) {
        for(grp in grpKey) {
            perGrpTodo(grp)
        }
    }

    //그룹별 일정 보여주기
    private fun perGrpTodo(groupNum: String) {
        groupRef.child(groupNum).child("schedule").child(curDate).addListenerForSingleValueEvent(todoNameValueEventListener)
    }

    private fun insertMode() {
        fab.setOnClickListener {
            insertTodo()
        }
    }

    private fun insertTodo() {
        var mYear = 0
        var mMonth = 0
        var mDay = 0
        var mHour = 0
        var mMinute = 0
        var btnDatePicker_start: Button?
        var btnDatePicker_end: Button? = null
        var txtTitle: EditText? = null
        var txt_startDate: EditText? = null
        var txt_startTime: EditText? = null
        var txt_endDate: EditText? = null
        var txt_endTime: EditText? = null
        var txt_start: TextView? = null
        var txt_end: TextView? = null
        var switch1: Switch? = null
        var flag: Boolean = false
        var cancelBtn: Button? = null
        var ok: Button? = null
        val view = layoutInflater.inflate(R.layout.activity_calendar_dialog_add, null, false).apply {
            btnDatePicker_start = findViewById<View>(R.id.btn_start_date) as Button
            btnDatePicker_end = findViewById<View>(R.id.btn_end_date) as Button
            txtTitle = findViewById<View>(R.id.calendar_title) as EditText
            txt_startDate = findViewById<View>(R.id.start_date) as EditText
            txt_startTime = findViewById<View>(R.id.start_time) as EditText
            txt_endDate = findViewById<View>(R.id.end_date) as EditText
            txt_endTime = findViewById<View>(R.id.end_time) as EditText
            txt_start = findViewById<View>(R.id.txt_startdate) as TextView
            txt_end = findViewById<View>(R.id.txt_enddate) as TextView
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
                        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth -> txt_startDate!!
                                .setText(year.toString()+ "-" +(monthOfYear + 1) + "-" +dayOfMonth ) }, mYear, mMonth, mDay)
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
                        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth -> txt_endDate!!
                                .setText(year.toString()+ "-" +(monthOfYear + 1) + "-" +dayOfMonth) }, mYear, mMonth, mDay)
                datePickerDialog.show()


            }
            cancelBtn = findViewById<View>(R.id.btn_cancel) as Button
            ok = findViewById<View>(R.id.btn_ok) as Button
            switch1 = findViewById(R.id.switch1)
            switch1!!.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    flag = true
                    btnDatePicker_end!!.visibility = View.GONE
                    btnDatePicker_start!!.visibility = View.GONE
                    txt_startDate!!.visibility = View.GONE
                    txt_startTime!!.visibility = View.GONE
                    txt_endDate!!.visibility = View.GONE
                    txt_endTime!!.visibility = View.GONE
                    txt_start!!.visibility=View.GONE
                    txt_end!!.visibility=View.GONE

                } else {
                    flag = false
                    btnDatePicker_end!!.visibility = View.VISIBLE
                    btnDatePicker_start!!.visibility = View.VISIBLE
                    txt_startDate!!.visibility = View.VISIBLE
                    txt_startTime!!.visibility = View.VISIBLE
                    txt_endDate!!.visibility = View.VISIBLE
                    txt_endTime!!.visibility = View.VISIBLE
                    txt_start!!.visibility=View.VISIBLE
                    txt_end!!.visibility=View.VISIBLE

                }
            })
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
            if (flag == true) {
                var wishRef = database.child("group").child(curGroup.toString()).child("wishlist")

                wishRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(dataSnapshot: DatabaseError) { }
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (data in dataSnapshot.children) {
                            var wishListNum = data.key
                            i++
                        }
                        wishRef.child("wishlist" + i).setValue(txtTitle!!.text.toString()!!)
                    }
                })
            //위시리스트
            } else {
                var todo = database.child("group").child(curGroup).child("schedule").child(txt_startDate!!.text.toString()!!)
                todo.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(dataSnapshot: DatabaseError) {}
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (data in dataSnapshot.children) {
                            var todoListNum = data.key
                            j++
                        }

                        todo.child(j.toString()).child("endDate").setValue(txt_endDate!!.text.toString())
                        todo.child(j.toString()).child("endTime").setValue(txt_endTime!!.text.toString())
                        //                       todo.child(i.toString()).child("memo").setValue(txtTitle!!.text.toString()!!)
                        todo.child(j.toString()).child("name").setValue(txtTitle!!.text.toString())
                        todo.child(j.toString()).child("startDate").setValue(txt_startDate!!.text.toString())
                        todo.child(j.toString()).child("startTime").setValue(txt_startTime!!.text.toString())
                    }
                })
            }
            dialog.dismiss()
        }
    }

}