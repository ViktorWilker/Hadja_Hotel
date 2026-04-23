package br.com.hadja_hotel.model
import java.time.LocalDateTime
data class Guest (
    val name: String,
    val registeredAt: LocalDateTime
)