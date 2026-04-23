package br.com.hadja_hotel.model

data class ReportData(
    val totalReservations: Int,
    val occupancyRate: Double,
    val totalGuests: Int,
    val totalEvents: Int,
    val hospitalityRevenue: Double,
    val eventsRevenue: Double,
    val grandTotal: Double
)