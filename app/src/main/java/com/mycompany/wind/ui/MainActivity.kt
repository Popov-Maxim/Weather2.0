package com.mycompany.wind.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.mycompany.wind.App
import com.mycompany.wind.ui.main.CityState
import com.mycompany.wind.ui.main.MainViewModel
import com.mycompany.wind.ui.main.WeatherState
import com.mycompany.wind.ui.theme.MainScreen
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var factory: MainViewModel.Factory

    private val viewModel: MainViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as App).appComponent.inject(this)
        setContent {
            MainScreen(viewModel = viewModel)
//            DefaultPreview()
        }
        lifecycleScope.launch {

            viewModel.cityState.collect { state ->
                when (state) {
                    CityState.Empty -> Log.d("myLogs", "${state::class.java}")
                    is CityState.Error -> Log.d("myLogs", "${state.exception}")
                    is CityState.Success -> Log.d("myLogs", "${state.cities}")
                }
            }
        }
        lifecycleScope.launch {

            viewModel.weatherState.collect { state ->
                when (state) {
                    WeatherState.Empty -> Log.d("myLogs", "${state::class.java}")
                    is WeatherState.Error -> Log.d("myLogs", "${state.exception}")
                    is WeatherState.Success -> Log.d("myLogs", "${state.city.weather}")
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
}

