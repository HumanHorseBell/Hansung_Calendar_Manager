package com.humanhorsebell.computer.hansung_calendar_manager

data class Todo(
    val todoDate: String,
    val todoEndDate: String,
    val startTime: String,
    val endTime: String,
    val name: String,
    val memo: String
)
