package com.sunassistant.view

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import colorResource
import com.sunassistant.coderesources.CommonColors
import com.sunassistant.coderesources.CommonDimen
import com.sunassistant.coderesources.CommonStrings_User
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.sunassistant.util.TimeUtils
import com.sunassistant.util.UVAssistant
import com.sunassistant.util.UVAssistant.colorForValueString

@Composable
fun CircularCounter(
    targetValue: Float?,
    lastFetchedAtTime: Long?,
    currentWeather: Int?,
    isDay: Boolean?
) {
    val animatedValue = remember { Animatable(0f) }
    val color = colorForValue(targetValue ?: 0f)

    if (targetValue != null) {
        LaunchedEffect(targetValue) {
            animatedValue.animateTo(
                targetValue,
                animationSpec = tween(durationMillis = 2000)
            )
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(CommonDimen.default_padding.dp,
                CommonDimen.padding_32.dp,
                CommonDimen.default_padding.dp,
                CommonDimen.default_padding.dp)
    ) {
        DrawCircularCounter(targetValue, color)
        CounterTextDisplay(
            targetValue,
            animatedValue.value,
            color,
            lastFetchedAtTime,
            currentWeather,
            isDay ?: true
        )
    }
}

@Composable
private fun DrawCircularCounter(targetValue: Float?, color: Color) {
    Canvas(modifier = Modifier.size(CommonDimen.circle_counter_size.dp)) {
        val center = center
        val radius = size.minDimension / 2
        val angleInRadians = Math.toRadians((360 / 12 * (targetValue ?: 0f)).toDouble())

        val endX = center.x + radius * kotlin.math.cos(angleInRadians).toFloat()
        val endY = center.y + radius * kotlin.math.sin(angleInRadians).toFloat()

        // Background circle
        drawCircle(
            color = color.copy(alpha = 0.05f),
            radius = radius
        )

        // Stroked circle
        drawCircle(
            color = color.copy(alpha = 0.3f),
            radius = radius,
            style = Stroke(width = 30f)
        )

        if (targetValue != null) {
            drawCircle(
                color = color,
                center = Offset(endX, endY),
                radius = 15f
            )
        }
    }
}

@Composable
private fun CounterTextDisplay(
    targetValue: Float?,
    animatedValue: Float,
    color: Color,
    lastFetchedAtTime: Long?,
    currentWeather: Int?,
    isDay: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxSize(), // This will make sure the Box fills the entire screen
        // Optional: Adjust padding as needed
        contentAlignment = Alignment.Center // This will center the content both horizontally and vertically
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxHeight()
        ) {
            Divider(
                Modifier
                    .width(CommonDimen.padding_56.dp)
                    .padding(
                        start = 0.dp,
                        top = 0.dp,
                        end = 0.dp,
                        bottom = 8.dp
                    )
            )
            Text(
                UVAssistant.getWeatherEmoji(weatherCode = currentWeather, nightEmojis = !isDay),
                fontSize = TextUnit(CommonDimen.text_size_24.toFloat(), TextUnitType.Sp),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 0.dp,
                        top = 0.dp,
                        end = 0.dp,
                        bottom = 2.dp
                    ),
                style = TextStyle(
                    fontFamily = SunAssistantThemeElements.poppinsFontFamily,
                    lineHeight = TextUnit.Unspecified
                )
            )
            Text(
                text = if (targetValue != null) "%.2f".format(animatedValue) else "-,--",
                color = color,
                fontSize = TextUnit(26.toFloat(), TextUnitType.Sp),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = TextStyle(
                    fontFamily = SunAssistantThemeElements.poppinsFontFamily,
                    lineHeight = TextUnit.Unspecified
                )
            )
            Text(
                text = CommonStrings_User.uv_index_subcircular,
                color = color,
                fontSize = TextUnit(CommonDimen.text_size_10.toFloat(), TextUnitType.Sp),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = TextStyle(
                    fontFamily = SunAssistantThemeElements.poppinsFontFamily,
                    lineHeight = TextUnit.Unspecified
                )
            )
            Text(
                text = TimeUtils.formatFetchedAtTime(lastFetchedAtTime),
                color = color,
                fontSize = TextUnit(CommonDimen.text_size_10.toFloat(), TextUnitType.Sp),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = TextStyle(
                    fontFamily = SunAssistantThemeElements.poppinsFontFamily,
                    lineHeight = TextUnit.Unspecified
                )
            )
            Divider(
                Modifier
                    .width(CommonDimen.padding_56.dp)
                    .padding(
                        start = 0.dp,
                        top = 14.dp,
                        end = 0.dp,
                        bottom = 0.dp
                    )
            )
        }
    }
}

fun colorForValue(value: Float?): Color {
    if (value == null) return Color.Gray
    return colorResource(colorForValueString(value))
}