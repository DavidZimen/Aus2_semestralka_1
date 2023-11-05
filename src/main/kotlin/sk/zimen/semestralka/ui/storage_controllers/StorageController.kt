package sk.zimen.semestralka.ui.storage_controllers

import javafx.application.Platform
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class StorageController : AbstractStorageController() {

    override val operationType: String = "loading"

    fun onLoad() {
        val width = quadWidth.text.toDouble()
        val height = quadHeight.text.toDouble()
        val depth = treeDepth.text.toInt()

        disableAll(true)
        GlobalScope.launch {
            try {
                propertyService.changeParameters(depth, -width, height, width, -height)
                propertyService.loadFromFile()
                parcelService.changeParameters(depth, -width, height, width, -height)
                parcelService.loadFromFile()

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

}