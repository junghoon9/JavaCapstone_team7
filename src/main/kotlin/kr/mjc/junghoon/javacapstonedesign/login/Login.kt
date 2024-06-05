package kr.mjc.junghoon.javacapstonedesign.login

import kr.mjc.junghoon.javacapstonedesign.main.StartAfterLoginUI
import kr.mjc.junghoon.javacapstonedesign.main.StartBeforeLoginUI
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.GridLayout
import java.awt.Insets
import javax.swing.*

class LoginUI : JFrame() {
    private val userManager = UserManager()

    init {
        title = "로그인"
        defaultCloseOperation = EXIT_ON_CLOSE
        layout = GridBagLayout()

        val loginUserIDField = JTextField(15)
        val loginPasswordField = JPasswordField(15)
        val loginButton = JButton("로그인")
        val registerButton = JButton("회원가입")
        val backButton = JButton("뒤로 가기")

        val userIDLabel = JLabel("아이디:")
        val passwordLabel = JLabel("비밀번호:")

        val gbc = GridBagConstraints().apply {
            gridx = 0
            gridy = 0
            insets = Insets(5, 5, 5, 5)
            anchor = GridBagConstraints.LINE_END
        }
        add(userIDLabel, gbc)

        gbc.gridx = 1
        gbc.anchor = GridBagConstraints.LINE_START
        add(loginUserIDField, gbc)

        gbc.gridx = 0
        gbc.gridy = 1
        gbc.anchor = GridBagConstraints.LINE_END
        add(passwordLabel, gbc)

        gbc.gridx = 1
        gbc.anchor = GridBagConstraints.LINE_START
        add(loginPasswordField, gbc)

        val buttonPanel = JPanel(GridLayout(3, 1, 0, 10)).apply {
            border = BorderFactory.createEmptyBorder(10, 0, 10, 0)
        }
        buttonPanel.add(loginButton)
        buttonPanel.add(registerButton)
        buttonPanel.add(backButton)

        gbc.gridx = 0
        gbc.gridy = 2
        gbc.gridwidth = 2
        gbc.anchor = GridBagConstraints.CENTER
        add(buttonPanel, gbc)

        val messageLabel = JLabel("", SwingConstants.CENTER)
        gbc.gridy = 3
        gbc.anchor = GridBagConstraints.CENTER
        add(messageLabel, gbc)

        // 로그인 버튼 액션
        loginButton.addActionListener {
            val userID = loginUserIDField.text
            val password = String(loginPasswordField.password)
            if (userID.isNotBlank() && password.isNotBlank()) {
                if (userManager.login(userID, password)) {
                    SwingUtilities.invokeLater {
                        val startAfterLoginUI = StartAfterLoginUI(userID)
                        startAfterLoginUI.isVisible = true
                    }
                    isVisible = false
                } else {
                    messageLabel.text = "아이디 또는 비밀번호가 틀립니다."
                }
            } else {
                messageLabel.text = "아이디와 비밀번호를 입력해주세요."
            }
        }

        // 회원가입 버튼 액션
        registerButton.addActionListener {
            showRegisterDialog()
        }

        backButton.addActionListener {
            LoginUI().dispose()
            val startBeforeLoginUI = StartBeforeLoginUI()
            startBeforeLoginUI.isVisible = true
            isVisible = false
        }

        // 창 크기 설정
        setSize(800, 600)
        setLocationRelativeTo(null) // 창을 화면 중앙에 배치
    }

    private fun showRegisterDialog() {
        val registerDialog = JDialog(this, "회원가입", true)
        registerDialog.layout = GridBagLayout()

        val regUserIDField = JTextField(15)
        val regPasswordField = JPasswordField(15)
        val regUsernameField = JTextField(15)
        val regButton = JButton("회원가입")
        val backButton = JButton("뒤로 가기")
        val messageLabel = JLabel("", SwingConstants.CENTER)

        val userIDLabel = JLabel("아이디:")
        val passwordLabel = JLabel("비밀번호:")
        val usernameLabel = JLabel("이름:")

        val gbc = GridBagConstraints().apply {
            gridx = 0
            gridy = 0
            insets = Insets(5, 5, 5, 5)
            anchor = GridBagConstraints.LINE_END
        }
        registerDialog.add(userIDLabel, gbc)

        gbc.gridx = 1
        gbc.anchor = GridBagConstraints.LINE_START
        registerDialog.add(regUserIDField, gbc)

        gbc.gridx = 0
        gbc.gridy = 1
        gbc.anchor = GridBagConstraints.LINE_END
        registerDialog.add(passwordLabel, gbc)

        gbc.gridx = 1
        gbc.anchor = GridBagConstraints.LINE_START
        registerDialog.add(regPasswordField, gbc)

        gbc.gridx = 0
        gbc.gridy = 2
        gbc.anchor = GridBagConstraints.LINE_END
        registerDialog.add(usernameLabel, gbc)

        gbc.gridx = 1
        gbc.anchor = GridBagConstraints.LINE_START
        registerDialog.add(regUsernameField, gbc)

        val buttonPanel = JPanel(GridLayout(2, 1, 0, 10)).apply {
            border = BorderFactory.createEmptyBorder(10, 0, 10, 0)
        }
        buttonPanel.add(regButton)
        buttonPanel.add(backButton)

        gbc.gridx = 0
        gbc.gridy = 3
        gbc.gridwidth = 2
        gbc.anchor = GridBagConstraints.CENTER
        registerDialog.add(buttonPanel, gbc)

        gbc.gridy = 4
        gbc.anchor = GridBagConstraints.CENTER
        registerDialog.add(messageLabel, gbc)

        regButton.addActionListener {
            val userID = regUserIDField.text
            val password = String(regPasswordField.password)
            val username = regUsernameField.text
            if (userID.isNotBlank() && password.isNotBlank() && username.isNotBlank()) {
                registerDialog.dispose()
                showConfirmDialog(userID, password, username)
            } else {
                messageLabel.text = "아이디, 비밀번호, 사용자 이름을 입력해주세요."
            }
        }

        backButton.addActionListener {
            registerDialog.dispose()
            val loginUI = LoginUI()
            loginUI.isVisible = true
            isVisible = false
        }

        // 회원가입 창 크기 설정
        registerDialog.setSize(800, 600)
        registerDialog.setLocationRelativeTo(this)
        registerDialog.isVisible = true
    }

    private fun showConfirmDialog(userID: String, password: String, username: String) {
        val confirmDialog = JDialog(this, "회원가입 확인", true)
        confirmDialog.layout = GridBagLayout()

        val infoLabel = JLabel("<html>" +
                                    "아이디: $userID<br>" +
                                    "비밀번호: $password<br>" +
                                    "사용자 이름: $username" +
                                    "</html>")
        val confirmButton = JButton("확인")
        val cancelButton = JButton("취소")
        val messageLabel = JLabel("", SwingConstants.CENTER)

        val gbc = GridBagConstraints().apply {
            gridx = 0
            gridy = 0
            insets = Insets(5, 5, 5, 5)
            anchor = GridBagConstraints.CENTER
        }
        confirmDialog.add(infoLabel, gbc)

        val buttonPanel = JPanel(GridLayout(1, 2, 10, 0)).apply {
            border = BorderFactory.createEmptyBorder(10, 0, 10, 0)
        }
        buttonPanel.add(confirmButton)
        buttonPanel.add(cancelButton)

        gbc.gridy = 1
        gbc.anchor = GridBagConstraints.CENTER
        confirmDialog.add(buttonPanel, gbc)

        gbc.gridy = 2
        gbc.anchor = GridBagConstraints.CENTER
        confirmDialog.add(messageLabel, gbc)

        confirmButton.addActionListener {
            if (userManager.checkUserIDExists(userID)) {
                messageLabel.text = "아이디가 이미 존재합니다."
            }
            else {
                if (userManager.register(userID, password, username)) {
                    JOptionPane.showMessageDialog(confirmDialog, "회원가입 성공!",
                        "회원가입", JOptionPane.INFORMATION_MESSAGE)
                    confirmDialog.dispose()
                }
                else {
                    messageLabel.text = "회원가입 실패: 오류가 발생했습니다."
                }
            }
        }

        cancelButton.addActionListener {
            confirmDialog.dispose()
        }

        // 확인 창 크기 설정
        confirmDialog.setSize(800, 600)
        confirmDialog.setLocationRelativeTo(this)
        confirmDialog.isVisible = true
    }
}
