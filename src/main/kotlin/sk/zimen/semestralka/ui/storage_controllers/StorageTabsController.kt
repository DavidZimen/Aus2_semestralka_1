package sk.zimen.semestralka.ui.storage_controllers

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Tab
import sk.zimen.semestralka.ui.util.setContent
import java.net.URL
import java.util.*

class StorageTabsController : Initializable {

    @FXML
    private lateinit var generatorTab: Tab
    @FXML
    private lateinit var loadTab: Tab

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        generatorTab.setContent("generator.fxml")
        loadTab.setContent("storage.fxml")
    }
}