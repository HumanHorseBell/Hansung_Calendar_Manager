package com.humanhorsebell.computer.hansung_calendar_manager

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.humanbell.TodoListAdapter
import com.example.humanbell.TodoListDialogAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_edit.*
import java.util.*

import kotlin.collections.ArrayList


class EditActivity : AppCompatActivity() {
    //login한 user의 데이터를 가져와 group 0-N 까지의 뒤져 grpMem에 속하면 schedule에 들어가 해당 날짜의 데이터 추출,,,,,여기서는 임의로 user2의 데이터 추출
    val databaseUser = FirebaseDatabase.getInstance().getReference()

    val databaseGroup = FirebaseDatabase.getInstance().reference.child("group")

    var group: Group? = null;

    //임의로 user결정 및 group 결정
    val curUser = "0"
    val curGroup = 0
    val database = FirebaseDatabase.getInstance().reference.child("group").child(curGroup.toString()).child("schedule").child("2019-12-05")
    val todoName = ArrayList<String>()
    var switch1: Switch? = null
    lateinit var myAdapter: BaseAdapter

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

    var i: Int = 1
    private var mYear = 0
    private var mMonth = 0
    private var mDay = 0
    private var mHour = 0
    private var mMinute = 0
    private var asdf: String? = null
    private var flag = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        insertMode()

        myAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, this.todoName)

        adapter = TodoListAdapter(this, items)
        //날짜마다 List 달라져야 함
        itemList = findViewById<View>(R.id.todoListView) as ListView
        //itemList.adapter = adapter
        itemList.adapter = myAdapter
        //임의로 넣어 둔 것
        items.add(Todo("test", "a", "a", "a", "a", "a", Comment("a", "a", "a", "a")))
        todoName.add("sibal")

        adapter.notifyDataSetChanged()
        adapter.notifyDataSetInvalidated()

        val calendar: Calendar = Calendar.getInstance()
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }
        //val curDate:String? = Calendar.YEAR.toString() + "-" + Calendar.MONTH.toString() + "-" + Calendar.DAY_OF_MONTH
        val sibal = "2019-12-05"

        //val query = database.orderByChild("startDate").equalTo(sibal)
        database.addListenerForSingleValueEvent(valueEventListener);
    }

    var valueEventListener: ValueEventListener = object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {}

        override fun onDataChange(p0: DataSnapshot) {
            for (data in p0.children) {
                val groupNum = data.key
                val title = data.child("name")
                if (groupNum != null) {
                    todoName.add(groupNum.toString())
                    myAdapter.notifyDataSetChanged()
                    myAdapter.notifyDataSetInvalidated()
                }
            }
        }

    }

    private fun insertMode() {//삽입모드 초기화
        fab.setOnClickListener {
            insertTodo()
        }
        deleteFab.setOnClickListener {
            deleteTodo()
        }
    }

    private fun updateMode() {
        /*fab.setOnClickListener { updateTodo() }//완료버튼 클릭하면 수정
        deleteFab.setOnClickListener { deleteTodo() }*/
    }

    override fun onDestroy() {
        super.onDestroy()
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
                        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth -> txtDate!!.setText(year.toString()+ "-" +dayOfMonth.toString() + "-" + (monthOfYear + 1)) }, mYear, mMonth, mDay)
                datePickerDialog.show()
            }
            btnTimePicker!!.setOnClickListener {
                val c = Calendar.getInstance()
                mHour = c[Calendar.HOUR_OF_DAY]
                mMinute = c[Calendar.MINUTE]
                // Launch Time Picker Dialog
                val timePickerDialog = TimePickerDialog(this.context,
                        TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute -> txtTime!!.setText("$hourOfDay:$minute") }, mHour, mMinute, false)
                timePickerDialog.show()
            }
            /*btnDatePicker!!.setOnClickListener {
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
                // Launch Time Picker Dialog
                val timePickerDialog = TimePickerDialog(this.context,
                        TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute -> txtTime!!.setText("$hourOfDay:$minute") }, mHour, mMinute, false)
                timePickerDialog.show()
            }*/
            cancelBtn = findViewById<View>(R.id.btn_cancel) as Button
            ok = findViewById<View>(R.id.btn_ok) as Button
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
        }
        val dialog = AlertDialog.Builder(this)
                .setView(view)
                .create()
        dialog.show()
        cancelBtn!!.setOnClickListener {
            dialog.dismiss()
        }
        //ok눌렀을 때도 리스너 알맞게 달기
        ok!!.setOnClickListener {
            if(flag==true) {

            print(txtTitle)
            asdf = txtTitle!!.text.toString()
            var database_jm = FirebaseDatabase.getInstance().reference
            var jm=database_jm.child("group").child(curGroup.toString()).child("wishlist")

            jm.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(dataSnapshot: DatabaseError) { }
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (data in dataSnapshot.children) {
                        var wishListNum = data.key
                    i++
                    }
                    jm.child("wishlist"+i).setValue(asdf!!)
                    i=1
                }
            })
//위시리스트
}else{
                var database_jm = FirebaseDatabase.getInstance().reference
                var jm = database_jm.child("group").child(curGroup.toString())
            //할일

}


            /* val a = findViewById<View>(R.id.todoEditText) as EditText
             a.setText(asdf.toString()) */
            dialog.dismiss()
        }
    }

    private fun nextId(): Int {
        return 0
    }

    private fun updateTodo() {//할일수정

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