package sk.zimen.semestralka.ui.storage_controllers

import javafx.application.Platform
import javafx.fxml.FXML
import javafx.scene.control.TextField
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import sk.zimen.semestralka.ui.util.allowOnlyInt
import sk.zimen.semestralka.ui.util.disable
import java.net.URL
import java.util.*

class GeneratorController : AbstractStorageController() {

    override val operationType: String = "generation"

    @FXML
    private lateinit var itemsCount: TextField

    @OptIn(DelicateCoroutinesApi::class)
    fun onGenerate() {
        val width = quadWidth.text.toDouble()
        val height = quadHeight.text.toDouble()
        val depth = treeDepth.text.toInt()
        val count = itemsCount.text.toInt()

        // Show loader on screen
        disableAll(true)

        //show loader on screen
        GlobalScope.launch {
            try {
                combinedService.changeParameters(depth, -width, height, width, -height)
                parcelService.generateData(count, depth, -width, height, width, -height)
                propertyService.generateData(count, depth, -width, height, width, -height)

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
        super.initialize(p0, p1)
        itemsCount.allowOnlyInt()
    }

    override fun disableForm(disable: Boolean) {
        super.disableForm(disable)
        itemsCount.disable(disable)
    }
}
