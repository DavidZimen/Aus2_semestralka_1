package sk.zimen.semestralka.ui.add_edit_controllers

import javafx.collections.FXCollections
import sk.zimen.semestralka.api.service.PropertyService
import sk.zimen.semestralka.api.types.Place
import sk.zimen.semestralka.api.types.Property
import sk.zimen.semestralka.ui.state.PropertyState

class AddEditPropertyController : AbstractAddEditController<Property>() {

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
        if (state.editItem != null) {
            associatedItems = FXCollections.observableArrayList(state.editItem?.parcelsForProperty as List<Place>)
        }
    }
}