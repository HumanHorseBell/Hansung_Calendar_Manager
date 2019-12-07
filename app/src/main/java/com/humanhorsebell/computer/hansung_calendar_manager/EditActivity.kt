package com.humanhorsebell.computer.hansung_calendar_manager

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.activity_timeline.*
import java.util.*
import kotlin.collections.ArrayList


class EditActivity : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance().reference
    val groupRef = database.child("group")
    val userRef = database.child("user")

    lateinit var curDate: String
    val todoName = ArrayList<String>()

    var groupkeys = ArrayList<String>()
    lateinit var adapter: BaseAdapter
    lateinit var itemList: ListView
    var astarty = "1"

    var selectedNum: String? = null
    var selectedGrp: String? = null

    //유저키, 그룹키 intent로 받아오기
    lateinit var userNo: String
    var groupNo: String? = null

    //일정의 제목DB만 가져오는 -
    val todoNameValueEventListener: ValueEventListener = object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {}
        override fun onDataChange(p0: DataSnapshot) {
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

        insertMode()

        val calendar: Calendar = Calendar.getInstance()
        curDate = calendar.get(Calendar.YEAR).toString() + "-" + (calendar.get(Calendar.MONTH).toString().toInt() + 1).toString() + "-" + calendar.get(Calendar.DAY_OF_MONTH).toString()

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, this.todoName)
        itemList = findViewById<View>(R.id.todoListView) as ListView
        itemList.adapter = adapter

        //유저키, 그룹키 받아오기
        userNo = intent.getStringExtra("userNo")
        groupNo = intent.getStringExtra("groupNo")

        switchActivity(this, applicationContext, linearLayout2, userNo, groupNo)


        if (groupNo == null)
            astarty = "0"

        if (astarty.equals("0")) {
            fab.visibility = View.GONE
        } else {
            fab.visibility = View.VISIBLE
        }

        //유저의 속한 그룹 찾기
        userRef.child(userNo).child("group").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                groupkeys.clear()
                for (data in dataSnapshot.children) {
                    var groupKey = data.key
                    if (groupKey != null) {
                        groupkeys.add(groupKey.toString())
                    }
                }
            }

        })

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            todoName.clear()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month + 1)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            curDate = year.toString() + "-" + ((month.toString()).toInt() + 1).toString() + "-" + dayOfMonth
            adapter.notifyDataSetChanged()
            adapter.notifyDataSetInvalidated()
            allOrOneGrp(groupNo)
        }
        allOrOneGrp(groupNo)


        itemList.setOnItemClickListener { parent, view, position, id ->
            var clickTodo = todoName[position]
            for (grp in groupkeys) {
                perGrpTodoDetail(clickTodo, grp)
            }
        }
    }

    private fun allOrOneGrp(grpNum: String?) {
        if (grpNum != null) {
            perGrpTodo(grpNum!!)
        } else {
            totalTodo(groupkeys)
        }
    }

    //전체 일정 보여주기(그룹이 하나라면 하나만 보여지겠쥬?)
    private fun totalTodo(grpKey: ArrayList<String>) {
        for (grp in grpKey) {
            perGrpTodo(grp)
        }
    }

    //그룹별 일정 보여주기
    private fun perGrpTodo(groupNum: String) {
        groupRef.child(groupNum).child("schedule").child(curDate).addValueEventListener(todoNameValueEventListener)
    }

    private fun perGrpTodoDetail(itemName: String, groupNum: String) {
        groupRef.child(groupNum).child("schedule").child(curDate).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                for (data in p0.children) {
                    var todoKey = data.key
                    var curTodoName = data.child("name").value.toString()
                    Log.i("@@@@@@", "curTodoName : " + curTodoName)
                    if (curTodoName.equals(itemName)) {
                        selectedNum = todoKey
                        selectedGrp = groupNum

                        val intent = Intent(this@EditActivity, ScheduleActivity::class.java)
                        intent.putExtra("userNo", userNo)
                        intent.putExtra("groupNo", selectedGrp)
                        intent.putExtra("day", curDate)
                        intent.putExtra("scheduleNo", selectedNum)
                        startActivity(intent)

                        Log.i("@@@@@@", "intent값 확인 : " + userNo + selectedGrp + curDate + selectedNum)
                    }
                }
            }

        })
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
            switch1!!.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    flag = true
                    btnDatePicker_end!!.visibility = View.GONE
                    btnDatePicker_start!!.visibility = View.GONE
                    txt_startDate!!.visibility = View.GONE
                    txt_startTime!!.visibility = View.GONE
                    txt_endDate!!.visibility = View.GONE
                    txt_endTime!!.visibility = View.GONE
                    txt_start!!.visibility = View.GONE
                    txt_end!!.visibility = View.GONE

                } else {
                    flag = false
                    btnDatePicker_end!!.visibility = View.VISIBLE
                    btnDatePicker_start!!.visibility = View.VISIBLE
                    txt_startDate!!.visibility = View.VISIBLE
                    txt_startTime!!.visibility = View.VISIBLE
                    txt_endDate!!.visibility = View.VISIBLE
                    txt_endTime!!.visibility = View.VISIBLE
                    txt_start!!.visibility = View.VISIBLE
                    txt_end!!.visibility = View.VISIBLE

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
                var wishRef = database.child("group").child(groupNo!!).child("wishlist")

                wishRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(dataSnapshot: DatabaseError) {}
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (data in dataSnapshot.children) {
                            i++
                        }
                        wishRef.child("wishlist" + i).setValue(txtTitle!!.text.toString()!!)
                    }
                })
                //위시리스트
            } else {
                var todo = database.child("group").child(groupNo!!).child("schedule").child(txt_startDate!!.text.toString()!!)
                todo.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(dataSnapshot: DatabaseError) {}
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (data in dataSnapshot.children) {
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

    //액션버튼 메뉴 액션바에 집어 넣기
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.showgroupmenu, menu)
        return true
    }

    //액션버튼 클릭 했을 때
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.groupmenuid -> {
                val intent2 = Intent(this@EditActivity, ShowGroup::class.java)
                intent2.putExtra("userNo", userNo)
                startActivity(intent2)
            }
        }
        return super.onOptionsItemSelected(item)
    }

}