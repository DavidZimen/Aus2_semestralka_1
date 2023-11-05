package sk.zimen.semestralka.ui.storage_controllers

import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.Tab
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import sk.zimen.semestralka.ui.util.disable
import sk.zimen.semestralka.ui.util.setContent
import sk.zimen.semestralka.ui.util.showSpinner
import java.net.URL
import java.util.*

class StorageTabsController : AbstractStorageController() {

    override val operationType: String = "saving"

    //UI elements
    @FXML
    private lateinit var generatorTab: Tab
    @FXML
    private lateinit var loadTab: Tab

    fun onSave() {
        disableAll(true)
        GlobalScope.launch {
            try {
                parcelService.saveToFile()
                propertyService.saveToFile()

                // Once the background tasks are done, update the UI on the JavaFX application thread
                Platform.runLater {
                    disableAll(false)
                    showSuccessAlert()
                }
            } catch (_: Exception) {
                // Update the UI to indicate an error
                Platform.runLater {
                    disableAll(false)
                    showErrorAlert()
                }
            }
        }
    }

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        generatorTab.setContent("generator.fxml")
        loadTab.setContent("storage.fxml")
        spinner.showSpinner(false, spinnerLabel)
    }

    override fun disableAll(disable: Boolean) {
        spinner.showSpinner(disable, spinnerLabel)
        button.disable(disable)
    }
}