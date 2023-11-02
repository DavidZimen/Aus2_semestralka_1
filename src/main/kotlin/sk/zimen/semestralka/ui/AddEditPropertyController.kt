package sk.zimen.semestralka.ui

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane
import sk.zimen.semestralka.Aus2Semestralka
import sk.zimen.semestralka.api.service.PropertyService
import sk.zimen.semestralka.api.types.GpsPosition
import sk.zimen.semestralka.api.types.HeightPos
import sk.zimen.semestralka.api.types.Property
import sk.zimen.semestralka.api.types.WidthPos

class AddEditPropertyController {

    private val propertyService = PropertyService.getInstance()
    @FXML
    private lateinit var borderPane: BorderPane
    @FXML
    private lateinit var addNumber: TextField
    @FXML
    private lateinit var addWidth: TextField
    @FXML
    private lateinit var addHeight: TextField
    @FXML
    private lateinit var addSPos: RadioButton
    @FXML
    private lateinit var addZPos: RadioButton
    @FXML
    private lateinit var addDesc: TextArea
    @FXML
    private lateinit var addSPos1: RadioButton
    @FXML
    private lateinit var addHeightBottom: TextField
    @FXML
    private lateinit var addZPos1: RadioButton
    @FXML
    private lateinit var addWidthBottom: TextField

    fun onCancel() {
        borderPane.children.setAll(Pane(FXMLLoader(Aus2Semestralka::class.java.getResource("properties.fxml")).load()))
    }

    fun onSave() {
        propertyService.add(
            Property(
                addNumber.text.toInt(),
                addDesc.text,
                GpsPosition(
                    addWidth.text.toDouble(),
                    if (addZPos.isSelected) WidthPos.Z else WidthPos.V,
                    addHeight.text.toDouble(),
                    if (addSPos.isSelected) HeightPos.S else HeightPos.J
                ),
                GpsPosition(
                    addWidthBottom.text.toDouble(),
                    if (addZPos1.isSelected) WidthPos.Z else WidthPos.V,
                    addHeightBottom.text.toDouble(),
                    if (addSPos1.isSelected) HeightPos.S else HeightPos.J
                ),
            )
        )
        val alert = Alert(Alert.AlertType.NONE, "Success", ButtonType.OK)
        alert.isResizable = false
        alert.title = "Success"
        alert.headerText = "Property with provided attributes successfully added."
        alert.showAndWait()
        if (alert.result == ButtonType.OK) onCancel()
    }
}