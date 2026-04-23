package br.com.hadja_hotel.model

data class FuelResult(
    val stationName: String,
    val ethanolPrice: Double,
    val gasolinePrice: Double,
    val bestFuel: String = "",
    val totalCost: Double = 0.0
)

