package com.example.humanbell

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.humanhorsebell.computer.hansung_calendar_manager.R
import com.humanhorsebell.computer.hansung_calendar_manager.Todo
import java.util.ArrayList

class TodoListAdapter(context: Context, item: ArrayList<Todo>): BaseAdapter() {
    private val context = context
    private val item = item

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = LayoutInflater.from(context).inflate(R.layout.todo_list, parent, false)
        val listView = view.findViewById<View>(R.id.textViewName) as TextView
        listView.text = item[position].name

        return view
    }

    override fun getItem(position: Int) = item[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getCount() = item.size
}