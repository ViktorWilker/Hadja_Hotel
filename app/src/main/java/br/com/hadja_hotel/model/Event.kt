package br.com.hadja_hotel.model

data class Event(
    val company: String,
    val auditorium: String,
    val weekday: String,
    val startHour: Int,
    val duration: Int,
    val guests: Int,
    val waiters: Int,
    val waiterCost: Double,
    val buffetCost: Double,
    val totalCost: Double
)