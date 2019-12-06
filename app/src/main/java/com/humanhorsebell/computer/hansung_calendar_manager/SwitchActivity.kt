package com.humanhorsebell.computer.hansung_calendar_manager

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.view.marginLeft

fun switchActivity(context: Context, linearLayout: LinearLayout){
    val buttons = ArrayList<Button>()

    for(i in 0..2){
        buttons.add(Button(context))
    }

    buttons[0].text = "Calendar"
    buttons[1].text = "Wishlist"
    buttons[2].text = "Timeline"

    buttons[0].setOnClickListener { View.OnClickListener{ v ->
        context.startActivity(Intent(context, EditActivity::class.java))
    } }

    buttons[1].setOnClickListener { View.OnClickListener{v ->
        context.startActivity(Intent(context, WishListActivity::class.java))
    } }

    buttons[2].setOnClickListener { View.OnClickListener { v ->
        context.startActivity(Intent(context, TimelineActivity::class.java))
    } }

    val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
    layoutParams.weight = 1.toFloat()
    layoutParams.leftMargin = 2
    layoutParams.rightMargin = 2

    for(i in 0 until buttons.size) {
        buttons[i].layoutParams = layoutParams
        buttons[i].setBackgroundColor(Color.rgb(63, 221, 225))
        linearLayout.addView(buttons[i])
    }
}