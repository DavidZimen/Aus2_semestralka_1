package sk.zimen.semestralka.ui.table_controllers

import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.BorderPane
import sk.zimen.semestralka.Aus2Semestralka
import sk.zimen.semestralka.api.types.HeightPos
import sk.zimen.semestralka.api.types.Place
import sk.zimen.semestralka.api.types.WidthPos
import sk.zimen.semestralka.ui.state.AbstractState
import sk.zimen.semestralka.ui.util.allowOnlyDouble
import java.net.URL
import java.util.*

abstract class AbstractTableController<T : Place> : Initializable {

    protected var tableItems = FXCollections.observableArrayList<T>()
    protected lateinit var state: AbstractState<T>
    @FXML
    protected lateinit var borderPane: BorderPane
    @FXML
    protected var deleteButton: Button? = null
    @FXML
    protected var editButton: Button? = null
    @FXML
    protected lateinit var width: TextField
    @FXML
    protected lateinit var height: TextField
    @FXML
    protected lateinit var zPos: RadioButton
    @FXML
    protected lateinit var sPos: RadioButton
    @FXML
    protected lateinit var vPos: RadioButton
    @FXML
    protected lateinit var jPos: RadioButton
    @FXML
    protected lateinit var table: TableView<T>
    @FXML
    protected lateinit var bottomHeightPos: TableColumn<T, String>
    @FXML
    protected lateinit var bottomHeightValue: TableColumn<T, String>
    @FXML
    protected lateinit var number: TableColumn<T, Double>
    @FXML
    protected lateinit var desc: TableColumn<T, String>
    @FXML
    protected lateinit var topWidthValue: TableColumn<T, String>
    @FXML
    protected lateinit var topWidthPos: TableColumn<T, String>
    @FXML
    protected lateinit var topHeightValue: TableColumn<T, String>
    @FXML
    protected lateinit var topHeightPos: TableColumn<T, String>
    @FXML
    protected lateinit var bottomWidthValue: TableColumn<T, String>
    @FXML
    protected lateinit var bottomWidthPos: TableColumn<T, String>

    abstract fun search()

    abstract fun onEdit()

    abstract fun onAdd()

    abstract fun deleteFromService(item: T)

    open fun initState() {
        state.searchBar?.let {
            width.text = it.width.toString()
            zPos.isSelected = it.wPos == WidthPos.Z
            vPos.isSelected = it.wPos == WidthPos.V
            height.text = it.height.toString()
            sPos.isSelected = it.hPos == HeightPos.S
            jPos.isSelected = it.hPos == HeightPos.J
            search()
        }
    }

    fun onDelete() {
        val alert = Alert(Alert.AlertType.CONFIRMATION)
        alert.isResizable = false
        alert.title = "Delete"
        alert.headerText = "Selected item will be permanently deleted. Are you sure you want to continue ?"
        alert.showAndWait()
        if (alert.result == ButtonType.OK) {
            val delProperty = table.selectionModel.selectedItem
            deleteFromService(delProperty)
            table.items.remove(delProperty)
        } else {
            alert.close()
        }
    }

    fun newSearchState() {
        state.newSearch(
            width.text.toDouble(),
            if (zPos.isSelected) WidthPos.Z else WidthPos.V,
            height.text.toDouble(),
            if (sPos.isSelected) HeightPos.S else HeightPos.J
        )
    }

    override fun initialize(p0: URL?, p1: ResourceBundle?) {
        table.selectionModel.selectionMode = SelectionMode.SINGLE
        desc.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.description) }
        number.cellValueFactory = PropertyValueFactory("number")
        topWidthValue.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.key.topLeft.width.toString()) }
        topWidthPos.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.key.topLeft.widthPosition.toString()) }
        topHeightValue.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.key.topLeft.height.toString()) }
        topHeightPos.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.key.topLeft.heightPosition.toString()) }
        bottomWidthValue.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.key.bottomRight.width.toString()) }
        bottomWidthPos.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.key.bottomRight.widthPosition.toString()) }
        bottomHeightValue.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.key.bottomRight.height.toString()) }
        bottomHeightPos.setCellValueFactory { cellData -> SimpleStringProperty(cellData.value.key.bottomRight.heightPosition.toString()) }
        deleteButton?.isVisible = false
        editButton?.isVisible = false
        width.allowOnlyDouble()
        height.allowOnlyDouble()

        //initialize visibility of delete and edit button
        table.selectionModel.selectedItemProperty().addListener { _, _, newSelection ->
            deleteButton?.isVisible = newSelection != null
            editButton?.isVisible = newSelection != null
        }

        initState()
    }

    protected fun navigate(path: String) {
        borderPane.children.setAll(BorderPane(FXMLLoader(Aus2Semestralka::class.java.getResource(path)).load()))
    }
}