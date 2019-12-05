package com.humanhorsebell.computer.hansung_calendar_manager

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*


fun setAlarm(context: Context, schedule: Schedule) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val notificationIntent = Intent(context, AlarmNotificationBroadcast::class.java)
    notificationIntent.putExtra("schedule", schedule)

    val pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
    val calendar = Calendar.getInstance()
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
    val date = formatter.parse(schedule.startDate.day + " " + schedule.startDate.time)
    calendar.time = date
    val alarmTime = calendar.timeInMillis
    val localTime = System.currentTimeMillis()
    val time = Calendar.getInstance()
    time.timeInMillis = localTime
    alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent)
}
