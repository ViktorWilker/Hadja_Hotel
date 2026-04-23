package br.com.hadja_hotel.service

import br.com.hadja_hotel.model.Guest
import br.com.hadja_hotel.model.HotelState
import br.com.hadja_hotel.model.OperationResult
import java.time.LocalDateTime

object GuestService {

    fun register(name: String, state: HotelState): OperationResult {
        if (state.guests.size > 14) {
            return OperationResult.LIMIT_REACHED
        }
        for (x in state.guests) {
            if (x.name == name) {
                return OperationResult.DUPLICATE
            }
        }
        state.guests.add(Guest(name, LocalDateTime.now()))
        return OperationResult.SUCCESS
    }

    fun findExact(name: String, state: HotelState): Guest? {
        for (x in state.guests) {
            if (x.name == name) {
                return x
            }
        }
        return null
    }

    fun findByPrefix(prefix: String, state: HotelState): List<Guest> {
        val list = mutableListOf<Guest>()
        for (x in state.guests) {
            if (x.name.startsWith(prefix)) {
                list.add(x)
            }
        }
        return list
    }

    fun listSorted(state: HotelState): List<Guest> {
        return state.guests.sortedBy { it.name }
    }

    fun update(name: String, newName: String, state: HotelState): OperationResult {
        val guest = state.guests.find { it.name == newName }
        if (guest == null) return OperationResult.DUPLICATE

        val index = state.guests.indexOf(guest)
        state.guests[index] = guest.copy(name = newName)

        return OperationResult.SUCCESS
    }

    fun remove(name: String, state: HotelState): OperationResult {
        val guest = state.guests.find { it.name == name }

        if (guest == null) return OperationResult.NOT_FOUND

        state.guests.remove(guest)

        return OperationResult.SUCCESS
    }
}
