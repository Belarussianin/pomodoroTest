package com.example.pomodoro.viewmodel

import android.os.Build
import android.os.CountDownTimer
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.util.*

class TimersViewModel : ViewModel() {
//    private val timerListItems = mutableStateListOf<TimerItem>()
//
//    val size: Int
//        get() = timerListItems.size
//
//    fun addItem(): UUID {
//        val newItem = TimerItemPomodoro().countDownTimer.onTick()
//        timerListItems.add(newItem)
//        return newItem.id
//    }
//
//    @RequiresApi(Build.VERSION_CODES.N)
//    fun removeItem(id: UUID) {
//        for (el in 0..timerListItems.lastIndex) {
//            println("old id's: ${timerListItems[el].id}")
//        }
//        timerListItems.removeIf { item ->
//            item.id == id
//        }
//        for (el in 0..timerListItems.lastIndex) {
//            println("new id's: ${timerListItems[el].id}")
//        }
//    }
//
//    fun clearAll() {
//        timerListItems.clear()
//    }

    fun main() {
        val timer = TimerItemPomodoro(currentTimeMillis = 10000L)

    }

    /**
     * Почти как у Олега, сделать список из этого дата-класса, при создании таймера специальной функцией создавать новый таймер и присваивать его в текущий дата-класс.
     */

    data class TimerItemPomodoro(
        val id: UUID = UUID.randomUUID(),
        var currentTimeMillis: Long,
        var state: TimersStates = TimersStates.STOPPED,
        var countDownTimer: CountDownTimer? = null
    )

    enum class TimersStates {
        STOPPED, STARTED, FINISHED
    }
}

