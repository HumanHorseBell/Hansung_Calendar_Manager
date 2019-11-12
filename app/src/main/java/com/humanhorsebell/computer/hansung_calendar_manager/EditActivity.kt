package com.example.humanbell

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.humanhorsebell.computer.hansung_calendar_manager.R
import kotlinx.android.synthetic.main.activity_edit.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.calendarView
import org.jetbrains.anko.yesButton
import java.util.*

class EditActivity : AppCompatActivity() {
    //val realm = Realm.getDefaultInstance()
    val calendar: Calendar = Calendar.getInstance()

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
        doneFab.setOnClickListener {
            insertTodo()
        }
    }

    private fun updateMode(id: Long) {//수정모드 초기화
//    val todo = realm.where<Todo>().equalTo("id",id).findFirst()!!
        //  todoEditText.setText(todo.title)
        //calendarView.date = todo.date

        doneFab.setOnClickListener { updateTodo(id) }//완료버튼 클릭하면 수정
        deleteFab.setOnClickListener { deleteTodo(id) }
    }

    override fun onDestroy() {
        super.onDestroy()
        //realm.close()
    }

    private fun insertTodo() {
        //realm.beginTransaction() 트랜잭션 시작
        //val newItem = realm.createObject<Todo>(nextId()) 새 객체 생성
        //newItem.title = todoEditText.text.toString() 값설정
        //newItem.date = calendar.timeInMillis

        //realm.commitTransaction() //트랜잭션 종료 반영
        alert("내용이 추가되었습니다.") {
            yesButton { finish() }
        }.show()
    }

    private fun nextId(): Int {
//        val maxId = realm.where<Todo>().max("id")
        // if(maxId != null){
        // return maxId.toInt()+1
        // }
        return 0
    }

    //createObject<T:RealmModel>(primaryKeyValue: Any?) T타입의 Realm의 객체 생성
    private fun updateTodo(id: Long) {//할일수정
        //realm.beginTransaction() 트랜잭션 시작
//    val updateItem = realm.where<Todo>().equalTo("id",id).findFirst()!! 값수정
// updateItem.title = todoEditText.text.toString()
        //updateItem.date = calendar.timeInMillis
        //realm.commitTransaction()//트랜잭션 종료 반영
        alert("내용이 변경되었습니다.") {
            yesButton { finish() }
        }.show()
    }

    private fun deleteTodo(id: Long) {
        //realm.beginTransaction() 객체 생성
        //val deleteItem = realm.where<Todo>().equalTo("id",id).findFirst()!! //삭제할객체
        //deleteItem.deleteFromRealm()
        //realm.commitTransaction()
        alert("내용이 삭제되었습니다.") {
            yesButton { finish() }
        }.show()
    }
}
