package kr.mjc.junghoon.javacapstonedesign.main

import java.awt.BorderLayout
import java.awt.Font
import javax.swing.*

class FinishUI(private val logic: Logic,
               private val loggedInUserID: String): JFrame() {
    private val homeButton = JButton("홈으로")
    private val restartButton = JButton("다시 하기")
    private var finalEnhancingInfoLabel = JLabel()

    init {
        title = "아이템 강화"
        defaultCloseOperation = EXIT_ON_CLOSE
        setSize(800, 600)
        setLocationRelativeTo(null)
        layout = BorderLayout()

        val topPanel = JPanel()
        val titleLabel = JLabel("강화 완료")
        titleLabel.font = Font("SansSerif", Font.BOLD, 24)
        topPanel.add(titleLabel)
        add(topPanel, BorderLayout.NORTH)

        val finalEnhancingInfoPanel = JPanel()
        val ehcLv = logic.enhancingLevel
        val sCount = logic.successCount
        val fCount = logic.failureCount
        val statN = logic.statName
        val stat = logic.stat
        val powerOrDefenceN = logic.powerOrDefenceName
        val totalPowerInc = logic.power
        val totalDefInc = logic.def
        val totalCost = logic.getTotalCost()
        val finalEnhancingInfoText = when (logic.itemType) {
            "무기" -> {
                "<html>" +
                "총 $ehcLv 강 강화 완료<br><br>" +
                "성공횟수: $sCount 회<br>" +
                "실패횟수: $fCount 회<br><br>" +
                "$statN: +$stat<br>" +
                "$powerOrDefenceN: +$totalPowerInc<br><br>" +
                "총 강화비용: $totalCost 골드<br>" +
                "</html>"
            }
            else -> { // 방어구
                "<html>" +
                "총 $ehcLv 강 강화 완료<br><br>" +
                "성공횟수: $sCount 회<br>" +
                "실패횟수: $fCount 회<br><br>" +
                "$statN: +$stat<br>" +
                "$powerOrDefenceN: +$totalDefInc<br><br>" +
                "총 강화비용: $totalCost 골드<br>" +
                "</html>"
            }
        }
        val font = Font("Arial", Font.PLAIN, 30)
        finalEnhancingInfoLabel = JLabel(finalEnhancingInfoText)
        finalEnhancingInfoLabel.font = font
        finalEnhancingInfoPanel.add(finalEnhancingInfoLabel)

        val buttonPanel = JPanel()
        buttonPanel.add(homeButton)
        buttonPanel.add(restartButton)
        homeButton.addActionListener {
            if (logic.itemType != null && logic.jobType != null && logic.itemLevel != null) {
                SwingUtilities.invokeLater {
                    FinishUI(logic, loggedInUserID).dispose()
                    val startAfterLoginUI = StartAfterLoginUI(loggedInUserID)
                    startAfterLoginUI.isVisible = true
                }
                isVisible = false
            }
        }
        restartButton.addActionListener {
            if (logic.itemType != null && logic.jobType != null && logic.itemLevel != null) {
                SwingUtilities.invokeLater {
                    FinishUI(logic, loggedInUserID).dispose()
                    val itemSelectingUI = ItemSelectingUI(loggedInUserID)
                    itemSelectingUI.isVisible = true
                }
                isVisible = false // 현재 화면을 숨김
            }
        }

        add(finalEnhancingInfoPanel, BorderLayout.CENTER)
        add(buttonPanel, BorderLayout.SOUTH)
    }
}
