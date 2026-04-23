package br.com.hadja_hotel.service

import br.com.hadja_hotel.model.HotelState

object AuthService {
    private var attempts = 0

    fun validatePassword(input: String): Boolean {
        recordAttempt()
        return input == "2678"
    }

    fun recordAttempt(): Int = ++attempts

    fun isBlocked(): Boolean = attempts >= 3

    fun startSession(name: String, state: HotelState) {
        state.currentUser = name
    }
}