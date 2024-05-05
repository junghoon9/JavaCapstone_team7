package kr.mjc.junghoon.javacapstonedesign

import javax.swing.*
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

class ItemSelectingUI : JFrame(), ActionListener {
    private val itemTypes = arrayOf("무기", "방어구")
    private val jobTypes = arrayOf("전사", "궁수", "마법사", "도적")

    private val itemTypeComboBox = JComboBox(itemTypes)
    private val jobTypeComboBox = JComboBox(jobTypes)
    private val levelTextField = JTextField(10)

    // 아이템 종류와 직업군 및 레벨을 저장할 변수
    private var selectedItemType: String? = null
    private var selectedJobType: String? = null
    private var selectedLevel: Int? = null

    init {
        title = "아이템 강화"
        defaultCloseOperation = EXIT_ON_CLOSE
        setSize(800, 600)
        setLocationRelativeTo(null)
        layout = GridLayout(4, 2)

        add(JLabel("아이템 종류:"))
        add(itemTypeComboBox)
        add(JLabel("착용 직업군:"))
        add(jobTypeComboBox)
        add(JLabel("착용 레벨(1-300):"))
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
                isVisible = false

                if (selectedLevel != null && selectedLevel in 1..300) {
                    // 여기서 강화 시작하는 로직을 넣으세요
                    val enhancingUI = EnhancingUI(selectedItemType, selectedJobType, selectedLevel)
                    enhancingUI.isVisible = true
                } else {
                    JOptionPane.showMessageDialog(this, "올바른 착용 레벨을 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE)
                }
            }
            "뒤로 가기" -> {
                val startUI = StartUI()
                startUI.isVisible = true
                isVisible = false // 현재 화면을 숨김
            }
        }
    }
    // 선택된 아이템 종류를 반환하는 메서드
    fun getSelectedItemType(): String? {
        return selectedItemType
    }

    // 선택된 직업군을 반환하는 메서드
    fun getSelectedJobType(): String? {
        return selectedJobType
    }

    // 선택된 레벨을 반환하는 메서드
    fun getSelectedLevel(): Int? {
        return selectedLevel
    }
}

fun main() {
    SwingUtilities.invokeLater {
        val itemSelectingUI = ItemSelectingUI()
        itemSelectingUI.isVisible = true
    }
}