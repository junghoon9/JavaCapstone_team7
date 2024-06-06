package kr.mjc.junghoon.javacapstonedesign.main

import java.text.NumberFormat
import java.util.*
import kotlin.math.pow
import kotlin.random.Random

class Logic(val itemType: String?,
            val jobType: String?,
            val itemLevel: Int?) {

    var stat = when (jobType) {
        "전사" -> 0
        "궁수" -> 0
        "마법사" -> 0
        else -> 0 // 도적
    }

    var statName = when (jobType) {
        "전사" -> "STR"
        "궁수" -> "DEX"
        "마법사" -> "INT"
        else -> "LUK" // 도적
    }

    var enhancingLevel = 0
    var power = when (jobType) {
        "마법사" -> 0
        else -> 0 // 전사, 궁수, 도적
    }
    var def = 0 // 방어력

    var powerOrDefenceName = when (itemType) {
        "무기" -> when (jobType) {
            "마법사" -> "마력"
            else -> "공격력" // 전사, 궁수, 도적
        }
        else -> "방어력" // 방어구
    }

    private var enhancingCost = itemLevel?.let { cost(it, enhancingLevel) }
    fun getCost(): String {
        return NumberFormat.getNumberInstance(Locale.US).format(enhancingCost)
    }

    private var totalCost: Long = 0
    fun getTotalCost(): String {
        return NumberFormat.getNumberInstance(Locale.US).format(totalCost)
    }

    var successCount = 0
    var failureCount = 0

    data class EnhancingData(
        var successChance: (Int) -> Double,
        var failureChance: (Int) -> Double,
        val cost: (Int, Int) -> Int
    )

    private var enhancingData = (0..24).associateWith {
        EnhancingData(
            successChance = { enhancingLevel -> successChance(enhancingLevel) },
            failureChance = { enhancingLevel -> failureChance(enhancingLevel) },
            cost = { itemLevel, enhancingLevel -> cost(itemLevel, enhancingLevel) }
        )
    }

    fun updateSuccessChance(correctCount: Int) {
        val bonus = correctCount * 0.0125
        enhancingData = enhancingData.mapValues { (enhancingLevel, data) ->
            data.copy(
                successChance = { successChance(enhancingLevel, bonus) },
                failureChance = { failureChance(enhancingLevel, bonus) }
            )
        }
    }

    private fun cost(itemLevel: Int, enhancingLevel: Int): Int {
        val baseCost = 1000
        val l = itemLevel.toDouble().pow(3)
        val e = (enhancingLevel + 1).toDouble().pow(2.7)
        return when (enhancingLevel) {
            in 0 until 10 -> Math.round(baseCost + l * (enhancingLevel + 1) / 36).toInt()
            10 -> Math.round(baseCost + l * e / 571).toInt()
            11 -> Math.round(baseCost + l * e / 314).toInt()
            12 -> Math.round(baseCost + l * e / 214).toInt()
            13 -> Math.round(baseCost + l * e / 157).toInt()
            14 -> Math.round(baseCost + l * e / 107).toInt()
            in 15 until 25 -> Math.round(baseCost + l * e / 200).toInt()
            else -> throw IllegalArgumentException("Invalid enhancingLevel: $enhancingLevel")
        }
    }

    private fun successChance(enhancingLevel: Int, bonus: Double = 0.0): Double {
        return when (enhancingLevel) {
            in 0 until 3 -> (0.95 - 0.05 * enhancingLevel) * (1 + bonus)
            in 3 until 15 -> (1 - 0.05 * enhancingLevel) * (1 + bonus)
            in 15 until 22 -> 0.3 * (1 + bonus)
            22 -> 0.03 * (1 + bonus)
            23 -> 0.02 * (1 + bonus)
            24 -> 0.01 * (1 + bonus)
            else -> throw IllegalArgumentException("Invalid enhancingLevel")
        }
    }

    fun getSuccessChance(): String {
        val successChance = enhancingData[enhancingLevel]?.successChance?.invoke(enhancingLevel) ?: 0.0
        return String.format("%.2f", successChance * 100)
    }

    private fun failureChance(enhancingLevel: Int, bonus: Double = 0.0): Double {
        return 1 - successChance(enhancingLevel, bonus)
    }

    fun getFailureChance(): String {
        val failureChance = enhancingData[enhancingLevel]?.failureChance?.invoke(enhancingLevel) ?: 0.0
        return String.format("%.2f", failureChance * 100)
    }

    var statIncrease = 20
    var powerIncrease = 10
    var defIncrease = 10

    val maxEnhancingLevel = when (itemLevel) {
        in Int.MIN_VALUE until 110 -> 5
        in 110 until 120 -> 10
        in 120 until 130 -> 15
        in 130 until 140 -> 20
        else -> 25
    }

    fun simulateItem() {
        if (enhancingLevel < maxEnhancingLevel) {
            val currentEnhancingData = enhancingData[enhancingLevel]
                ?: error("No data for enhancing level $enhancingLevel")

            if (enhancingLevel == 0 && successCount == 0 && failureCount == 0) {
                enhancingCost = currentEnhancingData.cost(itemLevel!!, enhancingLevel)
            }

            val chance = Random.nextDouble()
            val bonus = (enhancingData[enhancingLevel]?.successChance?.invoke(enhancingLevel)
                ?: 0.0) - successChance(enhancingLevel)

            if (chance < currentEnhancingData.successChance(enhancingLevel) + bonus) {
                enhancingLevel++
                successCount++
                enhancingCost = currentEnhancingData.cost(itemLevel!!, enhancingLevel)
                stat += statIncrease

                when (itemType) {
                    "무기" -> power += powerIncrease
                    else -> def += defIncrease // 방어구
                }

                updateSuccessChance(0)
            } else {
                failureCount++
                enhancingCost = currentEnhancingData.cost(itemLevel!!, enhancingLevel)

                if (enhancingLevel !in listOf(0, 10, 15, 20)) {
                    enhancingLevel--
                    stat -= statIncrease

                    when (itemType) {
                        "무기" -> power -= powerIncrease
                        else -> def -= defIncrease // 방어구
                    }
                }

                updateSuccessChance(0)
            }

            totalCost += currentEnhancingData.cost(itemLevel, enhancingLevel)
        }
    }
}
