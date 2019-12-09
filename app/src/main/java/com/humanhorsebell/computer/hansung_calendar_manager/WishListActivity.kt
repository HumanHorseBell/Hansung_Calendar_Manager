package com.humanhorsebell.computer.hansung_calendar_manager

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.activity_wish_list.*
import kotlinx.android.synthetic.main.activity_wish_list.fab
import kotlin.collections.ArrayList

class WishListActivity : AppCompatActivity() {
    var wishlist = ArrayList<String?>()
    val database = FirebaseDatabase.getInstance().reference
    lateinit var adapter: BaseAdapter
    lateinit var userNo: String
    var groupNo: String? = null
    var groupkeys = ArrayList<String>()

    val wishListValueEventListener: ValueEventListener = object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {}
        override fun onDataChange(p0: DataSnapshot) {
            for (data in p0.children) {
                val wishlistNum: String? = data.key
                if (wishlistNum != null) {
                    wishlist.add(data.value.toString())
                    adapter.notifyDataSetChanged()
                    adapter.notifyDataSetInvalidated()
                }
            }
        }

    }

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wish_list)

        userNo = intent.getStringExtra("userNo")
        groupNo = intent.getStringExtra("groupNo")
        Log.i("@@@@@@", "groupNo " + groupNo)

        switchActivity(this, applicationContext, linearBottom, userNo, groupNo)
        if(groupNo == null)
            fab.visibility = View.GONE

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, this.wishlist)

        val listView = findViewById<View>(R.id.wishListView) as ListView
        fab.setOnClickListener {
            insertTodo()
        }
        listView.adapter = adapter
        allOrOneGrp(groupNo)
    }

    private fun totalWish() {
        database.child("user").child(userNo).child("group").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                groupkeys.clear()
                for (data in dataSnapshot.children) {
                    var groupKey = data.key
                    if (groupKey != null) {
                        groupkeys.add(groupKey.toString())
                        Log.i("@@@@@@", "groupKey " + groupKey)
                        perGrpWish(groupKey.toString())
                    }
                }
            }

        })
    }

    private fun allOrOneGrp(grpNum: String?) {
        if(grpNum == null) {
            wishlist.clear()
            totalWish()
        }
        else {
            wishlist.clear()
            perGrpWish(grpNum)
        }
    }

    private fun perGrpWish(groupNum: String) {
        Log.i("@@@@@@", "이제 그룹별로 위키리스트 찍ㅡㄴ당")
        database.child("group").child(groupNum).child("wishlist").addValueEventListener(wishListValueEventListener)
    }

    private fun insertTodo() {
        var cancelBtn: Button? = null
        var ok: Button? = null
        var txtTitle: EditText? = null
        val view = layoutInflater.inflate(R.layout.wishlist_dialog, null, false).apply {
            cancelBtn = findViewById<View>(R.id.btn_cancel) as Button
            ok = findViewById<View>(R.id.btn_ok) as Button
            txtTitle = findViewById<View>(R.id.calendar_title) as EditText

        }
        val dialog = AlertDialog.Builder(this)
                .setView(view)
                .create()
        dialog.show()
        cancelBtn!!.setOnClickListener {
            dialog.dismiss()
        }
        ok!!.setOnClickListener {
            var i = 1
            var wishRef = database.child("group").child(groupNo!!).child("wishlist")

            wishRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(dataSnapshot: DatabaseError) {}
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (data in dataSnapshot.children) {
                        i++
                    }
                    wishRef.child("wishlist" + i).setValue(txtTitle!!.text.toString()!!)
                }
            })
            dialog.dismiss()
        }
        wishlist.clear()
    }

}
