package com.humanhorsebell.computer.hansung_calendar_manager

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.humanhorsebell.computer.hansung_calendar_manager.R

import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.content_main2.*
import org.jetbrains.anko.startActivity

class Main2Activity : AppCompatActivity() {
    //val realm = Realm.getDefaultInstance()- 객체 생성
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        setSupportActionBar(toolbar)

        //val realmResult = realm.where<Todo>().findAll().sort("date",Sort.DESCENDING)


//전체 할 일 정보를 가져와서 날짜순으로 내림차순 정렬
//val adapter = TodoListAdapter(realmResult)
        //listView.adapter = adapter
        fab.setOnClickListener {
            startActivity<EditActivity>()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //realm.close() 삭제쨔응
    }
}

