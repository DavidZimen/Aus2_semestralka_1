package sk.zimen.semestralka

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage
import sk.zimen.semestralka.utils.CsvUtils

class Aus2Semestralka : Application() {
    override fun start(stage: Stage) {
        CsvUtils.initialize()
        val fxmlLoader = FXMLLoader(Aus2Semestralka::class.java.getResource("main-page.fxml"))
        val scene = Scene(fxmlLoader.load())
        stage.title = "Semestrálna práca AUS2"
        stage.icons.add(Image(Aus2Semestralka::class.java.getResourceAsStream("icons/SYMBOL_T_biela.png")))
        stage.scene = scene
        stage.show()
    }
}

fun main() {
    Application.launch(Aus2Semestralka::class.java)
}