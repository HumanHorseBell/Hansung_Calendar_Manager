package com.humanhorsebell.computer.hansung_calendar_manager

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.add_category.*

class Add_Category : AppCompatActivity() {
    lateinit var userNo : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_category)

        userNo = intent.getStringExtra("userNo")

        //그룹추가
        addGroupbtn.setOnClickListener({
            val intent2 = Intent(this@Add_Category, AddGroupActivity::class.java)

            //기본키넘김=>얘로 나중에 사용자누군지 계속 구분해야지
            intent2.putExtra("userNo",userNo)
            startActivity(intent2)
        });
        //그룹원추가
        addGroupMembtn.setOnClickListener({
            val intent2 = Intent(this@Add_Category, ShowGroup::class.java)

            intent2.putExtra("userNo",userNo)
            startActivity(intent2)
        })

    }
}