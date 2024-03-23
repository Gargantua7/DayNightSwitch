package com.gargantua7.android.daynightswitch

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun DayNightSwitch(
    height: Dp = 24.dp,
    initValue: Boolean = true
) {

    var switchState by remember {
        mutableStateOf(initValue)
    }

    val skyColorStart by animateColorAsState(
        targetValue = if (switchState) Color(0xFF000C40) else Color(0xFFABDCFF),
        animationSpec = tween(500),
        label = "sky_color_start"
    )

    val skyColorEnd by animateColorAsState(
        targetValue = if (switchState) Color(0xFF607D8B) else Color(0xFF0396FF),
        animationSpec = tween(500),
        label = "sky_color_end"
    )

    val gradient = Brush.linearGradient(
        listOf(skyColorStart, skyColorEnd),
        start = Offset.Zero, end = Offset.Infinite
    )

    Box(
        modifier = Modifier
            .width(height * 2.5f)
            .height(height)
            .background(gradient, RoundedCornerShape(height / 2))
            .clip(RoundedCornerShape(height / 2))
            .clickable(indication = null, interactionSource = remember { MutableInteractionSource() }) { switchState = !switchState }
        ) {

        val skyDecorationOffset by animateDpAsState(
            targetValue = if (switchState) height else -height,
            animationSpec = tween(500),
            label = "sky_decoration_offset"
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(height * 3)
                .offset(y = skyDecorationOffset)
        ) {

            val bigSize = height / 6
            val midSize = height / 12
            val smallSize = height / 24

            Star(bigSize, height / 2 - bigSize / 2, bigSize / 2)
            Star(midSize, height / 2 - bigSize * 2, height / 4)
            Star(midSize, height / 2 - midSize, height / 2 - bigSize / 2)
            Star(bigSize, height * 1.04f, height / 2 - bigSize / 2)
            Star(midSize, height * 9.96f, height / 2 + bigSize / 2)

            Star(midSize, height - midSize * 2, height / 4)
            Star(midSize, height * 0.75f, height / 4 * 1.5f)
            Star(midSize, height * 9.96f, height * 0.625f)
            Star(smallSize, height / 2 - bigSize / 2, height - bigSize)

            val heightPx = with(LocalDensity.current) { height.toPx() }

            Canvas(
                modifier = Modifier
                    .width(height * 2.5f)
                    .height(height * 1.25f)
                    .alpha(0.5f)
                    .align(Alignment.BottomCenter)
            ) {
                Cloud(heightPx)
            }

            Canvas(
                modifier = Modifier
                    .width(height * 2.5f)
                    .height(height)
                    .align(Alignment.BottomStart)
            ) {
                Cloud(heightPx)
            }
        }

        val offset by animateDpAsState(
            targetValue = if (switchState) height * 1.5f else 0.dp,
            animationSpec = spring(stiffness = 200f, dampingRatio = 0.7f),
            label = "offset"
        )

        val background by animateColorAsState(
            targetValue = if (switchState) Color(0xFFB2BBB6) else Color(0xFFFFEE6F),
            animationSpec = tween(500),
            label = "button_background"
        )

        val holeAlpha by animateFloatAsState(
            targetValue = if (switchState) 1f else 0f,
            animationSpec = tween(500),
            label = "hole_alpha"
        )

        Canvas(
            modifier = Modifier
                .padding(height / 12)
                .size(height - height / 6)
                .offset(x = offset)
        ) {
            drawCircle(
                color = Color.White,
                radius = size.minDimension / 2 * 1.5f * 1.4f * 1.3f,
                center = center,
                alpha = 0.1f
            )

            drawCircle(
                color = Color.White,
                radius = size.minDimension / 2 * 1.5f * 1.4f,
                center = center,
                alpha = 0.1f
            )

            drawCircle(
                color = Color.White,
                radius = size.minDimension / 2 * 1.5f,
                center = center,
                alpha = 0.1f
            )

            drawCircle(
                color = background,
                radius = size.minDimension / 2,
                center = center
            )

            drawCircle(
                color = Color(0xFF686A67),
                radius = size.minDimension / 13,
                center = size.center - Offset(x = 0f, y = size.height / 4),
                alpha = holeAlpha
            )

            drawCircle(
                color = Color(0xFF686A67),
                radius = size.minDimension / 5,
                center = size.center + Offset(x = -size.width / 6, y = size.height / 10),
                alpha = holeAlpha
            )

            drawCircle(
                color = Color(0xFF686A67),
                radius = size.minDimension / 10,
                center = size.center + Offset(x = size.width / 4.8f, y = size.height / 5),
                alpha = holeAlpha
            )
        }
    }
}




@Composable
@Preview
private fun Preview() {

    Column(
        Modifier
            .background(Color.Black)
            .padding(12.dp)
    ) {
        DayNightSwitch(height = 72.dp, initValue = false)

        Spacer(modifier = Modifier.height(24.dp))

        DayNightSwitch(height = 72.dp)
    }

}

@Composable
@Preview
private fun Star(
    size: Dp = 4.dp,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp
) {

    Canvas(
        Modifier
            .size(size)
            .offset(offsetX, offsetY)
            .rotate(45f)
    ) {
        val path = Path().apply {
            val radius = this@Canvas.size.minDimension / 2
            val center = Offset(this@Canvas.size.width / 2, this@Canvas.size.height / 2)
            val angle = 90f
            val startAngle = -45f
            for (i in 0 until 4) {
                val start = Offset(
                    center.x + radius * cos(Math.toRadians((startAngle + angle * i).toDouble()).toFloat()),
                    center.y + radius * sin(Math.toRadians((startAngle + angle * i).toDouble()).toFloat())
                )
                val end = Offset(
                    center.x + radius * cos(Math.toRadians((startAngle + angle * (i + 1)).toDouble()).toFloat()),
                    center.y + radius * sin(Math.toRadians((startAngle + angle * (i + 1)).toDouble()).toFloat())
                )
                val control = Offset(
                    center.x + radius / 15f * cos(Math.toRadians((startAngle + angle * i + angle / 2).toDouble()).toFloat()),
                    center.y + radius / 15f * sin(Math.toRadians((startAngle + angle * i + angle / 2).toDouble()).toFloat())
                )
                if (i == 0) {
                    moveTo(start.x, start.y)
                }
                quadraticBezierTo(control.x, control.y, end.x, end.y)
            }
            close()
        }

        drawPath(
            path = path,
            color = Color.White
        )
    }

}

private fun DrawScope.Cloud(height: Float) {
    val width = height * 2.5f
    drawCircle(
        color = Color.White,
        radius = size.minDimension / 4,
        center = center + Offset(x = width / 3, y = height / 3)
    )

    drawCircle(
        color = Color.White,
        radius = size.minDimension / 3,
        center = center + Offset(x = width / 2, y = height / 4)
    )

    drawCircle(
        color = Color.White,
        radius = size.minDimension / 2,
        center = center + Offset(x = width / 1.7f, y = 0f)
    )

    drawCircle(
        color = Color.White,
        radius = size.minDimension / 2,
        center = center + Offset(x = width * 0.1f, y = height * 0.8f)
    )

    drawCircle(
        color = Color.White,
        radius = size.minDimension / 2,
        center = center + Offset(x = width * 0.3f, y = height * 0.75f)
    )

    drawCircle(
        color = Color.White,
        radius = size.minDimension / 2,
        center = center + Offset(x = -width * 0.1f, y = height * 0.85f)
    )
}