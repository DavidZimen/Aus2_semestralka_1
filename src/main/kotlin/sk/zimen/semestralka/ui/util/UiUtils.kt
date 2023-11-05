package sk.zimen.semestralka.ui.util

import javafx.fxml.FXMLLoader
import javafx.scene.control.*
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Pane
import sk.zimen.semestralka.Aus2Semestralka
import sk.zimen.semestralka.utils.DoubleUtils

fun <T : Pane> loadFile(name: String): T {
    val loader = FXMLLoader(Aus2Semestralka::class.java.getResource(name))
    return loader.load()
}

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

fun ProgressIndicator.showSpinner(show: Boolean, label: Label? = null) {
    isVisible = show
    label?.isVisible = show
}

fun Button.disable(disable: Boolean = true) {
    isDisable = disable
}

fun TextField.disable(disable: Boolean = true) {
    isDisable = disable
}

fun Tab.setContent(path: String) {
    content = loadFile<AnchorPane>(path)
}