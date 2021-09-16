package com.mycompany.wind.model.domain


data class Weather(
    val fact: Fact
) {
    data class Fact(
        val temp: Int
    )
}
