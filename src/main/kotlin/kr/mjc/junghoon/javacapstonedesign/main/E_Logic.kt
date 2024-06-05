package kr.mjc.junghoon.javacapstonedesign.main

import java.text.NumberFormat
import java.util.*
import kotlin.math.pow
import kotlin.random.Random

class Logic(
    val itemType: String?,
    val jobType: String?,
    val itemLevel: Int?) {
    private var strength = 0
    private var dexterity = 0
    private var intelligence = 0
    private var luck = 0

    // 직업에 따라 스탯 할당
    var stat = when (jobType) {
        "전사" -> strength
        "궁수" -> dexterity
        "마법사" -> intelligence
        else -> luck // 도적
    }

    var statName = when (jobType) {
        "전사" -> "STR"
        "궁수" -> "DEX"
        "마법사" -> "INT"
        else -> "LUK" // 도적
    }

    var enhancingLevel = 0

    private var ap = 0 // 공격력(attack power)
    private var mp = 0 // 마력(magic power)
    var def = 0 // 방어력
    var power = when (jobType) {
        "마법사" -> mp
        else -> ap // 전사, 궁수, 도적
    }

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

    // 최종 강화화면에 표시할 내용
    var successCount = 0
    var failureCount = 0

    data class EnhancingData(
        var successChance: (Int) -> Double,
        var failureChance: (Int) -> Double,
        val cost: (Int, Int) -> Int
    )

    fun updateSuccessChance(correctCount: Int) {
        val bonus = correctCount * 0.0125
        enhancingData = (0..24).associateWith {
            EnhancingData(
                successChance = { enhancingLevel -> successChance(enhancingLevel, bonus) },
                failureChance = { enhancingLevel -> failureChance(enhancingLevel, bonus) },
                cost = { itemLevel, enhancingLevel -> cost(itemLevel, enhancingLevel) }
            )
        }
    }

    private fun resetSuccessChance() {
        enhancingData = (0..24).associateWith {
            EnhancingData(
                successChance = { enhancingLevel -> successChance(enhancingLevel, 0.0) },
                failureChance = { enhancingLevel -> failureChance(enhancingLevel, 0.0) },
                cost = { itemLevel, enhancingLevel -> cost(itemLevel, enhancingLevel) }
            )
        }
    }


    private var enhancingData = (0..24).associateWith {
        EnhancingData(
            successChance = { enhancingLevel -> successChance(enhancingLevel, 0.0) },
            failureChance = { enhancingLevel -> failureChance(enhancingLevel, 0.0) },
            cost = { itemLevel, enhancingLevel -> cost(itemLevel, enhancingLevel) }
        )
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

    private fun successChance(enhancingLevel: Int, bonus: Double): Double {
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

    private fun failureChance(enhancingLevel: Int, bonus: Double): Double {
        return 1 - successChance(enhancingLevel, bonus)
    }

    fun getFailureChance(): String {
        val failureChance = enhancingData[enhancingLevel]?.failureChance?.invoke(enhancingLevel) ?: 0.0
        return String.format("%.2f", failureChance * 100)
    }

    var statIncrease = 20
    var powerIncrease = 10
    var defIncrease = 10

    // 아이템 레벨에 따른 최대 강화 단계 설정
    val maxEnhancingLevel = when(itemLevel) {
        in Int.MIN_VALUE until 110 -> 5
        in 110 until 120 -> 10
        in 120 until 130 -> 15
        in 130 until 140 -> 20
        else -> 25
    }


    fun simulateItem() {

        // 현재 강화 단계 < 최대 강화 단계
        if (enhancingLevel < maxEnhancingLevel) {
            val currentEnhancingData = enhancingData[enhancingLevel]
                ?: error("No data for enhancing level $enhancingLevel")

            // 맨 처음 강화 시도
            if (enhancingLevel == 0 && successCount == 0 && failureCount == 0) {
                enhancingCost = currentEnhancingData.cost(itemLevel!!, enhancingLevel)
            }

            val chance = Random.nextDouble()
            val bonus = (enhancingData[enhancingLevel]?.
            successChance?.invoke(enhancingLevel) ?: 0.0) - successChance(enhancingLevel, 0.0)

            //강화 성공
            if (chance < currentEnhancingData.successChance(enhancingLevel) + bonus) {
                enhancingLevel++
                successCount++
                enhancingCost = currentEnhancingData.cost(itemLevel!!, enhancingLevel)
                stat += statIncrease

                when (itemType) {
                    "무기" -> power += powerIncrease
                    else -> def += defIncrease// 방어구
                }

                resetSuccessChance()
            }

            //강화 실패
            else {
                // 0강, 10강, 15강, 20강에서 강화 실패 시 강화 단계 유지
                if ((enhancingLevel == 0 || enhancingLevel == 10 || enhancingLevel == 15 || enhancingLevel == 20)) {
                    failureCount++
                    enhancingCost = currentEnhancingData.cost(itemLevel!!, enhancingLevel)
                }
                else { //그 이외에는 하락
                    enhancingLevel--
                    failureCount++
                    enhancingCost = currentEnhancingData.cost(itemLevel!!, enhancingLevel)
                    stat -= statIncrease

                    when (itemType) {
                        "무기" -> power -= powerIncrease
                        else -> def -= defIncrease // 방어구
                    }

                    resetSuccessChance()
                }
            }
            totalCost += currentEnhancingData.cost(itemLevel, enhancingLevel)
        }
    }
}
