package br.com.hadja_hotel.service

import br.com.hadja_hotel.model.FuelResult
import kotlin.collections.minByOrNull

object FuelService {

    fun isEthanolWorthIt(ethanol: Double, gasoline: Double): Boolean {
        return ethanol <= gasoline * 0.70
    }

    fun getBestOption(station: FuelResult): FuelResult {
        return if (isEthanolWorthIt(station.ethanolPrice, station.gasolinePrice)) {
            station.copy(bestFuel = "Ethanol", totalCost = station.ethanolPrice * 42)
        } else {
            station.copy(bestFuel = "Gasolina", totalCost = station.gasolinePrice * 42)
        }
    }

    fun getCheapest(results: List<FuelResult>): FuelResult {
        return results.minByOrNull { it.totalCost }!!
    }
}