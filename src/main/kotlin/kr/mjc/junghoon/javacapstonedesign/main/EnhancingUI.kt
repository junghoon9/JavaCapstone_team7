package kr.mjc.junghoon.javacapstonedesign.main

import javax.swing.*
import java.awt.*
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent

class EnhancingUI(private val itemType: String?,
                  private val jobType: String?,
                  private val itemLevel: Int?,
                  private val logic: Logic
) : JFrame() {
    private val startButton = JButton("강화 시작")
    private val endButton = JButton("강화 종료")
    val miniGameCheckBox = JCheckBox("미니게임 실행")
    private var enhancingInfoLabel = JLabel()

    fun updateEnhancingInfo(logic: Logic) {
        // logic 객체에서 강화 정보 가져오기
        val ehcLevel = logic.enhancingLevel
        val statN = logic.statName
        val statInc = logic.statIncrease
        val powerOrDefenceN = logic.powerOrDefenceName
        val powerInc = logic.powerIncrease
        val defInc = logic.defIncrease
        val sChance = logic.getSuccessChance()
        val fChance = logic.getFailureChance()
        val cost = logic.getCost()

        // 강화 정보 업데이트
        val enhancingInfoText = when (itemType) {
            "무기" -> {
                "<html>" +
                "$ehcLevel 강 -> ${ehcLevel + 1} 강<br><br>" +
                "$statN: +$statInc<br>" +
                "$powerOrDefenceN: +$powerInc<br><br>" +
                "성공확률: $sChance %<br>" +
                "실패확률: $fChance %<br><br>" +
                "강화비용: $cost 골드<br>" +
                "</html>"
            }
            else -> { //방어구
                "<html>" +
                "$ehcLevel 강 -> ${ehcLevel + 1} 강<br><br>" +
                "$statN: +$statInc<br>" +
                "$powerOrDefenceN: +$defInc<br><br>" +
                "성공확률: $sChance %<br>" +
                "실패확률: $fChance %<br><br>" +
                "강화비용: $cost 골드<br>" +
                "</html>"
            }
        }

        // 강화 정보 레이블 업데이트
        enhancingInfoLabel.text = enhancingInfoText

        // 최대 강화차수에 도달했는지 확인
        if (logic.enhancingLevel >= logic.maxEnhancingLevel) {
            val finishUI = FinishUI(logic)
            finishUI.isVisible = true
            this@EnhancingUI.dispose() // 현재 창 닫기
            return
        }
    }



    init {
        title = "아이템 강화"
        defaultCloseOperation = EXIT_ON_CLOSE
        setSize(800, 600)
        setLocationRelativeTo(null)
        layout = BorderLayout()

        val topPanel = JPanel()
        val titleLabel = JLabel("$jobType $itemType 강화")
        titleLabel.font = Font("SansSerif", Font.BOLD, 24)
        topPanel.add(titleLabel)

        add(topPanel, BorderLayout.NORTH)

        // 좌측에 아이템 종류와 착용 직업군에 따라 다른 이미지를 표시할 패널
        val imagePanel = JPanel()
        val itemLabel = JLabel()
        val imagePath = when (itemType) {
            "무기" -> when (jobType) {
                "전사" -> "/images/weapon/warrior_weapon.png"
                "궁수" -> "/images/weapon/archer_weapon.png"
                "마법사" -> "/images/weapon/magician_weapon.png"
                else -> "/images/weapon/thief_weapon.png" //도적
            }
            else -> when (jobType) {
                "전사" -> "/images/armor/warrior_armor.png"
                "궁수" -> "/images/armor/archer_armor.png"
                "마법사" -> "/images/armor/magician_armor.png"
                else -> "/images/armor/thief_armor.png" //도적
            }
        }
        val imageURL = javaClass.getResource(imagePath)
        val originalImageIcon = ImageIcon(imageURL)

        // 이미지 크기 조절
        val scaledImage = originalImageIcon.image.getScaledInstance(350, 350, Image.SCALE_SMOOTH)
        val scaledImageIcon = ImageIcon(scaledImage)

        // JLabel()에 이미지 설정
        itemLabel.icon = scaledImageIcon
        itemLabel.verticalAlignment = SwingConstants.CENTER
        imagePanel.add(itemLabel)


        // 우측에 강화 정보를 표시할 빈 영역
        val enhancingInfoPanel = JPanel()
        val ehcLevel = logic.enhancingLevel
        val statN = logic.statName
        val statInc = logic.statIncrease
        val powerOrDefenceN = logic.powerOrDefenceName
        val powerInc = logic.powerIncrease
        val defInc = logic.defIncrease
        val sChance = logic.getSuccessChance()
        val fChance = logic.getFailureChance()
        val cost = logic.getCost()
        val enhancingInfoText = when (itemType) {
            "무기" -> {
                "<html>" +
                "$ehcLevel 강 -> ${ehcLevel + 1} 강<br><br>" +
                "$statN: +$statInc<br>" +
                "$powerOrDefenceN: +$powerInc<br><br>" +
                "성공확률: $sChance %<br>" +
                "실패확률: $fChance %<br><br>" +
                "강화비용: $cost 골드<br>" +
                "</html>"
            }
            else -> { // 방어구
                "<html>" +
                "$ehcLevel 강 -> ${ehcLevel + 1} 강<br><br>" +
                "$statN: +$statInc<br>" +
                "$powerOrDefenceN: +$defInc<br><br>" +
                "성공확률: $sChance %<br>" +
                "실패확률: $fChance %<br><br>" +
                "강화비용: $cost 골드<br>" +
                "</html>"
            }
        }
        val font = Font("Arial", Font.PLAIN, 30)
        enhancingInfoLabel = JLabel(enhancingInfoText)
        enhancingInfoLabel.font = font

        enhancingInfoPanel.add(enhancingInfoLabel)
        enhancingInfoPanel.add(miniGameCheckBox, BorderLayout.SOUTH)


        // 하단에 강화 시작 버튼과 강화 종료 버튼을 추가할 패널
        val buttonPanel = JPanel()

        // 강화 시작 버튼
        buttonPanel.add(startButton)
        startButton.addActionListener{
            if (itemType != null && jobType != null && itemLevel != null) {
                if (miniGameCheckBox.isSelected) {
                    val miniGame = MiniGame(this@EnhancingUI, logic)
                    miniGame.isVisible = true
                    this@EnhancingUI.isEnabled = false
                    miniGame.addWindowListener(object : WindowAdapter() {
                        override fun windowClosed(e: WindowEvent?) {
                            this@EnhancingUI.isEnabled = true
                        }
                    })
                }
                else {
                    logic.simulateItem()
                    this@EnhancingUI.updateEnhancingInfo(logic)
                }
            }
        }

        // 강화 종료 버튼
        buttonPanel.add(endButton)
        endButton.addActionListener{
            if (itemType != null && jobType != null && itemLevel != null) {
                val finishUI = FinishUI(logic)
                finishUI.isVisible = true
                isVisible = false
            }
        }

        add(imagePanel, BorderLayout.WEST)
        add(enhancingInfoPanel, BorderLayout.CENTER)
        add(buttonPanel, BorderLayout.SOUTH)
    }
}

fun main() {
    SwingUtilities.invokeLater {
        val itemSelectingUI = ItemSelectingUI()
        itemSelectingUI.isVisible = false

        // ItemSelectingUI 클래스에서 선택한 아이템 종류와 직업군을 가져옴
        val it = itemSelectingUI.selectedItemType
        val jt = itemSelectingUI.selectedJobType
        val il = itemSelectingUI.selectedLevel
        val lg = Logic(it, jt, il)

        // EnhancingUI 클래스의 인스턴스를 생성할 때 선택한 아이템 종류와 직업군을 전달
        val enhancingUI = EnhancingUI(it, jt, il, lg)
        enhancingUI.isVisible = true
    }
}