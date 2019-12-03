package com.humanhorsebell.computer.hansung_calendar_manager

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_edit.*
import java.util.*


class EditActivity : AppCompatActivity() {
    //val realm = Realm.getDefaultInstance()
    val calendar: Calendar = Calendar.getInstance()
    var btnDatePicker: Button? = null
    var btnTimePicker: Button? = null
    var txtDate: EditText? = null
    var txtTime: EditText? = null
    var cancelBtn: Button? = null
    private var mYear = 0
    private var mMonth = 0
    private var mDay = 0
    private var mHour = 0
    private var mMinute = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        val id = intent.getLongExtra("id", -1L)
        if (id == -1L) {
            insertMode()
        } else {
            updateMode(id)
        }
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

        }
    }

    private fun insertMode() {//삽입모드 초기화
        //       deleteFab.visibility= View.GONE
        fab.setOnClickListener {
            insertTodo()
        }
    }

    private fun updateMode(id: Long) {//수정모드 초기화
//    val todo = realm.where<Todo>().equalTo("id",id).findFirst()!!
        //  todoEditText.setText(todo.title)
        //calendarView.date = todo.date

        fab.setOnClickListener { updateTodo(id) }//완료버튼 클릭하면 수정
        deleteFab.setOnClickListener { deleteTodo(id) }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun insertTodo() {
        val view = layoutInflater.inflate(R.layout.activity_calendar_dialog, null, false).apply {
            var add_calendarBtn = findViewById<Button>(R.id.add_calendar)
            add_calendarBtn.setOnClickListener {
                insertDetailTodo(this)
            }
        }
        val dialog = AlertDialog.Builder(this)
                .setView(view)
                .create()
        dialog.show()
    }

    private fun insertDetailTodo(v: View) {
        var onAndoff = true
        val view = layoutInflater.inflate(R.layout.activity_calendar_dialog_add, null, false).apply {
            btnDatePicker = findViewById<View>(R.id.btn_date) as Button
            btnTimePicker = findViewById<View>(R.id.btn_time) as Button
            txtDate = findViewById<View>(R.id.in_date) as EditText
            txtTime = findViewById<View>(R.id.in_time) as EditText
            btnDatePicker!!.setOnClickListener {
                val c = Calendar.getInstance()
                mYear = c[Calendar.YEAR]
                mMonth = c[Calendar.MONTH]
                mDay = c[Calendar.DAY_OF_MONTH]
                val datePickerDialog = DatePickerDialog(v.context,
                        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth -> txtDate!!.setText(dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year) }, mYear, mMonth, mDay)
                datePickerDialog.show()
            }
            btnTimePicker!!.setOnClickListener{
                val c = Calendar.getInstance()
                mHour = c[Calendar.HOUR_OF_DAY]
                mMinute = c[Calendar.MINUTE]
                // Launch Time Picker Dialog
                val timePickerDialog = TimePickerDialog(v.context,
                        TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute -> txtTime!!.setText("$hourOfDay:$minute") }, mHour, mMinute, false)
                timePickerDialog.show()
            }
            cancelBtn = findViewById<View>(R.id.btn_cancel) as Button
            cancelBtn!!.setOnClickListener {
                onAndoff = false
                Toast.makeText(v.context, "눌림", Toast.LENGTH_SHORT)
            }
            val ok = findViewById<View>(R.id.btn_ok) as Button
            ok!!.setOnClickListener {
                val c = Calendar.getInstance()
                mHour = c[Calendar.HOUR_OF_DAY]
                mMinute = c[Calendar.MINUTE]
                // Launch Time Picker Dialog
                val timePickerDialog = TimePickerDialog(v.context,
                        TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute -> txtTime!!.setText("$hourOfDay:$minute") }, mHour, mMinute, false)
                timePickerDialog.show()
            }
        }
        val dialog = AlertDialog.Builder(v.context)
                .setView(view)
                .create()
        dialog.show()
        if(!onAndoff && dialog.isShowing()) {
            dialog.dismiss()
        }
    }

    private fun nextId(): Int {
        return 0
    }

    private fun updateTodo(id: Long) {//할일수정
        //realm.beginTransaction() 트랜잭션 시작
//    val updateItem = realm.where<Todo>().equalTo("id",id).findFirst()!! 값수정
// updateItem.title = todoEditText.text.toString()
        //updateItem.date = calendar.timeInMillis
        //realm.commitTransaction()//트랜잭션 종료 반영
        /*alert("내용이 변경되었습니다.") {
            yesButton { finish() }
        }.show()*/
    }

    private fun deleteTodo(id: Long) {
        //realm.beginTransaction() 객체 생성
        //val deleteItem = realm.where<Todo>().equalTo("id",id).findFirst()!! //삭제할객체
        //deleteItem.deleteFromRealm()
        //realm.commitTransaction()
        /*alert("내용이 삭제되었습니다.") {
            yesButton { finish() }
        }.show()*/
    }


}
