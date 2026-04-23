package br.com.hadja_hotel.service

import br.com.hadja_hotel.model.AcQuote

object AirConditioningService {
    fun calculateQuote(input: AcQuote): AcQuote {
        val bruto = input.pricePerUnit * input.quantity
        var discount = 0.0

        if (input.quantity >= input.minForDiscount) {
            discount = bruto * (input.discountPct / 100.0)
        }

        val total = bruto - discount + input.travelFee

        return input.copy(finalTotal = total)
    }

    fun findCheapest(quotes: List<AcQuote>): AcQuote {
        return quotes.minByOrNull { it.finalTotal }!!
    }

    fun findMostExpensive(quotes: List<AcQuote>): AcQuote {
        return quotes.maxByOrNull { it.finalTotal }!!
    }

    fun priceDiffPercentage(min: Double, max: Double): Double {
        return ((max - min) / min) * 100
    }
}