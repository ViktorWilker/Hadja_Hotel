package br.com.hadja_hotel.model

class HotelState {
    val rooms: Array<Boolean> = Array(20) { false }
    val reservations: MutableList<Reservation> = mutableListOf()
    val guests: MutableList<Guest> = mutableListOf()
    val events: MutableList<Event> = mutableListOf()
    val acQuotes: MutableList<AcQuote> = mutableListOf()
    var currentUser: String? = null
}

