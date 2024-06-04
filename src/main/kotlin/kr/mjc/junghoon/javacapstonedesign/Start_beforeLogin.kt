package kr.mjc.junghoon.javacapstonedesign.main

import kr.mjc.junghoon.javacapstonedesign.login.LoginUI
import javax.swing.*
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import kotlin.system.exitProcess

class StartBeforeLoginUI : JFrame(), ActionListener {
    init {
        title = "아이템 강화"
        defaultCloseOperation = EXIT_ON_CLOSE
        setSize(800, 600)
        setLocationRelativeTo(null)
        layout = GridBagLayout()

        val titleLabel = JLabel("아이템 강화 시뮬레이션")
        titleLabel.horizontalAlignment = SwingConstants.CENTER
        titleLabel.font = titleLabel.font.deriveFont(24f)
        val titleConstraints = GridBagConstraints().apply {
            gridx = 0
            gridy = 0
            insets = Insets(20, 0, 20, 0)
        }
        add(titleLabel, titleConstraints)

        val loginButton = JButton("로그인")
        loginButton.addActionListener(this)
        val itemSetupConstraints = GridBagConstraints().apply {
            gridx = 0
            gridy = 1
            insets = Insets(10, 0, 10, 0)
            ipadx = 48
        }
        add(loginButton, itemSetupConstraints)

        val exitButton = JButton("종료")
        exitButton.addActionListener(this)
        val exitConstraints = GridBagConstraints().apply {
            gridx = 0
            gridy = 2
            insets = Insets(10, 0, 20, 0)
            ipadx = 60
        }
        add(exitButton, exitConstraints)
    }

    override fun actionPerformed(e: ActionEvent?) {
        when (e?.actionCommand) {
            "로그인" -> {
                val loginUI = LoginUI()
                loginUI.isVisible = true
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
        val startBeforeLoginUI = StartBeforeLoginUI()
        startBeforeLoginUI.isVisible = true
    }
}