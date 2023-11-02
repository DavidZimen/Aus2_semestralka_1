package sk.zimen.semestralka.ui

import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.control.RadioButton
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextField
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
        navigate("add-edit-parcel.fxml")
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
        borderPane.children.setAll(Pane(loader.load()))
        if (search) searchParcels()
    }
}