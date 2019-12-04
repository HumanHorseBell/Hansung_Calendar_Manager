package com.humanhorsebell.computer.hansung_calendar_manager

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_join.*
import kotlinx.android.synthetic.main.custom_addgroup.*


class AddGroupActivity : AppCompatActivity() {
    val firebaseReference: FirebaseDatabase = FirebaseDatabase.getInstance()
    val databaseusers = firebaseReference.reference.child("user")
    val databasegroup = firebaseReference.reference.child("group")

    var checkGName = false //그룹이름 없으면 false 있으면 true
    lateinit var groupname : EditText
    lateinit var userNo : String
    var id: Int = 0 //현재 사용자 갯수 세어서 user기본키로 두려구

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

        //확인 눌렀을때 => 그룹이름 중복 확인
        builder.setPositiveButton("확인"){ dialogInterface, i ->
            //override fun onClick(dialog: DialogInterface?, which: Int) {

            //group->0->grpName : value
            databasegroup.child("g"+id.toString()).child("grpName").setValue(groupname.text.toString())
            //group->0->grpMem ->
            databasegroup.child("g"+id.toString()).child("grpMem").child(userNo).setValue("true")
            //user->userNo->group->그룹아이디 : true
            databaseusers.child(userNo).child("group").child("g"+id.toString()).setValue("true")

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

}