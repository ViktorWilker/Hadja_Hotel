package br.com.hadja_hotel.service

import br.com.hadja_hotel.model.HotelState
import br.com.hadja_hotel.model.ReportData

object ReportService {
    fun generate(state: HotelState): ReportData {
        val totalReservation = state.reservations.size
        val occupancyRate = state.rooms.count { it } / 20.0
        val totalGuests = state.guests.size
        val totalEvents = state.events.size
        val hospitalityRevenue = state.reservations.sumOf { it.total }
        val eventsRevenue = state.events.sumOf { it.totalCost }
        val grandTotal = hospitalityRevenue + eventsRevenue

        return ReportData(
            totalReservations = totalReservation,
            occupancyRate = occupancyRate,
            totalGuests = totalGuests,
            totalEvents = totalEvents,
            hospitalityRevenue = hospitalityRevenue,
            eventsRevenue = eventsRevenue,
            grandTotal = grandTotal
        )
    }
}