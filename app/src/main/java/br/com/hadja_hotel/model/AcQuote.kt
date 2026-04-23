package br.com.hadja_hotel.model

data class AcQuote(
    val company: String,
    val pricePerUnit: Double,
    val quantity: Int,
    val discountPct: Double,
    val minForDiscount: Int,
    val travelFee: Double,
    val finalTotal: Double = 0.0
)

