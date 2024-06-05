package kr.mjc.junghoon.javacapstonedesign.main

import javax.swing.*
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

class ItemSelectingUI(private val loggedInUserID: String) : JFrame(), ActionListener {
    private val itemTypes = arrayOf(null, "무기", "방어구")
    private val jobTypes = arrayOf(null, "전사", "궁수", "마법사", "도적")

    private val itemTypeComboBox = JComboBox(itemTypes)
    private val jobTypeComboBox = JComboBox(jobTypes)
    private val levelTextField = JTextField(10)

    private val itemTypeLabel = JLabel("아이템 종류:")
    private val jobTypeLabel = JLabel("착용 직업군:")
    private val levelLabel = JLabel("착용 레벨(1-300):")

    // 아이템 종류와 직업군 및 레벨을 저장할 변수
    private var selectedItemType: String? = null
    private var selectedJobType: String? = null
    private var selectedLevel: Int? = null

    init {
        title = "아이템 강화"
        defaultCloseOperation = EXIT_ON_CLOSE
        setSize(800, 600)
        setLocationRelativeTo(null)
        layout = GridBagLayout()

        val gbc = GridBagConstraints().apply {
            gridx = 0
            gridy = 0
            insets = Insets(5, 5, 5, 5)
            anchor = GridBagConstraints.LINE_END
        }
        add(itemTypeLabel, gbc)

        gbc.gridx = 1
        gbc.anchor = GridBagConstraints.LINE_START
        add(itemTypeComboBox, gbc)

        gbc.gridx = 0
        gbc.gridy = 1
        gbc.anchor = GridBagConstraints.LINE_END
        add(jobTypeLabel, gbc)

        gbc.gridx = 1
        gbc.anchor = GridBagConstraints.LINE_START
        add(jobTypeComboBox, gbc)

        gbc.gridx = 0
        gbc.gridy = 2
        gbc.anchor = GridBagConstraints.LINE_END
        add(levelLabel, gbc)

        gbc.gridx = 1
        gbc.anchor = GridBagConstraints.LINE_START
        add(levelTextField, gbc)

        val buttonPanel = JPanel(GridLayout(1, 2, 10, 0)).apply {
            border = BorderFactory.createEmptyBorder(10, 0, 10, 0)
        }

        val startButton = JButton("강화 시작")
        startButton.addActionListener(this)
        buttonPanel.add(startButton)

        val backButton = JButton("뒤로 가기")
        backButton.addActionListener(this)
        buttonPanel.add(backButton)

        gbc.gridx = 0
        gbc.gridy = 3
        gbc.gridwidth = 2
        gbc.anchor = GridBagConstraints.CENTER
        add(buttonPanel, gbc)
    }

    override fun actionPerformed(e: ActionEvent?) {
        when (e?.actionCommand) {
            "강화 시작" -> {
                selectedItemType = itemTypeComboBox.selectedItem?.toString()
                selectedJobType = jobTypeComboBox.selectedItem?.toString()
                selectedLevel = levelTextField.text.toIntOrNull()

                var isValid = true

                if (selectedItemType == null) {
                    itemTypeLabel.text = "아이템 종류:(아이템 종류를 선택하세요)"
                    itemTypeLabel.foreground = Color.RED
                    isValid = false
                } else {
                    itemTypeLabel.text = "아이템 종류:"
                    itemTypeLabel.foreground = Color.BLACK
                }

                if (selectedJobType == null) {
                    jobTypeLabel.text = "착용 직업군:(착용 직업군을 선택하세요)"
                    jobTypeLabel.foreground = Color.RED
                    isValid = false
                } else {
                    jobTypeLabel.text = "착용 직업군:"
                    jobTypeLabel.foreground = Color.BLACK
                }

                if (selectedLevel == null || selectedLevel !in 1..300) {
                    levelLabel.text = "착용 레벨(1-300):(올바른 착용 레벨을 입력하세요)"
                    levelLabel.foreground = Color.RED
                    isValid = false
                } else {
                    levelLabel.text = "착용 레벨(1-300):"
                    levelLabel.foreground = Color.BLACK
                }

                if (isValid) {
                    val logic = Logic(selectedItemType, selectedJobType, selectedLevel)
                    logic.simulateItem()

                    SwingUtilities.invokeLater {
                        val enhancingUI = EnhancingUI(selectedItemType, selectedJobType, selectedLevel, logic, loggedInUserID)
                        enhancingUI.isVisible = true
                    }
                    isVisible = false
                }
            }
            "뒤로 가기" -> {
                val startAfterLoginUI = StartAfterLoginUI(loggedInUserID)
                startAfterLoginUI.isVisible = true
                isVisible = false // 현재 화면을 숨김
            }
        }
    }
}
