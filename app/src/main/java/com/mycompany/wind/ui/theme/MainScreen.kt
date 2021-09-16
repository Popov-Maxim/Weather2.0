package com.mycompany.wind.ui.theme

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mycompany.wind.model.domain.City
import com.mycompany.wind.ui.main.CityState
import com.mycompany.wind.ui.main.MainViewModel
import com.mycompany.wind.ui.main.WeatherState
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


@Composable
fun MainScreen(viewModel: MainViewModel) = ScreenTheme {
    val cityState by viewModel.cityState.collectAsState()
    val weatherState by viewModel.weatherState.collectAsState()

    val city = when (cityState) {
        is CityState.Success -> {
            (cityState as CityState.Success).cities
        }
        else -> emptyList()
    }

    var selectedCity: City by remember {
        mutableStateOf(City("", 0.0, 0.0))
    }


    if (weatherState is WeatherState.Success) {
        if (selectedCity == (weatherState as WeatherState.Success).city) {
            selectedCity.weather = (weatherState as WeatherState.Success).city.weather
        }
    }

    Box(Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0x7F, 0xBA, 0xE4),
                        Color(0x7F, 0xBA, 0xE4, 0x0)
                    )
                )
            )

        }
        var x0 by remember { mutableStateOf(0F) }
        var y0 by remember { mutableStateOf(0F) }
        val visible = when (weatherState) {
            WeatherState.Wait -> true
            else -> false
        }
        Canvas(Modifier.fillMaxSize()) {
            x0 = size.width / 2.66F
            y0 = size.height / 2.11F
        }

        RotateAnimate(
            visible,
            millis = 1500,
            pivot = Offset(x0, y0)
        ) {
            drawSun(
                Color(0xFF, 0xFF, 0xFF, 0xB3),
                size.width / 2.66F,
                size.height / 2.11F,
                size.width / 8,
                size.width / 20
            )
        }

        Column(Modifier.align(Alignment.TopCenter)) {
            CityNameMenu(cities = city, ItemMenu = { it, modifier -> ItemMenu(it, modifier) }) {
                selectedCity = it
            }
        }
        Column(modifier = Modifier.align(Alignment.BottomEnd)) {
            val modifier = Modifier
                .padding(end = 30.dp)
                .align(Alignment.End)

            Text(
                "${selectedCity.weather?.fact?.temp ?: "-"}°",
                modifier,
                fontSize = 96.sp
            )
            Text(
                selectedCity.name,
                modifier.padding(bottom = 100.dp)
            )

        }
        Column(Modifier.align(Alignment.BottomCenter)) {
            Button(onClick = {
                city.let {
                    if (it.isNotEmpty())
                        viewModel.getWeatherFromCity(selectedCity.name)
                }
            }, Modifier.size(200.dp, 70.dp)) {
                Text(text = "getWeather")
            }
            Spacer(modifier = Modifier.size(30.dp))


        }

    }

}

@Composable
fun <T> CityNameMenu(
    cities: List<T>,
    ItemMenu: @Composable (T, Modifier) -> Unit,
    onChangeItem: (T) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    var selectedIndex by remember { mutableStateOf(0) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(23.dp)
            .clickable { expanded = true }
            .background(Color.White, CircleShape)
    ) {
        Box(
            modifier = Modifier
                .padding(11.dp)
                .background(Color(0x5C, 0x5C, 0x5C, 0x69), CircleShape)
                .size(34.dp)
        )

        if (cities.isNotEmpty()) {
            ItemMenu(cities[selectedIndex], Modifier.padding(start = 56.dp))
            onChangeItem(cities[selectedIndex])

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }) {
                cities.forEachIndexed { index, item ->
                    DropdownMenuItem(onClick = {
                        selectedIndex = index
                        expanded = false
                        onChangeItem(item)
                    }) {
                        ItemMenu(item, Modifier)
                    }
                }
            }
        }
    }
}

@Composable
fun ItemMenu(item: String) {
    Text(item)
}

@Composable
fun ItemMenu(item: City, modifier: Modifier = Modifier) {
    Text(
        item.name, modifier
    )
}


fun DrawScope.drawSun(color: Color, x0: Float, y0: Float, rSun: Float, wLine: Float) {

    drawCircle(
        color = color,
        radius = rSun,
        style = Stroke(width = wLine),
        center = Offset(x0, y0)
    )

    var x1: Float
    var y1: Float
    var x2: Float
    var y2: Float
    val range = 0..8
    val rLine = rSun * 1.8
    for (i in range) {
        x1 = (x0 - rLine
                + (1 - cos(2 * PI / range.last * i)) * rLine).toFloat()
        x2 =
            (x0 - size.maxDimension + (1 - cos(2 * PI / range.last * i)) * size.maxDimension).toFloat()

        y1 = (y0 - sin(2 * PI / range.last * i) * rLine).toFloat()
        y2 = (y0 - sin(2 * PI / range.last * i) * size.maxDimension).toFloat()
        drawLine(
            color = color,
            start = Offset(x1, y1),
            end = Offset(x2, y2),
            strokeWidth = wLine,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun RotateAnimate(
    visible: Boolean,
    millis: Int,
    pivot: Offset,
    block: DrawScope.() -> Unit
) {

    val transition = rememberInfiniteTransition()

    val degree by transition.animateValue(
        0,
        360,
        Int.VectorConverter,
        infiniteRepeatable(
            animation = tween(
                durationMillis = millis,
                easing = LinearEasing
            )
        )
    )
    Canvas(Modifier.fillMaxSize()) {
        if (visible) {
            rotate(
                degrees = degree.toFloat(),
                pivot = pivot,
                block = block
            )
        } else {
            rotate(
                degrees = 0F,
                pivot = pivot,
                block = block
            )
        }
    }
}