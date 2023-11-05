package sk.zimen.semestralka.ui.storage_controllers

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import sk.zimen.semestralka.api.service.CombinedService
import sk.zimen.semestralka.api.service.ParcelService
import sk.zimen.semestralka.api.service.PropertyService
import sk.zimen.semestralka.ui.util.allowOnlyDouble
import sk.zimen.semestralka.ui.util.allowOnlyInt
import sk.zimen.semestralka.ui.util.disable
import sk.zimen.semestralka.ui.util.showSpinner
import java.net.URL
import java.util.*

abstract class AbstractStorageController : Initializable {

    protected abstract val operationType: String
    protected val parcelService = ParcelService.getInstance()

    protected val propertyService = PropertyService.getInstance()

    protected val combinedService = CombinedService.getInstance()

    // UI properties
    @FXML
    protected lateinit var quadWidth: TextField
    @FXML
    protected lateinit var quadHeight: TextField
    @FXML
    protected lateinit var treeDepth: TextField
    @FXML
    protected lateinit var spinnerLabel: Label
    @FXML
    protected lateinit var spinner: ProgressIndicator
    @FXML
    protected lateinit var button: Button

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        spinner.showSpinner(false, spinnerLabel)
        quadWidth.allowOnlyDouble()
        quadHeight.allowOnlyDouble()
        treeDepth.allowOnlyInt()
    }

    protected fun showSuccessAlert() {
        val alert = Alert(Alert.AlertType.NONE, "" , ButtonType.OK)
        alert.isResizable = false
        alert.headerText = "Parcels and properties $operationType was successful."
        alert.showAndWait()
    }

    protected fun showErrorAlert() {
        val alert = Alert(Alert.AlertType.ERROR, "" , ButtonType.OK)
        alert.isResizable = false
        alert.headerText = "Parcels and properties $operationType failed, please try again later."
        alert.showAndWait()
    }

    protected fun disableAll(disable: Boolean = true) {
        spinner.showSpinner(disable, spinnerLabel)
        button.disable(disable)
        disableForm(disable)
    }

    protected open fun disableForm(disable: Boolean = true) {
        quadWidth.disable(disable)
        quadHeight.disable(disable)
        treeDepth.disable(disable)
    }
}