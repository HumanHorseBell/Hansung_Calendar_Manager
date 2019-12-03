package com.humanhorsebell.computer.hansung_calendar_manager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_main2.*

class WishListActivity : AppCompatActivity() {
    var wishlist = ArrayList<String?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wish_list)

        var adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, this.wishlist)

        val listView = findViewById<ListView>(R.id.wishListView)

        listView.adapter = adapter
    }
}
