package sk.zimen.semestralka.ui.add_edit_controllers

import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.scene.control.cell.PropertyValueFactory
import sk.zimen.semestralka.api.service.PropertyService
import sk.zimen.semestralka.api.types.Parcel
import sk.zimen.semestralka.api.types.Property
import sk.zimen.semestralka.ui.state.PropertyState

class AddEditPropertyController : AbstractAddEditController<Property, Parcel>() {

    private val propertyService = PropertyService.getInstance()

    override fun onSave() {
        if (editBefore == null) {
            try {
                propertyService.add(
                    Property(number.text.toInt(), desc.text, getGpsPosition(true), getGpsPosition(false))
                )
                showSuccessAlert(true)
            } catch (e: Exception) {
                showErrorAlert(true)
            }
        } else {
            try {
                propertyService.edit(
                    editBefore!!,
                    Property(number.text.toInt(), desc.text, getGpsPosition(true), getGpsPosition(false))
                )
                showSuccessAlert(false)
            } catch (e: Exception) {
                showErrorAlert(false)
            }
        }
    }

    override fun onCancel() {
        navigate("properties.fxml")
    }

    override fun initState() {
        state = PropertyState.getInstance()
        descCol.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.description) }
        numberCol.cellValueFactory = PropertyValueFactory("number")
        if (state.editItem == null) {
            associatedTable.isVisible = false
        } else {
            associatedItems = FXCollections.observableArrayList(state.editItem?.parcelsForProperty)
        }
    }
}