package kr.mjc.junghoon.javacapstonedesign

import kr.mjc.junghoon.javacapstonedesign.main.StartBeforeLoginUI
import javax.swing.SwingUtilities

fun main() {
    SwingUtilities.invokeLater {
        val startBeforeLoginUI = StartBeforeLoginUI()
        startBeforeLoginUI.isVisible = true
    }
}