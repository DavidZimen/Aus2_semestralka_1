package sk.zimen.semestralka.ui.util

import javafx.scene.control.TextField
import sk.zimen.semestralka.utils.DoubleUtils

fun TextField.allowOnlyDouble() {
    textProperty().addListener { _, _, newValue ->
        if (!newValue.matches(DoubleUtils.REGEX)) {
            text = newValue.replace(Regex("[^0-9.]"), "")
        }
    }
}

fun TextField.allowOnlyInt() {
    textProperty().addListener { _, _, newValue ->
        if (!newValue.matches(DoubleUtils.INTEGER_REGEX)) {
            text = newValue.replace(Regex("[^0-9]"), "")
        }
    }
}