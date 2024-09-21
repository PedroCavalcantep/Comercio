module fx.comercio {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.fasterxml.jackson.databind;

    opens fx.comercio to javafx.fxml;
    exports fx.comercio;
    exports fx.comercio.controllers;
    opens fx.comercio.controllers to javafx.fxml;
}