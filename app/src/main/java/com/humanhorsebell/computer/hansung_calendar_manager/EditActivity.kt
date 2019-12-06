package com.humanhorsebell.computer.hansung_calendar_manager

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.humanbell.TodoListDialogAdapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_calendar_dialog_add.*
import kotlinx.android.synthetic.main.activity_edit.*
import java.util.*
import kotlin.collections.ArrayList


class EditActivity : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance().reference
    val ref = database.child("group")

    var group: Group? = null;

    //임의로 user결정 및 group 결정
    val curUser = "user0"
    val curGroup = "0"
    val curGrpRef = FirebaseDatabase.getInstance().reference.child("group").child(curGroup)
    val todoName = ArrayList<String>()

    var grpMems = ArrayList<String>()
    var schedules = ArrayList<Todo>()
    var items = ArrayList<Todo>()
    lateinit var adapter: BaseAdapter
    lateinit var itemList: ListView
    var btnDatePicker: Button? = null
    var btnTimePicker: Button? = null
    var txtTitle: EditText? = null
    var txtDate: EditText? = null
    var txtTime: EditText? = null
    var switch1: Switch? = null
    var flag: Boolean = false
    private var mYear = 0
    private var mMonth = 0
    private var mDay = 0
    private var mHour = 0
    private var mMinute = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        insertMode()

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, this.todoName)
        //날짜마다 List 달라져야 함
        itemList = findViewById<View>(R.id.todoListView) as ListView
        itemList.adapter = adapter
        //임의로 넣어 둔 것
        items.add(Todo("test", "a", "a", "a", "a", "a", Comment("a", "a", "a", "a")))

        val calendar: Calendar = Calendar.getInstance()
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }
        //val curDate:String? = Calendar.YEAR.toString() + "-" + Calendar.MONTH.toString() + "-" + Calendar.DAY_OF_MONTH
        val date1 = "2019-10-20"

        //val query = database.orderByChild("startDate").equalTo(sibal)

        var valueEventListener: ValueEventListener = object : ValueEventListener {
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
        curGrpRef.child("schedule").child(date1).addListenerForSingleValueEvent(valueEventListener)
    }

    private fun insertMode() {//삽입모드 초기화
        fab.setOnClickListener {
            insertTodo()
        }
        deleteFab.setOnClickListener {
            deleteTodo()
        }
    }

    private fun insertTodo() {
        var cancelBtn: Button? = null
        var ok: Button? = null
        val view = layoutInflater.inflate(R.layout.activity_calendar_dialog_add, null, false).apply {
            btnDatePicker = findViewById<View>(R.id.btn_date) as Button
            btnTimePicker = findViewById<View>(R.id.btn_time) as Button
            txtTitle = findViewById<View>(R.id.calendar_title) as EditText
            txtDate = findViewById<View>(R.id.in_date) as EditText
            txtTime = findViewById<View>(R.id.in_time) as EditText
            btnDatePicker!!.setOnClickListener {
                val c = Calendar.getInstance()
                mYear = c[Calendar.YEAR]
                mMonth = c[Calendar.MONTH]
                mDay = c[Calendar.DAY_OF_MONTH]
                val datePickerDialog = DatePickerDialog(this.context,
                        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth -> txtDate!!.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year) }, mYear, mMonth, mDay)
                datePickerDialog.show()
            }
            btnTimePicker!!.setOnClickListener {
                val c = Calendar.getInstance()
                mHour = c[Calendar.HOUR_OF_DAY]
                mMinute = c[Calendar.MINUTE]
                val timePickerDialog = TimePickerDialog(this.context,
                        TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute -> txtTime!!.setText("$hourOfDay:$minute") }, mHour, mMinute, false)
                timePickerDialog.show()
            }
            switch1 = findViewById(R.id.switch1)
            switch1!!.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    flag = true
                    btnDatePicker!!.visibility = View.GONE
                    btnTimePicker!!.visibility = View.GONE
                    txtDate!!.visibility = View.GONE
                    txtTime!!.visibility = View.GONE

                } else {
                    flag = false
                    btnDatePicker!!.visibility = View.VISIBLE
                    btnTimePicker!!.visibility = View.VISIBLE
                    txtDate!!.visibility = View.VISIBLE
                    txtTime!!.visibility = View.VISIBLE

                }
            })
            cancelBtn = findViewById<View>(R.id.btn_cancel) as Button
            ok = findViewById<View>(R.id.btn_ok) as Button
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
                        i = 1
                    }
                })
            //위시리스트
            } else {
                var todo = database.child("group").child(curGroup)
                //할일

            }
            dialog.dismiss()
        }
    }

    private fun deleteTodo() {
        var closeBtn: Button? = null
        val view = layoutInflater.inflate(R.layout.activity_calendar_dialog, null, false).apply {
            closeBtn = findViewById<View>(R.id.btn_close) as Button
            val dialog_adapter = TodoListDialogAdapter(this.context, items)
            var listView = findViewById<View>(R.id.dialog_todoList) as ListView
            listView.adapter = dialog_adapter
            var remove_calendarBtn = findViewById<Button>(R.id.remove_calendar)
            remove_calendarBtn.setOnClickListener {
                //일정 삭제 버튼 누르면 삭제되게 구현
            }
        }
        val dialog = AlertDialog.Builder(this)
                .setView(view)
                .create()
        dialog.show()
        closeBtn!!.setOnClickListener {
            dialog.dismiss()
        }
    }
}