package kr.mjc.junghoon.javacapstonedesign

import javax.swing.*
import java.awt.*
import java.awt.event.*

class MiniGame(private val enhancingUI: EnhancingUI, private val logic: Logic) : JFrame() {

    private val countdownLabel = JLabel("5")
    private var countdown = 5
    private var timer: Timer? = null
    private var correctCount = 0

    init {
        title = "Mini Game"
        defaultCloseOperation = EXIT_ON_CLOSE
        setSize(600, 400)
        layout = BorderLayout()

        val mainPanel = JPanel(BorderLayout())

        // Countdown label
        countdownLabel.font = Font("Arial", Font.BOLD, 36)
        countdownLabel.foreground = Color.RED
        countdownLabel.horizontalAlignment = SwingConstants.CENTER
        mainPanel.add(countdownLabel, BorderLayout.NORTH)

        // Display random arrow images in a row
        val arrowPanel = JPanel(GridLayout(1, 4, 20, 0)) // 20 pixels horizontal gap
        val arrowImages = listOf(
            "src/main/resources/images/arrow/up.png",
            "src/main/resources/images/arrow/down.png",
            "src/main/resources/images/arrow/left.png",
            "src/main/resources/images/arrow/right.png"
        )

        val random = java.util.Random()
        for (i in 0 until 4) {
            val randomIndex = random.nextInt(arrowImages.size)
            val randomArrowImage = arrowImages[randomIndex]
            val arrowLabel = JLabel(ImageIcon(randomArrowImage))
            arrowLabel.alignmentX = Component.CENTER_ALIGNMENT
            arrowPanel.add(arrowLabel)
        }
        mainPanel.add(arrowPanel, BorderLayout.CENTER)

        contentPane.add(mainPanel)

        // Timer for countdown
        timer = Timer(1000) {
            countdown--
            countdownLabel.text = countdown.toString()
            if (countdown == 0) {
                timer?.stop()
                startEnhancing()
                dispose() // 프레임을 닫음
            }
        }
        timer?.start()

        // Key bindings
        mainPanel.isFocusable = true
        mainPanel.requestFocus() // 포커스 설정

        val inputMap = mainPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
        val actionMap = mainPanel.actionMap

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "UP")
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "DOWN")
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "LEFT")
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "RIGHT")

        actionMap.put("UP", object : AbstractAction() {
            override fun actionPerformed(e: ActionEvent) {
                handleKeyPress("src/main/resources/images/arrow/up.png")
            }
        })
        actionMap.put("DOWN", object : AbstractAction() {
            override fun actionPerformed(e: ActionEvent) {
                handleKeyPress("src/main/resources/images/arrow/down.png")
            }
        })
        actionMap.put("LEFT", object : AbstractAction() {
            override fun actionPerformed(e: ActionEvent) {
                handleKeyPress("src/main/resources/images/arrow/left.png")
            }
        })
        actionMap.put("RIGHT", object : AbstractAction() {
            override fun actionPerformed(e: ActionEvent) {
                handleKeyPress("src/main/resources/images/arrow/right.png")
            }
        })
    }

    private fun startEnhancing() {
        logic.updateSuccessChance(correctCount)
        logic.simulateItem()
        enhancingUI.updateEnhancingInfo(logic)
        enhancingUI.miniGameCheckBox.isSelected = false
    }

    private fun handleKeyPress(direction: String) {
        val arrowPanel = (contentPane.components.find { it is JPanel } as? JPanel)?.
        components?.getOrNull(1) as? JPanel
        val arrowLabels = arrowPanel?.components?.mapNotNull { it as? JLabel }
        if (arrowLabels != null && arrowLabels.size > correctCount) {
            val arrowLabel = arrowLabels[correctCount]
            if (direction == arrowLabel.icon.toString()) {
                arrowLabel.isVisible = false
                correctCount++
                if (correctCount == 4) {
                    timer?.stop()
                    startEnhancing()
                    dispose()
                }
            }
        }
    }
}
