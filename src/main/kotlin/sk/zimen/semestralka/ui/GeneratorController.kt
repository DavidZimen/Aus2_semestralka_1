package sk.zimen.semestralka.ui

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.*
import sk.zimen.semestralka.api.service.CombinedService
import sk.zimen.semestralka.api.service.ParcelService
import sk.zimen.semestralka.api.service.PropertyService
import java.net.URL
import java.util.*
import sk.zimen.semestralka.ui.util.allowOnlyDouble
import sk.zimen.semestralka.ui.util.allowOnlyInt

class GeneratorController : Initializable {

    private val parcelService = ParcelService.getInstance()
    private val propertyService = PropertyService.getInstance()
    private val combinedService = CombinedService.getInstance()
    @FXML
    private lateinit var quadWidth: TextField
    @FXML
    private lateinit var quadHeight: TextField
    @FXML
    private lateinit var treeDepth: TextField
    @FXML
    private lateinit var itemsCount: TextField
    @FXML
    private lateinit var spinnerLabel: Label
    @FXML
    private lateinit var spinner: ProgressIndicator

    fun onGenerate() {
        val width = quadWidth.text.toDouble()
        val height = quadHeight.text.toDouble()
        val depth = treeDepth.text.toInt()
        val count = itemsCount.text.toInt()

        //show loader on screen
        showSpinner(true)

        combinedService.changeParameters(depth, -width, height, width, -height)
        parcelService.generateData(count, depth, -width, height, width, -height)
        propertyService.generateData(count, depth, -width, height, width, -height)

        showSpinner(false)
        showSuccessAlert()
    }

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        showSpinner(false)
        quadWidth.allowOnlyDouble()
        quadHeight.allowOnlyDouble()
        treeDepth.allowOnlyInt()
        itemsCount.allowOnlyInt()
    }

    private fun showSpinner(show: Boolean) {
        spinner.isVisible = show
        spinnerLabel.isVisible = show
    }

    private fun showSuccessAlert() {
        val alert = Alert(Alert.AlertType.NONE, "Success", ButtonType.OK)
        alert.isResizable = false
        alert.headerText = "Parcels and properties generated successfully."
        alert.showAndWait()
    }
}
