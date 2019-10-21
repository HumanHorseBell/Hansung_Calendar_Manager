package com.example.kjhapplication

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker

import java.util.Calendar

import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.activity_join.*

/**
 * Created by lovel on 2019-10-21.
 */

class BirthCheckFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()      // 오늘 날짜로 디폴트값을 설정하기 위해 캘린더 객체 선언
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)              // MONTH : 0~11
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(activity, this, year, month, day) // this는 리스너를 가르키는데 이 프래그먼트 클래스 자신을 가리킨다.
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        // Do something with the date chosen by the user
        val activity = activity as JoinActivity              // JoinActivity의 birthday 버튼에 접근하기 위해 액티비티 객체 선언
        activity.birthcheckbtn.setText(year.toString() + "년 " + (month + 1) + "월 " + day + "일") // 유저가 선택한 날짜로 버튼 텍스트 변경
    }
}

