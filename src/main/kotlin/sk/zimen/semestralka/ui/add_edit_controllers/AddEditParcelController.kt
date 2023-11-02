package sk.zimen.semestralka.ui.add_edit_controllers

import sk.zimen.semestralka.api.service.ParcelService
import sk.zimen.semestralka.api.types.Parcel
import sk.zimen.semestralka.ui.state.ParcelState

class AddEditParcelController : AbstractAddEditController<Parcel>() {

    private val parcelService = ParcelService.getInstance()

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
                println(e)
                showErrorAlert(false)
            }
        }
    }

    override fun onCancel() {
        navigate("parcels.fxml")
    }

    override fun initState() {
        state = ParcelState.getInstance()
    }
}