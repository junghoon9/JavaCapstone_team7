package kr.mjc.junghoon.javacapstonedesign

import javax.swing.*
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import kotlin.system.exitProcess

class StartUI : JFrame(), ActionListener {
    init {
        title = "아이템 강화"
        defaultCloseOperation = EXIT_ON_CLOSE
        setSize(800, 600)
        setLocationRelativeTo(null)
        layout = GridLayout(3, 1)

        val titleLabel = JLabel("아이템 강화 시뮬레이션")
        titleLabel.horizontalAlignment = SwingConstants.CENTER
        add(titleLabel)

        val itemSetupButton = JButton("아이템 설정")
        itemSetupButton.addActionListener(this)
        add(itemSetupButton)

        val exitButton = JButton("종료")
        exitButton.addActionListener(this)
        add(exitButton)
    }

    override fun actionPerformed(e: ActionEvent?) {
        when (e?.actionCommand) {
            "아이템 설정" -> {
                val itemSelectingUI = ItemSelectingUI()
                itemSelectingUI.isVisible = true
                isVisible = false // 현재 화면을 숨김
            }
            "종료" -> {
                exitProcess(0)
            }
        }
    }
}


fun main() {
    SwingUtilities.invokeLater {
        val startUI = StartUI()
        startUI.isVisible = true
    }
}
