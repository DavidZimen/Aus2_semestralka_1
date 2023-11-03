package sk.zimen.semestralka

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage
import sk.zimen.semestralka.api.types.Parcel
import sk.zimen.semestralka.quadtree.boundary.Boundary
import sk.zimen.semestralka.utils.CsvUtils
import sk.zimen.semestralka.utils.Generator

class Aus2Semestralka : Application() {
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(Aus2Semestralka::class.java.getResource("main-page.fxml"))
        val scene = Scene(fxmlLoader.load())
        stage.title = "Semestrálna práca AUS2"
        stage.icons.add(Image(Aus2Semestralka::class.java.getResourceAsStream("icons/SYMBOL_T_biela.png")))
        stage.scene = scene
        stage.show()

        val parcels = Generator().generatePlaceItems(Parcel::class, Boundary(doubleArrayOf(3.0, 5.0), doubleArrayOf(5.0, 3.0)), 1)
        CsvUtils.writeDataToCSV("parcels.csv", Parcel::class, parcels)
    }
}

fun main() {
    Application.launch(Aus2Semestralka::class.java)
}