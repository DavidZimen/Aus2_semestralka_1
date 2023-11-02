package sk.zimen.semestralka.ui.table_controllers

import javafx.collections.FXCollections
import javafx.fxml.Initializable
import sk.zimen.semestralka.api.service.PropertyService
import sk.zimen.semestralka.api.types.GpsPosition
import sk.zimen.semestralka.api.types.Property
import sk.zimen.semestralka.ui.state.PropertyState

class PropertiesController : Initializable, AbstractTableController<Property>() {

    private val propertyService = PropertyService.getInstance()
    private val propertyState = PropertyState.getInstance()

    override fun search() {
        newSearchState()
        tableItems = propertyState.searchBar?.let { searchState ->
            with(searchState) {
                FXCollections.observableArrayList(
                    propertyService.find(GpsPosition(width, wPos, height, hPos))
                )
            }
        }

        table.items = tableItems
    }

    override fun onAdd() {
        propertyState.newEdit(null)
        navigate("add-edit-property.fxml")
    }

    override fun initState() {
        state = PropertyState.getInstance()
        super.initState()
    }

    override fun onEdit() {
        propertyState.newEdit(table.selectionModel.selectedItem)
        navigate("add-edit-property.fxml")
    }

    override fun deleteFromService(item: Property) {
        propertyService.delete(item)
    }
}