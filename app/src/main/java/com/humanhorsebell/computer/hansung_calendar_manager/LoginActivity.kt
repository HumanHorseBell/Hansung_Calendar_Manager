package com.humanhorsebell.computer.hansung_calendar_manager

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import android.R.*
/**
 * Created by lovel on 2019-10-21.
 */
class LoginActivity : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        creatuserbtn.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }

        //현재 로그인시 바로 그룹추가 fragment사용-> 나중에 바꾸기
        //그룹추가 DatePicker사용
        loginbtn.setOnClickListener {
            val intent2 = Intent(this, AddGroupFragment::class.java)
            startActivity(intent2)
        }
    }
}