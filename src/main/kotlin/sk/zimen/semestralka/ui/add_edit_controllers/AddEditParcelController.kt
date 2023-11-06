package sk.zimen.semestralka.ui.add_edit_controllers

import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.scene.control.cell.PropertyValueFactory
import sk.zimen.semestralka.api.service.ParcelService
import sk.zimen.semestralka.api.types.Parcel
import sk.zimen.semestralka.api.types.Property
import sk.zimen.semestralka.ui.state.ParcelState

class AddEditParcelController : AbstractAddEditController<Parcel>() {

    private val parcelService = ParcelService.getInstance()
    private var associatedProperties = FXCollections.observableArrayList<Property>()!!

    override fun onSave() {
        if (editBefore == null) {
            try {
                parcelService.add(
                    Parcel(number.text.toInt(), desc.text, getGpsPosition(true), getGpsPosition(false))
                )
                showSuccessAlert(true)
            } catch (e: Exception) {
                showErrorAlert(true)
            }
        } else {
            try {
                parcelService.edit(
                    editBefore!!,
                    Parcel(number.text.toInt(), desc.text, getGpsPosition(true), getGpsPosition(false))
                )
                showSuccessAlert(false)
            } catch (e: Exception) {
                showErrorAlert(false)
            }
        }
    }

    override fun onCancel() {
        navigate("parcels.fxml")
    }

    override fun initState() {
        state = ParcelState.getInstance()
        descCol.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.description) }
        numberCol.cellValueFactory = PropertyValueFactory("number")
        if (state.editItem == null) {
            associatedTable.isVisible = false
            label.isVisible = false
        } else {
            associatedProperties = FXCollections.observableArrayList(state.editItem?.propertiesForParcel)
        }
    }
}