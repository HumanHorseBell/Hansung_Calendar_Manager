package com.humanhorsebell.computer.hansung_calendar_manager

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.graphics.drawable.toBitmap

class AlarmNotificationBroadcast : BroadcastReceiver() {
    val channelId = "grpNo" //수정
    val channelName = "grpName" //수정

    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val bundle = intent?.extras
        val schedule = bundle?.getParcelable<Schedule>("schedule")


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
        contentView.setTextViewText(R.id.textViewTitle, schedule?.name)
        contentView.setTextViewText(R.id.textViewDate, schedule?.startDate?.day)
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