package sk.zimen.semestralka.ui

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
import sk.zimen.semestralka.api.service.PropertyService
import sk.zimen.semestralka.api.types.GpsPosition
import sk.zimen.semestralka.api.types.HeightPos
import sk.zimen.semestralka.api.types.Property
import sk.zimen.semestralka.api.types.WidthPos
import java.net.URL
import java.util.*

class PropertiesController : Initializable {

    private val propertyService = PropertyService.getInstance()
    private var properties = FXCollections.observableArrayList<Property>()
    @FXML
    private lateinit var borderPane: BorderPane
    @FXML
    private lateinit var deleteProperty: Button
    @FXML
    private lateinit var editProperty: Button
    @FXML
    private lateinit var width: TextField
    @FXML
    private lateinit var height: TextField
    @FXML
    private lateinit var zPos: RadioButton
    @FXML
    private lateinit var sPos: RadioButton
    @FXML
    private lateinit var propertiesTable: TableView<Property>
    @FXML
    private lateinit var bottomHeightPos: TableColumn<Property, String>
    @FXML
    private lateinit var bottomHeightValue: TableColumn<Property, String>
    @FXML
    private lateinit var number: TableColumn<Property, Double>
    @FXML
    private lateinit var desc: TableColumn<Property, String>
    @FXML
    private lateinit var topWidthValue: TableColumn<Property, String>
    @FXML
    private lateinit var topWidthPos: TableColumn<Property, String>
    @FXML
    private lateinit var topHeightValue: TableColumn<Property, String>
    @FXML
    private lateinit var topHeightPos: TableColumn<Property, String>
    @FXML
    private lateinit var bottomWidthValue: TableColumn<Property, String>
    @FXML
    private lateinit var bottomWidthPos: TableColumn<Property, String>

    fun searchProperties() {
        properties = FXCollections.observableArrayList(
            propertyService.find(GpsPosition(
                width.text.toDouble(),
                if (zPos.isSelected) WidthPos.Z else WidthPos.V,
                height.text.toDouble(),
                if (sPos.isSelected) HeightPos.S else HeightPos.J
            )
        ))

        propertiesTable.items = properties
    }

    fun openAddProperty() {
        borderPane.children.setAll(Pane(FXMLLoader(Aus2Semestralka::class.java.getResource("add-edit-property.fxml")).load()))
    }

    fun onEdit() {

    }

    fun onDelete() {
        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.isResizable = false
        alert.title = "Property delete"
        alert.headerText = "Selected property will be deleted. Are you sure you want to continue ?"
        alert.showAndWait()
        if (alert.result == ButtonType.OK) {
            val delProperty = propertiesTable.selectionModel.selectedItem
            propertyService.delete(delProperty)
            propertiesTable.items.remove(delProperty)
        } else {
            alert.close()
        }
    }

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        if (p0?.path?.contains("add") == true) return
        propertiesTable.selectionModel.selectionMode = SelectionMode.SINGLE
        desc.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.description) }
        number.cellValueFactory = PropertyValueFactory("number")
        topWidthValue.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.key.topLeft.width.toString()) }
        topWidthPos.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.key.topLeft.widthPosition.toString()) }
        topHeightValue.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.key.topLeft.height.toString()) }
        topHeightPos.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.key.topLeft.heightPosition.toString()) }
        bottomWidthValue.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.key.bottomRight.width.toString()) }
        bottomWidthPos.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.key.bottomRight.widthPosition.toString()) }
        bottomHeightValue.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.key.bottomRight.height.toString()) }
        bottomHeightPos.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.key.bottomRight.heightPosition.toString()) }
        deleteProperty.isVisible = false
        editProperty.isVisible = false

        //initialize visibility of delete and edit button
        propertiesTable.selectionModel.selectedItemProperty().addListener { _, _, newSelection ->
            deleteProperty.isVisible = newSelection != null
            editProperty.isVisible = newSelection != null
        }

        // unselect item from table
//        propertiesTable.setOnMouseClicked { event ->
//            if (event.clickCount == 1) {
//                val index: Int = propertiesTable.selectionModel.selectedIndex
//                if (index >= 0) {
//                    if (propertiesTable.selectionModel.isSelected(index)) {
//                        propertiesTable.selectionModel.clearSelection(index);
//                    } else {
//                        propertiesTable.selectionModel.select(index);
//                    }
//                }
//            }
//        }
    }
}