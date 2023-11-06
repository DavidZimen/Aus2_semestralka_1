package sk.zimen.semestralka.ui.add_edit_controllers

import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane
import sk.zimen.semestralka.Aus2Semestralka
import sk.zimen.semestralka.api.types.GpsPosition
import sk.zimen.semestralka.api.types.HeightPos
import sk.zimen.semestralka.api.types.Place
import sk.zimen.semestralka.api.types.WidthPos
import sk.zimen.semestralka.ui.state.AbstractState
import java.net.URL
import java.util.*

abstract class AbstractAddEditController<T : Place> : Initializable {

    protected lateinit var state: AbstractState<T>
    protected var editBefore: T? = null
    protected lateinit var type: String
    protected var associatedProperties = FXCollections.observableArrayList<Place>()!!
    @FXML
    protected lateinit var borderPane: BorderPane
    @FXML
    protected lateinit var number: TextField
    @FXML
    protected lateinit var desc: TextArea
    @FXML
    protected lateinit var widthTop: TextField
    @FXML
    protected lateinit var widthBottom: TextField
    @FXML
    protected lateinit var heightTop: TextField
    @FXML
    protected lateinit var heightBottom: TextField
    @FXML
    protected lateinit var sPos: RadioButton
    @FXML
    protected lateinit var jPos: RadioButton
    @FXML
    protected lateinit var zPos: RadioButton
    @FXML
    protected lateinit var vPos: RadioButton
    @FXML
    protected lateinit var sPosBottom: RadioButton
    @FXML
    protected lateinit var jPosBottom: RadioButton
    @FXML
    protected lateinit var zPosBottom: RadioButton
    @FXML
    protected lateinit var vPosBottom: RadioButton
    @FXML
    protected lateinit var header: Label
    @FXML
    protected lateinit var numberCol: TableColumn<Place, Int>
    @FXML
    protected lateinit var descCol: TableColumn<Place, String>
    @FXML
    protected lateinit var associatedTable: TableView<Place>
    @FXML
    protected lateinit var label: Label

    abstract fun onSave()

    abstract fun onCancel()

    abstract fun initState()

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        initState()
        editBefore = state.editItem
        if (editBefore == null) header.text = "New" else header.text = "Editing"
        editBefore?.also {
            number.text = it.number.toString()
            desc.text = it.description
            with(it.positions.topLeft) {
                widthTop.text = width.toString()
                zPos.isSelected = widthPosition == WidthPos.Z
                vPos.isSelected = widthPosition == WidthPos.V
                heightTop.text = height.toString()
                sPos.isSelected = heightPosition == HeightPos.S
                jPos.isSelected = heightPosition == HeightPos.J
            }
            with(it.positions.bottomRight) {
                widthBottom.text = width.toString()
                zPosBottom.isSelected = widthPosition == WidthPos.Z
                vPosBottom.isSelected = widthPosition == WidthPos.V
                heightBottom.text = height.toString()
                sPosBottom.isSelected = heightPosition == HeightPos.S
                jPosBottom.isSelected = heightPosition == HeightPos.J
            }
        }
        descCol.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.description) }
        numberCol.cellValueFactory = PropertyValueFactory("number")
        associatedTable.items = associatedProperties
        if (state.editItem == null) {
            label.isVisible = false
        }
    }

    protected fun getGpsPosition(top: Boolean): GpsPosition {
        return if (top) {
            GpsPosition(
                widthTop.text.toDouble(),
                if (zPos.isSelected) WidthPos.Z else WidthPos.V,
                heightTop.text.toDouble(),
                if (sPos.isSelected) HeightPos.S else HeightPos.J
            )
        } else {
            GpsPosition(
                widthBottom.text.toDouble(),
                if (zPosBottom.isSelected) WidthPos.Z else WidthPos.V,
                heightBottom.text.toDouble(),
                if (sPosBottom.isSelected) HeightPos.S else HeightPos.J
            )
        }
    }

    protected fun showSuccessAlert(add: Boolean) {
        val alert = Alert(Alert.AlertType.NONE, "Success", ButtonType.OK)
        val suffix = if (add) "added" else "edited"
        alert.isResizable = false
        alert.headerText = "Item with provided attributes successfully $suffix."
        alert.showAndWait()
        if (alert.result == ButtonType.OK) onCancel()
    }

    protected fun showErrorAlert(add: Boolean) {
        val alert = Alert(Alert.AlertType.ERROR, "Error")
        val suffix = if (add) "added" else "edited"
        alert.isResizable = false
        alert.headerText = "Item with provided attributes could not be $suffix. Please try again later."
        alert.showAndWait()
    }

    protected fun navigate(path: String) {
        val loader = FXMLLoader(Aus2Semestralka::class.java.getResource(path))
        borderPane.children.setAll(Pane(loader.load()))
    }
}