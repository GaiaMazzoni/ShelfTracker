package com.example.shelftracker.ui.composables

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate


data class ChartData(val value: Float, val label: String, val color: Color)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PieChart(
    data: MutableList<ChartData>,
    modifier: Modifier = Modifier
) {
    val totalValue = data.fold(0f) { sum, pieData -> sum + pieData.value }
    var startAngle = -90f

    Canvas(modifier = modifier) {
        data.forEach { pieData ->
            val sweepAngle = (pieData.value / totalValue) * 360f
            drawPieSlice(
                color = pieData.color,
                startAngle = startAngle,
                sweepAngle = sweepAngle
            )
            startAngle += sweepAngle
        }
    }
}

fun DrawScope.drawPieSlice(color: Color, startAngle: Float, sweepAngle: Float) {
    rotate(degrees = startAngle) {
        drawArc(
            color = color,
            startAngle = 0f,
            sweepAngle = sweepAngle,
            useCenter = true
        )
    }
}

@Composable
fun BarChart(
    data: MutableList<ChartData>,
    modifier: Modifier = Modifier
){
    Canvas(modifier = modifier) {
        val maxValue = data.maxOf { it.value }
        val barWidth = size.width / (data.size * 2)  // Calculate the width of each bar
        var xOffset = 0f

        data.forEach { barData ->
            val barHeight = (barData.value / maxValue) * size.height
            drawRect(
                color = barData.color,
                topLeft = Offset(xOffset, size.height - barHeight),
                size = androidx.compose.ui.geometry.Size(barWidth, barHeight)
            )
            xOffset += barWidth * 2  // Move to the next position
        }
    }
}

