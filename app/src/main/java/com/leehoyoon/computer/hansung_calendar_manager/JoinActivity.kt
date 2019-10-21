package com.humanhorsebell.computer.hansung_calendar_manager


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_join.*
import java.util.*


/**
 * Created by lovel on 2019-10-21.
 */
class JoinActivity : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)


        //생일 DatePicker사용
        birthcheckbtn.setOnClickListener {
            val newFragment = BirthCheckFragment()   //DatePickerFragment 객체 생성
            newFragment.show(getSupportFragmentManager(), "BirthCheck")                //프래그먼트 매니저를 이용하여 프래그먼트 보여주기
        }
    }
}