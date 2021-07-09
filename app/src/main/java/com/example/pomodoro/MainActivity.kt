package com.example.pomodoro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pomodoro.ui.theme.PomodoroTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PomodoroTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.padding(5.dp),
                    color = MaterialTheme.colors.background
                ) {
                    MainActivityContent()
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    PomodoroTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.padding(5.dp),
            color = MaterialTheme.colors.background
        ) {
            MainActivityContent()
        }
    }
}

@Composable
fun MainActivityContent() {
    var count by remember { mutableStateOf(5) }
    Column(verticalArrangement = Arrangement.SpaceEvenly) {
        LazyColumn(state = rememberLazyListState(), modifier = Modifier.height(600.dp)) {
            items(count) {
                TimerCard()
            }
        }
        Spacer(modifier = Modifier.height(50.dp))
        Button(
            onClick = { count++ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = stringResource(R.string.add_stopwatch_button_text))
        }
        Spacer(modifier = Modifier.height(5.dp))
        Button(
            onClick = { count-- },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "REMOVE STOPWATCH")
        }
    }
}

@Composable
fun TimerCard(height: Dp = 80.dp, padding: Dp = 3.dp, fontSize: TextUnit = 35.sp) {
    val size = if (height < 25.dp) 25.dp else height
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(size)
            .border(2.dp, Color.LightGray, RectangleShape)
            .padding(padding)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Row {
                Text(text = "00:", fontSize = fontSize)
                Text(text = "00:", fontSize = fontSize)
                Text(text = "00", fontSize = fontSize)
            }
            //Spacer(Modifier.requiredWidth(size / 2))
            TimerCircularProgressBar(height - padding)
            //Spacer(Modifier.requiredWidth(size / 2))
            IconButton(
                onClick = { },
                modifier = Modifier.size(height / 2)
            ) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = "delete button",
                    tint = Color.Red
                )
            }
        }
    }
}

@Composable
fun TimerCircularProgressBar(
    height: Dp,
    durationSeconds: Int = 5
) {
    val startPosition = 1.0f
    val endPosition = 0.0f
    var isActive by remember { mutableStateOf(false) }
    var currentTarget by remember { mutableStateOf(startPosition) }
    var millisLeft by remember { mutableStateOf(durationSeconds * 1000) }
    val animation = animateFloatAsState(
        targetValue = currentTarget,
        animationSpec = tween(
            ((1 - currentTarget) * millisLeft).toInt(),
            easing = LinearEasing
        )
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .wrapContentWidth()
            .height(height)
    ) {
        Box(
            modifier = Modifier.fillMaxHeight(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(
                progress = animation.value,
                modifier = Modifier
                    .background(Color.LightGray, CircleShape)
                    .size(height - 5.dp),
                color = Color.Red,
                strokeWidth = (height - 5.dp) / 2
            )
        }
        OutlinedButton(
            onClick = {
                isActive = !isActive
                currentTarget = if (isActive) endPosition else animation.value
                millisLeft = (animation.value * 100).toInt() * durationSeconds * 10
                if (animation.value == endPosition) {
                    millisLeft = durationSeconds * 1000
                    currentTarget = startPosition
                }
            },
            modifier = Modifier.height(height / 2)
        ) {
            if (isActive) {
                if (animation.value == endPosition) {
                    Text("RESTART")
                } else {
                    Text("STOP")
                }
            } else {
                Text("START")
            }
        }
    }
}

//@Composable
//fun PomodoroCircularProgressBar(
//    radius: Dp = 8.dp,
//    color: Color = Color.Red,
//    animDuration: Int
//) {
//    var periodMs by remember {
//        mutableStateOf(10L)
//    }
//    var currentMs by remember {
//        mutableStateOf(10L)
//    }
//    if (periodMs == 0L || currentMs == 0L) return
//
//    var animPlayedStatus by remember {
//        mutableStateOf(false)
//    }
//
//    val fill = 0
//    val startAngel: Float = (((currentMs % periodMs).toFloat() / periodMs) * 360)
//
//    LaunchedEffect(key1 = true) {
//        animPlayedStatus = true
//    }
//
//    Box(
//        contentAlignment = Alignment.Center,
//        modifier = Modifier.size(radius * 2f)
//    ) {
//        Canvas(modifier = Modifier.size(radius * 2f)) {
//            drawArc(
//                color,
//                startAngel,
//                -90f,
//                true,
//                style = Stroke(15.toDp().toPx(), cap = StrokeCap.Round),
//                size = size
//            )
//        }
//    }
//}


//if (periodMs == 0L || currentMs == 0L) return
//        val startAngel = (((currentMs % periodMs).toFloat() / periodMs) * 360)
//

//override fun onDraw(canvas: Canvas) {
//    super.onDraw(canvas)
//    if (periodMs == 0L || currentMs == 0L) return
//    val startAngel = (((currentMs % periodMs).toFloat() / periodMs) * 360)
//
//    canvas.drawArc(
//        0f,
//        0f,
//        width.toFloat(),
//        height.toFloat(),
//        -90f,
//        startAngel,
//        true,
//        paint
//    )
//}