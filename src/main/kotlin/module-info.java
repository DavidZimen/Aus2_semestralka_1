module sk.zimen.semestralka_1_kotlin {
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

    opens sk.zimen.semestralka_1_kotlin to javafx.fxml;
    exports sk.zimen.semestralka_1_kotlin;
}