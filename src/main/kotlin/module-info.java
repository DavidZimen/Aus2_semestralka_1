module sk.zimen.semestralka {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires kotlin.stdlib;

    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires commons.math3;
    requires org.apache.commons.collections4;
    requires java.desktop;
    requires kotlin.reflect;
    requires kotlinx.coroutines.test;
    requires kotlinx.coroutines.core;
    requires kotlinx.coroutines.core.jvm;
    requires org.apache.commons.lang3;

    opens sk.zimen.semestralka.ui to javafx.fxml;
    opens sk.zimen.semestralka.ui.table_controllers to javafx.fxml;
    opens sk.zimen.semestralka.ui.add_edit_controllers to javafx.fxml;
    opens sk.zimen.semestralka.ui.storage_controllers to javafx.fxml;
    opens sk.zimen.semestralka.api.types to javafx.base, kotlin.reflect;
    opens sk.zimen.semestralka.quadtree.interfaces to kotlin.reflect;
    opens sk.zimen.semestralka.quadtree.node to kotlin.reflect;
    opens sk.zimen.semestralka.quadtree to kotlin.reflect;
    exports sk.zimen.semestralka;
}