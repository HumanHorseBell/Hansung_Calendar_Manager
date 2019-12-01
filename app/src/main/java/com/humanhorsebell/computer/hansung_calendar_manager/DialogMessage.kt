package com.humanhorsebell.computer.hansung_calendar_manager

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context

fun Dialogmessage(context: Activity, title: String, message: String,close:String?=null) {
    val builder = AlertDialog.Builder(context)
    builder.setTitle(title)
    builder.setMessage(message)

    if(close==null) {
        builder.setPositiveButton("확인", null)
    }
    else if(close.equals("close")) {
        builder.setPositiveButton("확인") { dialogInterface, i ->
            context.finish()
        }
    }
    builder.show()
}
