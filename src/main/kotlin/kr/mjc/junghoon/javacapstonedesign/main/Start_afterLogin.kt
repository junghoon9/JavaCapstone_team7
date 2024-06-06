package kr.mjc.junghoon.javacapstonedesign.main

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
        add(titleLabel, createGridBagConstraints(0, 0, GridBagConstraints.CENTER, Insets(20, 0, 10, 0)))

        welcomeLabel = JLabel("환영합니다, ${getUsernameByID(loggerInUserID)} 님!")
        welcomeLabel.horizontalAlignment = SwingConstants.CENTER
        add(welcomeLabel, createGridBagConstraints(0, 1, GridBagConstraints.CENTER, Insets(0, 0, 20, 0)))

        val itemSetUpButton = JButton("아이템 설정")
        itemSetUpButton.addActionListener(this)
        add(itemSetUpButton, createGridBagConstraints(0, 2, GridBagConstraints.CENTER, Insets(20, 0, 10, 0), 20))

        val logoutButton = JButton("로그아웃")
        logoutButton.addActionListener(this)
        add(logoutButton, createGridBagConstraints(0, 3, GridBagConstraints.CENTER, Insets(10, 0, 20, 0), 35))
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
                else JOptionPane.showMessageDialog(this, "로그인이 필요합니다.", null, JOptionPane.WARNING_MESSAGE)
            }
            "로그아웃" -> {
                loggerInUserID = null
                welcomeLabel.text = ""
                JOptionPane.showMessageDialog(this, "로그아웃 되었습니다.", null, JOptionPane.INFORMATION_MESSAGE)
                SwingUtilities.invokeLater {
                    StartAfterLoginUI(loggerInUserID).dispose()
                    val startBeforeLoginUI = StartBeforeLoginUI()
                    startBeforeLoginUI.isVisible = true
                }
                isVisible = false
            }
        }
    }

    private fun createGridBagConstraints(
        gridX: Int,
        gridY: Int,
        anchor: Int,
        insets: Insets = Insets(5, 5, 5, 5),
        ipadX: Int = 0,
        gridWidth: Int = 1
    ): GridBagConstraints {
        return GridBagConstraints().apply {
            gridx = gridX
            gridy = gridY
            this.anchor = anchor
            this.insets = insets
            ipadx = ipadX
            gridwidth = gridWidth
        }
    }
}
