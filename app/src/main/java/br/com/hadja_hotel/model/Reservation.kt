package br.com.hadja_hotel.model

data class Reservation (
    val guestName: String,
    val roomNumber: Int,
    val roomType: RoomType,
    val dailyRate: Double,
    val nights: Int,
    val subtotal: Double,
    val serviceFee: Double,
    val total: Double
)