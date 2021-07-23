package com.example.pomodoro

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.lifecycle.LifecycleObserver
import com.example.pomodoro.services.TimerService
import com.example.pomodoro.ui.theme.PomodoroTheme
import com.example.pomodoro.viewmodel.TimersViewModel
import java.util.*

class MainActivity : ComponentActivity(), LifecycleObserver {
    private var currentServiceTime: Long = 0L
    private var timersCount: Int = 0

    @ExperimentalAnimationApi
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PomodoroTheme {
                TimersScreen(
                    currentTimersCount = { count ->
                        timersCount = count
                    },
                    onTick = { millisLeft ->
                        currentServiceTime = millisLeft
                        /** Periodic correcting of variable **/
                    }
                )
            }
        }
    }

    override fun onStop() {
        super.onStop()
        startTimerService()
    }

    override fun onStart() {
        super.onStart()
        stopTimerService()
    }

    private fun startTimerService() {
        if (timersCount == 1 && currentServiceTime > 0L) {
            val startIntent = Intent(this, TimerService::class.java)
            startIntent.putExtra(COMMAND_ID, COMMAND_START)
            startIntent.putExtra(STARTED_TIMER_TIME_MS, currentServiceTime)
            startService(startIntent)
        }
    }

    private fun stopTimerService() {
        currentServiceTime = 0L
        val stopIntent = Intent(this, TimerService::class.java)
        stopIntent.putExtra(COMMAND_ID, COMMAND_STOP)
        startService(stopIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimerService()
    }

    override fun onBackPressed() {
        minimizeApp()
    }

    private fun minimizeApp() {
        val startMain = Intent(Intent.ACTION_MAIN)
        startMain.addCategory(Intent.CATEGORY_HOME)
        startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(startMain)
    }
}

@ExperimentalAnimationApi
@RequiresApi(Build.VERSION_CODES.N)
@Preview(showBackground = true)
@Composable
fun Preview() {
    PomodoroTheme {
        TimersScreen(
            currentTimersCount = {
                //TODO()
            },
            onTick = {
                //TODO()
            }
        )
        //Test()
    }
}

@Composable
fun Test() {
    val list = remember { mutableStateListOf<Int>(0) }
    LazyColumn(
        modifier = Modifier.wrapContentSize()
    ) {
        items(list.size) { index ->
            Row {
                Text(
                    text = "Element index: ${list[index]}",
                    modifier = Modifier.wrapContentSize(),
                    fontSize = 20.sp
                )
                Button(
                    onClick = { list.removeAt(index) },
                    modifier = Modifier.wrapContentSize()
                ) {
                    Text(text = "delete", modifier = Modifier.wrapContentSize())
                }
            }
        }
    }
    Spacer(modifier = Modifier.size(50.dp))
    Row(
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(onClick = { list.add(list.size) }, modifier = Modifier.wrapContentSize()) {
            Text(text = "add", modifier = Modifier.wrapContentSize())
        }
        Button(onClick = { list.removeLastOrNull() }, modifier = Modifier.wrapContentSize()) {
            Text(text = "remove", modifier = Modifier.wrapContentSize())
        }
    }
}

@ExperimentalAnimationApi
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun TimersScreen(
    maxTimersCount: Int = 5,
    currentTimersCount: (count: Int) -> Unit,
    onTick: (millisLeft: Long) -> Unit
) {
    val timerHeight = 130
    val padding: Dp = 1.dp
    val buttonsBorderStrokeSize: Dp = 1.dp
    val timersViewModel = TimersViewModel()
    var countOfTimers by remember { mutableStateOf(1) }
    var numberText by remember { mutableStateOf("5") }

    val uuids = remember { mutableStateListOf<String>(UUID.randomUUID().toString()) }
    currentTimersCount(uuids.size)

    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        color = Color.White
                    )
                },
                modifier = Modifier.wrapContentHeight(align = Alignment.CenterVertically),
                backgroundColor = Color.Blue
            )
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight((timerHeight * 4).dp)
                ) {
                    /**
                     * Examples of [LazyColumn]
                    //                    items(countOfTimers) {
                    //                        TimerCard(
                    //                            durationMillis = numberText.toLongOrNull()?.times(1000) ?: 0,
                    //                            height = timerHeight,
                    //                            onDeleteButtonClick = { }
                    //                        )
                    //                    }
                    //
                    ////                val list = remember { mutableStateListOf("a", "b") }
                    ////                LazyColumn {
                    ////                    items(list.size) { index ->
                    ////                        Text(list[index])
                    ////                    }
                    ////                }
                     **/
                    itemsIndexed(uuids) { index, item ->
                        val visibility by remember {
                            mutableStateOf(
                                uuids.contains(item) && item == uuids[index]
                            )
                        }
                        AnimatedVisibility(
                            visible = visibility
                        ) {
                            TimerCard(
                                durationMillis = numberText.toLongOrNull()?.times(1000) ?: 0,
                                height = timerHeight,
                                onTick = { millisUntil -> onTick(millisUntil) },
                                onDeleteButtonClick = {
                                    uuids.remove(item)
                                    currentTimersCount(uuids.size)
                                    /** timersList.removeItem(currentItemId) **/
                                }
                            )
                        }
                    }
                }


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(padding),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    OutlinedButton(
                        enabled = uuids.size < maxTimersCount,
                        onClick = {
                            /** countOfTimers++ **/
                            UUID.randomUUID().toString().apply {
                                uuids.add(this)
                                currentTimersCount(uuids.size)
                            }
                        },
                        border = BorderStroke(buttonsBorderStrokeSize, Color.LightGray),
                        modifier = Modifier
                            .height(timerHeight.dp)
                            .wrapContentHeight()
                            .padding(padding)
                            .requiredWidth(Constraints().maxWidth.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.add_stopwatch_button_text),
                            fontSize = (timerHeight / 5).sp
                        )
                    }
                    OutlinedButton(
                        enabled = uuids.size > 0,
                        onClick = {
                            /** list.removeLast()  countOfTimers-- **/
                            uuids.removeLastOrNull()
                            currentTimersCount(uuids.size)
                        },
                        border = BorderStroke(buttonsBorderStrokeSize, Color.LightGray),
                        modifier = Modifier
                            .height(timerHeight.dp)
                            .wrapContentHeight()
                            .padding(padding)
                            .requiredWidth(Constraints().maxWidth.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.remove_stopwatch_button_text),
                            fontSize = (timerHeight / 5).sp
                        )
                    }
                }
            }
        }
    }
}

/**
 * durationMillis -> Init time in milliseconds for timer.
 *
 * modifier -> Define height and width
 * @see [Card]
 **/
@ExperimentalAnimationApi
@Composable
fun TimerCard(
    durationMillis: Long,
    height: Int,
    onTick: (millisUntil: Long) -> Unit,
    onDeleteButtonClick: () -> Unit
) {
    var previousTime by remember {
        mutableStateOf(durationMillis)
    }
    var currentTime by remember {
        mutableStateOf(previousTime)
    }
    var value by remember {
        mutableStateOf(1.00f)
    }
    var isTimerRunning by remember {
        mutableStateOf(false)
    }
    var isTimerFinished by remember {
        mutableStateOf(false)
    }
    var pointActive by remember {
        mutableStateOf(false)
    }

    val countDown = object : CountDownTimer(currentTime, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            if (!isTimerRunning) {
                cancel()
            }
            currentTime = millisUntilFinished
            value = currentTime.toFloat() / previousTime.toFloat()
            pointActive = true
            onTick(millisUntilFinished)
        }

        override fun onFinish() {
            value = 0f
            currentTime = 0
            pointActive = false
            isTimerFinished = true
            cancel()
        }
    }
    if (isTimerRunning) {
        countDown.start()
    }

    val padding: Int = height / 20
    Card(
        modifier = Modifier
            .background(
                color = if (isTimerFinished) Color.Red else Color.Unspecified,
                shape = RectangleShape
            )
            .fillMaxWidth()
            .requiredHeight(height.dp)
            .padding(padding.dp),
        shape = RoundedCornerShape((height / 10).dp),
        border = BorderStroke(2.dp, Color.Gray),
        elevation = (height / 10).dp
    ) {
        Row(
            modifier = Modifier.padding(padding.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (pointActive) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_fiber_manual_record_24),
                    contentDescription = "On indicator",
                    tint = Color.Red
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_fiber_manual_record_empty_24),
                    contentDescription = "empty"
                )
            }
            TimerClockFace(
                timeMillis = currentTime,
                height = height - padding,
                isTimeChangeEnable = !isTimerRunning,
                fontSize = (height / 5).sp,
                onTimeChanged = { timeMillis ->
                    currentTime = timeMillis
                    previousTime = timeMillis
                    value = 1f
                }
            )
            CircularProgressIndicatorFilled(
                progress = value,
                diameter = (height - height / 3).dp,
                color = Color.Red,
                backgroundColor = Color.Gray
            )
            IconButton(
                onClick = {
                    if (value == 0f) {
                        currentTime = previousTime
                        value = 1f
                        isTimerRunning = false
                        isTimerFinished = false
                    } else {
                        isTimerRunning = !isTimerRunning
                    }
                }
                /** onToggleStateButtonClicked() **/
            ) {
                Icon(
                    if (value == 0f) {
                        painterResource(id = R.drawable.ic_baseline_replay_24)
                    } else {
                        if (isTimerRunning) {
                            painterResource(id = R.drawable.ic_baseline_stop_circle_24)
                        } else {
                            painterResource(id = R.drawable.ic_twotone_play_circle_outline_24)
                        }
                    },
                    modifier = Modifier.size(48.dp),
                    contentDescription = "Start/Stop Button"
                )
            }
            IconButton(
                onClick = { onDeleteButtonClick() },
                /** onDeleteButtonClicked() **/
                //enabled = false
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_baseline_delete_24),
                    modifier = Modifier.size(48.dp),
                    contentDescription = "Delete Button"
                )
            }
        }
    }
}

@Composable
fun UpTimeButton(
    modifier: Modifier = Modifier.size(21.dp),
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Box(modifier = modifier) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.fillMaxSize(),
            enabled = enabled
        ) {
            Icon(
                painterResource(id = R.drawable.ic_baseline_arrow_circle_up_24),
                modifier = Modifier.fillMaxSize(),
                contentDescription = "Up time button"
            )
        }
    }
}

@Composable
fun DownTimeButton(
    modifier: Modifier = Modifier.size(21.dp),
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Box(modifier = modifier) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.fillMaxSize(),
            enabled = enabled
        ) {
            Icon(
                painterResource(id = R.drawable.ic_baseline_arrow_circle_down_24),
                modifier = Modifier.fillMaxSize(),
                contentDescription = "Down time button"
            )
        }
    }
}

/**
 * durationMillis -> Init time in milliseconds for timer.
 *
 * modifier -> Define height and width
 * @see [toTimeFormatString]
 **/
@Composable
fun TimerClockFace(
    timeMillis: Long,
    height: Int,
    isTimeChangeEnable: Boolean,
    fontSize: TextUnit = 35.sp,
    onTimeChanged: (timeMillis: Long) -> Unit
) {

    /**
     * plusHour/Minute/Second -> If true then +1. If false then -1. If null then no changes.
     * @see [TimerClockFace]
     **/
    fun changeTime(
        plusHour: Boolean? = null,
        plusMinute: Boolean? = null,
        plusSecond: Boolean? = null
    ) {
        var hours: Long = timeMillis.toHours()
        var minutes: Long = (timeMillis - hours * 3600 * 1000).toMinutes()
        var seconds: Long = (timeMillis - hours * 3600 * 1000 - minutes * 60_000).toSeconds()

        if (isTimeChangeEnable) {
            when (plusHour) {
                true -> {
                    if (hours + 1 > 99) hours = 0
                    else hours += 1
                }
                false -> {
                    if (hours - 1 < 0) hours = 99
                    else hours -= 1
                }
            }
            when (plusMinute) {
                true -> {
                    if (minutes + 1 > 59) {
                        minutes = 0
                    } else minutes += 1
                }
                false -> {
                    if (minutes - 1 < 0) minutes = 59
                    else minutes -= 1
                }
            }
            when (plusSecond) {
                true -> {
                    if (seconds + 1 > 59) seconds = 0
                    else seconds += 1
                }
                false -> {
                    if (seconds - 1 < 0) seconds = 59
                    else seconds -= 1
                }
            }
            onTimeChanged(toMillis(hours, minutes, seconds))
        }
    }

    Column(
        modifier = Modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.wrapContentHeight()
        ) {
            Spacer(modifier = Modifier.width((height / 50).dp))
            UpTimeButton(
                enabled = isTimeChangeEnable,
                modifier = Modifier.size((height / 4).dp),
                onClick = { changeTime(plusHour = true) }
            )
            Spacer(modifier = Modifier.width((height / 30).dp))
            UpTimeButton(
                enabled = isTimeChangeEnable,
                modifier = Modifier.size((height / 4).dp),
                onClick = { changeTime(plusMinute = true) }
            )
            Spacer(modifier = Modifier.width((height / 30).dp))
            UpTimeButton(
                enabled = isTimeChangeEnable,
                modifier = Modifier.size((height / 4).dp),
                onClick = { changeTime(plusSecond = true) }
            )
        }
        Row {
            Spacer(modifier = Modifier.width((height / 50).dp))
            Text(
                text = timeMillis.displayTime().dropLast(3),
                modifier = Modifier
                    .wrapContentHeight()
                    .wrapContentWidth(),
                fontSize = fontSize
            )
        }
        Row(
            modifier = Modifier.wrapContentHeight()
        ) {
            Spacer(modifier = Modifier.width((height / 50).dp))
            DownTimeButton(
                enabled = isTimeChangeEnable,
                modifier = Modifier.size((height / 4).dp),
                onClick = { changeTime(plusHour = false) }
            )
            Spacer(modifier = Modifier.width((height / 30).dp))
            DownTimeButton(
                enabled = isTimeChangeEnable,
                modifier = Modifier.size((height / 4).dp),
                onClick = { changeTime(plusMinute = false) }
            )
            Spacer(modifier = Modifier.width((height / 30).dp))
            DownTimeButton(
                enabled = isTimeChangeEnable,
                modifier = Modifier.size((height / 4).dp),
                onClick = { changeTime(plusSecond = false) }
            )
        }
    }
}

/**
 * progress -> From 0.0f to 1.0f
 *
 * diameter == Width == Height == Size
 *
 * color -> Color of progress indicator
 * @see [CircularProgressIndicator]
 **/
@Composable
fun CircularProgressIndicatorFilled(
    progress: Float,
    diameter: Dp,
    color: Color,
    backgroundColor: Color
) {
    CircularProgressIndicator(
        progress = progress,
        modifier = Modifier
            .background(backgroundColor, CircleShape)
            .requiredSize(diameter),
        color = color,
        strokeWidth = diameter / 2
    )
}