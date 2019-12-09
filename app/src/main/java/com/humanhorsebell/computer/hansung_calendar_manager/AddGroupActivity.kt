package com.humanhorsebell.computer.hansung_calendar_manager

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import java.util.*


class AddGroupActivity : AppCompatActivity() {
    val firebaseReference: FirebaseDatabase = FirebaseDatabase.getInstance()
    val databaseusers = firebaseReference.reference.child("user")
    val databasegroup = firebaseReference.reference.child("group")
    lateinit var databaseschedule : DatabaseReference


    lateinit var groupname : EditText
    lateinit var userNo : String
    var id: Int = 0 //현재 사용자 갯수 세어서 user기본키로 두려구
    lateinit var userBirth : Array<String>
    lateinit var thisyearbirth : String
    lateinit var userName : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userNo = intent.getStringExtra("userNo")
        //다이얼로그 만들기
        val builder = AlertDialog.Builder(this)
       // val layoutInflater = layoutInflater
        val view = layoutInflater.inflate(R.layout.custom_addgroup, null)

        groupname = view.findViewById<EditText>(R.id.editTextgroupname)

        builder.setView(view)
        builder.setTitle("그룹 추가")

        databasegroup.addListenerForSingleValueEvent(checkGroup)
        databaseusers.addListenerForSingleValueEvent(checkBirth)

        //확인 눌렀을때
        builder.setPositiveButton("확인"){ dialogInterface, i ->
            //override fun onClick(dialog: DialogInterface?, which: Int) {

            //group->0->grpName : value
            databasegroup.child("g"+id.toString()).child("grpName").setValue(groupname.text.toString())
            //group->0->grpMem ->
            databasegroup.child("g"+id.toString()).child("grpMem").child(userNo).setValue("true")
            //user->userNo->group->그룹아이디 : true
            databaseusers.child(userNo).child("group").child("g"+id.toString()).setValue("true")

            //Toast.makeText(this,userBirth[1]+userBirth[2],Toast.LENGTH_SHORT).show()
            //올해로 생일 년도 바꿔주기

            for(i in 0..9) {

                databaseschedule = databasegroup.child("g" + id.toString()).child("schedule")
                databaseschedule.addListenerForSingleValueEvent(checkscheduleID)

  }
            val intent2 = Intent(this@AddGroupActivity, ShowGroup::class.java)

            //기본키넘김=>얘로 나중에 사용자누군지 계속 구분해야지
            intent2.putExtra("userNo", userNo)
            startActivity(intent2)
            finish()
        }

        //취소 눌렀을때 => 그냥 끄기
        builder.setNegativeButton("취소"){ dialogInterface: DialogInterface, i: Int ->
            finish()
        }
        builder.create().show()


    }
    val checkGroup = object : ValueEventListener {
        //checkEmail=false
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            for (child in dataSnapshot.children) {
                id = dataSnapshot.childrenCount.toInt()
            }
        }
        override fun onCancelled(databaseError: DatabaseError) {}
    }

    val checkBirth = object : ValueEventListener {
        //checkEmail=false
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            for (child in dataSnapshot.children) {
                if(child.key.equals(userNo)){
                    userBirth = child.child("birth").value.toString().split("-").toTypedArray()
                    userName = child.child("name").value.toString()
                }
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
                databaseschedule.child(thisyearbirth).child(scheduleid.toString()).child("name").setValue(userName+" 님의 생일")
                //group->g1->schedule->user의 생일->키->memo : vlaue(추가mem name + "생일")
                databaseschedule.child(thisyearbirth).child(scheduleid.toString()).child("memo").setValue(userName + "님의 생일")

            }

        }
        override fun onCancelled(databaseError: DatabaseError) {}
    }

}