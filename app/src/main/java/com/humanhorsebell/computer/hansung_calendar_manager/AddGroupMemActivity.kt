package com.humanhorsebell.computer.hansung_calendar_manager

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.custom_adduser.*
import kotlinx.android.synthetic.main.listview_addgroup.*


class AddGroupMemActivity : AppCompatActivity() {
    val firebaseReference: FirebaseDatabase = FirebaseDatabase.getInstance()
    val databaseusers = firebaseReference.reference.child("user")
    val databasegroup = firebaseReference.reference.child("group")
    var groupMemList = ArrayList<String?>()
    //var groupUserList = arrayOf("김지현", "이호윤", "최지은", "김진민")
    lateinit var groupName : String //넘어온거
    lateinit var inputemail : EditText
    lateinit var adapter1 : ArrayAdapter<String?>
    lateinit var groupNo : String //그룹 기본키

    //그룹에 있는 유저 List에 저장

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listview_addgroup)

        groupName = intent.getStringExtra("groupName")

        databasegroup.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (child in dataSnapshot.children) {

                    if(child.child("grpName").value.toString().equals(groupName)) {
                        groupNo = child.key.toString()

                    }
                }
                adapter1.notifyDataSetChanged()
                adapter1.notifyDataSetInvalidated()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })

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
                Log.i("kjharu",child.child("email").value.toString())
                //이멜있나
                if (child.child("email").value.toString().equals(inputemail.text.toString())) {

                    checkemail=true
                    //리스트에 넣어주기
                    groupMemList.add(child.child("name").value.toString())
                    //user->group->그룹이름:true해주기
                    databaseusers.child(child.key.toString()).child("group").child(groupNo).setValue("true")

                    //group->그룹이름->grpMem->추가한 유저 추가
                    databasegroup.child(groupNo).child("grpMem").child(child.key.toString()).setValue("true")
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
}