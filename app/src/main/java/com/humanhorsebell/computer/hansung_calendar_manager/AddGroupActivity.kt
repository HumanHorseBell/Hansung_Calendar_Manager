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
import kotlinx.android.synthetic.main.custom_addgroup.*


class AddGroupActivity : AppCompatActivity() {
    val firebaseReference: FirebaseDatabase = FirebaseDatabase.getInstance()
    val databaseusers = firebaseReference.reference.child("users")
    val databasegroup = firebaseReference.reference.child("group")

    var checkGName = false //그룹이름 없으면 false 있으면 true
    lateinit var groupname : EditText
    lateinit var userNo : String

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

        //확인 눌렀을때 => 그룹이름 중복 확인
        builder.setPositiveButton("확인"){ dialogInterface, i ->
            //override fun onClick(dialog: DialogInterface?, which: Int) {
                databasegroup.addListenerForSingleValueEvent(checkGroup)

            //}
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
                //이미 있는 그룹이름
                if(child.key.toString().equals(groupname.text.toString())){
                    checkGName = true
                }
                else{

                }

            }
            if (checkGName.equals(false)) {
                //그룹에 그룹추가 + 맴버에 나추가
                databasegroup.child(groupname.text.toString()).child("grpMem").child(userNo).setValue("true")
                //나의 그룹정보에 userNo->group->그룹이름:true
                databaseusers.child(userNo).child("group").child(groupname.text.toString()).setValue("true")
                finish()
            }
            //그룹이름 중복
            else {
                Dialogmessage(this@AddGroupActivity, "경고", "이미 존재하는 그룹이름이예요","close")

            }

        }
        override fun onCancelled(databaseError: DatabaseError) {}
    }

}