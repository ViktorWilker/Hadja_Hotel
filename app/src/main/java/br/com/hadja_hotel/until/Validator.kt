package br.com.hadja_hotel.until

object Validator {
    fun isPositive(value: Int): Boolean {
        return value > 0
    }

    fun isInRange(value: Int, min: Int, max: Int): Boolean {
        return value in min..max
    }

    fun isNotBlank(value: String): Boolean {
        return value.isNotBlank()
    }

    fun isValid(input: Int, options: IntArray): Boolean {
        return input in options.map(Int::toInt)
    }

    fun isValidWeekDay(day: String): Boolean {
        val weekDays = arrayOf("monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday")
        return day in weekDays
    }
}