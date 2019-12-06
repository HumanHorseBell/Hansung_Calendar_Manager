package com.humanhorsebell.computer.hansung_calendar_manager

data class Todo(
    val name: String,
    val todoDate: String,
    val todoEndDate: String,
    val startTime: String,
    val endTime: String?,
    val memo: String?,
    val comment: Comment?
)
