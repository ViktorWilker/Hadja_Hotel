package br.com.hadja_hotel.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import br.com.hadja_hotel.model.AcQuote
import br.com.hadja_hotel.model.Event
import br.com.hadja_hotel.model.HotelState
import br.com.hadja_hotel.model.OperationResult
import br.com.hadja_hotel.model.ReportData
import br.com.hadja_hotel.model.Reservation
import br.com.hadja_hotel.service.AuthService
import br.com.hadja_hotel.service.EventService
import br.com.hadja_hotel.service.GuestService
import br.com.hadja_hotel.service.ReportService
import br.com.hadja_hotel.service.ReservationService

class HotelViewModel : ViewModel() {
    val state = HotelState()
    fun validatePassword(input: String) = AuthService.validatePassword(input)
    fun recordAttempt() = AuthService.recordAttempt()
    fun isBlocked() = AuthService.isBlocked()
    fun startSession(name: String) = AuthService.startSession(name, state)

    var reservations by mutableStateOf(state.reservations.toList())
        private set

    fun confirmReservation(reservation: Reservation) {
        val guestExists = state.guests.any { it.name == reservation.guestName }
        if (!guestExists) {
            GuestService.register(reservation.guestName, state)
        }
        ReservationService.confirm(reservation, state)
        reservations = state.reservations.toList()
        guests = state.guests.sortedBy { it.name }
    }


    fun getRoomGrid() = ReservationService.buildRoomGrid(state)

    var guests by mutableStateOf(state.guests.sortedBy { it.name })
        private set

    fun registerGuest(name: String): OperationResult {
        val result = GuestService.register(name, state)
        if (result == OperationResult.SUCCESS) {
            guests = state.guests.sortedBy { it.name }
        }
        return result
    }

    fun removeGuest(name: String): OperationResult {
        val result = GuestService.remove(name, state)
        if (result == OperationResult.SUCCESS) {
            guests = state.guests.sortedBy { it.name }
        }
        return result
    }

    fun updateGuest(name: String, newName: String): OperationResult {
        val result = GuestService.update(name, newName, state)
        if (result == OperationResult.SUCCESS) {
            guests = state.guests.sortedBy { it.name }
        }
        return result
    }

    var events by mutableStateOf(state.events.toList())
        private set

    fun confirmEvent(event: Event) {
        EventService.confirm(event, state)
        events = state.events.toList()
    }

    var acQuotes by mutableStateOf(state.acQuotes.toList())
        private set

    fun addQuote(quote: AcQuote) {
        state.acQuotes.add(quote)
        acQuotes = state.acQuotes.toList()
    }

    fun clearQuotes() {
        state.acQuotes.clear()
        acQuotes = state.acQuotes.toList()
    }
    fun generateReport() = ReportService.generate(state)
}