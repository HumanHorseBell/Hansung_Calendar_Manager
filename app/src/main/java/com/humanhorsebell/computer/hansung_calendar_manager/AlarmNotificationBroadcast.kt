package com.humanhorsebell.computer.hansung_calendar_manager

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toBitmap
import java.text.SimpleDateFormat
import java.util.*

var alarmSchedule: Schedule? = null

class AlarmNotificationBroadcast : BroadcastReceiver() {
    val channelId = "grpNo" //수정
    val channelName = "grpName" //수정

    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        /*val bundle = intent?.extras
        val schedule = bundle?.getParcelable<Schedule>("schedule")*/

        //버전 체크
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val importance = NotificationManager.IMPORTANCE_HIGH;
            val notificationChannel = NotificationChannel(channelId, channelName, importance)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        val builder =  NotificationCompat.Builder(context.applicationContext, channelId)
        val notificationIntent = Intent(context.applicationContext, context.applicationContext.javaClass) //수정
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP);

        val pendingIntent = PendingIntent.getActivity(context.applicationContext, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val contentView = RemoteViews(context.packageName, R.layout.notification_alarm)
        contentView.setTextViewText(R.id.textViewTitle, alarmSchedule?.name)
        contentView.setTextViewText(R.id.textViewDate, alarmSchedule?.startDate?.day)
        contentView.setImageViewBitmap(R.id.imageViewIcon, context.resources.getDrawable(R.drawable.calendar).toBitmap())

        builder.setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_notification_calendar_icon)
                .setContent(contentView)
                .setContentIntent(pendingIntent)
        notificationManager.notify(0, builder.build())
    }
}

fun setAlarm(context: Context, schedule: Schedule) {
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    val notificationIntent = Intent(context, AlarmNotificationBroadcast::class.java)

    val pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, 0)
    alarmSchedule = schedule

    val calendar = Calendar.getInstance()
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")
    val date = formatter.parse(schedule.startDate.day + " " + schedule.startDate.time)
    calendar.time = date
    val alarmTime = calendar.timeInMillis + 1000 * 60 * 15
    val localTime = System.currentTimeMillis()
    val time = Calendar.getInstance()
    time.timeInMillis = localTime
    alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent)
}
