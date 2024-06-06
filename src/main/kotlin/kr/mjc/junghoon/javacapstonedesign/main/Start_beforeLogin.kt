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
        add(titleLabel, createGridBagConstraints(0, 0, GridBagConstraints.CENTER, Insets(20, 0, 20, 0)))

        val loginButton = JButton("로그인")
        loginButton.addActionListener(this)
        add(loginButton, createGridBagConstraints(0, 1, GridBagConstraints.CENTER, Insets(10, 0, 10, 0), 48))

        val withdrawButton = JButton("회원탈퇴")
        withdrawButton.addActionListener(this)
        add(withdrawButton, createGridBagConstraints(0, 2, GridBagConstraints.CENTER, Insets(10, 0, 10, 0), 35))

        val exitButton = JButton("종료")
        exitButton.addActionListener(this)
        add(exitButton, createGridBagConstraints(0, 3, GridBagConstraints.CENTER, Insets(10, 0, 20, 0), 60))
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

        withdrawDialog.add(userIdLabel, createGridBagConstraints(0, 0, GridBagConstraints.LINE_END))
        withdrawDialog.add(userIdField, createGridBagConstraints(1, 0, GridBagConstraints.LINE_START))
        withdrawDialog.add(passwordLabel, createGridBagConstraints(0, 1, GridBagConstraints.LINE_END))
        withdrawDialog.add(passwordField, createGridBagConstraints(1, 1, GridBagConstraints.LINE_START))

        val buttonPanel = JPanel(GridLayout(1, 2, 10, 0)).apply {
            border = BorderFactory.createEmptyBorder(10, 0, 10, 0)
        }
        buttonPanel.add(withdrawButton)
        buttonPanel.add(cancelButton)

        withdrawDialog.add(buttonPanel, createGridBagConstraints(0, 2, GridBagConstraints.CENTER, gridWidth = 2))
        withdrawDialog.add(messageLabel, createGridBagConstraints(0, 3, GridBagConstraints.CENTER, gridWidth = 2))

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
