package com.humanhorsebell.computer.hansung_calendar_manager

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.listview_addgroup.listviewGroup
import kotlinx.android.synthetic.main.listview_showgroup.*
import java.util.*
import kotlin.collections.ArrayList


class ShowGroup : AppCompatActivity() {
    val firebaseReference: FirebaseDatabase = FirebaseDatabase.getInstance()
    val databaseusers = firebaseReference.reference.child("user")
    val databasegroup = firebaseReference.reference.child("group")
    lateinit var databaseschedule: DatabaseReference
    var groupList = ArrayList<String>()
    lateinit var userNo: String
    var groupNo: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listview_showgroup)
        setTitle("내가 속한 그룹")

        userNo = intent.getStringExtra("userNo")

        val adapter1 = ArrayAdapter(this, android.R.layout.simple_list_item_1, groupList)

        listviewGroup.adapter = adapter1


        //현재 그룹명을 list에 저장
        databasegroup.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                groupList.add("나의 캘린더")
                for (child in dataSnapshot.children) {

                    if (child.child("grpMem").child(userNo).value.toString().equals("true")) {
                        groupList.add(child.child("grpName").value.toString())
                    }
                }

                adapter1.notifyDataSetChanged()
                adapter1.notifyDataSetInvalidated()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
            }
        })

        //그룹에 해당되는 캘린더로 넘어가기
        listviewGroup.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            databasegroup.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var findGrp = false

                    for (child in dataSnapshot.children) {
                        if (child.child("grpName").value.toString().equals(groupList.get(position))) {
                            groupNo = child.key.toString()
                            findGrp = true
                        }
                    }
                    if (findGrp == false) {
                        groupNo = null
                    }
                    val intent2 = Intent(this@ShowGroup, EditActivity::class.java)
                    intent2.putExtra("userNo", userNo)

                    intent2.putExtra("groupNo", groupNo)
                    startActivity(intent2)
                    finish()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                }
            })

        }


        //그룹추가
        addgroupbtn.setOnClickListener({
            val intent2 = Intent(this@ShowGroup, AddGroupActivity::class.java)

            //기본키넘김=>얘로 나중에 사용자누군지 계속 구분해야지
            intent2.putExtra("userNo", userNo)
            startActivity(intent2)
            //리스트 다시그리기
            finish()


        })

        //길게누르면 그룹원 추가
        listviewGroup.setOnItemLongClickListener({ parent, view, position, id ->
            databasegroup.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    var findGrp = false

                    for (child in dataSnapshot.children) {
                        if (child.child("grpName").value.toString().equals(groupList.get(position))) {
                            groupNo = child.key.toString()
                            findGrp = true
                        }
                    }
                    if (findGrp == false) {
                        groupNo = null
                    }
                    if(groupNo == null){
                        Dialogmessage(this@ShowGroup,"알림","여기에는 그룹원을 추가할 수 없어요.")
                    }
                    else {
                        val intent2 = Intent(this@ShowGroup, AddGroupMemActivity::class.java)
                        intent2.putExtra("userNo", userNo)

                        intent2.putExtra("groupNo", groupNo)
                        intent2.putExtra("groupName",groupList.get(position))
                        startActivity(intent2)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                }
            })
            true
        })
    }

}