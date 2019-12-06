package com.humanhorsebell.computer.hansung_calendar_manager

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.listview_addgroup.*
import android.view.Gravity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T



class ShowGroup : AppCompatActivity() {
    val firebaseReference: FirebaseDatabase = FirebaseDatabase.getInstance()
    val databasegroup = firebaseReference.reference.child("group")
    var groupList = ArrayList<String>()
    lateinit var userNo : String
    lateinit var groupNo : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listview_showgroup)
        userNo = intent.getStringExtra("userNo")

        val adapter1 = ArrayAdapter(this, android.R.layout.simple_list_item_1, groupList)

        listviewGroup.adapter = adapter1

        //현재 그룹명을 list에 저장
        databasegroup.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                groupList.add("나의 캘린더")
                for (child in dataSnapshot.children) {

                    if(child.child("grpMem").child(userNo).value.toString().equals("true")) {
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




        listviewGroup.onItemClickListener = AdapterView.OnItemClickListener{parent, view, position, id ->
            val intent2 = Intent(this,EditActivity::class.java)
            intent2.putExtra("userNo",userNo)
            databasegroup.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    for (child in dataSnapshot.children) {

                        if(child.child("grpName").value.toString().equals(groupList.get(position))) {
                            val groupNo = child.key.toString()

                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                }
            })
            intent2.putExtra("groupNo",groupNo)
            startActivity(intent2)
        }

    }
}