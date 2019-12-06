package com.humanhorsebell.computer.hansung_calendar_manager

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import java.net.URL

class ListAdapter(val context: Context, val items: ArrayList<Schedule>): BaseAdapter(){
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = LayoutInflater.from(context).inflate(R.layout.list_view_item, null)

        val picture = view.findViewById<ImageView>(R.id.picture)
        val group = view.findViewById<TextView>(R.id.textViewGroup)
        val date = view.findViewById<TextView>(R.id.textViewDay)
        val title = view.findViewById<TextView>(R.id.textViewTitle)

        if(items[position].picture != null){
            Glide.with(view).load(items[position].picture.toString()).placeholder(R.drawable.picture).error(R.drawable.error).into(picture)
        }
        else{
            picture.setImageResource(R.drawable.picture)
        }

        group.text = items[position].grpName

        var dateText = items[position].startDate.day
        if(items[position].startDate.day != items[position].endDate.day){
            dateText += " ~ " + items[position].endDate.day
        }
        date.text = dateText
        title.text = items[position].name

        return view
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return items.size
    }
}