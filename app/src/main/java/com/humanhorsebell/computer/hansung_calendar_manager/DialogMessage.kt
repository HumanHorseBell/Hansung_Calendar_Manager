package com.humanhorsebell.computer.hansung_calendar_manager

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context

fun Dialogmessage(context: Context, title: String, message: String) {
    val builder = AlertDialog.Builder(context)
    builder.setTitle(title)
    builder.setMessage(message)
    builder.setPositiveButton("확인", null)
    builder.show()
}
