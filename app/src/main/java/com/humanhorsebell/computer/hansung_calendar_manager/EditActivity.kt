package com.humanhorsebell.computer.hansung_calendar_manager

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.humanbell.TodoListAdapter
import com.example.humanbell.TodoListDialogAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_edit.*
import org.jetbrains.anko.find
import java.util.*
import kotlin.collections.ArrayList


class EditActivity : AppCompatActivity() {
    //login한 user의 데이터를 가져와 group 0-N 까지의 뒤져 grpMem에 속하면 schedule에 들어가 해당 날짜의 데이터 추출,,,,,여기서는 임의로 user2의 데이터 추출
    val databaseUser = FirebaseDatabase.getInstance().reference.child("user")
    val databaseGroup = FirebaseDatabase.getInstance().reference.child("group")

    var grpMems = ArrayList<String>()
    var items = ArrayList<Todo>()
    lateinit var adapter: BaseAdapter
    lateinit var itemList: ListView
    var btnDatePicker: Button? = null
    var btnTimePicker: Button? = null
    var txtTitle: EditText? = null
    var txtDate: EditText? = null
    var txtTime: EditText? = null
    private var mYear = 0
    private var mMonth = 0
    private var mDay = 0
    private var mHour = 0
    private var mMinute = 0
    private var asdf: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        insertMode()

        adapter = TodoListAdapter(this, items)
        //날짜마다 List 달라져야 함
        itemList = findViewById<View>(R.id.todoListView) as ListView
        itemList.adapter = adapter
        //임의로 넣어 둔 것
        items.add(Todo("test", "a", "a", "a", "a", "a", Comment("a", "a", "a", "a")))

        adapter.notifyDataSetChanged()
        adapter.notifyDataSetInvalidated()

        val calendar: Calendar = Calendar.getInstance()
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }

        /*databaseGroup.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
                //안써도 됨
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (groupData in dataSnapshot.children) {
                    // child 내에 있는 데이터만큼 반복
                    val grpMemName = groupData.child("grpMem").value.toString()
                    val productkey = productData.key
                    val productName = productData.child("name").value.toString()

                    if(productkey!=null) {
                        productkeylist.add(productName)
                        val goods = Goods(productkey,productName)
                        arraylist.add(goods)
                        adapter.notifyDataSetChanged()
                        adapter.notifyDataSetInvalidated()
                    }
                }
            }

        })*/
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
            }
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
        //ok눌렀을 때도 리스너 알맞게 달기
        ok!!.setOnClickListener {
            asdf = txtTitle!!.text.toString()
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