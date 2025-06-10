module com.ylo.ylo_gradebook_v1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires com.fasterxml.jackson.databind;
    requires java.sql;

    opens models to javafx.base;

    opens com.ylo.ylo_gradebook_v1 to javafx.fxml;
    exports com.ylo.ylo_gradebook_v1;
}