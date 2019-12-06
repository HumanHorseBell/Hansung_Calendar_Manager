package com.humanhorsebell.computer.hansung_calendar_manager

import android.app.AlertDialog
import android.os.Bundle
import android.view.View

import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

import kotlinx.android.synthetic.main.listview_addgroup.*
import java.util.*
import kotlin.collections.ArrayList


class AddGroupMemActivity : AppCompatActivity() {
    val firebaseReference: FirebaseDatabase = FirebaseDatabase.getInstance()
    val databaseusers = firebaseReference.reference.child("user")
    val databasegroup = firebaseReference.reference.child("group")
    lateinit var databaseschedule : DatabaseReference
    var groupMemList = ArrayList<String?>()
    //var groupUserList = arrayOf("김지현", "이호윤", "최지은", "김진민")
    lateinit var groupName : String //넘어온거
    lateinit var inputemail : EditText
    lateinit var adapter1 : ArrayAdapter<String?>
    lateinit var groupNo : String //그룹 기본키
    lateinit var userNo : String //그룹 기본키
    lateinit var thisyearbirth : String
    //lateinit var scheduleid : Array<Int>
    lateinit var userName : String
    lateinit var userBirth : Array<String>

    //그룹에 있는 유저 List에 저장

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listview_addgroup)

        userNo = intent.getStringExtra("userNo")
        groupNo = intent.getStringExtra("groupNo")
        groupName = intent.getStringExtra("groupName")

        setTitle(groupName+"의 그룹원")

//        databasegroup.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//
//                for (child in dataSnapshot.children) {
//
//                    if(child.child("grpName").value.toString().equals(groupName)) {
//                        groupNo = child.key.toString()
//
//                    }
//                }
//                adapter1.notifyDataSetChanged()
//                adapter1.notifyDataSetInvalidated()
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // Failed to read value
//            }
//        })

        adapter1 = ArrayAdapter(this, android.R.layout.simple_list_item_1, groupMemList)

        listviewGroup.adapter = adapter1

        //현재 그룹원을 list에 저장
        databaseusers.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (child in dataSnapshot.children) {

                    if(child.child("group").child(groupNo).value.toString().equals("true")) {
                        groupMemList.add(child.child("name").value.toString())

                    }
                }
                adapter1.notifyDataSetChanged()
                adapter1.notifyDataSetInvalidated()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })


        floatingadduserbtn.setOnClickListener(onClickListener);

    }

    //그룹원 추가
    var onClickListener: View.OnClickListener = View.OnClickListener {
        //다이얼로그 만들기
        val builder = AlertDialog.Builder(this)
       // val layoutInflater = layoutInflater
        val view = layoutInflater.inflate(R.layout.custom_adduser, null)

        inputemail = view.findViewById<EditText>(R.id.editTextemail)

        builder.setView(view)
        builder.setTitle("그룹원 추가")

        //확인 눌렀을때 => 사용자추가(중복체크 후)
        builder.setPositiveButton("확인") { dialog, which ->
            databaseusers.addListenerForSingleValueEvent(checkEmail)
            //유저가 있는지 확인

        }
        //취소 눌렀을때 => 그냥 끄기
        builder.setNegativeButton("취소", null)
        builder.create().show()
    }

    //그룹원 이메일로 추가
    //email있는지 확인
    val checkEmail = object : ValueEventListener {
        var checkemail=false
        override fun onDataChange(dataSnapshot: DataSnapshot) {

            for (child in dataSnapshot.children) {
                //Log.i("kjharu",child.child("email").value.toString())
                //이멜있나
                if (child.child("email").value.toString().equals(inputemail.text.toString())) {

                    userBirth = child.child("birth").value.toString().split("-").toTypedArray()
                    checkemail=true
                    //리스트에 넣어주기
                    groupMemList.add(child.child("name").value.toString())
                    //user->group->그룹이름:true해주기
                    databaseusers.child(child.key.toString()).child("group").child(groupNo).setValue("true")

                    //group->그룹이름->grpMem->추가한 유저 추가
                    databasegroup.child(groupNo).child("grpMem").child(child.key.toString()).setValue("true")

                    databaseschedule = databasegroup.child(groupNo).child("schedule")
                    //추가되는 유저의 생일 추가
                    //올해로 생일 년도 바꿔주기
                   // val c = Calendar.getInstance()
                    //thisyearbirth = (c.get(Calendar.YEAR)).toString() + "-" + userBirth[1] + "-" + userBirth[2]
                    databaseschedule.addListenerForSingleValueEvent(checkscheduleID)

                    userName = child.child("name").value.toString()

                }
            }
            //이멜 못찾음
            if(!checkemail){
                Dialogmessage(this@AddGroupMemActivity,"경고","없는 이메일이예요.")
            }
            else{
                adapter1.notifyDataSetChanged()
                adapter1.notifyDataSetInvalidated()
                Dialogmessage(this@AddGroupMemActivity,"알림","그 분..추가했어요.")

            }

        }

        override fun onCancelled(databaseError: DatabaseError) {}
    }

    val checkscheduleID = object : ValueEventListener {
        //checkEmail=false
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            for(i in 0..9) {
                val c = Calendar.getInstance()
                thisyearbirth = (c.get(Calendar.YEAR) + i).toString() + "-" + userBirth[1] + "-" + userBirth[2]
                val scheduleid = dataSnapshot.child(thisyearbirth).childrenCount.toInt()

                //group->g1->schedule->user의 생일->키->startDate : vlaue(생일)
                databaseschedule.child(thisyearbirth).child(scheduleid.toString()).child("startDate").setValue(thisyearbirth)
                //group->g1->schedule->user의 생일->키->startTime : vlaue(00:00)
                databaseschedule.child(thisyearbirth).child(scheduleid.toString()).child("startTime").setValue("00:00")
                //group->g1->schedule->user의 생일->키->endDate : vlaue(생일)
                databaseschedule.child(thisyearbirth).child(scheduleid.toString()).child("endDate").setValue(thisyearbirth)
                //group->g1->schedule->user의 생일->키->endTime : vlaue(23:59)
                databaseschedule.child(thisyearbirth).child(scheduleid.toString()).child("endTime").setValue("11:59")
                //group->g1->schedule->user의 생일->키->name : vlaue("생일")
                databaseschedule.child(thisyearbirth).child(scheduleid.toString()).child("name").setValue("생일")
                //group->g1->schedule->user의 생일->키->memo : vlaue(추가mem name + "생일")
                databaseschedule.child(thisyearbirth).child(scheduleid.toString()).child("memo").setValue(userName + "님의 생일")

            }
        }
        override fun onCancelled(databaseError: DatabaseError) {}
    }

}