package kr.mjc.junghoon.javacapstonedesign.main

import kr.mjc.junghoon.javacapstonedesign.login.LoginUI
import kr.mjc.junghoon.javacapstonedesign.login.UserManager
import javax.swing.*
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import kotlin.system.exitProcess

class StartBeforeLoginUI : JFrame(), ActionListener {
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

        val withdrawButton = JButton("회원탈퇴")
        withdrawButton.addActionListener(this)
        val withdrawConstraints = GridBagConstraints().apply {
            gridx = 0
            gridy = 2
            insets = Insets(10, 0, 10, 0)
            ipadx = 35
        }
        add(withdrawButton, withdrawConstraints)

        val exitButton = JButton("종료")
        exitButton.addActionListener(this)
        val exitConstraints = GridBagConstraints().apply {
            gridx = 0
            gridy = 3
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
            "회원탈퇴" -> {
                showWithdrawDialog()
            }
            "종료" -> {
                exitProcess(0)
            }
        }
    }

    private fun showWithdrawDialog() {
        val withdrawDialog = JDialog(this, "회원탈퇴", true)
        withdrawDialog.layout = GridBagLayout()

        val userIdLabel = JLabel("아이디:")
        val userIdField = JTextField(15)
        val passwordLabel = JLabel("비밀번호:")
        val passwordField = JPasswordField(15)
        val withdrawButton = JButton("탈퇴")
        val cancelButton = JButton("취소")
        val messageLabel = JLabel("", SwingConstants.CENTER)

        val gbc = GridBagConstraints().apply {
            gridx = 0
            gridy = 0
            insets = Insets(5, 5, 5, 5)
            anchor = GridBagConstraints.LINE_END
        }
        withdrawDialog.add(userIdLabel, gbc)

        gbc.gridx = 1
        gbc.anchor = GridBagConstraints.LINE_START
        withdrawDialog.add(userIdField, gbc)

        gbc.gridx = 0
        gbc.gridy = 1
        gbc.anchor = GridBagConstraints.LINE_END
        withdrawDialog.add(passwordLabel, gbc)

        gbc.gridx = 1
        gbc.anchor = GridBagConstraints.LINE_START
        withdrawDialog.add(passwordField, gbc)

        val buttonPanel = JPanel(GridLayout(1, 2, 10, 0)).apply {
            border = BorderFactory.createEmptyBorder(10, 0, 10, 0)
        }
        buttonPanel.add(withdrawButton)
        buttonPanel.add(cancelButton)

        gbc.gridx = 0
        gbc.gridy = 2
        gbc.gridwidth = 2
        gbc.anchor = GridBagConstraints.CENTER
        withdrawDialog.add(buttonPanel, gbc)

        gbc.gridy = 3
        gbc.anchor = GridBagConstraints.CENTER
        withdrawDialog.add(messageLabel, gbc)

        withdrawButton.addActionListener {
            val userID = userIdField.text
            val password = String(passwordField.password)
            if (userID.isNotEmpty() && password.isNotEmpty()) {
                if (userManager.withdrawUser(userID, password)) {
                    JOptionPane.showMessageDialog(withdrawDialog, "회원탈퇴가 완료되었습니다.")
                    withdrawDialog.dispose()
                }
                else {
                    messageLabel.text = "회원탈퇴에 실패했습니다. 아이디와 비밀번호를 확인해주세요."
                }
            }
            else {
                messageLabel.text = "아이디와 비밀번호를 입력해주세요."
            }
        }
        cancelButton.addActionListener {
            withdrawDialog.dispose()
        }
        withdrawDialog.setSize(400, 200)
        withdrawDialog.setLocationRelativeTo(this)
        withdrawDialog.isVisible = true
    }
}
