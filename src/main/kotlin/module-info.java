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

    opens sk.zimen.semestralka to javafx.fxml;
    exports sk.zimen.semestralka;
}