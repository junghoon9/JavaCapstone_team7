package kr.mjc.junghoon.javacapstonedesign

import javax.swing.*
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

class ItemSelectingUI : JFrame(), ActionListener {
    private val itemTypes = arrayOf(null, "무기", "방어구")
    private val jobTypes = arrayOf(null, "전사", "궁수", "마법사", "도적")

    private val itemTypeComboBox = JComboBox(itemTypes)
    private val jobTypeComboBox = JComboBox(jobTypes)
    private val levelTextField = JTextField(10)

    private val itemTypeLabel = JLabel("아이템 종류:")
    private val jobTypeLabel = JLabel("착용 직업군:")
    private val levelLabel = JLabel("착용 레벨(1-300):")

    // 아이템 종류와 직업군 및 레벨을 저장할 변수
    var selectedItemType: String? = null
    var selectedJobType: String? = null
    var selectedLevel: Int? = null

    init {
        title = "아이템 강화"
        defaultCloseOperation = EXIT_ON_CLOSE
        setSize(800, 600)
        setLocationRelativeTo(null)
        layout = GridLayout(4, 2)

        add(itemTypeLabel)
        add(itemTypeComboBox)
        add(jobTypeLabel)
        add(jobTypeComboBox)
        add(levelLabel)
        add(levelTextField)

        val startButton = JButton("강화 시작")
        startButton.addActionListener(this)
        add(startButton)

        val backButton = JButton("뒤로 가기")
        backButton.addActionListener(this)
        add(backButton)
    }

    override fun actionPerformed(e: ActionEvent?) {
        when (e?.actionCommand) {
            "강화 시작" -> {
                selectedItemType = itemTypeComboBox.selectedItem?.toString()
                selectedJobType = jobTypeComboBox.selectedItem?.toString()
                selectedLevel = levelTextField.text.toIntOrNull()

                var isValid = true

                if (selectedItemType == null) {
                    itemTypeLabel.text = "아이템 종류: (아이템 종류를 선택하세요)"
                    itemTypeLabel.foreground = Color.RED
                    isValid = false
                } else {
                    itemTypeLabel.text = "아이템 종류:"
                    itemTypeLabel.foreground = Color.BLACK
                }

                if (selectedJobType == null) {
                    jobTypeLabel.text = "착용 직업군: (착용 직업군을 선택하세요)"
                    jobTypeLabel.foreground = Color.RED
                    isValid = false
                } else {
                    jobTypeLabel.text = "착용 직업군:"
                    jobTypeLabel.foreground = Color.BLACK
                }

                if (selectedLevel == null || selectedLevel !in 1..300) {
                    levelLabel.text = "착용 레벨(1-300): (올바른 착용 레벨을 입력하세요)"
                    levelLabel.foreground = Color.RED
                    isValid = false
                } else {
                    levelLabel.text = "착용 레벨(1-300):"
                    levelLabel.foreground = Color.BLACK
                }

                if (isValid) {
                    isVisible = false
                    val logic = Logic(selectedItemType, selectedJobType, selectedLevel)
                    val enhancingUI = EnhancingUI(selectedItemType, selectedJobType, selectedLevel, logic)
                    enhancingUI.isVisible = true
                }
            }
            "뒤로 가기" -> {
                val startUI = StartUI()
                startUI.isVisible = true
                isVisible = false // 현재 화면을 숨김
            }
        }
    }
}

fun main() {
    SwingUtilities.invokeLater {
        val itemSelectingUI = ItemSelectingUI()
        itemSelectingUI.isVisible = true
    }
}
