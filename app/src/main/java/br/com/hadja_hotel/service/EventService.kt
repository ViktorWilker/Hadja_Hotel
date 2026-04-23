package br.com.hadja_hotel.service

import br.com.hadja_hotel.model.BuffetResult
import br.com.hadja_hotel.model.Event
import br.com.hadja_hotel.model.HotelState

object EventService {

    fun selectAuditorium(guests: Int): Pair<String, Int> {
        val chairExtra: Int
        if (guests in 221..350) {
            return Pair("Colorado", 0)
        }

        if (guests in 151..220) {
            chairExtra = guests - 150
            return Pair("Laranja", chairExtra)
        }

        return Pair("Laranja", 0)
    }

    fun isAvailable(day: String, start: Int, end: Int): Boolean {
        if (start < 7) return false
        if (end > 23) return false
        if((day == "Saturday" || day == "Sunday" ) && end > 15){return false}

        return true
    }

    fun calculateWaitersCost(waiters: Int, duration: Int): Double {
        val costHour = 10.50 * duration
        return waiters * costHour
    }

    fun calculateBuffet(guests: Int): BuffetResult {
        val coffeeLiters = 0.2 * guests
        val coffeeCost = coffeeLiters * 0.80

        val waterLiters = 0.5 * guests
        val waterCost = waterLiters * 0.40

        val snacksAmount = 7 * guests
        val snacksCost = snacksAmount * 0.34

        val totalCost = coffeeCost + snacksCost + waterCost

        return BuffetResult(coffeeLiters, waterLiters, snacksAmount, totalCost)
    }

    fun confirm(event: Event, state: HotelState){
        state.events.add(event)
    }

    fun calculateWaiters(guests: Int, duration: Int): Int {
        val base = Math.ceil(guests / 12.0).toInt()
        val extra = duration / 2
        return base + extra
    }

}