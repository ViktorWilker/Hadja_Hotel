package br.com.hadja_hotel.until

import java.text.NumberFormat
import java.util.Locale

object Formatter {
    fun currency(value: Double): String {
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        return currencyFormat.format(value)
    }

    fun hour(hr: Int): String {
        val time = "$hr" + "h"
        return time
    }

    fun percentage(value: Double): String {
        val formatted = "%.1f%%".format(value)
        return formatted
    }
}