package com.example.pomodoro

fun Long.toTimeFormatString(): String {
    fun displaySlot(count: Long): String {
        return if (count / 10L > 0) {
            "$count"
        } else {
            "0$count"
        }
    }

    if (this <= 0L) {
        return "00:00:00.00"
    }

    return "${displaySlot(this / 1000 / 3600)}:${displaySlot(this / 1000 % 3600 / 60)}:" +
            "${displaySlot(this / 1000 % 60)}.${displaySlot(this % 1000 / 10)}"
}

fun toMillis(hours: Long = 0, minutes: Long = 0, seconds: Long = 0): Long = (hours * 3_600_000) + (minutes * 60_000) + (seconds * 1000)
fun Long.toHours(): Long = this / 1000 / 60 / 60
fun Long.toMinutes(): Long = this / 1000 / 60
fun Long.toSeconds(): Long = this / 1000

fun Long.displayTime(): String {
    if (this <= 0L) {
        return START_TIME
    }
    val h = this / 1000 / 3600
    val m = this / 1000 % 3600 / 60
    val s = this / 1000 % 60
    val ms = this % 1000 / 10

    return "${displaySlot(h)}:${displaySlot(m)}:${displaySlot(s)}:${displaySlot(ms)}"
}

private fun displaySlot(count: Long): String {
    return if (count / 10L > 0) {
        "$count"
    } else {
        "0$count"
    }
}