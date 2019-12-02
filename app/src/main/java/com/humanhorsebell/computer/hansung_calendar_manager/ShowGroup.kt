package com.humanhorsebell.computer.hansung_calendar_manager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.listview_addgroup.*

class ShowGroup : AppCompatActivity() {
    val firebaseReference: FirebaseDatabase = FirebaseDatabase.getInstance()
    val databasegroup = firebaseReference.reference.child("group")
    var groupList = ArrayList<String>()
    lateinit var userNo : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listview_showgroup)
        userNo = intent.getStringExtra("userNo")

        val adapter1 = ArrayAdapter(this, android.R.layout.simple_list_item_1, groupList)

        listviewGroup.adapter = adapter1

        //현재 그룹명을 list에 저장
        databasegroup.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (child in dataSnapshot.children) {

                    if(child.child("grpMem").child(userNo).value.toString().equals("true")) {
                        groupList.add(child.key.toString())
                    }
                    else{
                        //Log.i("kjharu",)
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
            val intent2 = Intent(this,AddGroupMemActivity::class.java)
            intent2.putExtra("userNo",userNo)
            intent2.putExtra("groupName",groupList.get(position))
            startActivity(intent2)
        }

    }
}