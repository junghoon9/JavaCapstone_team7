package kr.mjc.junghoon.javacapstonedesign.main

import kr.mjc.junghoon.javacapstonedesign.login.LoginUI
import kr.mjc.junghoon.javacapstonedesign.login.UserManager
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import javax.swing.*

class StartAfterLoginUI(private var loggerInUserID: String?) : JFrame(), ActionListener {
    private var welcomeLabel: JLabel
    private val userManager = UserManager()

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
            insets = Insets(20, 0, 10, 0)
        }
        add(titleLabel, titleConstraints)



        welcomeLabel = JLabel("환영합니다, ${getUsernameByID(loggerInUserID)} 님!")
        welcomeLabel.horizontalAlignment = SwingConstants.CENTER
        val welcomeConstraints = GridBagConstraints().apply {
            gridx = 0
            gridy = 1
            insets = Insets(0, 0, 20, 0)
        }
        add(welcomeLabel, welcomeConstraints)

        val itemSetUpButton = JButton("아이템 설정")
        itemSetUpButton.addActionListener(this)
        val itemSetupConstraints = GridBagConstraints().apply {
            gridx = 0
            gridy = 2
            insets = Insets(20, 0, 10, 0)
            ipadx = 20
        }
        add(itemSetUpButton, itemSetupConstraints)

        val logoutButton = JButton("로그아웃")
        logoutButton.addActionListener(this)
        val logoutConstraints = GridBagConstraints().apply {
            gridx = 0
            gridy = 3
            insets = Insets(10, 0, 20, 0)
            ipadx = 35
        }
        add(logoutButton, logoutConstraints)
    }

    private fun getUsernameByID(userID: String?): String {
        return if (userID != null) {
            userManager.getUsernameByUserID(userID)
        }
        else ""
    }

    override fun actionPerformed(e: ActionEvent?) {
        when (e?.actionCommand) {
            "아이템 설정" -> {
                if (loggerInUserID != null) {
                    SwingUtilities.invokeLater {
                        val itemSelectingUI = ItemSelectingUI(loggerInUserID!!)
                        itemSelectingUI.isVisible = true
                    }
                    isVisible = false // 현재 화면을 숨김
                }
                else JOptionPane.showMessageDialog(this, "로그인이 필요합니다.", "로그인 필요", JOptionPane.WARNING_MESSAGE)
            }
            "로그아웃" -> {
                loggerInUserID = null
                welcomeLabel.text = ""
                JOptionPane.showMessageDialog(this, "로그아웃 되었습니다.", "로그아웃", JOptionPane.INFORMATION_MESSAGE)
                SwingUtilities.invokeLater {
                    val loginUI = LoginUI()
                    loginUI.isVisible = true
                }
                isVisible = false
            }
        }
    }
}
