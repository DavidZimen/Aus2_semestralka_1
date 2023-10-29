package sk.zimen.semestralka.ui

import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.RadioButton
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.control.cell.PropertyValueFactory
import sk.zimen.semestralka.api.service.CombinedService
import sk.zimen.semestralka.api.service.ParcelService
import sk.zimen.semestralka.api.types.*
import java.net.URL
import java.util.*

class CombinedController : Initializable {

    private val combinedService = CombinedService.getInstance()
    private var places = FXCollections.observableArrayList<Place>()
    @FXML
    private lateinit var width: TextField
    @FXML
    private lateinit var height: TextField
    @FXML
    private lateinit var zPos: RadioButton
    @FXML
    private lateinit var sPos: RadioButton
    @FXML
    private lateinit var placesTable: TableView<Place>
    @FXML
    private lateinit var bottomHeightPos: TableColumn<Place, String>
    @FXML
    private lateinit var bottomHeightValue: TableColumn<Place, String>
    @FXML
    private lateinit var number: TableColumn<Place, Double>
    @FXML
    private lateinit var desc: TableColumn<Place, String>
    @FXML
    private lateinit var topWidthValue: TableColumn<Place, String>
    @FXML
    private lateinit var topWidthPos: TableColumn<Place, String>
    @FXML
    private lateinit var topHeightValue: TableColumn<Place, String>
    @FXML
    private lateinit var topHeightPos: TableColumn<Place, String>
    @FXML
    private lateinit var bottomWidthValue: TableColumn<Place, String>
    @FXML
    private lateinit var bottomWidthPos: TableColumn<Place, String>
    @FXML
    private lateinit var type: TableColumn<Place, String>
    
    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        ParcelService.getInstance()
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
        type.setCellValueFactory { cellData -> SimpleStringProperty(if (cellData.value is Property) Property::class.simpleName else Parcel::class.simpleName) }
    }

    fun searchPlaces() {
        places = FXCollections.observableArrayList(
            combinedService.find(
                GpsPosition(
                width.text.toDouble(),
                if (zPos.isSelected) WidthPos.Z else WidthPos.V,
                height.text.toDouble(),
                if (sPos.isSelected) HeightPos.S else HeightPos.J
            )
            ))

        placesTable.items = places
    }
}