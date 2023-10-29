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
        borderPane.children.setAll(Pane(FXMLLoader(Aus2Semestralka::class.java.getResource("add-property.fxml")).load()))
    }

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

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        if (p0?.path?.contains("add") == true) return
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
    }
}