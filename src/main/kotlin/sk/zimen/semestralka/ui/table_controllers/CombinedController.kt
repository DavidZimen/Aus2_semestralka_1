package sk.zimen.semestralka.ui.table_controllers

import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.TableColumn
import sk.zimen.semestralka.api.service.CombinedService
import sk.zimen.semestralka.api.types.*
import java.net.URL
import java.util.*

class CombinedController : Initializable, AbstractTableController<Place>() {

    private val combinedService = CombinedService.getInstance()
    @FXML
    private lateinit var type: TableColumn<Place, String>
    
    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        super.initialize(p0, p1)
        type.setCellValueFactory { cellData -> SimpleStringProperty(if (cellData.value is Property) Property::class.simpleName else Parcel::class.simpleName) }
    }

    override fun search() {
        tableItems = FXCollections.observableArrayList(
            combinedService.find(
                GpsPosition(
                width.text.toDouble(),
                if (zPos.isSelected) WidthPos.Z else WidthPos.V,
                height.text.toDouble(),
                if (sPos.isSelected) HeightPos.S else HeightPos.J
            )
            ))

        table.items = tableItems
    }

    override fun onEdit() {
        throw NotImplementedError("Will not be used in this component")
    }

    override fun onAdd() {
        throw NotImplementedError("Will not be used in this component")
    }

    override fun initState() { }

    override fun deleteFromService(item: Place) {
        throw NotImplementedError("Will not be used in this component")
    }
}