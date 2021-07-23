package com.example.pomodoro.viewmodel

import android.os.Build
import android.os.CountDownTimer
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class TimersViewModel : ViewModel() {
    //    private val timerListItems = mutableStateListOf<TimerItem>()

//    fun addItem(): UUID {
//        val newItem = TimerItemPomodoro().countDownTimer.onTick()
//        timerListItems.add(newItem)
//        return newItem.id
//    }
    //private val timersList = mutableStateListOf(TimerItemPomodoro(), TimerItemPomodoro(), TimerItemPomodoro(), TimerItemPomodoro(), TimerItemPomodoro())

    private val timers: MutableState<MutableList<TimerItemPomodoro>> = mutableStateOf(mutableListOf())

    var previousTime = mutableStateOf(0L)
    var currentTime = mutableStateOf(previousTime)
    var value = mutableStateOf(1.00f)
    var isTimerRunning = mutableStateOf(false)
    var isTimerFinished = mutableStateOf(false)
    var pointActive = mutableStateOf(false)

//    var testText = mutableStateOf("default text")
//
//    fun onTestTextChanged(text: String) {
//        this.testText.value = text
//    }

    fun addTimer(id: UUID) {
        this.timers.value.add(TimerItemPomodoro(id))
    }

    fun removeTimer(id: UUID) {
        this.timers.value.removeIf { item -> item.id == id }
    }

    /**
     * Почти как у Олега, сделать список из этого дата-класса, при создании таймера специальной функцией создавать новый таймер и присваивать его в текущий дата-класс.
     */

    data class TimerItemPomodoro(
        val id: UUID = UUID.randomUUID(),
        var previousTime: MutableState<Long> = mutableStateOf(0L),
        var currentTime: MutableState<MutableState<Long>> = mutableStateOf(previousTime),
        var value: MutableState<Float> = mutableStateOf(1.00f),
        var isTimerRunning: MutableState<Boolean> = mutableStateOf(false),
        var isTimerFinished: MutableState<Boolean> = mutableStateOf(false),
        var pointActive: MutableState<Boolean> = mutableStateOf(false)
    )

    enum class TimersStates {
        STOPPED, STARTED, FINISHED
    }
}

