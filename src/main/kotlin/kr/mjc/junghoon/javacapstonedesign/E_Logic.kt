package kr.mjc.junghoon.javacapstonedesign

import java.text.NumberFormat
import java.util.*
import javax.swing.SwingUtilities
import kotlin.math.pow
import kotlin.random.Random

class Logic(private val itemType: String?, private val jobType: String?, private val itemLevel: Int?) {
    private var strength = 0
    private var dexterity = 0
    private var intelligence = 0
    private var luck = 0

    private var stat = when (jobType) {
        "전사" -> strength
        "궁수" -> dexterity
        "마법사" -> intelligence
        else -> luck
    }

    fun statName(): String {
        return when (jobType) {
            "전사" -> "STR"
            "궁수" -> "DEX"
            "마법사" -> "INT"
            else -> "LUK" // 도적
        }
    }

    private var enhancingLevel = 0
    fun getEnhancingLevel(): Int {
        return enhancingLevel
    }

    private var ap = 0 // 공격력
    private var mp = 0 // 마력
    private var power = when (jobType) {
        "마법사" -> mp
        else -> ap // 전사, 궁수, 도적
    }

    fun powerName(): String {
        return when (jobType) {
            "마법사" -> "마력"
            else -> "공격력" // 전사, 궁수, 도적
        }
    }

    private var def = 0 // 방어력
    private fun getDef(): Int {
        return def
    }
    fun defName(): String {
        return "방어력"
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
    private var successCount = 0
    fun getSuccessCount(): Int {
        return successCount
    }
    private var failureCount = 0
    fun getFailureCount(): Int {
        return failureCount
    }


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

    private fun successChance(enhancingLevel: Int): Double {
        val miniGame = MiniGame()
        val crtPoint = miniGame.getCorrect()
        val bonus = crtPoint * 0.0125
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
        return String.format("%.2f", successChance(enhancingLevel) * 100)
    }

    private fun failureChance(enhancingLevel: Int): Double {
        return 1 - successChance(enhancingLevel)
    }
    fun getFailureChance(): String {
        return String.format("%.2f", failureChance(enhancingLevel) * 100)
    }

    private var statIncrease = 20
    fun getStatIncrease(): Int {
        return statIncrease
    }

    private var powerIncrease = 10
    fun getPowerIncrease(): Int {
        return powerIncrease
    }

    private var defIncrease = 10
    fun getDefIncrease(): Int {
        return defIncrease
    }

    fun simulateItem() {
        val maxEnhancingLevel = when(itemLevel!!) {
            in Int.MIN_VALUE until 110 -> 5
            in 110 until 120 -> 10
            in 120 until 130 -> 15
            in 130 until 140 -> 20
            else -> 25
        }

        if (enhancingLevel < maxEnhancingLevel) {
            val currentEnhancingData = enhancingData[enhancingLevel]
                ?: error("No data for enhancing level $enhancingLevel")

            if (enhancingLevel == 0 && successCount == 0 && failureCount == 0) {
                enhancingCost = currentEnhancingData.cost(itemLevel, enhancingLevel)
            }
            val chance = Random.nextDouble()

            //강화 성공
            if (chance < currentEnhancingData.successChance(enhancingLevel)) {
                enhancingLevel++
                successCount++
                enhancingCost = currentEnhancingData.cost(itemLevel, enhancingLevel)

                when (itemType) {
                    "무기" -> { stat += statIncrease; power += powerIncrease }
                    else -> { stat += statIncrease; def += defIncrease } // 방어구
                }
            }

            //강화 실패
            else {
                // 0강, 10강, 15강, 20강에서 강화 실패 시 강화 단계 유지
                if ((enhancingLevel == 0 || enhancingLevel == 10 || enhancingLevel == 15 || enhancingLevel == 20)) {
                    failureCount++
                    enhancingCost = currentEnhancingData.cost(itemLevel, enhancingLevel)
                }
                else { //그 이외에는 하락
                    enhancingLevel--
                    failureCount++
                    enhancingCost = currentEnhancingData.cost(itemLevel, enhancingLevel)

                    when (itemType) {
                        "무기" -> { stat -= statIncrease; power -= powerIncrease }
                        else -> { stat -= statIncrease; def -= defIncrease } // 방어구
                    }
                }
            }
            totalCost += currentEnhancingData.cost(itemLevel, enhancingLevel)
        }
        else {
            //todo
        }
    }
}

fun main() {
    SwingUtilities.invokeLater {
        val itemSelectingUI = ItemSelectingUI()
        itemSelectingUI.isVisible = false

        val it = itemSelectingUI.getSelectedItemType()
        val jt = itemSelectingUI.getSelectedJobType()
        val il = itemSelectingUI.getSelectedLevel()
        val logic = Logic(it, jt, il)
        logic.simulateItem()
    }
}