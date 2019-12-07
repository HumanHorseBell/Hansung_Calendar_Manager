package com.humanhorsebell.computer.hansung_calendar_manager

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.v4.media.MediaBrowserCompat
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.core.view.marginLeft

fun switchActivity(activity: Activity, context: Context, linearLayout: LinearLayout, userNo: String, groupNo: String?){
    val buttons = ArrayList<Button>()

    for(i in 0..2){
        buttons.add(Button(context))
    }

    buttons[0].text = "Calendar"
    buttons[1].text = "Wishlist"
    buttons[2].text = "Timeline"

    buttons[0].setOnClickListener { v ->
        val intent = Intent(context, EditActivity::class.java)
        moveToNextActivity(activity, context, intent, userNo, groupNo)
    }

    buttons[1].setOnClickListener {v ->
        val intent = Intent(context, WishListActivity::class.java)
        moveToNextActivity(activity, context, intent, userNo, groupNo)
    }

    buttons[2].setOnClickListener { v ->
        val intent = Intent(context, TimelineActivity::class.java)
        moveToNextActivity(activity, context, intent, userNo, groupNo)
    }

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

fun moveToNextActivity(activity: Activity, context: Context, intent: Intent, userNo: String, groupNo: String?){
    intent.putExtra("userNo", userNo)
    intent.putExtra("groupNo", groupNo)
    context.startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK))
    activity.finish()
}