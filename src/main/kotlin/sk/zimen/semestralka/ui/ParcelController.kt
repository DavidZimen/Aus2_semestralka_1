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
import sk.zimen.semestralka.api.service.ParcelService
import sk.zimen.semestralka.api.types.GpsPosition
import sk.zimen.semestralka.api.types.HeightPos
import sk.zimen.semestralka.api.types.Parcel
import sk.zimen.semestralka.api.types.WidthPos
import java.net.URL
import java.util.*

class ParcelController : Initializable {

    private val parcelService = ParcelService.getInstance()
    private var parcels = FXCollections.observableArrayList<Parcel>()
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
    private lateinit var parcelsTable: TableView<Parcel>
    @FXML
    private lateinit var bottomHeightPos: TableColumn<Parcel, String>
    @FXML
    private lateinit var bottomHeightValue: TableColumn<Parcel, String>
    @FXML
    private lateinit var number: TableColumn<Parcel, Double>
    @FXML
    private lateinit var desc: TableColumn<Parcel, String>
    @FXML
    private lateinit var topWidthValue: TableColumn<Parcel, String>
    @FXML
    private lateinit var topWidthPos: TableColumn<Parcel, String>
    @FXML
    private lateinit var topHeightValue: TableColumn<Parcel, String>
    @FXML
    private lateinit var topHeightPos: TableColumn<Parcel, String>
    @FXML
    private lateinit var bottomWidthValue: TableColumn<Parcel, String>
    @FXML
    private lateinit var bottomWidthPos: TableColumn<Parcel, String>
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

    fun searchParcels() {
        parcels = FXCollections.observableArrayList(
            parcelService.find(
                GpsPosition(
                width.text.toDouble(),
                if (zPos.isSelected) WidthPos.Z else WidthPos.V,
                height.text.toDouble(),
                if (sPos.isSelected) HeightPos.S else HeightPos.J
            )
            ))

        parcelsTable.items = parcels
    }

    fun openAddParcel() {
        navigate("add-parcel.fxml")
    }

    fun onCancel() {
        navigate("parcels.fxml")
    }

    fun onSave() {
        parcelService.add(
            Parcel(
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
        alert.headerText = "Parcel with provided attributes successfully added."
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

    private fun navigate(path: String, search: Boolean = false) {
        val loader = FXMLLoader(Aus2Semestralka::class.java.getResource(path))
        loader.setController(this)
        borderPane.children.setAll(Pane(loader.load()))
        if (search) searchParcels()
    }
}