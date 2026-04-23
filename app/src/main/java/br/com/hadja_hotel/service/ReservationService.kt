package br.com.hadja_hotel.service

import br.com.hadja_hotel.model.HotelState
import br.com.hadja_hotel.model.Reservation
import br.com.hadja_hotel.model.RoomType

object ReservationService {

    fun validateDailyRate(value: Double): Boolean {
        return value > 0
    }

    fun validateNigths(n: Int): Boolean {
        return n in 1..30
    }

    fun getRoomTypeFactor(roomType: RoomType): Double {
        return when (roomType) {
            RoomType.STANDARD -> 1.0
            RoomType.EXECUTIVE -> 1.35
            RoomType.LUXURY -> 1.65
        }
    }

    fun calculateTotals(dailyRate: Double, nights: Int, roomType: RoomType): Triple<Double, Double, Double> {
        val subtotal = dailyRate * nights * getRoomTypeFactor(roomType)
        val fee = subtotal * 0.10
        val total = subtotal + fee
        return Triple(subtotal, fee, total)

    }

    fun isRoomOccupied(number: Int, state: HotelState): Boolean {
        return state.rooms[number - 1]
    }

    fun getFreeRooms(state: HotelState): List<Int> {
        val freeRooms = mutableListOf<Boolean>()
        val freeRoomsIndex = mutableListOf<Int>()
        for (x in state.rooms.indices) {
            if (!state.rooms[x]) {
                freeRooms.add(true)
                freeRoomsIndex.add(x + 1)
            }
        }
        return freeRoomsIndex
    }

    fun confirm(reservation: Reservation, state: HotelState) {
        state.rooms[reservation.roomNumber - 1] = true
        state.reservations.add(reservation)
    }


    fun buildRoomGrid(state: HotelState): Array<Array<String>> {
        val grid = Array(4) { Array(5) { "" } }

        for (column in 0..3) {
            for (row in 0..4) {
                val x = column * 5 + row
                if (state.rooms[x]) {
                    grid[column][row] = "O"
                } else {
                    grid[column][row] = "L"
                }
            }
        }
        return grid
    }


}